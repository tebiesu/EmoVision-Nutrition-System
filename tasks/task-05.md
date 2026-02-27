# Task 05：REST 接口设计与契约

## 目标
- 将 PLAN 的接口描述固化为前后端共同契约。

## 接口范围
- 认证：`POST /api/v1/auth/login`
- 档案：`GET/PUT /api/v1/profile`
- 饮食：`POST /api/v1/meals`、`GET /api/v1/meals`、`GET /api/v1/meals/{id}`
- 情感：`POST /api/v1/emotions/analyze`
- 建议：`POST /api/v1/recommendations/generate`
- 看板：`GET /api/v1/dashboard/overview`、`GET /api/v1/dashboard/trend`

## 通用规范
- 鉴权：`Authorization: Bearer <token>`
- 统一返回：
```json
{"code":0,"message":"ok","data":{}}
```

## 执行步骤
1. 定义 DTO、枚举、错误码映射。
2. 输出 OpenAPI 文档。
3. 完成前后端字段名与类型对齐。

## 产出物
- `openapi.yaml` 或 SpringDoc 在线文档
- 接口字段对照表

## 验收标准
- 前端可据契约独立开发。
- 所有接口都有请求示例与响应示例。

