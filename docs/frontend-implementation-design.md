# 前端实现设计（Task 08）

## 1. 页面路由
1. `/login`：登录
2. `/dashboard`：看板首页
3. `/meal`：餐食上传与记录
4. `/emotion`：情感分析
5. `/recommendation`：建议中心
6. `/profile`：用户档案

## 2. 组件拆分
1. `MealUploadCard.vue`：上传图片、餐次选择、提交
2. `EmotionPanel.vue`：文本/图像输入与情绪结果展示
3. `NutritionBreakdown.vue`：营养结构图（ECharts）
4. `AdviceList.vue`：建议与风险提示卡片
5. `TrendChart.vue`：7/30 天趋势图

## 3. 页面骨架建议
```text
AppLayout
  ├── SideMenu
  ├── HeaderBar
  └── RouterView
      ├── DashboardPage
      ├── MealPage
      ├── EmotionPage
      ├── RecommendationPage
      └── ProfilePage
```

## 4. 响应式要求
1. 桌面端主布局为双栏/三栏信息区。
2. 移动端折叠导航，卡片纵向排列。
3. 上传与建议卡片在移动端保持可读。
