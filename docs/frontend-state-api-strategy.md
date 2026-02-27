# 前端状态与 API 对接策略（Task 08）

## 1. Pinia Store 设计
1. `useAuthStore`
   - `token`
   - `expireAt`
   - `login/logout`
2. `useProfileStore`
   - `profile`
   - `fetchProfile/updateProfile`
3. `useDashboardStore`
   - `overview`
   - `trendData`
   - `fetchOverview/fetchTrend`

## 2. Axios 拦截器
1. 请求拦截：自动附加 `Authorization: Bearer <token>`
2. 响应拦截：
   - `A0401` 自动跳转登录
   - 其他错误统一 toast

## 3. API 分层
- `src/api/auth.ts`
- `src/api/profile.ts`
- `src/api/meal.ts`
- `src/api/emotion.ts`
- `src/api/recommendation.ts`
- `src/api/dashboard.ts`

## 4. 验收
1. 关键流程接口可串联：上传 -> 分析 -> 建议 -> 看板刷新。
2. token 过期自动处理，不出现白屏。
