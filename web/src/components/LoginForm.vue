<template>
  <div>
    <el-form :model="form" ref="loginFormRef" @submit.prevent="login" :rules="rules">
      <el-form-item prop="username">
        <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            :prefix-icon="UserIcon"
            size="large"
        >
        </el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="LockIcon"
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
        <div class="social-icon icon-qq">
          <el-icon><qq-icon /></el-icon>
        </div>
        <div class="social-icon icon-wechat">
          <el-icon><wechat-icon /></el-icon>
        </div>
        <div class="social-icon icon-mobile">
          <el-icon><mobile-icon /></el-icon>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import {
  User as UserIcon,
  Lock as LockIcon,
  Iphone as MobileIcon
} from '@element-plus/icons-vue'
// 导入QQ和微信图标（Element Plus可能没有内置，需要自定义或使用其他图标库）
import QqIcon from '@/assets/icons/QqIcon.vue'
import WechatIcon from '@/assets/icons/WechatIcon.vue'
import axios from "axios";

const emit = defineEmits(['switch-form'])
const router = useRouter()

const form = ref({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名或邮箱', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

const login = () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.error('请输入用户名和密码')
    return
  }
  axios.post('/api/auth/login',form.value).then(res => {
    if (res.data.code === "200") {
      localStorage.setItem('token', res.data.data)
      autoLogin()
    }else {
      console.log(res.data)
      ElMessage.error(res.data.message)
    }
  })


}
const autoLogin = () => {
  const item = localStorage.getItem('token');
  if (item !== null){
    axios.get(`/api/auth/token?token=${item}`).then(res => {
      if (res.data.code === "200") {
        localStorage.setItem('user', JSON.stringify(res.data.data))
        console.log(res.data.data)
        ElMessage.success('登录成功！')
        if (res.data.data.role === 0){
          router.push('/user')
        }else {
          router.push('/admin')
        }

      }
    })
  }
}
autoLogin()
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

.icon-qq {
  background: linear-gradient(135deg, #12B7F5, #0084FF);
}

.icon-wechat {
  background: linear-gradient(135deg, #07C160, #09BB07);
}

.icon-mobile {
  background: linear-gradient(135deg, #9B59B6, #8E44AD);
}

.el-icon {
  font-size: 24px;
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