<!-- 订单信息列表 -->
<template>
  <div>
    <div class="crumb-title">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-shopping-cart-full"></i> 订单信息
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-card>
      <div>
        <!-- 店铺信息列表查询条件-->
        <div class="handle-box">
          <div class="block" style="float: left; margin-right: 10px;">
            <el-date-picker v-model="query.ordDat" style="width:135px;" format="YYYY-MM-DD" value-format="YYYY-MM-DD" type="date" placeholder="选择日期"></el-date-picker>
          </div>
          <el-input v-model="query.ordId" placeholder="订单ID" class="handle-input mr10" style="width: 180px"></el-input>
          <el-select v-model="query.shopId" placeholder="请选择店铺" class="handle-select mr10" style="width: 200px">
            <el-option v-for="item in userShop" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
          <el-input v-model="query.supId" placeholder="1688订单ID" class="handle-input mr10" style="width: 180px"></el-input>
          <el-input v-model="query.gdsId" placeholder="商品ID" class="handle-input mr10" style="width: 180px"></el-input>
          <el-select v-model="query.ordSta" placeholder="请选择订单状态" class="handle-select mr10" style="width: 180px">
            <el-option v-for="item in orderState" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch" round plain>搜索</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset" round plain>重置</el-button>
          <el-button type="danger" icon="el-icon-plus" @click="handleAdd" round plain>新增订单</el-button>
          <el-button type="success" icon="el-icon-upload2" @click="handleImport" round plain>导入</el-button>
        </div>
        <!-- 店铺信息列表-->
        <el-table :data="tableData" border class="table" ref="multipleTable" :row-class-name="tableAddClass"
                  :row-style="{height:'55px'}" :cell-style="{padding:'0px'}"  header-cell-class-name="table-header">
          <el-table-column prop="oiId" label="ID" v-if="false" align="center"></el-table-column>
          <el-table-column prop="ordId" label="订单ID" align="center"></el-table-column>
          <el-table-column prop="shopId" label="店铺Id" v-if="false" align="center"></el-table-column>
          <el-table-column prop="shopNm" label="店铺名称" align="center"></el-table-column>
          <el-table-column prop="ordDat" label="订单日期" align="center"></el-table-column>
          <el-table-column prop="supId" label="1688订单" align="center"></el-table-column>
          <el-table-column prop="gdsId" label="商品ID" align="center"></el-table-column>
          <el-table-column prop="gdsName" label="商品名称" align="center" class="ellipsis-line1">
            <template #default="scope">
              <el-tooltip class="item" effect="light" :content="scope.row.gdsName" placement="top">
                <el-link v-if="scope.row.url" :href="scope.row.url" target="_blank" type="primary" class="ellipsis-line1">{{scope.row.gdsName}}</el-link>
                <span v-else class="ellipsis-line1">{{scope.row.gdsName}}</span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column prop="url" label="商品链接" v-if="false" align="center"></el-table-column>
          <el-table-column prop="ordSta" label="订单状态" v-if="false" align="center"></el-table-column>
          <el-table-column prop="ordStaNm" label="订单状态" align="center"></el-table-column>
          <el-table-column prop="salAmt" label="商品总价" align="center"></el-table-column>
          <el-table-column prop="payAmt" label="实付金额" align="center"></el-table-column>
          <el-table-column prop="cosAmt" label="商品成本" align="center"></el-table-column>
          <el-table-column prop="othAmt" label="其他支出" align="center"></el-table-column>
          <el-table-column prop="talCos" label="总成本" align="center" width="90px">
            <template v-slot:header='scope'>
              <span>总成本
                <el-tooltip :aa="scope" class="item" effect="light" content="总成本=商品成本 + 其他支出" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="groPro" label="毛利润" align="center" width="90px">
            <template v-slot:header='scope'>
              <span>毛利润
                <el-tooltip :aa="scope" class="item" effect="light" content="毛利润=实付金额 - 总成本" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="groProRat" label="毛利率" align="center" width="90px">
            <template v-slot:header='scope'>
              <span>毛利率
                <el-tooltip :aa="scope" class="item" effect="light" content="毛利率=毛利润 / 商品成本 * 100%" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" align="center" width="80px">
            <template #default="scope">
              <el-tooltip class="item" effect="light" :content="scope.row.remark" placement="top">
                <span class="ellipsis-line1">{{scope.row.remark}}</span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="70px">
            <template #default="scope">
              <el-tooltip class="item" effect="light" content="新增" placement="top">
                <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)"></el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="light" content="删除" placement="top">
                <el-button type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.row)"></el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination">
          <el-pagination background layout="total, prev, pager, next" :current-page="query.pageNum"
                         :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange">
          </el-pagination>
        </div>
      </div>
      <!-- 新增/编辑订单弹出框 -->
      <!-- @findOrderList="findOrderList" 设置子弹窗可以调用父页面的方法 -->
      <order-edit ref="orderDialog" @findOrderList="findOrderList"></order-edit>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, getCurrentInstance } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import request from '../../utils/request';
