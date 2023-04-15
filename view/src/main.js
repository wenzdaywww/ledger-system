import {createApp} from 'vue';
import App from './App.vue';
import axios from './utils/axios';
import router from './router';
import ECharts  from 'vue-echarts'
import store from './store';
import installElementPlus from './plugins/element';
import './assets/css/icon.css';
const app = createApp(App);
installElementPlus(app);
app.config.globalProperties.$http = axios;
// 全局注册组件（也可以使用局部注册）
app.component('v-chart', ECharts)
app.use(store)
    .use(router)
    .mount('#app');