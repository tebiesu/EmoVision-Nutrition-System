package com.evns.controller;

import com.evns.auth.JwtService;
import com.evns.common.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Validated @RequestBody LoginRequest request) {
        String token = jwtService.generateToken(request.username());
        return ApiResponse.ok(Map.of(
                "token", token,
                "expireAt", Instant.now().plusSeconds(7200).toString()
        ));
    }

    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {
    }
}
