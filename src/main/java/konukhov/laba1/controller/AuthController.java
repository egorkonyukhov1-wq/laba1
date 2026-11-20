package konukhov.laba1.controller;

import jakarta.validation.Valid;
import konukhov.laba1.dto.LoginRequest;
import konukhov.laba1.dto.RefreshRequest;
import konukhov.laba1.dto.RegisterRequest;
import konukhov.laba1.dto.TokenPairResponse;
import konukhov.laba1.exception.InvalidRefreshException;
import konukhov.laba1.model.RefreshResult;
import konukhov.laba1.model.TokenPair;
import konukhov.laba1.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenPairResponse> login(@Valid @RequestBody LoginRequest req) {
        TokenPair pair = authService.login(req);
        return ResponseEntity.ok(new TokenPairResponse(pair.accessToken(), pair.refreshToken()));
    }

    @PostMapping("/register")
    public ResponseEntity<TokenPairResponse> register(@Valid @RequestBody RegisterRequest req) {
        TokenPair pair = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TokenPairResponse(pair.accessToken(), pair.refreshToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenPairResponse> refresh(@Valid @RequestBody RefreshRequest req) {
        RefreshResult result = authService.refresh(req.refreshToken());
        if (!result.isSuccess()) {
            throw new InvalidRefreshException(result.failure().name());
        }
        TokenPair pair = result.pair();
        return ResponseEntity.ok(new TokenPairResponse(pair.accessToken(), pair.refreshToken()));
    }
}