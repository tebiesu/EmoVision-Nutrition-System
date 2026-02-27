# Task 03：系统架构与核心链路

## 目标
- 落地分层架构与关键业务链路，定义 Provider 路由策略。

## 架构细节
- 分层：`controller / service / domain / infrastructure / common`
- 关键链路：
1. 上传餐食图
2. 视觉识别提取食物项
3. 情感识别输出标签与置信度
4. 规则引擎生成建议
5. 返回并落库 + 趋势统计

## Provider 策略
- 优先级：`NewApiProvider -> SiliconFlowProvider`
- 失败处理：
  - 超时：2 次重试（300ms/800ms）
  - 4xx：不重试，直接回退
  - 5xx/网络错误：重试后回退
- 记录：全量写 `provider_call_log`

## 执行步骤
1. 绘制架构图与时序图。
2. 定义每层包结构与职责边界。
3. 编写 Provider 路由状态机说明。

## 产出物
- 架构设计文档（图 + 文）
- Provider 路由规则文档

## 验收标准
- 链路、职责、回退策略可直接指导编码。
- 任一失败路径都有明确处理规则。

