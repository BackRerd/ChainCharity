<template>
  <div class="edit-post">
    <el-page-header :icon="null" title="返回" @back="router.go(-1)">
      <template #content>
        <span class="page-header-title">编辑帖子</span>
      </template>
    </el-page-header>

    <div class="post-form">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
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
                  <el-button :icon="BoldIcon" @click="insertText('**', '**')" />
                </el-tooltip>
                <el-tooltip content="斜体" placement="top">
                  <el-button :icon="ItalicIcon" @click="insertText('*', '*')" />
                </el-tooltip>
                <el-tooltip content="链接" placement="top">
                  <el-button :icon="LinkIcon" @click="insertText('[', '](url)')" />
                </el-tooltip>
                <el-tooltip content="代码" placement="top">
                  <el-button :icon="CodeIcon" @click="insertText('```\n', '\n```')" />
                </el-tooltip>
                <el-tooltip content="引用" placement="top">
                  <el-button :icon="QuoteIcon" @click="insertText('> ', '')" />
                </el-tooltip>
                <el-tooltip content="无序列表" placement="top">
                  <el-button :icon="UnorderedListIcon" @click="insertText('- ', '')" />
                </el-tooltip>
                <el-tooltip content="有序列表" placement="top">
                  <el-button :icon="OrderedListIcon" @click="insertText('1. ', '')" />
                </el-tooltip>
                <el-tooltip content="标题" placement="top">
                  <el-button :icon="TitleIcon" @click="insertText('## ', '')" />
                </el-tooltip>
                <el-tooltip content="分割线" placement="top">
                  <el-button :icon="DivideIcon" @click="insertText('\n---\n', '')" />
                </el-tooltip>
              </el-button-group>

              <el-upload
                  action=""
                  :show-file-list="false"
                  :before-upload="beforeImageUpload"
                  :http-request="handleImageUpload"
              >
                <el-tooltip content="插入图片" placement="top">
                  <el-button :icon="PictureIcon" />
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
                  <el-button :icon="DocumentAddIcon" />
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
              <h4 class="preview-title">
                <el-icon><View /></el-icon>
                <span>预览</span>
              </h4>
              <div class="markdown-content" v-html="renderedContent"></div>
            </div>
          </div>
        </el-form-item>

        <!-- 附件列表 -->
        <el-form-item label="附件" v-if="attachments.length > 0">
          <div class="attachment-list">
            <div v-for="file in attachments" :key="file.attachmentId || file.id" class="attachment-item">
              <div class="file-info">
                <el-icon><DocumentIcon /></el-icon>
                <span class="file-name">{{ file.fileName }}</span>
                <span class="file-size">({{ formatFileSize(file.fileSize) }})</span>
              </div>
              <el-button
                  type="danger"
                  :icon="DeleteIcon"
                  circle
                  size="small"
                  @click="removeAttachment(file)"
              />
            </div>
          </div>
        </el-form-item>

        <!-- 帖子选项 -->
        <el-form-item label="帖子选项">
          <el-checkbox v-model="form.isSticky">
            <el-icon><Top /></el-icon>
            <span>置顶</span>
          </el-checkbox>
          <el-checkbox v-model="form.isEssence">
            <el-icon><Star /></el-icon>
            <span>精华</span>
          </el-checkbox>
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="submitting" class="submit-btn">
            <el-icon><Upload /></el-icon>
            <span>更新帖子</span>
          </el-button>
          <el-button @click="router.go(-1)" class="cancel-btn">
            <el-icon><Close /></el-icon>
            <span>取消</span>
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {ref, computed, onMounted, nextTick} from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  Top,
  Star,
  View,
  Upload,
  Close,
  Edit as BoldIcon,
  Warning as ItalicIcon,
  MagicStick as CodeIcon,
  ChatDotRound as QuoteIcon,
  Link as LinkIcon,
  List as UnorderedListIcon,
  Finished as OrderedListIcon,
  Picture as PictureIcon,
  DocumentAdd as DocumentAddIcon,
  Document as DocumentIcon,
  Delete as DeleteIcon,
  Minus as DivideIcon,  // 用减号代替分割线图标
  Operation as TitleIcon  // 用操作图标代替字体大小图标
} from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import MarkdownIt from 'markdown-it'
import fileUploader from '@/utils/fileUploader'

const route = useRoute()
const router = useRouter()

// Markdown渲染器
const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true
})

// 表单数据
const form = ref({
  postId: '',
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
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2) + ' ' + sizes[i])
}

