
<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ChatLineRound, EditPen, User } from '@element-plus/icons-vue'
import {
  Clock,
  View,
  Folder,
  Star,
  Edit,
  Delete,
  Document,
  Download,
  Picture,
  Promotion
} from '@element-plus/icons-vue'
import dayjs from 'dayjs'
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

// 用户信息
const user = computed(() => {
  const userData = localStorage.getItem('user')
  return userData ? JSON.parse(userData) : null
})

// 帖子数据
const post = ref({
  title: '',
  content: '',
  createdAt: '',
  viewCount: 0,
  likeCount: 0,
  replyCount: 0,
  isSticky: false,
  isEssence: false
})

// 回复数据
const replies = ref([])
const replyContent = ref('')
const replying = ref(false)
const isLiked = ref(false)
const attachments = ref([])

// 分页数据
const replyPagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 渲染Markdown内容
const renderedContent = computed(() => {
  return md.render(post.value.content || '')
})

const renderedReplyPreview = computed(() => {
  return md.render(replyContent.value || '')
})

const renderReplyContent = (content) => {
  return md.render(content || '')
}

// 格式化时间
const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

// 格式化文件大小
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2) + ' ' + sizes[i])
}

// 检查是否有编辑权限
const canEdit = computed(() => {
  return user.value && (user.value.userId === post.value.userId || user.value.role === 0)
})

// 检查是否有删除权限
const canDelete = computed(() => {
  return user.value && (user.value.userId === post.value.userId || user.value.role === 0)
})

const canDeleteReply = (reply) => {
  return user.value && (user.value.userId === reply.userId || user.value.role === 0)
}

// 获取帖子详情
const fetchPostDetail = async () => {
  try {
    const response = await axios.get(`/forum/posts/${route.params.id}`)
    post.value = response.data.data

    // 获取分类名称
    if (post.value.categoryId) {
      const categoryRes = await axios.get(`/forum/categories/${post.value.categoryId}`)
      post.value.categoryName = categoryRes.data.data.name
    }

    // 获取作者信息
    if (post.value.userId) {
      const userRes = await axios.get(`/users/${post.value.userId}`)
      // console.log(userRes)
      post.value.userName = userRes.data.data.username
      post.value.avatar = userRes.data.data.avatar
    }

    // 检查是否点赞
    if (user.value) {
      const likeRes = await axios.get('/forum/likes/page', {
        params: {
          userId: user.value.userId,
          postId: route.params.id,
          pageSize: 1
        }
      })
      const likeCount = await axios.get('/forum/likes/page', {params: {postId: route.params.id, pageSize: 1}})
      post.value.likeCount = likeCount.data.data.total
      isLiked.value = likeRes.data.data.total > 0
    }

    // 获取附件
    const attachmentRes = await axios.get('/forum/attachments/list', {
      params: { postId: route.params.id }
    })
    attachments.value = attachmentRes.data.list || []
  } catch (error) {
    ElMessage.error('获取帖子详情失败: ' + (error.response?.data?.message || error.message))
  }
}

// 获取回复列表
const fetchReplies = async () => {
  try {
    const response = await axios.get('/forum/replies/page', {
      params: {
        postId: route.params.id,
        pageNum: replyPagination.value.pageNum,
        pageSize: replyPagination.value.pageSize
      }
    })
    replies.value = response.data.data.records || []
    replyPagination.value.total = response.data.total || 0

    // 获取回复用户信息
    for (const reply of replies.value) {
      if (reply.userId) {
        const userRes = await axios.get(`/users/${reply.userId}`)
        reply.userName = userRes.data.username
        reply.avatar = userRes.data.avatar

        // 检查是否点赞
        if (user.value) {
          const likeRes = await axios.get('/forum/likes/page', {
            params: {
              userId: user.value.userId,
              replyId: reply.replyId,
              pageSize: 1
            }
          })
          reply.isLiked = likeRes.data.total > 0
        }
      }
    }
  } catch (error) {
    ElMessage.error('获取回复列表失败: ' + (error.response?.data?.message || error.message))
  }
}

// 提交回复
const submitReply = async () => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('回复内容不能为空')
    return
  }

  try {
    replying.value = true
    await axios.post('/forum/replies', {
      postId: route.params.id,
      userId: user.value.userId,
      content: replyContent.value
    })

    ElMessage.success('回复成功')
    replyContent.value = ''
    await fetchReplies()
    await fetchPostDetail() // 更新帖子回复数
  } catch (error) {
    ElMessage.error('回复失败: ' + (error.response?.data?.message || error.message))
  } finally {
    replying.value = false
  }
}

