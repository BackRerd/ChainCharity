<template>
  <div :class="['charity-home', { 'dark-mode': isDarkMode }]">
    <!-- 顶部导航栏 - 重新设计 -->
    <header class="header" :class="{ 'scrolled': isScrolled }">
      <div class="header-container">
        <div class="logo" @click="goHome">
          <img src="https://via.placeholder.com/50x50/3a7bd5/ffffff?text=LOGO" alt="慈善Logo">
          <span>爱心慈善</span>
        </div>

        <el-menu
            :mode="isMobile ? 'vertical' : 'horizontal'"
            :default-active="activeIndex"
            class="nav-menu"
            @select="handleSelect"
            :background-color="isDarkMode ? '#1a1a1a' : 'transparent'"
            :text-color="isDarkMode ? '#fff' : '#333'"
            :active-text-color="isDarkMode ? '#ff9a5a' : '#3a7bd5'"
        >
          <el-menu-item index="1">首页</el-menu-item>
          <el-menu-item index="2">活动</el-menu-item>
          <el-menu-item index="3">捐助</el-menu-item>
          <el-menu-item index="4">志愿者</el-menu-item>
          <el-menu-item index="5">关于我们</el-menu-item>
        </el-menu>

        <div class="header-right">
          <el-switch
              v-model="isDarkMode"
              active-text="暗黑"
              inactive-text="明亮"
              @change="toggleTheme"
              class="theme-switch"
              :active-color="isDarkMode ? '#ff9a5a' : '#3a7bd5'"
          />
          <el-button
              type="primary"
              round
              @click="donateNow"
              class="donate-btn"
          >
            <i class="el-icon-coin"></i> 立即登录/注册
          </el-button>
        </div>
      </div>
    </header>

    <!-- 主要内容区域 - 完全重新设计 -->
    <main class="main-content">
      <!-- 英雄区域 - 新增渐变背景 -->
      <section class="hero-section" ref="heroSection">
        <div class="hero-content">
          <h1 class="hero-title" v-animate="'fadeInUp'">让世界充满爱</h1>
          <p class="hero-subtitle" v-animate="'fadeInUp'">您的每一份捐助都能改变一个生命</p>
          <el-button
              type="primary"
              size="large"
              round
              class="hero-btn"
              v-animate="'fadeInUp'"
              @click="donateNow"
          >
            加入我们
          </el-button>
        </div>
        <div class="hero-image" v-animate="'fadeIn'">
          <img src="https://via.placeholder.com/600x400/3a7bd5/ffffff?text=爱心图片" alt="慈善图片">
        </div>
      </section>

      <!-- 快速行动区域 - 卡片式设计 -->
      <section class="quick-actions">
        <div class="section-header" v-animate="'fadeInUp'">
          <h2>立即行动</h2>
          <p>选择您想帮助的方式</p>
        </div>
        <div class="action-cards">
          <div
              class="action-card"
              v-for="(action, index) in quickActions"
              :key="index"
              :style="{ backgroundColor: action.color }"
              v-animate="'fadeInUp'"
              :data-delay="index * 100"
              @click="handleAction(action.type)"
          >
            <div class="card-icon">
              <i :class="action.icon"></i>
            </div>
            <h3>{{ action.title }}</h3>
            <p>{{ action.description }}</p>
            <el-button type="text" class="card-link">
              了解更多 <i class="el-icon-arrow-right"></i>
            </el-button>
          </div>
        </div>
      </section>

      <!-- 特色活动 - 横向滑动卡片 -->
      <section class="featured-events">
        <div class="section-header" v-animate="'fadeInUp'">
          <h2>特色活动</h2>
          <el-button type="text" class="view-all">查看全部</el-button>
        </div>
        <div class="events-container">
          <div class="events-scroller" ref="eventsScroller">
            <div
                class="event-card"
                v-for="(event, index) in featuredEvents"
                :key="index"
                v-animate="'fadeInRight'"
                :data-delay="index * 50"
            >
              <div class="event-image">
                <img :src="event.image" :alt="event.title">
                <div class="event-tag" :style="{ backgroundColor: event.tagColor }">
                  {{ event.tag }}
                </div>
              </div>
              <div class="event-content">
                <div class="event-meta">
                  <span><i class="el-icon-date"></i> {{ event.date }}</span>
                  <span><i class="el-icon-location"></i> {{ event.location }}</span>
                </div>
                <h3>{{ event.title }}</h3>
                <p>{{ event.description }}</p>
                <el-progress
                    :percentage="event.progress"
                    :color="event.progressColor"
                    :show-text="false"
                ></el-progress>
                <div class="progress-info">
                  <span>已筹: {{ event.raised }}元</span>
                  <span>目标: {{ event.target }}元</span>
                </div>
                <el-button type="primary" plain round class="event-btn">
                  参与活动
                </el-button>
              </div>
            </div>
          </div>
          <button class="scroll-btn left" @click="scrollEvents(-1)" v-if="canScrollLeft">
            <i class="el-icon-arrow-left"></i>
          </button>
          <button class="scroll-btn right" @click="scrollEvents(1)" v-if="canScrollRight">
            <i class="el-icon-arrow-right"></i>
          </button>
        </div>
      </section>

      <!-- 数据统计 - 动态计数器 -->
      <section class="stats-section" v-animate="'fadeIn'">
        <div class="stats-bg"></div>
        <div class="stats-content">
          <div
              class="stat-item"
              v-for="(stat, index) in stats"
              :key="index"
              v-animate="'fadeInUp'"
              :data-delay="index * 150"
          >
            <div class="stat-number" :style="{ color: stat.color }">
              <count-up :end="stat.number" :duration="2"></count-up>+
            </div>
            <div class="stat-icon">
              <i :class="stat.icon"></i>
            </div>
            <p class="stat-title">{{ stat.title }}</p>
          </div>
        </div>
      </section>

      <!-- 最新动态 - 卡片与时间线结合 -->
      <section class="latest-updates">
        <div class="section-header" v-animate="'fadeInUp'">
          <h2>最新动态</h2>
        </div>
        <div class="updates-container">
          <div class="updates-timeline" v-animate="'fadeInLeft'">
            <div
                class="timeline-item"
                v-for="(update, index) in timelineUpdates"
                :key="index"
                :style="{ '--dot-color': update.color }"
            >
              <div class="timeline-dot"></div>
              <div class="timeline-content">
                <h4>{{ update.title }}</h4>
                <p>{{ update.content }}</p>
                <span class="timeline-date">{{ update.date }}</span>
              </div>
            </div>
          </div>
          <div class="updates-highlight" v-animate="'fadeInRight'">
            <el-carousel height="400px" :interval="5000" arrow="hover" indicator-position="outside">
              <el-carousel-item v-for="(highlight, index) in highlightUpdates" :key="index">
                <div class="highlight-card">
                  <img :src="highlight.image" :alt="highlight.title">
                  <div class="highlight-content">
                    <h3>{{ highlight.title }}</h3>
                    <p>{{ highlight.content }}</p>
                    <el-button type="primary" round>查看详情</el-button>
                  </div>
                </div>
              </el-carousel-item>
            </el-carousel>
          </div>
        </div>
      </section>

      <!-- 志愿者故事 - 卡片翻转效果 -->
      <section class="volunteer-stories">
        <div class="section-header" v-animate="'fadeInUp'">
          <h2>志愿者故事</h2>
          <p>听听他们的爱心经历</p>
        </div>
        <div class="stories-container">
          <div
              class="story-card"
              v-for="(story, index) in volunteerStories"
              :key="index"
              :class="{ 'flipped': story.flipped }"
              @click="story.flipped = !story.flipped"
              v-animate="'fadeInUp'"
              :data-delay="index * 100"
          >
            <div class="card-front">
              <img :src="story.avatar" :alt="story.name">
              <h3>{{ story.name }}</h3>
              <p class="story-role">{{ story.role }}</p>
              <p class="story-excerpt">{{ story.excerpt }}</p>
              <div class="story-tags">
                <el-tag
                    v-for="(tag, tagIndex) in story.tags"
                    :key="tagIndex"
                    :type="tagTypes[tagIndex % tagTypes.length]"
                    size="small"
                >
                  {{ tag }}
                </el-tag>
              </div>
            </div>
            <div class="card-back">
              <h3>{{ story.name }}的故事</h3>
              <p>{{ story.story }}</p>
              <el-button type="text" class="read-more">
                阅读完整故事 <i class="el-icon-arrow-right"></i>
              </el-button>
            </div>
          </div>
        </div>
      </section>

      <!-- 捐助呼吁 - 渐变背景和动画按钮 -->
      <section class="donation-call" v-animate="'fadeIn'">
        <div class="call-container">
          <h2 v-animate="'fadeInUp'">您的捐助可以改变一个生命</h2>
          <p v-animate="'fadeInUp'" data-delay="100">每一份爱心都能带来希望</p>
          <el-button
              type="primary"
              round
              class="pulse-btn"
              v-animate="'fadeInUp'"
              data-delay="200"
              @click="donateNow"
          >
            立即捐助
          </el-button>
        </div>
      </section>
    </main>

    <!-- 页脚 - 重新设计 -->
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-about" v-animate="'fadeInUp'">
          <div class="footer-logo">
            <img src="https://via.placeholder.com/50x50/3a7bd5/ffffff?text=LOGO" alt="慈善Logo">
            <span>爱心慈善</span>
          </div>
          <p>我们致力于帮助贫困儿童和弱势群体，让世界变得更美好。</p>
          <div class="social-links">
            <a href="#"><i class="el-icon-weibo"></i></a>
            <a href="#"><i class="el-icon-weixin"></i></a>
            <a href="#"><i class="el-icon-share"></i></a>
          </div>
        </div>

        <div class="footer-links">
          <div class="links-column" v-animate="'fadeInUp'" data-delay="100">
            <h3>快速链接</h3>
            <ul>
              <li><a href="#">首页</a></li>
              <li><a href="#">关于我们</a></li>
              <li><a href="#">捐助项目</a></li>
              <li><a href="#">志愿者</a></li>
            </ul>
          </div>
          <div class="links-column" v-animate="'fadeInUp'" data-delay="150">
            <h3>捐助方式</h3>
            <ul>
              <li><a href="#">一次性捐助</a></li>
              <li><a href="#">月捐计划</a></li>
              <li><a href="#">物资捐助</a></li>
              <li><a href="#">企业合作</a></li>
            </ul>
          </div>
          <div class="links-column" v-animate="'fadeInUp'" data-delay="200">
            <h3>联系方式</h3>
            <ul>
              <li><i class="el-icon-phone"></i> 123-456-7890</li>
              <li><i class="el-icon-message"></i> info@charity.org</li>
              <li><i class="el-icon-location"></i> 北京市朝阳区慈善路88号</li>
            </ul>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <p>© 2023 爱心慈善 版权所有</p>
        <div class="footer-legal">
          <a href="#">隐私政策</a>
          <a href="#">使用条款</a>
        </div>
      </div>
    </footer>

    <!-- 返回顶部按钮 -->
    <el-backtop :right="50" :bottom="50" target=".charity-home">
      <i class="el-icon-caret-top"></i>
    </el-backtop>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

