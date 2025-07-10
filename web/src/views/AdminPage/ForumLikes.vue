

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh, Plus, Delete } from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

// API基础路径
const API_BASE = '/forum/likes'

// 表格数据
const tableData = ref([])
const loading = ref(false)
const total = ref(0)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  userId: null,
  postId: null,
  replyId: null
})

// 表单数据
const formData = reactive({
  userId: null,
  postId: null,
  replyId: null
})

// 表单验证规则
const rules = {
  userId: [
    { required: true, message: '请输入用户ID', trigger: 'blur' },
    { type: 'number', message: '用户ID必须为数字', trigger: 'blur' }
  ],
  postId: [
    { required: true, message: '请输入帖子ID', trigger: 'blur' },
    { type: 'number', message: '帖子ID必须为数字', trigger: 'blur' }
  ]
}

// 对话框控制
const dialogVisible = ref(false)
const formRef = ref(null)

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString()
}

// 获取点赞列表
const fetchData = async () => {
  try {
    loading.value = true
    const params = {
      pageNum: queryParams.pageNum,
      pageSize: queryParams.pageSize,
      userId: queryParams.userId || undefined,
      postId: queryParams.postId || undefined,
      replyId: queryParams.replyId || undefined
    }

    const response = await axios.get(`${API_BASE}/page`, { params })
    if (response.data && response.data.data) {
      tableData.value = response.data.data.records
      total.value = response.data.data.total
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 查询
const handleQuery = () => {
  queryParams.pageNum = 1
  fetchData()
}

// 重置查询
const resetQuery = () => {
  queryParams.userId = null
  queryParams.postId = null
  queryParams.replyId = null
  handleQuery()
}

// 分页大小改变
const handleSizeChange = (val) => {
  queryParams.pageSize = val
  fetchData()
}

// 当前页改变
const handleCurrentChange = (val) => {
  queryParams.pageNum = val
  fetchData()
}

// 打开新增对话框
const openDialog = () => {
  Object.keys(formData).forEach(key => {
    formData[key] = null
  })
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate()

    const response = await axios.post(API_BASE, formData)
    if (response.data && response.data.success) {
      ElMessage.success('新增成功')
      dialogVisible.value = false
      fetchData()
    } else {
      ElMessage.error('新增失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  }
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('确认删除这条点赞记录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await axios.delete(`${API_BASE}/${id}`)
      console.log(response)
      if (response.data.code === "200") {
        ElMessage.success('删除成功')
        await fetchData()
      } else {
        ElMessage.error('删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 初始化加载数据
onMounted(() => {
  fetchData()
})
</script>
<template>
  <div class="forum-likes-container">
    <el-card class="box-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span class="header-title">论坛点赞管理</span>
          <div class="header-decoration"></div>
        </div>
      </template>

      <!-- 查询表单 -->
      <el-form :model="queryParams" inline class="search-form" @submit.prevent>
        <el-form-item label="用户ID" class="fade-in-item">
          <el-input
              v-model.number="queryParams.userId"
              placeholder="请输入用户ID"
              clearable
              @keyup.enter="handleQuery"
          >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="帖子ID" class="fade-in-item" style="animation-delay: 0.1s">
          <el-input
              v-model.number="queryParams.postId"
              placeholder="请输入帖子ID"
              clearable
              @keyup.enter="handleQuery"
          >
            <template #prefix>
              <el-icon><Document /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="回复ID" class="fade-in-item" style="animation-delay: 0.2s">
          <el-input
              v-model.number="queryParams.replyId"
              placeholder="请输入回复ID"
              clearable
              @keyup.enter="handleQuery"
          >
            <template #prefix>
              <el-icon><ChatLineRound /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item class="fade-in-item" style="animation-delay: 0.3s">
          <el-button type="primary" @click="handleQuery" :icon="Search" class="action-btn">查询</el-button>
          <el-button @click="resetQuery" :icon="Refresh" class="action-btn">重置</el-button>
          <el-button
              type="success"
              @click="openDialog"
              :icon="Plus"
              class="action-btn add-btn"
          >
            新增
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table
          :data="tableData"
          border
          stripe
          v-loading="loading"
          class="data-table"
          highlight-current-row
          @row-click="handleRowClick"
      >
        <el-table-column prop="likeId" label="点赞ID" width="80" align="center" />
        <el-table-column prop="userId" label="用户ID" width="100" align="center">
          <template #default="{ row }">
            <el-tag type="info" effect="plain">{{ row.userId }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="postId" label="帖子ID" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.postId" type="primary" effect="light">{{ row.postId }}</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="replyId" label="回复ID" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.replyId" type="warning" effect="light">{{ row.replyId }}</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            <div class="timestamp">
              <el-icon><Clock /></el-icon>
              <span>{{ formatDate(row.createdAt) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template #default="{ row }">
            <el-popconfirm
                title="确定要删除这条点赞记录吗？"
                @confirm="handleDelete(row.likeId)"
            >
              <template #reference>
                <el-button
                    type="danger"
                    size="small"
                    :icon="Delete"
                    circle
                    class="delete-btn"
                />
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
            v-model:current-page="queryParams.pageNum"
            v-model:page-size="queryParams.pageSize"
            :total="total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            background
        />
      </div>
    </el-card>

    <!-- 新增对话框 -->
    <el-dialog
        v-model="dialogVisible"
        title="新增点赞"
        width="500px"
        :close-on-click-modal="false"
        class="custom-dialog"
    >
      <el-form
          :model="formData"
          label-width="100px"
          :rules="rules"
          ref="formRef"
          status-icon
      >
        <el-form-item label="用户ID" prop="userId">
          <el-input
              v-model.number="formData.userId"
              placeholder="请输入用户ID"
              autocomplete="off"
          >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="帖子ID" prop="postId">
          <el-input
              v-model.number="formData.postId"
              placeholder="请输入帖子ID"
              autocomplete="off"
          >
            <template #prefix>
              <el-icon><Document /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="回复ID" prop="replyId">
          <el-input
              v-model.number="formData.replyId"
              placeholder="请输入回复ID"
              autocomplete="off"
          >
            <template #prefix>
              <el-icon><ChatLineRound /></el-icon>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
          <el-button
              type="primary"
              @click="submitForm"
              class="confirm-btn"
              :loading="submitting"
          >
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<style scoped>
.forum-likes-container {
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  min-height: 100vh;
}

.box-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.box-card:hover {
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12);
}

.card-header {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  position: relative;
  padding-bottom: 15px;
}

.header-title {
  font-size: 20px;
  font-weight: 600;
  color: #34495e;
}

.header-decoration {
  width: 60px;
  height: 4px;
  background: linear-gradient(90deg, #409eff, #67c23a);
  border-radius: 2px;
  margin-top: 8px;
}

.search-form {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f8fafc;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.action-btn {
  transition: all 0.3s;
  font-weight: 500;
}

.action-btn:hover {
  transform: translateY(-2px);
}

.add-btn {
  background: linear-gradient(135deg, #67c23a, #85ce61);
  border: none;
}

.data-table {
  margin-top: 20px;
  transition: all 0.3s;
}

.data-table:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.timestamp {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #606266;
}

.timestamp i {
  color: #909399;
}

.delete-btn {
  transition: all 0.3s;
}

.delete-btn:hover {
  transform: scale(1.1);
  background-color: #f56c6c;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  padding: 15px 0;
}

.custom-dialog {
  border-radius: 12px;
}

.confirm-btn {
  background: linear-gradient(135deg, #409eff, #79bbff);
  border: none;
}

.cancel-btn {
  transition: all 0.3s;
}

.cancel-btn:hover {
  color: #409eff;
  border-color: #c6e2ff;
  background-color: #ecf5ff;
}

/* 动画效果 */
.fade-in-item {
  animation: fadeIn 0.5s ease-out forwards;
  opacity: 0;
}

@keyframes fadeIn {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 为每个项目添加不同的延迟 */
.fade-in-item:nth-child(1) { animation-delay: 0.1s; }
.fade-in-item:nth-child(2) { animation-delay: 0.2s; }
.fade-in-item:nth-child(3) { animation-delay: 0.3s; }
.fade-in-item:nth-child(4) { animation-delay: 0.4s; }

/* 响应式调整 */
@media (max-width: 768px) {
  .search-form {
    padding: 15px;
  }

  .action-btn {
    margin-bottom: 10px;
  }
}
</style>