package konukhov.laba1.model;

import java.time.Instant;

public record IssuedToken(String token, Instant expiresAt) {}