// 动画指令
const vAnimate = {
  mounted(el, binding) {
    const animation = binding.value || 'fadeIn'
    const delay = el.dataset.delay || 0

    const observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          setTimeout(() => {
            el.style.opacity = 1
            el.classList.add(animation)
            observer.unobserve(el)
          }, delay)
        }
      })
    }, { threshold: 0.1 })

    el.style.opacity = 0
    observer.observe(el)
  }
}

// 计数组件
const CountUp = {
  props: ['end', 'duration'],
  template: `<span>{{ currentValue }}</span>`,
  data() {
    return {
      currentValue: 0
    }
  },
  mounted() {
    const increment = this.end / (this.duration * 60)
    const timer = setInterval(() => {
      this.currentValue += increment
      if (this.currentValue >= this.end) {
        this.currentValue = this.end
        clearInterval(timer)
      }
    }, 1000 / 60)
  }
}

const router = useRouter()

// 主题切换
const isDarkMode = ref(false)
const toggleTheme = () => {
  if (isDarkMode.value) {
    document.documentElement.setAttribute('data-theme', 'dark')
  } else {
    document.documentElement.setAttribute('data-theme', 'light')
  }
}

// 滚动检测
const isScrolled = ref(false)
const handleScroll = () => {
  isScrolled.value = window.scrollY > 50
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  checkMobile()
  window.addEventListener('resize', checkMobile)
  initEventScroller()
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  window.removeEventListener('resize', checkMobile)
})

