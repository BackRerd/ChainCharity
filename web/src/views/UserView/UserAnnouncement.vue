
<script setup>
import { ref, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { marked } from 'marked'
import axios from 'axios'
import { ElMessage } from 'element-plus'

// 数据状态
const announcements = ref([])
const loading = ref(true)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchTitle = ref('')
const filterType = ref('all')

// 弹窗相关
const detailVisible = ref(false)
const currentAnnouncement = ref({
  id: '',
  title: '',
  content: '',
  isTop: 0,
  createTime: '',
  updateTime: ''
})

// 获取公告列表
const fetchAnnouncements = async () => {
  try {
    loading.value = true
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      title: searchTitle.value
    }

    let url = '/announcements/page'
    if (filterType.value === 'top') {
      url = '/announcements/page?isTop=1'
    }

    const response = await axios.get(url, { params })
    announcements.value = response.data.data.records
    total.value = response.data.data.total
  } catch (error) {
    console.error('获取公告列表失败:', error)
    ElMessage.error('获取公告列表失败')
  } finally {
    loading.value = false
  }
}

// 显示公告详情
const showAnnouncementDetail = (announcement) => {
  currentAnnouncement.value = { ...announcement }
  detailVisible.value = true
}

// 搜索清除处理
const handleSearchClear = () => {
  searchTitle.value = ''
  fetchAnnouncements()
}

