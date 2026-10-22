import axios from 'axios'

// 后端接口基础地址:
// 优先读取环境变量 VITE_API_BASE_URL(见 .env.development / .env.production),
// 未配置时回退到本地开发地址,一处配置、全局生效
// 打包环境把该变量设为空串,表示走相对路径(前端静态资源与接口同源)
const BASE_URL =
  import.meta.env.VITE_API_BASE_URL === undefined
    ? 'http://localhost:8080'
    : import.meta.env.VITE_API_BASE_URL

// 统一 axios 实例:所有请求默认携带 Session Cookie,无需每个接口重复配置
const request = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
})

// 防止 401 并发时重复跳转
let redirecting = false

/**
 * 响应拦截器:统一处理登录状态失效
 *
 * 后端拦截器检测到未登录会返回 HTTP 401,这里把用户带回登录页,
 * 并记录当前地址,便于重新登录后回到原来的页面。
 */
request.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response && error.response.status

    // 登录失效:清除本地的登录标记并跳转登录页
    if (status === 401) {
      sessionStorage.removeItem('crm_user')
      if (!redirecting && window.location.pathname !== '/login') {
        redirecting = true
        const redirect = encodeURIComponent(window.location.pathname)
        window.location.assign(`/login?redirect=${redirect}`)
      }
    }

    return Promise.reject(error)
  },
)

export default request
