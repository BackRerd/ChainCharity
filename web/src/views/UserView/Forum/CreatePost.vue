<template>
  <div class="create-post">
    <el-page-header :icon="null" title="返回" @back="router.go(-1)">
      <template #content>
        <span class="page-header-title">发表新帖</span>
      </template>
    </el-page-header>

    <div class="post-form">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <!-- 标题 -->
        <el-form-item label="标题" prop="title">
          <el-input
              v-model="form.title"
              placeholder="请输入帖子标题"
              maxlength="100"
              show-word-limit
          />
        </el-form-item>

        <!-- 分类选择 -->
        <el-form-item label="分类" prop="categoryId">
          <el-select
              v-model="form.categoryId"
              placeholder="请选择分类"
              style="width: 100%"
              clearable
          >
            <el-option
                v-for="category in categories"
                :key="category.categoryId"
                :label="category.name"
                :value="category.categoryId"
            />
          </el-select>
        </el-form-item>

        <!-- 编辑器 -->
        <el-form-item label="内容" prop="content">
          <div class="editor-container">
            <div class="editor-toolbar">
              <el-button-group>
                <el-tooltip content="粗体" placement="top">
                  <el-button :icon="Bold" @click="insertText('**', '**')" />
                </el-tooltip>
                <el-tooltip content="斜体" placement="top">
                  <el-button :icon="Italic" @click="insertText('*', '*')" />
                </el-tooltip>
                <el-tooltip content="链接" placement="top">
                  <el-button :icon="Link" @click="insertText('[', '](url)')" />
                </el-tooltip>
                <el-tooltip content="代码" placement="top">
                  <el-button :icon="Code" @click="insertText('```\n', '\n```')" />
                </el-tooltip>
                <el-tooltip content="引用" placement="top">
                  <el-button :icon="Quote" @click="insertText('> ', '')" />
                </el-tooltip>
                <el-tooltip content="无序列表" placement="top">
                  <el-button :icon="List" @click="insertText('- ', '')" />
                </el-tooltip>
                <el-tooltip content="有序列表" placement="top">
                  <el-button :icon="Finished" @click="insertText('1. ', '')" />
                </el-tooltip>
              </el-button-group>

              <el-upload
                  action=""
                  :show-file-list="false"
                  :before-upload="beforeImageUpload"
                  :http-request="handleImageUpload"
              >
                <el-tooltip content="插入图片" placement="top">
                  <el-button :icon="Picture" />
                </el-tooltip>
              </el-upload>

              <el-upload
                  action=""
                  :show-file-list="false"
                  :before-upload="beforeFileUpload"
                  :http-request="handleFileUpload"
                  multiple
              >
                <el-tooltip content="添加附件" placement="top">
                  <el-button :icon="DocumentAdd" />
                </el-tooltip>
              </el-upload>
            </div>

            <el-input
                v-model="form.content"
                type="textarea"
                :rows="10"
                placeholder="请输入帖子内容 (支持Markdown语法)"
                resize="none"
                class="editor-textarea"
            />

            <div class="editor-preview">
              <h4>预览:</h4>
              <div class="markdown-content" v-html="renderedContent"></div>
            </div>
          </div>
        </el-form-item>

        <!-- 附件列表 -->
        <el-form-item label="附件" v-if="attachments.length > 0">
          <div class="attachment-list">
            <div v-for="file in attachments" :key="file.id" class="attachment-item">
              <div class="file-info">
                <el-icon><Document /></el-icon>
                <span class="file-name">{{ file.fileName }}</span>
                <span class="file-size">({{ formatFileSize(file.fileSize) }})</span>
              </div>
              <el-button
                  type="danger"
                  :icon="Delete"
                  circle
                  size="small"
                  @click="removeAttachment(file)"
              />
            </div>
          </div>
        </el-form-item>

        <!-- 帖子选项 -->
        <el-form-item label="帖子选项">
          <el-checkbox v-model="form.isSticky">置顶</el-checkbox>
          <el-checkbox v-model="form.isEssence">精华</el-checkbox>
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="submitting">
            发表帖子
          </el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button @click="saveDraft" v-if="!form.postId">保存草稿</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {ref, computed, onMounted, nextTick} from 'vue'
import { useRouter } from 'vue-router'
import {
  Edit as Bold,
  // 替换 Italic → 可以用 'Help' 或 'Warning'
  Warning as Italic,
  // 替换 Code → 可以用 'Cpu' 或 'MagicStick'
  MagicStick as Code,
  // 替换 Quote → 可以用 'ChatDotRound' 或 'Comment'
  ChatDotRound as Quote,
  Link,
  List,
  Finished,
  Picture,
  DocumentAdd,
  Document,
  Delete
} from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import MarkdownIt from 'markdown-it'
import fileUploader from '@/utils/fileUploader'

const router = useRouter()

// Markdown渲染器
const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true
})
const user = computed(() => {
  const userData = localStorage.getItem('user')
  return userData ? JSON.parse(userData) : null
})
// 表单数据
const form = ref({
  title: '',
  content: '',
  categoryId: null,
  isSticky: false,
  isEssence: false
})

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入帖子标题', trigger: 'blur' },
    { min: 5, max: 100, message: '长度在 5 到 100 个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入帖子内容', trigger: 'blur' },
    { min: 10, message: '内容至少需要 10 个字符', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ]
}

// 分类列表
const categories = ref([])
// 附件列表
const attachments = ref([])
// 表单引用
const formRef = ref(null)
// 提交状态
const submitting = ref(false)