// 格式化日期 (仅日期)
const formatDate = (dateString) => {
  const date = new Date(dateString)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

// 格式化日期时间 (详细)
const formatDateTime = (dateString) => {
  const date = new Date(dateString)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

// 渲染Markdown预览 (截断处理)
const renderMarkdownPreview = (content) => {
  const plainText = content.replace(/[#*`\-[\]]/g, '') // 简单去除Markdown标记
  const previewText = plainText.length > 150 ? plainText.substring(0, 150) + '...' : plainText
  return marked(previewText || '')
}

// 完整渲染Markdown
const renderMarkdown = (content) => {
  return marked(content || '')
}

// 初始化加载数据
onMounted(() => {
  fetchAnnouncements()
})
</script>
<template>
  <div class="announcements-page">
    <div class="header">
      <h1 class="page-title">爱心公告</h1>
      <div class="search-box">
        <el-input
            v-model="searchTitle"
            placeholder="搜索公告标题"
            clearable
            style="width: 300px"
            @clear="handleSearchClear"
            @keyup.enter="fetchAnnouncements"
            class="search-input"
        >
          <template #append>
            <el-button
                :icon="Search"
                @click="fetchAnnouncements"
                class="charity-search-button"
            />
          </template>
        </el-input>
      </div>
    </div>

    <div class="filter-container">
      <el-radio-group v-model="filterType" @change="fetchAnnouncements">
        <el-radio-button label="all" class="filter-button">全部公告</el-radio-button>
        <el-radio-button label="top" class="filter-button">置顶公告</el-radio-button>
      </el-radio-group>
    </div>

    <el-skeleton :loading="loading" animated :count="3" class="skeleton-loader">
      <template #default>
        <div class="announcement-list">
          <div
              v-for="announcement in announcements"
              :key="announcement.id"
              class="announcement-card"
              :class="{ 'is-top': announcement.isTop === 1 }"
              @click="showAnnouncementDetail(announcement)"
          >
            <div class="announcement-header">
              <span class="title">{{ announcement.title }}</span>
              <el-tag v-if="announcement.isTop === 1" type="danger" size="small" class="top-tag">
                <i class="el-icon-top"></i> 置顶
              </el-tag>
              <span class="time">{{ formatDate(announcement.createTime) }}</span>
            </div>
            <div class="announcement-content">
              <div class="markdown-preview" v-html="renderMarkdownPreview(announcement.content)" />
            </div>
            <div class="read-more">
              <span>点击查看详情 <i class="el-icon-arrow-right"></i></span>
            </div>
          </div>
        </div>

        <div class="pagination-container">
          <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :total="total"
              layout="prev, pager, next, jumper"
              @current-change="fetchAnnouncements"
              class="charity-pagination"
          />
        </div>
      </template>
    </el-skeleton>

    <!-- 公告详情弹窗 -->
    <el-dialog
        v-model="detailVisible"
        :title="currentAnnouncement.title"
        width="70%"
        top="5vh"
        custom-class="announcement-detail-dialog"
    >
      <div class="announcement-meta">
        <el-tag v-if="currentAnnouncement.isTop === 1" type="danger" size="small" class="top-tag">
          <i class="el-icon-top"></i> 置顶
        </el-tag>
        <span class="create-time">发布时间: {{ formatDateTime(currentAnnouncement.createTime) }}</span>
        <span class="update-time" v-if="currentAnnouncement.updateTime !== currentAnnouncement.createTime">
          最后更新: {{ formatDateTime(currentAnnouncement.updateTime) }}
        </span>
      </div>

      <div class="markdown-content" v-html="renderMarkdown(currentAnnouncement.content)" />

      <template #footer>
        <el-button
            @click="detailVisible = false"
            class="charity-close-button"
        >
          <i class="el-icon-close"></i> 关闭
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<style scoped>
.announcements-page {
  padding: 20px;
  background-color: #f9f5f0;
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding: 20px;
  background: linear-gradient(135deg, #ff7e5f, #feb47b);
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(254, 180, 123, 0.3);
}

.page-title {
  color: white;
  font-size: 28px;
  font-weight: bold;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
  margin: 0;
}

.search-box {
  display: flex;
  align-items: center;
}

.search-input {
  border-radius: 20px;
  overflow: hidden;
}

.filter-container {
  margin-bottom: 20px;
  text-align: center;
}

.announcement-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.announcement-card {
  background-color: white;
  border-radius: 10px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  border-left: 4px solid #ff7e5f;
  position: relative;
  overflow: hidden;
}

.announcement-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(254, 180, 123, 0.3);
}

.announcement-card.is-top {
  border-left: 4px solid #e74c3c;
  background-color: #fff9f7;
}

.announcement-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.announcement-header .title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-right: 10px;
  flex: 1;
  min-width: 60%;
}

.announcement-header .time {
  color: #888;
  font-size: 14px;
}

.top-tag {
  background-color: #ffece8;
  color: #e74c3c;
  border-color: #ffcdc2;
  margin-right: 10px;
}

.announcement-content {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 15px;
  max-height: 60px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.read-more {
  text-align: right;
  color: #e74c3c;
  font-size: 13px;
  opacity: 0;
  transition: opacity 0.3s;
}

.announcement-card:hover .read-more {
  opacity: 1;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}

.skeleton-loader {
  padding: 20px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.announcement-meta {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
  color: #666;
  font-size: 14px;
}

.markdown-content {
  line-height: 1.8;
  color: #444;
}

/* 按钮样式优化 */
.charity-search-button {
  background: linear-gradient(135deg, #ff9a8b, #ff6b6b);
  color: white;
  border: none;
  border-radius: 0 20px 20px 0;
  padding: 0 15px;
  height: 32px;
  transition: all 0.3s;
  box-shadow: 0 2px 4px rgba(255, 107, 107, 0.3);
}

.charity-search-button:hover {
  background: linear-gradient(135deg, #ff6b6b, #ff4757);
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(255, 107, 107, 0.4);
}

.charity-search-button:active {
  transform: translateY(0);
}

.charity-close-button {
  background: linear-gradient(135deg, #ff9a8b, #ff6b6b);
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 24px;
  transition: all 0.3s;
  box-shadow: 0 2px 6px rgba(255, 107, 107, 0.3);
  font-weight: 500;
}

.charity-close-button:hover {
  background: linear-gradient(135deg, #ff6b6b, #ff4757);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.4);
}

.charity-close-button:active {
  transform: translateY(0);
}

.charity-close-button i {
  margin-right: 6px;
}

.filter-button {
  border: none;
  background: #fff4f2;
  color: #ff6b6b;
  padding: 8px 16px;
  transition: all 0.3s;
}

.filter-button.is-active {
  background: linear-gradient(135deg, #ff9a8b, #ff6b6b);
  color: white;
  box-shadow: 0 2px 6px rgba(255, 107, 107, 0.3);
}

.charity-pagination {
  --el-pagination-bg-color: #fff9f7;
  --el-pagination-button-bg-color: #fff9f7;
  --el-pagination-button-disabled-bg-color: #f5e6e3;
  --el-pagination-hover-color: #ff6b6b;
  --el-pagination-button-hover-color: #ff6b6b;
}

.charity-pagination .btn-prev,
.charity-pagination .btn-next,
.charity-pagination .number {
  border-radius: 8px;
  margin: 0 4px;
  transition: all 0.3s;
}

.charity-pagination .number.is-active {
  background: linear-gradient(135deg, #ff9a8b, #ff6b6b);
  color: white;
  border-color: transparent;
}

/* 爱心标记 */
.announcement-card::after {
  content: "❤";
  position: absolute;
  right: 20px;
  top: 20px;
  color: rgba(231, 76, 60, 0.1);
  font-size: 24px;
  transition: all 0.3s;
}

.announcement-card:hover::after {
  color: rgba(231, 76, 60, 0.3);
  transform: scale(1.2);
}

/* 详情弹窗样式 */
:deep(.announcement-detail-dialog) {
  border-radius: 10px;
  background-color: #fff9f7;
}

:deep(.announcement-detail-dialog .el-dialog__header) {
  background: linear-gradient(135deg, #ff7e5f, #feb47b);
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  padding: 15px 20px;
}

:deep(.announcement-detail-dialog .el-dialog__title) {
  color: white;
  font-weight: bold;
}
</style>
