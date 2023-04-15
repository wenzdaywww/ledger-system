<template>
  <div>
    <div class="container">
      <el-row>
        <div style="margin-left: 20px;">
          店铺：
          <el-select v-model="query.shopId" class="handle-select mr10" style="width: 250px;" clearable="true" @change="shopChange">
            <el-option v-for="item in userShop" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </div>
      </el-row>
      <!--  日销售数据-->
      <el-row>
        <div class="schart-box">
            <day-sales-chart ref="daySalesChartObj"></day-sales-chart>
        </div>
      </el-row>
      <!-- 日订单数据-->
      <el-row>
        <div class="schart-box">
            <day-order-chart ref="dayOrderChartObj"></day-order-chart>
          <chat-line ></chat-line>
        </div>
      </el-row>
      <!-- 日订单交易状态数量数据-->
      <el-row>
        <div class="schart-box">
          <day-state-chart ref="dayStateChartObj"></day-state-chart>
        </div>
      </el-row>
      <!-- 月销售数据-->
      <el-row>
        <div class="schart-box">
          <month-sales-chart ref="monthSalesChartObj"></month-sales-chart>
        </div>
      </el-row>
      <!-- 月订单数据-->
      <el-row>
        <div class="schart-box">
          <month-order-chart ref="monthOrderChartObj"></month-order-chart>
        </div>
      </el-row>
      <!-- 月订单交易状态数量数据-->
      <el-row>
        <div class="schart-box">
          <month-state-chart ref="monthStateChartObj"></month-state-chart>
        </div>
      </el-row>
      <!-- 年销售数据-->
      <el-row>
        <div class="schart-box">
          <year-sales-chart ref="yearSalesChartObj"></year-sales-chart>
        </div>
      </el-row>
      <!-- 年订单数据-->
      <el-row>
        <div class="schart-box">
          <year-order-chart ref="yearOrderChartObj"></year-order-chart>
        </div>
      </el-row>
      <!-- 年订单交易状态数量数据-->
      <el-row>
        <div class="schart-box">
          <year-state-chart ref="yearStateChartObj"></year-state-chart>
        </div>
      </el-row>
    </div>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, ref} from "vue";
import request from "../../utils/request";
import daySalesChart from "./schart/DaySalesChart.vue"
import dayOrderChart from "./schart/DayOrderChart.vue"
import dayStateChart from "./schart/DayStateChart.vue"
import monthSalesChart from "./schart/MonthSalesChart.vue"
import monthOrderChart from "./schart/MonthOrderChart.vue"
import monthStateChart from "./schart/MonthStateChart.vue"
import yearSalesChart from "./schart/YearSalesChart.vue"
import yearOrderChart from "./schart/YearOrderChart.vue"
import yearStateChart from "./schart/YearStateChart.vue"

export default {
  name: "Charts",
  components: {
    daySalesChart,dayOrderChart,dayStateChart,
    monthSalesChart,monthOrderChart,monthStateChart,
    yearSalesChart,yearOrderChart,yearStateChart
  },
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 查询条件
    const query = reactive({
      shopId: ""
    });
    // 用户的所有店铺信息
    const userShop = ref([]);
    //日销售数据图表对象
    const daySalesChartObj = ref();
    //日订单数据图表对象
    const dayOrderChartObj = ref();
    //日订单交易状态数量数据图表对象
    const dayStateChartObj = ref();
    //月销售数据图表对象
    const monthSalesChartObj = ref();
    //月订单数据图表对象
    const monthOrderChartObj = ref();
    //月订单交易状态数量数据图表对象
    const monthStateChartObj = ref();
    //年销售数据图表对象
    const yearSalesChartObj = ref();
    //年订单量数据图表对象
    const yearOrderChartObj = ref();
    //年订单交易状态数量数据图表对象
    const yearStateChartObj = ref();
    // 查询用户的所有店铺信息
    const getUserShopArr = () => {
      axios.$http.get(request.userShop, null).then(function (res) {
        userShop.value = res.data;
        userShop.value.push({value: "",name: "所有店铺汇总数据"});
      });
    };
    getUserShopArr();
    // 查询日利润数据
    const shopChange = (item) => {
      //查询日销售数据
      daySalesChartObj.value.changeShopId(item);
      //查询日订单量数据
      dayOrderChartObj.value.changeShopId(item);
      //查询日订单交易状态数量数据
      dayStateChartObj.value.changeShopId(item);
      //查询月销售数据
      monthSalesChartObj.value.changeShopId(item);
      //查询月订单量数据
      monthOrderChartObj.value.changeShopId(item);
      //查询月订单交易状态数量数据
      monthStateChartObj.value.changeShopId(item);
      //查询年销售数据
      yearSalesChartObj.value.changeShopId(item);
      //查询年订单量数据
      yearOrderChartObj.value.changeShopId(item);
      //查询年订单交易状态数量数据
      yearStateChartObj.value.changeShopId(item);
    }
    //需要子组件加载完后定时执行的代码
    setTimeout(() => {
      shopChange("");
    }, 500)
    return {query,daySalesChartObj,dayOrderChartObj,dayStateChartObj,monthSalesChartObj,
      monthOrderChartObj,monthStateChartObj,yearSalesChartObj,yearOrderChartObj,yearStateChartObj,userShop,
      shopChange
    };
  },
};
</script>

<style scoped>
.schart-box {
  display: inline-block;
  width: 100%;
}
</style>