// 响应式判断
const isMobile = ref(false)
const checkMobile = () => {
  isMobile.value = window.innerWidth < 768
}

// 导航栏
const activeIndex = ref('1')
const handleSelect = (index) => {
  activeIndex.value = index
  ElMessage.success(`导航到 ${index}`)
}

const donateNow = () => {
  router.push('/auth')
}

const goHome = () => {
  activeIndex.value = '1'
}

// 快速行动
const quickActions = ref([
  {
    title: '立即捐助',
    description: '帮助需要帮助的人',
    icon: 'el-icon-coin',
    type: 'donate',
    color: '#3a7bd5'
  },
  {
    title: '成为志愿者',
    description: '用您的行动传递爱心',
    icon: 'el-icon-user-solid',
    type: 'volunteer',
    color: '#ff9a5a'
  },
  {
    title: '物资捐助',
    description: '捐赠物品帮助他人',
    icon: 'el-icon-box',
    type: 'goods',
    color: '#00d2ff'
  },
  {
    title: '分享故事',
    description: '传播爱心正能量',
    icon: 'el-icon-chat-line-round',
    type: 'story',
    color: '#a18cd1'
  }
])

const handleAction = (type) => {
  switch(type) {
    case 'donate':
      donateNow()
      break
    case 'volunteer':
      ElMessage.info('跳转到志愿者页面')
      break
    case 'goods':
      ElMessage.info('跳转到物资捐助页面')
      break
    case 'story':
      ElMessage.info('跳转到故事分享页面')
      break
  }
}

