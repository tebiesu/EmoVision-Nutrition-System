import { createApp } from "vue";
import { createPinia } from "pinia";
import { createRouter, createWebHistory } from "vue-router";
import App from "./App.vue";
import LoginPage from "./pages/LoginPage.vue";
import DashboardPage from "./pages/DashboardPage.vue";
import MealPage from "./pages/MealPage.vue";
import EmotionPage from "./pages/EmotionPage.vue";
import RecommendationPage from "./pages/RecommendationPage.vue";
import ProfilePage from "./pages/ProfilePage.vue";
import { useAuthStore } from "./stores/auth";

const routes = [
  { path: "/login", component: LoginPage },
  { path: "/dashboard", component: DashboardPage },
  { path: "/meal", component: MealPage },
  { path: "/emotion", component: EmotionPage },
  { path: "/recommendation", component: RecommendationPage },
  { path: "/profile", component: ProfilePage },
  { path: "/:pathMatch(.*)*", redirect: "/dashboard" }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to) => {
  const auth = useAuthStore();
  if (to.path !== "/login" && !auth.token) {
    return "/login";
  }
});

const app = createApp(App);
app.use(createPinia());
app.use(router);
app.mount("#app");
