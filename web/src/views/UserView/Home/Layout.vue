<template>
  <div class="app-container">
    <!-- 顶部导航栏 -->
    <MainHeader />

    <div class="main-content">
      <!-- 侧边导航栏 -->
      <NavBar />

      <!-- 主内容区 -->
      <div class="content-wrapper">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import MainHeader from '@/components/UserPage/MainHeader.vue'
import NavBar from '@/components/UserPage/NavBar.vue'
</script>

<style scoped>
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-content {
  display: flex;
  flex: 1;
}

.content-wrapper {
  flex: 1;
  padding: 20px;
  background-color: #f5f7fa;
}
</style>