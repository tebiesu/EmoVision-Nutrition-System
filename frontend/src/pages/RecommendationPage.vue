<template>
  <section>
    <h2>建议中心</h2>
    <input v-model.number="mealId" type="number" placeholder="mealId" />
    <input v-model.number="emotionId" type="number" placeholder="emotionId" />
    <button @click="generate">生成建议</button>
    <pre>{{ result }}</pre>
  </section>
</template>

<script setup>
import { ref } from "vue";
import { apiClient } from "../api/client";

const mealId = ref(2);
const emotionId = ref(3);
const result = ref({});

const generate = async () => {
  const r = await apiClient.post("/recommendations/generate", {
    mealId: mealId.value,
    emotionId: emotionId.value
  });
  result.value = r.data.data;
};
</script>
