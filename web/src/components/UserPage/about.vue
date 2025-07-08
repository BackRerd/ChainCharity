<template>
  <div :class="['about-page', { 'dark-mode': isDarkMode }]">
    <!-- 共用顶部导航栏 -->
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
            <i class="el-icon-coin"></i> 立即捐助
          </el-button>
        </div>
      </div>
    </header>

    <!-- 关于我们页面内容 -->
    <main class="about-content">
      <!-- 关于我们英雄区域 -->
      <section class="about-hero">
        <div class="hero-overlay"></div>
        <div class="hero-content" v-animate="'fadeInUp'">
          <h1>关于我们</h1>
          <p>爱心慈善成立于2010年，致力于帮助贫困儿童和弱势群体</p>
        </div>
      </section>

      <!-- 我们的使命 -->
      <section class="mission-section">
        <div class="section-container">
          <div class="mission-content" v-animate="'fadeInLeft'">
            <h2 class="section-title">我们的使命</h2>
            <p>爱心慈善的使命是通过教育、医疗和生活援助，改善贫困儿童和弱势群体的生活条件，为他们创造更美好的未来。</p>
            <p>我们相信每个孩子都应该有平等的机会去追求自己的梦想，无论他们出生在什么样的环境中。</p>
            <div class="mission-stats">
              <div class="stat-item" v-for="(stat, index) in missionStats" :key="index">
                <div class="stat-number">{{ stat.number }}+</div>
                <div class="stat-label">{{ stat.label }}</div>
              </div>
            </div>
          </div>
          <div class="mission-image" v-animate="'fadeInRight'">
            <img src="https://via.placeholder.com/600x400/3a7bd5/ffffff?text=我们的使命" alt="我们的使命">
          </div>
        </div>
      </section>

      <!-- 我们的价值观 -->
      <section class="values-section">
        <div class="section-header" v-animate="'fadeInUp'">
          <h2>我们的价值观</h2>
          <p>这些核心价值观指导着我们的一切行动</p>
        </div>
        <div class="values-grid">
          <div
              class="value-card"
              v-for="(value, index) in values"
              :key="index"
              :style="{ '--card-color': value.color }"
              v-animate="'fadeInUp'"
              :data-delay="index * 100"
          >
            <div class="value-icon">
              <i :class="value.icon"></i>
            </div>
            <h3>{{ value.title }}</h3>
            <p>{{ value.description }}</p>
          </div>
        </div>
      </section>

      <!-- 我们的团队 -->
      <section class="team-section">
        <div class="section-header" v-animate="'fadeInUp'">
          <h2>我们的团队</h2>
          <p>专业、热情、富有爱心的团队成员</p>
        </div>
        <div class="team-members">
          <div
              class="member-card"
              v-for="(member, index) in teamMembers"
              :key="index"
              v-animate="'fadeInUp'"
              :data-delay="index * 100"
          >
            <div class="member-image">
              <img :src="member.image" :alt="member.name">
              <div class="member-social">
                <a v-for="(social, sIndex) in member.social" :key="sIndex" :href="social.link">
                  <i :class="social.icon"></i>
                </a>
              </div>
            </div>
            <div class="member-info">
              <h3>{{ member.name }}</h3>
              <p class="position">{{ member.position }}</p>
              <p class="bio">{{ member.bio }}</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 我们的历史 -->
      <section class="history-section">
        <div class="section-header" v-animate="'fadeInUp'">
          <h2>我们的历史</h2>
          <p>从一个小小的善念发展到今天的历程</p>
        </div>
        <div class="timeline">
          <div
              class="timeline-item"
              v-for="(event, index) in historyEvents"
              :key="index"
              :class="{ 'left': index % 2 === 0, 'right': index % 2 !== 0 }"
              v-animate="index % 2 === 0 ? 'fadeInLeft' : 'fadeInRight'"
          >
            <div class="timeline-content">
              <div class="timeline-year">{{ event.year }}</div>
              <h3>{{ event.title }}</h3>
              <p>{{ event.description }}</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 合作伙伴 -->
      <section class="partners-section">
        <div class="section-header" v-animate="'fadeInUp'">
          <h2>我们的合作伙伴</h2>
          <p>感谢这些组织与我们携手同行</p>
        </div>
        <div class="partners-grid">
          <div
              class="partner-logo"
              v-for="(partner, index) in partners"
              :key="index"
              v-animate="'fadeIn'"
              :data-delay="index * 50"
          >
            <img :src="partner.logo" :alt="partner.name">
          </div>
        </div>
      </section>

      <!-- 加入我们 -->
      <section class="join-section">
        <div class="join-container" v-animate="'fadeIn'">
          <h2>加入我们的使命</h2>
          <p>无论您是希望成为志愿者、捐助者还是合作伙伴，我们都欢迎您的加入</p>
          <div class="join-buttons">
            <el-button type="primary" round @click="donateNow">立即捐助</el-button>
            <el-button type="success" round @click="joinAsVolunteer">成为志愿者</el-button>
            <el-button type="info" round @click="contactUs">联系我们</el-button>
          </div>
        </div>
      </section>
    </main>

    <!-- 共用页脚 -->
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
    <el-backtop :right="50" :bottom="50" target=".about-page">
      <i class="el-icon-caret-top"></i>
    </el-backtop>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
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
const activeIndex = ref('5')
const handleSelect = (index) => {
  activeIndex.value = index
  // 实际项目中这里应该跳转到对应路由
  if (index !== '5') {
    ElMessage.success(`导航到 ${index}`)
  }
}

