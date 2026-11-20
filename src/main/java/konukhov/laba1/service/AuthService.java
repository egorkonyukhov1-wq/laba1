package konukhov.laba1.service;

import konukhov.laba1.dto.LoginRequest;
import konukhov.laba1.dto.RegisterRequest;
import konukhov.laba1.exception.DuplicateRegistrationException;
import konukhov.laba1.exception.InvalidCredentialsException;
import konukhov.laba1.model.RefreshResult;
import konukhov.laba1.model.Role;
import konukhov.laba1.model.TokenPair;
import konukhov.laba1.model.User;
import konukhov.laba1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenPairService tokenPairService;

    public TokenPair login(LoginRequest req) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException();
        }
        User user = userRepository.findByEmail(req.email()).orElseThrow(InvalidCredentialsException::new);
        return tokenPairService.issuePair(user, req.deviceId());
    }

    @Transactional
    public TokenPair register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new DuplicateRegistrationException();
        }
        User user = new User();
        user.setEmail(req.email());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setRole(Role.USER);
        user.setEnabled(true);
        user = userRepository.saveAndFlush(user);

        return tokenPairService.issuePair(user, req.deviceId());
    }

    public RefreshResult refresh(String refreshToken) {
        return tokenPairService.refresh(refreshToken);
    }
}