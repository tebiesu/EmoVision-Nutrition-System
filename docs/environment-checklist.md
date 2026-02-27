# 环境检查清单（Task 02）

## 1. 本地开发环境
- [ ] 安装 JDK 17 并配置 `JAVA_HOME`
- [ ] 安装 Maven 3.9+
- [ ] 安装 Node.js 20+
- [ ] 安装 MySQL 8.0+ 并创建数据库
- [ ] Git 配置 UTF-8 与换行策略

## 2. 后端检查
- [ ] `java -version` 输出 17
- [ ] `mvn -version` 正常
- [ ] 可连接 MySQL
- [ ] 环境变量中已配置 Provider API Key

## 3. 前端检查
- [ ] `node -v` >= 20
- [ ] `npm -v` >= 10
- [ ] `npm ci` 成功
- [ ] `npm run dev` 启动成功

## 4. 数据检查
- [ ] 执行 `db/schema.sql` 成功
- [ ] 执行 `db/seed.sql` 成功
- [ ] 基础字典与规则种子存在

## 5. 发布前检查
- [ ] 前端 `npm run build` 成功
- [ ] 后端 `mvn clean package -DskipTests` 成功
- [ ] 健康检查接口可用
