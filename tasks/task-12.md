# Task 12：部署方案

## 目标
- 完成云端可运行部署，保证答辩演示稳定。

## 环境
- 云服务器：2C4G（最低）
- Ubuntu 22.04
- MySQL 8
- Nginx 反向代理

## 部署步骤
1. 前端构建：`npm run build`
2. 后端打包：`mvn clean package -DskipTests`
3. 启动后端：`java -jar app.jar --spring.profiles.active=prod`
4. Nginx 配置静态资源与 `/api` 转发

## 执行步骤
1. 编写部署脚本与环境变量模板。
2. 配置 Nginx 与 HTTPS（如证书可用）。
3. 完成一次完整发布演练。

## 验收标准
- 新服务器可按文档一次性部署成功。
- 前后端、数据库、第三方 API 均连通。

