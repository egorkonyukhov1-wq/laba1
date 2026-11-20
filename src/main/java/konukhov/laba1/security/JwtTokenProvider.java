package konukhov.laba1.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import konukhov.laba1.model.IssuedToken;
import konukhov.laba1.model.TokenType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.accessExpiration}") private long accessExpirationMs;
    @Value("${jwt.refreshExpiration}") private long refreshExpirationMs;

    private SecretKey signingKey;
    private Duration accessTtl;
    private Duration refreshTtl;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    void init() {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessTtl = Duration.ofMillis(accessExpirationMs);
        this.refreshTtl = Duration.ofMillis(refreshExpirationMs);
    }

    public IssuedToken createAccessToken(String username, Collection<String> authorities) {
        return create(username, authorities, TokenType.ACCESS, accessTtl);
    }

    public IssuedToken createRefreshToken(String username) {
        return create(username, List.of(), TokenType.REFRESH, refreshTtl);
    }

    private IssuedToken create(String username, Collection<String> authorities, TokenType type, Duration ttl) {
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiry = now.plus(ttl);

        JwtBuilder b = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .claim("type", type.name().toLowerCase(Locale.ROOT))
                .signWith(signingKey, Jwts.SIG.HS256);

        if (type == TokenType.ACCESS && !authorities.isEmpty()) {
            b.claim("auth", new ArrayList<>(authorities));
        }
        return new IssuedToken(b.compact(), expiry);
    }

    public Claims validateAndGet(String token, TokenType expectedType) {
        Claims claims = Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();

        if (claims.getSubject() == null || claims.getSubject().isBlank()) throw new JwtException("Missing sub");
        if (claims.getId() == null || claims.getId().isBlank()) throw new JwtException("Missing jti");
        if (claims.getExpiration() == null) throw new JwtException("Missing exp");

        String type = claims.get("type", String.class);
        if (type == null || !type.equalsIgnoreCase(expectedType.name())) {
            throw new JwtException("Unexpected token type: " + type);
        }
        return claims;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = validateAndGet(token, TokenType.ACCESS);
        UserDetails user = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
}