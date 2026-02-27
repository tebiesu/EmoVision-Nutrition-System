package com.evns.ai.provider.model;

public record EmotionAnalyzeCommand(
        String imageUrl,
        String text,
        String sceneTag
) {}
