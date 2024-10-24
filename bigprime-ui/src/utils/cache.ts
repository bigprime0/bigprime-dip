import { useStorage } from '@vueuse/core'

const sessionCache = {
  set: (key: string, value: any) => useStorage(key, value, sessionStorage, { mergeDefaults: true }),
  get: (key: string) => useStorage(key, sessionStorage),
  remove: (key: string) => sessionStorage.removeItem(key)
}
const localCache = {
  set: (key: string, value: any) => useStorage(key, value, localStorage, { mergeDefaults: true }),
  get: (key: string) => useStorage(key, localStorage),
  remove: (key: string) => localStorage.removeItem(key)
}

export default {
  session: sessionCache,
  local: localCache
}
