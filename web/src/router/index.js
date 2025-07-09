import { createRouter, createWebHistory } from 'vue-router'
import AuthView from '../views/AuthView.vue'
import Layout from "@/views/UserView/Home/Layout.vue";
import axios from "axios";
import {ElMessage} from "element-plus";

const routes = [
  {
    path: '/auth',
    name: 'auth',
    component: AuthView
  },
  {
    path: '/',
    name: 'home',
    component: () => import('@/components/UserPage/Index.vue'),
    meta: { title: '用户页面' },
  },
  {
    path: '/about',
    name: 'about',
    component: () => import('@/components/UserPage/about.vue'),
    meta: { title: '关于我们' },
  },
  {
    path: '/user',
    component: Layout,
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('../views/UserView/Home/Index.vue'),
        meta: { title: '慈善首页' }
      },
      {
        path: 'posts',
        name: 'Posts',
        component: () => import('../views/UserView/Home/Posts.vue'),
        meta: { title: '最新动态' }
      },
      {
        path: 'emergency',
        name: 'Emergency',
        component: () => import('../views/UserView/Home/Emergency.vue'),
        meta: { title: '紧急活动' }
      },
      {
        path: 'donate',
        name: 'Donate',
        component: () => import('../views/UserView/Donate.vue'),
        meta: { title: '我要捐赠' }
      },
      {
        path: 'assistance',
        name: 'Assistance',
        component: () => import('../views/UserView/Assistance.vue'),
        meta: { title: '我的受助' }
      },
      {
        path: '/user/user/announcement',
        name: 'UserAnnouncement',
        component: () => import('../views/UserView/UserAnnouncement.vue'),
        meta: { title: '最新公告' }
      },
    ]

  },
  {
    path: '/admin',
    name: 'admin',
    component: () => import('@/components/AdminManger/AdminMain.vue'),
    children: [
      {
        path: '',
        redirect: {name: 'Dashboard'},
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/AdminViews/Dashboard.vue'),
        meta: { title: '数据概览' }
      },
      {
        path: 'donations/list',
        name: 'DonationList',
        component: () => import('@/views/AdminViews/Donations.vue'),
        meta: { title: '捐赠记录' }
      },
      {
        path: 'donations/statistics',
        name: 'DonationStatistics',
        component: () => import('@/views/AdminViews/Donations.vue'),
        meta: { title: '捐赠统计' }
      },
      {
        path: 'beneficiaries',
        name: 'Beneficiaries',
        component: () => import('@/views/AdminViews/Beneficiaries.vue'),
        meta: { title: '受助者管理' }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/AdminViews/Settings.vue'),
        meta: { title: '系统设置' }
      },
      {
        path: 'activity/type',
        name: 'ActivityType',
        component: () => import('@/views/AdminPage/ActivityType.vue'),
        meta: { title: '类型管理' }
      },
      {
        path: 'activity/',
        name: 'Activity',
        component: () => import('@/views/AdminPage/Activity.vue'),
        meta: { title: '活动管理' }
      },
      {
        path: 'activity/participation',
        name: 'ActivityParticipation',
        component: () => import('@/views/AdminPage/ActivityParticipation.vue'),
        meta: { title: '活动管理' }
      },
      //捐赠管理
      {
        path: 'distributions',
        name: 'Distributions',
        component: () => import('@/views/AdminPage/Distributions.vue'),
        meta: { title: '活动管理' }
      },
      {
        path: 'item/donations',
        name: 'ItemDonations',
        component: () => import('@/views/AdminPage/ItemDonations.vue'),
        meta: { title: '物品捐赠管理' }
      },
      {
        path: 'money/donations',
        name: 'MoneyDonations',
        component: () => import('@/views/AdminPage/MoneyDonations.vue'),
        meta: { title: '资金捐赠管理' }
      },

      {
        path: '/admin/announcement',
        name: 'Announcement',
        component: () => import('@/views/AdminPage/Announcement.vue'),
        meta: { title: '公告管理' }
      },
      {
        path: 'volunteers',
        name: 'Volunteers',
        component: () => import('@/views/AdminViews/Volunteers.vue'),
        meta: {
          title: '志愿者管理',
          icon: 'User' // 用于侧边栏图标
        }
      },
      {
        path: '/admin/forum/attachments',
        name: 'ForumAttachments',
        component: () => import('@/views/AdminPage/ForumAttachments.vue'),
        meta: { title: '论坛附件' }
      },
      {
        path: '/admin/forum/categories',
        name: 'ForumCategories',
        component: () => import('@/views/AdminPage/ForumCategories.vue'),
        meta: { title: '论坛分页' }
      },
      {
        path: '/admin/forum/likes',
        name: 'ForumLikes',
        component: () => import('@/views/AdminPage/ForumLikes.vue'),
        meta: { title: '论坛喜欢' }
      },
      {
        path: '/admin/forum/posts',
        name: 'ForumPosts',
        component: () => import('@/views/AdminPage/ForumPosts.vue'),
        meta: { title: '论坛表' }
      },
      {
        path: '/admin/forum/replies',
        name: 'ForumReplies',
        component: () => import('@/views/AdminPage/ForumReplies.vue'),
        meta: { title: '论坛回复' }
      },
    ],
    meta: { title: '数据概览',requiresAuth: true },
  },

]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})
// router.beforeEach((to, from, next) => {
//   const isAuthenticated = localStorage.getItem("token") != null
//   if (!isAuthenticated && to.path.startsWith('/user')){
//     ElMessage.error("请你先进行登录!")
//   }
//   if (isAuthenticated && to.path.startsWith('/admin')) {
//     const user = JSON.parse(localStorage.getItem("user"))
//     router.push('/')
//     ElMessage.error("你没有资格访问本页面！您的IP不符合要求！")
//   }
//   next();
// });
export default router