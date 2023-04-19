<template>
  <div>
    <!-- 月订单数据-->
    <el-row>
      <div class="content-title">月订单量查询</div>
      <div class="schart-box">
        <div>
          <el-row style="margin-bottom: 5px;">
            <el-col :span="2.5" style="line-height: 2.5;">
              月份范围：
            </el-col>
            <el-col :span="6">
              <el-tooltip class="item" effect="light" content="月份范围超过12个月，最多查询12个月销售数据" placement="top">
                <div>
                  <el-date-picker v-model="monthOrderRange" type="monthrange" style="width:300px;margin-right: 20px;"
                                  format="YYYY-MM" value-format="YYYY-MM-DD" nge-separator="至" start-placeholder="开始月份" end-placeholder="截止月份">
                  </el-date-picker>
                </div>
              </el-tooltip>
            </el-col>
            <el-col :span="2">
              <el-button type="primary" icon="el-icon-search" @click="findMonthOrder" round plain>查询</el-button>
            </el-col>
            <el-col :span="2">
              <el-button icon="el-icon-refresh-left" @click="resetMonthOrder" round plain>重置</el-button>
            </el-col>
          </el-row>
        </div>
        <div class="schart" id="month-order-bar"></div>
      </div>
    </el-row>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, ref} from "vue";
import request from "../../../utils/request";
import {getInstanceByDom, init} from "echarts";

export default {
  name: "monthOrderChart",
  mounted() {
    let initChart = init(document.getElementById("month-order-bar"));
    //设置echarts对象的option属性
    initChart.setOption(this.monthOrderOption);
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
    const chartTitle = "{0} 至 {1} 订单量趋势图";
    //报表标题
    const noDataTitle = "查询不到 {0} 至 {1} 订单量数据";
    //报表标题
    const noOrderTitle = "没有月订单量数据";
    //月订单数据月份范围选择器
    const monthOrderRange = ref([]);
    //月订单数据
    const monthOrderOption = ref({
      title: {
        text: chartTitle,
        left: "center" //标题居中
      },
      tooltip: {},
      xAxis: {
        axisLabel: {
          rotate: 20 //x坐标倾斜度
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
      findMonthOrder();
    }
    //查询月订单数据
    const findMonthOrder = () => {
      if (monthOrderRange.value){
        query.startDate = monthOrderRange.value[0];
        query.endDate = monthOrderRange.value[1];
      }else {
        query.startDate = "";
        query.endDate = "";
      }
      axios.$http.get(request.monthOrder,query).then(function (res) {
        let monthOrderChart = getInstanceByDom(document.getElementById("month-order-bar"));
        let option = {
          title:{text: ""},
          legend:{data:[]},
          xAxis:{data:[]},
          series:[]
        };
        if (res.data && res.data.xaxis){
          option.title.text = chartTitle.replace("{0}",res.data.startDate).replace("{1}",res.data.endDate);
          option.xAxis.data = res.data.xaxis;
          for (let i = 0; i < res.data.series.length; ++i) {
            let  item = res.data.series[i];
            item.type = "bar";
            //上方显示数值
            item.label= {
              show: true,
              position: "top"
            };
            option.legend.data.push(item.name);
            option.series.push(item);
          }
        }else {
          option.title.text = res.data && res.data.startDate && res.data.endDate ?
              noDataTitle.replace("{0}",res.data.startDate).replace("{1}",res.data.endDate) : noOrderTitle;
          let oldOption = monthOrderChart.getOption();
          for (let i = 0; i < oldOption.series.length; ++i) {
            option.series.push({name:"",data:[]});
          }
        }
        monthOrderChart.setOption(option);
      }).catch(err => {});
    }
    //重置月订单数据查询月份
    const resetMonthOrder = () => {
      monthOrderRange.value = [];
      findMonthOrder();
    }
    return {monthOrderRange,monthOrderOption,
      findMonthOrder,resetMonthOrder,changeShopId
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