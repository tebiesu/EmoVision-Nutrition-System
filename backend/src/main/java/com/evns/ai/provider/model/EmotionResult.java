package com.evns.ai.provider.model;

public record EmotionResult(
        String emotionType,
        double confidence,
        String sourceProvider
) {}
