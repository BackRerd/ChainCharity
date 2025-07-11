import axios from 'axios';
import { ElMessage } from 'element-plus';

class FileUploader {
    constructor(options = {}) {
        this.baseURL = options.baseURL || '';
        this.defaultHeaders = {
            'Authorization': options.token ? `Bearer ${options.token}` : '',
            ...options.headers
        };
        this.maxImageSize = options.maxImageSize || 2 * 1024 * 1024; // 2MB
        this.maxFileSize = options.maxFileSize || 10 * 1024 * 1024; // 10MB
    }

    /**
     * 设置认证token
     * @param {string} token
     */
    setToken(token) {
        this.defaultHeaders['Authorization'] = `Bearer ${token}`;
    }

    /**
     * 上传图片
     * @param {File} file
     * @param {object} options
     * @returns {Promise<string>} 图片URL
     */
    async uploadImage(file, options = {}) {
        try {
            this._validateFile(file, 'image');

            const formData = new FormData();
            formData.append('file', file);
            if (options.userId) formData.append('userId', options.userId);

            const response = await axios.post(`${this.baseURL}/api/upload/image`, formData, {
                headers: {
                    ...this.defaultHeaders,
                    'Content-Type': 'multipart/form-data'
                },
                onUploadProgress: options.onProgress
            });

            return `${this.baseURL}${response.data}`;
        } catch (error) {
            this._handleError(error, '图片上传失败');
            throw error;
        }
    }

    /**
     * 上传文件
     * @param {File} file
     * @param {object} options
     * @returns {Promise<string>} 文件名
     */
    async uploadFile(file, options = {}) {
        try {
            this._validateFile(file, 'file');

            const formData = new FormData();
            formData.append('file', file);
            if (options.userId) formData.append('userId', options.userId);

            const response = await axios.post(`${this.baseURL}/api/upload/file`, formData, {
                headers: {
                    ...this.defaultHeaders,
                    'Content-Type': 'multipart/form-data'
                },
                onUploadProgress: options.onProgress
            });

            return response.data;
        } catch (error) {
            this._handleError(error, '文件上传失败');
            throw error;
        }
    }

    /**
     * 获取图片URL
     * @param {string} filename
     * @param {string} userId
     * @returns {string}
     */
    getImageUrl(filename, userId = '') {
        return userId
            ? `${this.baseURL}/api/images/${userId}/${filename}`
            : `${this.baseURL}/api/images/${filename}`;
    }

    /**
     * 下载文件
     * @param {string} filename
     * @param {string} userId
     * @returns {Promise<void>}
     */
    async downloadFile(filename, userId) {
        try {
            if (!filename || !userId) {
                throw new Error('文件名和用户ID不能为空');
            }

            const response = await axios.get(`${this.baseURL}/api/files/${userId}/${filename}`, {
                responseType: 'blob',
                headers: this.defaultHeaders
            });

            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        } catch (error) {
            this._handleError(error, '文件下载失败');
            throw error;
        }
    }

    // 私有方法
    _validateFile(file, type) {
        const isImage = type === 'image';
        const maxSize = isImage ? this.maxImageSize : this.maxFileSize;
        const typeName = isImage ? '图片' : '文件';

        if (!file) {
            throw new Error('请选择文件');
        }

        if (isImage && !['image/jpeg', 'image/png'].includes(file.type)) {
            throw new Error('只能上传JPG/PNG格式的图片');
        }

        if (file.size > maxSize) {
            throw new Error(`${typeName}大小不能超过${maxSize / 1024 / 1024}MB`);
        }
    }

    _handleError(error, defaultMessage) {
        console.error('上传错误:', error);

        let message = defaultMessage;
        if (error.response) {
            message += `: ${error.response.status} - ${error.response.data?.message || '服务器错误'}`;
        } else if (error.message) {
            message += `: ${error.message}`;
        }

        ElMessage.error(message);
    }
}

// 创建默认实例
const fileUploader = new FileUploader({
    baseURL: import.meta.env.VITE_API_BASE_URL || ''
});

export { FileUploader, fileUploader as default };