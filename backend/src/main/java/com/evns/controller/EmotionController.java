package com.evns.controller;

import com.evns.ai.provider.ProviderRouter;
import com.evns.ai.provider.model.EmotionAnalyzeCommand;
import com.evns.ai.provider.model.EmotionResult;
import com.evns.common.ApiResponse;
import com.evns.service.InMemoryStore;
import com.evns.service.ProviderRouterFactory;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/emotions")
public class EmotionController {
    private final ProviderRouterFactory providerRouterFactory;
    private final InMemoryStore store;

    public EmotionController(ProviderRouterFactory providerRouterFactory, InMemoryStore store) {
        this.providerRouterFactory = providerRouterFactory;
        this.store = store;
    }

    @PostMapping("/analyze")
    public ApiResponse<Map<String, Object>> analyze(
            Authentication authentication,
            @Validated @RequestBody AnalyzeRequest request
    ) {
        ProviderRouter router = providerRouterFactory.emotionRouter();
        EmotionResult result = router.analyze(new EmotionAnalyzeCommand(request.imageUrl(), request.text(), request.sceneTag()));
        long emotionId = store.nextId();
        InMemoryStore.EmotionRecord record = new InMemoryStore.EmotionRecord(
                emotionId,
                1L,
                request.mealId(),
                result.emotionType(),
                result.confidence(),
                result.sourceProvider(),
                request.imageUrl() != null ? "mix" : "text",
                Instant.now()
        );
        store.saveEmotion(record);
        return ApiResponse.ok(Map.of(
                "emotionId", emotionId,
                "emotionType", result.emotionType(),
                "confidence", result.confidence(),
                "sourceProvider", result.sourceProvider()
        ));
    }

    public record AnalyzeRequest(
            Long mealId,
            String imageUrl,
            @NotBlank String text,
            String sceneTag
    ) {
    }
}
