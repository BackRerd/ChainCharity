<template>
  <div class="forum-posts-container">
    <!-- 搜索和筛选区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input-number v-model="searchForm.userId" :min="1" controls-position="right" clearable />
        </el-form-item>
        <el-form-item label="分类">
          <el-select
              v-model="searchForm.categoryId"
              placeholder="请选择分类"
              clearable
              filterable
              remote
              :remote-method="searchCategories"
              :loading="categoryLoading"
              @focus="loadCategories"
          >
            <el-option
                v-for="category in categoryOptions"
                :key="category.categoryId"
                :label="category.name"
                :value="category.categoryId"
            />
            <el-pagination
                small
                layout="prev, pager, next"
                :total="categoryTotal"
                :page-size="categoryPageSize"
                :current-page="categoryCurrentPage"
                @current-change="handleCategoryPageChange"
                @size-change="handleCategorySizeChange"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="已发布" value="published" />
            <el-option label="待审核" value="pending" />
            <el-option label="已删除" value="deleted" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否置顶">
          <el-switch
              v-model="formData.isSticky"
              :active-value="1"
              :inactive-value="0"
              active-text="置顶"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮区域 -->
    <el-card shadow="never" class="operation-card">
      <el-button type="primary" @click="handleCreate">
        <el-icon><plus /></el-icon>新增帖子
      </el-button>
      <el-button type="danger" :disabled="!selectedIds.length" @click="handleBatchDelete">
        <el-icon><delete /></el-icon>批量删除
      </el-button>
    </el-card>

    <!-- 表格区域 -->
    <el-card shadow="never">
      <el-table
          v-loading="loading"
          :data="tableData"
          border
          style="width: 100%"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="postId" label="ID" width="80" />
        <el-table-column prop="title" label="标题" width="200">
          <template #default="{ row }">
            <el-link type="primary" @click="handleView(row.postId)">{{ row.title }}</el-link>
            <el-tag v-if="row.isSticky === 1" type="warning" size="small" style="margin-left: 5px">置顶</el-tag>
            <el-tag v-if="row.isEssence === 1" type="success" size="small" style="margin-left: 5px">精华</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="分类" width="120">
          <template #default="{ row }">
            {{ getCategoryName(row.categoryId) }}
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览数" width="100" />
        <el-table-column prop="replyCount" label="回复数" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">
              {{ statusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row.postId)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.postId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="fetchData"
            @current-change="fetchData"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="80%"
        top="5vh"
        @closed="resetForm"
    >
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select
              v-model="formData.categoryId"
              placeholder="请选择分类"
              filterable
              remote
              :remote-method="searchCategories"
              :loading="categoryLoading"
              @focus="loadCategories"
          >
            <el-option
                v-for="category in categoryOptions"
                :key="category.categoryId"
                :label="category.name"
                :value="category.categoryId"
            />
            <el-pagination
                small
                layout="prev, pager, next"
                :total="categoryTotal"
                :page-size="categoryPageSize"
                :current-page="categoryCurrentPage"
                @current-change="handleCategoryPageChange"
                @size-change="handleCategorySizeChange"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否置顶">
          <el-switch
              v-model="formData.isSticky"
              :active-value="1"
              :inactive-value="0"
              active-text="置顶"
          />
        </el-form-item>
        <el-form-item label="是否精华">
          <el-switch
              v-model="formData.isEssence"
              :active-value="1"
              :inactive-value="0"
              active-text="精华"
          />
        </el-form-item>
        <el-form-item label="是否关闭">
          <el-switch
              v-model="formData.isClosed"
              :active-value="1"
              :inactive-value="0"
              active-text="关闭回复"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio label="published">已发布</el-radio>
            <el-radio label="pending">待审核</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <div class="markdown-editor">
            <el-tabs v-model="activeTab" type="border-card">
              <el-tab-pane name="edit" label="编辑">
                <el-input
                    v-model="formData.content"
                    type="textarea"
                    :rows="15"
                    placeholder="请输入内容，支持Markdown语法"
                />
              </el-tab-pane>
              <el-tab-pane name="preview" label="预览">
                <div class="markdown-preview" v-html="markdownToHtml(formData.content)" />
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog
        v-model="detailDialogVisible"
        title="帖子详情"
        width="80%"
        top="5vh"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="ID">{{ currentPost.postId }}</el-descriptions-item>
        <el-descriptions-item label="标题">{{ currentPost.title }}</el-descriptions-item>
        <el-descriptions-item label="分类">{{ getCategoryName(currentPost.categoryId) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusTagType(currentPost.status)">
            {{ statusText(currentPost.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="浏览数">{{ currentPost.viewCount }}</el-descriptions-item>
        <el-descriptions-item label="回复数">{{ currentPost.replyCount }}</el-descriptions-item>
        <el-descriptions-item label="是否置顶">
          <el-tag :type="currentPost.isSticky ? 'success' : 'info'">
            {{ currentPost.isSticky ? '是' : '否' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="是否精华">
          <el-tag :type="currentPost.isEssence ? 'success' : 'info'">
            {{ currentPost.isEssence ? '是' : '否' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="是否关闭回复">
          <el-tag :type="currentPost.isClosed ? 'danger' : 'success'">
            {{ currentPost.isClosed ? '是' : '否' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDate(currentPost.createdAt) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatDate(currentPost.updatedAt) }}</el-descriptions-item>
        <el-descriptions-item label="内容" :span="2">
          <div class="markdown-preview" v-html="markdownToHtml(currentPost.content)" />
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import axios from 'axios'
import { marked } from 'marked'
import DOMPurify from 'dompurify'

// 基础API路径
const API_BASE = '/forum/posts'
const CATEGORY_API = '/forum/categories'

// 表格数据
const tableData = ref([])
const loading = ref(false)
const selectedIds = ref([])

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 搜索表单
const searchForm = reactive({
  title: '',
  userId: null,
  categoryId: null,
  status: '',
  isSticky: null
})

// 对话框相关
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const activeTab = ref('edit')

// 表单数据
const formData = reactive({
  postId: null,
  title: '',
  categoryId: null,
  content: '',
  isSticky: 0,
  isEssence: 0,
  isClosed: 0,
  status: 'published'
})

// 当前查看的帖子
const currentPost = reactive({
  postId: null,
  title: '',
  categoryId: null,
  content: '',
  viewCount: 0,
  replyCount: 0,
  isSticky: 0,
  isEssence: 0,
  isClosed: 0,
  status: 'published',
  createdAt: null,
  updatedAt: null
})

// 分类相关
const categoryOptions = ref([])
const categoryLoading = ref(false)
const categoryTotal = ref(0)
const categoryCurrentPage = ref(1)
const categoryPageSize = ref(10)
const categorySearchKeyword = ref('')

// 表单验证规则
const rules = reactive({
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

// 获取分类名称
const getCategoryName = (categoryId) => {
  const category = categoryOptions.value.find(item => item.categoryId === categoryId)
  return category ? category.name : '未知分类'
}

// 状态标签类型
const statusTagType = (status) => {
  const types = {
    published: 'success',
    pending: 'warning',
    deleted: 'danger'
  }
  return types[status] || ''
}

// 状态文本
const statusText = (status) => {
  const texts = {
    published: '已发布',
    pending: '待审核',
    deleted: '已删除'
  }
  return texts[status] || status
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

// Markdown转HTML
const markdownToHtml = (markdown) => {
  if (!markdown) return ''
  const html = marked(markdown)
  return DOMPurify.sanitize(html)
}

// 加载分类数据
const loadCategories = async (keyword = '', page = 1, size = 10) => {
  categoryLoading.value = true
  try {
    const params = {
      pageNum: page,
      pageSize: size
    }

    if (keyword) {
      params.name = keyword
    }

    const response = await axios.get(`${CATEGORY_API}/page`, { params })
    if (response.data && response.data.data) {
      categoryOptions.value = response.data.data.records
      categoryTotal.value = response.data.data.total
    }
  } catch (error) {
    console.error('获取分类失败:', error)
    ElMessage.error('获取分类失败')
  } finally {
    categoryLoading.value = false
  }
}

// 搜索分类
const searchCategories = (query) => {
  categorySearchKeyword.value = query
  loadCategories(query, 1, categoryPageSize.value)
}

// 分类分页变化
const handleCategoryPageChange = (page) => {
  categoryCurrentPage.value = page
  loadCategories(categorySearchKeyword.value, page, categoryPageSize.value)
}

// 分类分页大小变化
const handleCategorySizeChange = (size) => {
  categoryPageSize.value = size
  loadCategories(categorySearchKeyword.value, 1, size)
}

// 获取帖子数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.current,
      pageSize: pagination.size,
      ...searchForm
    }

    // 移除值为null或空字符串的参数
    Object.keys(params).forEach(key => {
      if (params[key] === null || params[key] === '') {
        delete params[key]
      }
    })

    const response = await axios.get(`${API_BASE}/page`, { params })
    if (response.data && response.data.data) {
      tableData.value = response.data.data.records
      pagination.total = response.data.data.total
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchData()
}

// 重置搜索
const resetSearch = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = key === 'title' || key === 'status' ? '' : null
  })
  handleSearch()
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.postId)
}

// 新增帖子
const handleCreate = () => {
  dialogTitle.value = '新增帖子'
  dialogVisible.value = true
}

// 编辑帖子
const handleEdit = async (id) => {
  try {
    const response = await axios.get(`${API_BASE}/${id}`)
    if (response.data && response.data.data) {
      const postData = response.data.data
      Object.assign(formData, {
        ...postData,
        isSticky: postData.isSticky === true ? 1 : (postData.isSticky || 0),
        isEssence: postData.isEssence === true ? 1 : (postData.isEssence || 0),
        isClosed: postData.isClosed === true ? 1 : (postData.isClosed || 0)
      })
      dialogTitle.value = '编辑帖子'
      dialogVisible.value = true
    }
  } catch (error) {
    console.error('获取帖子详情失败:', error)
    ElMessage.error('获取帖子详情失败')
  }
}

// 查看帖子详情
const handleView = async (id) => {
  try {
    const response = await axios.get(`${API_BASE}/${id}`)
    if (response.data && response.data.data) {
      const postData = response.data.data
      Object.assign(currentPost, {
        ...postData,
        isSticky: postData.isSticky || 0,
        isEssence: postData.isEssence || 0,
        isClosed: postData.isClosed || 0
      })
      detailDialogVisible.value = true
    }
  } catch (error) {
    console.error('获取帖子详情失败:', error)
    ElMessage.error('获取帖子详情失败')
  }
}

// 删除帖子
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该帖子吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`${API_BASE}/${id}`)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 批量删除
const handleBatchDelete = () => {
  if (!selectedIds.value.length) {
    ElMessage.warning('请选择要删除的帖子')
    return
  }

  ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 条帖子吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await Promise.all(selectedIds.value.map(id => axios.delete(`${API_BASE}/${id}`)))
      ElMessage.success('删除成功')
      selectedIds.value = []
      fetchData()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate()

    // 确保数值类型字段正确
    const submitData = {
      ...formData,
      isSticky: formData.isSticky || 0,
      isEssence: formData.isEssence || 0,
      isClosed: formData.isClosed || 0
    }

    if (formData.postId) {
      // 编辑
      await axios.put(`${API_BASE}/${formData.postId}`, submitData)
      ElMessage.success('更新成功')
    } else {
      // 新增
      await axios.post(API_BASE, submitData)
      ElMessage.success('新增成功')
    }

    dialogVisible.value = false
    fetchData()
  } catch (error) {
    if (error.response && error.response.data) {
      ElMessage.error(error.response.data.message || '操作失败')
    } else if (error.name !== 'ValidateError') {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(formData, {
    postId: null,
    title: '',
    categoryId: null,
    content: '',
    isSticky: 0,
    isEssence: 0,
    isClosed: 0,
    status: 'published'
  })
}

// 初始化
onMounted(() => {
  fetchData()
  loadCategories()
})
</script>

<style scoped>
.forum-posts-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.operation-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.markdown-editor {
  width: 100%;
}

.markdown-preview {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  min-height: 200px;
}

.markdown-preview :deep(h1) {
  font-size: 2em;
  margin: 0.67em 0;
  border-bottom: 1px solid #eee;
  padding-bottom: 0.3em;
}

.markdown-preview :deep(h2) {
  font-size: 1.5em;
  margin: 0.83em 0;
  border-bottom: 1px solid #eee;
  padding-bottom: 0.3em;
}

.markdown-preview :deep(pre) {
  background-color: #f6f8fa;
  border-radius: 3px;
  padding: 16px;
  overflow: auto;
}

.markdown-preview :deep(code) {
  background-color: rgba(27, 31, 35, 0.05);
  border-radius: 3px;
  padding: 0.2em 0.4em;
  font-family: SFMono-Regular, Consolas, Liberation Mono, Menlo, monospace;
}

.markdown-preview :deep(blockquote) {
  border-left: 4px solid #dfe2e5;
  color: #6a737d;
  padding: 0 1em;
  margin: 0 0 16px 0;
}

.markdown-preview :deep(table) {
  border-collapse: collapse;
  width: 100%;
  margin-bottom: 16px;
}

.markdown-preview :deep(th),
.markdown-preview :deep(td) {
  border: 1px solid #dfe2e5;
  padding: 6px 13px;
}

.markdown-preview :deep(tr) {
  background-color: #fff;
  border-top: 1px solid #c6cbd1;
}

.markdown-preview :deep(tr:nth-child(2n)) {
  background-color: #f6f8fa;
}

/* 分类选择器的分页样式 */
.el-select-dropdown .el-pagination {
  padding: 10px;
  text-align: center;
}
</style>