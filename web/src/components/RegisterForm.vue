<template>
  <div>
    <el-form :model="form" ref="registerFormRef" @submit.prevent="register">
      <el-form-item prop="username">
        <el-input
            v-model="form.username"
            placeholder="请输入姓名"
            :prefix-icon="UserIcon"
            size="large"
        >
        </el-input>
      </el-form-item>


      <el-form-item prop="phone">
        <el-input
            v-model="form.phone"
            placeholder="请输入手机号"
            :prefix-icon="IphoneIcon"
            size="large"
        >
        </el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input
            v-model="form.password"
            type="password"
            placeholder="设置密码"
            :prefix-icon="LockIcon"
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
            :prefix-icon="LockIcon"
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
  Iphone as IphoneIcon,
  Message as MessageIcon
} from '@element-plus/icons-vue'
import QqIcon from '@/assets/icons/QqIcon.vue'
import WechatIcon from '@/assets/icons/WechatIcon.vue'
import MobileIcon from '@/assets/icons/MobileIcon.vue'
import axios from "axios";

const emit = defineEmits(['switch-form'])
const router = useRouter()

const form = ref({
  username: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const register = () => {
  if (!form.value.username) {
    ElMessage.error('请输入姓名')
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

  axios.post('/api/auth/register', form.value).then(res => {
    if (res.data.code === "200"){
      ElMessage.success('注册成功！')
      emit('switch-form', 'login')
    }else {
      ElMessage.error(res.data.data)
    }
  })


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
</style>