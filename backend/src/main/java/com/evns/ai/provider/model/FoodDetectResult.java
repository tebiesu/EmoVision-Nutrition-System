package com.evns.ai.provider.model;

import java.util.List;

public record FoodDetectResult(
        List<FoodItem> items
) {
    public record FoodItem(
            String foodName,
            double estimatedWeightG,
            double confidence
    ) {}
}
