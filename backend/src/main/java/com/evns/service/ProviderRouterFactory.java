package com.evns.service;

import com.evns.ai.provider.EmotionProvider;
import com.evns.ai.provider.ProviderException;
import com.evns.ai.provider.ProviderRouter;
import com.evns.ai.provider.model.EmotionAnalyzeCommand;
import com.evns.ai.provider.model.EmotionResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProviderRouterFactory {
    public ProviderRouter emotionRouter() {
        EmotionProvider primary = new EmotionProvider() {
            @Override
            public String name() {
                return "newapi";
            }

            @Override
            public EmotionResult analyze(EmotionAnalyzeCommand command) {
                if (command.text() != null && command.text().contains("fallback")) {
                    throw new ProviderException("TIMEOUT", "simulate timeout");
                }
                return new EmotionResult("calm", 0.92, name());
            }
        };

        EmotionProvider secondary = new EmotionProvider() {
            @Override
            public String name() {
                return "siliconflow";
            }

            @Override
            public EmotionResult analyze(EmotionAnalyzeCommand command) {
                return new EmotionResult("tired", 0.88, name());
            }
        };

        return new ProviderRouter(primary, secondary, 2, List.of(300L, 800L), null, millis -> {});
    }
}
