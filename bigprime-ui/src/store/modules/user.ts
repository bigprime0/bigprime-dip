import { defineStore } from 'pinia'
import { SysUserService, type UserInfo } from '@/services/sys/sys-user'
import { setToken, removeToken, getUserId } from '@/utils/token'
import { useRouterStore } from '@/store/modules/router'
import { isEmpty } from 'lodash-es'


const useUserStore = defineStore('user', {
  state: () => {
    return {
      userInfo: {} as UserInfo
    }
  },

  actions: {
    async login(param: {}) {
      try {
        const res = await SysUserService.login(param)
        const { token, userInfo } = res.data
        setToken(token, userInfo.id, true)
        await this.setUser()
      } catch (err) {
        removeToken()
        throw err
      }
    },

    async save(user: UserInfo) {
      await SysUserService.save(user)
      if (user.id > 0 && user.id === this.userInfo.id) {
        this.$patch({ userInfo: user })
      }
    },

    async logout() {
      this.$reset()
      const routerStore = useRouterStore()
      routerStore.resetInfo()
      removeToken()
      localStorage.removeItem('token')
    },

    async setUser() {
      if (isEmpty(this.userInfo)) {
        const userId = getUserId()
        if (!isEmpty(userId)) {
          this.$patch({ userInfo: await SysUserService.getById(Number(userId)) })
        }
      }
    }
  }
})

export default useUserStore

