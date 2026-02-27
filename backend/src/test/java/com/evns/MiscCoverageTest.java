package com.evns;

import com.evns.ai.provider.model.EmotionAnalyzeCommand;
import com.evns.ai.provider.model.EmotionResult;
import com.evns.ai.provider.model.FoodDetectResult;
import com.evns.common.ApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class MiscCoverageTest {
    @Test
    void shouldCoverRecordModels() {
        EmotionAnalyzeCommand cmd = new EmotionAnalyzeCommand("img", "txt", "scene");
        Assertions.assertEquals("img", cmd.imageUrl());

        EmotionResult result = new EmotionResult("calm", 0.9, "newapi");
        Assertions.assertEquals("calm", result.emotionType());

        FoodDetectResult detectResult = new FoodDetectResult(List.of(new FoodDetectResult.FoodItem("rice", 100, 0.8)));
        Assertions.assertEquals(1, detectResult.items().size());

        ApiResponse<String> ok = ApiResponse.ok("x");
        Assertions.assertEquals(0, ok.code());
        ApiResponse<Object> err = ApiResponse.error(400, "bad");
        Assertions.assertEquals(400, err.code());
    }

}
