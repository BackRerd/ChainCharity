<template>
  <div class="charity-app">
    <!-- 导航栏 -->
    <nav class="charity-nav">
      <div class="container">
        <div class="logo">
          <router-link to="/">
            <img :src="logoImg" alt="爱心慈善">
            <span>爱心慈善</span>
          </router-link>
        </div>
        <ul class="nav-links">
          <li><router-link to="/">首页</router-link></li>
          <li><router-link to="/activities">活动</router-link></li>
          <li><router-link to="/articles">文章</router-link></li>
          <li><router-link to="/donate">捐赠</router-link></li>
          <li><router-link to="/about">关于我们</router-link></li>
        </ul>
        <div class="nav-buttons">
          <button class="btn-login">登录</button>
          <button class="btn-register">注册</button>
        </div>
      </div>
    </nav>

    <!-- 英雄区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">用爱心改变世界</h1>
        <p class="hero-subtitle">加入我们，为需要帮助的人们带去希望</p>
        <button class="btn-hero">立即参与</button>
      </div>
      <div class="hero-image">
        <img :src="heroImage" alt="慈善活动">
      </div>
      <div class="floating-hearts">
        <div class="heart" v-for="(heart, index) in hearts" :key="index" :style="heart.style">
          <img :src="heartIcon" alt="爱心">
        </div>
      </div>
    </section>

    <!-- 活动展示 -->
    <section class="activities-section">
      <div class="section-header">
        <h2>近期活动</h2>
        <p>参与我们的慈善活动，传递爱心</p>
      </div>
      <div class="activities-container">
        <div class="activity-card" v-for="activity in activities" :key="activity.id">
          <div class="activity-image">
            <img :src="activity.image" :alt="activity.title">
            <div class="date-badge">
              <span class="day">{{ activity.date.day }}</span>
              <span class="month">{{ activity.date.month }}</span>
            </div>
          </div>
          <div class="activity-content">
            <h3>{{ activity.title }}</h3>
            <p class="activity-desc">{{ activity.description }}</p>
            <div class="activity-meta">
              <span><i class="fas fa-map-marker-alt"></i> {{ activity.location }}</span>
              <span><i class="fas fa-users"></i> {{ activity.participants }} 人参与</span>
            </div>
            <button class="btn-join">立即报名</button>
          </div>
        </div>
      </div>
    </section>

    <!-- 文章列表 -->
    <section class="articles-section">
      <div class="section-header">
        <h2>最新文章</h2>
        <p>了解我们的慈善故事和最新动态</p>
      </div>
      <div class="articles-container">
        <div class="article-card" v-for="article in articles" :key="article.id">
          <div class="article-image">
            <img :src="article.image" :alt="article.title">
          </div>
          <div class="article-content">
            <div class="article-meta">
              <span class="category">{{ article.category }}</span>
              <span class="date">{{ article.date }}</span>
            </div>
            <h3>{{ article.title }}</h3>
            <p class="article-excerpt">{{ article.excerpt }}</p>
            <router-link :to="`/articles/${article.id}`" class="read-more">阅读更多</router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- 捐赠呼吁 -->
    <section class="donation-call">
      <div class="donation-content">
        <h2>您的捐赠可以改变一个生命</h2>
        <p>每一份爱心都将直接用于帮助需要的人们</p>
        <div class="donation-progress">
          <div class="progress-bar">
            <div class="progress" :style="{ width: donationProgress + '%' }"></div>
          </div>
          <div class="progress-text">
            <span>已筹集: ¥{{ currentAmount.toLocaleString() }}</span>
            <span>目标: ¥{{ targetAmount.toLocaleString() }}</span>
          </div>
        </div>
        <button class="btn-donate">立即捐赠</button>
      </div>
      <div class="donation-image">
        <img :src="donationImage" alt="捐赠">
      </div>
    </section>

    <!-- 志愿者故事 -->
    <section class="stories-section">
      <div class="section-header">
        <h2>志愿者故事</h2>
        <p>听听那些温暖人心的故事</p>
      </div>
      <div class="stories-container">
        <div class="story-card" v-for="story in stories" :key="story.id">
          <div class="story-image">
            <img :src="story.image" :alt="story.name">
          </div>
          <div class="story-content">
            <h3>{{ story.name }}</h3>
            <p class="story-title">{{ story.title }}</p>
            <p class="story-quote">"{{ story.quote }}"</p>
            <router-link :to="`/stories/${story.id}`" class="read-story">阅读完整故事</router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="charity-footer">
      <div class="footer-content">
        <div class="footer-about">
          <h3>关于爱心慈善</h3>
          <p>我们是一个非营利组织，致力于帮助贫困儿童、孤寡老人和需要帮助的群体。</p>
          <div class="social-links">
            <a href="#"><i class="fab fa-weixin"></i></a>
            <a href="#"><i class="fab fa-weibo"></i></a>
            <a href="#"><i class="fab fa-qq"></i></a>
            <a href="#"><i class="fab fa-douyin"></i></a>
          </div>
        </div>
        <div class="footer-links">
          <h3>快速链接</h3>
          <ul>
            <li><router-link to="/">首页</router-link></li>
            <li><router-link to="/about">关于我们</router-link></li>
            <li><router-link to="/activities">活动</router-link></li>
            <li><router-link to="/articles">文章</router-link></li>
            <li><router-link to="/donate">捐赠</router-link></li>
          </ul>
        </div>
        <div class="footer-contact">
          <h3>联系我们</h3>
          <p><i class="fas fa-map-marker-alt"></i> 北京市朝阳区慈善路88号</p>
          <p><i class="fas fa-phone"></i> 400-888-9999</p>
          <p><i class="fas fa-envelope"></i> info@aixincishan.org</p>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2023 爱心慈善组织. 保留所有权利.</p>
      </div>
      <div class="floating-doves">
        <div class="dove" v-for="(dove, index) in doves" :key="index" :style="dove.style">
          <img :src="doveIcon" alt="和平鸽">
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

