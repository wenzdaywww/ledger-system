<template>
  <div>
    <!--  年订单数据-->
    <el-row>
      <div class="content-title">年订单量查询</div>
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
              <el-button type="primary" icon="el-icon-search" @click="findYearOrder" round plain>查询</el-button>
            </el-col>
            <el-col :span="2">
              <el-button icon="el-icon-refresh-left" @click="resetYearOrder" round plain>重置</el-button>
            </el-col>
          </el-row>
        </div>
        <div class="schart" id="year-order-bar"></div>
      </div>
    </el-row>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, ref} from "vue";
import request from "../../../utils/request";
import {getInstanceByDom, init} from "echarts";

export default {
  name: "yearOrderChart",
  mounted() {
    let initChart = init(document.getElementById("year-order-bar"));
    //设置echarts对象的option属性
    initChart.setOption(this.yearOrderOption);
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
    const chartTitle = "年订单量数据图";
    //年订单数据
    const yearOrderOption = ref({
      title: {
        text: chartTitle,
        left: "center" //标题居中
      },
      tooltip: {},
      xAxis: {
        data: [] //x坐标数据
      },
      legend: {
        top: 25,
        data: [] //图例数据
      },
      yAxis:{},
      series: [] //折线数据
    });
    //店铺改变后重新查询
    const changeShopId = (shopIdTemp) => {
      query.shopId = shopIdTemp;
      findYearOrder();
    }
    //查询年订单数据
    const findYearOrder = () => {
      axios.$http.get(request.yearOrder,query).then(function (res) {
        let yearOrderChart = getInstanceByDom(document.getElementById("year-order-bar"));
        if (res.data){
          let option = {
            title:{text: chartTitle.replace("{0}",res.data.xaxis.length)},
            legend:{data:[]},
            xAxis:{data:res.data.xaxis},
            series:[]
          };
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
          yearOrderChart.setOption(option);
        }else {
          let oldOption = yearOrderChart.getOption();
          let option = {
            title: {text:"查询不到年订单量数据"},
            legend: {data:[]},
            xAxis: {data:[]},
            series: []
          };
          for (let i = 0; i < oldOption.series.length; ++i) {
            option.series.push({name:"",data:[]});
          }
          yearOrderChart.setOption(option);
        }
      }).catch(err => {});
    }
    //重置年订单数据查询日期
    const resetYearOrder = () => {
      query.startDate = "";
      query.endDate = "";
      findYearOrder();
    }
    return {query,yearOrderOption,
      findYearOrder,resetYearOrder,changeShopId
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
  color: red;
  text-align: center;
  width: 100%;
}
</style>