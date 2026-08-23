import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import 'ant-design-vue/dist/reset.css' // 注意：如果是 Ant Design Vue 4.x 版本，用这个
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')
