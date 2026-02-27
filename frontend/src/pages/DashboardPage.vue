<template>
  <section>
    <h2 data-testid="dashboard-title">Dashboard</h2>
    <p data-testid="dashboard-meal-count">mealCount: {{ overview.mealCount }}</p>
    <p>emotionCount: {{ overview.emotionCount }}</p>
    <p>totalCalories: {{ overview.totalCalories }}</p>
    <ul data-testid="dashboard-trend">
      <li v-for="item in trend" :key="item.date">{{ item.date }} / {{ item.calories }}</li>
    </ul>
  </section>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { apiClient } from "../api/client";

const overview = ref({ mealCount: 0, emotionCount: 0, totalCalories: 0 });
const trend = ref([]);

onMounted(async () => {
  const o = await apiClient.get("/dashboard/overview");
  overview.value = o.data.data;
  const t = await apiClient.get("/dashboard/trend?days=7");
  trend.value = t.data.data;
});
</script>
