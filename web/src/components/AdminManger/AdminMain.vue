<template>
  <el-container class="app-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '200px'" class="app-sidebar">
      <Sidebar :isCollapse="isCollapse" />
    </el-aside>

    <el-container>
      <!-- 顶部导航栏 -->
      <el-header class="app-header">
        <AppHeader
            :isCollapse="isCollapse"
            @toggle-collapse="toggleCollapse"
        />
      </el-header>

      <!-- 主内容区 -->
      <el-main class="app-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { ref } from 'vue'
import Sidebar from '@/components/AdminManger/Sidebar.vue'
import AppHeader from '@/components/AdminManger/AppHeader.vue'

export default {
  name: 'App',
  components: { Sidebar, AppHeader },
  setup() {
    const isCollapse = ref(false)


    const toggleCollapse = () => {
      isCollapse.value = !isCollapse.value
    }

    return { isCollapse, toggleCollapse }
  }
}
</script>

<style>
/* 全局样式 */
:root {
  --primary-color: #409EFF;
  --charity-primary: #5D9CEC; /* 慈善蓝 */
  --charity-secondary: #A0D468; /* 慈善绿 */
  --charity-accent: #FFCE54; /* 慈善黄 */
}

body {
  margin: 0;
  padding: 0;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

.app-container {
  height: 100vh;
}

.app-sidebar {
  width: 240px;
  background-color: #304156;
  transition: width 0.3s;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
}

.app-header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
}

.app-main {
  background-color: #f0f2f5;
  padding: 20px;
  min-height: calc(100vh - 60px);
}
</style>