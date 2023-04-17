<template>
  <div>
    <!--  日销售数据-->
    <el-row>
      <div class="content-title">日销售额查询</div>
      <div class="schart-box">
        <div>
          <el-row style="margin-bottom: 5px;">
            <el-col :span="2.5" style="line-height: 2.5;">
              日期范围：
            </el-col>
            <el-col :span="6">
              <el-tooltip class="item" effect="light" content="日期范围超过30天，最多查询30天销售数据" placement="top">
                <div>
                  <el-date-picker v-model="daySalesRange" type="daterange" style="width:300px;margin-right: 20px;"
                                  format="YYYY-MM-DD" value-format="YYYY-MM-DD" nge-separator="至" start-placeholder="开始日期" end-placeholder="截止日期">
                  </el-date-picker>
                </div>
              </el-tooltip>
            </el-col>
            <el-col :span="2">
              <el-button type="primary" icon="el-icon-search" @click="findDaySales" round plain>查询</el-button>
            </el-col>
            <el-col :span="2">
              <el-button icon="el-icon-refresh-left" @click="resetDaySales" round plain>重置</el-button>
            </el-col>
          </el-row>
        </div>
        <div class="schart" id="day-sales-line"></div>
      </div>
    </el-row>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, ref} from "vue";
import request from "../../../utils/request";
import {getInstanceByDom, init} from 'echarts';

export default {
  name: "daySalesChart",
  components: {},
  mounted() {
    let initChart = init(document.getElementById("day-sales-line"));
    //设置echarts对象的option属性
    initChart.setOption(this.daySalesOption);
  },
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 查询条件
    const query = reactive({
      shopId: "",
      startDate: "",
      endDate: ""
    });
    //报表标题
    const chartTitle = "{0} 至 {1} 销售额趋势图";
    //报表标题
    const noDataTitle = "查询不到 {0} 至 {1} 销售额数据";
    //日销售利润数据日期范围选择器
    const daySalesRange = ref([]);
    //日销售利润数据
    const daySalesOption = ref({
      title: {
        text: chartTitle,
        left: "center" //标题居中
      },
      tooltip: {},
      xAxis: {
        axisLabel: {
          rotate: 30 //x坐标倾斜度
        },
        data: [] //x坐标数据
      },
      legend: {
        top: 21,
        data: [] //图例数据
      },
      yAxis:{},
      series: [] //折线数据
    });
    //店铺改变后重新查询
    const changeShopId = (shopIdTemp) => {
      query.shopId = shopIdTemp;
      findDaySales();
    }
    //查询日销售利润数据
    const findDaySales = () => {
      if (daySalesRange.value){
        query.startDate = daySalesRange.value[0];
        query.endDate = daySalesRange.value[1];
      }else {
        query.startDate = "";
        query.endDate = "";
      }
      axios.$http.get(request.daySales,query).then(function (res) {
        let daySalesChart = getInstanceByDom(document.getElementById("day-sales-line"));
        if (res.data && res.data.xaxis){
          let option = {
            title: {text:chartTitle.replace("{0}",res.data.startDate).replace("{1}",res.data.endDate)},
            legend: {data:[]},
            xAxis: {data:res.data.xaxis},
            series: []
          };
          for (let i = 0; i < res.data.series.length; ++i) {
            let item = res.data.series[i];
            item.type = "line";
            //上方显示数值
            item.label= {
              show: true,
              position: "top"
            };
            option.legend.data.push(item.name);
            option.series.push(item);
          }
          daySalesChart.setOption(option);
        }else {
          let oldOption = daySalesChart.getOption();
          let option = {
            title: {text:noDataTitle.replace("{0}",res.data.startDate).replace("{1}",res.data.endDate)},
            legend: {data:[]},
            xAxis: {data:[]},
            series: []
          };
          for (let i = 0; i < oldOption.series.length; ++i) {
            option.series.push({name:"",data:[]});
          }
          daySalesChart.setOption(option);
        }
      }).catch(err => {});
    }
    //重置日销售利润数据查询日期
    const resetDaySales = () => {
      daySalesRange.value = [];
      findDaySales();
    }
    return {daySalesRange,daySalesOption,
      findDaySales,resetDaySales,changeShopId
    };
  },
};
</script>

<style scoped>
.schart-box {
  display: inline-block;
  width: 100%;
}
.schart {
  width: 100%;
  height: 500px;
}
.content-title {
  clear: both;
  font-weight: 400;
  line-height: 50px;
  margin: 10px 0;
  font-size: 22px;
  color: #66b1ff;
  text-align: center;
  width: 100%;
}
</style>