const donateNow = () => {
  ElMessage.info('跳转到捐助页面')
}

const joinAsVolunteer = () => {
  ElMessage.info('跳转到志愿者页面')
}

const contactUs = () => {
  ElMessage.info('跳转到联系我们页面')
}

const goHome = () => {
  activeIndex.value = '1'
  // router.push('/')
}

// 我们的使命数据
const missionStats = ref([
  { number: 12, label: '年经验' },
  { number: 50, label: '个项目' },
  { number: 10000, label: '受益者' },
  { number: 500, label: '志愿者' }
])

// 我们的价值观
const values = ref([
  {
    title: '诚信',
    description: '我们坚持最高的道德标准，确保每一分捐款都用于慈善事业',
    icon: 'el-icon-medal',
    color: '#3a7bd5'
  },
  {
    title: '透明',
    description: '我们公开所有财务信息，让捐助者清楚知道资金的去向',
    icon: 'el-icon-view',
    color: '#ff9a5a'
  },
  {
    title: '责任',
    description: '我们对捐助者和受益者负责，确保项目有效实施',
    icon: 'el-icon-finished',
    color: '#00d2ff'
  },
  {
    title: '同理心',
    description: '我们以同理心对待每一位需要帮助的人',
    icon: 'el-icon-user',
    color: '#a18cd1'
  },
  {
    title: '创新',
    description: '我们不断寻求创新的方式来解决社会问题',
    icon: 'el-icon-lightbulb',
    color: '#667eea'
  },
  {
    title: '合作',
    description: '我们相信通过合作可以产生更大的影响力',
    icon: 'el-icon-connection',
    color: '#4facfe'
  }
])

// 我们的团队
const teamMembers = ref([
  {
    name: '张明',
    position: '创始人兼CEO',
    bio: '张明先生拥有15年非营利组织管理经验，致力于儿童教育发展',
    image: 'https://via.placeholder.com/300x300/3a7bd5/ffffff?text=张明',
    social: [
      { icon: 'el-icon-weibo', link: '#' },
      { icon: 'el-icon-link', link: '#' }
    ]
  },
  {
    name: '李华',
    position: '项目总监',
    bio: '李华女士专注于项目管理，确保每个项目都能产生最大影响',
    image: 'https://via.placeholder.com/300x300/ff9a5a/ffffff?text=李华',
    social: [
      { icon: 'el-icon-weibo', link: '#' },
      { icon: 'el-icon-link', link: '#' }
    ]
  },
  {
    name: '王芳',
    position: '志愿者协调员',
    bio: '王芳负责志愿者招募和培训，建立了一支强大的志愿者团队',
    image: 'https://via.placeholder.com/300x300/00d2ff/ffffff?text=王芳',
    social: [
      { icon: 'el-icon-weibo', link: '#' },
      { icon: 'el-icon-link', link: '#' }
    ]
  },
  {
    name: '赵强',
    position: '财务主管',
    bio: '赵强负责财务管理，确保所有资金使用透明合规',
    image: 'https://via.placeholder.com/300x300/a18cd1/ffffff?text=赵强',
    social: [
      { icon: 'el-icon-weibo', link: '#' },
      { icon: 'el-icon-link', link: '#' }
    ]
  }
])

