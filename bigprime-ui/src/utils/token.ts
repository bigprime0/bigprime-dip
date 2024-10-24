/**
 * token 操作封装
 */
import { TOKEN_STORE_NAME, USER_ID } from '@/config/setting'

/**
 * 获取缓存的 token
 */
export const getToken = () => {
  const token = localStorage.getItem(TOKEN_STORE_NAME)
  if (!token) {
    return sessionStorage.getItem(TOKEN_STORE_NAME)
  }
  return token
}

/**
 * 获取缓存的用户ID
 */
export const getUserId =() => {
  const userId = localStorage.getItem(USER_ID)
  if (!userId) {
    return sessionStorage.getItem(USER_ID)
  }
  return userId
}

/**
 * 缓存 token
 * @param token token
 * @param userId 用户ID
 * @param remember 是否永久存储
 */
export const setToken = (token: string, userId: string, remember: boolean) => {
  removeToken()
  if (token) {
    if (remember) {
      localStorage.setItem(TOKEN_STORE_NAME, token)
      localStorage.setItem(USER_ID, userId)
    } else {
      sessionStorage.setItem(TOKEN_STORE_NAME, token)
      sessionStorage.setItem(USER_ID, userId)
    }
  }
}

/**
 * 移除 token
 */
export const removeToken = () => {
  localStorage.removeItem(TOKEN_STORE_NAME)
  localStorage.removeItem(USER_ID)
  sessionStorage.removeItem(TOKEN_STORE_NAME)
  sessionStorage.removeItem(USER_ID)
  sessionStorage.removeItem('ACTIVE_APP')
}