// 点赞处理
const handleLike = async () => {
  if (!user.value) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    if (isLiked.value) {
      // 取消点赞
      const likeRes = await axios.delete('/forum/likes', {
        params: {  // 注意这里是params而不是直接放对象
          id: 1,
          userId: user.value.userId,
          postId: route.params.id,
        }
      })
      isLiked.value = false
      post.value.likeCount -= 1

    } else {
      // 点赞
      await axios.post('/forum/likes', {
        userId: user.value.userId,
        postId: route.params.id
      })
      isLiked.value = true
      post.value.likeCount = (post.value.likeCount || 0) + 1
    }
  } catch (error) {
    ElMessage.error('操作失败: ' + (error.response?.data?.message || error.message))
  }
}

// 回复点赞处理
const handleReplyLike = async (reply) => {
  if (!user.value) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    if (reply.isLiked) {
      // 取消点赞
      const likeRes = await axios.get('/forum/likes/page', {
          userId: user.value.userId,
          replyId: reply.replyId,
          pageSize: 1
      })

      if (likeRes.data.list.length > 0) {
        await axios.delete(`/forum/likes/${likeRes.data.list[0].likeId}`)
        reply.isLiked = false
        reply.likeCount = Math.max(0, (reply.likeCount || 0) - 1)
      }
    } else {
      // 点赞
      await axios.post('/forum/likes', {
        postId: route.params.id,
        userId: user.value.userId,
        replyId: reply.replyId
      })
      reply.isLiked = true
      reply.likeCount = (reply.likeCount || 0) + 1
    }
  } catch (error) {
    ElMessage.error('操作失败: ' + (error.response?.data?.message || error.message))
  }
}

// 编辑帖子
const handleEdit = () => {
  router.push({ name: 'EditPost', params: { id: route.params.id } })
}

// 删除帖子
const handleDelete = async () => {
  try {
    await ElMessageBox.confirm('确定要删除这篇帖子吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await axios.delete(`/forum/posts/${route.params.id}`)
    ElMessage.success('删除成功')
    await router.push({name: 'ForumHome'})
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + (error.response?.data?.message || error.message))
    }
  }
}

// 删除回复
const handleReplyDelete = async (reply) => {
  try {
    await ElMessageBox.confirm('确定要删除这条回复吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await axios.delete(`/forum/replies/${reply.replyId}`)
    ElMessage.success('删除成功')
    await fetchReplies()
    await fetchPostDetail() // 更新帖子回复数
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + (error.response?.data?.message || error.message))
    }
  }
}

// 下载附件
const handleDownload = async (file) => {
  try {
    await fileUploader.downloadFile(file.fileName, file.userId)
    // 更新下载次数
    await axios.put(`/forum/attachments/${file.attachmentId}`, {
      downloadCount: (file.downloadCount || 0) + 1
    })
    file.downloadCount = (file.downloadCount || 0) + 1
  } catch (error) {
    ElMessage.error('下载失败: ' + (error.message || '未知错误'))
  }
}

// 图片上传处理
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

const handleImageUpload = async (options) => {
  try {
    const file = options.file
    const imageUrl = await fileUploader.uploadImage(file, { userId: user.value.userId })

    // 插入Markdown图片语法到回复内容
    const imgMarkdown = `![${file.name}](${imageUrl})`
    const cursorPos = options.onProgress ? 0 : replyContent.value.length
    replyContent.value =
        replyContent.value.slice(0, cursorPos) +
        imgMarkdown +
        replyContent.value.slice(cursorPos)

    ElMessage.success('图片上传成功')
  } catch (error) {
    ElMessage.error('图片上传失败: ' + (error.message || '未知错误'))
  }
}

// 分页变化处理
const handleReplySizeChange = (val) => {
  replyPagination.value.pageSize = val
  fetchReplies()
}

const handleReplyPageChange = (val) => {
  replyPagination.value.pageNum = val
  fetchReplies()
}

// 初始化数据
onMounted(() => {
  fetchPostDetail()
  fetchReplies()
})

// 监听路由变化
watch(
    () => route.params.id,
    (newId) => {
      if (newId) {
        fetchPostDetail()
        fetchReplies()
      }
    }
)
</script>

