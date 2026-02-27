# 基于情感识别与视觉分析的个人饮食健康智能助手

## 1. 项目定位与目标

### 1.1 目标
- 构建一个可演示、可交付的 Web 系统，完成“饮食记录 + 情感识别 + 个性化建议 + 趋势分析”闭环。
- 技术路线以工程落地优先：`Spring Boot + Vue3 + MySQL`。
- AI 能力采用云 API：主通道 `OpenAI/Gemini(New API)`，回退通道 `SiliconFlow`。

### 1.2 非目标
- 不做自训练大模型与复杂在线学习。
- 不做多角色复杂权限系统（采用单用户/轻账号方案）。
- 不做医疗诊断，仅提供健康管理建议。

## 2. 技术栈与版本

### 2.1 后端
- JDK 17
- Spring Boot 3.3.x
- Spring Web / Validation / Security(JWT)
- MyBatis-Plus 3.5.x
- MySQL 8.0+
- Maven 3.9+

### 2.2 前端
- Node.js 20+
- Vue 3 + Vite 5
- Element Plus
- Axios
- ECharts

### 2.3 基础能力
- 对象映射：MapStruct（可选）
- 接口文档：SpringDoc OpenAPI
- 日志：Logback + MDC 请求追踪

## 3. 系统架构设计

### 3.1 分层架构
- `controller`：REST 接口层，参数校验、统一返回。
- `service`：业务编排层，聚合识别、规则、建议、存储逻辑。
- `domain`：领域对象与规则计算核心。
- `infrastructure`：数据库访问、第三方 API 适配器、文件存储。
- `common`：异常码、工具类、响应封装、审计字段。

### 3.2 关键链路
1. 用户上传餐食图。
2. 后端调用视觉识别 Provider 提取食物项。
3. 后端调用情感识别 Provider 获取情绪标签与置信度。
4. 结合用户目标、禁忌、营养库，执行规则引擎。
5. 返回建议并落库，更新趋势统计。

### 3.3 Provider 路由策略
- 优先级：`NewApiProvider -> SiliconFlowProvider`
- 失败处理：
  - 超时：2 次重试（指数退避 300ms/800ms）
  - 4xx 业务错误：不重试，直接回退
  - 5xx / 网络错误：重试后回退
- 全过程记录 `provider_call_log`

## 4. 数据库设计（MySQL）

### 4.1 表结构总览
- `user_profile`：用户档案
- `meal_record`：饮食主记录
- `meal_food_item`：识别出的食物明细
- `emotion_record`：情感识别记录
- `recommendation_record`：建议记录
- `nutrition_food_dict`：营养字典
- `rule_definition`：规则配置
- `provider_call_log`：第三方调用日志

### 4.2 核心字段设计

#### user_profile
- `id` bigint pk
- `user_name` varchar(50)
- `gender` tinyint
- `age` int
- `height_cm` decimal(5,2)
- `weight_kg` decimal(5,2)
- `goal_type` varchar(20)  // lose_fat/gain_muscle/maintain
- `diet_taboo` json
- `activity_level` varchar(20)
- `created_at` datetime
- `updated_at` datetime

#### meal_record
- `id` bigint pk
- `user_id` bigint
- `meal_type` varchar(20) // breakfast/lunch/dinner/snack
- `eaten_at` datetime
- `image_url` varchar(255)
- `note` varchar(500)
- `total_calories` decimal(10,2)
- `protein_g` decimal(10,2)
- `fat_g` decimal(10,2)
- `carb_g` decimal(10,2)
- `created_at` datetime

#### meal_food_item
- `id` bigint pk
- `meal_id` bigint
- `food_name` varchar(100)
- `estimated_weight_g` decimal(10,2)
- `calories` decimal(10,2)
- `protein_g` decimal(10,2)
- `fat_g` decimal(10,2)
- `carb_g` decimal(10,2)
- `confidence` decimal(5,4)

