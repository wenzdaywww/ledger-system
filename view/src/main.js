import {createApp} from 'vue';
import App from './App.vue';
import axios from './utils/axios';
import router from './router';
import store from './store';
import installElementPlus from './plugins/element';
import './assets/css/icon.css';
const app = createApp(App);
installElementPlus(app);
app.config.globalProperties.$http = axios;
app.use(store)
    .use(router)
    .mount('#app');