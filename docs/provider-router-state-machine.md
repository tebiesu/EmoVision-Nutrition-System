# Provider 路由状态机（Task 03/06）

## 1. 路由优先级
1. 首选 `NewApiProvider`
2. 失败后按规则回退 `SiliconFlowProvider`

## 2. 状态机定义
```text
[START]
  -> TRY_PRIMARY
TRY_PRIMARY
  -> SUCCESS (调用成功)
  -> RETRY_PRIMARY (timeout / 5xx / network)
  -> FALLBACK (4xx or retries exhausted)
RETRY_PRIMARY
  -> TRY_PRIMARY (maxAttempts 内)
  -> FALLBACK (超过重试次数)
FALLBACK
  -> TRY_SECONDARY
TRY_SECONDARY
  -> SUCCESS
  -> FAILED_ALL
SUCCESS
  -> END
FAILED_ALL
  -> END (返回 P1002)
```

## 3. 错误处理
- Timeout：重试 2 次，退避 300ms/800ms
- 4xx：不重试，直接回退
- 5xx/网络错误：先重试，后回退

## 4. 日志要求
1. 每次尝试都记录 `trace_id/provider/api_type/latency_ms/success`
2. 所有失败记录 `error_message` 并脱敏
