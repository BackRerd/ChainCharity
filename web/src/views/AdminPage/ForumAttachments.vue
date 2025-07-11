<template>
  <div class="forum-attachments-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>论坛附件管理</span>
          <el-button type="primary" @click="handleCreate">添加附件</el-button>
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :model="searchForm" inline>
        <el-form-item label="帖子ID">
          <el-input-number v-model="searchForm.postId" :min="1" controls-position="right" />
        </el-form-item>
        <el-form-item label="回复ID">
          <el-input-number v-model="searchForm.replyId" :min="1" controls-position="right" />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input-number v-model="searchForm.userId" :min="1" controls-position="right" />
        </el-form-item>
        <el-form-item label="文件类型">
          <el-select v-model="searchForm.fileType" clearable placeholder="选择文件类型">
            <el-option label="图片" value="image" />
            <el-option label="文档" value="document" />
            <el-option label="视频" value="video" />
            <el-option label="音频" value="audio" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchAttachments">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table
          :data="attachments"
          v-loading="loading"
          border
          stripe
          highlight-current-row
          @sort-change="handleSortChange"
      >
        <el-table-column prop="attachmentId" label="ID" width="80" sortable="custom" />
        <el-table-column prop="postId" label="帖子ID" width="100" sortable="custom" />
        <el-table-column prop="replyId" label="回复ID" width="100" sortable="custom" />
        <el-table-column prop="userId" label="用户ID" width="100" sortable="custom" />
        <el-table-column prop="fileName" label="文件名" show-overflow-tooltip />
        <el-table-column label="文件类型" width="120" sortable="custom">
          <template #default="{ row }">
            <el-tag :type="getFileTypeTag(row.fileType)">{{ getFileTypeName(row.fileType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fileSize" label="大小" width="120" sortable="custom">
          <template #default="{ row }">
            {{ formatFileSize(row.fileSize) }}
          </template>
        </el-table-column>
        <el-table-column prop="downloadCount" label="下载次数" width="100" sortable="custom" />
        <el-table-column prop="createdAt" label="创建时间" width="180" sortable="custom">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleDownload(row)">下载</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.attachmentId)">删除</el-button>
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
            @size-change="fetchAttachments"
            @current-change="fetchAttachments"
        />
      </div>
    </el-card>

    <!-- 对话框表单 -->
    <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="50%"
        :close-on-click-modal="false"
        @closed="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="帖子ID" prop="postId">
          <el-input-number v-model="form.postId" :min="1" controls-position="right" />
        </el-form-item>
        <el-form-item label="回复ID" prop="replyId">
          <el-input-number v-model="form.replyId" :min="1" controls-position="right" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input-number v-model="form.userId" :min="1" controls-position="right" />
        </el-form-item>
        <el-form-item label="文件" prop="file">
          <el-upload
              ref="uploadRef"
              class="upload-demo"
              :auto-upload="false"
              :limit="1"
              :on-change="handleFileChange"
              :on-remove="handleFileRemove"
              :before-upload="beforeUpload"
          >
            <el-button type="primary">选择文件</el-button>
            <template #tip>
              <div class="el-upload__tip">
                请上传文件（最大10MB），支持图片、文档、视频、音频等格式
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitting">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'
import fileUploader from '@/utils/fileUploader.js'

// API基础URL
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const uploadRef = ref(null)
const currentFile = ref(null)
const sortParams = reactive({
  sortField: null,
  sortOrder: null
})


// 表单数据
const form = reactive({
  attachmentId: null,
  postId: null,
  replyId: null,
  userId: null,
  fileName: '',
  filePath: '',
  fileSize: 0,
  fileType: '',
  downloadCount: 0,
  createdAt: null
})

// 搜索表单
const searchForm = reactive({
  postId: null,
  replyId: null,
  userId: null,
  fileType: null
})

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 表格数据
const attachments = ref([])

// 验证规则
const rules = reactive({
  postId: [{ required: true, message: '请输入帖子ID', trigger: 'blur' }],
  userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
  file: [{ required: true, message: '请选择文件', trigger: 'change' }]
})

// 获取附件列表
const fetchAttachments = async () => {
  try {
    loading.value = true
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm,
      ...sortParams
    }

    // 移除空值
    Object.keys(params).forEach(key => {
      if (params[key] === null || params[key] === undefined || params[key] === '') {
        delete params[key]
      }
    })

    const response = await axios.get(`${API_BASE_URL}/forum/attachments/list`, { params })
    attachments.value = response.data.data.records
    pagination.total = response.data.data.total
  } catch (error) {
    ElMessage.error('获取附件列表失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

// 处理排序变化
const handleSortChange = ({ prop, order }) => {
  sortParams.sortField = prop
  sortParams.sortOrder = order === 'ascending' ? 'asc' : order === 'descending' ? 'desc' : null
  fetchAttachments()
}

// 文件上传前检查
const beforeUpload = (file) => {
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过10MB')
    return false
  }
  return true
}

// 处理文件变化
const handleFileChange = (file) => {
  currentFile.value = file.raw
}

// 处理文件移除
const handleFileRemove = () => {
  currentFile.value = null
}

// 创建附件
const handleCreate = () => {
  dialogTitle.value = '添加附件'
  dialogVisible.value = true
}

// 删除附件
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除此附件吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await axios.delete(`${API_BASE_URL}/forum/attachments/${id}`)
    ElMessage.success('附件删除成功')
    fetchAttachments()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除附件失败: ' + (error.response?.data?.message || error.message))
    }
  }
}

// 下载附件
const handleDownload = async (row) => {
  try {
    // 实现下载逻辑
    await fileUploader.downloadFile(row.fileName, row.userId)

    // 更新下载次数
    await axios.put(`${API_BASE_URL}/forum/attachments/${row.attachmentId}`, {
      ...row,
      downloadCount: row.downloadCount + 1
    })

    ElMessage.success('文件下载成功')
    fetchAttachments()
  } catch (error) {
    ElMessage.error('文件下载失败: ' + (error.response?.data?.message || error.message))
  }
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate()

    if (!currentFile.value) {
      ElMessage.error('请选择文件')
      return
    }

    submitting.value = true

    // 首先上传文件
    let fileName, filePath, fileType, fileSize

    if (currentFile.value.type.startsWith('image/')) {
      filePath = await fileUploader.uploadImage(currentFile.value, { userId: form.userId })
      fileType = 'image'
    } else {
      fileName = await fileUploader.uploadFile(currentFile.value, { userId: form.userId })
      filePath = fileUploader.getImageUrl(fileName, form.userId)
      fileType = currentFile.value.type.split('/')[0] || 'other'
    }

    fileSize = currentFile.value.size

    // 然后创建/更新附件记录
    const attachmentData = {
      ...form,
      fileName: currentFile.value.name,
      filePath,
      fileSize,
      fileType
    }

    if (form.attachmentId) {
      await axios.put(`${API_BASE_URL}/forum/attachments/${form.attachmentId}`, attachmentData)
      ElMessage.success('附件更新成功')
    } else {
      await axios.post(`${API_BASE_URL}/forum/attachments`, attachmentData)
      ElMessage.success('附件添加成功')
    }

    dialogVisible.value = false
    fetchAttachments()
  } catch (error) {
    ElMessage.error('操作失败: ' + (error.response?.data?.message || error.message))
  } finally {
    submitting.value = false
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.postId = null
  searchForm.replyId = null
  searchForm.userId = null
  searchForm.fileType = null
  fetchAttachments()
}

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields()
  uploadRef.value?.clearFiles()
  currentFile.value = null
  Object.assign(form, {
    attachmentId: null,
    postId: null,
    replyId: null,
    userId: null,
    fileName: '',
    filePath: '',
    fileSize: 0,
    fileType: '',
    downloadCount: 0,
    createdAt: null
  })
}

// 辅助函数
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

const formatFileSize = (bytes) => {
  if (!bytes) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2) + ' ' + sizes[i])
}

const getFileTypeTag = (type) => {
  const typeMap = {
    image: 'success',
    document: '',
    video: 'warning',
    audio: 'info',
    other: 'danger'
  }
  return typeMap[type] || ''
}

const getFileTypeName = (type) => {
  const typeMap = {
    image: '图片',
    document: '文档',
    video: '视频',
    audio: '音频',
    other: '其他'
  }
  return typeMap[type] || type
}

// 生命周期钩子
onMounted(() => {
  fetchAttachments()
})
</script>

<style scoped>
.forum-attachments-container {
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

.el-upload__tip {
  margin-top: 7px;
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.upload-demo {
  width: 100%;
}

:deep(.el-upload) {
  width: 100%;
}

:deep(.el-upload-list) {
  width: 100%;
}
</style>