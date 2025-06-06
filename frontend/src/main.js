import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 样式导入顺序很重要
import 'normalize.css'
import 'element-plus/dist/index.css'
import './styles/global.scss'

import App from './App.vue'
import router from './router'

import zhCn from 'element-plus/dist/locale/zh-cn.mjs' // 这里用 zhCn 接收

const app = createApp(App)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(createPinia())
app.use(router)
app.use(ElementPlus, {
  locale: zhCn, // 这里加上 locale 配置
  size: 'default',
  zIndex: 3000
})

app.mount('#app')
