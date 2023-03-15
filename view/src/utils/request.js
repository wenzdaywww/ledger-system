// 配置请求地址信息
export default {
    //调用搜狐的请求获取IP地址
    getIp: "sohu/cityjson?ie=utf-8",
    //退出
    login: "api/login",
    //退出
    logout: "api/logout",
    //注册用户
    register: "api/new/user",
    //查询用户信息
    userInfo: "api/user/info",
    //查询用户路由
    userRouter: "api/user/router",
    //修改用户信息
    editInfo: "api/user/edit",
    //查询用户信息
    modifyPwd: "api/user/pwd",
    //查询单个数据字典数据
    code: "api/code/code/",
    //查询多个数据字典数据
    codes: "api/code/codes",
    //查询我的店铺列表
    shopList: "api/shop/list",
    //修改我的店铺信息
    shopEdit: "api/shop/info",
    //新增店铺信息
    newShop: "api/shop/new",
    //删除店铺信息
    delShop: "api/shop/dlt/"
}