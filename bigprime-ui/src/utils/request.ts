import axios from 'axios'
import qs from 'qs'
import { getToken, removeToken } from '@/utils/token'
import { Modal } from '@opentiny/vue'
import { useUserStore } from '@/store'

// axios实例
const service = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  headers: { 'Content-Type': 'application/json;charset=UTF-8' }
})

/**
 * 添加请求拦截器
 */
service.interceptors.request.use((config: any) => {

    // 是否需要设置 token
    const isToken = (config.headers || {}).isToken === false

    //设置语言
    config.headers['Accept-Language'] = 'zh'

    //判断token
    if (getToken() && !isToken) {
      config.headers['Authorization'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }

    // 追加时间戳，防止GET请求缓存
    if (config.method?.toUpperCase() === 'GET') {
      config.params = { ...config.params, t: new Date().getTime() }
    }

    //form提交方式修改参数形式
    if (Object.values(config.headers).includes('application/x-www-form-urlencoded')) {
      config.data = qs.stringify(config.data)
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    if (response.status !== 200) {
      return Promise.reject(new Error(response.statusText || 'Error'))
    }

    if(response.headers['content-type'] === 'application/octet-stream'){
      return response;
    }

    const res = response.data

    // 响应成功
    if (response.headers['content-type'] === 'application/octet-stream' || res.code === 0 || res.code === 200) {
      return res
    }

    // 错误提示
    Modal.message({ message: res.msg, status: 'error' })
    // 没有权限，如：未登录、登录过期等，需要跳转到登录页
    if (res.code === 401) {
      useUserStore().logout()
    }

    return Promise.reject(new Error(res.msg || 'Error'))
  },
  error => {
    Modal.message({ message: error.message, status: 'error' })
    return Promise.reject(error)
  }
)
export default service


