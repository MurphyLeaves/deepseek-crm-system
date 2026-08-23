import axios from 'axios'

// 后端接口基础地址:
// 优先读取环境变量 VITE_API_BASE_URL(见 .env.development / .env.production),
// 未配置时回退到本地开发地址,一处配置、全局生效
const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

// 统一 axios 实例:所有请求默认携带 Session Cookie,无需每个接口重复配置
const request = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
})

export default request