// 渲染Markdown预览
const renderedContent = computed(() => {
  return md.render(form.value.content || '')
})

// 格式化文件大小
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 获取分类列表
const fetchCategories = async () => {
  try {
    const response = await axios.get('/forum/categories/page', {
      params: {
        pageSize: 100,
        isActive: true
      }
    })
    categories.value = response.data.data.records || []
  } catch (error) {
    ElMessage.error('获取分类列表失败: ' + (error.response?.data?.message || error.message))
  }
}

// 插入文本到编辑器
const insertText = (prefix, suffix) => {
  const textarea = document.querySelector('.editor-textarea textarea')
  if (!textarea) return

  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const selectedText = form.value.content.substring(start, end)

  form.value.content =
      form.value.content.substring(0, start) +
      prefix + selectedText + suffix +
      form.value.content.substring(end)

  // 重新聚焦并设置光标位置
  nextTick(() => {
    textarea.focus()
    const newCursorPos = start + prefix.length
    textarea.setSelectionRange(newCursorPos, newCursorPos + selectedText.length)
  })
}

// 图片上传前验证
const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2MB!')
    return false
  }
  return true
}

// 文件上传前验证
const beforeFileUpload = (file) => {
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过10MB!')
    return false
  }
  return true
}

// 处理图片上传
const handleImageUpload = async (options) => {
  try {
    const file = options.file
    const imageUrl = await fileUploader.uploadImage(file)

    // 插入Markdown图片语法
    const imgMarkdown = `![${file.name}](${imageUrl})`
    const cursorPos = form.value.content.length
    form.value.content =
        form.value.content.slice(0, cursorPos) +
        imgMarkdown +
        form.value.content.slice(cursorPos)

    ElMessage.success('图片上传成功')
  } catch (error) {
    ElMessage.error('图片上传失败: ' + (error.message || '未知错误'))
  }
}

// 处理文件上传
const handleFileUpload = async (options) => {
  try {
    const file = options.file
    const fileName = await fileUploader.uploadFile(file)

    // 添加到附件列表
    attachments.value.push({
      id: Date.now().toString(),
      fileName: file.name,
      filePath: fileName,
      fileSize: file.size,
      fileType: file.type
    })

    ElMessage.success('附件上传成功')
  } catch (error) {
    ElMessage.error('附件上传失败: ' + (error.message || '未知错误'))
  }
}

// 移除附件
const removeAttachment = (file) => {
  attachments.value = attachments.value.filter(f => f.id !== file.id)
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true

    // 创建帖子
    const postData = {
      title: form.value.title,
      userId: user.value.userId,
      content: form.value.content,
      categoryId: form.value.categoryId,
      viewCount: 0,
      replyCount: 0,
      lastReplyAt: new Date(),
      isClosed: 0,
      isSticky: form.value.isSticky ? 1 : 0,
      isEssence: form.value.isEssence ? 1 : 0
    }

    const response = await axios.post('/forum/posts', postData)
    console.log(response)
    const postId = response.data.data.postId

    // 上传附件
    if (attachments.value.length > 0) {
      await Promise.all(
          attachments.value.map(file => {
            return axios.post('/forum/attachments', {
              postId,
              fileName: file.fileName,
              filePath: file.filePath,
              fileSize: file.fileSize,
              fileType: file.fileType
            })
          })
      )
    }

    ElMessage.success('帖子发表成功')
    await router.push({name: 'PostDetail', params: {id: postId}})
  } catch (error) {
    if (error.name !== 'ValidateError') {
      console.log(error)
      ElMessage.error('发表失败: ' + (error.response?.data?.message || error.message))
    }
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  formRef.value.resetFields()
  attachments.value = []
}

// 保存草稿
const saveDraft = () => {
  if (!form.value.title && !form.value.content) {
    ElMessage.warning('没有内容可保存')
    return
  }

  const drafts = JSON.parse(localStorage.getItem('postDrafts') || [])
  drafts.push({
    ...form.value,
    attachments: [...attachments.value],
    createdAt: new Date().toISOString()
  })

  localStorage.setItem('postDrafts', JSON.stringify(drafts))
  ElMessage.success('草稿保存成功')
  resetForm()
}

// 初始化数据
onMounted(() => {
  fetchCategories()
})
</script>

<style scoped>
.create-post {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.page-header-title {
  font-size: 18px;
  font-weight: 500;
}

.post-form {
  margin-top: 20px;
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.editor-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.editor-toolbar {
  padding: 5px 10px;
  border-bottom: 1px solid #dcdfe6;
  display: flex;
  justify-content: space-between;
  background-color: #f5f7fa;
}

.editor-textarea {
  border: none;
}

.editor-textarea :deep(.el-textarea__inner) {
  border: none;
  padding: 15px;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', Arial, sans-serif;
  line-height: 1.6;
}

.editor-preview {
  padding: 15px;
  border-top: 1px dashed #eee;
}

.markdown-content {
  word-break: break-word;
}

.markdown-content :deep(h1),
.markdown-content :deep(h2),
.markdown-content :deep(h3) {
  margin-top: 1em;
  margin-bottom: 0.5em;
}

.markdown-content :deep(p) {
  margin: 0.8em 0;
}

.markdown-content :deep(img) {
  max-width: 100%;
  height: auto;
}

.attachment-list {
  width: 100%;
}

.attachment-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px dashed #eee;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.file-name {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.file-size {
  color: #999;
  font-size: 12px;
}
</style>