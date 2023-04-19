<template>
  <div class="rank-box">
    <!--  年销售额排行榜-->
    <div class="schart" id="year-sales-rank"></div>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, ref} from "vue";
import request from "../../../utils/request";
import {getInstanceByDom, init} from 'echarts';

export default {
  name: "yearSalesRank",
  components: {},
  mounted() {
    let initChart = init(document.getElementById("year-sales-rank"));
    //设置echarts对象的option属性
    initChart.setOption(this.yearSalesOption);
  },
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 查询条件
    const query = reactive({
      shopId: ""
    });
    //报表标题
    const rankTitle = "{0} 年销售额排行榜";
    //报表标题
    const noOrderTitle = "没有年销售额数据";
    //年销售额数据
    const yearSalesOption = ref({
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
      findYearSalesRank();
    }
    //年销售排行榜
    const findYearSalesRank = () => {
      axios.$http.get(request.yearSalesRank,query).then(function (res) {
        let yearSalesRank = getInstanceByDom(document.getElementById("year-sales-rank"));
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
          let oldOption = yearSalesRank.getOption();
          for (let i = 0; i < oldOption.series.length; ++i) {
            option.series.push({name:"",data:[]});
          }
        }
        yearSalesRank.setOption(option);
      }).catch(err => {});
    }
    return {yearSalesOption,
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
  height: 300px;
}
</style>