// 特色活动
const featuredEvents = ref([
  {
    title: '山区儿童助学计划',
    description: '为山区儿童提供教育资源和学习环境',
    image: 'https://via.placeholder.com/400x300/3a7bd5/ffffff?text=助学计划',
    date: '2023-10-15 至 2023-12-31',
    location: '云南山区',
    raised: 125000,
    target: 200000,
    progress: 62.5,
    progressColor: '#3a7bd5',
    tag: '教育',
    tagColor: '#ff9a5a'
  },
  {
    title: '冬季温暖包发放',
    description: '为贫困地区儿童提供冬季保暖物资',
    image: 'https://via.placeholder.com/400x300/00d2ff/ffffff?text=温暖包',
    date: '2023-11-20 至 2023-12-20',
    location: '甘肃贫困地区',
    raised: 80000,
    target: 150000,
    progress: 53.3,
    progressColor: '#00d2ff',
    tag: '物资',
    tagColor: '#3a7bd5'
  },
  {
    title: '敬老院爱心义诊',
    description: '为敬老院老人提供免费医疗服务',
    image: 'https://via.placeholder.com/400x300/a18cd1/ffffff?text=爱心义诊',
    date: '2023-11-05 至 2023-11-10',
    location: '北京朝阳区',
    raised: 45000,
    target: 50000,
    progress: 90,
    progressColor: '#a18cd1',
    tag: '医疗',
    tagColor: '#00d2ff'
  },
  {
    title: '环保公益活动',
    description: '组织志愿者清理城市公园垃圾',
    image: 'https://via.placeholder.com/400x300/ff9a5a/ffffff?text=环保活动',
    date: '2023-11-25',
    location: '上海浦东新区',
    raised: 35000,
    target: 40000,
    progress: 87.5,
    progressColor: '#ff9a5a',
    tag: '环保',
    tagColor: '#a18cd1'
  },
  {
    title: '特殊儿童关爱日',
    description: '为特殊儿童举办关爱活动',
    image: 'https://via.placeholder.com/400x300/667eea/ffffff?text=儿童关爱',
    date: '2023-12-03',
    location: '广州天河区',
    raised: 28000,
    target: 50000,
    progress: 56,
    progressColor: '#667eea',
    tag: '关爱',
    tagColor: '#ff9a5a'
  }
])

// 活动水平滚动
const eventsScroller = ref(null)
const canScrollLeft = ref(false)
const canScrollRight = ref(true)

const initEventScroller = () => {
  checkScrollButtons()
}

const scrollEvents = (direction) => {
  const scroller = eventsScroller.value
  if (scroller) {
    const scrollAmount = 300
    scroller.scrollBy({
      left: scrollAmount * direction,
      behavior: 'smooth'
    })

    setTimeout(checkScrollButtons, 500)
  }
}

const checkScrollButtons = () => {
  const scroller = eventsScroller.value
  if (scroller) {
    canScrollLeft.value = scroller.scrollLeft > 0
    canScrollRight.value = scroller.scrollLeft < (scroller.scrollWidth - scroller.clientWidth)
  }
}

// 统计数据
const stats = ref([
  {
    number: 1250,
    title: '捐助项目',
    icon: 'el-icon-collection',
    color: '#3a7bd5'
  },
  {
    number: 3560,
    title: '志愿者',
    icon: 'el-icon-user',
    color: '#ff9a5a'
  },
  {
    number: 12800,
    title: '受益儿童',
    icon: 'el-icon-child',
    color: '#00d2ff'
  },
  {
    number: 5600,
    title: '受益老人',
    icon: 'el-icon-help',
    color: '#a18cd1'
  }
])

// 最新动态
const timelineUpdates = ref([
  {
    title: '网站系统升级完成',
    content: '我们已完成网站系统升级，现在提供更流畅的捐助体验',
    date: '2023-11-12',
    color: '#3a7bd5'
  },
  {
    title: '冬季温暖包项目启动',
    content: '冬季温暖包项目正式启动，目标帮助1000名贫困儿童',
    date: '2023-11-05',
    color: '#ff9a5a'
  },
  {
    title: '志愿者培训会圆满结束',
    content: '最新一期志愿者培训会圆满结束，新增50名志愿者',
    date: '2023-10-28',
    color: '#00d2ff'
  },
  {
    title: '慈善晚宴成功举办',
    content: '年度慈善晚宴成功举办，筹集善款50万元',
    date: '2023-10-15',
    color: '#a18cd1'
  }
])

