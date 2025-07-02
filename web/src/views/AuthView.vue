<template>
  <div class="auth-container">
    <div class="background-image"></div>

    <div class="floating-hearts">
      <div v-for="i in 15" :key="i" class="heart" :style="heartStyle(i)">❤</div>
    </div>

    <div class="leaf" v-for="i in 3" :key="'leaf'+i" :style="leafStyle(i)"></div>

    <div class="auth-card">
      <div class="auth-header">
        <div class="logo">
          <i class="el-icon-user-solid"></i>
        </div>
        <h1>爱心慈善平台</h1>
        <p>让爱心传递，让温暖延续</p>
      </div>

      <div class="form-container">
        <transition name="fade" mode="out-in">
          <LoginForm v-if="currentForm === 'login'" @switch-form="switchForm" />
          <RegisterForm v-else @switch-form="switchForm" />
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import LoginForm from '@/components/LoginForm.vue'
import RegisterForm from '@/components/RegisterForm.vue'

export default {
  components: {
    LoginForm,
    RegisterForm
  },
  setup() {
    const currentForm = ref('login')

    const switchForm = (form) => {
      currentForm.value = form
    }

    const heartStyle = (i) => {
      return {
        left: Math.random() * 100 + 'vw',
        animationDuration: (Math.random() * 5 + 5) + 's',
        animationDelay: Math.random() * 5 + 's'
      }
    }

    const leafStyle = (i) => {
      const positions = [
        { top: '10%', left: '10%', duration: 15 },
        { top: '20%', right: '15%', duration: 18 },
        { bottom: '15%', left: '20%', duration: 12 }
      ]

      const colors = ['#5cd6d6', '#5c9ce6', '#ff9a3d']

      return {
        ...positions[i-1],
        animationDuration: positions[i-1].duration + 's',
        backgroundColor: colors[i-1]
      }
    }

    return {
      currentForm,
      switchForm,
      heartStyle,
      leafStyle
    }
  }
}
</script>

<style scoped>
.auth-container {
  position: relative;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}

.background-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('@/assets/images/img.png');
  background-size: cover;
  background-position: center;
  filter: blur(5px);
  opacity: 0.5;
  z-index: 1;
}

.floating-hearts {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  pointer-events: none;
  z-index: 2;
}

.auth-card {
  position: relative;
  z-index: 3;
  width: 380px;
  background: var(--card-bg);
  border-radius: 20px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  padding: 40px 30px;
  transition: all 0.5s ease;
}

.auth-header {
  text-align: center;
  margin-bottom: 30px;
  position: relative;
}

.auth-header h1 {
  font-size: 28px;
  color: var(--text-color);
  margin-bottom: 10px;
  font-weight: 600;
}

.auth-header p {
  color: #7f8c8d;
  font-size: 16px;
}

.auth-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

.leaf {
  position: absolute;
  width: 30px;
  height: 30px;
  background-size: contain;
  background-repeat: no-repeat;
  opacity: 0.7;
  animation: float 10s linear infinite;
  z-index: 1;
  border-radius: 50%;
  clip-path: path("M50,10 C70,5 90,20 85,40 C80,60 60,70 40,85 C20,70 5,50 15,30 C25,10 40,15 50,10 Z");
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 480px) {
  .auth-card {
    width: 90%;
    padding: 30px 20px;
  }
}
</style>