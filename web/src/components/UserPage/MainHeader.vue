<template>
  <el-header class="main-header">
    <div class="header-left">
      <span class="logo">慈善平台</span>
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentRouteName }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="header-right">
      <el-dropdown>
        <span class="user-info">
          <el-avatar :size="30" :src="userAvatar" />
          <span class="user-name">{{ userName }}</span>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>个人中心</el-dropdown-item>
            <el-dropdown-item>设置</el-dropdown-item>
            <el-dropdown-item @click="exit">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-header>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import router from "@/router/index.js";
const exit = () => {
  localStorage.setItem("user",null)
  localStorage.setItem("token",null)
  router.push('/')
}
const route = useRoute()
const currentRouteName = computed(() => route.meta.title || '')
const userName = JSON.parse(localStorage.getItem("user"))?.name ?? '爱心用户'
const userAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
</script>

<style scoped>
.main-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #409EFF;
  color: white;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12);
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  margin-right: 20px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.user-name {
  margin-left: 10px;
}

.el-breadcrumb {
  line-height: 1;
}

.el-breadcrumb :deep(.el-breadcrumb__inner) {
  color: white !important;
}

.el-breadcrumb :deep(.el-breadcrumb__separator) {
  color: white !important;
}
</style>