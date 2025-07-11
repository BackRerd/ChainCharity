<template>
  <div class="forum-home">
    <!-- 头部搜索和发帖 -->
    <div class="forum-header">
      <el-input
          v-model="searchKeyword"
          placeholder="搜索慈善话题..."
          class="search-input"
          clearable
          @clear="handleSearchClear"
          @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon class="search-icon"><Search /></el-icon>
        </template>
        <template #append>
          <el-button
              :icon="Search"
              @click="handleSearch"
              class="search-btn"
          />
        </template>
      </el-input>
      <el-button
          type="primary"
          :icon="Plus"
          @click="handleCreatePost"
          v-if="user"
          class="create-btn"
      >
        发起话题
      </el-button>
    </div>

    <!-- 分类导航 -->
    <div class="category-nav">
      <el-tabs v-model="activeCategory" @tab-change="handleCategoryChange">
        <el-tab-pane label="全部" name="all"></el-tab-pane>
        <el-tab-pane
            v-for="category in categories"
            :key="category.categoryId"
            :label="category.name"
            :name="category.categoryId.toString()"
        ></el-tab-pane>
      </el-tabs>
    </div>

    <!-- 帖子列表 -->
    <div class="post-list">
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated class="custom-skeleton" />
      </div>

      <template v-else>
        <div v-if="posts.length === 0" class="empty-tips">
          <el-empty description="暂无慈善话题，快来发起第一个吧！"
                    image="https://img.icons8.com/ios/100/charity.png" />
        </div>

        <transition-group name="post-list" tag="div">
          <div
              v-for="post in posts"
              :key="post.postId"
              class="post-item"
              @click="navigateToPost(post.postId)"
          >
            <div class="post-content">
              <div class="post-header">
                <span class="post-title">{{ post.title }}</span>
                <div class="post-tags">
                  <el-tag
                      v-if="post.isSticky"
                      type="warning"
                      size="small"
                      effect="dark"
                      class="tag-sticky"
                  >
                    <el-icon><Top /></el-icon> 置顶
                  </el-tag>
                  <el-tag
                      v-if="post.isEssence"
                      type="success"
                      size="small"
                      effect="dark"
                      class="tag-essence"
                  >
                    <el-icon><Star /></el-icon> 精华
                  </el-tag>
                </div>
              </div>
              <div class="post-meta">
                <span class="author">
                  <el-avatar :size="24" :src="post.avatar || 'https://img.icons8.com/ios/50/user-male-circle--v1.png'" />
                  {{ post.userName || '爱心网友' }}
                </span>
                <span class="category">
                  <el-icon><Folder /></el-icon>
                  {{ getCategoryName(post.categoryId) }}
                </span>
                <span class="time">
                  <el-icon><Clock /></el-icon>
                  {{ formatTime(post.createdAt) }}
                </span>
                <span class="view-count">
                  <el-icon><View /></el-icon>
                  {{ post.viewCount || 0 }}
                </span>
                <span class="reply-count">
                  <el-icon><ChatLineRound /></el-icon>
                  {{ post.replyCount || 0 }}
                </span>
              </div>
            </div>
            <div class="post-heart">
              <el-icon :size="24" color="#ff6b6b"><Heart /></el-icon>
            </div>
          </div>
        </transition-group>
      </template>

      <!-- 分页 -->
      <div class="pagination" v-if="posts.length > 0">
        <el-pagination
            v-model:current-page="pagination.pageNum"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[10, 20, 30, 50]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            background
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import {
  Search,
  Plus,
  User,
  Folder,
  Clock,
  View,
  ChatLineRound,
  Top,
  Star
} from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'

const router = useRouter()

// 用户信息
const user = computed(() => {
  const userData = localStorage.getItem('user')
  return userData ? JSON.parse(userData) : null
})

// 搜索相关
const searchKeyword = ref('')
const activeCategory = ref('all')
const loading = ref(false)

// 分页数据
const pagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 帖子数据
const posts = ref([])
const categories = ref([])

// 获取分类名称
const getCategoryName = (categoryId) => {
  if (!categoryId) return '未分类'
  const category = categories.value.find(c => c.categoryId === categoryId)
  return category ? category.name : '未知分类'
}

// 格式化时间
const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

