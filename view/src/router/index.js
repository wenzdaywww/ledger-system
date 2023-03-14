import {createRouter, createWebHistory} from "vue-router";
import routesDate from './routes';
import request from "../utils/request";
import store from "../store/index"
import axios from "../utils/axios"
const modules = import.meta.glob("../views/**/**.vue");// 接口请求
//路由信息
let routes = routesDate;
/**
 * 创建路由
 * @type {Router}
 */
const router = createRouter({
    history: createWebHistory(), //createWebHashHistory地址带#，createWebHistory不带#
    routes
});
/**
 * 获取用户的router信息
 * @param callBackFunc
 */
export const initUserRouter = function (callBackFunc){
    //加载用户拥有的路由权限
    axios.get(request.userInfo, null).then(function (res) {
        if(res.code === 200){
            //加载用户拥有的路由权限
            let routerTemp = ["/book","/shop","/year","/month","/order","/goods"];
            store.dispatch("updateRouter",routerTemp);
        }
        if (callBackFunc && callBackFunc instanceof  Function){
            callBackFunc();
        }
    });
}
/**
 * 清除用户的router信息
 */
export const clearUserRouter = function (){
    store.dispatch("updateRouter",null);
}
/**
 * 递归处理子路由
 * @deprecated 由于动态加载路由后，刷新页面会404，所以暂弃用
 * @param parent 父路由
 * @param children 子路由
 */
const hanleChilden = function (parent,children) {
    children.forEach (temp => {
        const vuePath = '../views/'+temp.vuePath;
        const tempRouter = {
            path: temp.menuUrl,
            name: temp.menuCode,
            meta : { title: temp.menuName },
            component : modules[vuePath],
            children: []
        };
        parent.push(tempRouter);
        if(temp.children){ // 有子router
            hanleChilden(tempRouter.children,temp.children);
        }
    });
}
/**
 * 路由跳转前的处理
 */
router.beforeEach((to, from, next) => {
    document.title = to.meta.title;
    let routerList = store.state.routerList;
    //跳转的路径在允许的routeer
    if (routerList.includes(to.path)){
        next();
    }else {
        //非刷新进入，则404
        if(from.name){
            next("/404");
        }else {
            initUserRouter(function (){
                let routerTemp = store.state.routerList;//获取允许的router
                if(routerTemp.includes(to.path)){
                    next();
                }else {
                    next("/404");
                }
            });
        }
    }
});

export default router ;
