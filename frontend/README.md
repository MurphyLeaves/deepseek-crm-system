# crm-frontend — 前端

B端客户标注与智能评估系统的 **Vue 3 前端**，与后端（`../` 根目录的 Spring Boot 工程）前后端分离。

## 技术栈

- **Vue 3.5** + Vite 8 + JavaScript
- **Ant Design Vue 4** + Element Plus（组件库）
- **Pinia**（状态管理）+ Vue Router（路由）
- **ECharts 6**（数据看板图表）
- **Axios**（HTTP 请求，`withCredentials` 携带 Session）

## 功能页面

| 页面 | 说明 |
|------|------|
| `Login.vue` | 登录（BCrypt + Session 认证） |
| `Home.vue` | 系统主页 |
| `CustomerList.vue` | 客户管理：CRUD、多标签组合筛选、分页 |
| `LabelManage.vue` | 标签体系管理 |
| `Dashboard.vue` | ECharts 数据看板 |
| `UserProfile.vue` | 用户信息与密码修改 |

## 目录结构

```
src/
├── api/          # 接口封装（customer/label/dashboard/evaluation/importExport/user/config）
├── views/        # 页面组件
├── components/   # 通用组件
├── stores/       # Pinia 状态
├── router/       # 路由配置
└── assets/       # 静态资源
```

## 本地运行

后端默认地址为 `http://localhost:8080`（各 api 文件中配置）。

```sh
npm install
npm run dev      # 开发模式，默认 http://localhost:5173
npm run build    # 构建产物输出到 dist/
```

> 注意：需要先启动后端服务（见仓库根目录 README），并保证后端已开启 CORS（允许前端源）。
