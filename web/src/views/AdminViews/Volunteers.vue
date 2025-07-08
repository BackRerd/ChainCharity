<template>
  <div class="volunteer-container">
    <!-- 页面标题 -->
    <el-page-header title="返回" @back="$router.go(-1)">
      <template #content>
        <h1 class="page-title">
          <el-icon><User /></el-icon> 志愿者管理
        </h1>
      </template>
    </el-page-header>

    <!-- 内容区域 -->
    <el-card class="main-card">
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-input
            v-model="searchKey"
            placeholder="搜索志愿者姓名/手机号"
            style="width: 300px"
            clearable
        >
          <template #append>
            <el-button :icon="Search" />
          </template>
        </el-input>
        <el-button type="primary" :icon="Plus">新增志愿者</el-button>
      </div>

      <!-- 数据表格 -->
      <el-table :data="volunteerList" stripe style="width: 100%">
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="phone" label="联系方式" width="150" />
        <el-table-column prop="skills" label="技能" />
        <el-table-column prop="joinDate" label="加入日期" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { User, Search, Plus } from '@element-plus/icons-vue'

// 模拟数据
const volunteerList = ref([
  { id: 1, name: '张三', phone: '13800138000', skills: '医疗急救、心理咨询', joinDate: '2023-01-15' },
  { id: 2, name: '李四', phone: '13900139000', skills: '外语翻译', joinDate: '2023-02-20' }
])

const searchKey = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(2)

const handleEdit = (row) => {
  console.log('编辑', row)
}

const handleDelete = (row) => {
  console.log('删除', row)
}
</script>

<style scoped>
.volunteer-container {
  padding: 20px;
}

.page-title {
  color: var(--charity-dark);
  margin: 0;
}

.main-card {
  margin-top: 20px;
  border-radius: 8px;
  border-top: 3px solid var(--charity-secondary);
}

.search-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>