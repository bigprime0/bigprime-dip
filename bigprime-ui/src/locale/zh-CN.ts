import localeCommon from '@/locale/common/zh-CN';

const allLocales = import.meta.glob('/src/views/**/locale/**/zh-CN.ts', { eager: true }) || {}

const locales = {}
Object.assign(locales, localeCommon)
for (const path in allLocales) {
  let cn:any = allLocales[path]
  Object.assign(locales, cn.default)
}

export default locales;
