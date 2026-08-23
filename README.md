# B端客户标注与智能评估系统（DeepSeek 版）

基于 **Spring Boot + MyBatis-Plus + MySQL + DeepSeek API + Vue 3** 的 B 端客户管理、标签标注与 AI 智能评估系统（本科毕业论文项目，前后端分离架构，本仓库包含后端完整实现与前端源码）。

## 技术栈

- **后端**：Spring Boot 3.x / Java 17 + MyBatis-Plus + MySQL 8.0
- **前端**：Vue 3.5 + Vite + Ant Design Vue 4 + Pinia + Vue Router + ECharts 6（见 `frontend/`）
- **AI 集成**：DeepSeek API（`deepseek-chat`），评估结果持久化缓存 + 强制刷新 + 动态提示词
- **认证**：BCrypt 密码加密 + Session 会话

## 核心功能

| 模块 | 说明 |
|------|------|
| 客户管理 | 客户 CRUD、多标签组合筛选（GROUP BY + HAVING）、分页查询优化、批量查标签解决 N+1 |
| 标签标注 | 客户多标签标注，标签体系灵活配置 |
| AI 智能评估 | 接入 DeepSeek 大模型，按动态提示词对客户进行智能评估，结果缓存可强制刷新 |
| Excel 导入导出 | Apache POI 实现，逐行校验（`ImportExportService`） |
| 数据看板 | ECharts 看板数据接口（`DashboardController`） |
| 用户认证 | 登录、密码修改、BCrypt 加密 + Session（`LoginController` / `UserService`） |
| 系统配置 | 提示词等系统参数动态配置（`SysConfig`，支持多占位符模板） |

## 目录结构

```
src/main/java/com/lestcrm/     # 后端
├── controller/    # REST 接口（Customer/Label/Evaluation/Dashboard/ImportExport/Login/Config）
├── service/       # 业务逻辑（CustomerServiceImpl/LabelServiceImpl/ImportExportServiceImpl 等）
├── mapper/        # MyBatis-Plus Mapper
├── entity/        # 实体（Customer/CustomerLabel/Label/User/SysConfig）
├── dto/           # 请求/响应 DTO（CustomerQueryDTO/CustomerSaveDTO/CustomerEvaluationDTO 等）
└── config/        # 配置（MyBatisPlusConfig/CorsConfig/RestTemplateConfig）

frontend/                        # 前端（Vue 3 单页应用，详见 frontend/README.md）
├── src/api/       # 接口封装
├── src/views/     # 页面（客户管理/标签/看板/登录/用户中心）
└── ...
```

资源文件：`src/main/resources/mapper/`（SQL 映射）、`templates/`（服务端页面模板）、`application.yaml`（配置）。

## 本地运行

1. 准备 MySQL 8.0，创建数据库 `crm_db`
2. 配置环境变量：
   - `DB_PASSWORD`：数据库密码
   - `DEEPSEEK_API_KEY`：DeepSeek 开放平台 API Key
3. 启动后端：

```bash
./mvnw spring-boot:run
```

4. 启动前端（另开终端）：

```bash
cd frontend
npm install
npm run dev      # 默认 http://localhost:5173
```

## 说明

- 数据库密码与 API Key 均通过环境变量注入，未硬编码在代码中
- 本仓库为毕业论文项目代码，用于展示 Java 后端 + AI 应用集成能力
