<template>
  <section>
    <h2>Emotion</h2>
    <input data-testid="emotion-text" v-model="text" placeholder="input text" />
    <button data-testid="emotion-submit" @click="analyze">analyze</button>
    <pre data-testid="emotion-result">{{ result }}</pre>
  </section>
</template>

<script setup>
import { ref } from "vue";
import { apiClient } from "../api/client";

const text = ref("stress fallback");
const result = ref({});

const analyze = async () => {
  const mealId = Number(localStorage.getItem("lastMealId") || "0") || null;
  const r = await apiClient.post("/emotions/analyze", { mealId, text: text.value, sceneTag: "workday" });
  result.value = r.data.data;
  localStorage.setItem("lastEmotionId", String(r.data.data.emotionId));
};
</script>
