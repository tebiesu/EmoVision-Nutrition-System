import { defineStore } from "pinia";
import { apiClient } from "../api/client";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    token: localStorage.getItem("token") || ""
  }),
  actions: {
    async login(username, password) {
      const { data } = await apiClient.post("/auth/login", { username, password });
      this.token = data.data.token;
      localStorage.setItem("token", this.token);
    },
    logout() {
      this.token = "";
      localStorage.removeItem("token");
    }
  }
});
