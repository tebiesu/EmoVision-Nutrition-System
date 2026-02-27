-- seed.sql (Task 04/07)

INSERT INTO nutrition_food_dict
(food_name, category, calories_100g, protein_100g, fat_100g, carb_100g, fiber_100g, sodium_mg_100g)
VALUES
('鸡胸肉', 'protein', 133, 24.0, 3.0, 0.0, 0.0, 74),
('西兰花', 'vegetable', 34, 2.8, 0.4, 6.6, 2.6, 33),
('米饭', 'staple', 116, 2.6, 0.3, 25.9, 0.3, 2),
('苹果', 'fruit', 52, 0.3, 0.2, 13.8, 2.4, 1)
ON DUPLICATE KEY UPDATE food_name = VALUES(food_name);

INSERT INTO rule_definition
(rule_code, priority, enabled, condition_json, action_json)
VALUES
('R001', 100, 1,
 JSON_OBJECT('emotion', 'stress', 'sugar_intake_high', true),
 JSON_OBJECT('advice', JSON_ARRAY('减少精制糖摄入', '用高蛋白零食替代甜食'), 'risk', JSON_ARRAY('情绪性进食风险'))),
('R002', 90, 1,
 JSON_OBJECT('goal', 'lose_fat', 'dinner_carb_high', true),
 JSON_OBJECT('advice', JSON_ARRAY('晚餐碳水建议下调20%'), 'risk', JSON_ARRAY('晚餐碳水超标'))),
('R003', 80, 1,
 JSON_OBJECT('emotion', 'tired'),
 JSON_OBJECT('advice', JSON_ARRAY('注意补水与电解质', '补充优质蛋白'), 'risk', JSON_ARRAY('疲劳状态影响饮食质量')))
ON DUPLICATE KEY UPDATE rule_code = VALUES(rule_code);
