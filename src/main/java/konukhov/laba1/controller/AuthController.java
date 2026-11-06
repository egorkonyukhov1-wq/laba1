package konukhov.laba1.controller;

import konukhov.laba1.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> request) {
        userService.registerUser(request.get("name"), request.get("email"), request.get("password"));
        return ResponseEntity.ok("User registered successfully");
    }
}