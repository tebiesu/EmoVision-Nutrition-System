<template>
  <section>
    <h2>餐食记录</h2>
    <input v-model="mealType" placeholder="mealType" />
    <input v-model.number="calories" type="number" placeholder="totalCalories" />
    <button @click="createMeal">提交</button>
    <ul>
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
  await apiClient.post("/meals", {
    mealType: mealType.value,
    totalCalories: calories.value,
    proteinG: 25,
    fatG: 10,
    carbG: 50
  });
  await loadMeals();
};

onMounted(loadMeals);
</script>
