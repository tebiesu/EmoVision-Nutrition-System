package com.evns.ai.provider;

import com.evns.ai.provider.model.FoodDetectResult;

public interface VisionProvider {
    String name();
    FoodDetectResult detectFoods(String imageUrl);
}
