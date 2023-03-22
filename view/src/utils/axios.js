import axios from "axios";
import qs from "qs";
import router from '../router';
import {ElMessage} from 'element-plus';
import utils from './utils';
//创建请求对象
const http = axios.create();
//设置超时
http.defaults.timeout = 180000;
http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded;charset=UTF-8";
http.interceptors.request.use(
    config => {
        config.withCredentials = true;
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);
http.interceptors.response.use(
    response => {
        //接口返回403
        if ((response.data && (response.data.code == 401 || response.data.code == 403)) || response.status == 401 || response.status == 403) {
            utils.clearCookie();
            router.push("/");//首页页面
        }else if(response.status == 200){
            return Promise.resolve(response);
        }else {
            return Promise.reject();
        }
    },
    error => {
        if (error.response.status == 403){
            utils.clearCookie();
            router.push("/");//首页页面
        }else {
            ElMessage.error("请求失败");
            return Promise.reject();
        }
    }
);
export default {
    post(url, data) {
        return new Promise((resolve, reject) => {
            http({
                method: 'post',
                url,
                data: qs.stringify(data)
            }).then(res => {
                if(res && res.data){
                    resolve(res.data);
                }
            }).catch(err => {
                reject(err);
            });
        });
    },
    upload(url, data) {
        return new Promise((resolve, reject) => {
            http({
                method: 'post',
                url,
                data: data,
                headers : {'Content-Type': 'multipart/form-data'}
            }).then(res => {
                if(res && res.data){
                    resolve(res.data);
                }
            }).catch(err => {
                reject(err);
            });
        });
    },
    get(url,data) {
        return new Promise((resolve, reject) => {
            http({
                method: 'get',
                url,
                params: data,
            }).then(res => {
                if(res && res.data){
                    resolve(res.data);
                }
            }).catch(err => {
                reject(err);
            })
        });
    }
};