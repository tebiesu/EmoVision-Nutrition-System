# Task 07：推荐引擎规则设计

## 目标
- 将情绪、营养、目标、禁忌转为可执行建议规则。

## 输入
- 情绪标签：如 `stress/tired/happy`
- 营养结构：碳水/脂肪/蛋白
- 用户目标：减脂/维持/增肌
- 禁忌：过敏/忌口

## 规则示例（PLAN 对齐）
- R001：`emotion=stress AND sugar_intake_high` -> 降低精制糖 + 高蛋白替代
- R002：`goal=lose_fat AND dinner_carb_high` -> 晚餐碳水下调 20%
- R003：`emotion=tired` -> 补水、电解质、优质蛋白提示

## 输出
- `adviceList`（最多 3 条）
- `riskFlags`（最多 2 条）
- `explainText`（规则触发解释）

## 执行步骤
1. 定义规则 DSL/JSON 结构。
2. 实现规则匹配与优先级仲裁。
3. 输出解释文本模板。

## 验收标准
- 同输入多次运行结果一致。
- 每条建议都可追溯触发规则。

