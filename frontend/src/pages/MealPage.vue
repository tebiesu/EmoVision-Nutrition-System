<template>
  <section>
    <h2>Meal</h2>
    <input data-testid="meal-type" v-model="mealType" placeholder="mealType" />
    <input data-testid="meal-calories" v-model.number="calories" type="number" placeholder="totalCalories" />
    <button data-testid="meal-submit" @click="createMeal">submit meal</button>
    <ul data-testid="meal-list">
      <li v-for="m in meals" :key="m.id">#{{ m.id }} {{ m.mealType }} / {{ m.totalCalories }}</li>
    </ul>
  </section>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { apiClient } from "../api/client";

const mealType = ref("dinner");
const calories = ref(500);
const meals = ref([]);

const loadMeals = async () => {
  const r = await apiClient.get("/meals?page=1&size=20");
  meals.value = r.data.data;
};

const createMeal = async () => {
  const r = await apiClient.post("/meals", {
    mealType: mealType.value,
    totalCalories: calories.value,
    proteinG: 25,
    fatG: 10,
    carbG: 50
  });
  localStorage.setItem("lastMealId", String(r.data.data.id));
  await loadMeals();
};

onMounted(loadMeals);
</script>
