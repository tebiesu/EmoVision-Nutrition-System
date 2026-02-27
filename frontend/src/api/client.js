import axios from "axios";
import { useAuthStore } from "../stores/auth";

export const apiClient = axios.create({
  baseURL: "/api/v1"
});

apiClient.interceptors.request.use((config) => {
  const auth = useAuthStore();
  if (auth.token) {
    config.headers.Authorization = `Bearer ${auth.token}`;
  }
  return config;
});

apiClient.interceptors.response.use(
  (resp) => resp,
  (error) => {
    if (error?.response?.status === 403) {
      const auth = useAuthStore();
      auth.logout();
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);
