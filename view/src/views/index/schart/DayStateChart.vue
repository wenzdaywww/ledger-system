<template>
  <div>
    <!--  日订单交易状态数量数据-->
    <el-row>
      <div class="content-title">日订单交易状态数量查询</div>
      <div class="schart-box">
        <div>
          <el-row style="margin-bottom: 5px;">
            <el-col :span="2.5" style="line-height: 2.5;">
              日期范围：
            </el-col>
            <el-col :span="6">
              <el-tooltip class="item" effect="light" content="日期范围超过60天，最多查询60天订单交易状态数据" placement="top">
                <div>
                  <el-date-picker v-model="dayStateRange" type="daterange" style="width:300px;margin-right: 20px;"
                                  format="YYYY-MM-DD" value-format="YYYY-MM-DD" nge-separator="至" start-placeholder="开始日期" end-placeholder="截止日期">
                  </el-date-picker>
                </div>
              </el-tooltip>
            </el-col>
            <el-col :span="2">
              <el-button type="primary" icon="el-icon-search" @click="findDayState" round plain>查询</el-button>
            </el-col>
            <el-col :span="2">
              <el-button icon="el-icon-refresh-left" @click="resetDayState" round plain>重置</el-button>
            </el-col>
          </el-row>
        </div>
        <div class="schart" id="day-state-pie"></div>
      </div>
    </el-row>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, ref} from "vue";
import request from "../../../utils/request";
import {getInstanceByDom, init} from "echarts";

export default {
  name: "dayStateChart",
  mounted() {
    let initChart = init(document.getElementById("day-state-pie"));
    //设置echarts对象的option属性
    initChart.setOption(this.dayStateOption);
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
    const chartTitle = "{0} 至 {1} 日订单状态数量比例图";
    //日订单交易状态数量数据日期范围选择器
    const dayStateRange = ref([]);
    //日订单交易状态数量数据
    const dayStateOption = ref({
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
              formatter: '{b}（{d}%）: {c}' //扇形数据显示格式
            }
          },
          data: [] //折线数据
        }
      ]
    });
    //店铺改变后重新查询
    const changeShopId = (shopIdTemp) => {
      query.shopId = shopIdTemp;
      findDayState();
    }
    //查询日订单交易状态数量数据
    const findDayState = () => {
      if (dayStateRange.value){
        query.startDate = dayStateRange.value[0];
        query.endDate = dayStateRange.value[1];
      }else {
        query.startDate = "";
        query.endDate = "";
      }
      axios.$http.get(request.dayState,query).then(function (res) {
        let dayStateChart = getInstanceByDom(document.getElementById("day-state-pie"));
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
          dayStateChart.setOption(option);
        }else {
          let option = {
            title: {text:"查询不到日订单状态数量"},
            graphic:[{
              style:{
                text: "总数：0"
              }
            }],
            series:[{
              data: []
            }]
          };
          dayStateChart.setOption(option);
        }
      }).catch(err => {});
    }
    //重置日订单交易状态数量数据查询日期
    const resetDayState = () => {
      dayStateRange.value = [];
      findDayState();
    }
    return {dayStateRange,dayStateOption,
      findDayState,resetDayState,changeShopId
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
  color: red;
  text-align: center;
  width: 100%;
}
</style>