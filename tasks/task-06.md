# Task 06：第三方 AI 接口抽象与路由

## 目标
- 抽象可扩展 Provider 接口，支持主备路由与故障回退。

## 接口定义
- `EmotionProvider#analyze(EmotionAnalyzeCommand)`
- `VisionProvider#detectFoods(String imageUrl)`

## 配置项
- `provider.newapi.base-url`
- `provider.newapi.api-key`
- `provider.siliconflow.base-url`
- `provider.siliconflow.api-key`
- `provider.timeout-ms`
- `provider.retry.max-attempts`

## 执行步骤
1. 编写 Provider Adapter 层。
2. 实现 Router（重试 + 回退 + 日志）。
3. 接入调用日志落库。

## 产出物
- Provider 抽象代码与实现类
- 路由器单元测试

## 验收标准
- 主通道失败时可自动回退。
- 每次调用均可追踪到 provider_call_log。

