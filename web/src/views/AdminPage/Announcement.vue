<template>
  <div class="announcement-container">
    <!-- 顶部标题区域 -->
    <div class="header fade-in-down">
      <h2 class="title-gradient">公告管理</h2>
      <el-button
          type="primary"
          @click="showAddDialog"
          class="add-btn"
          icon="Plus"
      >
        新增公告
      </el-button>
    </div>

    <!-- 搜索区域 -->
    <div class="search-bar fade-in">
      <el-input
          v-model="searchTitle"
          placeholder="请输入公告标题"
          style="width: 300px"
          clearable
          @clear="handleSearch"
          @keyup.enter="handleSearch"
          class="search-input"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-button
          type="primary"
          @click="handleSearch"
          class="search-btn"
      >
        搜索
      </el-button>
    </div>

    <!-- 表格区域 -->
    <div class="table-container fade-in-up">
      <el-table
          :data="announcementList"
          border
          style="width: 100%"
          v-loading="loading"
          element-loading-text="加载中..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(255, 255, 255, 0.7)"
          :row-class-name="tableRowClassName"
          @row-click="handleRowClick"
      >
        <el-table-column prop="title" label="标题" width="180">
          <template #default="{ row }">
            <span class="title-text">{{ row.title }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="isTop" label="是否置顶" width="100" align="center">
          <template #default="{ row }">
            <el-tag
                :type="row.isTop === 1 ? 'danger' : 'info'"
                effect="dark"
                :class="{'pulse-animation': row.isTop === 1}"
            >
              {{ row.isTop === 1 ? '置顶' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" sortable>
          <template #default="{ row }">
            <div class="time-cell">
              <el-icon><Clock /></el-icon>
              <span>{{ formatDate(row.createTime) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="180" sortable>
          <template #default="{ row }">
            <div class="time-cell">
              <el-icon><Refresh /></el-icon>
              <span>{{ formatDate(row.updateTime) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="{ row }">
            <el-button
                size="small"
                @click.stop="handleEdit(row)"
                class="edit-btn"
                icon="Edit"
            >
              编辑
            </el-button>
            <el-button
                size="small"
                type="danger"
                @click.stop="handleDelete(row.id)"
                class="delete-btn"
                icon="Delete"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页区域 -->
    <div class="pagination fade-in-up">
      <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchAnnouncements"
          @current-change="fetchAnnouncements"
          background
          class="custom-pagination"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="isEdit ? '编辑公告' : '新增公告'"
        width="50%"
        :close-on-click-modal="false"
        class="announcement-dialog"
    >
      <el-form :model="form" label-width="100px" ref="formRef">
        <el-form-item label="公告标题" prop="title" required>
          <el-input
              v-model="form.title"
              placeholder="请输入公告标题"
              show-word-limit
              maxlength="50"
          />
        </el-form-item>
        <el-form-item label="是否置顶">
          <el-switch
              v-model="form.isTop"
              :active-value="1"
              :inactive-value="0"
              active-text="置顶"
              inactive-text="普通"
              inline-prompt
          />
        </el-form-item>
        <el-form-item label="公告内容" prop="content" required>
          <el-input
              v-model="form.content"
              type="textarea"
              :rows="10"
              placeholder="支持Markdown格式"
              show-word-limit
              maxlength="2000"
          />
          <div class="markdown-tip">
            <el-icon><InfoFilled /></el-icon>
            <span>支持Markdown语法</span>
          </div>
        </el-form-item>
        <el-form-item label="分类ID">
          <el-select
              v-model="form.categoryId"
              placeholder="请选择分类"
              clearable
              filterable
          >
            <el-option
                v-for="item in categoryOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
          <el-button
              type="primary"
              @click="handleSubmit"
              class="confirm-btn"
              :loading="submitting"
          >
            {{ isEdit ? '更新' : '发布' }}
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const announcementList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchTitle = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  id: '',
  title: '',
  content: '',
  isTop: 0,
  categoryId: ''
})

// 获取公告列表
const fetchAnnouncements = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      title: searchTitle.value
    }
    const response = await axios.get('/announcements/page', { params })
    announcementList.value = response.data.data.records
    total.value = response.data.data.total
  } catch (error) {
    ElMessage.error('获取公告列表失败')
    console.error(error)
  }
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString()
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchAnnouncements()
}

// 显示新增对话框
const showAddDialog = () => {
  isEdit.value = false
  form.value = {
    id: '',
    title: '',
    content: '',
    isTop: 0,
    categoryId: ''
  }
  dialogVisible.value = true
}

// 编辑公告
const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!form.value.title || !form.value.content) {
    ElMessage.warning('请填写标题和内容')
    return
  }

  try {
    if (isEdit.value) {
      await axios.put(`/announcements/${form.value.id}`, form.value)
      ElMessage.success('更新成功')
    } else {
      await axios.post('/announcements', form.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    await fetchAnnouncements()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '新增失败')
    console.error(error)
  }
}

// 删除公告
const handleDelete = (id) => {
  ElMessageBox.confirm('确认删除该公告吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
      .then(async () => {
        try {
          await axios.delete(`/announcements/${id}`)
          ElMessage.success('删除成功')
          await fetchAnnouncements()
        } catch (error) {
          ElMessage.error('删除失败')
          console.error(error)
        }
      })
      .catch(() => {})
}

onMounted(() => {
  fetchAnnouncements()
})
</script>

<style scoped>
.announcement-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 40px);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  animation: fadeInDown 0.5s ease-out;
}

