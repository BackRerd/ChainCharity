<template>
  <div class="charity-donation-container">
    <!-- 顶部标题和统计卡片 -->
    <div class="charity-header">
      <div class="stats-cards">
        <el-card shadow="hover" class="stats-card total-card" @mouseenter="hoverCard('total')" @mouseleave="resetCard">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-money"></i>
            </div>
            <div class="card-text">
              <h3>总捐赠金额</h3>
              <p class="amount">¥{{ formatAmount(totalAmount) }}</p>
            </div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card approved-card" @mouseenter="hoverCard('approved')" @mouseleave="resetCard">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-success"></i>
            </div>
            <div class="card-text">
              <h3>已批准捐赠</h3>
              <p class="amount">{{ approvedCount }} 笔</p>
            </div>
          </div>
        </el-card>
        <el-card shadow="hover" class="stats-card pending-card" @mouseenter="hoverCard('pending')" @mouseleave="resetCard">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-time"></i>
            </div>
            <div class="card-text">
              <h3>待审批捐赠</h3>
              <p class="amount">{{ pendingCount }} 笔</p>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 主内容区域 -->
    <el-card shadow="never" class="main-content-card">
      <!-- 搜索和操作区域 -->
      <div class="operation-container">
        <el-button
            type="primary"
            @click="handleAdd"
            :icon="Plus"
            class="add-btn"
            plain
        >
          <span class="btn-text">添加捐赠</span>
        </el-button>

        <div class="search-group">
          <el-input
              v-model="searchQuery"
              placeholder="请输入活动ID、捐赠者ID或捐赠金额"
              clearable
              @clear="handleSearchClear"
              @keyup.enter="handleSearch"
              class="search-input"
          >
            <template #prefix>
              <el-icon class="search-icon"><Search /></el-icon>
            </template>
          </el-input>
          <el-button
              type="primary"
              @click="handleSearch"
              class="search-btn"
          >
            搜索
          </el-button>
        </div>
      </div>

      <!-- 数据表格 -->
      <div class="table-container">
        <el-table
            :data="tableData"
            border
            style="width: 100%; margin-top: 20px"
            v-loading="loading"
            :header-cell-style="{background: '#f5f7fa', color: '#606266'}"
            @row-click="handleRowClick"
            highlight-current-row
            class="donation-table"
        >
          <el-table-column prop="donationId" label="捐赠ID" width="150" align="center">
            <template #header>
              <span class="column-header">
                <i class="el-icon-tickets"></i> 捐赠ID
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="activityId" label="活动ID" width="150" align="center">
            <template #header>
              <span class="column-header">
                <i class="el-icon-s-flag"></i> 活动ID
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="donorId" label="捐赠者ID" width="170" align="center">
            <template #header>
              <span class="column-header">
                <i class="el-icon-user"></i> 捐赠者ID
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="amount" label="金额" width="200" align="center">
            <template #header>
              <span class="column-header">
                <i class="el-icon-coin"></i> 金额
              </span>
            </template>
            <template #default="{ row }">
              <div class="amount-cell">
                <span class="currency">¥</span>
                <span class="value">{{ row.amount.toFixed(2) }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="donationTime" label="捐赠时间" width="230" align="center">
            <template #header>
              <span class="column-header">
                <i class="el-icon-time"></i> 捐赠时间
              </span>
            </template>
            <template #default="{ row }">
              <div class="time-cell">
                <i class="el-icon-date"></i>
                {{ formatDateTime(row.donationTime) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="approvalStatus" label="审批状态" width="200" align="center">
            <template #header>
    <span class="column-header">
      <i class="el-icon-finished"></i> 审批状态
    </span>
            </template>
            <template #default="{ row }">
              <el-tag
                  :type="getStatusTagType(row.approvalStatus)"
                  effect="light"
                  class="status-tag"
              >
                <i :class="getStatusIcon(row.approvalStatus)"></i>
                {{ getApprovalStatusText(row.approvalStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="170" align="center">
            <template #header>
    <span class="column-header">
      <i class="el-icon-circle-check"></i> 状态
    </span>
            </template>
            <template #default="{ row }">
              <el-tag
                  :type="getDonationStatusTagType(row.status)"
                  effect="light"
                  class="status-tag"
              >
                <i :class="getDonationStatusIcon(row.status)"></i>
                {{ getDonationStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="250" align="center" fixed="right">
            <template #header>
              <span class="column-header">
                <i class="el-icon-setting"></i> 操作
              </span>
            </template>
            <template #default="{ row }">
              <el-button
                  size="small"
                  @click.stop="handleEdit(row)"
                  :icon="Edit"
                  class="action-btn edit-btn"
              >
                编辑
              </el-button>
              <el-button
                  size="small"
                  @click.stop="handleDelete(row.donationId)"
                  :icon="Delete"
                  class="action-btn delete-btn"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 30, 50]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            background
        />
      </div>
    </el-card>

    <!-- 添加/编辑对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="600px"
        :close-on-click-modal="false"
        class="donation-dialog"
        :before-close="handleDialogClose"
    >
      <el-form
          ref="donationForm"
          :model="formData"
          :rules="formRules"
          label-width="120px"
          label-position="left"
      >
        <el-form-item label="捐赠ID" prop="donationId" v-if="isEdit">
          <el-input v-model="formData.donationId" disabled class="form-input">
            <template #prefix>
              <i class="el-icon-tickets"></i>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="活动ID" prop="activityId">
          <el-input-number
              v-model="formData.activityId"
              :min="1"
              controls-position="right"
              class="form-input"
          >
            <template #prefix>
              <i class="el-icon-s-flag"></i>
            </template>
          </el-input-number>
        </el-form-item>
        <el-form-item label="捐赠者ID" prop="donorId">
          <el-input-number
              v-model="formData.donorId"
              :min="1"
              controls-position="right"
              class="form-input"
          >
            <template #prefix>
              <i class="el-icon-user"></i>
            </template>
          </el-input-number>
        </el-form-item>
        <el-form-item label="捐赠金额" prop="amount">
          <el-input-number
              v-model="formData.amount"
              :min="0.01"
              :precision="2"
              :step="100"
              controls-position="right"
              class="form-input"
          >
            <template #prefix>
              <i class="el-icon-coin"></i>
            </template>
          </el-input-number>
        </el-form-item>
        <el-form-item label="审批状态" prop="approvalStatus">
          <el-select
              v-model="formData.approvalStatus"
              placeholder="请选择审批状态"
              class="form-input"
          >
            <template #prefix>
              <i class="el-icon-finished"></i>
            </template>
            <el-option label="待审批" value="pending">
              <span><i class="el-icon-time"></i> 待审批</span>
            </el-option>
            <el-option label="已批准" value="approved">
              <span><i class="el-icon-success"></i> 已批准</span>
            </el-option>
            <el-option label="已拒绝" value="rejected">
              <span><i class="el-icon-error"></i> 已拒绝</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
              v-model="formData.status"
              placeholder="请选择状态"
              class="form-input"
          >
            <template #prefix>
              <i class="el-icon-circle-check"></i>
            </template>
            <el-option label="待处理" value="pending">
              <span><i class="el-icon-warning"></i> 待处理</span>
            </el-option>
            <el-option label="已接收" value="received">
              <span><i class="el-icon-circle-check"></i> 已接收</span>
            </el-option>
            <el-option label="已分配" value="distributed">
              <span><i class="el-icon-finished"></i> 已分配</span>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose" class="dialog-btn cancel-btn">取消</el-button>
          <el-button
              type="primary"
              @click="submitForm"
              class="dialog-btn confirm-btn"
              :loading="submitting"
          >
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { Plus, Edit, Delete, Search } from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

// API 基础URL
const API_BASE_URL = '/money-donations'

// 表格数据
const tableData = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchQuery = ref('')
const submitting = ref(false)

// 统计信息
const totalAmount = ref(0)
const approvedCount = ref(0)
const pendingCount = ref(0)

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const donationForm = ref(null)

// 表单数据
const formData = reactive({
  donationId: null,
  activityId: null,
  donorId: null,
  amount: null,
  donationTime: null,
  approvalStatus: '待审批',
  status: '有效'
})
// 审批状态映射
const approvalStatusMap = {
  'pending': '待审批',
  'approved': '已批准',
  'rejected': '已拒绝'
}

// 捐赠状态映射
const donationStatusMap = {
  'pending': '待处理',
  'received': '已接收',
  'distributed': '已分配'
}

// 获取审批状态显示文本
const getApprovalStatusText = (status) => {
  return approvalStatusMap[status] || status
}

// 获取捐赠状态显示文本
const getDonationStatusText = (status) => {
  return donationStatusMap[status] || status
}

// 表单验证规则
const formRules = reactive({
  activityId: [{ required: true, message: '请输入活动ID', trigger: 'blur' }],
  donorId: [{ required: true, message: '请输入捐赠者ID', trigger: 'blur' }],
  amount: [{ required: true, message: '请输入捐赠金额', trigger: 'blur' }],
  approvalStatus: [{ required: true, message: '请选择审批状态', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})
// 获取审批状态标签类型
const getStatusTagType = (status) => {
  switch (status) {
    case 'approved':
      return 'success'
    case 'rejected':
      return 'danger'
    default:
      return 'warning'
  }
}

// 获取捐赠状态标签类型
const getDonationStatusTagType = (status) => {
  switch (status) {
    case 'received':
      return 'success'
    case 'distributed':
      return 'primary'
    default:
      return 'info'
  }
}

// 获取审批状态图标
const getStatusIcon = (status) => {
  switch (status) {
    case 'approved':
      return 'el-icon-success'
    case 'rejected':
      return 'el-icon-error'
    default:
      return 'el-icon-time'
  }
}

// 获取捐赠状态图标
const getDonationStatusIcon = (status) => {
  switch (status) {
    case 'received':
      return 'el-icon-circle-check'
    case 'distributed':
      return 'el-icon-finished'
    default:
      return 'el-icon-warning'
  }
}

// 格式化金额显示
const formatAmount = (amount) => {
  return amount.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

// 格式化日期时间
const formatDateTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString()
}

// 获取捐赠列表和统计数据
const fetchDonations = async () => {
  try {
    loading.value = true
    const [donationsRes, statsRes] = await Promise.all([
      axios.get(`${API_BASE_URL}/page`, {
        params: {
          page: currentPage.value,
          size: pageSize.value,
          search: searchQuery.value
        }
      }),
      axios.get(`${API_BASE_URL}/stats`)
    ])

    tableData.value = donationsRes.data.data.records || []
    total.value = donationsRes.data.data.total || 0

    const stats = statsRes.data.data || {}
    totalAmount.value = stats.totalAmount || 0
    approvedCount.value = stats.approvedCount || 0
    pendingCount.value = stats.pendingCount || 0
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 处理分页大小变化
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchDonations()
}

// 处理当前页变化
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchDonations()
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchDonations()
}

// 处理搜索清除
const handleSearchClear = () => {
  searchQuery.value = ''
  handleSearch()
}

// 处理添加
const handleAdd = () => {
  dialogTitle.value = '添加资金捐赠'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 处理编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑资金捐赠'
  isEdit.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 处理行点击
const handleRowClick = (row) => {
  // 可以在这里添加行点击的逻辑，比如显示详情等
  console.log('行点击:', row)
}

// 处理删除
const handleDelete = (id) => {
  ElMessageBox.confirm('确认删除这条捐赠记录吗?', '警告', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
    customClass: 'delete-confirm-dialog'
  })
      .then(async () => {
        try {
          await axios.delete(`${API_BASE_URL}/${id}`)
          ElMessage.success('删除成功')
          fetchDonations()
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('删除失败')
        }
      })
      .catch(() => {})
}

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    donationId: null,
    activityId: null,
    donorId: null,
    amount: null,
    donationTime: null,
    approvalStatus: '待审批',
    status: '有效'
  })
  if (donationForm.value) {
    donationForm.value.resetFields()
  }
}

// 处理对话框关闭
const handleDialogClose = () => {
  donationForm.value.resetFields()
  dialogVisible.value = false
}

// 提交表单
const submitForm = async () => {
  try {
    await donationForm.value.validate()
    submitting.value = true

    if (isEdit.value) {
      // 更新
      await axios.put(API_BASE_URL, formData)
      ElMessage.success('更新成功')
    } else {
      // 新增
      await axios.post(API_BASE_URL, formData)
      ElMessage.success('添加成功')
    }

    dialogVisible.value = false
    fetchDonations()
  } catch (error) {
    if (error.response) {
      // 请求已发出，服务器响应状态码不在 2xx 范围内
      console.error('操作失败:', error.response.data)
      ElMessage.error(`操作失败: ${error.response.data.message || '未知错误'}`)
    } else if (error.request) {
      // 请求已发出但没有收到响应
      console.error('操作失败: 无响应', error.request)
      ElMessage.error('操作失败: 服务器无响应')
    } else {
      // 其他错误
      console.error('操作失败:', error.message)
      if (error.message !== 'Validate Failed') {
        ElMessage.error(`操作失败: ${error.message}`)
      }
    }
  } finally {
    submitting.value = false
  }
}

// 卡片悬停效果
const hoverCard = (type) => {
  const cards = document.querySelectorAll('.stats-card')
  cards.forEach(card => {
    if (!card.classList.contains(`${type}-card`)) {
      card.style.opacity = '0.7'
      card.style.transform = 'scale(0.95)'
    } else {
      card.style.transform = 'scale(1.05)'
    }
  })
}

const resetCard = () => {
  const cards = document.querySelectorAll('.stats-card')
  cards.forEach(card => {
    card.style.opacity = '1'
    card.style.transform = 'scale(1)'
  })
}

// 初始化加载数据
onMounted(() => {
  fetchDonations()
})
</script>

<style lang="scss" scoped>
.charity-donation-container {
  padding: 20px;
  background-color: #f8f9fa;
  min-height: 100vh;

  .charity-header {
    margin-bottom: 20px;

    h1 {
      color: #2c3e50;
      font-size: 24px;
      margin-bottom: 20px;
      display: flex;
      align-items: center;
      font-weight: 600;

      i {
        margin-right: 10px;
        color: #e74c3c;
      }
    }

    .stats-cards {
      display: flex;
      gap: 20px;
      margin-bottom: 20px;

      .stats-card {
        flex: 1;
        border-radius: 10px;
        transition: all 0.3s ease;
        cursor: pointer;
        border: none;

        &:hover {
          box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
        }

        .card-content {
          display: flex;
          align-items: center;
          padding: 15px;

          .card-icon {
            font-size: 36px;
            margin-right: 15px;
            width: 60px;
            height: 60px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
          }

          .card-text {
            h3 {
              margin: 0;
              font-size: 16px;
              color: #7f8c8d;
              font-weight: 500;
            }

            .amount {
              margin: 5px 0 0;
              font-size: 24px;
              font-weight: 600;
            }
          }
        }

        &.total-card {
          background: linear-gradient(135deg, #ff9a9e 0%, #fad0c4 100%);

          .card-icon {
            background-color: rgba(255, 255, 255, 0.2);
          }

          .amount {
            color: #e74c3c;
          }
        }

        &.approved-card {
          background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%);

          .card-icon {
            background-color: rgba(255, 255, 255, 0.2);
          }

          .amount {
            color: #3498db;
          }
        }

        &.pending-card {
          background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);

          .card-icon {
            background-color: rgba(255, 255, 255, 0.2);
          }

          .amount {
            color: #f39c12;
          }
        }
      }
    }
  }

  .main-content-card {
    border-radius: 10px;
    border: none;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .operation-container {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      flex-wrap: wrap;
      gap: 15px;

      .add-btn {
        background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
        border: none;
        color: white;
        font-weight: 500;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 5px 15px rgba(231, 76, 60, 0.3);
        }

        .btn-text {
          margin-left: 5px;
        }
      }

      .search-group {
        display: flex;
        align-items: center;
        gap: 10px;

        .search-input {
          width: 300px;

          :deep(.el-input__wrapper) {
            border-radius: 20px;
            padding-left: 15px;

            .search-icon {
              color: #e74c3c;
              font-size: 16px;
            }
          }
        }

        .search-btn {
          border-radius: 20px;
          background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
          border: none;
          color: white;
          font-weight: 500;
          transition: all 0.3s ease;

          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(52, 152, 219, 0.3);
          }
        }
      }
    }

    .table-container {
      .donation-table {
        :deep(.el-table__header) {
          th {
            background-color: #f5f7fa;
          }
        }

        :deep(.el-table__body) {
          tr:hover > td {
            background-color: #f0f9ff !important;
          }
        }

        .column-header {
          display: flex;
          align-items: center;

          i {
            margin-right: 5px;
            color: #e74c3c;
          }
        }

        .amount-cell {
          display: flex;
          align-items: center;
          justify-content: center;

          .currency {
            color: #e74c3c;
            font-weight: bold;
            margin-right: 2px;
          }

          .value {
            font-weight: 500;
          }
        }

        .time-cell {
          display: flex;
          align-items: center;
          justify-content: center;

          i {
            margin-right: 5px;
            color: #7f8c8d;
          }
        }

        .status-tag {
          display: flex;
          align-items: center;
          padding: 5px 10px;
          border-radius: 15px;

          i {
            margin-right: 5px;
          }
        }

        .action-btn {
          border-radius: 15px;
          font-weight: 500;
          transition: all 0.2s ease;

          &.edit-btn {
            background-color: #f0f7ff;
            color: #3498db;
            border-color: #d4e6ff;

            &:hover {
              background-color: #d4e6ff;
              transform: translateY(-2px);
            }
          }

          &.delete-btn {
            background-color: #fff0f0;
            color: #e74c3c;
            border-color: #ffd4d4;

            &:hover {
              background-color: #ffd4d4;
              transform: translateY(-2px);
            }
          }
        }
      }
    }

    .pagination-container {
      margin-top: 20px;
      display: flex;
      justify-content: center;

      :deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
        background-color: #e74c3c;
      }

      :deep(.el-pagination.is-background .el-pager li:not(.is-disabled):hover) {
        color: #e74c3c;
      }
    }
  }

  .donation-dialog {
    :deep(.el-dialog) {
      border-radius: 10px;

      .el-dialog__header {
        border-bottom: 1px solid #eee;
        padding: 15px 20px;
        margin-right: 0;

        .el-dialog__title {
          color: #2c3e50;
          font-weight: 600;
        }
      }

      .el-dialog__body {
        padding: 20px 30px;

        .form-input {
          width: 100%;

          :deep(.el-input__wrapper) {
            border-radius: 8px;

            .el-input__prefix {
              display: flex;
              align-items: center;
              padding-left: 10px;

              i {
                color: #e74c3c;
              }
            }
          }
        }
      }

      .el-dialog__footer {
        border-top: 1px solid #eee;
        padding: 15px 20px;

        .dialog-footer {
          display: flex;
          justify-content: flex-end;

          .dialog-btn {
            border-radius: 20px;
            padding: 8px 20px;
            font-weight: 500;
            transition: all 0.2s ease;

            &.cancel-btn {
              &:hover {
                color: #e74c3c;
                border-color: #e74c3c;
              }
            }

            &.confirm-btn {
              background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
              border: none;

              &:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(231, 76, 60, 0.3);
              }
            }
          }
        }
      }
    }
  }
}

// 删除确认对话框样式
.delete-confirm-dialog {
  :deep(.el-message-box__content) {
    padding: 20px;
  }

  :deep(.el-message-box__btns) {
    .el-button {
      border-radius: 20px;
      padding: 8px 20px;

      &.el-button--primary {
        background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
        border: none;
      }
    }
  }
}

// 动画效果
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.el-table__row {
  animation: fadeIn 0.3s ease forwards;
  opacity: 0;

  @for $i from 1 through 10 {
    &:nth-child(#{$i}) {
      animation-delay: $i * 0.05s;
    }
  }
}
</style>