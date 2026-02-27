<template>
  <section>
    <h2>Recommendation</h2>
    <input data-testid="recommendation-meal-id" v-model.number="mealId" type="number" placeholder="mealId" />
    <input data-testid="recommendation-emotion-id" v-model.number="emotionId" type="number" placeholder="emotionId" />
    <button data-testid="recommendation-submit" @click="generate">generate</button>
    <pre data-testid="recommendation-result">{{ result }}</pre>
  </section>
</template>

<script setup>
import { ref } from "vue";
import { apiClient } from "../api/client";

const mealId = ref(Number(localStorage.getItem("lastMealId") || "1"));
const emotionId = ref(Number(localStorage.getItem("lastEmotionId") || "1"));
const result = ref({});

const generate = async () => {
  const r = await apiClient.post("/recommendations/generate", {
    mealId: mealId.value,
    emotionId: emotionId.value
  });
  result.value = r.data.data;
};
</script>