const highlightUpdates = ref([
  {
    title: '山区助学项目进展',
    content: '我们的山区助学项目已帮助500多名儿童重返校园',
    image: 'https://via.placeholder.com/800x400/3a7bd5/ffffff?text=助学项目'
  },
  {
    title: '敬老院爱心活动回顾',
    content: '上个月的敬老院爱心活动获得老人们的一致好评',
    image: 'https://via.placeholder.com/800x400/ff9a5a/ffffff?text=敬老活动'
  },
  {
    title: '环保公益成果展示',
    content: '我们的环保公益活动已清理超过10吨垃圾',
    image: 'https://via.placeholder.com/800x400/00d2ff/ffffff?text=环保成果'
  }
])

// 志愿者故事
const tagTypes = ['', 'success', 'info', 'warning', 'danger']
const volunteerStories = ref([
  {
    name: '张明',
    role: '支教志愿者',
    avatar: 'https://via.placeholder.com/150/3a7bd5/ffffff?text=张',
    excerpt: '在山区支教的一年让我明白了教育的真正意义',
    story: '去年我辞去城市工作，前往云南山区支教。那里的孩子们对知识的渴望让我深受感动。虽然条件艰苦，但看到他们一天天进步，我觉得一切付出都值得。',
    tags: ['教育', '山区', '长期'],
    flipped: false
  },
  {
    name: '李华',
    role: '医疗志愿者',
    avatar: 'https://via.placeholder.com/150/ff9a5a/ffffff?text=李',
    excerpt: '用我的专业知识帮助需要帮助的人',
    story: '作为一名医生，我每月都会参加敬老院的义诊活动。老人们常常因为经济原因无法及时就医，我们的服务对他们来说非常重要。',
    tags: ['医疗', '老人', '专业'],
    flipped: false
  },
  {
    name: '王芳',
    role: '环保志愿者',
    avatar: 'https://via.placeholder.com/150/00d2ff/ffffff?text=王',
    excerpt: '保护环境是我们每个人的责任',
    story: '我带领团队每月组织一次环保活动，清理公园和河道的垃圾。最让我高兴的是看到越来越多的人加入我们，共同保护环境。',
    tags: ['环保', '团队', '社区'],
    flipped: false
  }
])
</script>

<style scoped>
/* 基础样式 */
.charity-home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f8fafc;
  color: #333;
  transition: all 0.3s ease;
}

.charity-home.dark-mode {
  background-color: #121212;
  color: #e0e0e0;
}

/* 顶部导航栏 - 重新设计 */
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background-color: rgba(255, 255, 255, 0.95);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.header.scrolled {
  background-color: rgba(255, 255, 255, 0.98);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.dark-mode .header {
  background-color: rgba(26, 26, 26, 0.95);
  box-shadow: 0 2px 10px rgba(255, 255, 255, 0.05);
}

.dark-mode .header.scrolled {
  background-color: rgba(26, 26, 26, 0.98);
  box-shadow: 0 4px 12px rgba(255, 255, 255, 0.1);
}

.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 70px;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.logo:hover {
  transform: scale(1.05);
}

.logo img {
  height: 40px;
  margin-right: 10px;
}

.logo span {
  font-size: 20px;
  font-weight: bold;
  color: #3a7bd5;
}

.dark-mode .logo span {
  color: #ff9a5a;
}

.nav-menu {
  flex: 1;
  border-bottom: none;
  margin: 0 20px;
}

.nav-menu.el-menu--horizontal {
  height: 70px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.theme-switch {
  margin-right: 10px;
}

.donate-btn {
  background: linear-gradient(135deg, #3a7bd5, #00d2ff);
  border: none;
  padding: 10px 20px;
  font-weight: bold;
  transition: all 0.3s ease;
}

.donate-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(58, 123, 213, 0.3);
}

.dark-mode .donate-btn {
  background: linear-gradient(135deg, #ff9a5a, #ff6e7f);
}

.dark-mode .donate-btn:hover {
  box-shadow: 0 4px 12px rgba(255, 154, 90, 0.3);
}

/* 主要内容区域 */
.main-content {
  flex: 1;
  margin-top: 70px;
  overflow-x: hidden;
}

/* 英雄区域 */
.hero-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 80px 40px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8eb 100%);
  position: relative;
  overflow: hidden;
}

.dark-mode .hero-section {
  background: linear-gradient(135deg, #121212 0%, #1a1a1a 100%);
}

.hero-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxMDAlIiBoZWlnaHQ9IjEwMCUiPjxkZWZzPjxwYXR0ZXJuIGlkPSJwYXR0ZXJuIiB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHBhdHRlcm5Vbml0cz0idXNlclNwYWNlT25Vc2UiIHBhdHRlcm5UcmFuc2Zvcm09InJvdGF0ZSg0NSkiPjxyZWN0IHdpZHRoPSIyMCIgaGVpZ2h0PSIyMCIgZmlsbD0icmdiYSgyNTUsMjU1LDI1NSwwLjAzKSIvPjwvcGF0dGVybj48L2RlZnM+PHJlY3Qgd2lkdGg9IjEwMCUiIGhlaWdodD0iMTAwJSIgZmlsbD0idXJsKCNwYXR0ZXJuKSIvPjwvc3ZnPg==');
}

.hero-content {
  flex: 1;
  max-width: 600px;
  z-index: 1;
}

.hero-title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #3a7bd5;
  line-height: 1.2;
}

