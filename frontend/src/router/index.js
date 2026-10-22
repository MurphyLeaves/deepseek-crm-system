import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import CustomerList from '../views/CustomerList.vue'
import LabelManage from '../views/LabelManage.vue'
import Dashboard from '../views/Dashboard.vue'
import UserProfile from '../views/UserProfile.vue'
import request from '../api/request'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: Login, meta: { public: true } },
  { path: '/home', name: 'Home', component: Home },
  { path: '/customers', name: 'CustomerList', component: CustomerList },
  { path: '/labels', name: 'LabelManage', component: LabelManage },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard },
  { path: '/profile', name: 'UserProfile', component: UserProfile },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

/**
 * 全局前置守卫：登录访问控制
 *
 * 进入任何非公开页面之前，先向后端 /api/user/info 确认一次登录状态
 * （Session 保存在 Cookie 中，前端无法直接读取）。
 * 未登录或 Session 已过期时统一跳转到登录页，并记录原目标地址，
 * 登录成功后可以直接回到原来要访问的页面。
 */
router.beforeEach(async (to, from, next) => {
  // 登录页本身以及标记为 public 的页面直接放行
  if (to.meta && to.meta.public) {
    next()
    return
  }

  try {
    const res = await request.get('/api/user/info')
    if (res.data && res.data.code === 200) {
      next()
    } else {
      next({ path: '/login', query: { redirect: to.fullPath } })
    }
  } catch (e) {
    // 401 或网络异常，一律回到登录页
    next({ path: '/login', query: { redirect: to.fullPath } })
  }
})

export default router
