<template>
  <div class="home-container">
    <div class="header">
      <h1>📊 B端客户标注与智能评估系统</h1>
      <div class="user-info">
        <span>欢迎，{{ userInfo?.realName || userInfo?.username || '用户' }}</span>
        <button @click="handleLogout" class="logout-btn">退出</button>
      </div>
    </div>
    <div class="content">
      <p style="font-size: 18px">🎉 系统登录成功！</p>
      <p style="color: #888; margin-top: 10px">请选择功能模块开始使用</p>
      <div
        style="margin-top: 30px; display: flex; gap: 20px; justify-content: center; flex-wrap: wrap"
      >
        <button @click="$router.push('/customers')" class="btn-menu">📋 进入客户管理</button>
        <button @click="$router.push('/labels')" class="btn-menu">🏷️ 进入标签管理</button>
        <button @click="$router.push('/dashboard')" class="btn-menu">📊 进入数据看板</button>
        <button @click="$router.push('/profile')" class="btn-menu">⚙️ 系统管理</button>
      </div>
    </div>
  </div>
</template>

<script>
import request from '../api/request'

export default {
  name: 'Home',
  data() {
    return {
      userInfo: null,
    }
  },
  // ✅ 使用 beforeRouteEnter 确保每次进入页面都刷新数据
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.getUserInfo()
    })
  },
  // ✅ mounted 中也要保留，处理直接刷新浏览器的情况
  mounted() {
    this.getUserInfo()
  },
  methods: {
    async getUserInfo() {
      try {
        const response = await request.get('/api/user/info')
        if (response.data.code === 200) {
          this.userInfo = response.data.data
        } else {
          // 未登录或 Session 过期，跳转登录页
          this.$router.push('/login')
        }
      } catch (error) {
        console.error('获取用户信息失败', error)
        // 网络错误时也跳转登录页
        this.$router.push('/login')
      }
    },
    async handleLogout() {
      await request.post('/api/logout', {})
      this.$router.push('/login')
    },
  },
}
</script>

<style scoped>
.home-container {
  font-family: 'Microsoft YaHei', sans-serif;
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}
.header {
  background: white;
  padding: 20px 30px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header h1 {
  font-size: 22px;
  color: #333;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}
.user-info span {
  color: #666;
}
.logout-btn {
  padding: 6px 16px;
  border: 1px solid #667eea;
  border-radius: 4px;
  background: white;
  color: #667eea;
  cursor: pointer;
}
.logout-btn:hover {
  background: #667eea;
  color: white;
}
.content {
  margin-top: 20px;
  padding: 40px 30px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
}
.btn-menu {
  padding: 12px 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: opacity 0.2s;
}
.btn-menu:hover {
  opacity: 0.85;
}
</style>
