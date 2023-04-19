<template>
  <div class="rank-box">
    <!--  店铺订单量排行榜-->
    <div class="schart" id="shop-order-rank"></div>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, ref} from "vue";
import request from "../../../utils/request";
import {getInstanceByDom, init} from 'echarts';

export default {
  name: "shopOrderRank",
  components: {},
  mounted() {
    let initChart = init(document.getElementById("shop-order-rank"));
    //设置echarts对象的option属性
    initChart.setOption(this.shopOrderOption);
  },
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    //报表标题
    const rankTitle = "店铺订单量排行榜";
    //店铺订单量数据
    const shopOrderOption = ref({
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
    //店铺销售排行榜
    const findShopOrderRank = () => {
      axios.$http.get(request.shopOrderRank,null).then(function (res) {
        let shopOrderRank = getInstanceByDom(document.getElementById("shop-order-rank"));
        let option = {
          legend: {data:[]},
          yAxis: {data:[]},
          series: []
        };
        if (res.data && res.data.yaxis){
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
          let oldOption = shopOrderRank.getOption();
          for (let i = 0; i < oldOption.series.length; ++i) {
            option.series.push({name:"",data:[]});
          }
        }
        shopOrderRank.setOption(option);
      }).catch(err => {});
    }
    findShopOrderRank();
    return {shopOrderOption
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