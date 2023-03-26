<!-- 月销售额 -->
<template>
  <div>
    <el-card>
      <div v-loading="pageLoading">
        <!-- 月销售额列表查询条件-->
        <div class="handle-box">
          <div class="block" style="float: left; margin-right: 10px;">
            <el-date-picker v-model="query.month" type="month" format="YYYY-MM" value-format="YYYY-MM" placeholder="选择月份"></el-date-picker>
          </div>
          <el-select v-model="query.shopId" placeholder="请选择店铺" class="handle-select mr10" style="width: 250px">
            <el-option v-for="item in userShop" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
          <el-tooltip class="item" effect="light" content="按店铺查：即查询店铺的月销售额&nbsp;&nbsp;&nbsp;按月份查：即查询所有店铺的月销售额" placement="top">
            <el-switch v-model="query.all" inactive-text="按店铺查" active-text="按月份查" style="margin-right: 10px;"></el-switch>
          </el-tooltip>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch" round plain>搜索</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset" round plain>重置</el-button>
          <el-button type="danger" icon="el-icon-plus" @click="handleAdd" round plain>新增月销售</el-button>
          <el-tooltip class="item" effect="light" content="根据订单信息统计每月销售额" placement="top">
            <el-button type="success" icon="el-icon-s-data" @click="handleCount" round plain>统计月销售</el-button>
          </el-tooltip>
        </div>
        <!-- 店铺信息列表-->
        <el-table :data="tableData" border class="table" ref="multipleTable" :cell-class-name="addCellClass"
                  :row-style="{height:'55px'}" :cell-style="{padding: '0px'}" header-cell-class-name="table-header">
          <el-table-column prop="msId" label="ID" v-if="false" align="center"></el-table-column>
          <el-table-column prop="month" label="月份" align="center" sortable></el-table-column>
          <el-table-column prop="shopId" label="店铺ID" v-if="false" align="center"></el-table-column>
          <el-table-column prop="shopNm" label="店铺名称" align="center" sortable></el-table-column>
          <el-table-column prop="retPro" label="月净利润" align="center">
            <template v-slot:header='scope'>
              <span>月净利润
                <el-tooltip :aa="scope" class="item" effect="light" content="月净利润=月销售额 - 月支出费" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="retProRat" label="月净利率" align="center">
            <template v-slot:header='scope'>
              <span>月净利率
                <el-tooltip :aa="scope" class="item" effect="light" content="月净利率=月净利润 / 月支出费 * 100%" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="groPro" label="月毛利润" align="center">
            <template v-slot:header='scope'>
              <span>月毛利润
                <el-tooltip :aa="scope" class="item" effect="light" content="月毛利润=月销售额 - 月成本费" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="groProRat" label="月毛利率" align="center">
            <template v-slot:header='scope'>
              <span>月毛利率
                <el-tooltip :aa="scope" class="item" effect="light" content="月毛利率=月毛利润 / 月成本费 * 100%" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="talOrd" label="月订单量" align="center">
            <template v-slot:header='scope'>
              <span>月订单量
                <el-tooltip :aa="scope" class="item" effect="light" content="本月日订单量合计" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="sucOrd" label="月成交单" align="center">
            <template v-slot:header='scope'>
              <span>月成交单
                <el-tooltip :aa="scope" class="item" effect="light" content="本月日成交单合计" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="faiOrd" label="月流失单" align="center">
            <template v-slot:header='scope'>
              <span>月流失单
                <el-tooltip :aa="scope" class="item" effect="light" content="本月日流失单量合计" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="salAmt" label="月销售额" align="center">
            <template v-slot:header='scope'>
              <span>月销售额
                <el-tooltip :aa="scope" class="item" effect="light" content="本月日销售额合计" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="cosAmt" label="月成本费" align="center">
            <template v-slot:header='scope'>
              <span>月成本费
                <el-tooltip :aa="scope" class="item" effect="light" content="本月日成本费合计" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="advAmt" label="月推广费" align="center">
            <template v-slot:header='scope'>
              <span>月推广费
                <el-tooltip :aa="scope" class="item" effect="light" content="需要手动录入" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
            <template #default="scope">
              {{scope.row.advAmt}}<br/>
              <el-tooltip class="item" effect="light" content="增加推广费" placement="top">
                <div class="el-icon-plus" @click="handleIncrease(scope.row)" style="width: 16px;height: 16px;margin-right:5px;color: deepskyblue;"></div>
              </el-tooltip>
              <el-tooltip class="item" effect="light" content="减少推广费" placement="top">
                <div class="el-icon-minus" @click="handleReduce(scope.row)" style="width: 16px;height: 16px;color: red;"></div>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column prop="serAmt" label="月服务费" align="center">
            <template v-slot:header='scope'>
              <span>月服务费
                <el-tooltip :aa="scope" class="item" effect="light" content="需要手动录入" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
            <template #default="scope">
              {{scope.row.serAmt}}<br/>
              <el-tooltip class="item" effect="light" content="增加服务费" placement="top">
                <div class="el-icon-plus" @click="handleIncrease(scope.row)" style="width: 16px;height: 16px;margin-right:5px;color: deepskyblue"></div>
              </el-tooltip>
              <el-tooltip class="item" effect="light" content="减少服务费" placement="top">
                <div class="el-icon-minus" @click="handleReduce(scope.row)" style="width: 16px;height: 16px;margin-right:5px;color: red;"></div>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column prop="virAmt" label="月刷单费" align="center">
            <template v-slot:header='scope'>
              <span>月刷单费
                <el-tooltip :aa="scope" class="item" effect="light" content="本月日刷单费合计" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="talCos" label="月支出费" align="center">
            <template v-slot:header='scope'>
              <span>月支出费
                <el-tooltip :aa="scope" class="item" effect="light" content="月支出费 = 月成本费 + 月推广费 + 月服务费 + 月刷单费" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
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
      <!-- 新增/编辑月销售弹出框 -->
      <!-- @findOrderList="findOrderList" 设置子弹窗可以调用父页面的方法 -->
      <month-edit ref="monthDialog" @findMonthList="findMonthList"></month-edit>
      <!-- 增加/减少月销售费用弹出框-->
      <month-amt-edit ref="monthAmtDialog" @findMonthList="findMonthList"></month-amt-edit>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, getCurrentInstance } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import request from '../../utils/request';
