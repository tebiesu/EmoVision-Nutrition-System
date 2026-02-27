# 系统架构设计（Task 03）

> 边界约束：仅覆盖 P0 闭环能力。

## 1. 分层职责
- `controller`：参数校验、鉴权、响应封装。
- `service`：业务编排，串联识别、规则、落库。
- `domain`：规则匹配与营养计算。
- `infrastructure`：数据库访问、第三方 Provider 适配。
- `common`：异常码、工具类、trace 透传。

## 2. 核心链路
1. 用户上传餐食图片与餐次信息。
2. 系统调用视觉 Provider 识别食物项。
3. 系统调用情感 Provider 输出情绪标签。
4. 规则引擎基于目标、营养、禁忌生成建议。
5. 返回建议并落库，更新看板统计。

## 3. 时序图（ASCII）
```text
User -> API Gateway -> MealController -> MealService
MealService -> VisionRouter -> NewApiVisionProvider
VisionRouter -> SiliconFlowVisionProvider (fallback when needed)
MealService -> EmotionRouter -> NewApiEmotionProvider
EmotionRouter -> SiliconFlowEmotionProvider (fallback when needed)
MealService -> RuleEngine -> RecommendationRecord
MealService -> DashboardAggregator
MealService -> User: response(code/message/data)
```

## 4. 包结构建议
```text
backend/
  src/main/java/com/evns/
    common/
    controller/
    service/
    domain/
    infrastructure/
      provider/
      persistence/
```

## 5. 验收口径
1. 任意失败路径均有异常码与回退路径。
2. 主备 Provider 路由可测试。
3. traceId 可贯穿请求与 Provider 调用。
