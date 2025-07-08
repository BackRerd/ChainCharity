<template>
  <div class="charity-activity-container">
    <el-card class="box-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span class="title">慈善活动管理</span>
          <el-button
              type="primary"
              @click="handleAdd"
              class="add-btn"
              :icon="Plus"
              plain
          >
            新增活动
          </el-button>
        </div>
      </template>

      <!-- 查询表单 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true" class="query-form">
        <el-form-item label="活动标题" prop="title">
          <el-input
              v-model="queryParams.title"
              placeholder="请输入活动标题"
              clearable
              @keyup.enter="handleQuery"
              class="query-input"
          />
        </el-form-item>
        <el-form-item label="活动分类" prop="category_id">
          <el-select
              v-model="queryParams.category_id"
              placeholder="请选择分类"
              clearable
              class="query-select"
          >
            <el-option
                v-for="category in categoryOptions"
                :key="category.category_id"
                :label="category.category_name"
                :value="category.category_id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="活动时间" prop="dateRange">
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
        <el-form-item>
          <el-button type="primary" @click="handleQuery" :icon="Search" class="query-button">搜索</el-button>
          <el-button @click="resetQuery" :icon="Refresh" class="query-button">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格和分页 -->
      <div class="table-and-pagination">
        <el-table
            :data="tableData"
            v-loading="loading"
            style="width: 100%"
            stripe
            border
            :header-cell-style="{
            background: 'linear-gradient(to right, #f8f4ff, #e8f4ff)',
            color: '#5a5a5a',
            fontWeight: 'bold'
          }"
            height="calc(100vh - 320px)"
        >
          <el-table-column prop="activity_id" label="ID" width="80" align="center" />
          <el-table-column prop="title" label="活动标题" min-width="150" />
          <el-table-column prop="category_name" label="分类" width="120" align="center">
            <template #default="scope">
              <el-tag>{{ scope.row.category_name }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="活动描述" min-width="180" show-overflow-tooltip />
          <el-table-column prop="date_range" label="活动时间" width="220" align="center">
            <template #default="scope">
              <div>{{ scope.row.start_date }} 至</div>
              <div>{{ scope.row.end_date }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template #default="scope">
              <el-tag :type="getStatusTagType(scope.row.status)">
                {{ formatStatus(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="created_by" label="创建人" width="100" align="center">
            <template #default="scope">
              <el-tag :type="getUserTagType(scope.row.created_by)">{{ scope.row.created_by ? `用户${scope.row.created_by}` : '系统' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="created_at" label="创建时间" width="180" align="center" />
          <el-table-column label="操作" width="150" align="center" fixed="right">
            <template #default="scope">
              <el-button
                  size="small"
                  type="primary"
                  @click="handleEdit(scope.row)"
                  :icon="Edit"
                  plain
              />
              <el-button
                  size="small"
                  type="danger"
                  @click="handleDelete(scope.row)"
                  :icon="Delete"
                  plain
              />
            </template>
          </el-table-column>
        </el-table>

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

    <!-- 新增/编辑对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="600px"
        :close-on-click-modal="false"
        @closed="resetForm"
        class="activity-dialog"
    >
      <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="100px"
          label-position="right"
      >
        <el-form-item label="活动标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入活动标题" />
        </el-form-item>
        <el-form-item label="活动分类" prop="category_id">
          <el-select
              v-model="form.category_id"
              filterable
              remote
              reserve-keyword
              placeholder="请搜索并选择分类"
              :remote-method="handleCategorySearch"
              :loading="categorySelectLoading"
              style="width: 100%"
          >
            <el-option
                v-for="category in categoryOptions"
                :key="category.category_id"
                :label="category.category_name"
                :value="category.category_id"
            >
              <div class="category-option">
                <span>{{ category.category_name }}</span>
                <el-tag v-if="category.description" size="small" type="info" class="ml-2">
                  {{ category.description }}
                </el-tag>
              </div>
            </el-option>

            <!-- 分页控件 -->
            <div class="pagination-footer">
              <el-pagination
                  small
                  layout="prev, pager, next"
                  :current-page="categoryPagination.current"
                  :page-size="categoryPagination.size"
                  :total="categoryPagination.total"
                  @current-change="handleCategoryPageChange"
              />
            </div>
          </el-select>
        </el-form-item>
        <el-form-item label="活动时间" prop="date_range">
          <el-date-picker
              v-model="form.date_range"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
              style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="活动状态" prop="status">
          <el-select
              v-model="form.status"
              placeholder="请选择状态"
              style="width: 100%"
          >
            <el-option
                v-for="item in statusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="活动描述" prop="description">
          <el-input
              v-model="form.description"
              type="textarea"
              :rows="4"
              placeholder="请输入活动描述"
          />
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
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Edit, Delete } from '@element-plus/icons-vue'

// 状态定义
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const queryForm = ref(null)

// 数据定义
const tableData = ref([])
const total = ref(0)
const categoryOptions = ref([])

const categorySelectLoading = ref(false)
const categoryPagination = reactive({
  current: 1,
  size: 10,
  total: 0,
  keyword: ''
})

// 状态选项
const statusOptions = [
  { value: 'planning', label: '计划中' },
  { value: 'ongoing', label: '进行中' },
  { value: 'completed', label: '已完成' }
]

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  title: '',
  category_id: null,
  status: null,
  dateRange: []
})

// 表单数据
const form = reactive({
  activity_id: null,
  title: '',
  description: '',
  category_id: null,
  date_range: [],
  status: 'planning'
})

// 表单验证规则
const rules = reactive({
  title: [{ required: true, message: '请输入活动标题', trigger: 'blur' }],
  category_id: [{ required: true, message: '请选择活动分类', trigger: 'change' }],
  date_range: [{ required: true, message: '请选择活动时间', trigger: 'change' }],
  status: [{ required: true, message: '请选择活动状态', trigger: 'change' }]
})

// 获取活动列表
const getList = async () => {
  try {
    loading.value = true

    // 处理查询参数
    const params = {
      page: queryParams.pageNum,
      size: queryParams.pageSize,
      title: queryParams.title,
      categoryId: queryParams.category_id,
      status: queryParams.status
    }

    // 如果有日期范围
    if (queryParams.dateRange && queryParams.dateRange.length === 2) {
      params.startDate = queryParams.dateRange[0]
      params.endDate = queryParams.dateRange[1]
    }

    const response = await axios.get('/view-activities/page', { params })

    if (response.data.code === '200') {
      tableData.value = response.data.data.records.map(item => ({
        activity_id: item.activityId,
        title: item.title,
        description: item.description,
        category_id: item.categoryId,
        category_name: item.categoryName,
        start_date: formatDate(item.startDate),
        end_date: formatDate(item.endDate),
        status: item.status,
        created_by: item.createdBy,
        created_at: formatDateTime(item.createdAt)
      }))
      total.value = response.data.data.total
    } else {
      ElMessage.error(response.data.msg || '获取活动列表失败')
    }
  } catch (error) {
    ElMessage.error('请求失败，请稍后重试')
    console.error('获取活动列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取分类选项
const getCategoryOptions = async (keyword = '') => {
  try {
    categorySelectLoading.value = true
    categoryPagination.keyword = keyword

    const params = {
      page: categoryPagination.current,
      size: categoryPagination.size,
      CategoryName: keyword
    }

    const response = await axios.get('/categories/page', { params })

    if (response.data.code === '200') {
      categoryOptions.value = response.data.data.records.map(item => ({
        category_id: item.categoryId,
        category_name: item.categoryName,
        description: item.description
      }))
      categoryPagination.total = response.data.data.total
    } else {
      ElMessage.error(response.data.msg || '获取分类列表失败')
    }
  } catch (error) {
    ElMessage.error('获取分类列表失败，请稍后重试')
    console.error('获取分类列表失败:', error)
  } finally {
    categorySelectLoading.value = false
  }
}

// 日期格式化
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toISOString().split('T')[0]
}

// 日期时间格式化
const formatDateTime = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

// 状态标签类型
const getStatusTagType = (status) => {
  switch (status) {
    case 'planning': return 'info'
    case 'ongoing': return 'primary'
    case 'completed': return 'success'
    default: return ''
  }
}

// 用户标签类型
const getUserTagType = (userId) => {
  return userId ? '' : 'info'
}

// 格式化状态显示
const formatStatus = (status) => {
  const option = statusOptions.find(item => item.value === status)
  return option ? option.label : status
}

// 查询处理
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置查询
const resetQuery = () => {
  queryForm.value.resetFields()
  handleQuery()
}

// 分页大小变化
const handleSizeChange = (val) => {
  queryParams.pageSize = val
  getList()
}

// 当前页变化
const handleCurrentChange = (val) => {
  queryParams.pageNum = val
  getList()
}

// 新增活动
const handleAdd = () => {
  dialogTitle.value = '新增活动'
  dialogVisible.value = true
}

// 编辑活动
const handleEdit = (row) => {
  dialogTitle.value = '编辑活动'
  Object.assign(form, {
    activity_id: row.activity_id,
    title: row.title,
    description: row.description,
    category_id: row.category_id,
    date_range: [row.start_date, row.end_date],
    status: row.status
  })
  dialogVisible.value = true
}

// 删除活动
const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除活动 "${row.title}" 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await axios.delete(`/activities/${row.activity_id}`)
      if (response.data.code === '200') {
        ElMessage.success('删除成功')
        getList()
      } else {
        ElMessage.error(response.data.msg || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败，请稍后重试')
      console.error('删除活动失败:', error)
    }
  }).catch(() => {})
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate()

    const formData = {
      title: form.title,
      description: form.description,
      categoryId: form.category_id,
      startDate: form.date_range[0],
      endDate: form.date_range[1],
      status: form.status
    }

    let response
    if (form.activity_id) {
      // 编辑
      formData.activityId = form.activity_id
      response = await axios.put('/activities', formData)
    } else {
      // 新增
      response = await axios.post('/activities', formData)
    }

    if (response.data.code === '200') {
      ElMessage.success(form.activity_id ? '修改成功' : '新增成功')
      dialogVisible.value = false
      getList()
    } else {
      ElMessage.error(response.data.msg || '操作失败')
    }
  } catch (error) {
    if (error.response) {
      ElMessage.error(error.response.data.msg || '操作失败')
    }
    console.error('提交表单失败:', error)
  }
}

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields()
  Object.assign(form, {
    activity_id: null,
    title: '',
    description: '',
    category_id: null,
    date_range: [],
    status: 'planning'
  })
}
const handleCategorySearch = (query) => {
  if (query) {
    categoryPagination.current = 1
    getCategoryOptions(query)
  } else {
    getCategoryOptions()
  }
}

// 分类选择分页变化
const handleCategoryPageChange = (page) => {
  categoryPagination.current = page
  getCategoryOptions(categoryPagination.keyword)
}
// 初始化
onMounted(() => {
  getList()
  getCategoryOptions()
})
</script>
<style scoped>
.charity-activity-container {
  padding: 16px;
  height: calc(100vh - 32px);
  background-color: #f5f9fc; /* 更浅的背景色 */
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
  color: #2c3e50; /* 深色文字 */
  background: linear-gradient(to right, #1976d2, #0d47a1); /* 蓝色渐变 */
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.add-btn {
  background: linear-gradient(to right, #1976d2, #0d47a1); /* 蓝色渐变 */
  color: white;
  border: none;
  transition: all 0.3s;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(25, 118, 210, 0.3); /* 蓝色阴影 */
}

/* 紧凑型查询表单 */
.compact-query-form {
  padding: 12px 16px;
  background-color: #e3f2fd; /* 浅蓝色背景 */
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
  width: 180px;
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

/* 表格容器 */
.table-and-pagination-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0; /* 防止溢出 */
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
  background-color: #e3f2fd !important; /* 浅蓝色悬停背景 */
}

/* 分页样式 */
.pagination-container {
  padding: 12px 16px;
  background-color: #e3f2fd; /* 与查询表单一致的背景色 */
  border-radius: 0 0 8px 8px;
  border-top: 1px solid #bbdefb; /* 比背景稍深的边框 */
  display: flex;
  justify-content: flex-end;
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background: linear-gradient(to right, #1976d2, #0d47a1); /* 蓝色渐变 */
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled):hover) {
  color: #1976d2; /* 主蓝色 */
}

/* 对话框样式调整 */
.activity-dialog :deep(.el-dialog__header) {
  background: linear-gradient(to right, #e3f2fd, #bbdefb); /* 浅蓝色渐变 */
  margin-right: 0;
  padding: 16px 20px;
  border-bottom: 1px solid #90caf9;
}

.activity-dialog :deep(.el-dialog__title) {
  color: #2c3e50;
  font-weight: bold;
}

.confirm-btn {
  background: linear-gradient(to right, #1976d2, #0d47a1); /* 蓝色渐变 */
  border: none;
  transition: all 0.3s;
}

.confirm-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(25, 118, 210, 0.3); /* 蓝色阴影 */
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .compact-query-form {
    gap: 6px;
  }

  .query-input {
    width: 150px;
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