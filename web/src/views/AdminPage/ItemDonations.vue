<template>
  <div class="app-container">
    <el-card class="main-card" shadow="hover">
      <!-- 统计卡片 - 添加动画效果 -->
      <div class="stats-container">
        <el-card shadow="hover" class="stat-card total" @mouseenter="hoverStat('total')" @mouseleave="resetStat">
          <div class="stat-item">
            <div class="stat-value animate-number">{{ animatedStats.totalDonations }}</div>
            <div class="stat-label">
              <i class="el-icon-collection icon"></i>
              <span>总捐赠数量</span>
            </div>
          </div>
          <div class="stat-footer">比上月 ↑12%</div>
        </el-card>

        <el-card shadow="hover" class="stat-card approved" @mouseenter="hoverStat('approved')" @mouseleave="resetStat">
          <div class="stat-item">
            <div class="stat-value animate-number">{{ animatedStats.approvedDonations }}</div>
            <div class="stat-label">
              <i class="el-icon-success icon"></i>
              <span>已批准捐赠</span>
            </div>
          </div>
          <div class="stat-footer">占比 {{ approvedPercentage }}%</div>
        </el-card>

        <el-card shadow="hover" class="stat-card pending" @mouseenter="hoverStat('pending')" @mouseleave="resetStat">
          <div class="stat-item">
            <div class="stat-value animate-number">{{ animatedStats.pendingDonations }}</div>
            <div class="stat-label">
              <i class="el-icon-time icon"></i>
              <span>待审批捐赠</span>
            </div>
          </div>
          <div class="stat-footer">待处理 <el-progress :percentage="pendingPercentage" :color="pendingColor" :show-text="false"></el-progress></div>
        </el-card>
      </div>

      <!-- 操作工具栏 - 添加渐变背景 -->
      <div class="toolbar animated-gradient">
        <el-button type="primary" @click="showAddDialog" class="action-button">
          <i class="el-icon-circle-plus"></i> 新增捐赠
        </el-button>
        <el-input
            v-model="searchQuery"
            placeholder="搜索物品类型"
            style="width: 250px; margin-left: 15px"
            clearable
            @clear="handleSearchClear"
            @keyup.enter="handleSearch"
            class="search-input"
        >
          <template #prefix>
            <i class="el-icon-search"></i>
          </template>
        </el-input>
        <el-button type="info" @click="refreshData" class="refresh-button">
          <i class="el-icon-refresh" :class="{ 'rotate-animation': refreshing }"></i> 刷新
        </el-button>
      </div>

      <!-- 数据表格 - 添加斑马纹和悬停效果 -->
      <el-table
          :data="tableData"
          border
          style="width: 100%; margin-top: 20px"
          v-loading="loading"
          element-loading-text="数据加载中..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(255, 255, 255, 0.7)"
          stripe
          highlight-current-row
          @row-click="handleRowClick"
          class="data-table"
      >
        <el-table-column prop="donationId" label="ID" width="100" align="center" />
        <el-table-column prop="activityId" label="活动ID" width="120" align="center" />
        <el-table-column prop="donorId" label="捐赠者ID" width="120" align="center" />
        <el-table-column prop="itemType" label="物品类型" width="140">
          <template #default="{ row }">
            <el-tag effect="dark" :color="getItemTypeColor(row.itemType)">
              {{ row.itemType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="160" align="center">
          <template #default="{ row }">
            <el-progress
                :percentage="Math.min(100, row.quantity)"
                :show-text="false"
                :color="getQuantityColor(row.quantity)"
                :stroke-width="12"
            />
            <span class="quantity-text">{{ row.quantity }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="approvalStatus" label="审批状态" width="140" align="center">
          <template #default="{ row }">
            <el-tag
                :type="getStatusTagType(row.approvalStatus)"
                effect="light"
                :class="'status-tag-' + row.approvalStatus"
            >
              <i :class="getStatusIcon(row.approvalStatus)"></i>
              {{ formatStatus(row.approvalStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="物品状态" width="140" align="center">
          <template #default="{ row }">
            <el-tag
                :type="getItemStatusTagType(row.status)"
                effect="light"
                :class="'item-status-tag-' + row.status"
            >
              <i :class="getItemStatusIcon(row.status)"></i>
              {{ formatItemStatus(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="donationTime" label="捐赠时间" width="200" align="center">
          <template #default="{ row }">
            <div class="time-display">
              <i class="el-icon-time time-icon"></i>
              <span>{{ formatDate(row.donationTime) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="{ row }">
            <el-button
                size="small"
                @click.stop="handleEdit(row)"
                class="edit-button"
            >
              <i class="el-icon-edit"></i> 编辑
            </el-button>
            <el-popconfirm
                title="确定要删除这条记录吗？"
                confirm-button-text="确定"
                cancel-button-text="取消"
                icon="el-icon-warning"
                icon-color="red"
                @confirm="handleDelete(row.donationId)"
            >
              <template #reference>
                <el-button
                    size="small"
                    type="danger"
                    @click.stop
                    class="delete-button"
                >
                  <i class="el-icon-delete"></i> 删除
                </el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 - 添加背景和边框 -->
      <div class="pagination-container">
        <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[5, 10, 20, 50]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            background
            class="custom-pagination"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 - 美化样式 -->
    <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="50%"
        :close-on-click-modal="false"
        custom-class="donation-dialog"
        @closed="resetForm"
    >
      <el-form
          ref="formRef"
          :model="formData"
          :rules="rules"
          label-width="120px"
          label-position="top"
          class="donation-form"
      >
        <div class="form-row">
          <el-form-item label="活动ID" prop="activityId">
            <el-input-number
                v-model="formData.activityId"
                :min="1"
                controls-position="right"
                class="form-input"
            />
          </el-form-item>
          <el-form-item label="捐赠者ID" prop="donorId">
            <el-input-number
                v-model="formData.donorId"
                :min="1"
                controls-position="right"
                class="form-input"
            />
          </el-form-item>
        </div>

        <el-form-item label="物品类型" prop="itemType">
          <el-select
              v-model="formData.itemType"
              placeholder="请选择物品类型"
              class="form-input"
              filterable
              allow-create
          >
            <el-option
                v-for="item in itemTypes"
                :key="item"
                :label="item"
                :value="item"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="数量" prop="quantity">
          <el-input-number
              v-model="formData.quantity"
              :min="1"
              :max="1000"
              controls-position="right"
              class="form-input"
          />
        </el-form-item>

        <el-form-item label="图片路径" prop="imagePath">
          <el-upload
              class="image-uploader"
              action="https://jsonplaceholder.typicode.com/posts/"
              :show-file-list="false"
              :on-success="handleUploadSuccess"
              :before-upload="beforeUpload"
          >
            <img v-if="formData.imagePath" :src="formData.imagePath" class="uploaded-image">
            <i v-else class="el-icon-plus image-uploader-icon"></i>
          </el-upload>
        </el-form-item>

        <div class="form-row">
          <el-form-item label="审批状态" prop="approvalStatus">
            <el-select
                v-model="formData.approvalStatus"
                placeholder="请选择审批状态"
                class="form-input"
            >
              <el-option
                  v-for="status in approvalStatusOptions"
                  :key="status.value"
                  :label="status.label"
                  :value="status.value"
                  :class="'status-option-' + status.value"
              >
                <span :class="'status-icon ' + getStatusIcon(status.value)"></span>
                <span>{{ status.label }}</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="物品状态" prop="status">
            <el-select
                v-model="formData.status"
                placeholder="请选择物品状态"
                class="form-input"
            >
              <el-option
                  v-for="status in itemStatusOptions"
                  :key="status.value"
                  :label="status.label"
                  :value="status.value"
                  :class="'item-status-option-' + status.value"
              >
                <span :class="'status-icon ' + getItemStatusIcon(status.value)"></span>
                <span>{{ status.label }}</span>
              </el-option>
            </el-select>
          </el-form-item>
        </div>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" class="cancel-button">取消</el-button>
          <el-button
              type="primary"
              @click="handleSubmit"
              class="confirm-button"
              :loading="submitting"
          >
            {{ submitting ? '提交中...' : '确定' }}
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from "axios";
import { animateValue } from '@/utils/animation'

// 表格数据
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchQuery = ref('')
const loading = ref(false)
const refreshing = ref(false)
const submitting = ref(false)

// 统计数据
const stats = ref({
  totalDonations: 0,
  approvedDonations: 0,
  pendingDonations: 0
})

// 动画统计数据
const animatedStats = reactive({
  totalDonations: 0,
  approvedDonations: 0,
  pendingDonations: 0
})

// 计算属性
const approvedPercentage = computed(() => {
  if (stats.value.totalDonations === 0) return 0
  return Math.round((stats.value.approvedDonations / stats.value.totalDonations) * 100)
})

const pendingPercentage = computed(() => {
  if (stats.value.totalDonations === 0) return 0
  return Math.round((stats.value.pendingDonations / stats.value.totalDonations) * 100)
})

const pendingColor = computed(() => {
  const percentage = pendingPercentage.value
  if (percentage > 30) return '#f56c6c'
  if (percentage > 10) return '#e6a23c'
  return '#67c23a'
})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('新增捐赠')
const formRef = ref(null)
const formData = ref({
  donationId: null,
  activityId: null,
  donorId: null,
  itemType: '',
  quantity: 1,
  imagePath: '',
  approvalStatus: 'pending',
  status: 'pending'
})

// 选项数据
const itemTypes = ref([
  '衣物', '食品', '书籍', '玩具', '电子产品', '家具', '日用品', '其他'
])

const approvalStatusOptions = ref([
  { value: 'pending', label: '待审批' },
  { value: 'approved', label: '已批准' },
  { value: 'rejected', label: '已拒绝' }
])

const itemStatusOptions = ref([
  { value: 'pending', label: '待处理' },
  { value: 'received', label: '已接收' },
  { value: 'distributed', label: '已分发' }
])

// 验证规则
const rules = {
  activityId: [{ required: true, message: '请输入活动ID', trigger: 'blur' }],
  donorId: [{ required: true, message: '请输入捐赠者ID', trigger: 'blur' }],
  itemType: [{ required: true, message: '请输入物品类型', trigger: 'blur' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }]
}

// 初始化数据
onMounted(() => {
  fetchData()
  fetchStats()
})

// 获取表格数据
const fetchData = async () => {
  try {
    loading.value = true
    let url = `/item-donations/page?page=${currentPage.value}&size=${pageSize.value}`
    if (searchQuery.value) {
      url += `&itemType=${searchQuery.value}`
    }

    const response = await axios.get(url)
    if (response.data && response.data.code === "200") {
      tableData.value = response.data.data.records || []
      total.value = response.data.data.total || 0
    } else {
      ElMessage.error(response.data?.msg || '获取数据失败')
    }
  } catch (error) {
    ElMessage.error('请求失败: ' + error.message)
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

// 获取统计数据
const fetchStats = async () => {
  try {
    const response = await axios.get('/item-donations/stats')
    if (response.data && response.data.code === "200") {
      stats.value = response.data.data

      // 为统计数据添加动画效果
      animateValue(animatedStats, 'totalDonations', 0, stats.value.totalDonations, 1000)
      animateValue(animatedStats, 'approvedDonations', 0, stats.value.approvedDonations, 1000)
      animateValue(animatedStats, 'pendingDonations', 0, stats.value.pendingDonations, 1000)
    }
  } catch (error) {
    console.error('获取统计信息失败:', error)
  }
}

// 刷新数据
const refreshData = () => {
  refreshing.value = true
  fetchData()
  fetchStats()
}

// 显示新增对话框
const showAddDialog = () => {
  dialogTitle.value = '新增捐赠'
  formData.value = {
    donationId: null,
    activityId: null,
    donorId: null,
    itemType: '',
    quantity: 1,
    imagePath: '',
    approvalStatus: 'pending',
    status: 'pending'
  }
  dialogVisible.value = true
}

// 显示编辑对话框
const handleEdit = (row) => {
  dialogTitle.value = '编辑捐赠'
  formData.value = { ...row }
  dialogVisible.value = true
}

// 行点击事件
const handleRowClick = (row) => {
  // 可以在这里添加行点击的逻辑，比如显示详情
  console.log('行点击:', row)
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true

    let response
    if (formData.value.donationId) {
      // 更新
      response = await axios.put('/item-donations', formData.value)
    } else {
      // 新增
      response = await axios.post('/item-donations', formData.value)
    }

    if (response.data && response.data.code === "200") {
      ElMessage.success('操作成功')
      dialogVisible.value = false
      fetchData()
      fetchStats()
    } else {
      ElMessage.error(response.data?.msg || '操作失败')
    }
  } catch (error) {
    ElMessage.error('请求失败: ' + error.message)
  } finally {
    submitting.value = false
  }
}

// 删除记录
const handleDelete = async (id) => {
  try {
    const response = await axios.delete(`/item-donations/${id}`)
    if (response.data && response.data.code === "200") {
      ElMessage.success('删除成功')
      fetchData()
      fetchStats()
    } else {
      ElMessage.error(response.data?.msg || '删除失败')
    }
  } catch (error) {
    ElMessage.error('删除失败: ' + error.message)
  }
}

// 分页大小改变
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

// 当前页改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchData()
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchData()
}

// 清除搜索
const handleSearchClear = () => {
  searchQuery.value = ''
  fetchData()
}

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields()
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString()
}

// 格式化状态
const formatStatus = (status) => {
  const map = {
    'pending': '待审批',
    'approved': '已批准',
    'rejected': '已拒绝'
  }
  return map[status] || status
}

// 格式化物品状态
const formatItemStatus = (status) => {
  const map = {
    'pending': '待处理',
    'received': '已接收',
    'distributed': '已分发'
  }
  return map[status] || status
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

// 获取物品状态标签类型
const getItemStatusTagType = (status) => {
  switch (status) {
    case 'received':
      return 'success'
    case 'pending':
      return 'warning'
    case 'distributed':
      return 'primary'
    default:
      return 'info'
  }
}

// 获取状态图标
const getStatusIcon = (status) => {
  switch (status) {
    case 'approved':
      return 'el-icon-success'
    case 'pending':
      return 'el-icon-time'
    case 'rejected':
      return 'el-icon-error'
    default:
      return 'el-icon-question'
  }
}

// 获取物品状态图标
const getItemStatusIcon = (status) => {
  switch (status) {
    case 'received':
      return 'el-icon-box'
    case 'pending':
      return 'el-icon-watch'
    case 'distributed':
      return 'el-icon-truck'
    default:
      return 'el-icon-question'
  }
}

// 获取物品类型颜色
const getItemTypeColor = (type) => {
  const colors = {
    '衣物': '#409EFF',
    '食品': '#67C23A',
    '书籍': '#E6A23C',
    '玩具': '#F56C6C',
    '电子产品': '#909399',
    '家具': '#B37FEB',
    '日用品': '#FF85C0',
    '其他': '#9E9E9E'
  }
  return colors[type] || '#909399'
}

// 获取数量颜色
const getQuantityColor = (quantity) => {
  if (quantity > 50) return '#F56C6C'
  if (quantity > 20) return '#E6A23C'
  return '#67C23A'
}

// 统计卡片悬停效果
const hoverStat = (type) => {
  const card = document.querySelector(`.stat-card.${type}`)
  if (card) {
    card.style.transform = 'translateY(-5px)'
    card.style.boxShadow = '0 6px 16px rgba(0, 0, 0, 0.15)'
  }
}

const resetStat = () => {
  const cards = document.querySelectorAll('.stat-card')
  cards.forEach(card => {
    card.style.transform = ''
    card.style.boxShadow = ''
  })
}

// 图片上传处理
const handleUploadSuccess = (response, file) => {
  formData.value.imagePath = URL.createObjectURL(file.raw)
}

const beforeUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传图片只能是 JPG/PNG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
  }
  return isJPG && isLt2M
}
</script>

<style scoped lang="scss">
.app-container {
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  min-height: 100vh;
}

.main-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  background-color: rgba(255, 255, 255, 0.95);

  &:hover {
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
  }
}

.stats-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 24px;
  gap: 16px;

  .stat-card {
    flex: 1;
    border-radius: 10px;
    border: none;
    transition: all 0.3s ease;
    position: relative;
    overflow: hidden;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 4px;
    }

    &.total {
      background: linear-gradient(135deg, #f6f8ff 0%, #eef1ff 100%);

      &::before {
        background: linear-gradient(90deg, #409EFF 0%, #79BBFF 100%);
      }

      .stat-value {
        color: #409EFF;
      }
    }

    &.approved {
      background: linear-gradient(135deg, #f0f9eb 0%, #e1f3d8 100%);

      &::before {
        background: linear-gradient(90deg, #67C23A 0%, #95D475 100%);
      }

      .stat-value {
        color: #67C23A;
      }
    }

    &.pending {
      background: linear-gradient(135deg, #fdf6ec 0%, #faecd8 100%);

      &::before {
        background: linear-gradient(90deg, #E6A23C 0%, #EEBE77 100%);
      }

      .stat-value {
        color: #E6A23C;
      }
    }

    .stat-item {
      text-align: center;
      padding: 20px 0;

      .stat-value {
        font-size: 32px;
        font-weight: bold;
        margin-bottom: 8px;
        font-family: 'Arial Rounded MT Bold', 'Arial', sans-serif;
        transition: all 0.5s ease;
      }

      .stat-label {
        color: #606266;
        font-size: 14px;
        display: flex;
        align-items: center;
        justify-content: center;

        .icon {
          margin-right: 6px;
          font-size: 16px;
        }
      }
    }

    .stat-footer {
      padding: 8px 0;
      text-align: center;
      font-size: 12px;
      color: #909399;
      border-top: 1px dashed #ebeef5;
    }
  }
}

.toolbar {
  margin-bottom: 20px;
  padding: 16px;
  border-radius: 8px;
  display: flex;
  align-items: center;

  &.animated-gradient {
    background: linear-gradient(270deg, #f0f2f5, #e6e9ed, #f0f2f5);
    background-size: 400% 400%;
    animation: gradientBG 8s ease infinite;
  }

  .action-button {
    background: linear-gradient(135deg, #409EFF 0%, #79BBFF 100%);
    border: none;
    font-weight: 500;
    letter-spacing: 0.5px;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
    }
  }

  .refresh-button {
    margin-left: 10px;
    transition: all 0.3s;

    &:hover {
      transform: rotate(360deg);
    }

    .rotate-animation {
      animation: rotate 1s linear infinite;
    }
  }

  .search-input {
    :deep(.el-input__inner) {
      border-radius: 20px;
      padding-left: 35px;
    }

    :deep(.el-input__prefix) {
      left: 10px;
      color: #909399;
    }
  }
}

.data-table {
  :deep(th) {
    background-color: #f5f7fa !important;
    color: #606266;
    font-weight: 600;
  }

  :deep(.el-table__row) {
    transition: all 0.2s;

    &:hover {
      transform: scale(1.005);
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    }
  }

  .quantity-text {
    display: inline-block;
    margin-left: 8px;
    font-weight: 500;
  }

  .time-display {
    display: flex;
    align-items: center;

    .time-icon {
      margin-right: 5px;
      color: #909399;
    }
  }

  .edit-button {
    background: linear-gradient(135deg, #f0f9eb 0%, #e1f3d8 100%);
    border-color: #e1f3d8;
    color: #67C23A;

    &:hover {
      background: linear-gradient(135deg, #e1f3d8 0%, #d1edc4 100%);
    }
  }

  .delete-button {
    background: linear-gradient(135deg, #fef0f0 0%, #fde2e2 100%);
    border-color: #fde2e2;

    &:hover {
      background: linear-gradient(135deg, #fde2e2 0%, #fcd3d3 100%);
    }
  }
}

.pagination-container {
  margin-top: 24px;
  padding: 12px;
  background-color: #f5f7fa;
  border-radius: 8px;

  .custom-pagination {
    :deep(.btn-prev), :deep(.btn-next) {
      border-radius: 50%;
      width: 32px;
      height: 32px;
    }

    :deep(.number) {
      border-radius: 50%;
      width: 32px;
      height: 32px;
      min-width: 32px;
    }

    :deep(.active) {
      background: linear-gradient(135deg, #409EFF 0%, #79BBFF 100%);
    }
  }
}

.donation-dialog {
  border-radius: 12px;

  .donation-form {
    padding: 0 20px;

    .form-row {
      display: flex;
      gap: 20px;

      .el-form-item {
        flex: 1;
      }
    }

    .form-input {
      width: 100%;
    }

    .image-uploader {
      :deep(.el-upload) {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        transition: all 0.3s;
        width: 100%;
        height: 180px;

        &:hover {
          border-color: #409EFF;
        }
      }

      .image-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 100%;
        height: 180px;
        line-height: 180px;
        text-align: center;
      }

      .uploaded-image {
        width: 100%;
        height: 180px;
        display: block;
        object-fit: cover;
      }
    }

    .status-option-pending {
      color: #E6A23C;
    }

    .status-option-approved {
      color: #67C23A;
    }

    .status-option-rejected {
      color: #F56C6C;
    }

    .item-status-option-pending {
      color: #E6A23C;
    }

    .item-status-option-received {
      color: #67C23A;
    }

    .item-status-option-distributed {
      color: #409EFF;
    }

    .status-icon {
      margin-right: 8px;
    }
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;

    .cancel-button {
      background: linear-gradient(135deg, #f5f5f5 0%, #e9e9e9 100%);
      border: none;

      &:hover {
        background: linear-gradient(135deg, #e9e9e9 0%, #dddddd 100%);
      }
    }

    .confirm-button {
      background: linear-gradient(135deg, #409EFF 0%, #79BBFF 100%);
      border: none;
      margin-left: 10px;

      &:hover {
        background: linear-gradient(135deg, #79BBFF 0%, #409EFF 100%);
      }
    }
  }
}

/* 动画效果 */
@keyframes gradientBG {
  0% {
    background-position: 0% 50%
  }
  50% {
    background-position: 100% 50%
  }
  100% {
    background-position: 0% 50%
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg)
  }
  to {
    transform: rotate(360deg)
  }
}

/* 响应式调整 */
@media (max-width: 992px) {
  .stats-container {
    flex-direction: column;

    .stat-card {
      margin-bottom: 16px;
    }
  }

  .toolbar {
    flex-direction: column;
    gap: 10px;

    .el-input {
      width: 100% !important;
      margin-left: 0 !important;
    }
  }
}
</style>