.dark-mode .hero-title {
  color: #ff9a5a;
}

.hero-subtitle {
  font-size: 20px;
  margin-bottom: 30px;
  color: #555;
  line-height: 1.6;
}

.dark-mode .hero-subtitle {
  color: #bbb;
}

.hero-btn {
  background: linear-gradient(135deg, #3a7bd5, #00d2ff);
  border: none;
  padding: 12px 30px;
  font-size: 18px;
  font-weight: bold;
  transition: all 0.3s ease;
}

.hero-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(58, 123, 213, 0.3);
}

.dark-mode .hero-btn {
  background: linear-gradient(135deg, #ff9a5a, #ff6e7f);
}

.dark-mode .hero-btn:hover {
  box-shadow: 0 8px 20px rgba(255, 154, 90, 0.3);
}

.hero-image {
  flex: 1;
  max-width: 500px;
  z-index: 1;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
  transition: transform 0.5s ease;
}

.hero-image:hover {
  transform: scale(1.03);
}

.hero-image img {
  width: 100%;
  height: auto;
  display: block;
}

/* 快速行动区域 */
.quick-actions {
  padding: 60px 40px;
  background-color: #fff;
}

.dark-mode .quick-actions {
  background-color: #1a1a1a;
}

.section-header {
  text-align: center;
  margin-bottom: 40px;
}

.section-header h2 {
  font-size: 36px;
  margin-bottom: 10px;
  color: #3a7bd5;
}

.dark-mode .section-header h2 {
  color: #ff9a5a;
}

.section-header p {
  font-size: 18px;
  color: #666;
}

.dark-mode .section-header p {
  color: #aaa;
}

.action-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.action-card {
  padding: 30px;
  border-radius: 10px;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  z-index: 1;
}

.action-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.1);
  z-index: -1;
}

.action-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
}

.card-icon {
  font-size: 40px;
  margin-bottom: 20px;
  opacity: 0.9;
}

.action-card h3 {
  font-size: 22px;
  margin-bottom: 10px;
}

.action-card p {
  font-size: 16px;
  margin-bottom: 20px;
  opacity: 0.9;
}

.card-link {
  color: white !important;
  font-weight: bold;
  padding: 0;
  transition: all 0.3s ease;
}

.card-link i {
  transition: transform 0.3s ease;
}

.card-link:hover i {
  transform: translateX(5px);
}

/* 特色活动 */
.featured-events {
  padding: 60px 20px;
  background-color: #f5f7fa;
  position: relative;
}

.dark-mode .featured-events {
  background-color: #121212;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto 30px;
  padding: 0 20px;
}

.view-all {
  font-size: 16px;
  color: #3a7bd5;
  font-weight: bold;
}

.dark-mode .view-all {
  color: #ff9a5a;
}

.view-all:hover {
  color: #00d2ff;
}

.events-container {
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
}

.events-scroller {
  display: flex;
  gap: 20px;
  padding: 10px 20px;
  overflow-x: auto;
  scroll-behavior: smooth;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE and Edge */
}

.events-scroller::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera */
}

.event-card {
  min-width: 350px;
  background-color: #fff;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.dark-mode .event-card {
  background-color: #1e1e1e;
  box-shadow: 0 5px 15px rgba(255, 255, 255, 0.05);
}

.event-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
}

.dark-mode .event-card:hover {
  box-shadow: 0 10px 25px rgba(255, 255, 255, 0.1);
}

.event-image {
  position: relative;
  height: 200px;
}

.event-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.event-tag {
  position: absolute;
  top: 15px;
  right: 15px;
  padding: 5px 10px;
  border-radius: 20px;
  color: white;
  font-size: 12px;
  font-weight: bold;
}

.event-content {
  padding: 20px;
}

.event-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  font-size: 12px;
  color: #666;
}

.dark-mode .event-meta {
  color: #aaa;
}

.event-meta i {
  margin-right: 5px;
}

.event-content h3 {
  font-size: 18px;
  margin-bottom: 10px;
}

.event-content p {
  font-size: 14px;
  color: #666;
  margin-bottom: 15px;
  line-height: 1.5;
}

.dark-mode .event-content p {
  color: #aaa;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.event-btn {
  width: 100%;
  margin-top: 15px;
  transition: all 0.3s ease;
}

.event-btn:hover {
  transform: translateY(-2px);
}

.scroll-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: white;
  border: none;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
  transition: all 0.3s ease;
}

