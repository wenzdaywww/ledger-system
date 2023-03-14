export default [
    {
        path: "/",
        redirect: "/index"
    },{
        path: "/",
        name: "Home",
        component: () => import ("../views/index/Home.vue"),
        children: [
            {
                path: "/index",
                name: "ledgerIndex",
                meta: { title: '首页' },
                component: () => import ("../views/index/Index.vue")
            }
        ]
    },{
        path: "/login",
        name: "Login",
        meta: { title: '用户登录' },
        component: () => import ("../views/user/Login.vue")
    },{
        path: "/register",
        name: "Register",
        meta: { title: '用户注册' },
        component: () => import ("../views/user/Register.vue")
    },{
        path: "/",
        name: "Ledger",
        meta: {
            title: '店铺中心'
        },
        component: () => import ("../views/Home.vue"),
        children: [
            {
                path: "/book",
                name: "book",
                meta: { title: '我的账本' },
                component: () => import ("../views/book/book.vue")
            }, {
                path: "/shop",
                name: "shop",
                meta: { title: '我的店铺' },
                component: () => import ("../views/shop/ShopList.vue")
            }, {
                path: "/year",
                name: "year",
                meta: { title: '年销售额' },
                component: () => import ("../views/shop/ShopList.vue")
            }, {
                path: "/month",
                name: "month",
                meta: { title: '月销售额' },
                component: () => import ("../views/shop/ShopList.vue")
            }, {
                path: "/order",
                name: "order",
                meta: { title: '订单信息' },
                component: () => import ("../views/shop/ShopList.vue")
            }, {
                path: "/goods",
                name: "goods",
                meta: { title: '商品信息' },
                component: () => import ("../views/shop/ShopList.vue")
            }
        ]
    }, {
        path: "/403",
        name: "403",
        meta: { title: "403" },
        component: () => import ("../views/common/403.vue")
    },{
        path: "/404",
        name: "404",
        meta: { title: "404" },
        component: () => import ("../views/common/404.vue")
    },{
        path: "/:catchAll(.*)", // 页面404跳转
        redirect: "/404"
    }
]