// 获取帖子列表
const fetchPosts = async () => {
  try {
    loading.value = true
    const params = {
      pageNum: pagination.value.pageNum,
      pageSize: pagination.value.pageSize
    }

    if (activeCategory.value !== 'all') {
      params.categoryId = activeCategory.value
    }

    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }

    const response = await axios.get('/forum/posts/page', { params })
    posts.value = response.data.data.records || []
    pagination.value.total = response.data.data.total || 0
  } catch (error) {
    ElMessage.error('获取帖子列表失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
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

// 分页变化处理
const handleSizeChange = (val) => {
  pagination.value.pageSize = val
  fetchPosts()
}

const handleCurrentChange = (val) => {
  pagination.value.pageNum = val
  fetchPosts()
}

// 分类变化处理
const handleCategoryChange = () => {
  pagination.value.pageNum = 1
  fetchPosts()
}

// 搜索处理
const handleSearch = () => {
  pagination.value.pageNum = 1
  fetchPosts()
}

const handleSearchClear = () => {
  searchKeyword.value = ''
  fetchPosts()
}

// 创建帖子
const handleCreatePost = () => {
  router.push({ name: 'CreatePost' })
}

// 跳转到帖子详情
const navigateToPost = (postId) => {
  router.push({ name: 'PostDetail', params: { id: postId } })
}

// 初始化数据
onMounted(() => {
  fetchPosts()
  fetchCategories()
})
</script>

<style scoped lang="scss">
.forum-home {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background: linear-gradient(to bottom, #f9f9f9, #fff);
  min-height: 100vh;
}

.forum-header {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  align-items: center;

  .search-input {
    flex: 1;
    transition: all 0.3s ease;

    &:focus-within {
      box-shadow: 0 0 0 2px rgba(255, 138, 101, 0.2);
    }

    .search-icon {
      color: #ff8a65;
      font-size: 18px;
    }

    .search-btn {
      background-color: #ff8a65;
      color: white;
      border-color: #ff8a65;

      &:hover {
        background-color: #ff7043;
      }
    }
  }

  .create-btn {
    background-color: #4caf50;
    border-color: #4caf50;
    font-weight: 500;
    transition: all 0.3s ease;

    &:hover {
      background-color: #43a047;
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(76, 175, 80, 0.3);
    }
  }
}

.category-nav {
  margin-bottom: 25px;
  background: white;
  border-radius: 8px;
  padding: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);

  :deep(.el-tabs__nav-wrap::after) {
    height: 0;
  }

  :deep(.el-tabs__item) {
    font-size: 15px;
    color: #666;
    padding: 0 20px;
    height: 40px;
    line-height: 40px;

    &:hover {
      color: #ff8a65;
    }
  }

  :deep(.el-tabs__item.is-active) {
    color: #ff8a65;
    font-weight: 500;
  }

  :deep(.el-tabs__active-bar) {
    background-color: #ff8a65;
    height: 3px;
    border-radius: 3px;
  }
}

.post-list {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  overflow: hidden;

  .loading-container {
    padding: 20px;

    .custom-skeleton {
      :deep(.el-skeleton__item) {
        background: linear-gradient(90deg, #f5f5f5 25%, #e0e0e0 50%, #f5f5f5 75%);
        background-size: 400% 100%;
        animation: skeleton-loading 1.5s ease infinite;
      }
    }
  }

  .empty-tips {
    padding: 60px 0;
    text-align: center;

    :deep(.el-empty__description p) {
      color: #888;
      font-size: 16px;
      margin-top: 15px;
    }
  }
}

.post-item {
  padding: 18px 25px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  justify-content: space-between;
  align-items: center;

  &:hover {
    background-color: #fff9f7;
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(255, 138, 101, 0.1);

    .post-title {
      color: #ff8a65;
    }

    .post-heart {
      transform: scale(1.1);
    }
  }

  .post-content {
    flex: 1;
  }

  .post-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 10px;

    .post-title {
      font-size: 17px;
      font-weight: 500;
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      transition: color 0.3s ease;
      color: #333;
    }

    .post-tags {
      display: flex;
      gap: 8px;

      .tag-sticky {
        background-color: #fff3e0;
        border-color: #ffe0b2;
        color: #e65100;
      }

      .tag-essence {
        background-color: #e8f5e9;
        border-color: #c8e6c9;
        color: #2e7d32;
      }
    }
  }

  .post-meta {
    display: flex;
    gap: 20px;
    font-size: 13px;
    color: #888;
    align-items: center;

    span {
      display: flex;
      align-items: center;
      gap: 5px;

      .el-icon {
        font-size: 14px;
      }
    }

    .author {
      display: flex;
      align-items: center;
      gap: 6px;

      .el-avatar {
        margin-right: 5px;
      }
    }
  }

  .post-heart {
    margin-left: 15px;
    transition: all 0.3s ease;
    opacity: 0.7;

    &:hover {
      opacity: 1;
      transform: scale(1.2);
    }
  }
}

.pagination {
  padding: 25px 20px;
  display: flex;
  justify-content: center;
  background-color: #fafafa;

  :deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
    background-color: #ff8a65;
  }

  :deep(.el-pagination.is-background .el-pager li:not(.is-disabled):hover) {
    color: #ff8a65;
  }
}

/* 列表过渡动画 */
.post-list-move,
.post-list-enter-active,
.post-list-leave-active {
  transition: all 0.5s ease;
}

.post-list-enter-from,
.post-list-leave-to {
  opacity: 0;
  transform: translateY(30px);
}

.post-list-leave-active {
  position: absolute;
}

@keyframes skeleton-loading {
  0% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0 50%;
  }
}
</style>