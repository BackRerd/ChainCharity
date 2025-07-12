<template>
  <el-header class="main-header">
    <div class="header-left">
      <div class="logo-container" @click="goHome">
        <span class="logo-icon">❤️</span>
        <span class="logo-text">爱心慈善平台</span>
      </div>
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">爱心首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentRouteName }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="header-right">
      <div class="donate-btn" @click="navigateToDonate">
        <el-icon><Present /></el-icon>
        <span>立即捐赠</span>
      </div>
      <el-dropdown trigger="hover">
        <span class="user-info">
          <el-avatar :size="36" :src="userAvatar" class="user-avatar" />
          <span class="user-name">{{ userName }}</span>
          <el-icon class="user-arrow"><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu class="user-dropdown">
            <el-dropdown-item class="dropdown-item" @click="navigateToProfile">
              <el-icon><User /></el-icon>
              <span>个人中心</span>
            </el-dropdown-item>
            <el-dropdown-item class="dropdown-item" @click="navigateToSettings">
              <el-icon><Setting /></el-icon>
              <span>账户设置</span>
            </el-dropdown-item>
            <el-dropdown-item divided class="dropdown-item" @click="exit">
              <el-icon><SwitchButton /></el-icon>
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-header>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  User,
  Setting,
  SwitchButton,
  Present,
  ArrowDown
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const goHome = () => {
  router.push('/')
}

const exit = () => {
  localStorage.setItem("user", null)
  localStorage.setItem("token", null)
  router.push('/login')
}

const navigateToDonate = () => {
  router.push('/donate')
}

const navigateToProfile = () => {
  router.push('/profile')
}

const navigateToSettings = () => {
  router.push('/settings')
}

const currentRouteName = computed(() => route.meta.title || '')
const userName = JSON.parse(localStorage.getItem("user"))?.name || '爱心用户'
const userAvatar = computed(() => {
  return JSON.parse(localStorage.getItem("user"))?.avatar ||
      'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
})
</script>
<style scoped>
.main-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, var(--primary-gradient-start), var(--primary-gradient-end));
  color: var(--text-white);
  padding: 0 24px;
  height: 64px;
  box-shadow: 0 2px 15px var(--primary-shadow);
  position: relative;
  z-index: 999;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 30px;
}

.logo-container {
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.logo-container:hover {
  transform: scale(1.03);
}

.logo-container:hover .logo-icon {
  animation: pulse 0.8s ease;
}

.logo-icon {
  font-size: 26px;
  margin-right: 8px;
  color: var(--text-yellow);
}

.logo-text {
  font-size: 21px;
  font-weight: bold;
  letter-spacing: 1px;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
}

.breadcrumb {
  font-size: 15px;
}

.breadcrumb :deep(.el-breadcrumb__inner) {
  color: rgba(255, 255, 255, 0.95) !important;
  transition: all 0.2s ease;
}

.breadcrumb :deep(.el-breadcrumb__inner:hover) {
  color: var(--text-light-yellow) !important;
  text-decoration: underline;
}

.breadcrumb :deep(.el-breadcrumb__separator) {
  color: var(--breadcrumb-separator) !important;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 25px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 24px;
  transition: all 0.3s ease;
  background-color: var(--user-info-bg);
}

.user-info:hover {
  background-color: var(--user-info-hover);
}

.user-info:hover .user-arrow {
  transform: translateY(3px);
}

.user-avatar {
  border: 2px solid var(--avatar-border);
  transition: all 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.1);
  border-color: var(--avatar-border-hover);
}

.user-name {
  margin: 0 10px;
  font-size: 15px;
  font-weight: 500;
}

.user-arrow {
  transition: all 0.3s ease;
  font-size: 14px;
  color: var(--text-yellow);
}

.user-dropdown {
  border: none;
  padding: 8px 0;
  background-color: var(--dropdown-bg);
  box-shadow: 0 5px 20px var(--dropdown-shadow);
  border-radius: 12px;
  overflow: hidden;
  margin-top: 5px;
}

.dropdown-item {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  color: var(--text-gray);
  transition: all 0.2s ease;
}

.dropdown-item:hover {
  background-color: var(--dropdown-item-hover);
  color: var(--dropdown-item-hover-text);
}

.dropdown-item:hover .el-icon {
  color: var(--dropdown-item-hover-text);
}

.dropdown-item .el-icon {
  margin-right: 10px;
  font-size: 17px;
  color: var(--dropdown-icon);
}

.donate-btn {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  background-color: var(--donate-btn-bg);
  color: var(--text-white);
  border-radius: 24px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  font-size: 15px;
  font-weight: 600;
  box-shadow: 0 4px 15px var(--donate-btn-shadow);
}

.donate-btn:hover {
  background-color: var(--donate-btn-hover);
  transform: translateY(-3px);
  box-shadow: 0 7px 20px var(--donate-btn-shadow-hover);
}

.donate-btn:active {
  transform: translateY(-1px);
}

.donate-btn .el-icon {
  margin-right: 8px;
  font-size: 18px;
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.2); }
  100% { transform: scale(1); }
}
</style>