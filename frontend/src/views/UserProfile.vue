<template>
  <div class="user-profile-container">
    <!-- 返回按钮 -->
    <div class="back-bar">
      <button class="back-btn" @click="goBack">← 返回首页</button>
    </div>

    <h2>👤 系统管理</h2>

    <!-- 系统信息 -->
    <div class="info-card">
      <h3>📌 系统信息</h3>
      <div class="info-item"><span>系统名称：</span>B端客户标注与智能评估系统</div>
      <div class="info-item"><span>版本号：</span>v1.0.0</div>
      <div class="info-item">
        <span>当前用户：</span>{{ currentUser?.realName || currentUser?.username || '未登录' }}
      </div>
      <div class="info-item"><span>用户角色：</span>{{ formatRole(currentUser?.role) }}</div>
    </div>

    <!-- 修改密码 -->
    <div class="info-card">
      <h3>🔑 修改密码</h3>

      <!-- 原密码 -->
      <div class="form-row">
        <label>原密码：</label>
        <div style="display: flex; align-items: center; position: relative">
          <input
            :type="showPassword.oldPassword ? 'text' : 'password'"
            v-model="passwordForm.oldPassword"
            placeholder="请输入原密码"
            style="
              width: 280px;
              padding: 8px 40px 8px 12px;
              border: 1px solid #ddd;
              border-radius: 4px;
              font-size: 14px;
            "
          />
          <span
            @click="togglePasswordVisibility('oldPassword')"
            style="position: absolute; right: 10px; cursor: pointer; color: #888; user-select: none"
          >
            {{ showPassword.oldPassword ? '🙈' : '👁️' }}
          </span>
        </div>
      </div>

      <!-- 新密码 -->
      <div class="form-row">
        <label>新密码：</label>
        <div style="display: flex; align-items: center; position: relative">
          <input
            :type="showPassword.newPassword ? 'text' : 'password'"
            v-model="passwordForm.newPassword"
            placeholder="请输入新密码（至少6位）"
            style="
              width: 280px;
              padding: 8px 40px 8px 12px;
              border: 1px solid #ddd;
              border-radius: 4px;
              font-size: 14px;
            "
          />
          <span
            @click="togglePasswordVisibility('newPassword')"
            style="position: absolute; right: 10px; cursor: pointer; color: #888; user-select: none"
          >
            {{ showPassword.newPassword ? '🙈' : '👁️' }}
          </span>
        </div>
      </div>

      <!-- 确认密码 -->
      <div class="form-row">
        <label>确认密码：</label>
        <div style="display: flex; align-items: center; position: relative">
          <input
            :type="showPassword.confirmPassword ? 'text' : 'password'"
            v-model="passwordForm.confirmPassword"
            placeholder="请再次输入新密码"
            style="
              width: 280px;
              padding: 8px 40px 8px 12px;
              border: 1px solid #ddd;
              border-radius: 4px;
              font-size: 14px;
            "
          />
          <span
            @click="togglePasswordVisibility('confirmPassword')"
            style="position: absolute; right: 10px; cursor: pointer; color: #888; user-select: none"
          >
            {{ showPassword.confirmPassword ? '🙈' : '👁️' }}
          </span>
        </div>
      </div>

      <button @click="handleUpdatePassword" class="btn-save">保存密码</button>
    </div>

    <!-- 用户列表（管理员可见） -->
    <div class="info-card" v-if="isAdmin">
      <h3>👥 用户列表</h3>
      <table class="user-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>真实姓名</th>
            <th>角色</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in userList" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.realName || '-' }}</td>
            <td>{{ formatRole(user.role) }}</td>
          </tr>
          <tr v-if="userList.length === 0">
            <td colspan="4" style="text-align: center; color: #999">暂无用户数据</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { message, Modal } from 'ant-design-vue'
import { getUserList, updatePassword } from '../api/user'
import request from '../api/request'

