package com.evns.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStore {
    public record UserProfile(
            long id,
            String userName,
            Integer age,
            String goalType,
            List<String> dietTaboo,
            Instant updatedAt
    ) {
    }

    public record MealRecord(
            long id,
            long userId,
            String mealType,
            Instant eatenAt,
            String imageUrl,
            String note,
            double totalCalories,
            double proteinG,
            double fatG,
            double carbG
    ) {
    }

    public record EmotionRecord(
            long id,
            long userId,
            Long mealId,
            String emotionType,
            double confidence,
            String sourceProvider,
            String inputType,
            Instant analyzedAt
    ) {
    }

    public record RecommendationRecord(
            long id,
            long userId,
            Long mealId,
            Long emotionId,
            List<String> adviceList,
            List<String> riskFlags,
            String explainText,
            Instant createdAt
    ) {
    }

    private final AtomicLong idGenerator = new AtomicLong(1);
    private final Map<Long, UserProfile> profiles = new ConcurrentHashMap<>();
    private final Map<Long, MealRecord> meals = new ConcurrentHashMap<>();
    private final Map<Long, EmotionRecord> emotions = new ConcurrentHashMap<>();
    private final Map<Long, RecommendationRecord> recommendations = new ConcurrentHashMap<>();

    public InMemoryStore() {
        profiles.put(1L, new UserProfile(1L, "demo", 24, "maintain", List.of(), Instant.now()));
    }

    public long nextId() {
        return idGenerator.incrementAndGet();
    }

    public UserProfile getProfile(long userId) {
        return profiles.get(userId);
    }

    public UserProfile saveProfile(UserProfile profile) {
        profiles.put(profile.id(), profile);
        return profile;
    }

    public MealRecord saveMeal(MealRecord mealRecord) {
        meals.put(mealRecord.id(), mealRecord);
        return mealRecord;
    }

    public MealRecord getMeal(long mealId) {
        return meals.get(mealId);
    }

    public List<MealRecord> listMealsByUser(long userId) {
        return meals.values().stream().filter(m -> m.userId() == userId).toList();
    }

    public EmotionRecord saveEmotion(EmotionRecord emotionRecord) {
        emotions.put(emotionRecord.id(), emotionRecord);
        return emotionRecord;
    }

    public EmotionRecord getEmotion(long id) {
        return emotions.get(id);
    }

    public List<EmotionRecord> listEmotionsByUser(long userId) {
        return emotions.values().stream().filter(e -> e.userId() == userId).toList();
    }

    public RecommendationRecord saveRecommendation(RecommendationRecord recommendationRecord) {
        recommendations.put(recommendationRecord.id(), recommendationRecord);
        return recommendationRecord;
    }

    public List<RecommendationRecord> listRecommendationsByUser(long userId) {
        return new ArrayList<>(recommendations.values().stream().filter(r -> r.userId() == userId).toList());
    }
}
