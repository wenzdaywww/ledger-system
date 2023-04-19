<template>
  <div>
    <div class="right-rank-div">
      <!-- 店铺销售额排行榜 -->
      <el-row>
        <el-col :span="24">
          <shop-sales-rank></shop-sales-rank>
        </el-col>
      </el-row>
      <!-- 店铺订单量排行榜 -->
      <el-row>
        <el-col :span="24">
          <shop-order-rank></shop-order-rank>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>

import shopSalesRank from "./left/ShopSalesRank.vue"
import shopOrderRank from "./left/ShopOrderRank.vue"

export default {
  name: "LeftRank",
  components: {shopSalesRank,shopOrderRank},
  setup() {
    return {};
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