import orderEdit from "./OrderEdit.vue";

export default {
  name: "orderList",
  components: { orderEdit },
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 查询条件
    const query = reactive({
      ordDat: "",
      shopId: "",
      ordId: "",
      supId: "",
      gdsId: "",
      ordSta: "",
      pageNum: 1,
      pageSize: 10
    });
    //订单弹出窗对象
    const orderDialog = ref();
    // 表格数据
    const tableData = ref([]);
    // 页数
    const pageTotal = ref(0);
    // 用户的所有店铺信息
    const userShop = ref([]);
    // 订单状态
    const orderState = ref([]);
    // 查询
    const tableAddClass = ({row,rowIndex}) => {
      //【交易成功】、【已发货，待签收】设置红色
      if (['4','3'].indexOf(row.ordSta) != -1) {
        return 'tr-red';
      }//【刷单】设置绿色
      else if (['7'].indexOf(row.ordSta) != -1) {
        return 'tr-green';
      }//【待确认】设置红色
      else if (['0'].indexOf(row.ordSta) != -1) {
        return 'tr-blue';
      }//其他设置灰色
      else{
        return 'tr-gray';
      }
    };
    const handleSearch = () => {
      query.pageNum = 1;
      findOrderList();
    };
    // 重置
    const handleReset = () => {
      query.shopId = "";
      query.ordDat = "",
      query.ordId = "",
      query.supId = "",
      query.gdsId = "",
      query.ordSta = "",
      findOrderList();
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageNum = val;
      findOrderList();
    };
    //删除店铺
    const handleDelete = (row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除店铺【" + row.shopNm + "】订单ID：" + row.ordId + "的数据吗？", "提示", {type: "warning"}).then(() => {
        axios.$http.post(request.delOrder+row.oiId, null).then(function (res) {
          if(res.code === 200){
            ElMessage.success(res.data);
            findOrderList();
          }else {
            ElMessage.error(res.data);
          }
        });
      }).catch(() => {});
    };
    //新增店铺
    const handleAdd = () => {
      orderDialog.value.openOrderDialog(null,userShop,orderState);//调用子组件方法
    };
    //编辑店铺
    const handleEdit = (row) => {
      orderDialog.value.openOrderDialog(row,userShop,orderState);//调用子组件方法
    };
    //导入订单
    const handleImport = () => {
      //TODO 2023/3/18 19:55 导入功能，待开发
    };
    // 获取表格数据
    const findOrderList = () => {
      axios.$http.get(request.orderList,query).then(function (res) {
        if(res.code === 200){
          tableData.value = res.data;
          pageTotal.value = res.totalNum;
        }else{
          ElMessage.error(res.data);
        }
      })
    };
    findOrderList();
    // 查询用户的所有店铺信息
    const getUserShopArr = () => {
      axios.$http.get(request.userShop, null).then(function (res) {
        if(res.code === 200){
          userShop.value = res.data;
        }else {
          ElMessage.error(res.data);
        }
      });
    };
    getUserShopArr();
    // 查询数据字典
    const getCodeDataArr = () => {
      axios.$http.get(request.code + "OrderState", null).then(function (res) {
        if(res.code === 200){
          orderState.value = res.data.OrderState;
        }else {
          ElMessage.error(res.data);
        }
      });
    };
    getCodeDataArr();
    return { query,orderDialog,tableData,pageTotal,userShop,orderState,
      handleSearch,handleReset,handlePageChange,handleEdit,handleAdd,handleDelete,findOrderList,tableAddClass,handleImport};
  }
};
</script>

<style >
.date-div .el-input{
  width: 250px;
}
.handle-box {
  margin-bottom: 20px;
}
.handle-select {
  width: 120px;
}
.handle-input {
  width: 200px;
  display: inline-block;
}
.table {
  width: 100%;
  font-size: 14px;
}
.mr10 {
  margin-right: 10px;
}
.red {
  color: #ff0000;
}
.ellipsis-line1{
  display: -webkit-box;/*作为弹性伸缩盒子模型显示*/
  -webkit-line-clamp: 1; /*显示的行数；如果要设置2行加...则设置为2*/
  overflow: hidden; /*超出的文本隐藏*/
  text-overflow: ellipsis; /* 溢出用省略号*/
  -webkit-box-orient: vertical;/*伸缩盒子的子元素排列：从上到下*/
}
.el-table .tr-red {
  color: red ;
}
.el-table .tr-green {
  color: green ;
}
.el-table .tr-blue {
  color: blue ;
}
.el-table .tr-gray {
  color: gainsboro ;
}
</style>
