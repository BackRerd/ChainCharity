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
  background: linear-gradient(135deg, #fff 0%, #fff9f5 100%);
  box-shadow: 0 2px 10px rgba(255, 152, 0, 0.1);
  padding: 0 20px;
  border-bottom: 1px solid #ffe0b2;
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
  color: #e65100; /* 深橙色 */
  letter-spacing: 0.5px;
}

.header-subtitle {
  font-size: 12px;
  color: #fb8c00; /* 浅橙色 */
  opacity: 0.8;
}

.collapse-btn {
  border-color: #ffcc80;
  color: #e65100;
}

.collapse-btn:hover {
  background-color: #ffe0b2;
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
  background-color: #ffecb3;
}

.user-avatar {
  background-color: #fb8c00;
}

.user-details {
  display: flex;
  flex-direction: column;
  margin-left: 10px;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: #e65100;
}

.user-role {
  font-size: 12px;
  color: #ffa726;
}

/* 下拉菜单样式 */
.user-dropdown {
  border-radius: 8px;
  border: 1px solid #ffcc80;
  box-shadow: 0 4px 12px rgba(255, 152, 0, 0.15);
}

.dropdown-item {
  padding: 8px 16px;
  color: #e65100;
  display: flex;
  align-items: center;
  gap: 8px;
}

.dropdown-item:hover {
  background-color: #ffecb3;
  color: #bf360c;
}

.exit-item {
  color: #d84315;
}

.exit-item:hover {
  background-color: #ffccbc;
}
</style>