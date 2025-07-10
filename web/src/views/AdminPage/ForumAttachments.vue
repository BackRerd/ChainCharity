<template>
  <div class="forum-attachments">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <h2>论坛附件管理</h2>
          <el-button type="primary" @click="showUploadDialog = true">
            <el-icon><plus /></el-icon>上传附件
          </el-button>
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :model="searchForm" inline>
        <el-form-item label="帖子ID">
          <el-input v-model="searchForm.postId" placeholder="请输入帖子ID" clearable />
        </el-form-item>
        <el-form-item label="回复ID">
          <el-input v-model="searchForm.replyId" placeholder="请输入回复ID" clearable />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userId" placeholder="请输入用户ID" clearable />
        </el-form-item>
        <el-form-item label="文件类型">
          <el-select v-model="searchForm.fileType" placeholder="请选择文件类型" clearable>
            <el-option label="图片" value="image" />
            <el-option label="文档" value="document" />
            <el-option label="压缩包" value="archive" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchAttachments">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 附件表格 -->
      <el-table :data="attachments" v-loading="loading" border stripe>
        <el-table-column prop="attachmentId" label="ID" width="80" />
        <el-table-column prop="fileName" label="文件名" min-width="150">
          <template #default="{ row }">
            <div class="file-name-cell">
              <el-icon v-if="row.fileType === 'image'">
                <picture />
              </el-icon>
              <el-icon v-else>
                <document />
              </el-icon>
              <span>{{ row.fileName }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="fileType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getFileTypeTagType(row.fileType)">
              {{ row.fileType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fileSize" label="大小" width="100">
          <template #default="{ row }">
            {{ formatFileSize(row.fileSize) }}
          </template>
        </el-table-column>
        <el-table-column prop="downloadCount" label="下载次数" width="100" />
        <el-table-column prop="createdAt" label="上传时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
                v-if="row.fileType === 'image'"
                type="primary"
                size="small"
                @click="previewImage(row)"
            >
              预览
            </el-button>
            <el-button
                type="success"
                size="small"
                @click="downloadFile(row)"
            >
              下载
            </el-button>
            <el-button
                type="danger"
                size="small"
                @click="deleteAttachment(row.attachmentId)"
            >
              删除
            </el-button>
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

    <!-- 上传对话框 -->
    <el-dialog v-model="showUploadDialog" title="上传附件" width="50%">
      <file-uploader
          type="file"
          @upload-success="handleUploadSuccess"
          @upload-error="handleUploadError"
      />
      <template #footer>
        <el-button @click="showUploadDialog = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 图片预览对话框 -->
    <el-dialog v-model="showImagePreview" title="图片预览">
      <img :src="previewImageUrl" alt="预览" class="preview-image" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Picture, Document, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'
import FileUploader from '@/components/FileUploader.vue'

// 搜索表单
const searchForm = ref({
  postId: null,
  replyId: null,
  userId: null,
  fileType: null
})

// 分页数据
const pagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 附件列表
const attachments = ref([])
const loading = ref(false)

// 对话框控制
const showUploadDialog = ref(false)
const showImagePreview = ref(false)
const previewImageUrl = ref('')

// 获取附件列表
const fetchAttachments = async () => {
  try {
    loading.value = true

    const params = {
      pageNum: pagination.value.pageNum,
      pageSize: pagination.value.pageSize,
      ...searchForm.value
    }

    // 清理空值
    Object.keys(params).forEach(key => {
      if (params[key] === null || params[key] === '') {
        delete params[key]
      }
    })

    const response = await axios.get('/forum/attachments/list', { params })
    attachments.value = response.data.data.records
    pagination.value.total = response.data.data.total
  } catch (error) {
    console.error('获取附件列表失败:', error)
    ElMessage.error('获取附件列表失败')
  } finally {
    loading.value = false
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    postId: null,
    replyId: null,
    userId: null,
    fileType: null
  }
  fetchAttachments()
}

// 预览图片
const previewImage = (row) => {
  previewImageUrl.value = `/api/images/${row.userId}/${row.filePath}`
  showImagePreview.value = true
}

// 下载文件
const downloadFile = (row) => {
  const downloadUrl = `/api/files/${row.userId}/${row.filePath}`
  const link = document.createElement('a')
  link.href = downloadUrl
  link.download = row.fileName
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)

  // 更新下载次数
  updateDownloadCount(row.attachmentId)
}

// 更新下载次数
const updateDownloadCount = async (id) => {
  try {
    await axios.put(`/forum/attachments/${id}`, {
      downloadCount: 1 // 这里应该从服务器获取当前值并+1，简化处理
    })
    // 可以重新获取列表或直接更新本地数据
  } catch (error) {
    console.error('更新下载次数失败:', error)
  }
}

// 删除附件
const deleteAttachment = (id) => {
  ElMessageBox.confirm('确定要删除这个附件吗?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/forum/attachments/${id}`)
      ElMessage.success('删除成功')
      fetchAttachments()
    } catch (error) {
      console.error('删除附件失败:', error)
      ElMessage.error('删除附件失败')
    }
  }).catch(() => {})
}

// 上传成功处理
const handleUploadSuccess = () => {
  showUploadDialog.value = false
  fetchAttachments()
  ElMessage.success('上传成功')
}

// 上传错误处理
const handleUploadError = (error) => {
  console.error('上传失败:', error)
  ElMessage.error('上传失败')
}

// 格式化文件大小
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 格式化日期时间
const formatDateTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString()
}

// 获取文件类型标签样式
const getFileTypeTagType = (fileType) => {
  switch (fileType) {
    case 'image': return 'success'
    case 'document': return ''
    case 'archive': return 'warning'
    default: return 'info'
  }
}

// 初始化加载数据
onMounted(() => {
  fetchAttachments()
})
</script>

<style scoped>
.forum-attachments {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.file-name-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.preview-image {
  max-width: 100%;
  max-height: 70vh;
  display: block;
  margin: 0 auto;
}
</style>