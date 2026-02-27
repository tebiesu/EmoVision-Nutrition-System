-- schema.sql (Task 04)
-- MySQL 8.0+

CREATE TABLE IF NOT EXISTS user_profile (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_name VARCHAR(50) NOT NULL,
  gender TINYINT NULL,
  age INT NULL,
  height_cm DECIMAL(5,2) NULL,
  weight_kg DECIMAL(5,2) NULL,
  goal_type VARCHAR(20) NOT NULL,
  diet_taboo JSON NULL,
  activity_level VARCHAR(20) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS meal_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  meal_type VARCHAR(20) NOT NULL,
  eaten_at DATETIME NOT NULL,
  image_url VARCHAR(255) NULL,
  note VARCHAR(500) NULL,
  total_calories DECIMAL(10,2) NULL,
  protein_g DECIMAL(10,2) NULL,
  fat_g DECIMAL(10,2) NULL,
  carb_g DECIMAL(10,2) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_meal_user FOREIGN KEY (user_id) REFERENCES user_profile(id),
  INDEX idx_meal_user_time (user_id, eaten_at),
  INDEX idx_meal_type_time (meal_type, eaten_at)
);

CREATE TABLE IF NOT EXISTS meal_food_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  meal_id BIGINT NOT NULL,
  food_name VARCHAR(100) NOT NULL,
  estimated_weight_g DECIMAL(10,2) NULL,
  calories DECIMAL(10,2) NULL,
  protein_g DECIMAL(10,2) NULL,
  fat_g DECIMAL(10,2) NULL,
  carb_g DECIMAL(10,2) NULL,
  confidence DECIMAL(5,4) NULL,
  CONSTRAINT fk_food_meal FOREIGN KEY (meal_id) REFERENCES meal_record(id),
  INDEX idx_food_meal (meal_id)
);

CREATE TABLE IF NOT EXISTS emotion_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  meal_id BIGINT NULL,
  emotion_type VARCHAR(30) NOT NULL,
  confidence DECIMAL(5,4) NULL,
  source_provider VARCHAR(30) NOT NULL,
  input_type VARCHAR(20) NOT NULL,
  analyzed_at DATETIME NOT NULL,
  CONSTRAINT fk_emotion_user FOREIGN KEY (user_id) REFERENCES user_profile(id),
  CONSTRAINT fk_emotion_meal FOREIGN KEY (meal_id) REFERENCES meal_record(id),
  INDEX idx_emotion_user_time (user_id, analyzed_at)
);

CREATE TABLE IF NOT EXISTS recommendation_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  meal_id BIGINT NULL,
  emotion_id BIGINT NULL,
  advice_json JSON NOT NULL,
  risk_json JSON NULL,
  explain_text TEXT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_reco_user FOREIGN KEY (user_id) REFERENCES user_profile(id),
  CONSTRAINT fk_reco_meal FOREIGN KEY (meal_id) REFERENCES meal_record(id),
  CONSTRAINT fk_reco_emotion FOREIGN KEY (emotion_id) REFERENCES emotion_record(id),
  INDEX idx_reco_user_time (user_id, created_at)
);

CREATE TABLE IF NOT EXISTS nutrition_food_dict (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  food_name VARCHAR(100) NOT NULL,
  category VARCHAR(30) NULL,
  calories_100g DECIMAL(10,2) NOT NULL,
  protein_100g DECIMAL(10,2) NOT NULL,
  fat_100g DECIMAL(10,2) NOT NULL,
  carb_100g DECIMAL(10,2) NOT NULL,
  fiber_100g DECIMAL(10,2) NULL,
  sodium_mg_100g DECIMAL(10,2) NULL,
  UNIQUE KEY uk_food_name (food_name)
);

CREATE TABLE IF NOT EXISTS rule_definition (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  rule_code VARCHAR(50) NOT NULL,
  priority INT NOT NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  condition_json JSON NOT NULL,
  action_json JSON NOT NULL,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_rule_code (rule_code),
  INDEX idx_rule_enabled_priority (enabled, priority)
);

CREATE TABLE IF NOT EXISTS provider_call_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  trace_id VARCHAR(64) NOT NULL,
  provider VARCHAR(30) NOT NULL,
  api_type VARCHAR(30) NOT NULL,
  request_digest VARCHAR(255) NULL,
  response_code VARCHAR(20) NULL,
  success TINYINT NOT NULL,
  latency_ms INT NOT NULL,
  error_message VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_provider_trace (trace_id, created_at),
  INDEX idx_provider_type_time (provider, api_type, created_at)
);
