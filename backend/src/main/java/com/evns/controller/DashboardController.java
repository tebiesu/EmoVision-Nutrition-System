package com.evns.controller;

import com.evns.common.ApiResponse;
import com.evns.service.InMemoryStore;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final InMemoryStore store;

    public DashboardController(InMemoryStore store) {
        this.store = store;
    }

    @GetMapping("/overview")
    public ApiResponse<Map<String, Object>> overview(Authentication authentication) {
        List<InMemoryStore.MealRecord> meals = store.listMealsByUser(1L);
        List<InMemoryStore.EmotionRecord> emotions = store.listEmotionsByUser(1L);
        double calories = meals.stream().mapToDouble(InMemoryStore.MealRecord::totalCalories).sum();
        return ApiResponse.ok(Map.of(
                "mealCount", meals.size(),
                "emotionCount", emotions.size(),
                "totalCalories", calories
        ));
    }

    @GetMapping("/trend")
    public ApiResponse<List<Map<String, Object>>> trend(
            Authentication authentication,
            @RequestParam(defaultValue = "7") int days
    ) {
        Map<LocalDate, Double> caloriesByDay = new HashMap<>();
        for (InMemoryStore.MealRecord meal : store.listMealsByUser(1L)) {
            LocalDate date = meal.eatenAt().atZone(ZoneOffset.UTC).toLocalDate();
            caloriesByDay.merge(date, meal.totalCalories(), Double::sum);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now(ZoneOffset.UTC).minusDays(i);
            result.add(Map.of(
                    "date", date.toString(),
                    "calories", caloriesByDay.getOrDefault(date, 0.0)
            ));
        }
        return ApiResponse.ok(result);
    }
}
