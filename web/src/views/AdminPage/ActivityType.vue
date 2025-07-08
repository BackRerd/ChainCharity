<template>
  <div class="charity-category-container">
    <el-card class="box-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span class="title">慈善活动分类管理</span>
          <el-button
              type="primary"
              @click="handleAdd"
              class="add-btn"
              :icon="Plus"
              plain
          >
            新增分类
          </el-button>
        </div>
      </template>

      <!-- 查询表单 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true" class="query-form">
        <el-form-item label="分类名称" prop="categoryName">
          <el-input
              v-model="queryParams.categoryName"
              placeholder="请输入分类名称"
              clearable
              @keyup.enter="handleQuery"
              class="query-input"
          />
        </el-form-item>
        <el-form-item label="创建时间" prop="dateRange">
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
            height="calc(100vh - 290px)"
        >
          <el-table-column prop="categoryId" label="ID" width="80" align="center" />
          <el-table-column prop="categoryName" label="分类名称" min-width="120" />
          <el-table-column prop="description" label="描述" min-width="180" show-overflow-tooltip />
          <el-table-column prop="createdBy" label="创建人" width="100" align="center">
            <template #default="scope">
              <el-tag :type="getUserTagType(scope.row.created_by)">{{ scope.row.created_by ? `用户${scope.row.created_by}` : '系统' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="创建时间" width="180" align="center" />
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

        <!-- 分页 - 现在与表格更紧密 -->
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
        width="500px"
        :close-on-click-modal="false"
        @closed="resetForm"
        class="category-dialog"
    >
      <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="100px"
          label-position="right"
      >
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="form.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类描述" prop="description">
          <el-input
              v-model="form.description"
              type="textarea"
              :rows="4"
              placeholder="请输入分类描述"
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
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

// 查询表单引用
const queryForm = ref(null)
// 表单引用
const formRef = ref(null)
// 对话框标题
const dialogTitle = ref('')
// 对话框显示状态
const dialogVisible = ref(false)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 9,
  categoryName: '',
  dateRange: []
})

// 表格数据
const tableData = ref([])
const total = ref(0)
const loading = ref(false)

// 表单数据
const form = reactive({
  categoryId: undefined,
  categoryName: '',
  description: '',
  createdBy: null,
  created_at: ''
})

