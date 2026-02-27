package com.evns.controller;

import com.evns.common.ApiResponse;
import com.evns.service.InMemoryStore;
import com.evns.service.RecommendationService;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {
    private final InMemoryStore store;
    private final RecommendationService recommendationService;

    public RecommendationController(InMemoryStore store, RecommendationService recommendationService) {
        this.store = store;
        this.recommendationService = recommendationService;
    }

    @PostMapping("/generate")
    public ApiResponse<Map<String, Object>> generate(
            Authentication authentication,
            @Validated @RequestBody GenerateRequest request
    ) {
        InMemoryStore.MealRecord meal = store.getMeal(request.mealId());
        InMemoryStore.EmotionRecord emotion = store.getEmotion(request.emotionId());
        InMemoryStore.UserProfile profile = store.getProfile(1L);

        RecommendationService.RecommendationResult result = recommendationService.generate(
                emotion == null ? "calm" : emotion.emotionType(),
                profile == null ? "maintain" : profile.goalType(),
                meal == null ? 0 : meal.carbG(),
                meal == null ? 0 : Math.min(1, meal.totalCalories() / 1200.0)
        );

        long recommendationId = store.nextId();
        store.saveRecommendation(new InMemoryStore.RecommendationRecord(
                recommendationId,
                1L,
                request.mealId(),
                request.emotionId(),
                result.adviceList(),
                result.riskFlags(),
                result.explainText(),
                Instant.now()
        ));

        return ApiResponse.ok(Map.of(
                "recommendationId", recommendationId,
                "adviceList", result.adviceList(),
                "riskFlags", result.riskFlags(),
                "explainText", result.explainText()
        ));
    }

    public record GenerateRequest(
            @NotNull Long mealId,
            @NotNull Long emotionId
    ) {
    }
}
