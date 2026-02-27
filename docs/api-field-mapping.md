# 接口字段对照表（Task 05）

## 1. 统一返回结构
- `code`: 业务状态码（0 成功）
- `message`: 人类可读信息
- `data`: 业务数据

## 2. 核心 DTO 对照
1. LoginReq
   - `username`: string
   - `password`: string
2. MealsCreateReq(form-data)
   - `image`: file
   - `mealType`: breakfast/lunch/dinner/snack
   - `eatenAt`: datetime
   - `note`: string
3. EmotionAnalyzeReq
   - `imageUrl`: string
   - `text`: string
   - `sceneTag`: string
4. RecommendationGenerateReq
   - `mealId`: long
   - `emotionId`: long

## 3. 错误码映射
- `A0000`: success
- `A0400`: 参数错误
- `A0401`: 未认证
- `A0500`: 系统异常
- `P1001`: Provider 超时
- `P1002`: Provider 全部失败
