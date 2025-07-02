<template>
  <div>
    <el-form :model="form" ref="registerFormRef" @submit.prevent="register">
      <el-form-item prop="name">
        <el-input
            v-model="form.name"
            placeholder="请输入姓名"
            prefix-icon="el-icon-user"
            size="large"
        >
        </el-input>
      </el-form-item>

      <el-form-item prop="email">
        <el-input
            v-model="form.email"
            placeholder="请输入邮箱"
            prefix-icon="el-icon-message"
            size="large"
        >
        </el-input>
      </el-form-item>

      <el-form-item prop="phone">
        <el-input
            v-model="form.phone"
            placeholder="请输入手机号"
            prefix-icon="el-icon-mobile-phone"
            size="large"
        >
        </el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input
            v-model="form.password"
            type="password"
            placeholder="设置密码"
            prefix-icon="el-icon-lock"
            show-password
            size="large"
        >
        </el-input>
      </el-form-item>

      <el-form-item prop="confirmPassword">
        <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="确认密码"
            prefix-icon="el-icon-lock"
            show-password
            size="large"
        >
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button
            type="primary"
            class="register-btn"
            native-type="submit"
            size="large"
            style="width: 350px"
        >
          注 册
        </el-button>
      </el-form-item>
    </el-form>

    <div class="switch-form">
      已有账号？<a @click="emit('switch-form', 'login')">立即登录</a>
    </div>

    <div class="social-login">
      <div class="social-title">
        <span>或使用以下方式注册</span>
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
  name: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const register = () => {
  if (!form.value.name) {
    ElMessage.error('请输入姓名')
    return
  }

  if (!form.value.email) {
    ElMessage.error('请输入邮箱')
    return
  }

  if (!form.value.phone) {
    ElMessage.error('请输入手机号')
    return
  }

  if (!form.value.password) {
    ElMessage.error('请输入密码')
    return
  }

  if (form.value.password !== form.value.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }

  // 模拟注册成功
  ElMessage.success('注册成功！')

  // 切换到登录表单
  emit('switch-form', 'login')
}
</script>

<style scoped>
.el-form-item {
  margin-bottom: 22px;
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
</style>