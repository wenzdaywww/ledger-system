<template>
  <div>
    <div class="chart-div">
      <!-- 日销售数据-->
      <el-row>
        <el-col :span="24">
          <day-sales-chart ref="daySalesChartObj"></day-sales-chart>
        </el-col>
      </el-row>
      <!-- 日订单数据-->
      <el-row>
        <el-col :span="24">
          <day-order-chart ref="dayOrderChartObj"></day-order-chart>
        </el-col>
      </el-row>
      <!-- 日订单交易状态数量数据-->
      <el-row>
        <el-col :span="24">
          <day-state-chart ref="dayStateChartObj"></day-state-chart>
        </el-col>
      </el-row>
      <!-- 月销售数据-->
      <el-row>
        <el-col :span="24">
          <month-sales-chart ref="monthSalesChartObj"></month-sales-chart>
        </el-col>
      </el-row>
      <!-- 月订单数据-->
      <el-row>
        <el-col :span="24">
          <month-order-chart ref="monthOrderChartObj"></month-order-chart>
        </el-col>
      </el-row>
      <!-- 月订单交易状态数量数据-->
      <el-row>
        <el-col :span="24">
          <month-state-chart ref="monthStateChartObj"></month-state-chart>
        </el-col>
      </el-row>
      <!-- 年销售数据-->
      <el-row>
        <el-col :span="24">
          <year-sales-chart ref="yearSalesChartObj"></year-sales-chart>
        </el-col>
      </el-row>
      <!-- 年订单数据-->
      <el-row>
        <el-col :span="24">
          <year-order-chart ref="yearOrderChartObj"></year-order-chart>
        </el-col>
      </el-row>
      <!-- 年订单交易状态数量数据-->
      <el-row>
        <el-col :span="24">
          <year-state-chart ref="yearStateChartObj"></year-state-chart>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import {ref} from "vue";
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
    // 店铺变动重新查询
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
    return {daySalesChartObj,dayOrderChartObj,dayStateChartObj,monthSalesChartObj,
      monthOrderChartObj,monthStateChartObj,yearSalesChartObj,yearOrderChartObj,yearStateChartObj,
      shopChange
    };
  },
};
</script>

<style scoped>
.chart-div{
  padding: 10px;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
}
</style>