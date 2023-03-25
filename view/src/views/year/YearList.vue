<!-- 年销售额 -->
<template>
  <div>
    <el-card>
      <div>
        <!-- 年销售额列表查询条件-->
        <div class="handle-box">
          <el-select v-model="query.shopId" placeholder="请选择店铺" class="handle-select mr10" style="width: 250px">
            <el-option v-for="item in userShop" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
          <div class="block" style="float: left; margin-right: 10px;">
            <el-date-picker v-model="query.year" type="year" format="YYYY" value-format="YYYY" placeholder="选择年份"></el-date-picker>
          </div>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch" round plain>搜索</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset" round plain>重置</el-button>
          <el-tooltip class="item" effect="light" content="根据月销售统计年销售额" placement="top">
            <el-button type="success" icon="el-icon-s-data" @click="handleCount" round plain>统计年销售</el-button>
          </el-tooltip>
        </div>
        <!-- 店铺信息列表-->
        <el-table :data="tableData" border class="table" ref="multipleTable" :cell-class-name="addCellClass" header-cell-class-name="table-header">
          <el-table-column prop="year" label="年份" align="center" sortable></el-table-column>
          <el-table-column prop="shopNm" label="店铺名称" align="center" sortable></el-table-column>
          <el-table-column prop="retPro" label="年净利润" align="center">
            <template v-slot:header='scope'>
              <span>年净利润
                <el-tooltip :aa="scope" class="item" effect="light" content="年净利润=年销售额 - 年支出费" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="retProRat" label="年净利率" align="center">
            <template v-slot:header='scope'>
              <span>年净利率
                <el-tooltip :aa="scope" class="item" effect="light" content="年净利率=年净利润 / 年支出费 * 100%" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="groPro" label="年毛利润" align="center">
            <template v-slot:header='scope'>
              <span>年毛利润
                <el-tooltip :aa="scope" class="item" effect="light" content="年毛利润=年销售额 - 年成本费" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="groProRat" label="年毛利率" align="center">
            <template v-slot:header='scope'>
              <span>年毛利率
                <el-tooltip :aa="scope" class="item" effect="light" content="年毛利率=年毛利润 / 年成本费 * 100%" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="talOrd" label="年订单数" align="center"></el-table-column>
          <el-table-column prop="sucOrd" label="年成交单数" align="center"></el-table-column>
          <el-table-column prop="faiOrd" label="年失败单数" align="center"></el-table-column>
          <el-table-column prop="salAmt" label="年销售额" align="center"></el-table-column>
          <el-table-column prop="cosAmt" label="年成本费" align="center"></el-table-column>
          <el-table-column prop="advAmt" label="年推广费" align="center"></el-table-column>
          <el-table-column prop="serAmt" label="年服务费" align="center"></el-table-column>
          <el-table-column prop="virAmt" label="年刷单费" align="center"></el-table-column>
          <el-table-column prop="talCos" label="年支出费" align="center">
            <template v-slot:header='scope'>
              <span>年支出费
                <el-tooltip :aa="scope" class="item" effect="light" content="年支出费=年成本费 + 年推广费 + 年服务费 + 年刷单费" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
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
    // 查询
    const handleSearch = () => {
      query.pageNum = 1;
      findYearList();
    };
    //列添加颜色
    const addCellClass = ({row, column, rowIndex, columnIndex}) => {
      //列的label的名称
      if (row.retPro > 0) {
        return 'col-red';
      }else if(row.retPro < 0){
        return 'col-green';
      }else {
        return 'col-gray';
      }
    }
    // 重置
    const handleReset = () => {
      query.shopId = "";
      query.year = "";
      findYearList();
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageNum = val;
      findYearList();
    };
    // 统计年销售
    const handleCount = () => {
      axios.$http.post(request.yearCount,null).then(function (res) {
        ElMessage.success(res.data);
        findYearList();
      })
    };
    // 获取表格数据
    const findYearList = () => {
      axios.$http.get(request.yearList,query).then(function (res) {
        tableData.value = res.data;
        pageTotal.value = res.totalNum;
      })
    };
    findYearList();
    // 查询用户的所有店铺信息
    const getUserShopArr = () => {
      axios.$http.get(request.userShop, null).then(function (res) {
        userShop.value = res.data;
      });
    };
    getUserShopArr();
    return { query,tableData,pageTotal,userShop,
      handleSearch,handleReset,handlePageChange,handleCount,addCellClass};
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
/deep/.col-red {
  color: red;
}
/deep/.col-green {
  color: #07bd07;
}
/deep/.col-gray {
  color: black ;
}
</style>
