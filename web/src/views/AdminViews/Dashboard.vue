<template>
  <div class="dashboard-container">
    <!-- 页面标题 - 添加背景渐变和阴影 -->
    <div class="page-header">
      <div class="header-content">
        <h1>慈善平台数据总览</h1>
        <div class="header-tags">
          <el-tag type="success" effect="dark">
            <el-icon><Refresh /></el-icon> 实时更新
          </el-tag>
          <el-tag type="info" effect="dark">
            <el-icon><Clock /></el-icon> {{ currentTime }}
          </el-tag>
        </div>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="refreshAllData" :loading="refreshing">
          <el-icon><Refresh /></el-icon> 刷新数据
        </el-button>
        <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="fetchCustomRangeData"
            class="date-picker"
        />
      </div>
    </div>
    <!-- 底部概览数据 - 添加网格布局 -->
    <div class="overview-section">
      <el-card shadow="hover" class="overview-card">
        <template #header>
          <div class="overview-header">
            <el-icon><DataAnalysis /></el-icon>
            <span>平台概览数据</span>
          </div>
        </template>
        <div class="overview-grid">
          <div
              class="overview-item"
              v-for="(item, index) in overviewItems"
              :key="index"
              :style="{ '--item-color': overviewColors[index % overviewColors.length] }"
          >
            <div class="overview-icon">
              <el-icon :size="24">
                <component :is="item.icon" />
              </el-icon>
            </div>
            <div class="overview-content">
              <div class="overview-label">{{ item.label }}</div>
              <div class="overview-value">{{ item.value }}</div>
              <div class="overview-subtext">{{ item.subtext }}</div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 主要图表区域 - 添加卡片悬浮效果 -->
    <div class="chart-area">
      <el-row :gutter="20">
        <!-- 用户统计 -->
        <el-col :xs="24" :sm="12">
          <div class="chart-wrapper">
            <el-card shadow="hover" class="chart-card">
              <template #header>
                <div class="chart-header">
                  <span>用户统计</span>
                  <el-tag type="info" effect="dark">今日新增: {{ realtimeData.todayNewUsers || 0 }}</el-tag>
                </div>
              </template>
              <div class="chart-container">
                <div ref="userTypeChart" style="width: 100%; height: 300px;"></div>
              </div>
              <div class="chart-footer">
                <el-icon><User /></el-icon>
                <span>总用户数: {{ overviewData.users?.total || 0 }}</span>
              </div>
            </el-card>
          </div>
        </el-col>

        <!-- 捐赠统计 -->
        <el-col :xs="24" :sm="12">
          <div class="chart-wrapper">
            <el-card shadow="hover" class="chart-card">
              <template #header>
                <div class="chart-header">
                  <span>捐赠统计</span>
                  <el-tag type="info" effect="dark">今日捐赠: ¥{{ formatNumber(realtimeData.todayMoneyDonations || 0) }}</el-tag>
                </div>
              </template>
              <div class="chart-container">
                <div ref="donationChart" style="width: 100%; height: 300px;"></div>
              </div>
              <div class="chart-footer">
                <el-icon><Coin /></el-icon>
                <span>总捐赠额: ¥{{ formatNumber(overviewData.donations?.totalMoney || 0) }}</span>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 活动统计 -->
        <el-col :xs="24" :sm="12">
          <div class="chart-wrapper">
            <el-card shadow="hover" class="chart-card">
              <template #header>
                <div class="chart-header">
                  <span>活动统计</span>
                  <el-tag type="info" effect="dark">今日新增: {{ realtimeData.todayNewActivities || 0 }}</el-tag>
                </div>
              </template>
              <div class="chart-container">
                <div ref="activityChart" style="width: 100%; height: 300px;"></div>
              </div>
              <div class="chart-footer">
                <el-icon><Calendar /></el-icon>
                <span>总活动数: {{ overviewData.activities?.total || 0 }}</span>
              </div>
            </el-card>
          </div>
        </el-col>

        <!-- 论坛统计 -->
        <el-col :xs="24" :sm="12">
          <div class="chart-wrapper">
            <el-card shadow="hover" class="chart-card">
              <template #header>
                <div class="chart-header">
                  <span>论坛互动</span>
                  <el-tag type="info" effect="dark">今日发帖: {{ realtimeData.todayNewPosts || 0 }}</el-tag>
                </div>
              </template>
              <div class="chart-container">
                <div ref="forumChart" style="width: 100%; height: 300px;"></div>
              </div>
              <div class="chart-footer">
                <el-icon><ChatLineRound /></el-icon>
                <span>总帖子数: {{ overviewData.forum?.posts || 0 }}</span>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>

      <!-- 趋势图表 - 添加全宽卡片 -->
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="24">
          <div class="chart-wrapper">
            <el-card shadow="hover" class="chart-card trend-card">
              <template #header>
                <div class="chart-header">
                  <span>捐赠趋势</span>
                  <el-radio-group v-model="trendType" size="small" @change="updateTrendChart">
                    <el-radio-button label="money">金钱捐赠</el-radio-button>
                    <el-radio-button label="item">物品捐赠</el-radio-button>
                  </el-radio-group>
                </div>
              </template>
              <div class="chart-container">
                <div ref="trendChart" style="width: 100%; height: 350px;"></div>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </div>
    <!-- 实时数据卡片 - 添加悬浮动画和渐变背景 -->
    <div class="realtime-cards">
      <div class="overview-header">
        <el-icon><DataAnalysis /></el-icon>
        <span>近期数据查询</span>
      </div>
      <el-row :gutter="20">
        <el-col
            :xs="24"
            :sm="12"
            :md="8"
            :lg="6"
            v-for="(item, index) in realtimeDataItems"
            :key="index"
        >
          <div
              class="card-wrapper"
              :style="{ '--card-color': item.color }"
              @mouseenter="handleCardHover(index)"
              @mouseleave="handleCardLeave(index)"
          >
            <el-card
                shadow="hover"
                class="realtime-card"
                :class="{ 'card-hover': activeHoverIndex === index }"
            >
              <div class="card-content">
                <div class="card-icon">
                  <el-icon :size="28">
                    <component :is="item.icon" />
                  </el-icon>
                </div>
                <div class="card-info">
                  <div class="card-title">{{ item.title }}</div>
                  <div class="card-value">{{ item.value }}</div>
                  <div class="card-trend">
                    <span :class="item.trendClass">{{ item.trend }}</span>
                    <el-icon :class="item.trendIconClass">
                      <component :is="item.trendIcon" />
                    </el-icon>
                  </div>
                </div>
              </div>
              <div class="card-wave"></div>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </div>
    <!-- 数据加载动画 -->
    <transition name="fade">
      <div class="loading-overlay" v-if="loading">
        <div class="loading-content">
          <el-icon class="loading-icon" :size="50"><Loading /></el-icon>
          <p>数据加载中...</p>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import * as echarts from 'echarts'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import {
  Refresh,
  User,
  Coin,
  Box,
  Calendar,
  ChatLineRound,
  TrendCharts,
  Loading,
  Clock,
  DataAnalysis,
  Setting,
  Tickets,
  Goods,
  ChatDotRound,
  Bell
} from '@element-plus/icons-vue'

