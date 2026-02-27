# 推荐引擎设计（Task 07）

## 1. 输入域
1. 情绪标签：`stress/tired/happy/sad`
2. 营养结构：`carb/protein/fat/sugar`
3. 用户目标：`lose_fat/maintain/gain_muscle`
4. 禁忌信息：过敏、忌口、不耐受

## 2. 规则结构（JSON DSL）
```json
{
  "ruleCode": "R001",
  "priority": 100,
  "enabled": true,
  "condition": {
    "emotion": "stress",
    "sugar_intake_high": true
  },
  "action": {
    "advice": ["减少精制糖摄入", "高蛋白零食替代甜食"],
    "risk": ["情绪性进食风险"]
  }
}
```

## 3. 仲裁策略
1. 先按 `priority` 降序排序。
2. 同优先级按 `ruleCode` 升序稳定排序。
3. 最终输出：
   - `adviceList` 最多 3 条
   - `riskFlags` 最多 2 条

## 4. 可解释性
1. 每条建议必须附带 `triggeredRules`。
2. 输出 `explainText`，例如：  
   “命中 R001（压力+高糖）和 R002（减脂+晚餐碳水偏高），因此建议控制精制糖并下调晚餐碳水比例。”

## 5. 一致性要求
1. 相同输入重复执行结果一致。
2. 禁止随机排序与不稳定输出。
