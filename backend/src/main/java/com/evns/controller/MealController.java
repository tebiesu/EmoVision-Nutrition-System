package com.evns.controller;

import com.evns.common.ApiResponse;
import com.evns.service.InMemoryStore;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/meals")
public class MealController {
    private final InMemoryStore store;

    public MealController(InMemoryStore store) {
        this.store = store;
    }

    @PostMapping
    public ApiResponse<InMemoryStore.MealRecord> createMeal(
            Authentication authentication,
            @Validated @RequestBody CreateMealRequest request
    ) {
        long id = store.nextId();
        InMemoryStore.MealRecord meal = new InMemoryStore.MealRecord(
                id,
                1L,
                request.mealType(),
                request.eatenAt() == null ? Instant.now() : request.eatenAt(),
                request.imageUrl(),
                request.note(),
                request.totalCalories(),
                request.proteinG(),
                request.fatG(),
                request.carbG()
        );
        store.saveMeal(meal);
        return ApiResponse.ok(meal);
    }

    @GetMapping
    public ApiResponse<List<InMemoryStore.MealRecord>> listMeals(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<InMemoryStore.MealRecord> meals = store.listMealsByUser(1L);
        return ApiResponse.ok(meals.stream().skip((long) (page - 1) * size).limit(size).toList());
    }

    @GetMapping("/{id}")
    public ApiResponse<InMemoryStore.MealRecord> getMeal(Authentication authentication, @PathVariable long id) {
        return ApiResponse.ok(store.getMeal(id));
    }

    public record CreateMealRequest(
            @NotBlank String mealType,
            Instant eatenAt,
            String imageUrl,
            String note,
            double totalCalories,
            double proteinG,
            double fatG,
            double carbG
    ) {
    }
}