// 使用网络图片URL
const logoImg = ref('https://img.icons8.com/fluency/96/charity.png')
const heroImage = ref('https://images.unsplash.com/photo-1507679799987-c73779587ccf?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80')
const donationImage = ref('https://images.unsplash.com/photo-1551524559-8af4e6624178?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80')
const heartIcon = ref('https://img.icons8.com/fluency/48/like.png')
const doveIcon = ref('https://img.icons8.com/color/48/dove.png')

// 浮动爱心效果
const hearts = ref(
    Array(10).fill().map(() => ({
      style: {
        left: `${Math.random() * 100}%`,
        top: `${Math.random() * 100}%`,
        opacity: Math.random() * 0.5 + 0.3,
        transform: `scale(${Math.random() * 0.5 + 0.5})`,
        animationDuration: `${Math.random() * 3 + 2}s`
      }
    }))
)

// 浮动鸽子效果
const doves = ref(
    Array(5).fill().map(() => ({
      style: {
        left: `${Math.random() * 100}%`,
        top: `${Math.random() * 100}%`,
        opacity: Math.random() * 0.5 + 0.3,
        animationDuration: `${Math.random() * 10 + 5}s`
      }
    }))
)

// 活动数据
const activities = ref([
  {
    id: 1,
    title: '山区儿童助学计划',
    description: '为偏远山区的儿童提供教育资源和学习用品，帮助他们获得更好的教育机会。',
    image: 'https://images.unsplash.com/photo-1523050854058-8df90110c9f1?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80',
    date: { day: '15', month: '七月' },
    location: '云南山区',
    participants: 235
  },
  {
    id: 2,
    title: '敬老院爱心慰问',
    description: '组织志愿者前往敬老院，为老人们送去温暖和关怀，陪伴他们度过愉快的一天。',
    image: 'https://images.unsplash.com/photo-1516589091380-5d6019716b86?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80',
    date: { day: '22', month: '七月' },
    location: '北京市朝阳区',
    participants: 120
  },
  {
    id: 3,
    title: '流浪动物救助行动',
    description: '救助城市中的流浪动物，为它们提供医疗、食物和寻找新的家庭。',
    image: 'https://images.unsplash.com/photo-1544568100-847a948585b9?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80',
    date: { day: '5', month: '八月' },
    location: '上海市各区',
    participants: 180
  }
])

