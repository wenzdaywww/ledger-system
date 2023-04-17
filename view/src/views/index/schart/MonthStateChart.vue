<template>
  <div>
    <!--  月订单交易状态数量数据-->
    <el-row>
      <div class="content-title">月订单交易状态数量查询</div>
      <div class="schart-box">
        <div>
          <el-row style="margin-bottom: 5px;">
            <el-col :span="2.5" style="line-height: 2.5;">
              月份范围：
            </el-col>
            <el-col :span="6">
              <el-tooltip class="item" effect="light" content="月份范围超过30天，最多查询30天订单状态数据" placement="top">
                <div>
                  <el-date-picker v-model="monthStateRange" type="monthrange" style="width:300px;margin-right: 20px;"
                                  format="YYYY-MM" value-format="YYYY-MM-DD" nge-separator="至" start-placeholder="开始月份" end-placeholder="截止月份">
                  </el-date-picker>
                </div>
              </el-tooltip>
            </el-col>
            <el-col :span="2">
              <el-button type="primary" icon="el-icon-search" @click="findMonthState" round plain>查询</el-button>
            </el-col>
            <el-col :span="2">
              <el-button icon="el-icon-refresh-left" @click="resetMonthState" round plain>重置</el-button>
            </el-col>
          </el-row>
        </div>
        <div class="schart" id="month-state-pie"></div>
      </div>
    </el-row>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, ref} from "vue";
import request from "../../../utils/request";
import {getInstanceByDom,init} from "echarts";

export default {
  name: "monthStateChart",
  mounted() {
    let initChart = init(document.getElementById("month-state-pie"));
    //设置echarts对象的option属性
    initChart.setOption(this.monthStateOption);
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
    const chartTitle = "{0} 至 {1} 订单交易状态数量比例图";
    //报表标题
    const noDataTitle = "查询不到 {0} 至 {1} 订单交易状态数量数据";
    //月订单交易状态数量数据日期范围选择器
    const monthStateRange = ref([]);
    //月订单交易状态数量数据
    const monthStateOption = ref({
      title: {
        text: chartTitle,
        left: "center" //标题居中
      },
      // 设置环形中间的数据
      graphic: [{
        type: 'text',
        left: "center",
        top: "center",
        style: {
          text: "", //环形中间的数据
          textAlign: "center",
          fill: "black",
          fontSize: 16
        }
      }],
      series: [
        {
          type:"pie",
          radius: ['80','60%'],
          label : {
            normal : {
              formatter: '{b}（{d}%）: {c}'
            }
          },
          data: [] //折线数据
        }
      ]
    });
    //店铺改变后重新查询
    const changeShopId = (shopIdTemp) => {
      query.shopId = shopIdTemp;
      findMonthState();
    }
    //查询月订单交易状态数量数据
    const findMonthState = () => {
      if (monthStateRange.value){
        query.startDate = monthStateRange.value[0];
        query.endDate = monthStateRange.value[1];
      }else {
        query.startDate = "";
        query.endDate = "";
      }
      axios.$http.get(request.monthState,query).then(function (res) {
        let monthStateChart = getInstanceByDom(document.getElementById("month-state-pie"));
        if (res.data && res.data.series){
          let option = {
            title: {
              text: chartTitle.replace("{0}",res.data.startDate).replace("{1}",res.data.endDate)
            },
            graphic:[{
              style:{
                text: "总数：" + res.data.total
              }
            }],
            series:[{
              data: res.data.series
            }]
          };
          monthStateChart.setOption(option);
        }else {
          let option = {
            title: {text:noDataTitle.replace("{0}",res.data.startDate).replace("{1}",res.data.endDate)},
            graphic:[{
              style:{
                text: "总数：0"
              }
            }],
            series:[{
              data: []
            }]
          };
          monthStateChart.setOption(option);
        }
      }).catch(err => {});
    }
    //重置月订单交易状态数量数据查询日期
    const resetMonthState = () => {
      monthStateRange.value = [];
      findMonthState();
    }
    return {monthStateRange,monthStateOption,
      findMonthState,resetMonthState,changeShopId
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