.dark-mode .scroll-btn {
  background-color: #333;
  box-shadow: 0 2px 10px rgba(255, 255, 255, 0.1);
}

.scroll-btn:hover {
  background-color: #3a7bd5;
  color: white;
}

.dark-mode .scroll-btn:hover {
  background-color: #ff9a5a;
}

.scroll-btn.left {
  left: 10px;
}

.scroll-btn.right {
  right: 10px;
}

.scroll-btn i {
  font-size: 18px;
}

/* 数据统计 */
.stats-section {
  padding: 80px 20px;
  position: relative;
  overflow: hidden;
}

.stats-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #3a7bd5 0%, #00d2ff 100%);
  opacity: 0.9;
  z-index: 0;
}

.dark-mode .stats-bg {
  background: linear-gradient(135deg, #ff9a5a 0%, #ff6e7f 100%);
}

.stats-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

.stat-item {
  background-color: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(5px);
  padding: 30px;
  border-radius: 10px;
  text-align: center;
  color: white;
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-5px);
  background-color: rgba(255, 255, 255, 0.2);
}

.stat-number {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 10px;
}

.stat-icon {
  font-size: 40px;
  margin-bottom: 15px;
}

.stat-title {
  font-size: 18px;
  margin: 0;
  opacity: 0.9;
}

/* 最新动态 */
.latest-updates {
  padding: 60px 20px;
  background-color: #fff;
}

.dark-mode .latest-updates {
  background-color: #1a1a1a;
}