import monthEdit from "./MonthEdit.vue";
import monthAmtEdit from "./MonthAmtEdit.vue";

export default {
  name: "monthSales",
  components: { monthEdit,monthAmtEdit },
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 查询条件
    const query = reactive({
      shopId: "",
      month: "",
      all: false,
      pageNum: 1,
      pageSize: 10
    });
    // 整个页面Loading 加载遮罩层控制
    const pageLoading = ref(false);
    //月销售弹出窗对象
    const monthDialog = ref();
    //月销售金额弹出窗对象
    const monthAmtDialog = ref();
    // 表格数据
    const tableData = ref([]);
    // 页数
    const pageTotal = ref(0);
    // 用户的所有店铺信息
    const userShop = ref([]);
    // 查询
    const handleSearch = () => {
      query.pageNum = 1;
      findMonthList();
    };
    // 重置
    const handleReset = () => {
      query.shopId = "";
      query.month = "";
      query.all = false;
      findMonthList();
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageNum = val;
      findMonthList();
    };
    //增加月销售费用弹出框
    const handleIncrease = (row) => {
      monthAmtDialog.value.openMonthAmtDialog(row,true);//调用子组件方法
    };
    //减少月销售费用弹出框
    const handleReduce = (row) => {
      monthAmtDialog.value.openMonthAmtDialog(row,false);//调用子组件方法
    };
    //列添加颜色
    const addCellClass = ({row, column, rowIndex, columnIndex}) => {
      //列的label的名称
      if (row.retPro > 0) {
        return 'col-red';
      }else if(row.retPro < 0){
        return 'col-green';
      }
      if(!row.shopNm && columnIndex == 15){
        return 'col-display-none div';
      }
    }
    // 统计月销售额数
    const handleCount = () => {
      pageLoading.value = true;
      axios.$http.post(request.monthCount,null).then(function (res) {
        pageLoading.value = false;
        ElMessage.success(res.data);
        findMonthList();
      }).catch(err => {pageLoading.value = false;});
    };
    //删除月销售额数据
    const handleDelete = (row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除店铺【" + row.shopNm + "】" + row.month + "月份的销售数据吗？", "提示",
          {confirmButtonText: '确定',cancelButtonText: '关闭',type: 'warning',center: true,roundButton: true}
      ).then(() => {
        pageLoading.value = true;
        axios.$http.post(request.delMonth+row.msId, null).then(function (res) {
          pageLoading.value = false;
          ElMessage.success(res.data);
          findMonthList();
        });
      }).catch(err => {pageLoading.value = false;});
    };
    //新增月销售额数据
    const handleAdd = () => {
      monthDialog.value.openMonthDialog("新增月销售额",null,userShop);//调用子组件方法
    };
    //编辑月销售额数据
    const handleEdit = (row) => {
      monthDialog.value.openMonthDialog("编辑月销售额",row,userShop);//调用子组件方法
    };
    // 获取表格数据
    const findMonthList = () => {
      pageLoading.value = true;
      axios.$http.get(request.monthList,query).then(function (res) {
        pageLoading.value = false;
        tableData.value = res.data;
        pageTotal.value = res.totalNum;
      }).catch(err => {
        pageLoading.value = false;
      });
    };
    findMonthList();
    // 查询用户的所有店铺信息
    const getUserShopArr = () => {
      axios.$http.get(request.userShop, null).then(function (res) {
        userShop.value = res.data;
      });
    };
    getUserShopArr();
    return { query,tableData,pageTotal,userShop,monthDialog,monthAmtDialog,pageLoading,
      handleSearch,handleReset,handlePageChange,handleCount,handleAdd,handleDelete,
      handleEdit,findMonthList,handleIncrease,handleReduce,addCellClass};
  }
};
</script>

<style scoped>
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
/deep/.col-red {
  color: red;
}
/deep/.col-green {
  color: #07bd07;
}
/deep/.col-gray {
  color: black ;
}
/deep/.col-display-none div{
  display: none;
}
</style>
