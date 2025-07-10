<template>
  <div class="file-uploader">
    <!-- 图片上传 -->
    <div v-if="type === 'image'" class="upload-section">
      <el-upload
          action="#"
          :auto-upload="false"
          :show-file-list="false"
          :on-change="handleImageChange"
          :before-upload="beforeImageUpload"
          accept="image/*"
          drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
          将图片拖到此处，或<em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            只能上传 JPG/PNG 格式图片，且不超过 5MB
          </div>
        </template>
      </el-upload>

      <div v-if="imageUrl" class="image-preview">
        <img :src="imageUrl" alt="预览" class="preview-image" />
        <div class="image-actions">
          <el-button type="danger" size="small" @click="removeImage">
            <el-icon><delete /></el-icon>删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 文件上传 -->
    <div v-else class="upload-section">
      <el-upload
          action="#"
          :auto-upload="false"
          :show-file-list="true"
          :on-change="handleFileChange"
          :before-upload="beforeFileUpload"
          :on-remove="handleFileRemove"
          drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
          将文件拖到此处，或<em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            支持上传各种类型文件，单个文件不超过 50MB
          </div>
        </template>
      </el-upload>
    </div>

    <!-- 上传进度 -->
    <el-progress
        v-if="uploading"
        :percentage="progressPercent"
        :status="uploadStatus"
    />

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button
          type="primary"
          :loading="uploading"
          @click="submitUpload"
      >
        {{ uploading ? '上传中...' : '开始上传' }}
      </el-button>
      <el-button @click="cancelUpload">取消</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { UploadFilled, Delete } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { uploadImage, uploadFile } from '@/utils/upload'

const props = defineProps({
  // 上传类型: image 或 file
  type: {
    type: String,
    default: 'file',
    validator: (value) => ['image', 'file'].includes(value)
  },
  // 用户ID (可选)
  userId: {
    type: String,
    default: null
  },
  // 初始图片URL (仅当 type=image 时有效)
  initialImageUrl: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['upload-success', 'upload-error', 'update:initialImageUrl'])

// 图片上传相关
const imageFile = ref(null)
const imageUrl = ref(props.initialImageUrl)

// 文件上传相关
const fileList = ref([])

// 上传状态
const uploading = ref(false)
const progressPercent = ref(0)
const uploadStatus = ref('')

// 监听初始图片URL变化
watch(() => props.initialImageUrl, (newVal) => {
  imageUrl.value = newVal
})

// 图片选择处理
const handleImageChange = (file) => {
  const isImage = file.raw.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }

  imageFile.value = file.raw
  imageUrl.value = URL.createObjectURL(file.raw)
  return true
}

// 图片上传前验证
const beforeImageUpload = (file) => {
  const isJPGorPNG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isJPGorPNG) {
    ElMessage.error('上传图片只能是 JPG/PNG 格式!')
  }
  if (!isLt5M) {
    ElMessage.error('上传图片大小不能超过 5MB!')
  }

  return isJPGorPNG && isLt5M
}

// 文件选择处理
const handleFileChange = (file, files) => {
  fileList.value = files
}

// 文件移除处理
const handleFileRemove = (file, files) => {
  fileList.value = files
}

// 文件上传前验证
const beforeFileUpload = (file) => {
  const isLt50M = file.size / 1024 / 1024 < 50
  if (!isLt50M) {
    ElMessage.error('上传文件大小不能超过 50MB!')
  }
  return isLt50M
}

// 删除图片
const removeImage = () => {
  imageFile.value = null
  imageUrl.value = ''
  emit('update:initialImageUrl', '')
}

// 提交上传
const submitUpload = async () => {
  if (props.type === 'image') {
    if (!imageFile.value) {
      ElMessage.warning('请选择要上传的图片')
      return
    }
    await uploadImageFile()
  } else {
    if (fileList.value.length === 0) {
      ElMessage.warning('请选择要上传的文件')
      return
    }
    await uploadFiles()
  }
}

// 上传图片
const uploadImageFile = async () => {
  uploading.value = true
  progressPercent.value = 0
  uploadStatus.value = ''

  try {
    // 模拟进度
    const timer = setInterval(() => {
      if (progressPercent.value < 90) {
        progressPercent.value += 10
      }
    }, 200)

    const url = await uploadImage(imageFile.value, props.userId)

    clearInterval(timer)
    progressPercent.value = 100
    uploadStatus.value = 'success'

    imageUrl.value = url
    emit('upload-success', url)
    emit('update:initialImageUrl', url)
    ElMessage.success('图片上传成功')
  } catch (error) {
    uploadStatus.value = 'exception'
    emit('upload-error', error)
    ElMessage.error('图片上传失败')
  } finally {
    uploading.value = false
  }
}

// 上传文件
const uploadFiles = async () => {
  uploading.value = true
  progressPercent.value = 0
  uploadStatus.value = ''

  try {
    const results = []
    const totalFiles = fileList.value.length

    for (let i = 0; i < totalFiles; i++) {
      const file = fileList.value[i].raw

      // 更新进度
      progressPercent.value = Math.floor((i / totalFiles) * 100)

      try {
        const result = await uploadFile(file, props.userId)
        results.push({
          fileName: file.name,
          result: result,
          success: true
        })
      } catch (error) {
        results.push({
          fileName: file.name,
          error: error.message,
          success: false
        })
      }
    }

    progressPercent.value = 100
    uploadStatus.value = 'success'

    emit('upload-success', results)
    ElMessage.success(`文件上传完成 (成功 ${results.filter(r => r.success).length}/${totalFiles} 个)`)
  } catch (error) {
    uploadStatus.value = 'exception'
    emit('upload-error', error)
    ElMessage.error('文件上传过程中出错')
  } finally {
    uploading.value = false
  }
}

// 取消上传
const cancelUpload = () => {
  // 这里可以添加取消上传的逻辑
  uploading.value = false
  progressPercent.value = 0
  uploadStatus.value = ''
  emit('upload-error', new Error('用户取消上传'))
  ElMessage.warning('上传已取消')
}
</script>

<style scoped>
.file-uploader {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
}

.upload-section {
  margin-bottom: 20px;
}

.image-preview {
  margin-top: 20px;
  text-align: center;
}

.preview-image {
  max-width: 100%;
  max-height: 300px;
  display: block;
  margin: 0 auto 10px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.image-actions {
  margin-top: 10px;
}

.action-buttons {
  margin-top: 20px;
  text-align: right;
}

:deep(.el-upload) {
  width: 100%;
}

:deep(.el-upload-dragger) {
  width: 100%;
  padding: 20px;
}
</style>