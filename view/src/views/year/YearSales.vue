<!-- 年销售额 -->
<template>
  <div>
    <div class="crumb-title">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-data-analysis"></i> 年销售额
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-card>
      <div>
        <!-- 年销售额列表查询条件-->
        <div class="handle-box">
          <el-select v-model="query.shopId" placeholder="请选择店铺" class="handle-select mr10" style="width: 250px">
            <el-option v-for="item in userShop" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
          <div class="block" style="float: left; margin-right: 10px;">
            <el-date-picker v-model="query.year" type="year" placeholder="选择年份"></el-date-picker>
          </div>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch" round>搜索</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset" round>重置</el-button>
        </div>
        <!-- 店铺信息列表-->
        <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
          <el-table-column prop="shopId" label="年份" align="center"></el-table-column>
          <el-table-column prop="shopNm" label="店铺名称" align="center"></el-table-column>
          <el-table-column prop="retPro" label="年净利润" align="center"></el-table-column>
          <el-table-column prop="retProRat" label="年净利率" align="center"></el-table-column>
          <el-table-column prop="groPro" label="年毛利润" align="center"></el-table-column>
          <el-table-column prop="groProRat" label="年毛利率" align="center"></el-table-column>
          <el-table-column prop="talOrd" label="年订单数" align="center"></el-table-column>
          <el-table-column prop="sucOrd" label="年成交单数" align="center"></el-table-column>
          <el-table-column prop="faiOrd" label="年失败单数" align="center"></el-table-column>
          <el-table-column prop="salAmt" label="年销售额" align="center"></el-table-column>
          <el-table-column prop="cosAmt" label="年成本费" align="center"></el-table-column>
          <el-table-column prop="advAmt" label="年推广费" align="center"></el-table-column>
          <el-table-column prop="serAmt" label="年服务费" align="center"></el-table-column>
          <el-table-column prop="virAmt" label="年刷单费" align="center"></el-table-column>
        </el-table>
        <div class="pagination">
          <el-pagination background layout="total, prev, pager, next" :current-page="query.pageNum"
                         :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange">
          </el-pagination>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, getCurrentInstance } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import request from '../../utils/request';

export default {
  name: "yearSales",
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 查询条件
    const query = reactive({
      shopId: "",
      year: "",
      pageNum: 1,
      pageSize: 10
    });
    // 表格数据
    const tableData = ref([]);
    // 页数
    const pageTotal = ref(0);
    // 用户的所有店铺信息
    const userShop = ref([]);
    // 获取表格数据
    const getData = () => {
      axios.$http.get(request.yearList,query).then(function (res) {
        if(res.code === 200){
          tableData.value = res.data;
          pageTotal.value = res.totalNum;
        }else if(res.code === 500){
          ElMessage.error('查询失败');
        }
      })
    };
    getData();
    // 查询用户的所有店铺信息
    const getUserShopArr = () => {
      axios.$http.get(request.userShop, null).then(function (res) {
        if(res.code === 200){
          shopTpCode.value = res.data;
        }
      });
    };
    getUserShopArr();
    // 查询
    const handleSearch = () => {
      query.pageNum = 1;
      getData();
    };
    // 重置
    const handleReset = () => {
      query.shopId = "";
      query.year = "";
      getData();
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageNum = val;
      getData();
    };
    return { query,tableData,pageTotal,userShop,
      handleSearch,handleReset,handlePageChange};
  }
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}
.handle-select {
  width: 120px;
}
.handle-input {
  width: 200px;
  display: inline-block;
}
.table {
  width: 100%;
  font-size: 14px;
}
.mr10 {
  margin-right: 10px;
}
.red {
  color: #ff0000;
}
</style>
