package com.evns.ai.provider;

import com.evns.ai.provider.model.EmotionAnalyzeCommand;
import com.evns.ai.provider.model.EmotionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProviderRouter {
    private final EmotionProvider primary;
    private final EmotionProvider secondary;
    private final int maxAttempts;
    private final List<Long> backoffMillis;
    private final AttemptLogger logger;
    private final Sleeper sleeper;

    public ProviderRouter(
            EmotionProvider primary,
            EmotionProvider secondary,
            int maxAttempts,
            List<Long> backoffMillis,
            AttemptLogger logger,
            Sleeper sleeper
    ) {
        this.primary = Objects.requireNonNull(primary);
        this.secondary = Objects.requireNonNull(secondary);
        this.maxAttempts = Math.max(1, maxAttempts);
        this.backoffMillis = backoffMillis == null ? List.of() : new ArrayList<>(backoffMillis);
        this.logger = logger == null ? (p, a, s, e) -> {} : logger;
        this.sleeper = sleeper == null ? Thread::sleep : sleeper;
    }

    public EmotionResult analyze(EmotionAnalyzeCommand command) {
        ProviderException last = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                EmotionResult result = primary.analyze(command);
                logger.log(primary.name(), attempt, true, null);
                return result;
            } catch (ProviderException ex) {
                logger.log(primary.name(), attempt, false, ex.getMessage());
                last = ex;
                if (!isRetryable(ex) || attempt == maxAttempts) {
                    break;
                }
                sleepBackoff(attempt);
            }
        }

        try {
            EmotionResult fallback = secondary.analyze(command);
            logger.log(secondary.name(), 1, true, null);
            return fallback;
        } catch (ProviderException ex) {
            logger.log(secondary.name(), 1, false, ex.getMessage());
            throw new ProviderException("P1002", "all providers failed", last == null ? ex : last);
        }
    }

    private boolean isRetryable(ProviderException ex) {
        return "TIMEOUT".equals(ex.code()) || "NETWORK".equals(ex.code()) || "5XX".equals(ex.code());
    }

    private void sleepBackoff(int attempt) {
        int idx = attempt - 1;
        if (idx >= 0 && idx < backoffMillis.size()) {
            try {
                sleeper.sleep(backoffMillis.get(idx));
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @FunctionalInterface
    public interface AttemptLogger {
        void log(String provider, int attempt, boolean success, String errorMessage);
    }

    @FunctionalInterface
    public interface Sleeper {
        void sleep(long millis) throws InterruptedException;
    }
}