export default {
  data() {
    return {
      currentUser: null,
      userList: [],
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: '',
      },
      showPassword: {
        oldPassword: false,
        newPassword: false,
        confirmPassword: false,
      },
    }
  },
  mounted() {
    this.loadUserInfo()
  },
  computed: {
    /**
     * 当前登录用户是否为管理员
     * 数据库中 role 实际存储为小写 "admin"，这里统一转小写比较，
     * 避免大小写不一致导致管理员也看不到用户列表。
     */
    isAdmin() {
      return String(this.currentUser?.role || '').toLowerCase() === 'admin'
    },
  },
  methods: {
    goBack() {
      this.$router.push('/home')
    },

    /**
     * 角色显示文案：admin -> 管理员，user -> 普通用户
     */
    formatRole(role) {
      const value = String(role || '').toLowerCase()
      if (value === 'admin') return '管理员'
      if (value === 'user') return '普通用户'
      return role || '普通用户'
    },

    loadUserInfo() {
      request
        .get('/api/user/info')
        .then((res) => {
          if (res.data.code === 200) {
            this.currentUser = res.data.data
            if (String(this.currentUser?.role || '').toLowerCase() === 'admin') {
              this.loadUserList()
            }
          }
        })
        .catch(() => {
          this.currentUser = { realName: '用户', role: 'user' }
        })
    },

    async loadUserList() {
      try {
        const res = await getUserList()
        if (res.data.code === 200) {
          this.userList = res.data.data || []
        }
      } catch (e) {
        console.error('加载用户列表失败', e)
      }
    },

    togglePasswordVisibility(field) {
      this.showPassword[field] = !this.showPassword[field]
    },

    async handleUpdatePassword() {
      const { oldPassword, newPassword, confirmPassword } = this.passwordForm

      if (!oldPassword) {
        message.warning('请输入原密码')
        return
      }
      if (!newPassword || newPassword.length < 6) {
        message.warning('新密码至少6位')
        return
      }
      if (newPassword !== confirmPassword) {
        message.warning('两次输入的新密码不一致')
        return
      }

      try {
        const res = await updatePassword(oldPassword, newPassword)
        if (res.data.code === 200) {
          Modal.success({
            title: '密码修改成功',
            content: '请重新登录系统',
            okText: '去登录',
            onOk: () => {
              this.$router.push('/login')
            },
          })
          this.passwordForm = { oldPassword: '', newPassword: '', confirmPassword: '' }
        } else {
          Modal.error({
            title: '修改失败',
            content: res.data.message || '原密码错误，请重新输入',
          })
        }
      } catch (e) {
        Modal.error({
          title: '修改失败',
          content: '网络异常，请检查后端服务是否运行',
        })
        console.error(e)
      }
    },
  },
}
</script>

<style scoped>
.user-profile-container {
  padding: 20px;
  background: #f0f2f5;
  min-height: 100vh;
}

.back-bar {
  margin-bottom: 16px;
}
.back-btn {
  background: none;
  border: none;
  color: #667eea;
  font-size: 14px;
  cursor: pointer;
  padding: 4px 0;
}
.back-btn:hover {
  color: #764ba2;
}

.user-profile-container h2 {
  margin-bottom: 20px;
}

.info-card {
  background: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
  max-width: 800px;
}
.info-card h3 {
  margin-bottom: 16px;
  font-size: 16px;
  color: #333;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.info-item {
  padding: 6px 0;
  font-size: 14px;
  color: #555;
}
.info-item span {
  font-weight: 600;
  color: #333;
  display: inline-block;
  width: 100px;
}

.form-row {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}
.form-row label {
  width: 100px;
  font-size: 14px;
  color: #555;
}

.btn-save {
  padding: 8px 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  margin-left: 100px;
}
.btn-save:hover {
  opacity: 0.85;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}
.user-table th {
  background: #f5f7fa;
  padding: 10px 12px;
  text-align: left;
  border-bottom: 2px solid #e8ecf1;
}
.user-table td {
  padding: 8px 12px;
  border-bottom: 1px solid #e8ecf1;
}
.user-table tr:hover td {
  background: #fafafa;
}

/* 密码框输入框聚焦效果 */
input:focus {
  outline: none;
  border-color: #667eea !important;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}
</style>
