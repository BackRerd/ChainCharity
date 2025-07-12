<template>
  <div class="header-container">
    <div class="header-left">
      <el-button
          @click="$emit('toggle-collapse')"
          :icon="isCollapse ? Expand : Fold"
          circle
          plain
          size="small"
          class="collapse-btn"
      />
      <div class="header-title-container">
        <span class="header-title">慈善平台管理系统</span>
        <span class="header-subtitle">用爱心连接世界</span>
      </div>
    </div>

    <div class="header-right">
      <el-dropdown>
        <div class="user-info">
          <el-avatar :size="34" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" class="user-avatar" />
          <div class="user-details">
            <span class="user-name">{{ data.user.name || '管理员' }}</span>
            <span class="user-role">慈善管理员</span>
          </div>
        </div>
        <template #dropdown>
          <el-dropdown-menu class="user-dropdown">
            <el-dropdown-item class="dropdown-item">
              <el-icon><User /></el-icon>
              <span>个人中心</span>
            </el-dropdown-item>
            <el-dropdown-item class="dropdown-item">
              <el-icon><Setting /></el-icon>
              <span>系统设置</span>
            </el-dropdown-item>
            <el-dropdown-item @click="exit" divided class="dropdown-item exit-item">
              <el-icon><SwitchButton /></el-icon>
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { Fold, Expand, User, Setting, SwitchButton } from '@element-plus/icons-vue'
import router from "@/router/index.js";
import {ref} from "vue";

const data = ref({
  user: JSON.parse(localStorage.getItem('user')),
})

const exit = () => {
  localStorage.setItem("token",null)
  localStorage.setItem("user",null)
  router.push('/auth')
}

defineProps({
  isCollapse: Boolean
})

defineEmits(['toggle-collapse'])
</script>

<style scoped>
.header-container {
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, var(--text-white) 0%, var(--sidebar-bg-start) 100%);
  box-shadow: 0 2px 10px var(--primary-shadow);
  padding: 0 20px;
  border-bottom: 1px solid var(--sidebar-border);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.header-title-container {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--sidebar-active);
  letter-spacing: 0.5px;
}

.header-subtitle {
  font-size: 12px;
  color: var(--sidebar-icon);
  opacity: 0.8;
}

.collapse-btn {
  border-color: var(--sidebar-border);
  color: var(--sidebar-active);
}

.collapse-btn:hover {
  background-color: var(--sidebar-hover-bg);
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.user-info:hover {
  background-color: var(--sidebar-hover-bg);
}

.user-avatar {
  background-color: var(--sidebar-icon);
}

.user-details {
  display: flex;
  flex-direction: column;
  margin-left: 10px;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--sidebar-active);
}

.user-role {
  font-size: 12px;
  color: var(--sidebar-icon);
}

/* 下拉菜单样式 */
.user-dropdown {
  border-radius: 8px;
  border: 1px solid var(--sidebar-border);
  box-shadow: 0 4px 12px var(--primary-shadow);
}

.dropdown-item {
  padding: 8px 16px;
  color: var(--sidebar-active);
  display: flex;
  align-items: center;
  gap: 8px;
}

.dropdown-item:hover {
  background-color: var(--sidebar-hover-bg);
  color: var(--sidebar-active);
}

.exit-item {
  color: var(--donate-btn-bg);
}

.exit-item:hover {
  background-color: var(--donate-btn-hover);
  color: var(--text-white);
}
</style>