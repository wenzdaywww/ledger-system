<template>
  <div>
    <!--  年销售数据-->
    <el-row>
      <div class="content-title">年销售额查询</div>
      <div class="schart-box">
        <div>
          <el-row style="margin-bottom: 5px;">
            <el-col :span="2.5" style="line-height: 2.5;">
              年份范围：
            </el-col>
            <el-col :span="3">
              <el-tooltip class="item" effect="light" content="年份范围超过10年，最多查询10年销售数据" placement="top">
                <div>
                  <el-date-picker v-model="query.startDate" type="year" style="width:130px;"
                                  format="YYYY" value-format="YYYY-MM-DD"  placeholder="开始年份">
                  </el-date-picker>
                </div>
              </el-tooltip>
            </el-col>
            <el-col :span="1">
              <div style="text-align: center;width:20px;line-height: 2.5;">-</div>
            </el-col>
            <el-col :span="3">
              <el-tooltip class="item" effect="light" content="年份范围超过10年，最多查询10年销售数据" placement="top">
                <div>
                  <el-date-picker v-model="query.endDate" type="year" style="width:130px;"
                                  format="YYYY" value-format="YYYY-MM-DD"  placeholder="截止年份">
                  </el-date-picker>
                </div>
              </el-tooltip>
            </el-col>
            <el-col :span="2">
              <el-button type="primary" icon="el-icon-search" @click="findYearSales" round plain>查询</el-button>
            </el-col>
            <el-col :span="2">
              <el-button icon="el-icon-refresh-left" @click="resetYearSales" round plain>重置</el-button>
            </el-col>
          </el-row>
        </div>
        <div class="schart" id="year-sales-bar"></div>
      </div>
    </el-row>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, ref} from "vue";
import request from "../../../utils/request";
import {getInstanceByDom, init} from "echarts";

export default {
  name: "yearSalesChart",
  mounted() {
    let initChart = init(document.getElementById("year-sales-bar"));
    //设置echarts对象的option属性
    initChart.setOption(this.yearSalesOption);
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
    //报表标题
    const noOrderTitle = "没有年销售额数据";
    //年销售数据
    const yearSalesOption = ref({
      title: {
        text: chartTitle,
        left: "center" //标题居中
      },
      tooltip: {},
      xAxis: {
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
      findYearSales();
    }
    //查询年销售数据
    const findYearSales = () => {
      axios.$http.get(request.yearSales,query).then(function (res) {
        let yearSalesChart = getInstanceByDom(document.getElementById("year-sales-bar"));
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
          let oldOption = yearSalesChart.getOption();
          for (let i = 0; i < oldOption.series.length; ++i) {
            option.series.push({name:"",data:[]});
          }
        }
        yearSalesChart.setOption(option);
      }).catch(err => {});
    }
    //重置年销售数据查询年份
    const resetYearSales = () => {
      query.startDate = "";
      query.endDate = "";
      findYearSales();
    }
    return {query,yearSalesOption,
      findYearSales,resetYearSales,changeShopId
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