import { useI18n } from 'vue-i18n';
import { LOCALE } from '@/config/setting'
import { isEmpty } from 'lodash-es'

export default function useLocale() {
  const i18 = useI18n()
  const changeLocale = (value: string) => {
    i18.locale.value = value;
    localStorage.setItem(LOCALE, value)
  }
  const getLocale = () => {
    const value = localStorage.getItem(LOCALE) as string
    if (!isEmpty(value)) {
      i18.locale.value = value
    }
    return i18.locale.value
  }

  return {
    changeLocale,
    getLocale
  };
}
