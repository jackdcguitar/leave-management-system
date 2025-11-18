import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 請求攔截器
api.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 響應攔截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    // 不要在攔截器自動跳轉，讓組件自己處理
    // if (error.response && error.response.status === 401) {
    //   window.location.href = '/login'
    // }
    return Promise.reject(error)
  }
)

// 認證 API
export const authAPI = {
  login: (username, password) =>
    api.post('/auth/login', { username, password }),
  logout: () =>
    api.post('/auth/logout'),
  getCurrentUser: () =>
    api.get('/auth/current-user')
}

// 請假 API
export const leaveAPI = {
  getMyLeaves: () =>
    api.get('/leaves'),
  applyLeave: (data) =>
    api.post('/leaves', data),
  updateLeaveStatus: (id, status) =>
    api.put(`/leaves/${id}/status`, { status })
}

// 加班 API
export const overtimeAPI = {
  getMyOvertimes: () =>
    api.get('/overtimes'),
  applyOvertime: (data) =>
    api.post('/overtimes', data)
}

// 使用者 API (管理員)
export const userAPI = {
  getAllUsers: () =>
    api.get('/admin/users'),
  createUser: (data) =>
    api.post('/admin/users', data),
  updateUser: (id, data) =>
    api.put(`/admin/users/${id}`, data)
}

export default api