// 文章数据
const articles = ref([
  {
    id: 1,
    title: '山区支教日记：一个志愿者的心路历程',
    excerpt: '记录了我作为志愿者在云南山区支教三个月的所见所闻和感悟...',
    image: 'https://images.unsplash.com/photo-1523050854058-8df90110c9f1?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80',
    category: '志愿者故事',
    date: '2023-06-15'
  },
  {
    id: 2,
    title: '慈善组织如何提高透明度',
    excerpt: '探讨慈善组织如何通过财务公开、项目追踪等方式提高透明度，赢得公众信任...',
    image: 'https://images.unsplash.com/photo-1450101499163-c8848c66ca85?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80',
    category: '慈善知识',
    date: '2023-06-08'
  },
  {
    id: 3,
    title: '2023年上半年慈善项目总结',
    excerpt: '回顾我们组织在2023年上半年开展的各项慈善项目及其成果...',
    image: 'https://images.unsplash.com/photo-1521791136064-7986c2920216?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80',
    category: '组织动态',
    date: '2023-05-30'
  }
])

// 志愿者故事
const stories = ref([
  {
    id: 1,
    name: '张明',
    title: '支教志愿者',
    quote: '看到孩子们渴望知识的眼神，我知道这一切都值得。',
    image: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
  },
  {
    id: 2,
    name: '李华',
    title: '敬老院志愿者',
    quote: '老人们的故事让我明白，陪伴是最长情的告白。',
    image: 'https://images.unsplash.com/photo-1531123897727-8f129e1688ce?ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
  },
  {
    id: 3,
    name: '王芳',
    title: '动物救助志愿者',
    quote: '每一个生命都值得被温柔以待。',
    image: 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
  }
])

// 捐赠数据
const currentAmount = ref(1250000)
const targetAmount = ref(2000000)
const donationProgress = computed(() => {
  return Math.min((currentAmount.value / targetAmount.value) * 100, 100)
})

// 模拟捐赠金额增长
onMounted(() => {
  setInterval(() => {
    currentAmount.value += Math.floor(Math.random() * 1000)
    if (currentAmount.value > targetAmount.value) {
      currentAmount.value = targetAmount.value
    }
  }, 3000)
})
</script>

<style lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+SC:wght@300;400;500;700&display=swap');
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Noto Sans SC', sans-serif;
  color: #333;
  background-color: #f9f9f9;
}

.charity-app {
  overflow-x: hidden;
}

.container {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
}

/* 导航栏样式 */
.charity-nav {
  background-color: rgba(255, 255, 255, 0.95);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: fixed;
  width: 100%;
  z-index: 1000;
  padding: 15px 0;
  transition: all 0.3s ease;

  .container {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .logo {
    a {
      display: flex;
      align-items: center;
      text-decoration: none;
      color: #e74c3c;
      font-weight: 700;
      font-size: 1.5rem;

      img {
        height: 40px;
        margin-right: 10px;
      }
    }
  }

  .nav-links {
    display: flex;
    list-style: none;

    li {
      margin: 0 15px;
      position: relative;

      a {
        text-decoration: none;
        color: #555;
        font-weight: 500;
        transition: color 0.3s;
        padding: 5px 0;
        position: relative;

        &:after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 0;
          width: 0;
          height: 2px;
          background-color: #e74c3c;
          transition: width 0.3s;
        }

        &:hover {
          color: #e74c3c;

          &:after {
            width: 100%;
          }
        }

        &.router-link-exact-active {
          color: #e74c3c;
          font-weight: 700;

          &:after {
            width: 100%;
          }
        }
      }
    }
  }

  .nav-buttons {
    display: flex;

    button {
      margin-left: 10px;
      padding: 8px 20px;
      border-radius: 4px;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.3s;
    }

    .btn-login {
      background: transparent;
      border: 1px solid #e74c3c;
      color: #e74c3c;

      &:hover {
        background: #e74c3c;
        color: white;
      }
    }

    .btn-register {
      background: #e74c3c;
      border: 1px solid #e74c3c;
      color: white;

      &:hover {
        background: #c0392b;
        border-color: #c0392b;
      }
    }
  }
}