// 我们的历史
const historyEvents = ref([
  {
    year: '2010',
    title: '爱心慈善成立',
    description: '由几位热心公益的朋友共同创立，最初只有一个小办公室和5名志愿者'
  },
  {
    year: '2012',
    title: '第一个助学项目启动',
    description: '在云南山区启动第一个助学项目，帮助50名儿童重返校园'
  },
  {
    year: '2015',
    title: '获得非营利组织认证',
    description: '正式注册为非营利组织，获得政府认可'
  },
  {
    year: '2017',
    title: '志愿者网络扩展',
    description: '志愿者人数突破200人，项目覆盖3个省份'
  },
  {
    year: '2020',
    title: '抗击新冠疫情',
    description: '在疫情期间为受影响家庭提供紧急援助'
  },
  {
    year: '2023',
    title: '十周年庆典',
    description: '庆祝成立十周年，累计帮助超过10000名受益者'
  }
])

// 合作伙伴
const partners = ref([
  {
    name: 'ABC基金会',
    logo: 'https://via.placeholder.com/150x80/3a7bd5/ffffff?text=ABC基金会'
  },
  {
    name: 'XYZ企业',
    logo: 'https://via.placeholder.com/150x80/ff9a5a/ffffff?text=XYZ企业'
  },
  {
    name: '爱心教育',
    logo: 'https://via.placeholder.com/150x80/00d2ff/ffffff?text=爱心教育'
  },
  {
    name: '阳光医疗',
    logo: 'https://via.placeholder.com/150x80/a18cd1/ffffff?text=阳光医疗'
  },
  {
    name: '未来科技',
    logo: 'https://via.placeholder.com/150x80/667eea/ffffff?text=未来科技'
  },
  {
    name: '绿色环保',
    logo: 'https://via.placeholder.com/150x80/4facfe/ffffff?text=绿色环保'
  }
])
</script>

<style scoped>
/* 基础样式 */
.about-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f8fafc;
  color: #333;
  transition: all 0.3s ease;
}

.about-page.dark-mode {
  background-color: #121212;
  color: #e0e0e0;
}

/* 共用顶部导航栏 - 与主页一致 */
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

/* 关于我们内容 */
.about-content {
  flex: 1;
  margin-top: 70px;
}

/* 英雄区域 */
.about-hero {
  height: 400px;
  background-image: url('https://via.placeholder.com/1920x600/3a7bd5/ffffff?text=关于我们');
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.hero-content {
  position: relative;
  z-index: 1;
  text-align: center;
  color: white;
  max-width: 800px;
  padding: 0 20px;
}

.hero-content h1 {
  font-size: 48px;
  margin-bottom: 20px;
}

.hero-content p {
  font-size: 20px;
  opacity: 0.9;
}

/* 我们的使命 */
.mission-section {
  padding: 80px 20px;
  background-color: #fff;
}

.dark-mode .mission-section {
  background-color: #1a1a1a;
}

.section-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 50px;
}

.mission-content {
  flex: 1;
}

.section-title {
  font-size: 36px;
  margin-bottom: 20px;
  color: #3a7bd5;
  position: relative;
  padding-bottom: 15px;
}

.dark-mode .section-title {
  color: #ff9a5a;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 60px;
  height: 3px;
  background-color: #3a7bd5;
}

.dark-mode .section-title::after {
  background-color: #ff9a5a;
}

