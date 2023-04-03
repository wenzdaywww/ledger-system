// 配置请求地址信息
export default {
    //公共请求
    //调用搜狐的请求获取IP地址
    getIp: "sohu/cityjson?ie=utf-8",
    //退出
    login: "api/login",
    //退出
    logout: "api/logout",
    //查询单个数据字典数据
    code: "api/code/code/",
    //查询多个数据字典数据
    codes: "api/code/codes",
    //修改用户信息
    editInfo: "api/user/edit",
    //查询用户信息
    modifyPwd: "api/user/pwd",
    //注册用户
    register: "api/new/user",
    //查询用户信息
    userInfo: "api/user/info",
    //查询用户路由
    userRouter: "api/user/router",

    //我的账簿请求
    //查询用户账簿信息
    bookInfo: "api/book/info",
    //统计用户账簿信息
    bookCount: "api/book/tal",
    //生成报表
    createReport: "api/book/exp",
    //查询报表
    docList: "api/book/doc",

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

    //年销售额
    //查询年销售额列表
    yearList: "api/year/list",
    //统计年销售额
    yearCount: "api/year/tal",

    //月销售额
    //查询月销售额列表
    monthList: "api/month/list",
    //统计月销售额
    monthCount: "api/month/tal",
    //删除月销售额信息
    delMonth: "api/month/dlt/",
    //编辑月销售额信息
    editMonth: "api/month/new",
    //增加/减少月销售额的推广费和服务费
    amtStep: "api/month/amt",

    //日销售额
    //查询日销售额列表
    dayList: "api/day/list",
    //统计日销售额
    dayCount: "api/day/tal",

    //订单信息
    //查询订单信息列表
    orderList: "api/order/list",
    //编辑/新增订单信息
    editOrder: "api/order/new",
    //删除订单信息
    delOrder: "api/order/dlt/",
    //导入订单信息
    importOrder: "api/order/ipt",

    //新增/修改我的支出信息
    payEdit: "api/pay/info",
    //删除我的支出信息
    payDel: "api/pay/dlt/",
    //查询支出信息列表
    payList: "api/pay/list",

    //修改我的商品信息
    goodsEdit: "api/goods/info",
    //删除店铺信息
    delGoods: "api/goods/dlt/",
    //查询商品列表
    goodsList: "api/goods/list",

    //图表查询
    //查询日销售利润图表数据
    daySales: "api/chart/daySales",
    //查询日订单量图表数据
    dayOrder: "api/chart/dayOrder",
    //查询月销售图表数据
    monthSales: "api/chart/monthSales",
    //查询月订单量图表数据
    monthOrder: "api/chart/monthOrder",
    //查询年销售图表数据
    yearSales: "api/chart/yearSales",
    //查询年订单量图表数据
    yearOrder: "api/chart/yearOrder",

}