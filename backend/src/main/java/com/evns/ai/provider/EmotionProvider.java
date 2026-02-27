package com.evns.ai.provider;

import com.evns.ai.provider.model.EmotionAnalyzeCommand;
import com.evns.ai.provider.model.EmotionResult;

public interface EmotionProvider {
    String name();
    EmotionResult analyze(EmotionAnalyzeCommand command);
}
