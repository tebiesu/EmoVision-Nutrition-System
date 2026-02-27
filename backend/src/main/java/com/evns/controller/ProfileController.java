package com.evns.controller;

import com.evns.common.ApiResponse;
import com.evns.service.InMemoryStore;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final InMemoryStore store;

    public ProfileController(InMemoryStore store) {
        this.store = store;
    }

    @GetMapping
    public ApiResponse<InMemoryStore.UserProfile> getProfile(Authentication authentication) {
        long userId = 1L;
        return ApiResponse.ok(store.getProfile(userId));
    }

    @PutMapping
    public ApiResponse<InMemoryStore.UserProfile> updateProfile(
            Authentication authentication,
            @Validated @RequestBody UpdateProfileRequest request
    ) {
        long userId = 1L;
        InMemoryStore.UserProfile updated = new InMemoryStore.UserProfile(
                userId,
                request.userName(),
                request.age(),
                request.goalType(),
                request.dietTaboo(),
                Instant.now()
        );
        store.saveProfile(updated);
        return ApiResponse.ok(updated);
    }

    public record UpdateProfileRequest(
            @NotBlank String userName,
            Integer age,
            @NotBlank String goalType,
            List<String> dietTaboo
    ) {
    }
}
