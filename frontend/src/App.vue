<template>
  <div class="app-shell">
    <aside class="menu" v-if="auth.token">
      <router-link data-testid="nav-dashboard" to="/dashboard">dashboard</router-link>
      <router-link data-testid="nav-meal" to="/meal">meal</router-link>
      <router-link data-testid="nav-emotion" to="/emotion">emotion</router-link>
      <router-link data-testid="nav-recommendation" to="/recommendation">recommendation</router-link>
      <router-link data-testid="nav-profile" to="/profile">profile</router-link>
    </aside>
    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useAuthStore } from "./stores/auth";

const auth = useAuthStore();
</script>

<style scoped>
.app-shell {
  display: grid;
  grid-template-columns: 220px 1fr;
  min-height: 100vh;
  background: linear-gradient(135deg, #f6fbff 0%, #eef5f3 100%);
}
.menu {
  padding: 20px;
  border-right: 1px solid #dce7e3;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.menu a {
  color: #19363a;
  text-decoration: none;
  font-weight: 700;
}
.content {
  padding: 24px;
}
@media (max-width: 900px) {
  .app-shell {
    grid-template-columns: 1fr;
  }
  .menu {
    flex-direction: row;
    flex-wrap: wrap;
  }
}
</style>
