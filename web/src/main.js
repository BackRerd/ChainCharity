import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'
import '@/assets/variables.css'
import axios from "axios";
import {createPinia} from "pinia";

const app = createApp(App)
const pinia = createPinia()

axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL;
// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(router)
app.use(ElementPlus)
app.use(pinia)

app.mount('#app')
