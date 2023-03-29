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
          <el-button type="primary" icon="el-icon-search" @click="handleSearch" round plain>查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset" round plain>重置</el-button>
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
          <el-table-column prop="shopNm" label="店铺名称" align="center" sortable width="120px;">
            <template v-slot:header='scope'>
              <span>店铺名称
                <el-tooltip :aa="scope" class="item" effect="light" content="店铺名称为空，则该笔数据是所有店铺汇总的月销售额数据" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
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
          </el-table-column>
          <el-table-column prop="serAmt" label="月服务费" align="center">
            <template v-slot:header='scope'>
              <span>月服务费
                <el-tooltip :aa="scope" class="item" effect="light" content="需要手动录入" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
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
    </el-card>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import request from '../../utils/request';
import monthEdit from "./MonthEdit.vue";

export default {
  name: "monthSales",
  components: { monthEdit },
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
    //列添加颜色
    const addCellClass = ({row, column, rowIndex, columnIndex}) => {
      //列的label的名称
      if (row.retPro > 0) {
        return 'col-red';
      }else if(row.retPro < 0){
        return 'col-green';
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
    return { query,tableData,pageTotal,userShop,monthDialog,pageLoading,
      handleSearch,handleReset,handlePageChange,handleCount,findMonthList,addCellClass};
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
</style>
