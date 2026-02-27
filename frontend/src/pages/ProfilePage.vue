<template>
  <section>
    <h2>用户档案</h2>
    <input v-model="form.userName" placeholder="用户名" />
    <input v-model.number="form.age" type="number" placeholder="年龄" />
    <input v-model="form.goalType" placeholder="目标" />
    <button @click="save">保存</button>
    <pre>{{ profile }}</pre>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { apiClient } from "../api/client";

const profile = ref({});
const form = reactive({
  userName: "demo",
  age: 24,
  goalType: "maintain",
  dietTaboo: []
});

const load = async () => {
  const r = await apiClient.get("/profile");
  profile.value = r.data.data;
};

const save = async () => {
  await apiClient.put("/profile", form);
  await load();
};

onMounted(load);
</script>