<template>
  <div class="post-detail">
    <!-- 帖子内容区域 -->
    <div class="post-content animate__animated animate__fadeIn">
      <div class="post-header">
        <h1>{{ post.title }}</h1>
        <div class="post-meta">
          <span class="author">
            <el-avatar :size="30" :src="post.avatar || ''" class="avatar-pulse">
              {{ post.userName ? post.userName.charAt(0) : '?' }}
            </el-avatar>
            {{ post.userName || '匿名用户' }}
          </span>
          <span class="time">
            <el-icon><Clock /></el-icon>
            {{ formatTime(post.createdAt) }}
          </span>
          <span class="category">
            <el-icon><Folder /></el-icon>
            {{ post.categoryName || '未分类' }}
          </span>
          <span class="view-count">
            <el-icon><View /></el-icon>
            {{ post.viewCount || 0 }} 浏览
          </span>
          <el-tag v-if="post.isSticky" type="warning" size="small" effect="dark" class="tag-shine">
            置顶
          </el-tag>
          <el-tag v-if="post.isEssence" type="danger" size="small" effect="dark" class="tag-shine">
            精华
          </el-tag>
        </div>
      </div>

      <div class="post-body">
        <div class="markdown-content" v-html="renderedContent"></div>

        <!-- 附件展示 -->
        <div class="attachments" v-if="attachments.length > 0">
          <h3><el-icon><Document /></el-icon> 附件列表</h3>
          <div class="attachment-list">
            <div v-for="file in attachments" :key="file.attachmentId" class="attachment-item hover-grow">
              <el-link :underline="false" @click="handleDownload(file)" class="file-link">
                <el-icon><Document /></el-icon>
                {{ file.fileName }} ({{ formatFileSize(file.fileSize) }})
              </el-link>
              <span class="download-count">
                <el-icon><Download /></el-icon>
                {{ file.downloadCount || 0 }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <div class="post-actions">
        <el-button
            :icon="Star"
            @click="handleLike"
            :type="isLiked ? 'danger' : ''"
            class="action-btn"
            round
        >
          {{ isLiked ? '已点赞' : '点赞' }} ({{ post.likeCount || 0 }})
        </el-button>
        <el-button
            v-if="canEdit"
            :icon="Edit"
            @click="handleEdit"
            class="action-btn"
            round
        >编辑</el-button>
        <el-button
            v-if="canDelete"
            :icon="Delete"
            @click="handleDelete"
            class="action-btn"
            round
        >删除</el-button>
      </div>
    </div>

    <!-- 回复区域 -->
    <div class="reply-section animate__animated animate__fadeIn">
      <h2><el-icon><ChatLineRound /></el-icon> 回复 ({{ replies.length }})</h2>

      <!-- 回复列表 -->
      <div class="reply-list">
        <div
            v-for="reply in replies"
            :key="reply.replyId"
            class="reply-item animate__animated animate__fadeInUp"
        >
          <div class="reply-header">
            <el-avatar :size="40" :src="reply.avatar || ''" class="avatar-hover">
              {{ reply.userName ? reply.userName.charAt(0) : '?' }}
            </el-avatar>
            <div class="reply-user">
              <span class="username">{{ reply.userName || '匿名用户' }}</span>
              <span class="time">{{ formatTime(reply.createdAt) }}</span>
            </div>
            <div class="reply-actions">
              <el-button
                  size="small"
                  :icon="Star"
                  circle
                  @click="handleReplyLike(reply)"
                  :type="reply.isLiked ? 'danger' : ''"
                  class="like-btn"
              />
              <el-button
                  v-if="canDeleteReply(reply)"
                  size="small"
                  :icon="Delete"
                  circle
                  @click="handleReplyDelete(reply)"
                  class="delete-btn"
              />
            </div>
          </div>
          <div class="reply-content">
            <div class="markdown-content" v-html="renderReplyContent(reply.content)"></div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination" v-if="replies.length > 0">
        <el-pagination
            v-model:current-page="replyPagination.pageNum"
            v-model:page-size="replyPagination.pageSize"
            :page-sizes="[10, 20, 30]"
            layout="prev, pager, next"
            :total="replyPagination.total"
            @size-change="handleReplySizeChange"
            @current-change="handleReplyPageChange"
            background
        />
      </div>

      <!-- 回复表单 -->
      <div class="reply-form" v-if="user">
        <h3><el-icon><EditPen /></el-icon> 发表回复</h3>
        <div class="editor-container">
          <el-input
              v-model="replyContent"
              type="textarea"
              :rows="5"
              placeholder="请输入回复内容 (支持Markdown语法)"
              resize="none"
              class="editor-input"
          />
          <div class="editor-toolbar">
            <el-upload
                action=""
                :show-file-list="false"
                :before-upload="beforeImageUpload"
                :http-request="handleImageUpload"
            >
              <el-button type="text" :icon="Picture" class="toolbar-btn">插入图片</el-button>
            </el-upload>
            <el-button
                type="primary"
                :icon="Promotion"
                @click="submitReply"
                :loading="replying"
                class="submit-btn"
                round
            >
              发表回复
            </el-button>
          </div>
          <div class="editor-preview" v-if="replyContent">
            <h4>预览:</h4>
            <div class="markdown-content" v-html="renderedReplyPreview"></div>
          </div>
        </div>
      </div>
      <div v-else class="login-tip">
        <el-button type="primary" @click="router.push('/login')" class="login-btn" round>
          <el-icon><User /></el-icon> 登录后即可回复
        </el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 基础变量 */
:root {
  --primary-color: #FF6B6B;
  --secondary-color: #4ECDC4;
  --accent-color: #45B7D1;
  --background-color: #F7F9FC;
  --card-bg: #FFFFFF;
  --text-color: #333;
  --text-light: #666;
  --border-color: #E1E5EB;
  --shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  --transition: all 0.3s ease;
}

.post-detail {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  background-color: var(--background-color);
  min-height: 100vh;
}

.post-content {
  background-color: var(--card-bg);
  border-radius: 12px;
  padding: 25px;
  margin-bottom: 25px;
  box-shadow: var(--shadow);
  border-left: 4px solid var(--primary-color);
  transition: var(--transition);
}

.post-content:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.post-header h1 {
  margin: 0 0 15px 0;
  font-size: 28px;
  color: var(--text-color);
  font-weight: 600;
  line-height: 1.3;
}

.post-meta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 20px;
  font-size: 14px;
  color: var(--text-light);
}

