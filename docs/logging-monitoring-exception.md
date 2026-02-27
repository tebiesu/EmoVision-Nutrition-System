# 日志、监控与异常规范（Task 10）

## 1. 异常码
- `A0000` 成功
- `A0400` 参数错误
- `A0401` 未认证
- `A0500` 系统异常
- `P1001` Provider 超时
- `P1002` Provider 全部失败

## 2. 日志策略
1. 每个请求生成 `traceId` 并写入 MDC。
2. Provider 调用写结构化日志：
   - provider
   - apiType
   - latencyMs
   - success
   - errorCode
3. 所有敏感字段脱敏。

## 3. 监控指标
1. API 总请求数、成功率、P95 延迟
2. Provider 失败率、超时率
3. 核心链路失败数（上传/分析/建议）

## 4. 排障路径
1. 通过 `traceId` 关联接口日志与 `provider_call_log`
2. 根据错误码快速定位：参数、鉴权、系统、Provider
