# 部署说明（Task 12）

## 1. 目标环境
- Ubuntu 22.04
- MySQL 8.0+
- Nginx 1.20+
- Java 17
- Node.js 20+

## 2. 关键文件
- `.env.prod.example`：环境变量模板
- `deploy.sh`：部署脚本
- `nginx.conf`：反向代理配置示例

## 3. 部署流程
1. 配置环境变量并导入数据库。
2. 构建前端与后端。
3. 启动后端服务。
4. 配置 Nginx，验证 `/api` 转发。

## 4. Docker Compose 一键编排
在项目根目录执行：

```bash
docker compose up -d --build
```

访问：
- 前端：`http://localhost:5173`
- 后端健康检查：`http://localhost:8080/api/v1/health`