// 图表引用
const userTypeChart = ref(null)
const donationChart = ref(null)
const activityChart = ref(null)
const forumChart = ref(null)
const trendChart = ref(null)

// 数据状态
const overviewData = ref({})
const userAnalysis = ref({})
const donationAnalysis = ref({})
const activityAnalysis = ref({})
const forumAnalysis = ref({})
const realtimeData = ref({})
const dateRange = ref([])
const trendType = ref('money')
const loading = ref(true)
const refreshing = ref(false)
const activeHoverIndex = ref(-1)
const currentTime = ref('')

// 图表实例
let charts = {
  userType: null,
  donation: null,
  activity: null,
  forum: null,
  trend: null
}

// 配色方案
const cardColors = ['#5B8FF9', '#5AD8A6', '#5D7092', '#F6BD16', '#6F5EF9', '#6DC8EC']
const overviewColors = ['#9254DE', '#36CFC9', '#FF9C6E', '#FFC069', '#597EF7', '#FF85C0']

// 初始化图表
const initCharts = () => {
  // 销毁旧实例
  Object.values(charts).forEach(chart => chart?.dispose())

  // 创建新实例
  charts = {
    userType: echarts.init(userTypeChart.value),
    donation: echarts.init(donationChart.value),
    activity: echarts.init(activityChart.value),
    forum: echarts.init(forumChart.value),
    trend: echarts.init(trendChart.value)
  }

  // 响应式调整
  const resizeCharts = () => Object.values(charts).forEach(chart => chart?.resize())
  window.addEventListener('resize', resizeCharts)
}

