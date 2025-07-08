<template>
  <div class="distribution-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>我的领取记录</span>
          <el-button type="primary" @click="fetchData" :loading="loading">刷新数据</el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="物品领取" name="item">
          <el-table
              :data="itemDistributions"
              border
              style="width: 100%"
              v-loading="loading"
              empty-text="暂无物品领取记录"
          >
            <el-table-column prop="distributionId" label="记录ID" width="100" />
            <el-table-column prop="activityId" label="活动ID" width="100" />
            <el-table-column prop="itemDonationId" label="物品ID" width="100" />
            <el-table-column prop="distributedQuantity" label="领取数量" width="120" />
            <el-table-column prop="distributionTime" label="领取时间" width="180">
              <template #default="{ row }">
                {{ formatTime(row.distributionTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="approvalStatus" label="审核状态" width="120">
              <template #default="{ row }">
                <el-tag :type="getStatusTagType(row.approvalStatus)">
                  {{ translateStatus(row.approvalStatus) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination-container">
            <el-pagination
                v-model:current-page="itemPage.current"
                v-model:page-size="itemPage.size"
                :total="itemPage.total"
                layout="total, prev, pager, next, jumper"
                @current-change="handleItemPageChange"
            />
          </div>
        </el-tab-pane>

        <el-tab-pane label="资金领取" name="money">
          <el-table
              :data="moneyDistributions"
              border
              style="width: 100%"
              v-loading="loading"
              empty-text="暂无资金领取记录"
          >
            <el-table-column prop="distributionId" label="记录ID" width="100" />
            <el-table-column prop="activityId" label="活动ID" width="100" />
            <el-table-column prop="moneyDonationId" label="资金ID" width="100" />
            <el-table-column prop="distributedAmount" label="领取金额" width="120">
              <template #default="{ row }">
                {{ formatCurrency(row.distributedAmount) }}
              </template>
            </el-table-column>
            <el-table-column prop="distributionTime" label="领取时间" width="180">
              <template #default="{ row }">
                {{ formatTime(row.distributionTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="approvalStatus" label="审核状态" width="120">
              <template #default="{ row }">
                <el-tag :type="getStatusTagType(row.approvalStatus)">
                  {{ translateStatus(row.approvalStatus) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination-container">
            <el-pagination
                v-model:current-page="moneyPage.current"
                v-model:page-size="moneyPage.size"
                :total="moneyPage.total"
                layout="total, prev, pager, next, jumper"
                @current-change="handleMoneyPageChange"
            />
          </div>
        </el-tab-pane>
      </el-tabs>

      <div class="statistics-container" v-if="statistics">
        <el-divider />
        <h3>领取统计</h3>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card shadow="hover">
              <div class="stat-card">
                <div class="stat-icon">
                  <el-icon><Box /></el-icon>
                </div>
                <div class="stat-content">
                  <div class="stat-title">物品领取总数</div>
                  <div class="stat-value">{{ statistics.itemTotal || 0 }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="hover">
              <div class="stat-card">
                <div class="stat-icon">
                  <el-icon><Money /></el-icon>
                </div>
                <div class="stat-content">
                  <div class="stat-title">资金领取总额</div>
                  <div class="stat-value">{{ formatCurrency(statistics.moneyTotal || 0) }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { Box, Money } from '@element-plus/icons-vue'

// 获取当前用户ID
const user = JSON.parse(localStorage.getItem('user')) || {}
const recipientId = user.userId || null

// 活动数据
const activeTab = ref('item')
const loading = ref(false)
const itemDistributions = ref([])
const moneyDistributions = ref([])
const statistics = ref(null)

// 分页配置
const itemPage = ref({
  current: 1,
  size: 10,
  total: 0
})

const moneyPage = ref({
  current: 1,
  size: 10,
  total: 0
})

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString()
}

// 格式化金额
const formatCurrency = (amount) => {
  if (!amount) return '¥0.00'
  return `¥${parseFloat(amount).toFixed(2)}`
}

// 状态翻译
const translateStatus = (status) => {
  const statusMap = {
    'pending': '待审核',
    'approved': '已通过',
    'rejected': '已拒绝'
  }
  return statusMap[status] || status
}

// 获取状态标签类型
const getStatusTagType = (status) => {
  switch (status) {
    case 'approved':
      return 'success'
    case 'pending':
      return 'warning'
    case 'rejected':
      return 'danger'
    default:
      return 'info'
  }
}

// 获取物品分发记录
const fetchItemDistributions = async () => {
  if (!recipientId) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    loading.value = true
    const response = await axios.get('/distributions/page', {
      params: {
        page: itemPage.value.current,
        size: itemPage.value.size,
        recipient_id: recipientId,
        donation_type: 'item'
      }
    })

    if (response.data && response.data.data) {
      itemDistributions.value = response.data.data.records || []
      itemPage.value.total = response.data.data.total || 0
    }
  } catch (error) {
    ElMessage.error('获取物品领取记录失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

// 获取资金分发记录
const fetchMoneyDistributions = async () => {
  if (!recipientId) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    loading.value = true
    const response = await axios.get('/distributions/page', {
      params: {
        page: moneyPage.value.current,
        size: moneyPage.value.size,
        recipient_id: recipientId,
        donation_type: 'money'
      }
    })

    if (response.data && response.data.data) {
      moneyDistributions.value = response.data.data.records || []
      moneyPage.value.total = response.data.data.total || 0
    }
  } catch (error) {
    ElMessage.error('获取资金领取记录失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

// 获取统计信息
const fetchStatistics = async () => {
  if (!recipientId) return

  try {
    const response = await axios.get('/distributions/statistics', {
      params: {
        recipient_id: recipientId
      }
    })

    if (response.data && response.data.data) {
      statistics.value = response.data.data
    }
  } catch (error) {
    console.error('获取统计信息失败:', error)
  }
}

// 处理分页变化
const handleItemPageChange = (page) => {
  itemPage.value.current = page
  fetchItemDistributions()
}

const handleMoneyPageChange = (page) => {
  moneyPage.value.current = page
  fetchMoneyDistributions()
}

// 处理标签切换
const handleTabChange = (tabName) => {
  if (tabName === 'item' && itemDistributions.value.length === 0) {
    fetchItemDistributions()
  } else if (tabName === 'money' && moneyDistributions.value.length === 0) {
    fetchMoneyDistributions()
  }
}

// 刷新数据
const fetchData = () => {
  if (activeTab.value === 'item') {
    fetchItemDistributions()
  } else {
    fetchMoneyDistributions()
  }
  fetchStatistics()
}

// 初始化加载数据
onMounted(() => {
  if (recipientId) {
    fetchItemDistributions()
    fetchStatistics()
  } else {
    ElMessage.warning('请先登录')
  }
})
</script>

<style scoped>
.distribution-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.statistics-container {
  margin-top: 30px;
}

.stat-card {
  display: flex;
  align-items: center;
}

.stat-icon {
  font-size: 40px;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  color: #409EFF;
}

.stat-content {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
}
</style>