// 获取帖子详情
const fetchPostDetail = async () => {
  try {
    const response = await axios.get(`/forum/posts/${route.params.id}`)
    form.value = {
      postId: response.data.data.postId,
      title: response.data.data.title,
      content: response.data.data.content,
      categoryId: response.data.data.categoryId,
      isSticky: response.data.data.isSticky,
      isEssence: response.data.data.isEssence
    }

    // 获取附件
    const attachmentRes = await axios.get('/forum/attachments/list', {
      params: { postId: route.params.id }
    })
    attachments.value = attachmentRes.data.list || []
  } catch (error) {
    ElMessage.error('获取帖子详情失败: ' + (error.response?.data?.message || error.message))
    await router.push({name: 'ForumHome'})
  }
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
const removeAttachment = async (file) => {
  try {
    if (file.attachmentId) {
      await axios.delete(`/forum/attachments/${file.attachmentId}`)
    }
    attachments.value = attachments.value.filter(f =>
        f.attachmentId ? f.attachmentId !== file.attachmentId : f.id !== file.id
    )
    ElMessage.success('附件删除成功')
  } catch (error) {
    ElMessage.error('附件删除失败: ' + (error.response?.data?.message || error.message))
  }
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true

    // 更新帖子
    const postData = {
      title: form.value.title,
      content: form.value.content,
      categoryId: form.value.categoryId,
      isSticky: form.value.isSticky,
      isEssence: form.value.isEssence
    }

    await axios.put(`/forum/posts/${form.value.postId}`, postData)

    // 上传新附件
    if (attachments.value.some(file => !file.attachmentId)) {
      await Promise.all(
          attachments.value
              .filter(file => !file.attachmentId)
              .map(file => {
                return axios.post('/forum/attachments', {
                  postId: form.value.postId,
                  fileName: file.fileName,
                  filePath: file.filePath,
                  fileSize: file.fileSize,
                  fileType: file.fileType
                })
              })
      )
    }

    ElMessage.success('帖子更新成功')
    await router.push({name: 'PostDetail', params: {id: form.value.postId}})
  } catch (error) {
    if (error.name !== 'ValidateError') {
      ElMessage.error('更新失败: ' + (error.response?.data?.message || error.message))
    }
  } finally {
    submitting.value = false
  }
}

// 初始化数据
onMounted(() => {
  fetchPostDetail()
  fetchCategories()
})
</script>

<style scoped lang="scss">
.edit-post {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f8fafc;
  min-height: 100vh;
}

.page-header-title {
  font-size: 18px;
  font-weight: 500;
  color: #2c3e50;
}

.post-form {
  margin-top: 20px;
  background-color: #fff;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  border: 1px solid #e6ebf5;
}

.editor-container {
  border: 1px solid #e6ebf5;
  border-radius: 6px;
  overflow: hidden;
}

.editor-toolbar {
  padding: 8px 12px;
  border-bottom: 1px solid #e6ebf5;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f8fafc;

  .el-button-group {
    display: flex;
    flex-wrap: wrap;
    gap: 5px;
  }

  .el-button {
    border-radius: 4px;
    color: #5a6a85;
    background-color: #fff;
    border-color: #e6ebf5;

    &:hover {
      color: #409eff;
      border-color: #c6e2ff;
      background-color: #ecf5ff;
    }
  }
}

.editor-textarea {
  border: none;
  font-size: 14px;
}

.editor-textarea :deep(.el-textarea__inner) {
  border: none;
  padding: 15px;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', Arial, sans-serif;
  line-height: 1.6;
  color: #34495e;
  background-color: #fff;
  transition: none;

  &:focus {
    box-shadow: none;
  }
}

.editor-preview {
  padding: 15px;
  border-top: 1px dashed #e6ebf5;
  background-color: #f8fafc;

  .preview-title {
    display: flex;
    align-items: center;
    gap: 8px;
    color: #5a6a85;
    margin-bottom: 12px;
    font-size: 15px;

    .el-icon {
      font-size: 16px;
    }
  }
}

.markdown-content {
  word-break: break-word;
  color: #34495e;
  line-height: 1.7;
}

.markdown-content :deep(h1),
.markdown-content :deep(h2),
.markdown-content :deep(h3) {
  margin-top: 1em;
  margin-bottom: 0.5em;
  color: #2c3e50;
}

.markdown-content :deep(p) {
  margin: 0.8em 0;
}

.markdown-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.markdown-content :deep(a) {
  color: #409eff;
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
}

.markdown-content :deep(code) {
  background-color: #f3f4f6;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: Consolas, Monaco, 'Andale Mono', monospace;
}

.markdown-content :deep(pre) {
  background-color: #f8fafc;
  padding: 12px;
  border-radius: 4px;
  overflow: auto;

  code {
    background-color: transparent;
    padding: 0;
  }
}

.attachment-list {
  width: 100%;
  border: 1px solid #e6ebf5;
  border-radius: 6px;
  padding: 10px;
  background-color: #f8fafc;
}

.attachment-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 10px;
  border-bottom: 1px dashed #e6ebf5;
  transition: background-color 0.2s;

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    background-color: #f0f7ff;
  }
}

.file-info {
  display: flex;
  align-items: center;
  gap: 10px;
  flex: 1;
  min-width: 0;

  .el-icon {
    color: #409eff;
    font-size: 18px;
  }
}

.file-name {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #34495e;
}

.file-size {
  color: #99a9bf;
  font-size: 12px;
  margin-left: 5px;
}

.el-form-item {
  margin-bottom: 22px;

  :deep(.el-form-item__label) {
    color: #5a6a85;
    font-weight: 500;
  }
}

.el-checkbox {
  margin-right: 20px;

  .el-icon {
    margin-right: 5px;
    color: #f7b731;
  }

  &.is-checked {
    .el-icon {
      color: #409eff;
    }
  }
}

.submit-btn {
  background-color: #409eff;
  border-color: #409eff;
  padding: 10px 20px;

  &:hover {
    background-color: #66b1ff;
    border-color: #66b1ff;
  }

  .el-icon {
    margin-right: 5px;
  }
}

.cancel-btn {
  padding: 10px 20px;

  .el-icon {
    margin-right: 5px;
  }
}
</style>