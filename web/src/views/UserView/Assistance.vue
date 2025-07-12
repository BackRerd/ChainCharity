
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
<template>
  <div class="distribution-container">
    <el-card shadow="hover" class="charity-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">我的爱心领取记录</span>
          <el-button type="primary" @click="fetchData" :loading="loading" class="refresh-btn">
            <el-icon><Refresh /></el-icon>
            刷新数据
          </el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab" @tab-change="handleTabChange" class="charity-tabs">
        <el-tab-pane label="物品领取" name="item" class="tab-pane">
          <el-table
              :data="itemDistributions"
              border
              style="width: 100%"
              v-loading="loading"
              empty-text="暂无物品领取记录"
              class="charity-table"
              :header-cell-style="{background: '#f5f7fa', color: '#34495e'}"
          >
            <el-table-column prop="distributionId" label="记录ID" width="100" align="center" />
            <el-table-column prop="activityId" label="活动ID" width="100" align="center" />
            <el-table-column prop="itemDonationId" label="物品ID" width="100" align="center" />
            <el-table-column prop="distributedQuantity" label="领取数量" width="120" align="center" />
            <el-table-column prop="distributionTime" label="领取时间" width="180" align="center">
              <template #default="{ row }">
                {{ formatTime(row.distributionTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="approvalStatus" label="审核状态" width="120" align="center">
              <template #default="{ row }">
                <el-tag :type="getStatusTagType(row.approvalStatus)" class="status-tag">
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
                class="charity-pagination"
            />
          </div>
        </el-tab-pane>

        <el-tab-pane label="资金领取" name="money" class="tab-pane">
          <el-table
              :data="moneyDistributions"
              border
              style="width: 100%"
              v-loading="loading"
              empty-text="暂无资金领取记录"
              class="charity-table"
              :header-cell-style="{background: '#f5f7fa', color: '#34495e'}"
          >
            <el-table-column prop="distributionId" label="记录ID" width="100" align="center" />
            <el-table-column prop="activityId" label="活动ID" width="100" align="center" />
            <el-table-column prop="moneyDonationId" label="资金ID" width="100" align="center" />
            <el-table-column prop="distributedAmount" label="领取金额" width="120" align="center">
              <template #default="{ row }">
                <span class="money-amount">{{ formatCurrency(row.distributedAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="distributionTime" label="领取时间" width="180" align="center">
              <template #default="{ row }">
                {{ formatTime(row.distributionTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="approvalStatus" label="审核状态" width="120" align="center">
              <template #default="{ row }">
                <el-tag :type="getStatusTagType(row.approvalStatus)" class="status-tag">
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
                class="charity-pagination"
            />
          </div>
        </el-tab-pane>
      </el-tabs>

      <div class="statistics-container" v-if="statistics">
        <el-divider class="charity-divider" />
        <h3 class="statistics-title">爱心领取统计</h3>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card shadow="hover" class="stat-card-item">
              <div class="stat-card">
                <div class="stat-icon item-icon">
                  <el-icon><Box /></el-icon>
                </div>
                <div class="stat-content">
                  <div class="stat-title">物品领取总数</div>
                  <div class="stat-value">{{ statistics.itemTotal || 0 }}</div>
                  <div class="stat-desc">感谢社会各界的物资捐助</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="hover" class="stat-card-money">
              <div class="stat-card">
                <div class="stat-icon money-icon">
                  <el-icon><Money /></el-icon>
                </div>
                <div class="stat-content">
                  <div class="stat-title">资金领取总额</div>
                  <div class="stat-value">{{ formatCurrency(statistics.moneyTotal || 0) }}</div>
                  <div class="stat-desc">感谢社会各界的爱心捐赠</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>


<style scoped>
.distribution-container {
  padding: 20px;
  background-color: #f9f5f0; /* 温暖的米色背景 */
}

.charity-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  background-color: #fff;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: #34495e;
  position: relative;
  padding-left: 12px;
}

.card-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 16px;
  background-color: #e74c3c; /* 慈善红 */
  border-radius: 2px;
}

.refresh-btn {
  background-color: #3498db; /* 信任蓝 */
  border-color: #3498db;
  border-radius: 6px;
  padding: 8px 16px;
}

.refresh-btn:hover {
  background-color: #2980b9;
  border-color: #2980b9;
}

.charity-tabs {
  margin-top: 10px;
}

.charity-tabs :deep(.el-tabs__nav-wrap::after) {
  background-color: #e8e8e8;
}

.charity-tabs :deep(.el-tabs__active-bar) {
  background-color: #e74c3c; /* 慈善红 */
  height: 3px;
}

.charity-tabs :deep(.el-tabs__item) {
  font-weight: 500;
  color: #7f8c8d;
}

.charity-tabs :deep(.el-tabs__item.is-active) {
  color: #e74c3c; /* 慈善红 */
}

.charity-tabs :deep(.el-tabs__item:hover) {
  color: #e74c3c; /* 慈善红 */
}

.charity-table {
  border-radius: 8px;
  overflow: hidden;
}

.charity-table :deep(th) {
  font-weight: 600;
}

.charity-table :deep(.el-table__row:hover) {
  background-color: #fff9f5;
}

.status-tag {
  border-radius: 4px;
  font-weight: 500;
  padding: 0 8px;
}

.money-amount {
  font-weight: 600;
  color: #e74c3c; /* 慈善红 */
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.charity-pagination :deep(.btn-prev),
.charity-pagination :deep(.btn-next),
.charity-pagination :deep(.number) {
  border-radius: 6px;
}

.charity-pagination :deep(.number.active) {
  background-color: #e74c3c; /* 慈善红 */
  color: white;
}

.charity-pagination :deep(.number:hover) {
  color: #e74c3c; /* 慈善红 */
}

.statistics-container {
  margin-top: 30px;
}

.charity-divider {
  border-color: #eee;
}

.statistics-title {
  color: #34495e;
  font-size: 16px;
  margin-bottom: 20px;
  text-align: center;
  position: relative;
  padding-bottom: 10px;
}

.statistics-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 2px;
  background-color: #e74c3c; /* 慈善红 */
}

.stat-card-item, .stat-card-money {
  border-radius: 10px;
  border: none;
  transition: all 0.3s ease;
}

.stat-card-item {
  background-color: #fff9f5;
}

.stat-card-money {
  background-color: #f5f9ff;
}

.stat-card-item:hover, .stat-card-money:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 16px;
}

.stat-icon {
  font-size: 40px;
  width: 70px;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  border-radius: 50%;
}

.item-icon {
  background-color: #fde8e6; /* 浅红色背景 */
  color: #e74c3c; /* 慈善红 */
}

.money-icon {
  background-color: #e6f2ff; /* 浅蓝色背景 */
  color: #3498db; /* 信任蓝 */
}

.stat-content {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #7f8c8d;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #34495e;
  margin-bottom: 5px;
}

.stat-desc {
  font-size: 12px;
  color: #95a5a6;
  font-style: italic;
}
</style>