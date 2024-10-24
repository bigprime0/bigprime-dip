import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import TinyVue from '@opentiny/vue'
import i18n from './locale'
import App from './App.vue'
import router from './router/index'
import JsonViewer from 'vue3-json-viewer'
//import "vue3-json-viewer/dist/index.css";
import '@/assets/style/bigprime-json-theme.css'

const app = createApp(App)
app.use(createPinia())
app.use(TinyVue)
app.use(router)
app.use(i18n({ locale: 'zhCN' }))
app.use(JsonViewer)
app.mount('#app')
