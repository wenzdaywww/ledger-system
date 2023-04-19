<template>
  <div>
    <div class="right-rank-div">
      <!-- 日销售额排行榜 -->
      <el-row>
        <el-col :span="24">
          <day-sales-rank ref="daySalesRankObj"></day-sales-rank>
        </el-col>
      </el-row>
      <!-- 日订单量排行榜 -->
      <el-row>
        <el-col :span="24">
          <day-order-rank ref="dayOrderRankObj"></day-order-rank>
        </el-col>
      </el-row>
      <!-- 月销售额排行榜 -->
      <el-row>
        <el-col :span="24">
          <month-sales-rank ref="monthSalesRankObj"></month-sales-rank>
        </el-col>
      </el-row>
      <!-- 月订单量排行榜 -->
      <el-row>
        <el-col :span="24">
          <month-order-rank ref="monthOrderRankObj"></month-order-rank>
        </el-col>
      </el-row>
      <!-- 年销售额排行榜 -->
      <el-row>
        <el-col :span="24">
          <year-sales-rank ref="yearSalesRankObj"></year-sales-rank>
        </el-col>
      </el-row>
      <!-- 年订单量排行榜 -->
      <el-row>
        <el-col :span="24">
          <year-order-rank ref="yearOrderRankObj"></year-order-rank>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>

import {ref} from "vue";
import daySalesRank from "./right/DaySalesRank.vue"
import dayOrderRank from "./right/DayOrderRank.vue"
import monthSalesRank from "./right/MonthSalesRank.vue"
import monthOrderRank from "./right/MonthOrderRank.vue"
import yearSalesRank from "./right/YearSalesRank.vue"
import yearOrderRank from "./right/YearOrderRank.vue"

export default {
  name: "RightRank",
  components: {daySalesRank,dayOrderRank,monthSalesRank,monthOrderRank,yearSalesRank,yearOrderRank},
  setup() {
    //日销售额排行榜
    const daySalesRankObj = ref();
    //日订单量排行榜
    const dayOrderRankObj = ref();
    //月销售额排行榜
    const monthSalesRankObj = ref();
    //月订单量排行榜
    const monthOrderRankObj = ref();
    //年销售额排行榜
    const yearSalesRankObj = ref();
    //年订单量排行榜
    const yearOrderRankObj = ref();
    // 店铺变动重新查询
    const shopChange = (item) => {
      //日销售额排行榜查询
      daySalesRankObj.value.changeShopId(item);
      //日销售额排行榜查询
      dayOrderRankObj.value.changeShopId(item);
      //月销售额排行榜查询
      monthSalesRankObj.value.changeShopId(item);
      //月销售额排行榜查询
      monthOrderRankObj.value.changeShopId(item);
      //年销售额排行榜查询
      yearSalesRankObj.value.changeShopId(item);
      //年销售额排行榜查询
      yearOrderRankObj.value.changeShopId(item);
    }
    return {daySalesRankObj,dayOrderRankObj,monthSalesRankObj,monthOrderRankObj,yearSalesRankObj,yearOrderRankObj,
      shopChange
    };
  },
};
</script>

<style scoped>
.left-rank-box {
  display: inline-block;
  width: 100%;
}
.right-rank-div{
  margin-left: 3px;
  margin-right: 3px;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
}
</style>