# Task 04：数据库设计与建模

## 目标
- 将 PLAN 中全部数据模型转为可执行数据库设计。

## 表清单（必须全覆盖）
- `user_profile`
- `meal_record`
- `meal_food_item`
- `emotion_record`
- `recommendation_record`
- `nutrition_food_dict`
- `rule_definition`
- `provider_call_log`

## 字段要点
- `user_profile`：目标、禁忌、活动水平
- `meal_record`：餐次、时间、总营养
- `meal_food_item`：识别项与置信度
- `emotion_record`：情绪标签、来源、输入类型
- `recommendation_record`：建议 JSON、风险 JSON、解释文本
- `rule_definition`：条件/动作 JSON 与优先级
- `provider_call_log`：trace_id、provider、耗时、状态码、错误

## 执行步骤
1. 输出 ER 图。
2. 编写 DDL（含索引、主外键、默认值）。
3. 设计初始化字典数据与规则种子数据。

## 产出物
- `schema.sql`
- `seed.sql`
- ER 图文档

## 验收标准
- DDL 可一次性执行通过。
- 核心查询（按用户/日期/餐次）具备索引支撑。

