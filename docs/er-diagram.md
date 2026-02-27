# ER 图说明（Task 04）

## 1. 实体关系（ASCII）
```text
user_profile (1) ────────< (N) meal_record
user_profile (1) ────────< (N) emotion_record
meal_record   (1) ────────< (N) meal_food_item
meal_record   (1) ────────< (N) recommendation_record
emotion_record(1) ────────< (N) recommendation_record

rule_definition 独立配置表
nutrition_food_dict 独立字典表
provider_call_log 独立调用日志表（按 trace_id 聚合）
```

## 2. 关键索引策略
1. `meal_record(user_id, eaten_at)`：看板趋势查询。
2. `emotion_record(user_id, analyzed_at)`：情绪趋势分析。
3. `provider_call_log(trace_id, created_at)`：链路追踪。
