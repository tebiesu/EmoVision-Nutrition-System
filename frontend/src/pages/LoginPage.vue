<template>
  <section class="card">
    <h2>Login</h2>
    <input data-testid="login-username" v-model="username" placeholder="username" />
    <input data-testid="login-password" v-model="password" type="password" placeholder="password" />
    <button data-testid="login-submit" @click="onLogin">login</button>
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
