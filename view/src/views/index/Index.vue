<!-- 首页 -->
<template>
  <div style="height: 100%;">
    <v-header />
    <div class="index-div">
      <div>
        <el-row>
          <el-col :span="4">
            <left-rank v-if="showChart"></left-rank>
          </el-col>
          <el-col :span="20">
            <el-row>
              <div class="shop-div" v-if="showChart">
                店铺：
                <el-select v-model="query.shopId" class="handle-select mr10" style="width: 400px;" clearable="true" @change="shopChange">
                  <el-option v-for="item in userShop" :key="item.value" :label="item.name" :value="item.value"></el-option>
                </el-select>
              </div>
            </el-row>
            <el-row>
              <el-col :span="19">
                <data-charts ref="dataChartObj" v-if="showChart"></data-charts>
              </el-col>
              <el-col :span="5">
                <right-rank ref="rightRankObj" v-if="showChart"></right-rank>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>
<script>

import {getCurrentInstance, reactive, ref} from "vue";
import utils from "../../utils/utils";
import request from "../../utils/request";
import dataCharts from "./Charts.vue";
import rightRank from "./RightRank.vue";
import leftRank from "./LeftRank.vue";

export default {
  name: "Index",
  components: {leftRank,dataCharts,rightRank},
  mounted() {
    //定义可被同级页面调用的方法
    window['hidenChart'] = () => {
      this.hidenChart();
    }
  },
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 是否已登录
    const showChart = ref(false);
    // 用户的所有店铺信息
    const userShop = ref([]);
    //图表对象
    const dataChartObj = ref();
    //右边排行榜图表对象
    const rightRankObj = ref();
    // 查询条件
    const query = reactive({
      shopId: ""
    });
    // 查询用户的所有店铺信息
    const getUserShopArr = () => {
      axios.$http.get(request.userShop, null).then(function (res) {
        if (res.data){
          showChart.value = true;
          userShop.value = res.data;
          userShop.value.push({value: "",name: "所有店铺汇总数据"});
        }else {
          showChart.value = false;
        }
      });
    };
    // 店铺变动重新查询
    const shopChange = (item) => {
      dataChartObj.value.shopChange(item);
      rightRankObj.value.shopChange(item);
    }
    //隐藏图表，退出登录调用
    const hidenChart = () => {
      showChart.value = false;
    }
    //判断是否已登录
    if(utils.isLogin()){
      showChart.value = true;
      getUserShopArr();
      //需要子组件加载完后定时执行的代码
      setTimeout(() => {
        shopChange("");
      }, 500);
    }else {
      showChart.value = false;
    }
    return {query,showChart,userShop,dataChartObj,rightRankObj,
      hidenChart,shopChange
    };
  },
};
</script>
<style scoped>
.index-div{
  height: calc(100% - 70px);
  overflow-y: auto; /* 设置纵向滚动条 */
}
.shop-div{
  width: 100%;
  margin-bottom: 3px;
  margin-right: 3px;
  padding: 10px;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  text-align: center;
}
</style>