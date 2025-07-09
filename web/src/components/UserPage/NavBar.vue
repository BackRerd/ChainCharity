<template>
  <el-aside class="nav-bar" width="200px">
    <el-menu
        :default-active="activeMenu"
        router
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        class="el-menu-vertical"
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
        <el-menu-item index="/user/user/announcement">
          <el-icon><WarningFilled /></el-icon>
          <span>最新公告</span>
        </el-menu-item>
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
  height: 100vh; /* 改为100vh占满整个视窗高度 */
  position: sticky; /* 添加粘性定位 */
  top: 0; /* 粘性定位顶部 */
  overflow-y: auto; /* 添加滚动条以防内容过多 */
}

.el-menu {
  border-right: none;
  height: 100%; /* 让菜单充满父容器 */
}

.el-menu-vertical {
  min-height: 100vh; /* 最小高度为视窗高度 */
}

.el-menu-item.is-active {
  background-color: #434a50 !important;
}
</style>