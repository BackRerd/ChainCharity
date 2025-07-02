<template>
  <div>
    <el-form :model="form" ref="loginFormRef" @submit.prevent="login">
      <el-form-item prop="username">
        <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            prefix-icon="el-icon-user"
            size="large"
        >
        </el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            show-password
            size="large"
        >
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button
            type="primary"
            class="login-btn"
            native-type="submit"
            size="large"
            style="width: 350px"
        >
          登 录
        </el-button>
      </el-form-item>
    </el-form>

    <div class="switch-form">
      还没有账号？<a @click="emit('switch-form', 'register')">立即注册</a>
      <div class="forgot-password">
        <a href="#" @click.prevent="forgotPassword">忘记密码？</a>
      </div>
    </div>

    <div class="social-login">
      <div class="social-title">
        <span>其他登录方式</span>
      </div>
      <div class="social-icons">
        <div class="social-icon icon-google">
          <i class="el-icon-mobile-phone"></i>
        </div>
        <div class="social-icon icon-facebook">
          <i class="el-icon-chat-dot-round"></i>
        </div>
        <div class="social-icon icon-twitter">
          <i class="el-icon-chat-line-round"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const emit = defineEmits(['switch-form'])
const router = useRouter()

const form = ref({
  username: '',
  password: ''
})

const login = () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.error('请输入用户名和密码')
    return
  }

  // 模拟登录成功
  localStorage.setItem('isAuthenticated', 'true')
  ElMessage.success('登录成功！')

  // 跳转到首页
  router.push('/')
}

const forgotPassword = () => {
  ElMessage.info('密码重置功能正在开发中')
}
</script>

<style scoped>
.form-container {
  position: relative;
}

.el-form-item {
  margin-bottom: 22px;
}

.el-input__inner {
  border-radius: 12px;
  padding: 12px 15px;
  border: 1px solid #e1e8ed;
  transition: all 0.3s;
}

.el-input__inner:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(92, 156, 230, 0.2);
}

.forgot-password {
  text-align: right;
  margin-top: -20px;
  margin-bottom: 20px;
}

.forgot-password a {
  color: var(--primary-color);
  text-decoration: none;
  font-size: 14px;
}

.forgot-password a:hover {
  text-decoration: underline;
}

.switch-form {
  text-align: center;
  margin-top: 20px;
  color: #7f8c8d;
  font-size: 14px;
}

.switch-form a {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  margin-left: 5px;
}

.switch-form a:hover {
  color: #ff6b6b;
  text-decoration: underline;
}

.social-login {
  margin-top: 25px;
  text-align: center;
}

.social-title {
  position: relative;
  color: #7f8c8d;
  font-size: 14px;
  margin-bottom: 15px;
}

.social-title::before, .social-title::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 30%;
  height: 1px;
  background: #e1e8ed;
}

.social-title::before {
  left: 0;
}

.social-title::after {
  right: 0;
}

.social-icons {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.social-icon {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.social-icon:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 10px rgba(0, 0, 0, 0.15);
}

.icon-google {
  background: linear-gradient(135deg, #4285f4, #34a853);
}

.icon-facebook {
  background: linear-gradient(135deg, #3b5998, #4267b2);
}

.icon-twitter {
  background: linear-gradient(135deg, #1da1f2, #1a91da);
}
</style>