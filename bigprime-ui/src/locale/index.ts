import { createI18n } from 'vue-i18n';
import locale from '@opentiny/vue-locale';
import en from './en-US';
import cn from './zh-CN';

export const LOCALE_OPTIONS = [
  { label: '简体中文', value: 'zhCN' },
  { label: 'English', value: 'enUS' },
];

const i18nmode = (option: any) => {
  option.legacy = false;
  option.globalInjection = true;
  option.silentTranslationWarn = true;
  return createI18n(option);
};




export default (i18n: any) =>
  locale.initI18n({
    i18n,
    createI18n: i18nmode,
    messages: {
      enUS: en,
      zhCN: cn,
    },
  });