/* 英雄区域 */
.hero-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 150px 0 100px;
  position: relative;
  background: linear-gradient(135deg, #f9f9f9 0%, #fff5f5 100%);
  overflow: hidden;

  .hero-content {
    flex: 1;
    padding-right: 50px;
    animation: fadeInUp 1s ease;

    .hero-title {
      font-size: 3rem;
      font-weight: 700;
      color: #e74c3c;
      margin-bottom: 20px;
      line-height: 1.2;
    }

    .hero-subtitle {
      font-size: 1.2rem;
      color: #666;
      margin-bottom: 30px;
      max-width: 500px;
    }

    .btn-hero {
      background: #e74c3c;
      color: white;
      border: none;
      padding: 12px 30px;
      font-size: 1.1rem;
      border-radius: 30px;
      cursor: pointer;
      transition: all 0.3s;
      box-shadow: 0 5px 15px rgba(231, 76, 60, 0.3);

      &:hover {
        background: #c0392b;
        transform: translateY(-3px);
        box-shadow: 0 8px 20px rgba(231, 76, 60, 0.4);
      }
    }
  }

  .hero-image {
    flex: 1;
    animation: fadeInRight 1s ease;

    img {
      width: 100%;
      max-width: 600px;
      border-radius: 10px;
      box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
    }
  }

  .floating-hearts {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    pointer-events: none;
    z-index: 1;

    .heart {
      position: absolute;
      width: 20px;
      height: 20px;
      background: url('@/assets/images/logo.png') no-repeat center center;
      background-size: contain;
      animation: float linear infinite;
    }
  }
}

/* 活动区域 */
.activities-section {
  padding: 80px 0;
  background-color: white;

  .section-header {
    text-align: center;
    margin-bottom: 50px;

    h2 {
      font-size: 2.5rem;
      color: #e74c3c;
      margin-bottom: 15px;
    }

    p {
      color: #777;
      font-size: 1.1rem;
    }
  }

  .activities-container {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
    gap: 30px;
  }

  .activity-card {
    background: white;
    border-radius: 10px;
    overflow: hidden;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s, box-shadow 0.3s;
    position: relative;

    &:hover {
      transform: translateY(-10px);
      box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
    }

    .activity-image {
      position: relative;
      height: 200px;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        transition: transform 0.5s;
      }

      .date-badge {
        position: absolute;
        top: 20px;
        right: 20px;
        background: rgba(231, 76, 60, 0.9);
        color: white;
        width: 60px;
        height: 60px;
        border-radius: 50%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        font-weight: bold;

        .day {
          font-size: 1.5rem;
          line-height: 1;
        }

        .month {
          font-size: 0.9rem;
        }
      }
    }

    &:hover .activity-image img {
      transform: scale(1.1);
    }

    .activity-content {
      padding: 25px;

      h3 {
        font-size: 1.5rem;
        margin-bottom: 15px;
        color: #333;
      }

      .activity-desc {
        color: #666;
        margin-bottom: 20px;
        line-height: 1.6;
      }

      .activity-meta {
        display: flex;
        justify-content: space-between;
        margin-bottom: 20px;
        color: #888;
        font-size: 0.9rem;

        i {
          margin-right: 5px;
          color: #e74c3c;
        }
      }

      .btn-join {
        background: #e74c3c;
        color: white;
        border: none;
        padding: 10px 25px;
        border-radius: 30px;
        cursor: pointer;
        transition: all 0.3s;
        font-weight: 500;

        &:hover {
          background: #c0392b;
          transform: translateY(-2px);
        }
      }
    }
  }
}