#### emotion_record
- `id` bigint pk
- `user_id` bigint
- `meal_id` bigint null
- `emotion_type` varchar(30) // calm/stress/anxiety/happy/sad/tired
- `confidence` decimal(5,4)
- `source_provider` varchar(30)
- `input_type` varchar(20) // image/text/mix
- `analyzed_at` datetime

#### recommendation_record
- `id` bigint pk
- `user_id` bigint
- `meal_id` bigint null
- `emotion_id` bigint null
- `advice_json` json
- `risk_json` json
- `explain_text` text
- `created_at` datetime

#### nutrition_food_dict
- `id` bigint pk
- `food_name` varchar(100)
- `category` varchar(30)
- `calories_100g` decimal(10,2)
- `protein_100g` decimal(10,2)
- `fat_100g` decimal(10,2)
- `carb_100g` decimal(10,2)
- `fiber_100g` decimal(10,2)
- `sodium_mg_100g` decimal(10,2)

#### rule_definition
- `id` bigint pk
- `rule_code` varchar(50) unique
- `priority` int
- `enabled` tinyint
- `condition_json` json
- `action_json` json
- `updated_at` datetime

#### provider_call_log
- `id` bigint pk
- `trace_id` varchar(64)
- `provider` varchar(30)
- `api_type` varchar(30) // emotion/vision/chat
- `request_digest` varchar(255)
- `response_code` varchar(20)
- `success` tinyint
- `latency_ms` int
- `error_message` varchar(500)
- `created_at` datetime

## 5. 接口设计（RESTful）

### 5.1 通用规范
- Base URL：`/api/v1`
- 鉴权：`Authorization: Bearer <token>`
- 返回体：
```json
{
  "code": 0,
  "message": "ok",
  "data": {}
}
```

### 5.2 认证接口
- `POST /auth/login`
  - req: `{ "username": "xxx", "password": "xxx" }`
  - resp: `{ "token": "...", "expireAt": "..." }`

### 5.3 用户档案
- `GET /profile`
- `PUT /profile`

### 5.4 饮食记录
- `POST /meals`
  - form-data: `image`, `mealType`, `eatenAt`, `note`
- `GET /meals?page=1&size=10&date=2026-03-01`
- `GET /meals/{id}`

### 5.5 情感分析
- `POST /emotions/analyze`
  - req:
```json
{
  "imageUrl": "https://...",
  "text": "今天压力很大",
  "sceneTag": "workday"
}
```

### 5.6 建议生成
- `POST /recommendations/generate`
  - req: `{ "mealId": 1, "emotionId": 2 }`
  - resp: 建议列表 + 风险提示 + 解释文本

### 5.7 看板统计
- `GET /dashboard/overview`
- `GET /dashboard/trend?days=7`

## 6. 第三方 AI 接口抽象

### 6.1 接口定义
```java
public interface EmotionProvider {
    EmotionResult analyze(EmotionAnalyzeCommand command);
}

public interface VisionProvider {
    FoodDetectResult detectFoods(String imageUrl);
}
```

### 6.2 Router 逻辑
```text
try NewApiProvider
if fail -> retry
if still fail -> switch SiliconFlowProvider
record all attempts
```

### 6.3 配置项
- `provider.newapi.base-url`
- `provider.newapi.api-key`
- `provider.siliconflow.base-url`
- `provider.siliconflow.api-key`
- `provider.timeout-ms`
- `provider.retry.max-attempts`

## 7. 推荐引擎设计

### 7.1 输入
- 最新情绪标签（如 `stress`）
- 当餐营养结构（碳水/脂肪/蛋白）
- 用户目标（减脂/维持/增肌）
- 禁忌信息（过敏、忌口）

### 7.2 规则示例
- 规则 R001：`emotion=stress AND sugar_intake_high`
  - 动作：减少精制糖建议 + 增加高蛋白零食替代
