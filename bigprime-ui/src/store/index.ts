import { createPinia } from 'pinia'
import useUserStore from './modules/user'
import useAppStore from './modules/app'

const pinia = createPinia()

export { useUserStore, useAppStore }
export default pinia
