import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'

export const useForumStore = defineStore('forum', () => {
    const posts = ref([])
    const categories = ref([])
    const currentPost = ref(null)
    const replies = ref([])

    const baseURL = '/forum'

    // 获取帖子列表
    const fetchPosts = async (params = {}) => {
        try {
            const response = await axios.get(`${baseURL}/posts/page`, { params })
            posts.value = response.data.data.records
            console.log(posts.value)
        } catch (error) {
            console.error('获取帖子列表失败:', error)
            throw error
        }
    }

    // 获取分类列表
    const fetchCategories = async () => {
        try {
            const response = await axios.get(`${baseURL}/categories/page`)
            categories.value = response.data.data.records
        } catch (error) {
            console.error('获取分类列表失败:', error)
        }
    }

    // 获取帖子详情
    const fetchPostById = async (postId) => {
        try {
            const response = await axios.get(`${baseURL}/posts/${postId}`)
            currentPost.value = response.data.data
            return response.data.data
        } catch (error) {
            console.error('获取帖子详情失败:', error)
            throw error
        }
    }

    // 获取回复列表
    const fetchReplies = async (postId) => {
        try {
            const response = await axios.get(`${baseURL}/replies/page`, {
                params: { postId }
            })
            replies.value = response.data.data.records
        } catch (error) {
            console.error('获取回复列表失败:', error)
        }
    }

    // 创建帖子
    const createPost = async (postData) => {
        try {
            const response = await axios.post(`${baseURL}/posts`, { post: postData })
            return response.data.data
        } catch (error) {
            console.error('创建帖子失败:', error)
            throw error
        }
    }

    // 创建回复
    const createReply = async (replyData) => {
        try {
            const response = await axios.post(`${baseURL}/replies`, { reply: replyData })
            return response.data.data
        } catch (error) {
            console.error('创建回复失败:', error)
            throw error
        }
    }

    // 点赞帖子
    const likePost = async (postId) => {
        try {
            const user = JSON.parse(localStorage.getItem('user'))
            await axios.post(`${baseURL}/likes`, {
                like: { userId: user.userId, postId }
            })
        } catch (error) {
            console.error('点赞失败:', error)
            throw error
        }
    }

    return {
        posts,
        categories,
        currentPost,
        replies,
        fetchPosts,
        fetchCategories,
        fetchPostById,
        fetchReplies,
        createPost,
        createReply,
        likePost
    }
})