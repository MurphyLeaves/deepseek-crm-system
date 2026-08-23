<template>
  <div class="login-container">
    <div class="login-card">
      <h2>📊 B端客户标注系统</h2>
      <p class="subtitle">基于 Spring Boot + DeepSeek</p>

      <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>

      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>用户名</label>
          <input type="text" v-model="loginForm.username" placeholder="请输入用户名" required />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input type="password" v-model="loginForm.password" placeholder="请输入密码" required />
        </div>
        <button type="submit" class="btn-login" :disabled="loading">
          {{ loading ? '登录中...' : '登 录' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
      },
      errorMsg: '',
      loading: false,
    }
  },
  methods: {
    async handleLogin() {
      this.loading = true
      this.errorMsg = ''
      try {
        const response = await axios.post('http://localhost:8080/api/login', this.loginForm, {
          withCredentials: true, // 允许携带cookie
        })
        if (response.data.code === 200) {
          // 登录成功，跳转到主页
          this.$router.push('/home')
        } else {
          this.errorMsg = response.data.message || '登录失败'
        }
      } catch (error) {
        this.errorMsg = '网络错误，请检查后端服务是否启动'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
  },
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  width: 380px;
}
.login-card h2 {
  text-align: center;
  color: #333;
  margin-bottom: 4px;
}
.subtitle {
  text-align: center;
  color: #999;
  font-size: 14px;
  margin-bottom: 30px;
}
.form-group {
  margin-bottom: 20px;
}
.form-group label {
  display: block;
  margin-bottom: 6px;
  color: #555;
  font-size: 14px;
}
.form-group input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  box-sizing: border-box;
}
.form-group input:focus {
  outline: none;
  border-color: #667eea;
}
.btn-login {
  width: 100%;
  padding: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
}
.btn-login:hover {
  opacity: 0.9;
}
.btn-login:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.error-msg {
  color: #e74c3c;
  font-size: 14px;
  text-align: center;
  margin-bottom: 15px;
  padding: 8px;
  background: #fde8e8;
  border-radius: 4px;
}
</style>
