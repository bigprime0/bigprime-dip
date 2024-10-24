import localeCommon from '@/locale/common/en-US';

const allLocales = import.meta.glob('/src/views/**/locale/**/en-US.ts',{ eager: true }) || {}

const locales = {}
Object.assign(locales, localeCommon)
for (const path in allLocales) {
  let en:any = allLocales[path]
  Object.assign(locales, en.default)
}

export default locales;
