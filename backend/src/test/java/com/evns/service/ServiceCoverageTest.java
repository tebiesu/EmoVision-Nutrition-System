package com.evns.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

class ServiceCoverageTest {
    @Test
    void shouldCoverStoreMethods() {
        InMemoryStore store = new InMemoryStore();
        InMemoryStore.UserProfile profile = new InMemoryStore.UserProfile(1L, "u", 20, "maintain", List.of("pepper"), Instant.now());
        store.saveProfile(profile);
        Assertions.assertNotNull(store.getProfile(1L));

        long mealId = store.nextId();
        store.saveMeal(new InMemoryStore.MealRecord(mealId, 1L, "dinner", Instant.now(), "", "", 500, 20, 10, 60));
        Assertions.assertEquals(1, store.listMealsByUser(1L).size());
        Assertions.assertNotNull(store.getMeal(mealId));

        long emotionId = store.nextId();
        store.saveEmotion(new InMemoryStore.EmotionRecord(emotionId, 1L, mealId, "stress", 0.9, "newapi", "text", Instant.now()));
        Assertions.assertEquals(1, store.listEmotionsByUser(1L).size());
        Assertions.assertNotNull(store.getEmotion(emotionId));

        long recommendationId = store.nextId();
        store.saveRecommendation(new InMemoryStore.RecommendationRecord(
                recommendationId, 1L, mealId, emotionId, List.of("a"), List.of("r"), "e", Instant.now()
        ));
        Assertions.assertEquals(1, store.listRecommendationsByUser(1L).size());

        store.saveProviderLog("newapi", 1, true, null);
        Assertions.assertEquals(1, store.listProviderLogs().size());
    }

    @Test
    void shouldCoverRecommendationRules() {
        RecommendationService service = new RecommendationService();
        RecommendationService.RecommendationResult r1 = service.generate("stress", "maintain", 50, 0.9);
        Assertions.assertFalse(r1.adviceList().isEmpty());
        Assertions.assertTrue(r1.explainText().contains("R001"));

        RecommendationService.RecommendationResult r2 = service.generate("calm", "lose_fat", 100, 0.2);
        Assertions.assertTrue(r2.explainText().contains("R002"));

        RecommendationService.RecommendationResult r3 = service.generate("tired", "maintain", 10, 0.1);
        Assertions.assertTrue(r3.explainText().contains("R003"));

        RecommendationService.RecommendationResult r4 = service.generate("happy", "maintain", 10, 0.1);
        Assertions.assertTrue(r4.explainText().contains("R000"));
    }
}
