<template>
  <div class="rank-box">
    <!--  月销售额排行榜-->
    <div class="schart" id="month-sales-rank"></div>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, ref} from "vue";
import request from "../../../utils/request";
import {getInstanceByDom, init} from 'echarts';

export default {
  name: "monthSalesRank",
  components: {},
  mounted() {
    let initChart = init(document.getElementById("month-sales-rank"));
    //设置echarts对象的option属性
    initChart.setOption(this.monthSalesOption);
  },
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 查询条件
    const query = reactive({
      shopId: ""
    });
    //报表标题
    const rankTitle = "{0} 月销售额排行榜";
    //报表标题
    const noOrderTitle = "没有月销售额数据";
    //月销售额数据
    const monthSalesOption = ref({
      title: {
        text: rankTitle,
        left: "center" //标题居中
      },
      tooltip: {},
      xAxis: {
        type: 'value'
      },
      legend: {
        top: 25,
        data: [] //图例数据
      },
      yAxis: {
        type: 'category',
        show: false,
        data: [] //y轴名称
      },
      series: [] //排行榜数据
    });
    //店铺改变后重新查询
    const changeShopId = (shopIdTemp) => {
      query.shopId = shopIdTemp;
      findMonthSalesRank();
    }
    //月销售排行榜
    const findMonthSalesRank = () => {
      axios.$http.get(request.monthSalesRank,query).then(function (res) {
        let monthSalesRank = getInstanceByDom(document.getElementById("month-sales-rank"));
        let option = {
          title: {text:""},
          legend: {data:[]},
          yAxis: {data:[]},
          series: []
        };
        if (res.data && res.data.yaxis){
          option.title.text = rankTitle.replace("{0}",res.data.shopName);
          option.yAxis.data = res.data.yaxis;
          for (let i = 0; i < res.data.series.length; ++i) {
            let item = res.data.series[i];
            item.type = "bar";
            //上方显示数值
            item.label = {
              show: true,
              position: "top"
            };
            item.stack = 'total';
            item.label = {
              show: true
            };
            item.emphasis = {
              focus: 'series'
            };
            option.legend.data.push(item.name);
            option.series.push(item);
          }
        }else {
          option.title.text = res.data && res.data.shopName ? rankTitle.replace("{0}",res.data.shopName) : noOrderTitle;
          let oldOption = monthSalesRank.getOption();
          for (let i = 0; i < oldOption.series.length; ++i) {
            option.series.push({name:"",data:[]});
          }
        }
        monthSalesRank.setOption(option);
      }).catch(err => {});
    }
    return {monthSalesOption,
      changeShopId
    };
  },
};
</script>

<style scoped>
.rank-box {
  display: inline-block;
  width: 100%;
}
.schart {
  width: 100%;
  height: 500px;
}
</style>