# Task 08：前端实现设计（Vue3）

## 目标
- 实现可演示前端闭环：上传、分析、建议、趋势。

## 路由
- `/login`
- `/dashboard`
- `/meal`
- `/emotion`
- `/recommendation`
- `/profile`

## 组件
- `MealUploadCard.vue`
- `EmotionPanel.vue`
- `NutritionBreakdown.vue`
- `AdviceList.vue`
- `TrendChart.vue`

## 状态管理
- `pinia` 管理用户、token、看板缓存
- Axios 拦截器自动携带 token

## 执行步骤
1. 搭建路由与布局骨架。
2. 完成页面组件联动与 API 对接。
3. 完成 ECharts 趋势可视化。

## 验收标准
- 端到端操作无断点。
- 页面在桌面端和移动端均可用。

