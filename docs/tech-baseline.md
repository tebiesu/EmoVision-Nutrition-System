# 技术基线文档（Task 02）

> 范围约束：本文件遵循 `docs/requirement-boundary.md` 与 `docs/mvp-priority-list.md`。

## 1. 版本基线
### 1.1 后端
- JDK: 17
- Maven: 3.9+
- Spring Boot: 3.3.x
- Spring Security: 6.x（JWT）
- MyBatis-Plus: 3.5.x
- MySQL: 8.0+

### 1.2 前端
- Node.js: 20+
- 包管理器: npm 10+（或 pnpm 9+，二选一并固定）
- Vue: 3.x
- Vite: 5.x
- Element Plus: 2.x
- Axios: 1.x
- ECharts: 5.x

### 1.3 工程支撑
- SpringDoc OpenAPI: 2.x
- Logback + MDC traceId
- MapStruct: 1.5.x（可选）

## 2. 依赖管理策略
1. 后端依赖由 `pom.xml` 统一锁定版本，不允许在子模块漂移。
2. 前端依赖通过 lockfile 固定，提交 `package-lock.json`。
3. CI 中增加版本检查，禁止“本地可用、线上失败”。

## 3. 编码规范
1. 文件编码统一 UTF-8。
2. 时间字段统一 UTC 存储，接口输出 ISO8601。
3. 数据库命名下划线风格，Java 代码驼峰风格。

## 4. 构建与运行命令
- 后端构建：`mvn clean package -DskipTests`
- 后端测试：`mvn test`
- 前端安装：`npm ci`
- 前端开发：`npm run dev`
- 前端构建：`npm run build`

## 5. 验收标准
1. 新成员按文档可在当日启动前后端。
2. 构建链路在同一版本基线下可复现。
3. 不存在依赖版本冲突导致的启动失败。