.updates-container {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.updates-timeline {
  position: relative;
  padding: 20px 0;
}

.updates-timeline::before {
  content: '';
  position: absolute;
  top: 0;
  left: 9px;
  height: 100%;
  width: 2px;
  background-color: #e0e0e0;
}

.dark-mode .updates-timeline::before {
  background-color: #333;
}

.timeline-item {
  position: relative;
  padding-left: 40px;
  margin-bottom: 30px;
}

.timeline-dot {
  position: absolute;
  left: 0;
  top: 5px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background-color: var(--dot-color, #3a7bd5);
  border: 3px solid white;
}

.dark-mode .timeline-dot {
  border-color: #1a1a1a;
}

.timeline-content {
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.dark-mode .timeline-content {
  background-color: #222;
  box-shadow: 0 2px 10px rgba(255, 255, 255, 0.05);
}

.timeline-content h4 {
  margin-top: 0;
  margin-bottom: 10px;
  color: #3a7bd5;
}

.dark-mode .timeline-content h4 {
  color: #ff9a5a;
}

.timeline-content p {
  margin-bottom: 10px;
  color: #555;
}

.dark-mode .timeline-content p {
  color: #bbb;
}

.timeline-date {
  font-size: 12px;
  color: #999;
}

.updates-highlight {
  border-radius: 10px;
  overflow: hidden;
}

.highlight-card {
  position: relative;
  height: 100%;
}

.highlight-card img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.highlight-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
  color: white;
}

.highlight-content h3 {
  font-size: 24px;
  margin-bottom: 10px;
}

.highlight-content p {
  margin-bottom: 20px;
  opacity: 0.9;
}

/* 志愿者故事 */
.volunteer-stories {
  padding: 60px 20px;
  background-color: #f5f7fa;
}

.dark-mode .volunteer-stories {
  background-color: #121212;
}

.stories-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.story-card {
  height: 400px;
  perspective: 1000px;
  cursor: pointer;
}

.card-front,
.card-back {
  position: absolute;
  width: 100%;
  height: 100%;
  backface-visibility: hidden;
  transition: transform 0.6s ease;
  border-radius: 10px;
  padding: 30px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.dark-mode .card-front,
.dark-mode .card-back {
  box-shadow: 0 5px 15px rgba(255, 255, 255, 0.05);
}

.card-front {
  background-color: white;
  transform: rotateY(0deg);
}

.dark-mode .card-front {
  background-color: #1e1e1e;
}

.card-back {
  background-color: #3a7bd5;
  color: white;
  transform: rotateY(180deg);
}

.dark-mode .card-back {
  background-color: #ff9a5a;
}

.story-card.flipped .card-front {
  transform: rotateY(180deg);
}

.story-card.flipped .card-back {
  transform: rotateY(360deg);
}

.story-card img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 20px;
  border: 3px solid #3a7bd5;
}

.dark-mode .story-card img {
  border-color: #ff9a5a;
}

.story-card h3 {
  margin: 10px 0;
}

.story-role {
  color: #3a7bd5;
  font-weight: bold;
  margin-bottom: 15px;
}

.dark-mode .story-role {
  color: #ff9a5a;
}

.story-excerpt {
  color: #666;
  margin-bottom: 20px;
  line-height: 1.6;
}

.dark-mode .story-excerpt {
  color: #aaa;
}

.story-tags {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 8px;
}

.read-more {
  color: white !important;
  margin-top: 20px;
  font-weight: bold;
}

.read-more i {
  transition: transform 0.3s ease;
}

.read-more:hover i {
  transform: translateX(5px);
}

/* 捐助呼吁 */
.donation-call {
  padding: 100px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  text-align: center;
  color: white;
  position: relative;
  overflow: hidden;
}

.dark-mode .donation-call {
  background: linear-gradient(135deg, #ff9a5a 0%, #ff6e7f 100%);
}

.call-container {
  max-width: 800px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

.donation-call h2 {
  font-size: 42px;
  margin-bottom: 20px;
}

.donation-call p {
  font-size: 20px;
  margin-bottom: 30px;
  opacity: 0.9;
}

.pulse-btn {
  background-color: white;
  color: #3a7bd5;
  font-size: 18px;
  font-weight: bold;
  padding: 15px 40px;
  border: none;
  animation: pulse 2s infinite;
  transition: all 0.3s ease;
}

.dark-mode .pulse-btn {
  color: #ff9a5a;
}

.pulse-btn:hover {
  transform: translateY(-3px);
  animation: none;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(255, 255, 255, 0.4);
  }
  70% {
    box-shadow: 0 0 0 15px rgba(255, 255, 255, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(255, 255, 255, 0);
  }
}

/* 页脚 */
.footer {
  background-color: #2c3e50;
  color: white;
  padding-top: 60px;
}

.dark-mode .footer {
  background-color: #0d0d0d;
}

.footer-content {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 40px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-about {
  padding-right: 40px;
}

.footer-logo {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.footer-logo img {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

.footer-logo span {
  font-size: 20px;
  font-weight: bold;
}

.footer-about p {
  margin-bottom: 20px;
  opacity: 0.8;
  line-height: 1.6;
}

.social-links {
  display: flex;
  gap: 15px;
}

.social-links a {
  color: white;
  font-size: 20px;
  opacity: 0.7;
  transition: opacity 0.3s ease;
}

.social-links a:hover {
  opacity: 1;
}

.footer-links {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
}

.links-column h3 {
  font-size: 18px;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.links-column ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.links-column ul li {
  margin-bottom: 12px;
}

.links-column ul li a {
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  transition: color 0.3s ease;
}

.links-column ul li a:hover {
  color: white;
}

.links-column ul li i {
  margin-right: 8px;
  width: 20px;
  text-align: center;
}

.footer-bottom {
  margin-top: 60px;
  padding: 20px 0;
  background-color: rgba(0, 0, 0, 0.2);
  text-align: center;
  font-size: 14px;
  opacity: 0.8;
}

.footer-legal {
  margin-top: 10px;
}

.footer-legal a {
  color: white;
  margin: 0 10px;
  text-decoration: none;
}

.footer-legal a:hover {
  text-decoration: underline;
}

/* 动画 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

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

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInLeft {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
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

.fadeIn {
  animation: fadeIn 0.6s ease forwards;
}

.fadeInUp {
  animation: fadeInUp 0.6s ease forwards;
}

.fadeInDown {
  animation: fadeInDown 0.6s ease forwards;
}

.fadeInLeft {
  animation: fadeInLeft 0.6s ease forwards;
}

.fadeInRight {
  animation: fadeInRight 0.6s ease forwards;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .hero-section {
    flex-direction: column;
    text-align: center;
    padding: 60px 20px;
  }

  .hero-content {
    margin-bottom: 40px;
  }

  .updates-container {
    grid-template-columns: 1fr;
  }

  .footer-content {
    grid-template-columns: 1fr;
  }

  .footer-about {
    padding-right: 0;
    text-align: center;
  }

  .social-links {
    justify-content: center;
  }
}

@media (max-width: 768px) {
  .header-container {
    flex-direction: column;
    height: auto;
    padding: 15px;
  }

  .logo {
    margin-bottom: 15px;
  }

  .nav-menu {
    margin: 15px 0;
  }

  .header-right {
    width: 100%;
    justify-content: center;
    margin-top: 15px;
  }

  .hero-title {
    font-size: 36px;
  }

  .hero-subtitle {
    font-size: 18px;
  }

  .action-cards {
    grid-template-columns: 1fr;
  }

  .footer-links {
    grid-template-columns: 1fr;
  }

  .links-column {
    text-align: center;
  }
}

@media (max-width: 576px) {
  .hero-title {
    font-size: 30px;
  }

  .section-header h2 {
    font-size: 28px;
  }

  .stats-content {
    grid-template-columns: 1fr;
  }

  .stat-item {
    padding: 20px;
  }

  .stat-number {
    font-size: 36px;
  }

  .donation-call h2 {
    font-size: 32px;
  }

  .donation-call p {
    font-size: 18px;
  }
}
</style>