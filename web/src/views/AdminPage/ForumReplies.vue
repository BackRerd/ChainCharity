<template>
  <div class="forum-replies-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>论坛回复管理</span>
          <el-button type="primary" @click="handleCreate">新增回复</el-button>
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :model="searchForm" inline>
        <el-form-item label="帖子ID">
          <el-input-number v-model="searchForm.postId" :min="1" clearable />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input-number v-model="searchForm.userId" :min="1" clearable />
        </el-form-item>
        <el-form-item label="是否首楼">
          <el-select v-model="searchForm.isFirst" clearable placeholder="请选择">
            <el-option label="是" :value="true" />
            <el-option label="否" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-input v-model="searchForm.status" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="replyId" label="回复ID" width="80" />
        <el-table-column prop="postId" label="帖子ID" width="80" />
        <el-table-column prop="userId" label="用户ID" width="80" />
        <el-table-column prop="content" label="内容" show-overflow-tooltip />
        <el-table-column prop="likeCount" label="点赞数" width="90" />
        <el-table-column prop="isFirst" label="是否首楼" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isFirst ? 'success' : 'info'">
              {{ row.isFirst ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120" />
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="updatedAt" label="更新时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.updatedAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.replyId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
            v-model:current-page="pagination.pageNum"
            v-model:page-size="pagination.pageSize"
            :total="pagination.total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="50%">
      <el-form :model="formData" label-width="100px" :rules="rules" ref="formRef">
        <el-form-item label="帖子ID" prop="postId">
          <el-input-number v-model="formData.postId" :min="1" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input-number v-model="formData.userId" :min="1" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="formData.content" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="点赞数" prop="likeCount">
          <el-input-number v-model="formData.likeCount" :min="0" />
        </el-form-item>
        <el-form-item label="是否首楼" prop="isFirst">
          <el-switch v-model="formData.isFirst" active-text="是" inactive-text="否" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-input v-model="formData.status" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const API_URL = '/forum/replies'

// 表格数据
const tableData = ref([])
const loading = ref(false)

// 搜索表单
const searchForm = reactive({
  postId: null,
  userId: null,
  isFirst: null,
  status: ''
})

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formData = reactive({
  replyId: null,
  postId: null,
  userId: null,
  content: '',
  likeCount: 0,
  isFirst: false,
  status: ''
})
const formRef = ref(null)

// 表单验证规则
const rules = {
  postId: [{ required: true, message: '请输入帖子ID', trigger: 'blur' }],
  userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
  content: [{ required: true, message: '请输入回复内容', trigger: 'blur' }]
}

// 初始化加载数据
onMounted(() => {
  fetchData()
})

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    // 移除值为null或空的参数
    Object.keys(params).forEach(key => {
      if (params[key] === null || params[key] === '') {
        delete params[key]
      }
    })

    const response = await axios.get(`${API_URL}/page`, { params })
    if (response.data && response.data.data) {
      tableData.value = response.data.data.records
      pagination.total = response.data.data.total
    }
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
  searchForm.postId = null
  searchForm.userId = null
  searchForm.isFirst = null
  searchForm.status = ''
  handleSearch()
}

// 分页大小改变
const handleSizeChange = (size) => {
  pagination.pageSize = size
  fetchData()
}

// 当前页改变
const handleCurrentChange = (page) => {
  pagination.pageNum = page
  fetchData()
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString()
}

// 新增
const handleCreate = () => {
  dialogTitle.value = '新增回复'
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑回复'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  formData.replyId = null
  formData.postId = null
  formData.userId = null
  formData.content = ''
  formData.likeCount = 0
  formData.isFirst = false
  formData.status = ''
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate()

    if (formData.replyId) {
      // 更新
      await axios.put(`${API_URL}/${formData.replyId}`, formData)
      ElMessage.success('更新成功')
    } else {
      // 新增
      await axios.post(API_URL, formData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error('提交失败:', error)
    if (error.response) {
      ElMessage.error(`提交失败: ${error.response.data.message || '服务器错误'}`)
    } else if (error.message) {
      ElMessage.error(`提交失败: ${error.message}`)
    }
  }
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除这条回复吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`${API_URL}/${id}`)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 用户取消
  })
}
</script>

<style scoped>
.forum-replies-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>