/* 文章区域 */
.articles-section {
  padding: 80px 0;
  background-color: #f5f5f5;

  .section-header {
    text-align: center;
    margin-bottom: 50px;

    h2 {
      font-size: 2.5rem;
      color: #e74c3c;
      margin-bottom: 15px;
    }

    p {
      color: #777;
      font-size: 1.1rem;
    }
  }

  .articles-container {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 30px;
  }

  .article-card {
    background: white;
    border-radius: 10px;
    overflow: hidden;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s, box-shadow 0.3s;

    &:hover {
      transform: translateY(-10px);
      box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
    }

    .article-image {
      height: 200px;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        transition: transform 0.5s;
      }
    }

    &:hover .article-image img {
      transform: scale(1.1);
    }

    .article-content {
      padding: 25px;

      .article-meta {
        display: flex;
        justify-content: space-between;
        margin-bottom: 15px;
        font-size: 0.9rem;

        .category {
          background: rgba(231, 76, 60, 0.1);
          color: #e74c3c;
          padding: 3px 10px;
          border-radius: 20px;
          font-weight: 500;
        }

        .date {
          color: #888;
        }
      }

      h3 {
        font-size: 1.3rem;
        margin-bottom: 15px;
        color: #333;
        line-height: 1.4;
      }

      .article-excerpt {
        color: #666;
        margin-bottom: 20px;
        line-height: 1.6;
      }

      .read-more {
        color: #e74c3c;
        text-decoration: none;
        font-weight: 500;
        position: relative;
        padding-bottom: 3px;

        &:after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 0;
          width: 0;
          height: 2px;
          background-color: #e74c3c;
          transition: width 0.3s;
        }

        &:hover:after {
          width: 100%;
        }
      }
    }
  }
}

/* 捐赠呼吁区域 */
.donation-call {
  display: flex;
  align-items: center;
  padding: 80px 0;
  background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
  color: white;
  position: relative;
  overflow: hidden;

  .donation-content {
    flex: 1;
    padding-right: 50px;
    z-index: 2;

    h2 {
      font-size: 2.5rem;
      margin-bottom: 20px;
    }

    p {
      font-size: 1.2rem;
      margin-bottom: 30px;
      opacity: 0.9;
    }

    .donation-progress {
      margin-bottom: 30px;

      .progress-bar {
        height: 10px;
        background: rgba(255, 255, 255, 0.2);
        border-radius: 5px;
        margin-bottom: 10px;
        overflow: hidden;

        .progress {
          height: 100%;
          background: white;
          border-radius: 5px;
          transition: width 1s ease;
        }
      }

      .progress-text {
        display: flex;
        justify-content: space-between;
        font-weight: 500;
      }
    }

    .btn-donate {
      background: white;
      color: #e74c3c;
      border: none;
      padding: 12px 30px;
      font-size: 1.1rem;
      border-radius: 30px;
      cursor: pointer;
      transition: all 0.3s;
      font-weight: 700;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 20px rgba(0, 0, 0, 0.3);
      }
    }
  }

  .donation-image {
    flex: 1;
    z-index: 2;

    img {
      width: 100%;
      max-width: 500px;
      border-radius: 10px;
      box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
    }
  }

  &:before {
    content: '';
    position: absolute;
    top: -50px;
    right: -50px;
    width: 200px;
    height: 200px;
    background: rgba(255, 255, 255, 0.05);
    border-radius: 50%;
  }

  &:after {
    content: '';
    position: absolute;
    bottom: -100px;
    left: -100px;
    width: 300px;
    height: 300px;
    background: rgba(255, 255, 255, 0.05);
    border-radius: 50%;
  }
}

/* 志愿者故事区域 */
.stories-section {
  padding: 80px 0;
  background-color: white;

  .section-header {
    text-align: center;
    margin-bottom: 50px;

    h2 {
      font-size: 2.5rem;
      color: #e74c3c;
      margin-bottom: 15px;
    }

    p {
      color: #777;
      font-size: 1.1rem;
    }
  }

  .stories-container {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 30px;
  }

  .story-card {
    background: white;
    border-radius: 10px;
    overflow: hidden;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s, box-shadow 0.3s;
    display: flex;
    flex-direction: column;

    &:hover {
      transform: translateY(-10px);
      box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
    }

    .story-image {
      height: 250px;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        transition: transform 0.5s;
      }
    }

    &:hover .story-image img {
      transform: scale(1.1);
    }

    .story-content {
      padding: 25px;
      flex: 1;
      display: flex;
      flex-direction: column;

      h3 {
        font-size: 1.5rem;
        margin-bottom: 5px;
        color: #333;
      }

      .story-title {
        color: #e74c3c;
        font-weight: 500;
        margin-bottom: 15px;
      }

      .story-quote {
        color: #666;
        font-style: italic;
        line-height: 1.6;
        margin-bottom: 20px;
        flex: 1;
      }

      .read-story {
        color: #e74c3c;
        text-decoration: none;
        font-weight: 500;
        position: relative;
        padding-bottom: 3px;
        align-self: flex-start;

        &:after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 0;
          width: 0;
          height: 2px;
          background-color: #e74c3c;
          transition: width 0.3s;
        }

        &:hover:after {
          width: 100%;
        }
      }
    }
  }
}

