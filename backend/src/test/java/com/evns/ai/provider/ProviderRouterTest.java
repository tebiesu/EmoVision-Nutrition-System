package com.evns.ai.provider;

import com.evns.ai.provider.model.EmotionAnalyzeCommand;
import com.evns.ai.provider.model.EmotionResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class ProviderRouterTest {

    @Test
    void shouldFallbackWhenPrimaryTimeout() {
        EmotionProvider primary = new EmotionProvider() {
            @Override
            public String name() {
                return "newapi";
            }

            @Override
            public EmotionResult analyze(EmotionAnalyzeCommand command) {
                throw new ProviderException("TIMEOUT", "timeout");
            }
        };

        EmotionProvider secondary = new EmotionProvider() {
            @Override
            public String name() {
                return "siliconflow";
            }

            @Override
            public EmotionResult analyze(EmotionAnalyzeCommand command) {
                return new EmotionResult("calm", 0.88, "siliconflow");
            }
        };

        ProviderRouter router = new ProviderRouter(
                primary, secondary, 2, List.of(1L, 1L), null, millis -> {}
        );

        EmotionResult result = router.analyze(new EmotionAnalyzeCommand("", "压力很大", "workday"));
        Assertions.assertEquals("siliconflow", result.sourceProvider());
    }

    @Test
    void shouldNotRetryOn4xx() {
        AtomicInteger callCounter = new AtomicInteger(0);
        EmotionProvider primary = new EmotionProvider() {
            @Override
            public String name() {
                return "newapi";
            }

            @Override
            public EmotionResult analyze(EmotionAnalyzeCommand command) {
                callCounter.incrementAndGet();
                throw new ProviderException("4XX", "bad request");
            }
        };

        EmotionProvider secondary = new EmotionProvider() {
            @Override
            public String name() {
                return "siliconflow";
            }

            @Override
            public EmotionResult analyze(EmotionAnalyzeCommand command) {
                return new EmotionResult("tired", 0.9, "siliconflow");
            }
        };

        ProviderRouter router = new ProviderRouter(
                primary, secondary, 3, List.of(1L, 1L), null, millis -> {}
        );
        router.analyze(new EmotionAnalyzeCommand("", "有点焦虑", "night"));
        Assertions.assertEquals(1, callCounter.get());
    }

    @Test
    void shouldThrowWhenAllProviderFailed() {
        EmotionProvider failed = new EmotionProvider() {
            @Override
            public String name() {
                return "failed";
            }

            @Override
            public EmotionResult analyze(EmotionAnalyzeCommand command) {
                throw new ProviderException("5XX", "server error");
            }
        };

        ProviderRouter router = new ProviderRouter(
                failed, failed, 2, List.of(1L, 1L), null, millis -> {}
        );

        ProviderException ex = Assertions.assertThrows(
                ProviderException.class,
                () -> router.analyze(new EmotionAnalyzeCommand("", "", ""))
        );
        Assertions.assertEquals("P1002", ex.code());
    }
}
