# B端客户标注与智能评估系统（DeepSeek 版）

基于 **Spring Boot + MyBatis-Plus + MySQL + DeepSeek API + Vue 3** 的 B 端客户管理、标签标注与 AI 智能评估系统（本科毕业论文项目，前后端分离架构，本仓库包含后端完整实现与前端源码）。

## 技术栈

- **后端**：Spring Boot 4.1.0 / Java 17 + MyBatis-Plus + MySQL 8.0
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

## 快速启动（答辩 / 演示用，单端口）

前端构建产物已经放在 `src/main/resources/static/`，由 Spring Boot 直接托管，
**整个系统只跑 8080 一个端口，不需要装 Node。**

前置条件：安装 **JDK 17+** 与 **MySQL 8.0**。

1. 导入数据库（首次使用，会建库、建表并写入演示数据）：

```bash
mysql -uroot -p < db/crm_db.sql
```

2. 把 `config.local.bat.example` 复制一份改名为 `config.local.bat`，填写：

```bat
set "DB_PASSWORD=你的MySQL密码"
set "DEEPSEEK_API_KEY=sk-xxxxxxxx"
```

3. 双击 `start.bat`

浏览器访问 **http://localhost:8080**，默认账号 `admin` / `123456`。

### 用另一台电脑访问

`start.bat` 启动后会打印两个地址：

```
本机浏览器访问:   http://localhost:8080
其他电脑访问:     http://192.168.1.4:8080
```

两台电脑连同一个 WiFi / 路由器，在另一台的浏览器里直接输入第二个地址即可。

注意事项：

- 第一次访问如果连不上，多半是 Windows 防火墙拦截，用管理员 PowerShell 执行一次：
  `netsh advfirewall firewall add rule name="CRM-8080" dir=in action=allow protocol=TCP localport=8080`
- 主机不能休眠，电源计划调成"从不"，否则服务会断
- 局域网 IP 由路由器动态分配，换网络后会变，以启动窗口打印的为准
- 演示 AI 评估需要外网（调用 DeepSeek）；没有网络时，已评估过的客户仍能展示缓存结果

## 开发模式（前后端分离）

1. 准备 MySQL 8.0，创建数据库 `crm_db`（或用上面的 `db/crm_db.sql` 导入）
2. 配置环境变量：
   - `DB_PASSWORD`：数据库密码
   - `DEEPSEEK_API_KEY`：DeepSeek 开放平台 API Key
3. 启动后端：

```bash
./mvnw spring-boot:run -Dmaven.compiler.proc=full
```

> JDK 23 起 javac 默认不再运行注解处理器，Lombok 会失效导致大量 "找不到符号 getXxx()"，
> 因此需要显式加 `-Dmaven.compiler.proc=full`。

4. 启动前端（另开终端）：

```bash
cd frontend
npm install
npm run dev      # 默认 http://localhost:5173
```

5. 修改前端后重新生成静态资源（用于单端口部署）：

```bash
cd frontend && npm run build
# 然后把 dist/ 的内容复制到 src/main/resources/static/
```

## 常见问题

| 现象 | 原因与处理 |
|---|---|
| 启动报 `Port 8080 was already in use` | 上一次的服务没关干净，换端口：`start.bat` 里改 `SERVER_PORT=8081` |
| 另一台电脑打不开 | 先确认两台在同一网段、防火墙已放行 8080、主机未休眠 |
| AI 评估一直失败 | 检查 `DEEPSEEK_API_KEY` 是否有效、主机是否能访问外网 |
| 编译报大量 `找不到符号 getXxx()` | 漏了 `-Dmaven.compiler.proc=full`（JDK 23+ 必需） |

## 说明

- 数据库密码与 API Key 均通过环境变量注入，未硬编码在代码中；`config.local.bat` 已被 `.gitignore` 忽略
- 本仓库为毕业论文项目代码，用于展示 Java 后端 + AI 应用集成能力
