<template>
  <el-menu
      :default-active="activeMenu"
      class="el-menu-vertical"
      :collapse="isCollapse"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409EFF"
      router
      unique-opened
  >
    <div class="sidebar-logo">
      <img src="../../assets/images/logo.png" alt="Logo" v-if="!isCollapse" style="height: 40px;">
      <img src="../../assets/images/logo.png" alt="Logo" v-else style="height: 30px;">
    </div>

    <!-- 所有路径前加上/admin前缀 -->
    <el-menu-item index="/admin/dashboard">
      <el-icon><pie-chart /></el-icon>
      <template #title>数据概览</template>
    </el-menu-item>

    <el-menu-item index="/admin/beneficiaries">
      <el-icon><user /></el-icon>
      <template #title>受助者管理</template>
    </el-menu-item>

    <el-menu-item index="/admin/settings">
      <el-icon><setting /></el-icon>
      <template #title>系统设置</template>
    </el-menu-item>

    <el-sub-menu index="activity">
      <template #title>
        <el-icon><coin /></el-icon>
        <span>活动管理</span>
      </template>
      <el-menu-item index="/admin/activity/type">活动类型</el-menu-item>
      <el-menu-item index="/admin/activity">活动管理</el-menu-item>
      <el-menu-item index="/admin/activity/participation">参与活动审核</el-menu-item>
    </el-sub-menu>

    <el-sub-menu index="distributions">
      <template #title>
        <el-icon><coin /></el-icon>
        <span>物资管理</span>
      </template>
      <el-menu-item index="/admin/distributions">物资分配管理</el-menu-item>
      <el-menu-item index="/admin/item/donations">物品捐赠管理</el-menu-item>
      <el-menu-item index="/admin/money/donations">资金捐赠管理</el-menu-item>
    </el-sub-menu>

    <el-sub-menu index="users">
      <template #title>
        <el-icon><user /></el-icon>
        <span>其他管理</span>
      </template>
      <el-menu-item index="/admin/volunteers">用户管理</el-menu-item>
      <el-menu-item index="/admin/announcement">公告管理</el-menu-item>
    </el-sub-menu>

    <el-sub-menu index="post">
      <template #title>
        <el-icon><user /></el-icon>
        <span>帖子管理</span>
      </template>
      <el-menu-item index="/admin/volunteers">帖子分类</el-menu-item>
      <el-menu-item index="/admin/volunteers">帖子管理</el-menu-item>
      <el-menu-item index="/admin/volunteers">帖子回复</el-menu-item>
      <el-menu-item index="/admin/volunteers">帖子点赞</el-menu-item>
      <el-menu-item index="/admin/volunteers">帖子收藏</el-menu-item>
      <el-menu-item index="/admin/volunteers">帖子附件</el-menu-item>
    </el-sub-menu>
  </el-menu>
</template>

<script>
import { PieChart, Coin, User, Setting } from '@element-plus/icons-vue'
import { computed } from 'vue'
import { useRoute } from 'vue-router'

export default {
  name: 'Sidebar',
  components: { PieChart, Coin, User, Setting },
  props: {
    isCollapse: Boolean
  },
  setup() {
    const route = useRoute()

    const activeMenu = computed(() => {
      return route.path
    })

    return { activeMenu }
  }
}
</script>

<style scoped>
.el-menu-vertical:not(.el-menu--collapse) {
  width: 200px;
}

.sidebar-logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #2b2f3a;
  overflow: hidden;
}

.el-menu {
  border-right: none;
}

.el-menu-item.is-active {
  background-color: #263445 !important;
}
</style>