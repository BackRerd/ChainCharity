<template>
  <div class="header-container">
    <div class="header-left">
      <el-button
          @click="$emit('toggle-collapse')"
          :icon="isCollapse ? Expand : Fold"
          circle
          plain
          size="small"
      />
      <span class="header-title">慈善平台管理系统</span>
    </div>

    <div class="header-right">
      <el-dropdown>
        <div class="user-info">
          <el-avatar :size="30" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
          <span class="user-name">{{ data.user.name || '管理员' }}</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>个人中心</el-dropdown-item>
            <el-dropdown-item>系统设置</el-dropdown-item>
            <el-dropdown-item @click="exit" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { Fold, Expand } from '@element-plus/icons-vue'
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
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-title {
  margin-left: 15px;
  font-size: 18px;
  color: var(--charity-primary);
  font-weight: 600;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.user-name {
  margin-left: 10px;
  font-size: 14px;
}
</style>