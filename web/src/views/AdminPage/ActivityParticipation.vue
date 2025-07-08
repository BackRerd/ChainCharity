<template>
  <div class="participation-management-container">
    <el-card class="box-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span class="title">活动参与管理</span>
          <el-button
              type="primary"
              @click="handleAdd"
              class="add-btn"
              :icon="Plus"
              plain
          >
            新增参与记录
          </el-button>
        </div>
      </template>

      <!-- 查询表单 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true" class="compact-query-form">
        <el-form-item label="活动名称" prop="activity_id">
          <el-select
              v-model="queryParams.activity_id"
              placeholder="选择活动"
              clearable
              filterable
              class="query-select"
          >
            <el-option
                v-for="activity in activityOptions"
                :key="activity.activity_id"
                :label="activity.title"
                :value="activity.activity_id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="用户ID" prop="user_id">
          <el-input
              v-model="queryParams.user_id"
              placeholder="输入用户ID"
              clearable
              class="query-input"
          />
        </el-form-item>
        <el-form-item label="参与角色" prop="role">
          <el-select
              v-model="queryParams.role"
              placeholder="选择角色"
              clearable
              class="query-select"
          >
            <el-option
                v-for="item in roleOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态" prop="approval_status">
          <el-select
              v-model="queryParams.approval_status"
              placeholder="选择状态"
              clearable
              class="query-select"
          >
            <el-option
                v-for="item in statusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="参与时间" prop="dateRange">
          <el-date-picker
              v-model="queryParams.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
              class="query-date"
          />
        </el-form-item>
        <el-form-item class="query-buttons">
          <el-button type="primary" @click="handleQuery" :icon="Search" class="query-button">搜索</el-button>
          <el-button @click="resetQuery" :icon="Refresh" class="query-button">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格和分页 -->
      <div class="table-and-pagination-wrapper">
        <div class="table-container">
          <el-table
              :data="tableData"
              v-loading="loading"
              style="width: 100%"
              stripe
              border
              :header-cell-style="{
              background: 'linear-gradient(to right, #f0f7ff, #d0e3ff)',
              color: '#2c3e50',
              fontWeight: 'bold'
            }"
              height="calc(100vh - 300px)"
          >
            <el-table-column prop="participation_id" label="参与ID" width="90" align="center" />
            <el-table-column prop="activity_title" label="活动名称" min-width="150" show-overflow-tooltip />
            <el-table-column prop="user_name" label="用户姓名" width="120" align="center">
              <template #default="scope">
                <el-tag>{{ scope.row.user_name || `用户${scope.row.user_id}` }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="role" label="参与角色" width="100" align="center">
              <template #default="scope">
                <el-tag :type="scope.row.role === 'donor' ? 'success' : 'warning'">
                  {{ formatRole(scope.row.role) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="participation_time" label="参与时间" width="180" align="center" />
            <el-table-column prop="approval_status" label="审核状态" width="120" align="center">
              <template #default="scope">
                <el-tag :type="getStatusTagType(scope.row.approval_status)">
                  {{ formatStatus(scope.row.approval_status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="reviewer_name" label="审核人" width="120" align="center">
              <template #default="scope">
                <span v-if="scope.row.reviewed_by">
                  <el-tag>{{ scope.row.reviewer_name || `管理员${scope.row.reviewed_by}` }}</el-tag>
                </span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column prop="reviewed_at" label="审核时间" width="180" align="center">
              <template #default="scope">
                {{ scope.row.reviewed_at || '-' }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template #default="scope">
                <el-button
                    v-if="scope.row.approval_status === 'pending'"
                    size="small"
                    type="success"
                    @click="handleApprove(scope.row)"
                    :icon="CircleCheck"
                    plain
                >通过</el-button>
                <el-button
                    v-if="scope.row.approval_status === 'pending'"
                    size="small"
                    type="danger"
                    @click="handleReject(scope.row)"
                    :icon="CircleClose"
                    plain
                >拒绝</el-button>
                <el-button
                    size="small"
                    type="danger"
                    @click="handleDelete(scope.row)"
                    :icon="Delete"
                    plain
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
              v-model:current-page="queryParams.pageNum"
              v-model:page-size="queryParams.pageSize"
              :page-sizes="[10, 20, 30, 50]"
              :background="true"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </el-card>

    <!-- 新增参与记录对话框 -->
    <el-dialog
        v-model="dialogVisible"
        title="新增参与记录"
        width="500px"
        :close-on-click-modal="false"
        @closed="resetForm"
        class="participation-dialog"
    >
      <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="100px"
          label-position="right"
      >
        <el-form-item label="选择活动" prop="activity_id">
          <el-select
              v-model="form.activity_id"
              placeholder="请选择活动"
              style="width: 100%"
              filterable
          >
            <el-option
                v-for="activity in activityOptions"
                :key="activity.activity_id"
                :label="activity.title"
                :value="activity.activity_id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="用户ID" prop="user_id">
          <el-input
              v-model="form.user_id"
              placeholder="请输入用户ID"
          />
        </el-form-item>
        <el-form-item label="参与角色" prop="role">
          <el-select
              v-model="form.role"
              placeholder="请选择角色"
              style="width: 100%"
          >
            <el-option
                v-for="item in roleOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" class="confirm-btn">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh, Plus, CircleCheck, CircleClose, Delete } from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

// 数据状态
const loading = ref(false)
const dialogVisible = ref(false)
const tableData = ref([])
const total = ref(0)
const activityOptions = ref([])

// 查询参数
const queryParams = reactive({
  activity_id: null,
  user_id: null,
  role: null,
  approval_status: null,
  dateRange: [],
  pageNum: 1,
  pageSize: 10
})

// 表单数据
const form = reactive({
  activity_id: null,
  user_id: null,
  role: null
})

// 表单验证规则
const rules = reactive({
  activity_id: [{ required: true, message: '请选择活动', trigger: 'blur' }],
  user_id: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'blur' }]
})

// 选项数据
const roleOptions = ref([
  { value: 'donor', label: '捐赠者' },
  { value: 'recipient', label: '受助者' }
])

const statusOptions = ref([
  { value: 'pending', label: '待审核' },
  { value: 'approved', label: '已通过' },
  { value: 'rejected', label: '已拒绝' }
])

// 格式化方法
const formatRole = (role) => {
  const option = roleOptions.value.find(item => item.value === role)
  return option ? option.label : role
}

const formatStatus = (status) => {
  const option = statusOptions.value.find(item => item.value === status)
  return option ? option.label : status
}

const getStatusTagType = (status) => {
  switch (status) {
    case 'approved': return 'success'
    case 'rejected': return 'danger'
    case 'pending': return 'warning'
    default: return 'info'
  }
}

// API 请求方法
const fetchParticipations = async () => {
  try {
    loading.value = true
    const params = {
      page: queryParams.pageNum,
      size: queryParams.pageSize,
      activityId: queryParams.activity_id,
      userId: queryParams.user_id,
      role: queryParams.role,
      approvalStatus: queryParams.approval_status
    }

    // 添加日期范围参数
    if (queryParams.dateRange && queryParams.dateRange.length === 2) {
      params.startTime = queryParams.dateRange[0] + ' 00:00:00'
      params.endTime = queryParams.dateRange[1] + ' 23:59:59'
    }

    const response = await axios.get('/participations/page-view', { params })
    if (response.data.code === '200') {
      tableData.value = response.data.data.records.map(item => ({
        participation_id: item.participationId,
        activity_id: item.activityId,
        activity_title: item.activityTitle,
        user_id: item.userId,
        user_name: item.participantUsername,
        role: item.role,
        participation_time: formatDateTime(item.participationTime),
        approval_status: item.approvalStatus,
        reviewed_by: item.reviewedBy,
        reviewed_at: item.reviewedAt ? formatDateTime(item.reviewedAt) : null
      }))
      total.value = response.data.data.total
    } else {
      ElMessage.error(response.data.msg || '获取数据失败')
    }
  } catch (error) {
    ElMessage.error('请求失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

const fetchActivities = async () => {
  try {
    // 这里需要替换为获取活动列表的实际API
    // const response = await axios.get('/activities/list')
    // 模拟数据
    activityOptions.value = [
      { activity_id: 1018, title: 'Ms.' },
      { activity_id: 1028, title: 'Prof.' }
    ]
  } catch (error) {
    ElMessage.error('获取活动列表失败: ' + error.message)
  }
}

// 日期时间格式化
const formatDateTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  }).replace(/\//g, '-')
}

// 事件处理
const handleQuery = () => {
  queryParams.pageNum = 1
  fetchParticipations()
}

const resetQuery = () => {
  queryParams.activity_id = null
  queryParams.user_id = null
  queryParams.role = null
  queryParams.approval_status = null
  queryParams.dateRange = []
  handleQuery()
}

const handleSizeChange = (val) => {
  queryParams.pageSize = val
  fetchParticipations()
}

const handleCurrentChange = (val) => {
  queryParams.pageNum = val
  fetchParticipations()
}

const handleAdd = () => {
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    const formData = {
      activityId: form.activity_id,
      userId: form.user_id,
      role: form.role
    }

    const response = await axios.post('/participations', formData)
    if (response.data.code === '200') {
      ElMessage.success('添加成功')
      dialogVisible.value = false
      fetchParticipations()
    } else {
      ElMessage.error(response.data.msg || '添加失败')
    }
  } catch (error) {
    ElMessage.error('请求失败: ' + error.message)
  }
}

const handleApprove = async (row) => {
  try {
    const response = await axios.put('/participations', {
      participationId: row.participation_id,
      approvalStatus: 'approved',
      reviewedBy: 1 // 这里应该是当前登录用户的ID
    })
    if (response.data.code === '200') {
      ElMessage.success('审核通过')
      fetchParticipations()
    } else {
      ElMessage.error(response.data.msg || '操作失败')
    }
  } catch (error) {
    ElMessage.error('请求失败: ' + error.message)
  }
}

const handleReject = async (row) => {
  try {
    const response = await axios.put('/participations', {
      participationId: row.participation_id,
      approvalStatus: 'rejected',
      reviewedBy: 1 // 这里应该是当前登录用户的ID
    })
    if (response.data.code === '200') {
      ElMessage.success('已拒绝')
      fetchParticipations()
    } else {
      ElMessage.error(response.data.msg || '操作失败')
    }
  } catch (error) {
    ElMessage.error('请求失败: ' + error.message)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条参与记录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await axios.delete(`/participations/${row.participation_id}`)
      if (response.data.code === '200') {
        ElMessage.success('删除成功')
        fetchParticipations()
      } else {
        ElMessage.error(response.data.msg || '删除失败')
      }
    } catch (error) {
      ElMessage.error('请求失败: ' + error.message)
    }
  }).catch(() => {})
}

const resetForm = () => {
  form.activity_id = null
  form.user_id = null
  form.role = null
}

// 初始化
onMounted(() => {
  fetchParticipations()
  fetchActivities()
})
</script>

<style scoped>
.participation-management-container {
  padding: 16px;
  height: calc(100vh - 32px);
  background-color: #f5f9fc;
}

.box-card {
  height: 100%;
  border-radius: 8px;
  border: none;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #e0e0e0;
}

.title {
  font-size: 18px;
  font-weight: bold;
  color: #2c3e50;
  background: linear-gradient(to right, #1e88e5, #0d47a1);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.add-btn {
  background: linear-gradient(to right, #1e88e5, #0d47a1);
  color: white;
  border: none;
  transition: all 0.3s;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(30, 136, 229, 0.3);
}

.compact-query-form {
  padding: 12px 16px;
  background-color: #e3f2fd;
  border-radius: 8px;
  margin-bottom: 12px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}

.compact-query-form .el-form-item {
  margin-bottom: 0;
}

.compact-query-form .el-form-item__label {
  padding-right: 5px;
  font-size: 13px;
}

.query-input {
  width: 120px;
}

.query-select {
  width: 120px;
}

.query-date {
  width: 240px;
}

.query-button {
  transition: all 0.3s;
  padding: 8px 12px;
}

.query-button:hover {
  transform: translateY(-1px);
}

.table-and-pagination-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

.table-container {
  flex: 1;
  overflow: hidden;
  background-color: #fff;
  border-radius: 8px 8px 0 0;
}

.el-table {
  border-radius: 8px 8px 0 0;
}

.el-table :deep(.el-table__row) {
  transition: all 0.3s;
}

.el-table :deep(.el-table__row:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  background-color: #e3f2fd !important;
}

.pagination-container {
  padding: 12px 16px;
  background-color: #e3f2fd;
  border-radius: 0 0 8px 8px;
  border-top: 1px solid #bbdefb;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background: linear-gradient(to right, #1e88e5, #0d47a1);
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled):hover) {
  color: #1e88e5;
}

.participation-dialog :deep(.el-dialog__header) {
  background: linear-gradient(to right, #e3f2fd, #bbdefb);
  margin-right: 0;
  padding: 16px 20px;
  border-bottom: 1px solid #90caf9;
}

.participation-dialog :deep(.el-dialog__title) {
  color: #2c3e50;
  font-weight: bold;
}

.confirm-btn {
  background: linear-gradient(to right, #1e88e5, #0d47a1);
  border: none;
  transition: all 0.3s;
}

.confirm-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(30, 136, 229, 0.3);
}

@media (max-width: 1200px) {
  .compact-query-form {
    gap: 6px;
  }

  .query-input {
    width: 100px;
  }

  .query-select {
    width: 100px;
  }

  .query-date {
    width: 200px;
  }
}

@media (max-width: 992px) {
  .compact-query-form {
    flex-direction: column;
    align-items: flex-start;
  }

  .query-input, .query-select, .query-date {
    width: 100%;
    margin-bottom: 8px;
  }

  .query-buttons {
    width: 100%;
    display: flex;
    justify-content: flex-end;
  }
}

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .add-btn {
    margin-top: 10px;
    align-self: flex-end;
  }

  .el-table :deep(.el-table__cell) {
    padding: 8px 0;
  }
}
</style>