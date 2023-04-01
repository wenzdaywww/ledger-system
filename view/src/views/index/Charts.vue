<template>
  <div>
    <div class="container">
      <el-row>
        <div style="margin-left: 20px;">
          店铺：
          <el-select v-model="query.shopId" class="handle-select mr10" style="width: 250px" clearable="true" @change="shopChange">
            <el-option v-for="item in userShop" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </div>
      </el-row>
      <!--      日销售数据-->
      <el-row>
        <div class="schart-box">
          <schart class="schart" canvasId="day-sales-line" :options="daySalesOption"></schart>
        </div>
      </el-row>
      <!--      日订单数据-->
      <el-row v-if="dayOrderOption.labels.length > 0">
        <div class="schart-box">
          <schart class="schart" canvasId="day-order-line" :options="dayOrderOption"></schart>
        </div>
      </el-row>
      <!--      月销售数据-->
      <el-row v-if="monthSalesOption.labels.length > 0">
        <div class="schart-box">
          <schart class="schart" canvasId="month-sales-bar" :options="monthSalesOption"></schart>
        </div>
      </el-row>
      <!--      月订单数据-->
      <el-row v-if="monthOrderOption.labels.length > 0">
        <div class="schart-box">
          <schart class="schart" canvasId="month-order-bar" :options="monthOrderOption"></schart>
        </div>
      </el-row>
      <!--      年销售数据-->
      <el-row v-if="yearSalesOption.labels.length > 0">
        <div class="schart-box">
          <schart class="schart" canvasId="year-sales-bar" :options="yearSalesOption"></schart>
        </div>
      </el-row>
      <!--      年订单数据-->
      <el-row v-if="yearOrderOption.labels.length > 0">
        <div class="schart-box">
          <schart class="schart" canvasId="year-order-bar" :options="yearOrderOption"></schart>
        </div>
      </el-row>
    </div>
  </div>
</template>

<script>
import Schart from "vue-schart";
import {getCurrentInstance, inject, reactive, ref} from "vue";
import request from "../../utils/request";
import {ElLoading, ElMessage} from "element-plus";
export default {
  name: "dataCharts",
  components: { Schart},
  setup(props, context) {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 查询条件
    const query = reactive({
      shopId: "",
      loadNum: 0
    });
    // 用户的所有店铺信息
    const userShop = ref([]);
    //报表标题
    const chartTitle = {
      daySales : "近{0}天日销售趋势图",
      dayOrder : "近{0}天日订单量趋势图",
      yearSales : "近{0}年的年销售数据图",
      yearOrder : "近{0}年的年订单量数据图",
    }
    //TODO 2023/4/2 00:25 图表负y轴未显示，待处理
    //日销售利润数据
    const daySalesOption = ref({
      type: "line",
      xRorate: 20,
      title: { text: chartTitle.daySales},
      labels: [],
      datasets: [],
    });
    //日订单数据
    const dayOrderOption = ref({
      type: "line",
      xRorate: 20,
      title: { text: chartTitle.dayOrder},
      labels: [],
      datasets: [],
    });
    //月销售数据
    const monthSalesOption = ref({
      type: "bar",
      title: { text: "近一年的月销售数据图"},
      labels: [],
      datasets: [],
    });
    //月订单数据
    const monthOrderOption = ref({
      type: "bar",
      title: { text: "近一年的月订单量数据图"},
      labels: [],
      datasets: [],
    });
    //年销售数据
    const yearSalesOption = ref({
      type: "bar",
      title: { text: chartTitle.yearSales},
      labels: [],
      datasets: [],
    });
    //年订单量数据
    const yearOrderOption = ref({
      type: "bar",
      title: { text: chartTitle.yearOrder},
      labels: [],
      datasets: [],
    });
    /**
     * 查询图表数据
     * @param serviceName 请求方法
     * @param chartObj 图表数据对象
     */
    const findChartData = (serviceName,chartObj) => {
      query.loadNum = query.loadNum + 1;
      let uploadLoading = null;
      //最后一个请求需要添加遮罩层
      if (query.loadNum == 9){
        uploadLoading = ElLoading.service({text: "加载中...",fullscreen: true});
      }
      axios.$http.get(serviceName,query).then(function (res) {
        if (uploadLoading){
          uploadLoading.close();
        }
        chartObj.title.text = chartObj.title.text.replace("{0}",res.data.labels.length);
        chartObj.labels = res.data.labels;
        chartObj.datasets = res.data.datasets;
      }).catch(err => {
        if (uploadLoading){
          uploadLoading.close();
        }
      });
    };
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
      query.loadNum = 0;
      //查询日销售利润数据
      daySalesOption.value.title.text = chartTitle.daySales;//标题重置
      findChartData(request.daySales,daySalesOption.value);
      //查询日订单量数据
      dayOrderOption.value.title.text = chartTitle.dayOrder;//标题重置
      findChartData(request.dayOrder,dayOrderOption.value);
      //查询月销售数据
      findChartData(request.monthSales,monthSalesOption.value);
      //查询月订单量数据
      findChartData(request.monthOrder,monthOrderOption.value);
      //查询年销售数据
      yearSalesOption.value.title.text = chartTitle.yearSales;//标题重置
      findChartData(request.yearSales,yearSalesOption.value);
      // //查询年订单量数据
      yearOrderOption.value.title.text = chartTitle.yearOrder;//标题重置
      findChartData(request.yearOrder,yearOrderOption.value);
    }
    shopChange(null);
    return {query,shopChange,daySalesOption,dayOrderOption,monthSalesOption,
      monthOrderOption,yearSalesOption,yearOrderOption,userShop
    };
  },
};
</script>

<style scoped>
.schart-box {
  display: inline-block;
  margin: 20px;
  width: 100%;
}
.schart {
  width: 100%;
  height: 400px;
}
.content-title {
  clear: both;
  font-weight: 400;
  line-height: 50px;
  margin: 10px 0;
  font-size: 22px;
  color: #1f2f3d;
}
</style>