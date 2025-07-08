<template>
  <el-aside class="nav-bar" width="200px">
    <el-menu
        :default-active="activeMenu"
        router
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
    >
      <el-menu-item index="/">
        <el-icon><HomeFilled /></el-icon>
        <span>首页</span>
      </el-menu-item>

      <el-sub-menu index="/user/home">
        <template #title>
          <el-icon><Menu /></el-icon>
          <span>首页功能</span>
        </template>
        <el-menu-item index="/user/posts">
          <el-icon><Document /></el-icon>
          <span>最新动态</span>
        </el-menu-item>
        <el-menu-item index="/user/emergency">
          <el-icon><WarningFilled /></el-icon>
          <span>紧急活动</span>
        </el-menu-item>
      </el-sub-menu>

      <el-menu-item index="/user/donate">
        <el-icon><Present /></el-icon>
        <span>我要捐赠</span>
      </el-menu-item>

      <el-menu-item index="/user/assistance">
        <el-icon><User /></el-icon>
        <span>我的受助</span>
      </el-menu-item>
    </el-menu>
  </el-aside>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import {
  HomeFilled,
  Menu,
  Document,
  WarningFilled as WarningFilled,
  Present,
  User
} from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style scoped>
.nav-bar {
  background-color: #545c64;
  color: white;
  height: calc(100vh - 60px);
}

.el-menu {
  border-right: none;
}

.el-menu-item.is-active {
  background-color: #434a50 !important;
}
</style>