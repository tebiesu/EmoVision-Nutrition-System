<template>
  <section>
    <h2>情感分析</h2>
    <input v-model="text" placeholder="输入文本" />
    <button @click="analyze">分析</button>
    <pre>{{ result }}</pre>
  </section>
</template>

<script setup>
import { ref } from "vue";
import { apiClient } from "../api/client";

const text = ref("今天压力大 fallback");
const result = ref({});

const analyze = async () => {
  const r = await apiClient.post("/emotions/analyze", { text: text.value, sceneTag: "workday" });
  result.value = r.data.data;
};
</script>