// 数据格式化
const formatNumber = num => {
  if (typeof num !== 'number') return '0'
  return num.toLocaleString('zh-CN')
}

const formatCurrency = num => {
  if (typeof num !== 'number') return '¥0'
  return `¥${num.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
}

// API请求函数
const fetchData = async (endpoint) => {
  try {
    const { data } = await axios.get(`/api/visualization/${endpoint}`)
    return data.data
  } catch (error) {
    console.error(`获取${endpoint}数据失败:`, error)
    ElMessage.error(`获取${endpoint.replace('-', '')}数据失败`)
    return null
  }
}

// 数据获取与处理
const loadAllData = async () => {
  loading.value = true
  try {
    const [
      overview,
      usersAnalysis,
      donationsAnalysis,
      activitiesAnalysis,
      forumAnalysisRes,
      realtime
    ] = await Promise.all([
      fetchData('overview'),
      fetchData('users/analysis'),
      fetchData('donations/analysis'),
      fetchData('activities/analysis'),
      fetchData('forum/analysis'),
      fetchData('realtime')
    ])

    if (overview) {
      overviewData.value = overview
      // updateOverviewItems()
    }

    if (usersAnalysis) {
      userAnalysis.value = usersAnalysis
      renderUserChart()
    }

    if (donationsAnalysis) {
      donationAnalysis.value = donationsAnalysis
      renderDonationChart()
      renderTrendChart()
    }

    if (activitiesAnalysis) {
      activityAnalysis.value = activitiesAnalysis
      renderActivityChart()
    }

    if (forumAnalysisRes) {
      forumAnalysis.value = forumAnalysisRes
      renderForumChart()
    }

    if (realtime) {
      realtimeData.value = realtime
      updateRealtimeCards()
    }
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

// 图表渲染函数
const renderUserChart = () => {
  const { userTypeDistribution } = userAnalysis.value
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center',
      textStyle: {
        color: '#666'
      }
    },
    series: [{
      name: '用户分布',
      type: 'pie',
      radius: ['50%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '18',
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: [
        { value: userTypeDistribution?.donor || 0, name: '捐赠者', itemStyle: { color: '#5B8FF9' } },
        { value: userTypeDistribution?.recipient || 0, name: '受助者', itemStyle: { color: '#5AD8A6' } },
        { value: userTypeDistribution?.enterprise || 0, name: '企业用户', itemStyle: { color: '#F6BD16' } }
      ]
    }]
  }
  charts.userType.setOption(option)
}

const renderDonationChart = () => {
  const { moneyDonations, itemDonations } = donationAnalysis.value
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['金钱捐赠', '物品捐赠'],
      textStyle: {
        color: '#666'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['总数量', '已批准', '待审核'],
      axisLine: {
        lineStyle: {
          color: '#eee'
        }
      },
      axisLabel: {
        color: '#666'
      }
    },
    yAxis: [
      {
        type: 'value',
        name: '金额(元)',
        position: 'left',
        axisLine: {
          lineStyle: {
            color: '#5B8FF9'
          }
        },
        axisLabel: {
          formatter: '{value}'
        },
        splitLine: {
          lineStyle: {
            color: '#f5f5f5'
          }
        }
      },
      {
        type: 'value',
        name: '物品数量',
        position: 'right',
        axisLine: {
          lineStyle: {
            color: '#F6BD16'
          }
        },
        axisLabel: {
          formatter: '{value}'
        },
        splitLine: {
          show: false
        }
      }
    ],
    series: [
      {
        name: '金钱捐赠',
        type: 'bar',
        yAxisIndex: 0,
        barWidth: '30%',
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#5D8CAE' }, // 主色
            { offset: 1, color: '#88B2CC' }  // 辅助色
          ]),
          borderRadius: [0, 4, 4, 0]
        },
        data: [
          moneyDonations?.totalAmount || 0,
          moneyDonations?.approvedCount || 0,
          moneyDonations?.pendingCount || 0
        ]
      },
      {
        name: '物品捐赠',
        type: 'bar',
        yAxisIndex: 1,
        barWidth: '30%',
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#E3B08D' }, // 主色
            { offset: 1, color: '#D1A58E' }  // 辅助色
          ]),
          borderRadius: [0, 4, 4, 0]
        },
        data: [
          itemDonations?.totalCount || 0,
          itemDonations?.approvedCount || 0,
          itemDonations?.pendingCount || 0
        ]
      }
    ]
  }
  charts.donation.setOption(option)
}

const renderActivityChart = () => {
  const { statusDistribution } = activityAnalysis.value
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'horizontal',
      bottom: 0,
      textStyle: {
        color: '#666'
      }
    },
    series: [{
      name: '活动状态',
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '45%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '18',
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: [
        { value: statusDistribution?.planning || 0, name: '计划中', itemStyle: { color: '#597EF7' } },
        { value: statusDistribution?.ongoing || 0, name: '进行中', itemStyle: { color: '#36CFC9' } },
        { value: statusDistribution?.completed || 0, name: '已完成', itemStyle: { color: '#9254DE' } }
      ]
    }]
  }
  charts.activity.setOption(option)
}

const renderForumChart = () => {
  const { interactions } = forumAnalysis.value
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['回复', '点赞'],
      axisLine: {
        lineStyle: {
          color: '#eee'
        }
      },
      axisLabel: {
        color: '#666'
      }
    },
    yAxis: {
      type: 'value',
      axisLine: {
        lineStyle: {
          color: '#eee'
        }
      },
      axisLabel: {
        color: '#666'
      },
      splitLine: {
        lineStyle: {
          color: '#f5f5f5'
        }
      }
    },
    series: [{
      name: '互动量',
      type: 'bar',
      barWidth: '40%',
      itemStyle: {
        color: function(params) {
          const colorList = ['#FF85C0', '#FF9C6E'];
          return colorList[params.dataIndex];
        },
        borderRadius: [4, 4, 0, 0]
      },
      data: [
        { value: interactions?.replies || 0, name: '回复' },
        { value: interactions?.likes || 0, name: '点赞' }
      ]
    }]
  }
  charts.forum.setOption(option)
}

const renderTrendChart = () => {
  const { donationTrend } = donationAnalysis.value || []
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#6a7985'
        }
      }
    },
    legend: {
      data: [trendType.value === 'money' ? '金钱捐赠' : '物品捐赠'],
      textStyle: {
        color: '#666'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: donationTrend?.map(item => item.month) || [],
      axisLine: {
        lineStyle: {
          color: '#eee'
        }
      },
      axisLabel: {
        color: '#666'
      }
    },
    yAxis: {
      type: 'value',
      axisLine: {
        lineStyle: {
          color: '#eee'
        }
      },
      axisLabel: {
        color: '#666'
      },
      splitLine: {
        lineStyle: {
          color: '#f5f5f5'
        }
      }
    },
    series: [
      {
        name: trendType.value === 'money' ? '金钱捐赠' : '物品捐赠',
        type: 'line',
        stack: '总量',
        smooth: true,
        lineStyle: {
          width: 3,
          color: trendType.value === 'money' ? '#5B8FF9' : '#5AD8A6'
        },
        showSymbol: false,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: trendType.value === 'money' ? 'rgba(91, 143, 249, 0.3)' : 'rgba(90, 216, 166, 0.3)'
            },
            {
              offset: 1,
              color: trendType.value === 'money' ? 'rgba(91, 143, 249, 0.1)' : 'rgba(90, 216, 166, 0.1)'
            }
          ])
        },
        emphasis: {
          focus: 'series'
        },
        data: trendType.value === 'money'
            ? donationTrend?.map(item => item.moneyAmount) || []
            : donationTrend?.map(item => item.itemCount) || []
      }
    ]
  }
  charts.trend.setOption(option)
}

// 数据展示组件
const realtimeDataItems = computed(() => {
  const { todayNewUsers, todayMoneyDonations, todayItemDonations, todayNewActivities, todayNewPosts, todayNewReplies } = realtimeData.value
  return [
    {
      title: '新增用户',
      value: todayNewUsers || 0,
      icon: User,
      color: cardColors[0],
      trend: '+12%',
      trendClass: 'positive',
      trendIcon: 'Top',
      trendIconClass: 'positive-icon'
    },
    {
      title: '金钱捐赠',
      value: formatCurrency(todayMoneyDonations),
      icon: Coin,
      color: cardColors[1],
      trend: '+8.5%',
      trendClass: 'positive',
      trendIcon: 'Top',
      trendIconClass: 'positive-icon'
    },
    {
      title: '物品捐赠',
      value: todayItemDonations || 0,
      icon: Box,
      color: cardColors[2],
      trend: '+5.2%',
      trendClass: 'positive',
      trendIcon: 'Top',
      trendIconClass: 'positive-icon'
    },
    {
      title: '新增活动',
      value: todayNewActivities || 0,
      icon: Calendar,
      color: cardColors[3],
      trend: '-2.1%',
      trendClass: 'negative',
      trendIcon: 'Bottom',
      trendIconClass: 'negative-icon'
    },
    {
      title: '新增帖子',
      value: todayNewPosts || 0,
      icon: ChatLineRound,
      color: cardColors[4],
      trend: '+15.3%',
      trendClass: 'positive',
      trendIcon: 'Top',
      trendIconClass: 'positive-icon'
    },
    {
      title: '新增回复',
      value: todayNewReplies || 0,
      icon: TrendCharts,
      color: cardColors[5],
      trend: '+9.7%',
      trendClass: 'positive',
      trendIcon: 'Top',
      trendIconClass: 'positive-icon'
    }
  ]
})

const overviewItems = computed(() => {
  const { users, activities, donations, forum, announcements } = overviewData.value
  return [
    {
      label: '总用户数',
      value: users?.total || 0,
      subtext: `管理员: ${users?.admin || 0} | 企业: ${users?.enterprise || 0}`,
      icon: User
    },
    {
      label: '总活动数',
      value: activities?.total || 0,
      subtext: `进行中: ${activities?.ongoing || 0} | 已完成: ${activities?.completed || 0}`,
      icon: Calendar
    },
    {
      label: '总捐赠额',
      value: formatCurrency(donations?.totalMoney),
      subtext: `已批准: ${formatCurrency(donations?.approvedMoney)}`,
      icon: Coin
    },
    {
      label: '总捐赠物品',
      value: donations?.totalItems || 0,
      subtext: `已批准: ${donations?.approvedCount || 0}`,
      icon: Box
    },
    {
      label: '论坛互动',
      value: forum?.posts || 0,
      subtext: `回复: ${forum?.replies || 0} | 点赞: ${forum?.likes || 0}`,
      icon: ChatDotRound
    },
    {
      label: '公告数量',
      value: announcements || 0,
      subtext: '系统公告',
      icon: Bell
    }
  ]
})

// 交互函数
const handleCardHover = (index) => {
  activeHoverIndex.value = index
}

const handleCardLeave = () => {
  activeHoverIndex.value = -1
}

const refreshAllData = async () => {
  refreshing.value = true
  await loadAllData()
}

const updateTrendChart = () => {
  renderTrendChart()
}

const fetchCustomRangeData = () => {
  if (dateRange.value && dateRange.value.length === 2) {
    loadAllData()
  }
}

// 更新时间
const updateCurrentTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  })
}

// 初始化
onMounted(async () => {
  initCharts()
  updateCurrentTime()
  setInterval(updateCurrentTime, 1000)
  await loadAllData()

  // 设置定时刷新
  setInterval(() => {
    fetchData('realtime').then(data => {
      if (data) {
        realtimeData.value = data
      }
    })
  }, 120000) // 每2分钟刷新实时数据
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f5f7fa;
  position: relative;
  min-height: 100vh;
}

/* 页面标题样式 */
.page-header {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
  background: linear-gradient(135deg, #6B73FF 0%, #000DFF 100%);
  padding: 20px;
  border-radius: 10px;
  color: white;
  box-shadow: 0 4px 12px rgba(0, 13, 255, 0.1);
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 15px;
}

.page-header h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.header-tags {
  display: flex;
  gap: 10px;
}

.header-tags .el-tag {
  font-weight: 500;
  display: flex;
  align-items: center;
}

.header-tags .el-tag i {
  margin-right: 5px;
}

.header-actions {
  display: flex;
  align-items: center;
  width: 100%;
}

.date-picker {
  margin-left: 10px;
  flex-grow: 1;
}

/* 实时数据卡片样式 */
.realtime-cards {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 10px;
  margin-top: 20px;
  margin-bottom: 20px;
}

.card-wrapper {
  position: relative;
  margin-bottom: 20px;
  --card-color: #5B8FF9;
}

.realtime-card {
  border-radius: 10px;
  border: none;
  transition: all 0.3s ease;
  overflow: hidden;
  position: relative;
  background-color: white;
}

.card-hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.card-content {
  display: flex;
  align-items: center;
  padding: 20px;
  position: relative;
  z-index: 2;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-right: 15px;
  background-color: var(--card-color);
  box-shadow: 0 4px 12px rgba(var(--card-color), 0.2);
  transition: all 0.3s ease;
}

.card-hover .card-icon {
  transform: scale(1.1);
}

.card-info {
  flex: 1;
}

.card-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.card-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
}

.positive {
  color: #67C23A;
}

.negative {
  color: #F56C6C;
}

.positive-icon {
  color: #67C23A;
  margin-left: 3px;
}

.negative-icon {
  color: #F56C6C;
  margin-left: 3px;
}

.card-wave {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 10px;
  background: linear-gradient(90deg, var(--card-color) 0%, rgba(var(--card-color), 0.3) 100%);
  transition: all 0.3s ease;
}

.card-hover .card-wave {
  height: 15px;
}

/* 图表区域样式 */
.chart-area {
  margin-bottom: 20px;
}

.chart-wrapper {
  transition: all 0.3s ease;
}

.chart-wrapper:hover {
  transform: translateY(-3px);
}

.chart-card {
  border-radius: 10px;
  border: none;
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.chart-card:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.trend-card {
  background: linear-gradient(135deg, #f6faff 0%, #f0f7ff 100%);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
}

.chart-header span {
  font-weight: 600;
  color: #303133;
}

.chart-container {
  width: 100%;
  padding: 10px;
}

.chart-footer {
  padding: 10px 20px;
  border-top: 1px solid #eee;
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #666;
}

.chart-footer i {
  margin-right: 8px;
  color: #5B8FF9;
}

/* 概览区域样式 */
.overview-section {
  margin-top: 20px;
  margin-bottom: 20px;
}

.overview-card {
  border-radius: 10px;
  border: none;
}

.overview-header {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.overview-header i {
  margin-right: 8px;
  color: #5B8FF9;
}

.overview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  padding: 15px;
}

.overview-item {
  display: flex;
  padding: 15px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  border-left: 4px solid var(--item-color);
}

.overview-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.overview-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  color: white;
  background-color: var(--item-color);
}

.overview-content {
  flex: 1;
}

.overview-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.overview-value {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 3px;
}

.overview-subtext {
  font-size: 12px;
  color: #909399;
}

/* 加载动画 */
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.loading-content {
  text-align: center;
}

.loading-icon {
  animation: rotate 2s linear infinite;
  color: #5B8FF9;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .overview-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .header-actions {
    flex-direction: column;
    gap: 10px;
  }

  .date-picker {
    margin-left: 0;
    width: 100%;
  }

  .overview-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 576px) {
  .card-content {
    flex-direction: column;
    text-align: center;
  }

  .card-icon {
    margin-right: 0;
    margin-bottom: 15px;
  }
}
</style>