.title-gradient {
  background: linear-gradient(to right, #409eff, #67c23a);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  font-size: 24px;
  font-weight: bold;
}

.add-btn {
  transition: all 0.3s;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.search-bar {
  display: flex;
  margin-bottom: 20px;
  gap: 10px;
  animation: fadeIn 0.5s ease-out 0.2s both;
}

.search-input {
  transition: all 0.3s;
}

.search-input:hover {
  box-shadow: 0 0 8px rgba(64, 158, 255, 0.2);
}

.search-btn {
  transition: all 0.3s;
}

.search-btn:hover {
  transform: translateY(-1px);
}

.table-container {
  background-color: #fff;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  animation: fadeInUp 0.5s ease-out 0.3s both;
}

.title-text {
  font-weight: 500;
  transition: color 0.2s;
}

.title-text:hover {
  color: #409eff;
  cursor: pointer;
}

.time-cell {
  display: flex;
  align-items: center;
  gap: 5px;
}

.edit-btn {
  transition: all 0.2s;
}

.edit-btn:hover {
  background-color: #ecf5ff;
  color: #409eff;
  transform: scale(1.05);
}

.delete-btn {
  transition: all 0.2s;
}

.delete-btn:hover {
  background-color: #fef0f0;
  color: #f56c6c;
  transform: scale(1.05);
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  animation: fadeInUp 0.5s ease-out 0.4s both;
}

.custom-pagination {
  background-color: #fff;
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.announcement-dialog {
  border-radius: 12px;
}

.markdown-tip {
  margin-top: 5px;
  font-size: 12px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 5px;
}

.cancel-btn {
  transition: all 0.2s;
}

.cancel-btn:hover {
  transform: translateX(-3px);
}

.confirm-btn {
  transition: all 0.2s;
}

.confirm-btn:hover {
  transform: translateX(3px);
}

/* 自定义动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.fade-in {
  animation: fadeIn 0.5s ease-out;
}

.fade-in-down {
  animation: fadeInDown 0.5s ease-out;
}

.fade-in-up {
  animation: fadeInUp 0.5s ease-out;
}

/* 脉冲动画效果 */
.pulse-animation {
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

/* 表格行效果 */
:deep(.el-table__row) {
  transition: all 0.3s;
}

:deep(.el-table__row:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}
</style>