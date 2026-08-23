import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import CustomerList from '../views/CustomerList.vue'
import LabelManage from '../views/LabelManage.vue'
import Dashboard from '../views/Dashboard.vue'
import UserProfile from '../views/UserProfile.vue'


const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: Login },
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

export default router
