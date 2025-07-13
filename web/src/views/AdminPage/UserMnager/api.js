import axios from 'axios'


const api = {
    // 用户相关API
    getUsers: (params) => axios.get(`/users/page`, { params }),
    addUser: (data) => axios.post(`/users`, data),
    updateUser: (data) => axios.put(`/users`, data),
    deleteUser: (id) => axios.delete(`/users/${id}`),
    getUserById: (id) => axios.get(`/users/${id}`),

    // 用户档案相关API
    getProfile: (userId) => axios.get(`/user-profiles/${userId}`),
    addProfile: (data) => axios.post(`/user-profiles`, data),
    updateProfile: (data) => axios.put(`/user-profiles`, data),
    deleteProfile: (id) => axios.delete(`/user-profiles/${id}`)
}

export default api