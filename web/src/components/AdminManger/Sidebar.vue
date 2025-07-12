<template>
  <el-aside class="nav-bar" width="240px">
    <div class="logo-container">
      <h2 class="org-name">慈善管理系统</h2>
    </div>
    <el-menu
        :default-active="activeMenu"
        router
        background-color="transparent"
        text-color="#5a5a5a"
        active-text-color="#d16654"
        class="el-menu-vertical"
    >
      <el-menu-item index="/admin/dashboard" class="menu-item">
        <el-icon class="menu-icon"><DataAnalysis /></el-icon>
        <span class="menu-text">数据概览</span>
        <div class="hover-effect"></div>
      </el-menu-item>

<!--      <el-menu-item index="/admin/beneficiaries" class="menu-item">-->
<!--        <el-icon class="menu-icon"><UserFilled /></el-icon>-->
<!--        <span class="menu-text">受助者管理</span>-->
<!--        <div class="hover-effect"></div>-->
<!--      </el-menu-item>-->

      <el-menu-item index="/admin/settings" class="menu-item">
        <el-icon class="menu-icon"><Setting /></el-icon>
        <span class="menu-text">系统设置</span>
        <div class="hover-effect"></div>
      </el-menu-item>

      <el-sub-menu index="activity" class="menu-item">
        <template #title>
          <el-icon class="menu-icon"><Calendar /></el-icon>
          <span class="menu-text">活动管理</span>
        </template>
        <el-menu-item index="/admin/activity/type" class="submenu-item">
          <el-icon><List /></el-icon>
          <span>活动类型</span>
        </el-menu-item>
        <el-menu-item index="/admin/activity" class="submenu-item">
          <el-icon><Management /></el-icon>
          <span>活动管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/activity/participation" class="submenu-item">
          <el-icon><Checked /></el-icon>
          <span>参与活动审核</span>
        </el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="distributions" class="menu-item">
        <template #title>
          <el-icon class="menu-icon"><Box /></el-icon>
          <span class="menu-text">物资管理</span>
        </template>
        <el-menu-item index="/admin/distributions" class="submenu-item">
          <el-icon><Share /></el-icon>
          <span>物资分配管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/item/donations" class="submenu-item">
          <el-icon><Present /></el-icon>
          <span>物品捐赠管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/money/donations" class="submenu-item">
          <el-icon><Money /></el-icon>
          <span>资金捐赠管理</span>
        </el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="users" class="menu-item">
        <template #title>
          <el-icon class="menu-icon"><User /></el-icon>
          <span class="menu-text">其他管理</span>
        </template>
        <el-menu-item index="/admin/volunteers" class="submenu-item">
          <el-icon><Avatar /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/announcement" class="submenu-item">
          <el-icon><Bell /></el-icon>
          <span>公告管理</span>
        </el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="post" class="menu-item">
        <template #title>
          <el-icon class="menu-icon"><Document /></el-icon>
          <span class="menu-text">帖子管理</span>
        </template>
        <el-menu-item index="/admin/forum/categories" class="submenu-item">
          <el-icon><Folder /></el-icon>
          <span>帖子分类</span>
        </el-menu-item>
        <el-menu-item index="/admin/forum/posts" class="submenu-item">
          <el-icon><DocumentCopy /></el-icon>
          <span>帖子管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/forum/replies" class="submenu-item">
          <el-icon><ChatLineRound /></el-icon>
          <span>帖子回复</span>
        </el-menu-item>
        <el-menu-item index="/admin/forum/likes" class="submenu-item">
          <el-icon><Star /></el-icon>
          <span>帖子点赞</span>
        </el-menu-item>
        <el-menu-item index="/admin/forum/attachments" class="submenu-item">
          <el-icon><Paperclip /></el-icon>
          <span>帖子附件</span>
        </el-menu-item>
      </el-sub-menu>
    </el-menu>
  </el-aside>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import {
  DataAnalysis, UserFilled, Setting, Calendar, List,
  Management, Checked, Box, Share, Present,
  Money, Avatar, Bell, Document, Folder,
  DocumentCopy, ChatLineRound, Star, Paperclip
} from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style scoped lang="scss">
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
  justify-content: center;
  padding: 20px 15px;
  background-color: var(--sidebar-active-bg);
  margin-bottom: 10px;
  transition: all 0.3s ease;
  border-bottom: 1px solid var(--sidebar-border);
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

  &:hover {
    background-color: var(--sidebar-hover-bg) !important;
    transform: translateX(3px);
  }

  &.is-active {
    background-color: var(--sidebar-active-bg) !important;
    color: var(--sidebar-active);
    font-weight: 500;

    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 0;
      height: 100%;
      width: 3px;
      background-color: var(--menu-active-indicator);
    }

    .menu-icon {
      color: var(--sidebar-icon);
    }
  }
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

  &:hover {
    background-color: rgba(var(--sidebar-active), 0.08) !important;
    padding-left: 18px !important;
  }

  &.is-active {
    background-color: var(--sidebar-hover-bg) !important;
    color: var(--sidebar-active);
  }

  .el-icon {
    color: var(--sidebar-icon);
  }
}

.donation-counter {
  margin: 25px 15px;
  padding: 18px;
  background-color: var(--menu-item-bg);
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

  span {
    font-size: 14px;
    margin-left: 4px;
    color: var(--text-light-gray);
  }
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

  &:hover {
    background-color: var(--scrollbar-thumb-hover);
  }
}
</style>