<template>
  <div class="dashboard-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>平台数据概览</span>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-icon" style="background-color: #f0f9eb;">
                <el-icon color="#67C23A" :size="30"><user /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-title">注册用户</div>
                <div class="stat-value">1,234</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-icon" style="background-color: #fdf6ec;">
                <el-icon color="#E6A23C" :size="30"><coin /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-title">捐赠总额</div>
                <div class="stat-value">¥ 456,789</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-icon" style="background-color: #ecf5ff;">
                <el-icon color="#409EFF" :size="30"><help /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-title">受助项目</div>
                <div class="stat-value">56</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-icon" style="background-color: #fef0f0;">
                <el-icon color="#F56C6C" :size="30"><bell /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-title">待处理申请</div>
                <div class="stat-value">12</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="16">
          <el-card shadow="hover">
            <div id="donation-chart" style="height: 350px;"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <div id="project-chart" style="height: 350px;"></div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { User, Coin, Help, Bell } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  components: { User, Coin, Help, Bell },
  mounted() {
    this.initCharts()
  },
  methods: {
    initCharts() {
      // 捐赠趋势图
      const donationChart = echarts.init(document.getElementById('donation-chart'))
      donationChart.setOption({
        title: { text: '捐赠趋势', left: 'center' },
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月']
        },
        yAxis: { type: 'value' },
        series: [{
          data: [120, 200, 150, 80, 70, 110, 130],
          type: 'bar',
          itemStyle: { color: '#5D9CEC' }
        }]
      })

      // 项目分布图
      const projectChart = echarts.init(document.getElementById('project-chart'))
      projectChart.setOption({
        title: { text: '项目分布', left: 'center' },
        tooltip: { trigger: 'item' },
        series: [{
          name: '项目类型',
          type: 'pie',
          radius: '70%',
          data: [
            { value: 35, name: '教育助学' },
            { value: 25, name: '医疗救助' },
            { value: 20, name: '扶贫济困' },
            { value: 15, name: '环境保护' },
            { value: 5, name: '其他' }
          ],
          itemStyle: {
            borderRadius: 5,
            borderColor: '#fff',
            borderWidth: 2,
            color: function(params) {
              const colorList = ['#5D9CEC', '#A0D468', '#FFCE54', '#ED5565', '#AC92EC']
              return colorList[params.dataIndex]
            }
          },
          label: { show: true, formatter: '{b}: {d}%' }
        }]
      })
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
  color: var(--charity-primary);
}

.stat-card {
  margin-bottom: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.stat-info {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 22px;
  font-weight: bold;
  color: #303133;
}
</style>