.mission-content p {
  margin-bottom: 20px;
  line-height: 1.6;
  color: #555;
}

.dark-mode .mission-content p {
  color: #bbb;
}

.mission-stats {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-top: 30px;
}

.stat-item {
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  transition: all 0.3s ease;
}

.dark-mode .stat-item {
  background-color: #222;
}

.stat-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.dark-mode .stat-item:hover {
  box-shadow: 0 5px 15px rgba(255, 255, 255, 0.05);
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #3a7bd5;
  margin-bottom: 5px;
}

.dark-mode .stat-number {
  color: #ff9a5a;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.dark-mode .stat-label {
  color: #999;
}

.mission-image {
  flex: 1;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
  transition: transform 0.5s ease;
}

.mission-image:hover {
  transform: scale(1.03);
}

.mission-image img {
  width: 100%;
  height: auto;
  display: block;
}

/* 我们的价值观 */
.values-section {
  padding: 80px 20px;
  background-color: #f5f7fa;
}

.dark-mode .values-section {
  background-color: #121212;
}

.section-header {
  text-align: center;
  max-width: 800px;
  margin: 0 auto 50px;
}

.section-header h2 {
  font-size: 36px;
  margin-bottom: 15px;
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

.values-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.value-card {
  background-color: #fff;
  border-radius: 10px;
  padding: 30px;
  text-align: center;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  z-index: 1;
}

.dark-mode .value-card {
  background-color: #1e1e1e;
}

.value-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 5px;
  background-color: var(--card-color);
}

.value-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

.dark-mode .value-card:hover {
  box-shadow: 0 15px 30px rgba(255, 255, 255, 0.05);
}

