<template>
  <section class="card">
    <h2>登录</h2>
    <input v-model="username" placeholder="用户名" />
    <input v-model="password" type="password" placeholder="密码" />
    <button @click="onLogin">登录</button>
  </section>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "../stores/auth";

const router = useRouter();
const auth = useAuthStore();
const username = ref("demo");
const password = ref("demo");

const onLogin = async () => {
  await auth.login(username.value, password.value);
  await router.push("/dashboard");
};
</script>

<style scoped>
.card {
  max-width: 360px;
  margin: 60px auto;
  display: grid;
  gap: 12px;
}
input, button {
  padding: 10px;
}
</style>
