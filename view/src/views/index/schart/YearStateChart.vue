<template>
  <div>
    <!--  年订单交易状态数量数据-->
    <el-row>
      <div class="content-title">年订单交易状态数量查询</div>
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
              <el-button type="primary" icon="el-icon-search" @click="findYearState" round plain>查询</el-button>
            </el-col>
            <el-col :span="2">
              <el-button icon="el-icon-refresh-left" @click="resetYearState" round plain>重置</el-button>
            </el-col>
          </el-row>
        </div>
        <div class="schart" id="year-state-pie"></div>
      </div>
    </el-row>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, ref} from "vue";
import request from "../../../utils/request";
import {getInstanceByDom, init} from "echarts";

export default {
  name: "yearStateChart",
  mounted() {
    let initChart = init(document.getElementById("year-state-pie"));
    //设置echarts对象的option属性
    initChart.setOption(this.yearStateOption);
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
    const chartTitle = "{0} 至 {1} 年订单交易状态数量比例图";
    //年订单交易状态数量数据
    const yearStateOption = ref({
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
      findYearState();
    }
    //查询年订单交易状态数量数据
    const findYearState = () => {
      axios.$http.get(request.yearState,query).then(function (res) {
        let yearStateChart = getInstanceByDom(document.getElementById("year-state-pie"));
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
          yearStateChart.setOption(option);
        }else {
          let option = {
            title: {text:"查询不到月订单状态数量"},
            graphic:[{
              style:{
                text: "总数：0"
              }
            }],
            series:[{
              data: []
            }]
          };
          yearStateChart.setOption(option);
        }
      }).catch(err => {});
    }
    //重置年订单交易状态数量数据查询日期
    const resetYearState = () => {
      query.startDate = "";
      query.endDate = "";
      findYearState();
    }
    return {query,yearStateOption,
      findYearState,resetYearState,changeShopId
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