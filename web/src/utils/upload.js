import axios from 'axios'

/**
 * 上传图片
 * @param {File} file - 要上传的图片文件
 * @param {string} [userId] - 用户ID (可选)
 * @returns {Promise<string>} 返回图片URL
 */
export const uploadImage = async (file, userId = null) => {
    const formData = new FormData()
    formData.append('file', file)
    if (userId) formData.append('userId', userId)

    try {
        const response = await axios.post('/api/upload/image', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
        return response.data
    } catch (error) {
        console.error('图片上传失败:', error)
        throw error
    }
}

/**
 * 上传文件
 * @param {File} file - 要上传的文件
 * @param {string} [userId] - 用户ID (可选)
 * @returns {Promise<string>} 返回上传结果信息
 */
export const uploadFile = async (file, userId = null) => {
    const formData = new FormData()
    formData.append('file', file)
    if (userId) formData.append('userId', userId)

    try {
        const response = await axios.post('/api/upload/file', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
        return response.data
    } catch (error) {
        console.error('文件上传失败:', error)
        throw error
    }
}

/**
 * 获取图片完整URL
 * @param {string} path - 图片路径
 * @param {string} [userId] - 用户ID (可选)
 * @returns {string} 完整图片URL
 */
export const getImageUrl = (path, userId = null) => {
    if (!path) return ''
    if (path.startsWith('http')) return path
    return userId ? `/api/images/${userId}/${path}` : `/api/images/${path}`
}
/**
 * 创建论坛附件记录
 * @param {Object} attachmentData - 附件数据
 * @returns {Promise<Object>} 返回创建的附件记录
 */
export const createForumAttachment = async (attachmentData) => {
    try {
        const response = await axios.post('/forum/attachments', attachmentData)
        return response.data.data
    } catch (error) {
        console.error('创建附件记录失败:', error)
        throw error
    }
}
/**
 * 更新论坛附件记录
 * @param {number} id - 附件ID
 * @param {Object} attachmentData - 附件数据
 * @returns {Promise<Object>} 返回更新的附件记录
 */
export const updateForumAttachment = async (id, attachmentData) => {
    try {
        const response = await axios.put(`/forum/attachments/${id}`, attachmentData)
        return response.data.data
    } catch (error) {
        console.error('更新附件记录失败:', error)
        throw error
    }
}
/**
 * 获取论坛附件详情
 * @param {number} id - 附件ID
 * @returns {Promise<Object>} 返回附件详情
 */
export const getForumAttachment = async (id) => {
    try {
        const response = await axios.get(`/forum/attachments/${id}`)
        return response.data.data
    } catch (error) {
        console.error('获取附件详情失败:', error)
        throw error
    }
}

/**
 * 获取文件下载URL
 * @param {string} path - 文件路径
 * @param {string} userId - 用户ID
 * @returns {string} 文件下载URL
 */
export const getFileDownloadUrl = (path, userId) => {
    if (!path || !userId) return ''
    return `/api/files/${userId}/${path}`
}