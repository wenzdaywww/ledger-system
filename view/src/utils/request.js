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
    //查询用户账簿信息
    bookInfo: "api/book/info",
    //统计用户账簿信息
    bookCount: "api/book/tal",
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
    //查询我的所有店铺
    userShop: "api/shop/all",
    //修改我的店铺信息
    shopEdit: "api/shop/info",
    //新增店铺信息
    newShop: "api/shop/new",
    //删除店铺信息
    delShop: "api/shop/dlt/",
    //统计店铺销售额
    shopCount: "api/shop/tal",
    //查询年销售额列表
    yearList: "api/year/list",
    //统计年销售额
    yearCount: "api/year/tal",
    //查询月销售额列表
    monthList: "api/month/list",
    //统计月销售额
    monthCount: "api/month/tal",
    //删除月销售额信息
    delMonth: "api/month/dlt/",
    //编辑月销售额信息
    editMonth: "api/month/new",
    //查询订单信息列表
    orderList: "api/order/list",
    //编辑/新增订单信息
    editOrder: "api/order/new",
    //删除订单信息
    delOrder: "api/order/dlt/"
}