<template>
  <el-aside class="nav-bar" width="240px">
    <ChatAI />
    <div class="logo-container" @click="goHome">
      <!--      <img src="@/assets/images/logo.png" alt="慈善Logo" class="logo" />-->
      <h2 class="org-name">感谢您的使用！</h2>
    </div>
    <el-menu
        :default-active="activeMenu"
        router
        background-color="transparent"
        text-color="#5a5a5a"
        active-text-color="#d16654"
        class="el-menu-vertical"
    >
      <el-menu-item index="/" class="menu-item">
        <el-icon class="menu-icon"><HomeFilled /></el-icon>
        <span class="menu-text">爱心首页</span>
        <div class="hover-effect"></div>
      </el-menu-item>

      <el-sub-menu index="/user/services" class="menu-item">
        <template #title>
          <el-icon class="menu-icon"><Menu /></el-icon>
          <span class="menu-text">爱心服务</span>
        </template>
        <el-menu-item index="/user/announcement" class="submenu-item">
          <el-icon><BellFilled /></el-icon>
          <span>慈善公告</span>
          <div class="pulse-dot"></div>
        </el-menu-item>
        <el-menu-item index="/user/posts" class="submenu-item">
          <el-icon><Calendar /></el-icon>
          <span>爱心活动</span>
        </el-menu-item>
        <el-menu-item index="/user/forum" class="submenu-item">
          <el-icon><Warning /></el-icon>
          <span>论坛查看</span>
        </el-menu-item>
      </el-sub-menu>

      <el-menu-item index="/user/donate" class="menu-item">
        <el-icon class="menu-icon"><Present /></el-icon>
        <span class="menu-text">爱心捐赠</span>
        <div class="hover-effect"></div>
      </el-menu-item>

      <el-menu-item index="/user/assistance" class="menu-item">
        <el-icon class="menu-icon"><User /></el-icon>
        <span class="menu-text">我的受助</span>
        <div class="hover-effect"></div>
      </el-menu-item>

      <div class="donation-counter">
        <div class="counter-title">爱心累计</div>
        <div class="counter-number">1,283,456<span>元</span></div>
        <div class="counter-sub">已帮助 2,456 人</div>
        <div class="progress-bar">
          <div class="progress" :style="{width: '75%'}"></div>
        </div>
        <div class="counter-goal">目标: 2,000,000元</div>

      </div>
    </el-menu>
  </el-aside>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import ChatAI from '@/components/UserPage/ChatAI.vue'
import {
  HomeFilled,
  Menu,
  BellFilled,
  Calendar,
  Present,
  User,
  Warning
} from '@element-plus/icons-vue'
import ThemePalette from "@/components/UserPage/ThemePalette.vue";

const router = useRouter()
const route = useRoute()

const goHome = () => {
  router.push('/')
}

const activeMenu = computed(() => route.path)
</script>

<style scoped>
.nav-bar {
  background: linear-gradient(to bottom, var(--sidebar-bg-start) 0%, var(--sidebar-bg-end) 100%);
  color: var(--sidebar-text);
  height: 100vh;
  position: sticky;
  top: 0;
  overflow-y: auto;
  box-shadow: 2px 0 10px var(--sidebar-shadow);
  transition: all 0.3s ease;
  z-index: 998;
}

.logo-container {
  display: flex;
  align-items: center;
  padding: 20px 15px;
  background-color: var(--sidebar-active-bg);
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-bottom: 1px solid var(--sidebar-border);
}

.logo-container:hover {
  background-color: var(--sidebar-hover-bg);
}

.logo {
  width: 200px;
  height: 200px;
  margin-right: 12px;
}

.org-name {
  color: var(--sidebar-active);
  font-size: 20px;
  font-weight: bold;
  margin: 0;
}

.el-menu {
  border-right: none;
  height: 100%;
  background-color: transparent !important;
}

.el-menu-vertical {
  min-height: calc(100vh - 80px);
}

.menu-item {
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  margin: 6px 10px;
  border-radius: 6px;
  background-color: var(--menu-item-bg);
  color: var(--sidebar-text);
}

.menu-item:hover {
  background-color: var(--sidebar-hover-bg) !important;
  transform: translateX(3px);
}

.menu-item.is-active {
  background-color: var(--sidebar-active-bg) !important;
  color: var(--sidebar-active);
  font-weight: 500;
}

.menu-item.is-active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  width: 3px;
  background-color: var(--menu-active-indicator);
}

.menu-item.is-active .menu-icon {
  color: var(--sidebar-icon);
}

.hover-effect {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, var(--sidebar-hover-bg), transparent);
  transition: all 0.6s ease;
}

.menu-item:hover .hover-effect {
  left: 100%;
}

.menu-icon {
  margin-right: 12px;
  font-size: 18px;
  color: var(--sidebar-icon);
}

.menu-text {
  font-size: 15px;
}

.submenu-item {
  transition: all 0.3s ease;
  background-color: var(--submenu-item-bg) !important;
}

.submenu-item:hover {
  background-color: rgba(209, 102, 84, 0.08) !important;
  padding-left: 18px !important;
}

.submenu-item.is-active {
  background-color: var(--sidebar-active-bg) !important;
  color: var(--sidebar-active);
}

.submenu-item .el-icon {
  color: var(--sidebar-icon);
}

.pulse-dot {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  width: 8px;
  height: 8px;
  background-color: var(--sidebar-active);
  border-radius: 50%;
  opacity: 0.7;
  animation: pulse 2s infinite;
}

.donation-counter {
  margin: 25px 15px;
  padding: 18px;
  background-color: var(--submenu-item-bg);
  border-radius: 8px;
  text-align: center;
  animation: fadeIn 1s ease;
  border: 1px solid var(--sidebar-border);
}

.counter-title {
  font-size: 15px;
  color: var(--sidebar-text);
  margin-bottom: 8px;
}

.counter-number {
  font-size: 24px;
  font-weight: bold;
  color: var(--sidebar-active);
  margin-bottom: 5px;
}

.counter-number span {
  font-size: 14px;
  margin-left: 4px;
  color: var(--text-light-gray);
}

.counter-sub {
  font-size: 13px;
  color: var(--text-light-gray);
  margin-bottom: 15px;
}

.progress-bar {
  height: 5px;
  background-color: var(--progress-bg);
  border-radius: 3px;
  margin: 12px 0;
  overflow: hidden;
}

.progress {
  height: 100%;
  background: linear-gradient(to right, var(--progress-start), var(--progress-end));
  border-radius: 3px;
  transition: width 1s ease;
}

.counter-goal {
  font-size: 12px;
  color: var(--text-light-gray);
}

@keyframes pulse {
  0% { transform: translateY(-50%) scale(1); opacity: 0.7; }
  50% { transform: translateY(-50%) scale(1.2); opacity: 1; }
  100% { transform: translateY(-50%) scale(1); opacity: 0.7; }
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.nav-bar::-webkit-scrollbar {
  width: 5px;
}

.nav-bar::-webkit-scrollbar-thumb {
  background-color: var(--scrollbar-thumb);
  border-radius: 3px;
}

.nav-bar::-webkit-scrollbar-thumb:hover {
  background-color: var(--scrollbar-thumb-hover);
}
</style>