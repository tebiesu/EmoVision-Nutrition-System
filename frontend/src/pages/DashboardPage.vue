<template>
  <section>
    <h2>看板</h2>
    <p>餐食数：{{ overview.mealCount }}</p>
    <p>情感数：{{ overview.emotionCount }}</p>
    <p>总热量：{{ overview.totalCalories }}</p>
    <ul>
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
