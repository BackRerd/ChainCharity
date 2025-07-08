<template>
  <div class="activity-container">
    <!-- 搜索筛选区域 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="24">
        <el-card shadow="always" class="filter-card animate__animated animate__fadeIn">
          <el-form :inline="true" :model="filterParams" class="filter-form">
            <el-form-item label="活动标题">
              <el-input
                  v-model="filterParams.title"
                  placeholder="请输入活动标题"
                  clearable
                  @clear="fetchActivities"
                  class="search-input"
              >
                <template #prefix>
                  <i class="el-icon-search"></i>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="活动分类">
              <el-select
                  v-model="filterParams.categoryId"
                  placeholder="请选择分类"
                  clearable
                  filterable
                  remote
                  :remote-method="searchCategories"
                  :loading="categoryLoading"
                  @clear="fetchActivities"
                  @change="fetchActivities"
                  class="category-select"
              >
                <el-option
                    v-for="category in categories"
                    :key="category.categoryId"
                    :label="category.categoryName"
                    :value="category.categoryId"
                />
                <el-pagination
                    small
                    layout="prev, pager, next"
                    :total="categoryPagination.total"
                    :page-size="categoryPagination.pageSize"
                    :current-page="categoryPagination.currentPage"
                    @current-change="handleCategoryPageChange"
                    class="category-pagination"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="活动状态">
              <el-select
                  v-model="filterParams.status"
                  placeholder="请选择状态"
                  clearable
                  @clear="fetchActivities"
                  @change="fetchActivities"
                  class="status-select"
              >
                <el-option label="筹备中" value="planning" />
                <el-option label="进行中" value="ongoing" />
                <el-option label="已结束" value="completed" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="fetchActivities" class="search-btn" icon="el-icon-search">搜索</el-button>
              <el-button @click="resetFilter" class="reset-btn" icon="el-icon-refresh">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <!-- 活动列表 -->
    <el-row :gutter="20" class="activity-list">
      <template v-if="activities.length > 0">
        <el-col
            v-for="(activity, index) in activities"
            :key="activity.activityId"
            :xs="24"
            :sm="12"
            :md="8"
            :lg="6"
            class="activity-item"
        >
          <el-card
              shadow="hover"
              class="activity-card animate__animated animate__fadeInUp"
              :style="{'animation-delay': `${index * 0.1}s`}"
              @mouseenter="hoverIndex = index"
              @mouseleave="hoverIndex = -1"
              :class="{'hover-card': hoverIndex === index}"
          >
            <div class="activity-header">
              <el-tag
                  :type="getStatusTagType(activity.status)"
                  size="small"
                  :class="`status-tag status-${activity.status}`"
              >
                {{ getStatusText(activity.status) }}
              </el-tag>
              <span class="activity-category">{{ activity.categoryName }}</span>
            </div>
            <h3 class="activity-title">{{ activity.title }}</h3>
            <div class="activity-image-placeholder">
              <i class="el-icon-picture-outline"></i>
            </div>
            <p class="activity-desc">{{ truncateDescription(activity.description) }}</p>
            <div class="activity-dates">
              <span><i class="el-icon-date"></i> {{ formatDate(activity.startDate) }}</span>
              <span class="date-separator">至</span>
              <span>{{ formatDate(activity.endDate) }}</span>
            </div>
            <div class="activity-actions">
              <el-button
                  type="primary"
                  size="small"
                  @click="showActivityDetail(activity)"
                  class="action-btn detail-btn"
              >
                <i class="el-icon-view"></i> 查看详情
              </el-button>
              <el-button
                  type="success"
                  size="small"
                  @click="handleParticipate(activity)"
                  class="action-btn participate-btn"
              >
                <i class="el-icon-user"></i> 参与活动
              </el-button>
              <el-button
                  type="warning"
                  size="small"
                  @click="handleReceiveHelp(activity)"
                  class="action-btn help-btn"
              >
                <i class="el-icon-help"></i> 参与受助
              </el-button>
            </div>
          </el-card>
        </el-col>
      </template>
      <template v-else>
        <el-col :span="24" class="no-data animate__animated animate__fadeIn">
          <el-empty description="暂无活动数据" image="/images/empty.png">
            <el-button type="primary" @click="resetFilter">重置筛选条件</el-button>
          </el-empty>
        </el-col>
      </template>
    </el-row>

    <!-- 分页 -->
    <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[8, 16, 24, 32]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination animate__animated animate__fadeIn"
    />

    <!-- 活动详情对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="currentActivity.title"
        width="60%"
        :before-close="handleClose"
        class="activity-dialog"
        :close-on-click-modal="false"
    >
      <div class="activity-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="活动分类" label-class-name="detail-label">
            <el-tag type="info" class="category-tag">
              {{ currentActivity.categoryName }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="活动状态" label-class-name="detail-label">
            <el-tag :type="getStatusTagType(currentActivity.status)" class="status-tag">
              {{ getStatusText(currentActivity.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="开始日期" label-class-name="detail-label">
            <span class="date-text">
              <i class="el-icon-date"></i> {{ formatDate(currentActivity.startDate) }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="结束日期" label-class-name="detail-label">
            <span class="date-text">
              <i class="el-icon-date"></i> {{ formatDate(currentActivity.endDate) }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="活动描述" :span="2" label-class-name="detail-label">
            <div class="description-content">
              {{ currentActivity.description }}
            </div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" class="dialog-btn close-btn">
            <i class="el-icon-close"></i> 关闭
          </el-button>
          <el-button
              type="primary"
              @click="handleParticipate(currentActivity)"
              class="dialog-btn participate-btn"
          >
            <i class="el-icon-user"></i> 参与活动
          </el-button>
          <el-button
              type="warning"
              @click="handleReceiveHelp(currentActivity)"
              class="dialog-btn help-btn"
          >
            <i class="el-icon-help"></i> 参与受助
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

// 数据状态
const activities = ref([])
const categories = ref([])
const currentActivity = ref({
  activityId: null,
  title: '',
  description: '',
  categoryId: null,
  categoryName: '',
  startDate: '',
  endDate: '',
  status: '',
  createdBy: null,
  createdAt: ''
})
const dialogVisible = ref(false)
const categoryLoading = ref(false)

// 分页和筛选参数
const pagination = ref({
  currentPage: 1,
  pageSize: 8,
  total: 0
})

const categoryPagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const filterParams = ref({
  title: '',
  categoryId: null,
  status: ''
})

// 初始化加载数据
onMounted(() => {
  fetchActivities()
  fetchCategories()
})

// 获取活动列表
const fetchActivities = async () => {
  try {
    const params = {
      page: pagination.value.currentPage,
      size: pagination.value.pageSize,
      ...filterParams.value
    }

    // 移除值为null或空的参数
    Object.keys(params).forEach(key => {
      if (params[key] === null || params[key] === '') {
        delete params[key]
      }
    })

    const response = await axios.get('/view-activities/page', { params })
    if (response.data && response.data.data) {
      activities.value = response.data.data.records || []
      pagination.value.total = response.data.data.total || 0
    } else {
      activities.value = []
      pagination.value.total = 0
    }
  } catch (error) {
    console.error('获取活动列表失败:', error)
    ElMessage.error('获取活动列表失败')
    activities.value = []
    pagination.value.total = 0
  }
}

// 获取分类列表
const fetchCategories = async (categoryName = '') => {
  try {
    categoryLoading.value = true
    const response = await axios.get('/categories/page', {
      params: {
        page: categoryPagination.value.currentPage,
        size: categoryPagination.value.pageSize,
        categoryName: categoryName
      }
    })
    if (response.data && response.data.data) {
      categories.value = response.data.data.records || []
      categoryPagination.value.total = response.data.data.total || 0
    } else {
      categories.value = []
      categoryPagination.value.total = 0
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
    ElMessage.error('获取分类列表失败')
    categories.value = []
    categoryPagination.value.total = 0
  } finally {
    categoryLoading.value = false
  }
}

// 搜索分类
const searchCategories = (query) => {
  fetchCategories(query)
}

// 分类分页变化
const handleCategoryPageChange = (page) => {
  categoryPagination.value.currentPage = page
  fetchCategories()
}

// 分页处理
const handleSizeChange = (val) => {
  pagination.value.pageSize = val
  pagination.value.currentPage = 1
  fetchActivities()
}

const handleCurrentChange = (val) => {
  pagination.value.currentPage = val
  fetchActivities()
}

// 重置筛选条件
const resetFilter = () => {
  filterParams.value = {
    title: '',
    categoryId: null,
    status: ''
  }
  pagination.value.currentPage = 1
  fetchActivities()
}

// 显示活动详情
const showActivityDetail = (activity) => {
  currentActivity.value = { ...activity }
  dialogVisible.value = true
}

// 参与活动
const handleParticipate = (activity) => {
  const formSData = ref({
    activityId: activity.activityId,
    userId: JSON.parse(localStorage.getItem("user")).userId,
    role: 'donor',
    participationTime: new Date(),
    approvalStatus: 'pending',
    reviewedBy: '0',
    reviewedAt: new Date()
  })
  axios.post('/participations',formSData.value).then(res => {
    if (res.data.code === "200"){
      ElMessage.success(`您已成功报名参与活动: ${activity.title}|${activity.activityId}`)
    }else {
      ElMessage.warning("您已参加过该活动！")
    }
  }).catch(res => {
    ElMessage.warning("您已参加过该活动！")
  })

}

// 参与受助
const handleReceiveHelp = (activity) => {
  const formSData = ref({
    activityId: activity.activityId,
    userId: JSON.parse(localStorage.getItem("user")).userId,
    role: 'recipient',
    participationTime: new Date(),
    approvalStatus: 'pending',
    reviewedBy: '0',
    reviewedAt: new Date()
  })
  axios.post('/participations',formSData.value).then(res => {
    if (res.data.code === "200"){
      ElMessage.success(`您已成功申请受助于活动: ${activity.title}|${activity.activityId}`)
    }else {
      ElMessage.warning("您已参加过该活动！")
    }
  }).catch(res => {
    ElMessage.warning("您已参加过该活动！")
  })
}

// 辅助函数
const truncateDescription = (text) => {
  if (!text) return ''
  return text.length > 100 ? text.substring(0, 100) + '...' : text
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN')
}

const getStatusText = (status) => {
  const statusMap = {
    planning: '筹备中',
    ongoing: '进行中',
    completed: '已结束'
  }
  return statusMap[status] || status
}

const getStatusTagType = (status) => {
  const typeMap = {
    planning: 'info',
    ongoing: 'success',
    completed: 'warning'
  }
  return typeMap[status] || ''
}

const handleClose = (done) => {
  done()
}
</script>

<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+SC:wght@300;400;500;700&display=swap');

.activity-container {
  font-family: 'Noto Sans SC', sans-serif;
  padding: 20px;
  background-color: #f8f9fa;
  min-height: 100vh;
}

/* 筛选卡片样式 */
.filter-card {
  border-radius: 12px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8eb 100%);
  border: none;

  .filter-form {
    display: flex;
    flex-wrap: wrap;
    align-items: center;

    .el-form-item {
      margin-bottom: 0;
      margin-right: 15px;
    }
  }
}

.search-input {
  width: 220px;

  :deep(.el-input__inner) {
    border-radius: 20px;
    padding-left: 35px;
    border: 1px solid #dcdfe6;
    transition: all 0.3s;

    &:hover {
      border-color: #c0c4cc;
    }

    &:focus {
      border-color: #409EFF;
      box-shadow: 0 0 8px rgba(64, 158, 255, 0.3);
    }
  }
}

.category-select, .status-select {
  width: 180px;

  :deep(.el-input__inner) {
    border-radius: 20px;
    transition: all 0.3s;
  }
}

.search-btn {
  border-radius: 20px;
  padding: 10px 20px;
  background-color: #4e97fd;
  border: none;
  transition: all 0.3s;

  &:hover {
    background-color: #3a86f6;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(78, 151, 253, 0.3);
  }

  &:active {
    transform: translateY(0);
  }
}

.reset-btn {
  border-radius: 20px;
  padding: 10px 20px;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  &:active {
    transform: translateY(0);
  }
}

/* 活动列表样式 */
.activity-list {
  margin-top: 20px;
}

.activity-item {
  margin-bottom: 20px;
  transition: transform 0.3s;

  &:hover {
    transform: translateY(-5px);
  }
}

.activity-card {
  border-radius: 12px;
  height: 100%;
  display: flex;
  flex-direction: column;
  transition: all 0.3s;
  border: none;
  overflow: hidden;

  &.hover-card {
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
    transform: translateY(-5px);
  }

  .activity-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;

    .activity-category {
      font-size: 12px;
      color: #909399;
      background-color: #f0f2f5;
      padding: 2px 8px;
      border-radius: 10px;
    }
  }

  .activity-title {
    font-size: 18px;
    font-weight: 500;
    color: #303133;
    margin: 0 0 10px 0;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .activity-image-placeholder {
    height: 120px;
    background-color: #f5f7fa;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 12px;
    color: #c0c4cc;
    font-size: 40px;
    transition: all 0.3s;

    &:hover {
      background-color: #e4e8eb;
    }
  }

  .activity-desc {
    font-size: 14px;
    color: #606266;
    line-height: 1.5;
    margin-bottom: 15px;
    flex-grow: 1;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .activity-dates {
    display: flex;
    align-items: center;
    font-size: 12px;
    color: #909399;
    margin-bottom: 15px;
    flex-wrap: wrap;

    .date-separator {
      margin: 0 5px;
    }
  }

  .activity-actions {
    display: flex;
    justify-content: space-between;

    .action-btn {
      flex: 1;
      margin: 0 3px;
      border-radius: 15px;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-2px);
      }

      &:active {
        transform: translateY(0);
      }
    }

    .detail-btn {
      background-color: #4e97fd;
      border: none;

      &:hover {
        background-color: #3a86f6;
        box-shadow: 0 4px 8px rgba(78, 151, 253, 0.3);
      }
    }

    .participate-btn {
      background-color: #67c23a;
      border: none;

      &:hover {
        background-color: #5daf34;
        box-shadow: 0 4px 8px rgba(103, 194, 58, 0.3);
      }
    }

    .help-btn {
      background-color: #e6a23c;
      border: none;

      &:hover {
        background-color: #d6992e;
        box-shadow: 0 4px 8px rgba(230, 162, 60, 0.3);
      }
    }
  }
}

/* 状态标签样式 */
.status-tag {
  font-weight: 500;
  border-radius: 10px;
  padding: 0 8px;

  &.status-planning {
    background-color: #f0f9eb;
    color: #67c23a;
    border-color: #e1f3d8;
  }

  &.status-ongoing {
    background-color: #ecf5ff;
    color: #409eff;
    border-color: #d9ecff;
  }

  &.status-completed {
    background-color: #fef0f0;
    color: #f56c6c;
    border-color: #fde2e2;
  }
}

/* 分页样式 */
.pagination {
  margin-top: 30px;
  text-align: center;
  padding: 15px 0;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

/* 对话框样式 */
.activity-dialog {
  :deep(.el-dialog) {
    border-radius: 12px;
    overflow: hidden;
  }

  :deep(.el-dialog__header) {
    background: linear-gradient(135deg, #4e97fd 0%, #3a86f6 100%);
    margin: 0;
    padding: 15px 20px;

    .el-dialog__title {
      color: white;
      font-weight: 500;
    }

    .el-dialog__headerbtn {
      top: 15px;

      .el-dialog__close {
        color: white;
      }
    }
  }

  :deep(.el-dialog__body) {
    padding: 20px;
  }
}

.activity-detail {
  .detail-label {
    font-weight: 500;
    color: #606266;
  }

  .category-tag {
    border-radius: 10px;
    padding: 0 10px;
  }

  .date-text {
    display: flex;
    align-items: center;

    i {
      margin-right: 5px;
    }
  }

  .description-content {
    line-height: 1.6;
    color: #606266;
    padding: 5px 0;
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;

  .dialog-btn {
    border-radius: 20px;
    padding: 10px 20px;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-2px);
    }

    &:active {
      transform: translateY(0);
    }
  }

  .close-btn {
    &:hover {
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }
  }

  .participate-btn {
    background-color: #67c23a;
    border: none;

    &:hover {
      background-color: #5daf34;
      box-shadow: 0 4px 8px rgba(103, 194, 58, 0.3);
    }
  }

  .help-btn {
    background-color: #e6a23c;
    border: none;

    &:hover {
      background-color: #d6992e;
      box-shadow: 0 4px 8px rgba(230, 162, 60, 0.3);
    }
  }
}

/* 空数据样式 */
.no-data {
  padding: 40px 0;
  text-align: center;

  .el-empty {
    :deep(.el-empty__description) {
      p {
        color: #909399;
        font-size: 16px;
      }
    }
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .filter-form {
    flex-direction: column;
    align-items: flex-start !important;

    .el-form-item {
      margin-right: 0 !important;
      margin-bottom: 10px !important;
      width: 100%;

      .el-input, .el-select {
        width: 100% !important;
      }
    }
  }

  .activity-actions {
    flex-direction: column;

    .el-button {
      width: 100%;
      margin: 5px 0 !important;
    }
  }

  .dialog-footer {
    flex-direction: column;

    .el-button {
      width: 100%;
      margin: 5px 0 !important;
    }
  }
}
</style>