import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 100000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['token'] = `${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 直接返回响应数据
    return response.data
  },
  error => {
    console.error('Response Error:', error.response)
    const { response } = error
    let message = '网络错误，请稍后重试'

    if (response) {
      message = response.data?.msg || '请求失败'
    }

    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default request