// 表单验证规则
const rules = reactive({
  categoryName: [
    { required: true, message: '分类名称不能为空', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在2到50个字符', trigger: 'blur' }
  ],
  description: [
    { max: 500, message: '描述不能超过500个字符', trigger: 'blur' }
  ]
})

// 获取分类列表
const getList = async () => {
  try {
    loading.value = true
    const params = {
      page: queryParams.pageNum,
      size: queryParams.pageSize,
      // CategoryName: queryParams.categoryName
    }

    const response = await axios.get('/categories/page', { params })
    if (response.data.code === "200") {
      tableData.value = response.data.data.records
      total.value = response.data.data.total
      console.log(response)
    } else {
      ElMessage.error(response.data.msg || '获取分类列表失败')
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
    ElMessage.error('获取分类列表失败')
  } finally {
    loading.value = false
  }
}

// 查询
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

// 新增分类
const handleAdd = () => {
  dialogTitle.value = '新增分类'
  dialogVisible.value = true
}

// 编辑分类
const handleEdit = async (row) => {
  try {
    const response = await axios.get(`/categories/${row.categoryId}`)
    if (response.data.code === "200") {
      Object.assign(form, response.data.data)
      dialogTitle.value = '编辑分类'
      dialogVisible.value = true
    } else {
      ElMessage.error(response.data.msg || '获取分类信息失败')
    }
  } catch (error) {
    console.error('获取分类信息失败:', error)
    ElMessage.error('获取分类信息失败')
  }
}

// 删除分类
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该分类吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await axios.delete(`/categories/${row.categoryId}`)
      if (response.data.code === "200") {
        ElMessage.success('删除成功')
        getList()
      } else {
        ElMessage.error(response.data.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除分类失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 提交表单
const submitForm = async () => {
  await formRef.value.validate()
  try {
    let response
    if (form.categoryId) {
      response = await axios.put('/categories', form)
    } else {
      response = await axios.post('/categories', form)
    }

    if (response.data.code === "200") {
      ElMessage.success('操作成功')
      dialogVisible.value = false
      getList()
    } else {
      ElMessage.error(response.data.msg || '操作失败')
    }
  } catch (error) {
    console.error('操作失败:', error)
    if (error.response && error.response.status === 409) {
      ElMessage.error('分类名称已存在')
    } else {
      ElMessage.error('操作失败')
    }
  }
}

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields()
  Object.assign(form, {
    categoryId: undefined,
    categoryName: '',
    description: '',
    created_by: null,
    created_at: ''
  })
}

// 用户标签类型
const getUserTagType = (userId) => {
  return userId ? '' : 'info'
}

// 初始化加载数据
onMounted(() => {
  getList()
})
</script>

<style scoped>
.charity-category-container {
  padding: 20px;
  height: 100%;
}

.box-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.query-form {
  margin-bottom: 20px;
}

.query-input {
  width: 200px;
}

.query-date {
  width: 250px;
}

.query-button {
  margin-left: 10px;
}

.table-and-pagination {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.pagination-container {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}

.category-dialog .el-form-item {
  margin-bottom: 20px;
}

.confirm-btn {
  margin-left: 10px;
}
</style>

<style scoped>
.charity-category-container {
  padding: 16px;
  height: calc(100vh - 32px);
  background-color: #f8f9fa;
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
  padding: 0 16px;
}

.title {
  font-size: 18px;
  font-weight: bold;
  color: #5a5a5a;
  background: linear-gradient(to right, #6a11cb, #2575fc);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.add-btn {
  background: linear-gradient(to right, #6a11cb, #2575fc);
  color: white;
  border: none;
  transition: all 0.3s;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(106, 17, 203, 0.3);
}

.query-form {
  padding: 16px;
  background-color: #f8f4ff;
  border-radius: 8px;
  margin-bottom: 12px; /* 减少与表格的间距 */
}


.query-input {
  width: 200px;
}

.query-date {
  width: 250px;
}

.query-button {
  transition: all 0.3s;
}

.query-button:hover {
  transform: translateY(-1px);
}
/* 新增的表格和分页容器 */
.table-and-pagination {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #fff;
  border-radius: 8px;
  padding: 0; /* 移除内边距 */
  overflow: hidden;
}

.table-wrapper {
  flex: 1;
  overflow: hidden;
}

.el-table {
  border-radius: 8px 8px 0 0; /* 只圆角上方 */
  overflow: hidden;
  flex: 1;
}

.el-table :deep(.el-table__row) {
  transition: all 0.3s;
}

.el-table :deep(.el-table__row:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  background-color: #f8f4ff !important;
}

/* 分页样式调整 */
.pagination-container {
  padding: 12px 16px;
  background-color: #f8f4ff;
  border-radius: 0 0 8px 8px; /* 只圆角下方 */
  border-top: 1px solid #ebeef5;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background: linear-gradient(to right, #6a11cb, #2575fc);
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled):hover) {
  color: #6a11cb;
}

.category-dialog :deep(.el-dialog__header) {
  background: linear-gradient(to right, #f8f4ff, #e8f4ff);
  margin-right: 0;
  padding: 16px 20px;
  border-bottom: 1px solid #e6e6e6;
}
.category-dialog :deep(.el-dialog__title) {
  color: #5a5a5a;
  font-weight: bold;
}

.confirm-btn {
  background: linear-gradient(to right, #6a11cb, #2575fc);
  border: none;
  transition: all 0.3s;
}
.confirm-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(106, 17, 203, 0.3);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .query-form {
    flex-direction: column;
  }

  .query-input, .query-date {
    width: 100%;
    margin-bottom: 10px;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .add-btn {
    margin-top: 10px;
    align-self: flex-end;
  }
}
</style>