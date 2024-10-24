import { defineStore } from 'pinia'
import { isEmpty, toLower } from 'lodash-es'
import type { RouteRecordRaw } from 'vue-router'
import { type MenuInfo, SysMenuService } from '@/services/sys/sys-menu'

export interface RouterDes {
  id: number,
  pid:number,
  name: string,
  url: string,
  icon: string,
  urlDes: string,
  pathDes: string,
  nameDes: string
}

export interface MenuSelect {
  id: number,
  label: string,
  children: MenuSelect[]
}

const allModules = import.meta.glob('/src/views/**/*.vue')
const getComponent = (url: string) => {
  const component = allModules[`/src/views/${url}.vue`]
  return component
}

export const useRouterStore = defineStore('myRouter', {
  state: () => ({
    routers: [] as RouteRecordRaw[],
    routerDes: [] as RouterDes[],
    filter: [] as string[],
    menuSelects: [] as MenuSelect[]
  }),

  actions: {
    async getRouters() {
      if (isEmpty(this.routers)) {
        await this.setRouters()
      }
      return [...this.routers]
    },

    async saveMenu(data:MenuInfo){
      await SysMenuService.save(data)
      await this.setRouters()
    },

    async deleteMenu(id:number){
      await SysMenuService.deleteById(id)
      await this.setRouters()
    },

    //设置路由集合信息
    async setRouters() {
      const menus = await SysMenuService.getMenus()
      const routers = [] as RouteRecordRaw[]
      const parentDes = {
        nameDes: 'menu'
      } as RouterDes
      const routerDes = [] as RouterDes[]
      const filter = [] as string[]

      const menuSelects = [] as MenuSelect[]

      menus.forEach(menu => {
        const result = this.getRouter(menu, parentDes)
        routers.push(result.router)
        routerDes.push(...result.routerDesList)
        filter.push(menu.name)
        menuSelects.push(result.menuSelect)
      })
      this.routers = routers
      this.routerDes = routerDes
      this.filter = filter
      this.menuSelects = menuSelects
    },

    //获取路由信息
    getRouter(menu: MenuInfo, parentDes: RouterDes) {
      let component
      if (!isEmpty(menu.url)) {
        component = getComponent(menu.url)
      }

      const router = {
        path: menu.name,
        name: menu.name,
        id: menu.name,
        label: `${parentDes.nameDes}.${toLower(menu.name)}`,
        component: component,
        meta: {
          requiresAuth: true,
          order: menu.sort
        },
        children: []
      } as RouteRecordRaw

      const routerDes = {
        id: menu.id,
        pid: menu.pid,
        name: menu.name,
        url: menu.url,
        icon: menu.icon,
        urlDes: !isEmpty(menu.url) ? `@/views/${menu.url}.vue` : '',
        pathDes: isEmpty(parentDes.pathDes) ? `${menu.name}` : `${parentDes.pathDes}/${menu.name}`,
        nameDes: `${parentDes.nameDes}.${toLower(menu.name)}`
      } as RouterDes
      const routerDesList = [] as RouterDes[]
      routerDesList.push(routerDes)

      const menuSelect = {
        id: menu.id,
        label: `${parentDes.nameDes}.${toLower(menu.name)}`,
        children: []
      } as MenuSelect

      if (!isEmpty(menu.children)) {
        menu.children.forEach(childMenu => {
          const child = this.getRouter(childMenu, routerDes)
          router.children?.push(child.router)
          routerDesList.push(...child.routerDesList)
          menuSelect.children?.push(child.menuSelect)
        })
      }
      return { router: router, routerDesList: routerDesList, menuSelect: menuSelect }
    },

    resetInfo() {
      this.$reset()
    }
  }
})

