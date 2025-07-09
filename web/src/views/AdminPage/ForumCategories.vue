
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

// 表格数据
const tableData = ref([])
const loading = ref(false)

// 分页数据
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 搜索表单
const searchForm = reactive({
  name: '',
  parentId: null,
  isActive: null
})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const formData = reactive({
  categoryId: null,
  name: '',
  description: '',
  parentId: null,
  sortOrder: 0,
  isActive: 1
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  sortOrder: [
    { required: true, message: '请输入排序值', trigger: 'blur' }
  ]
}

// 初始化加载数据
onMounted(() => {
  fetchData()
})

// 获取数据
const fetchData = async () => {
  try {
    loading.value = true
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }

    const response = await axios.get('/forum/categories/page', { params })
    const { data } = response.data

    tableData.value = data.records
    pagination.total = data.total
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

// 重置搜索
const resetSearch = () => {
  searchForm.name = ''
  searchForm.parentId = null
  searchForm.isActive = null
  handleSearch()
}

// 分页大小改变
const handleSizeChange = (val) => {
  pagination.pageSize = val
  fetchData()
}

// 当前页改变
const handleCurrentChange = (val) => {
  pagination.pageNum = val
  fetchData()
}

// 新增分类
const handleCreate = () => {
  dialogTitle.value = '新增分类'
  resetForm()
  dialogVisible.value = true
}

// 编辑分类
const handleEdit = (row) => {
  dialogTitle.value = '编辑分类'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  formData.categoryId = null
  formData.name = ''
  formData.description = ''
  formData.parentId = null
  formData.sortOrder = 0
  formData.isActive = 1
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate()

    if (formData.categoryId) {
      // 更新
      await axios.put(`/forum/categories/${formData.categoryId}`, formData)
      ElMessage.success('更新成功')
    } else {
      // 新增
      await axios.post('/forum/categories', formData)
      ElMessage.success('新增成功')
    }

    dialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 删除分类
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除此分类吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/forum/categories/${id}`)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}
</script>

<template>
  <div class="forum-categories-container">
    <el-card shadow="hover" class="animated-card">
      <template #header>
        <div class="card-header">
          <span class="title-text">论坛分类管理</span>
          <el-button type="primary" @click="handleCreate" icon="Plus" circle class="add-button" />
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form" @submit.prevent>
        <el-form-item label="分类名称" class="search-item">
          <el-input
              v-model="searchForm.name"
              placeholder="请输入分类名称"
              clearable
              suffix-icon="Search"
              @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="父级ID" class="search-item">
          <el-input-number
              v-model="searchForm.parentId"
              placeholder="请输入父级ID"
              :min="0"
              controls-position="right"
              class="number-input"
          />
        </el-form-item>
        <el-form-item label="状态" class="search-item">
          <el-select
              v-model="searchForm.isActive"
              placeholder="请选择状态"
              clearable
              class="status-select"
          >
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item class="search-item">
          <el-button
              type="primary"
              @click="handleSearch"
              icon="Search"
              class="search-button"
          >搜索</el-button>
          <el-button
              @click="resetSearch"
              icon="Refresh"
              class="reset-button"
          >重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table
          :data="tableData"
          border
          style="width: 100%"
          v-loading="loading"
          class="data-table"
          :row-class-name="tableRowClassName"
          @row-click="handleRowClick"
      >
        <el-table-column prop="categoryId" label="ID" width="80" align="center" />
        <el-table-column prop="name" label="分类名称" width="150">
          <template #default="{ row }">
            <span class="category-name">{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="parentId" label="父级ID" width="100" align="center" />
        <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag
                :type="row.isActive === 1 ? 'success' : 'danger'"
                effect="dark"
                class="status-tag"
            >
              {{ row.isActive === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" align="center">
          <template #default="{ row }">
            <div class="timestamp">
              <el-icon><Clock /></el-icon>
              {{ formatDate(row.createdAt) }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="{ row }">
            <el-button
                size="small"
                @click.stop="handleEdit(row)"
                icon="Edit"
                class="action-button edit-button"
            >编辑</el-button>
            <el-button
                size="small"
                type="danger"
                @click.stop="handleDelete(row.categoryId)"
                icon="Delete"
                class="action-button delete-button"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
            v-model:current-page="pagination.pageNum"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            background
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="50%"
        class="form-dialog"
        :close-on-click-modal="false"
    >
      <el-form
          ref="formRef"
          :model="formData"
          :rules="rules"
          label-width="100px"
          label-position="top"
      >
        <el-form-item label="分类名称" prop="name">
          <el-input
              v-model="formData.name"
              placeholder="请输入分类名称"
              clearable
              class="form-input"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
              v-model="formData.description"
              type="textarea"
              placeholder="请输入描述"
              :rows="3"
              resize="none"
              class="form-textarea"
          />
        </el-form-item>
        <div class="form-row">
          <el-form-item label="父级ID" prop="parentId" class="form-col">
            <el-input-number
                v-model="formData.parentId"
                :min="0"
                placeholder="请输入父级ID"
                controls-position="right"
                class="form-number-input"
            />
          </el-form-item>
          <el-form-item label="排序" prop="sortOrder" class="form-col">
            <el-input-number
                v-model="formData.sortOrder"
                :min="0"
                placeholder="请输入排序值"
                controls-position="right"
                class="form-number-input"
            />
          </el-form-item>
        </div>
        <el-form-item label="状态" prop="isActive">
          <el-radio-group v-model="formData.isActive" class="status-radio-group">
            <el-radio :label="1" border>启用</el-radio>
            <el-radio :label="0" border>禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" class="dialog-button cancel-button">取消</el-button>
          <el-button type="primary" @click="submitForm" class="dialog-button confirm-button">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.forum-categories-container {
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  min-height: 100vh;
}

.animated-card {
  transition: all 0.3s ease;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
  border: none;
}

.animated-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  background: linear-gradient(to right, #409eff, #79bbff);
  color: white;
}

.title-text {
  font-size: 18px;
  font-weight: bold;
  letter-spacing: 1px;
}

.add-button {
  font-size: 16px;
  box-shadow: 0 2px 10px rgba(64, 158, 255, 0.3);
  transition: all 0.3s ease;
}

.add-button:hover {
  transform: rotate(90deg) scale(1.1);
}

.search-form {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.search-item {
  margin-right: 15px;
  margin-bottom: 10px;
}

.number-input {
  width: 120px;
}

.status-select {
  width: 120px;
}

.search-button, .reset-button {
  padding: 8px 15px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.search-button:hover {
  background: #66b1ff;
  transform: translateY(-2px);
}

.reset-button:hover {
  background: #f0f0f0;
  transform: translateY(-2px);
}

.data-table {
  margin-top: 15px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.data-table :deep(.el-table__row) {
  transition: all 0.3s ease;
}

.data-table :deep(.el-table__row:hover) {
  transform: scale(1.01);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  background-color: #f5f7fa !important;
}

.category-name {
  font-weight: 500;
  color: #409eff;
}

.status-tag {
  border-radius: 12px;
  padding: 0 10px;
  font-weight: bold;
}

.timestamp {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #666;
}

.action-button {
  padding: 5px 10px;
  border-radius: 15px;
  transition: all 0.3s ease;
}

.edit-button:hover {
  background: #ecf5ff;
  color: #409eff;
  transform: translateY(-2px);
}

.delete-button:hover {
  background: #fef0f0;
  color: #f56c6c;
  transform: translateY(-2px);
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  padding: 15px 0;
}

.form-dialog {
  border-radius: 12px;
  overflow: hidden;
}

.form-dialog :deep(.el-dialog__header) {
  background: linear-gradient(to right, #409eff, #79bbff);
  margin-right: 0;
  padding: 15px 20px;
  color: white;
}

.form-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: bold;
}

.form-input, .form-textarea {
  border-radius: 8px;
}

.form-textarea :deep(.el-textarea__inner) {
  min-height: 80px !important;
}

.form-row {
  display: flex;
  gap: 20px;
}

.form-col {
  flex: 1;
}

.form-number-input {
  width: 100%;
}

.status-radio-group {
  width: 100%;
}

.status-radio-group :deep(.el-radio) {
  margin-right: 15px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.dialog-button {
  padding: 8px 20px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.cancel-button:hover {
  background: #f0f0f0;
  transform: translateY(-2px);
}

.confirm-button:hover {
  background: #66b1ff;
  transform: translateY(-2px);
}

/* 动画效果 */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.data-table :deep(.el-table__row) {
  animation: fadeIn 0.5s ease forwards;
  opacity: 0;
}

.data-table :deep(.el-table__row:nth-child(1)) { animation-delay: 0.1s; }
.data-table :deep(.el-table__row:nth-child(2)) { animation-delay: 0.2s; }
.data-table :deep(.el-table__row:nth-child(3)) { animation-delay: 0.3s; }
.data-table :deep(.el-table__row:nth-child(4)) { animation-delay: 0.4s; }
.data-table :deep(.el-table__row:nth-child(5)) { animation-delay: 0.5s; }
</style>