.post-meta .author {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  color: var(--text-color);
}

.post-meta .el-avatar {
  margin-right: 5px;
  transition: var(--transition);
}

.post-body {
  margin: 25px 0;
  line-height: 1.8;
  color: var(--text-color);
}

.markdown-content {
  word-break: break-word;
  font-size: 15px;
}

.markdown-content :deep(h1) {
  font-size: 24px;
  color: var(--primary-color);
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 8px;
  margin: 1.5em 0 0.8em;
}

.markdown-content :deep(h2) {
  font-size: 20px;
  color: var(--accent-color);
  margin: 1.3em 0 0.7em;
}

.markdown-content :deep(h3) {
  font-size: 18px;
  color: var(--secondary-color);
  margin: 1.1em 0 0.6em;
}

.markdown-content :deep(p) {
  margin: 1em 0;
  line-height: 1.8;
}

.markdown-content :deep(a) {
  color: var(--accent-color);
  text-decoration: none;
  transition: var(--transition);
}

.markdown-content :deep(a:hover) {
  color: var(--primary-color);
  text-decoration: underline;
}

.markdown-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin: 10px 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: var(--transition);
}

.markdown-content :deep(img:hover) {
  transform: scale(1.01);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.markdown-content :deep(blockquote) {
  border-left: 4px solid var(--secondary-color);
  padding: 10px 15px;
  margin: 15px 0;
  background-color: rgba(78, 205, 196, 0.05);
  color: var(--text-light);
  border-radius: 0 8px 8px 0;
}

.attachments {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px dashed var(--border-color);
}

.attachments h3 {
  font-size: 18px;
  color: var(--accent-color);
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.attachment-list {
  margin-top: 15px;
}

.attachment-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 15px;
  border-bottom: 1px dashed var(--border-color);
  transition: var(--transition);
}

.attachment-item:hover {
  background-color: rgba(69, 183, 209, 0.05);
  border-radius: 6px;
}

.file-link {
  color: var(--text-color);
  transition: var(--transition);
  display: flex;
  align-items: center;
  gap: 8px;
}

.file-link:hover {
  color: var(--accent-color);
}

.download-count {
  color: var(--text-light);
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.post-actions {
  margin-top: 25px;
  padding-top: 20px;
  border-top: 1px solid var(--border-color);
  display: flex;
  gap: 12px;
}

.action-btn {
  transition: var(--transition);
  font-weight: 500;
}

.action-btn:hover {
  transform: translateY(-2px);
}

.reply-section {
  background-color: var(--card-bg);
  border-radius: 12px;
  padding: 25px;
  box-shadow: var(--shadow);
  transition: var(--transition);
}

.reply-section:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.reply-section h2 {
  font-size: 22px;
  color: var(--text-color);
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.reply-list {
  margin: 25px 0;
}

.reply-item {
  padding: 20px;
  border-bottom: 1px solid var(--border-color);
  transition: var(--transition);
}

.reply-item:hover {
  background-color: rgba(255, 107, 107, 0.03);
  border-radius: 8px;
}

.reply-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.reply-user {
  flex: 1;
  margin-left: 15px;
}

.reply-user .username {
  font-weight: 500;
  color: var(--text-color);
  transition: var(--transition);
}

.reply-user .username:hover {
  color: var(--primary-color);
}

.reply-user .time {
  font-size: 13px;
  color: var(--text-light);
  margin-left: 12px;
}

.reply-actions {
  display: flex;
  gap: 8px;
}

.like-btn, .delete-btn {
  transition: var(--transition);
}

.like-btn:hover {
  transform: scale(1.1);
  color: var(--primary-color);
}

.delete-btn:hover {
  transform: scale(1.1);
  color: var(--primary-color);
}

.reply-content {
  margin-left: 55px;
  padding-left: 10px;
  border-left: 2px solid rgba(78, 205, 196, 0.3);
}

.reply-form {
  margin-top: 35px;
  padding-top: 20px;
  border-top: 1px solid var(--border-color);
}

.reply-form h3 {
  font-size: 18px;
  color: var(--text-color);
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.editor-container {
  margin-top: 20px;
}

.editor-input :deep(.el-textarea__inner) {
  border-radius: 8px;
  border: 1px solid var(--border-color);
  transition: var(--transition);
  padding: 15px;
}

.editor-input :deep(.el-textarea__inner:focus) {
  border-color: var(--accent-color);
  box-shadow: 0 0 0 2px rgba(69, 183, 209, 0.2);
}

.editor-toolbar {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
}

.toolbar-btn {
  color: var(--text-light);
  transition: var(--transition);
}

.toolbar-btn:hover {
  color: var(--accent-color);
}

.submit-btn {
  background-color: var(--primary-color);
  border: none;
  padding: 10px 25px;
  transition: var(--transition);
}

.submit-btn:hover {
  background-color: #FF5252;
  transform: translateY(-2px);
}

.editor-preview {
  margin-top: 20px;
  padding: 15px;
  border: 1px dashed var(--border-color);
  border-radius: 8px;
  background-color: rgba(247, 249, 252, 0.5);
}

.editor-preview h4 {
  font-size: 16px;
  color: var(--text-light);
  margin-bottom: 10px;
}

.login-tip {
  text-align: center;
  margin: 30px 0;
}

.login-btn {
  background-color: var(--primary-color);
  border: none;
  padding: 12px 30px;
  font-size: 15px;
  transition: var(--transition);
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.login-btn:hover {
  background-color: #FF5252;
  transform: translateY(-2px);
}

.pagination {
  margin-top: 25px;
  display: flex;
  justify-content: center;
}

.pagination :deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background-color: var(--primary-color);
}

.pagination :deep(.el-pagination.is-background .el-pager li:not(.is-disabled):hover) {
  color: var(--primary-color);
}

/* 动画效果 */
.avatar-pulse {
  animation: pulse 2s infinite;
}

.avatar-hover {
  transition: var(--transition);
}

.avatar-hover:hover {
  transform: scale(1.1);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.tag-shine {
  position: relative;
  overflow: hidden;
}

.tag-shine::after {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(
      to bottom right,
      rgba(255, 255, 255, 0) 0%,
      rgba(255, 255, 255, 0) 45%,
      rgba(255, 255, 255, 0.5) 48%,
      rgba(255, 255, 255, 0.8) 50%,
      rgba(255, 255, 255, 0.5) 52%,
      rgba(255, 255, 255, 0) 55%,
      rgba(255, 255, 255, 0) 100%
  );
  transform: rotate(30deg);
  animation: shine 3s infinite;
}

.hover-grow {
  transition: var(--transition);
}

.hover-grow:hover {
  transform: scale(1.02);
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(255, 107, 107, 0.4);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(255, 107, 107, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(255, 107, 107, 0);
  }
}

@keyframes shine {
  0% {
    left: -50%;
    top: -50%;
  }
  100% {
    left: 150%;
    top: 150%;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .post-detail {
    padding: 15px;
  }

  .post-content, .reply-section {
    padding: 18px;
  }

  .post-header h1 {
    font-size: 22px;
  }

  .post-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .reply-content {
    margin-left: 0;
    padding-left: 0;
    border-left: none;
    padding-top: 10px;
    border-top: 1px dashed var(--border-color);
  }

  .reply-header {
    flex-wrap: wrap;
  }

  .reply-actions {
    margin-top: 10px;
    width: 100%;
    justify-content: flex-end;
  }
}
</style>