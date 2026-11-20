package konukhov.laba1.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import konukhov.laba1.model.*;
import konukhov.laba1.repository.UserRepository;
import konukhov.laba1.repository.UserSessionRepository;
import konukhov.laba1.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenPairService {

    private final UserSessionRepository sessionRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Transactional
    public TokenPair issuePair(User user, String deviceId) {
        List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        IssuedToken access = jwtTokenProvider.createAccessToken(user.getUsername(), authorities);
        IssuedToken refresh = jwtTokenProvider.createRefreshToken(user.getUsername());

        UserSession session = new UserSession();
        session.setUsername(user.getUsername());
        session.setDeviceId(deviceId);
        session.setAccessToken(access.token());
        session.setRefreshToken(refresh.token());
        session.setAccessTokenExpiry(access.expiresAt());
        session.setRefreshTokenExpiry(refresh.expiresAt());
        session.setStatus(SessionStatus.ACTIVE);
        sessionRepository.saveAndFlush(session);

        return new TokenPair(access.token(), refresh.token());
    }

    @Transactional
    public RefreshResult refresh(String refreshJwt) {
        Claims claims;
        try {
            claims = jwtTokenProvider.validateAndGet(refreshJwt, TokenType.REFRESH);
        } catch (JwtException | IllegalArgumentException e) {
            return RefreshResult.failure(RefreshFailure.INVALID_TOKEN);
        }

        Optional<UserSession> opt = sessionRepository.findByRefreshTokenForUpdate(refreshJwt);
        if (opt.isEmpty()) return RefreshResult.failure(RefreshFailure.SESSION_NOT_FOUND);
        UserSession session = opt.get();

        switch (session.getStatus()) {
            case REVOKED -> { return RefreshResult.failure(RefreshFailure.TOKEN_REVOKED); }
            case USED -> {
                if (session.getRefreshTokenExpiry().isAfter(Instant.now())) {
                    session.setStatus(SessionStatus.REVOKED);
                }
                return RefreshResult.failure(RefreshFailure.TOKEN_REUSED);
            }
            case ACTIVE -> { /* continue */ }
        }

        if (session.getRefreshTokenExpiry().isBefore(Instant.now())) return RefreshResult.failure(RefreshFailure.EXPIRED);
        if (!Objects.equals(claims.getSubject(), session.getUsername())) return RefreshResult.failure(RefreshFailure.SUBJECT_MISMATCH);

        User user = userRepository.findByEmail(session.getUsername()).orElse(null);
        if (user == null || !user.isEnabled()) return RefreshResult.failure(RefreshFailure.USER_NOT_AVAILABLE);

        session.setStatus(SessionStatus.USED);

        List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        IssuedToken newAccess = jwtTokenProvider.createAccessToken(user.getUsername(), authorities);
        IssuedToken newRefresh = jwtTokenProvider.createRefreshToken(user.getUsername());

        UserSession newSession = new UserSession();
        newSession.setUsername(user.getUsername());
        newSession.setDeviceId(session.getDeviceId());
        newSession.setAccessToken(newAccess.token());
        newSession.setRefreshToken(newRefresh.token());
        newSession.setAccessTokenExpiry(newAccess.expiresAt());
        newSession.setRefreshTokenExpiry(newRefresh.expiresAt());
        newSession.setStatus(SessionStatus.ACTIVE);

        sessionRepository.saveAndFlush(newSession);

        return RefreshResult.success(new TokenPair(newAccess.token(), newRefresh.token()));
    }
}