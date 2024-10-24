import NProgress from 'nprogress'
import type { RouteRecordRaw } from 'vue-router'
import { createRouter, createWebHashHistory, createWebHistory } from 'vue-router'
import { REDIRECT_PATH } from '@/config/setting'
import DefaultLayout from '@/layout/default-layout.vue'
import LoginView from '@/views/login/login.vue'
import { getToken } from '@/utils/token'
import { useRouterStore } from '@/store/modules/router'
import { isEmpty } from 'lodash-es'
import { useUserStore } from '@/store'


NProgress.configure({
  speed: 200,
  minimum: 0.02,
  trickleSpeed: 200,
  showSpinner: false
})

const homeRoutes = {
  name: 'home',
  path: import.meta.env.VITE_CONTEXT || '/home',
  component: DefaultLayout,
  children: [] as RouteRecordRaw[],
  meta: {
    requiresAuth: true
  }
}

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: {
      requiresAuth: false
    }
  },
  homeRoutes
]

//创建路由映射表、路由对象
const router = createRouter({
  //路由模式，分为hash与history，hash模式会在url中出现#,history模式不包含#看起来相对干净些
  //history: createWebHistory(import.meta.env.BASE_URL),
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})


//路由前置守卫,验证用户身份，权限等
//to: 即将进入目标路由对象，包含路径、参数、查询参数等
//from:当前导航正要离开的路由对象
router.beforeEach(async (to, from) => {
  const token = getToken()
  if (isEmpty(token) && to.name !== 'login') {
    return { name: 'login' }
  }
  if (!isEmpty(token)) {
    const home = router.getRoutes().filter(o => o.name === 'home' && !isEmpty(o.children))
    if (isEmpty(home)) {
      //动态路由
      const routerStore = useRouterStore()
      const userStore = useUserStore()
      await routerStore.setRouters()
      if (isEmpty(routerStore.routers)) {
        //没有菜单权限,直接退出
        await userStore.logout()
        return { name: 'login' }
      }
      await userStore.setUser()
      homeRoutes.children = routerStore.routers
      router.addRoute(homeRoutes)
      //其它数据

      const name = to.path.split('/')
      return { name: name[name.length - 1] }
    }
  }
})

router.afterEach(async to => {
  if (!to.path.includes(REDIRECT_PATH) && NProgress.isStarted()) {
    setTimeout(() => {
      NProgress.done(true)
    }, 200)
  }
})

export default router