- 规则 R002：`goal=lose_fat AND dinner_carb_high`
  - 动作：晚餐碳水建议下调 20%
- 规则 R003：`emotion=tired`
  - 动作：增加补水、电解质和优质蛋白提示

### 7.3 输出
- `adviceList`：可执行建议（最多 3 条）
- `riskFlags`：风险提示（最多 2 条）
- `explainText`：触发规则解释（用于答辩展示）

## 8. 前端实现设计（Vue3）

### 8.1 页面路由
- `/login` 登录
- `/dashboard` 首页看板
- `/meal` 饮食记录
- `/emotion` 情感分析
- `/recommendation` 建议中心
- `/profile` 个人档案

### 8.2 组件划分
- `MealUploadCard.vue`：图片上传+餐次选择
- `EmotionPanel.vue`：情感输入与分析结果
- `NutritionBreakdown.vue`：营养构成图
- `AdviceList.vue`：建议卡片
- `TrendChart.vue`：7/30 天趋势图

### 8.3 状态管理
- `pinia` 管理用户信息、token、看板缓存
- Axios 拦截器自动附带 token

## 9. 安全与合规

### 9.1 基础安全
- JWT 鉴权，接口权限校验
- 参数校验（Hibernate Validator）
- SQL 注入防护（参数化查询）
- 文件上传类型/大小限制

### 9.2 数据合规
- 明确告知“仅健康建议，非医疗诊断”
- 用户隐私字段最小化存储
- API Key 仅配置在服务端环境变量

## 10. 日志、监控与异常

### 10.1 统一异常码
- `A0000` 成功
- `A0400` 参数错误
- `A0401` 未认证
- `A0500` 系统异常
- `P1001` Provider 超时
- `P1002` Provider 全部失败

### 10.2 日志策略
- 每次请求生成 `traceId`
- Provider 调用写结构化日志与数据库调用日志
- 敏感信息（token、key）脱敏

## 11. 测试设计

### 11.1 单元测试
- `ProviderRouterTest`：回退策略验证
- `RuleEngineTest`：规则命中与优先级验证
- `NutritionCalculatorTest`：营养计算精度

### 11.2 集成测试
- 上传图片 -> 识别 -> 建议生成全链路
- Provider 主通道异常时系统降级
- 看板数据聚合正确性

### 11.3 验收用例
1. 上传早餐图，系统输出食物和热量估算。
2. 输入“压力大”，系统给出低糖替代建议。
3. 查看 7 天趋势图，能看到饮食与情绪变化。

## 12. 部署方案

### 12.1 环境
- 云服务器：2C4G（最低）
- OS：Ubuntu 22.04
- MySQL 8
- Nginx 反向代理

### 12.2 部署步骤
1. 前端构建：`npm run build`
2. 后端打包：`mvn clean package -DskipTests`
3. 启动后端：`java -jar app.jar --spring.profiles.active=prod`
4. Nginx 配置静态资源与 `/api` 转发

## 13. 里程碑与交付物

### 13.1 里程碑
1. 第1-2周：需求与开题材料
2. 第3-6周：核心功能开发
3. 第7-10周：联调与测试
4. 第11-14周：优化与论文初稿
5. 第15-16周：答辩材料与最终提交

### 13.2 交付物
- 源代码（前后端）
- 数据库脚本
- 接口文档
- 测试报告
- 论文与答辩 PPT

## 14. 风险与应对
- 风险1：第三方 API 不稳定
  - 预案：双 Provider 回退 + 本地 Mock
- 风险2：识别结果波动
  - 预案：增加人工修正入口 + 置信度阈值
- 风险3：进度延迟
  - 预案：优先核心闭环，次要功能后置

## 15. 最小可行版本（MVP）
- 必做：
  - 用户档案
  - 餐食上传与识别
  - 情感识别
  - 规则建议
  - 基础看板
- 可延期：
  - 更复杂的规则可视化配置
  - 多终端适配
  - 高级报表导出