/* 页脚样式 */
.charity-footer {
  background: #222;
  color: #ddd;
  padding: 80px 0 20px;
  position: relative;

  .footer-content {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 40px;
    margin-bottom: 50px;
  }

  .footer-about {
    h3 {
      color: white;
      font-size: 1.5rem;
      margin-bottom: 20px;
    }

    p {
      line-height: 1.6;
      margin-bottom: 20px;
    }

    .social-links {
      display: flex;
      gap: 15px;

      a {
        color: #ddd;
        font-size: 1.2rem;
        transition: color 0.3s;

        &:hover {
          color: #e74c3c;
        }
      }
    }
  }

  .footer-links {
    h3 {
      color: white;
      font-size: 1.5rem;
      margin-bottom: 20px;
    }

    ul {
      list-style: none;

      li {
        margin-bottom: 10px;

        a {
          color: #ddd;
          text-decoration: none;
          transition: color 0.3s;
          position: relative;
          padding-left: 15px;

          &:before {
            content: '→';
            position: absolute;
            left: 0;
            color: #e74c3c;
            opacity: 0;
            transition: all 0.3s;
          }

          &:hover {
            color: #e74c3c;
            padding-left: 20px;

            &:before {
              opacity: 1;
            }
          }
        }
      }
    }
  }

  .footer-contact {
    h3 {
      color: white;
      font-size: 1.5rem;
      margin-bottom: 20px;
    }

    p {
      margin-bottom: 15px;
      display: flex;
      align-items: flex-start;
      line-height: 1.6;

      i {
        color: #e74c3c;
        margin-right: 10px;
        margin-top: 3px;
      }
    }
  }

  .footer-bottom {
    text-align: center;
    padding-top: 20px;
    border-top: 1px solid #444;
    color: #888;
    font-size: 0.9rem;
  }

  .floating-doves {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    pointer-events: none;
    z-index: 1;

    .dove {
      position: absolute;
      width: 40px;
      height: 40px;
      background: url('@/assets/images/img.png') no-repeat center center;
      background-size: contain;
      animation: float linear infinite;
    }
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes float {
  0% {
    transform: translateY(0) translateX(0);
  }
  50% {
    transform: translateY(-20px) translateX(10px);
  }
  100% {
    transform: translateY(0) translateX(0);
  }
}

/* 响应式设计 */
@media (max-width: 992px) {
  .hero-section {
    flex-direction: column;
    padding: 120px 0 60px;
    text-align: center;

    .hero-content {
      padding-right: 0;
      margin-bottom: 40px;

      .hero-subtitle {
        margin-left: auto;
        margin-right: auto;
      }
    }
  }

  .donation-call {
    flex-direction: column;
    text-align: center;

    .donation-content {
      padding-right: 0;
      margin-bottom: 40px;
    }
  }
}

@media (max-width: 768px) {
  .charity-nav {
    .nav-links {
      display: none;
    }
  }

  .hero-section {
    .hero-title {
      font-size: 2.2rem;
    }
  }

  .activities-container,
  .articles-container,
  .stories-container {
    grid-template-columns: 1fr;
  }
}

/* 其他工具类 */
.btn {
  display: inline-block;
  padding: 10px 20px;
  border-radius: 4px;
  font-weight: 500;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background: #e74c3c;
  color: white;
  border: 1px solid #e74c3c;

  &:hover {
    background: #c0392b;
    border-color: #c0392b;
  }
}

.btn-outline {
  background: transparent;
  color: #e74c3c;
  border: 1px solid #e74c3c;

  &:hover {
    background: #e74c3c;
    color: white;
  }
}
</style>