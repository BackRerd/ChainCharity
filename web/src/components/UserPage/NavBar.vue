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
          <span>紧急救助</span>
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

const router = useRouter()
const route = useRoute()

const goHome = () => {
  router.push('/')
}

const activeMenu = computed(() => route.path)
</script>

<style scoped lang="scss">
.nav-bar {
  background: linear-gradient(to bottom, #f8f1e9 0%, #f1d4c3 100%);
  color: #5a5a5a;
  height: 100vh;
  position: sticky;
  top: 0;
  overflow-y: auto;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  z-index: 998;
}

.logo-container {
  display: flex;
  align-items: center;
  padding: 20px 15px;
  background-color: rgba(209, 102, 84, 0.1);
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-bottom: 1px solid rgba(209, 102, 84, 0.1);

  &:hover {
    background-color: rgba(209, 102, 84, 0.15);
  }
}

.logo {
  width: 200px;
  height: 200px;
  margin-right: 12px;
}

.org-name {
  color: #d16654;
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
  background-color: rgba(255, 255, 255, 0.3);
  color: #5a5a5a;

  &:hover {
    background-color: rgba(209, 102, 84, 0.1) !important;
    transform: translateX(3px);
  }

  &.is-active {
    background-color: rgba(209, 102, 84, 0.15) !important;
    color: #d16654;
    font-weight: 500;

    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 0;
      height: 100%;
      width: 3px;
      background-color: #d16654;
    }

    .menu-icon {
      color: #d16654;
    }
  }
}

.hover-effect {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(209, 102, 84, 0.1), transparent);
  transition: all 0.6s ease;
}

.menu-item:hover .hover-effect {
  left: 100%;
}

.menu-icon {
  margin-right: 12px;
  font-size: 18px;
  color: #d16654;
}

.menu-text {
  font-size: 15px;
}

.submenu-item {
  transition: all 0.3s ease;
  background-color: rgba(255, 255, 255, 0.4) !important;

  &:hover {
    background-color: rgba(209, 102, 84, 0.08) !important;
    padding-left: 18px !important;
  }

  &.is-active {
    background-color: rgba(209, 102, 84, 0.1) !important;
    color: #d16654;
  }

  .el-icon {
    color: #d16654;
  }
}

.pulse-dot {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  width: 8px;
  height: 8px;
  background-color: #d16654;
  border-radius: 50%;
  opacity: 0.7;
  animation: pulse 2s infinite;
}

.donation-counter {
  margin: 25px 15px;
  padding: 18px;
  background-color: rgba(255, 255, 255, 0.4);
  border-radius: 8px;
  text-align: center;
  animation: fadeIn 1s ease;
  border: 1px solid rgba(209, 102, 84, 0.2);
}

.counter-title {
  font-size: 15px;
  color: #5a5a5a;
  margin-bottom: 8px;
}

.counter-number {
  font-size: 24px;
  font-weight: bold;
  color: #d16654;
  margin-bottom: 5px;

  span {
    font-size: 14px;
    margin-left: 4px;
    color: #888;
  }
}

.counter-sub {
  font-size: 13px;
  color: #888;
  margin-bottom: 15px;
}

.progress-bar {
  height: 5px;
  background-color: rgba(209, 102, 84, 0.1);
  border-radius: 3px;
  margin: 12px 0;
  overflow: hidden;
}

.progress {
  height: 100%;
  background: linear-gradient(to right, #f8b195, #d16654);
  border-radius: 3px;
  transition: width 1s ease;
}

.counter-goal {
  font-size: 12px;
  color: #888;
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
  background-color: rgba(209, 102, 84, 0.3);
  border-radius: 3px;

  &:hover {
    background-color: rgba(209, 102, 84, 0.5);
  }
}
</style>