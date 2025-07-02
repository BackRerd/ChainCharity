<template>
  <div class="home-container">
    <div class="header">
      <div class="logo">
        <i class="el-icon-user-solid"></i>
      </div>
      <h1>欢迎来到爱心慈善平台</h1>
      <p>让爱心传递，让温暖延续</p>
    </div>

    <div class="content">
      <el-card class="welcome-card">
        <div class="welcome-content">
          <h2>感谢您登录爱心慈善平台</h2>
          <p>在这里，您可以：</p>
          <ul>
            <li>参与慈善项目捐赠</li>
            <li>发起您自己的慈善项目</li>
            <li>查看善款使用情况</li>
            <li>加入志愿者团队</li>
          </ul>

          <div class="stats">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="stat-card">
                  <div class="stat-value">1,258</div>
                  <div class="stat-label">进行中项目</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="stat-card">
                  <div class="stat-value">¥8,652,417</div>
                  <div class="stat-label">已筹集善款</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="stat-card">
                  <div class="stat-value">24,598</div>
                  <div class="stat-label">爱心人士</div>
                </div>
              </el-col>
            </el-row>
          </div>

          <el-button type="primary" size="large" @click="exploreProjects">探索项目</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const exploreProjects = () => {
  ElMessage.info('项目浏览功能正在开发中')
}
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.header {
  text-align: center;
  margin-bottom: 40px;
}

.header h1 {
  font-size: 36px;
  color: var(--text-color);
  margin-bottom: 10px;
}

.header p {
  font-size: 18px;
  color: #7f8c8d;
}

.welcome-card {
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  background: var(--card-bg);
}

.welcome-content {
  padding: 30px;
  text-align: center;
}

.welcome-content h2 {
  margin-bottom: 20px;
  color: var(--text-color);
}

.welcome-content p {
  margin-bottom: 15px;
  font-size: 18px;
}

.welcome-content ul {
  text-align: left;
  max-width: 500px;
  margin: 0 auto 30px;
}

.welcome-content ul li {
  margin-bottom: 10px;
  font-size: 16px;
}

.stats {
  margin: 40px 0;
}

.stat-card {
  background: var(--light-bg);
  border-radius: 12px;
  padding: 20px;
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: var(--primary-color);
  margin-bottom: 10px;
}

.stat-label {
  font-size: 14px;
  color: #7f8c8d;
}

.el-button {
  padding: 12px 40px;
  font-size: 16px;
  border-radius: 12px;
}
</style>