.value-icon {
  width: 70px;
  height: 70px;
  margin: 0 auto 20px;
  background-color: rgba(58, 123, 213, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  color: var(--card-color);
}

.value-card h3 {
  margin-bottom: 15px;
  font-size: 22px;
}

.value-card p {
  color: #666;
  line-height: 1.6;
}

.dark-mode .value-card p {
  color: #bbb;
}

/* 我们的团队 */
.team-section {
  padding: 80px 20px;
  background-color: #fff;
}

.dark-mode .team-section {
  background-color: #1a1a1a;
}

.team-members {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.member-card {
  background-color: #f5f7fa;
  border-radius: 10px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.dark-mode .member-card {
  background-color: #222;
}

.member-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

.dark-mode .member-card:hover {
  box-shadow: 0 15px 30px rgba(255, 255, 255, 0.05);
}

.member-image {
  position: relative;
  height: 300px;
}

.member-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.member-social {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
  display: flex;
  justify-content: center;
  gap: 15px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.member-card:hover .member-social {
  opacity: 1;
}

.member-social a {
  color: white;
  font-size: 20px;
  transition: transform 0.3s ease;
}

.member-social a:hover {
  transform: translateY(-3px);
}

.member-info {
  padding: 20px;
  text-align: center;
}

.member-info h3 {
  margin-bottom: 5px;
  font-size: 20px;
}

.position {
  color: #3a7bd5;
  font-weight: bold;
  margin-bottom: 15px;
}

.dark-mode .position {
  color: #ff9a5a;
}

.bio {
  color: #666;
  line-height: 1.6;
}

.dark-mode .bio {
  color: #bbb;
}

/* 我们的历史 */
.history-section {
  padding: 80px 20px;
  background-color: #f5f7fa;
}

.dark-mode .history-section {
  background-color: #121212;
}

.timeline {
  position: relative;
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 0;
}

.timeline::before {
  content: '';
  position: absolute;
  top: 0;
  left: 50%;
  width: 2px;
  height: 100%;
  background-color: #3a7bd5;
  transform: translateX(-50%);
}

.dark-mode .timeline::before {
  background-color: #ff9a5a;
}

.timeline-item {
  padding: 20px 50px;
  position: relative;
  width: 50%;
  box-sizing: border-box;
}

.timeline-item.left {
  left: 0;
}

.timeline-item.right {
  left: 50%;
}

.timeline-content {
  padding: 30px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  position: relative;
}

.dark-mode .timeline-content {
  background-color: #1e1e1e;
  box-shadow: 0 5px 15px rgba(255, 255, 255, 0.05);
}

.timeline-content::before {
  content: '';
  position: absolute;
  top: 30px;
  width: 20px;
  height: 20px;
  background-color: #fff;
  transform: rotate(45deg);
  z-index: -1;
}

.dark-mode .timeline-content::before {
  background-color: #1e1e1e;
}

.timeline-item.left .timeline-content::before {
  right: -10px;
}

.timeline-item.right .timeline-content::before {
  left: -10px;
}

.timeline-year {
  position: absolute;
  top: 20px;
  width: 100px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  background-color: #3a7bd5;
  color: white;
  border-radius: 20px;
  font-weight: bold;
}

.dark-mode .timeline-year {
  background-color: #ff9a5a;
}

.timeline-item.left .timeline-year {
  right: -120px;
}

.timeline-item.right .timeline-year {
  left: -120px;
}

.timeline-content h3 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #3a7bd5;
}

.dark-mode .timeline-content h3 {
  color: #ff9a5a;
}

/* 合作伙伴 */
.partners-section {
  padding: 80px 20px;
  background-color: #fff;
}

.dark-mode .partners-section {
  background-color: #1a1a1a;
}

.partners-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
  align-items: center;
}

.partner-logo {
  background-color: #f5f7fa;
  padding: 30px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  height: 120px;
}

.dark-mode .partner-logo {
  background-color: #222;
}

.partner-logo:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.dark-mode .partner-logo:hover {
  box-shadow: 0 5px 15px rgba(255, 255, 255, 0.05);
}

.partner-logo img {
  max-width: 100%;
  max-height: 60px;
  filter: grayscale(100%);
  opacity: 0.7;
  transition: all 0.3s ease;
}

.partner-logo:hover img {
  filter: grayscale(0%);
  opacity: 1;
}

/* 加入我们 */
.join-section {
  padding: 100px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  text-align: center;
  color: white;
  position: relative;
  overflow: hidden;
}

.dark-mode .join-section {
  background: linear-gradient(135deg, #ff9a5a 0%, #ff6e7f 100%);
}

.join-container {
  max-width: 800px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

.join-container h2 {
  font-size: 42px;
  margin-bottom: 20px;
}

.join-container p {
  font-size: 20px;
  margin-bottom: 30px;
  opacity: 0.9;
}

.join-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  flex-wrap: wrap;
}

.join-buttons .el-button {
  padding: 12px 30px;
  font-size: 16px;
  font-weight: bold;
  transition: all 0.3s ease;
}

.join-buttons .el-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

/* 共用页脚 - 与主页一致 */
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
  .section-container {
    flex-direction: column;
  }

  .mission-image {
    order: -1;
    max-width: 600px;
    margin: 0 auto;
  }

  .timeline::before {
    left: 30px;
  }

  .timeline-item {
    width: 100%;
    padding-left: 80px;
    padding-right: 20px;
  }

  .timeline-item.left,
  .timeline-item.right {
    left: 0;
  }

  .timeline-item.left .timeline-year,
  .timeline-item.right .timeline-year {
    left: 0;
    right: auto;
  }

  .timeline-item.left .timeline-content::before {
    right: auto;
    left: -10px;
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

  .hero-content h1 {
    font-size: 36px;
  }

  .hero-content p {
    font-size: 18px;
  }

  .join-buttons {
    flex-direction: column;
    align-items: center;
  }

  .footer-links {
    grid-template-columns: 1fr;
  }

  .links-column {
    text-align: center;
  }
}

@media (max-width: 576px) {
  .hero-content h1 {
    font-size: 30px;
  }

  .section-header h2 {
    font-size: 28px;
  }

  .mission-stats {
    grid-template-columns: 1fr;
  }

  .join-container h2 {
    font-size: 32px;
  }

  .join-container p {
    font-size: 18px;
  }
}
</style>