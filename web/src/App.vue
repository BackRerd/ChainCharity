<template>
  <div class="app-container">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+SC:wght@300;400;500;700&display=swap');

:root {
  --primary-color: #5c9ce6;
  --secondary-color: #5cd6d6;
  --accent-color: #ff9a3d;
  --text-color: #2c3e50;
  --light-bg: #f8f9fa;
  --card-bg: rgba(255, 255, 255, 0.92);
  --success-color: #67c23a;
  --error-color: #f56c6c;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Noto Sans SC', sans-serif;
  background: linear-gradient(135deg, #e8f4f8, #f8f4e8);
  height: 100vh;
  overflow: hidden;
  color: var(--text-color);
}

.app-container {
  height: 100vh;
  overflow: auto;
}

.logo {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto 20px;
  box-shadow: 0 5px 15px rgba(92, 156, 230, 0.3);
  animation: pulse 2s infinite;
}

.logo i {
  font-size: 40px;
  color: white;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(92, 156, 230, 0.4);
  }
  70% {
    transform: scale(1.05);
    box-shadow: 0 0 0 15px rgba(92, 156, 230, 0);
  }
  100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(92, 156, 230, 0);
  }
}

.heart {
  position: absolute;
  color: #ff6b6b;
  font-size: 20px;
  opacity: 0;
  animation: float 8s linear infinite;
}

@keyframes float {
  0% {
    transform: translateY(100vh) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 0.7;
  }
  90% {
    opacity: 0.7;
  }
  100% {
    transform: translateY(-100px) rotate(360deg);
    opacity: 0;
  }
}

.el-button {
  transition: all 0.3s;
  border: none;
}

.login-btn {
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  box-shadow: 0 4px 10px rgba(92, 156, 230, 0.3);
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(92, 156, 230, 0.4);
}

.register-btn {
  background: linear-gradient(135deg, var(--accent-color), #ff6b6b);
  box-shadow: 0 4px 10px rgba(255, 107, 107, 0.3);
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(255, 107, 107, 0.4);
}
</style>