package com.evns.controller;

import com.evns.common.ApiResponse;
import com.evns.service.InMemoryStore;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/provider-logs")
public class ProviderLogController {
    private final InMemoryStore store;

    public ProviderLogController(InMemoryStore store) {
        this.store = store;
    }

    @GetMapping
    public ApiResponse<List<InMemoryStore.ProviderCallLog>> list(Authentication authentication) {
        return ApiResponse.ok(store.listProviderLogs());
    }
}
