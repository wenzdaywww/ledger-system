<!-- 首页 -->
<template>
  <div style="height: 100%;">
    <v-header />
    <div class="index-div">
      <div>
        <el-row>
          <el-col :span="3">
          </el-col>
          <el-col :span="18">
            <data-charts v-if="showChart"></data-charts>
          </el-col>
          <el-col :span="3">
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>
<script>

import dataCharts from "./Charts.vue";
import {ref} from "vue";
import utils from "../../utils/utils";

export default {
  name: "Index",
  components: {dataCharts},
  mounted() {
    //定义可被同级页面调用的方法
    window['hidenChart'] = () => {
      this.hidenChart();
    }
  },
  setup() {
    // 是否已登录
    const showChart = ref(false);
    if (utils.isLogin()){
      showChart.value = true;
    }else {
      showChart.value = false;
    }
    //隐藏图表，退出登录调用
    const hidenChart = () => {
      showChart.value = false;
    }
    return {showChart,hidenChart};
  },
};
</script>
<style scoped>
.index-div{
  height: calc(100% - 70px);
  overflow-y: auto; /* 设置纵向滚动条 */
}
</style>