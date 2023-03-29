<!-- 日销售额 -->
<template>
  <div>
    <el-card>
      <div v-loading="pageLoading">
        <!-- 日销售额列表查询条件-->
        <div class="handle-box">
          <el-row>
            <el-col :span="4">
              <div class="block">
                <el-date-picker v-model="datRange" type="daterange" style="width:260px;" format="YYYY-MM-DD" value-format="YYYY-MM-DD"
                                nge-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
                </el-date-picker>
              </div>
            </el-col>
            <el-col :span="2.5">
              <el-select v-model="query.shopId" placeholder="请选择店铺" class="handle-select mr10" style="width: 170px">
                <el-option v-for="item in userShop" :key="item.value" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <el-tooltip class="item" effect="light" content="按店铺查：即查询店铺的日销售额&nbsp;&nbsp;&nbsp;按日查：即查询所有店铺的日销售额" placement="top">
                <el-switch v-model="query.all" inactive-text="按店铺查" active-text="按日期查" style="margin-right: 10px;"></el-switch>
              </el-tooltip>
              <el-button type="primary" icon="el-icon-search" @click="handleSearch" round plain>查询</el-button>
              <el-button icon="el-icon-refresh-left" @click="handleReset" round plain>重置</el-button>
              <el-tooltip class="item" effect="light" content="根据订单信息统计日销售额" placement="top">
                <el-button type="success" icon="el-icon-s-data" @click="handleCount" round plain>统计日销售</el-button>
              </el-tooltip>
            </el-col>
          </el-row>
        </div>
        <!-- 店铺信息列表-->
        <el-table :data="tableData" border class="table" ref="multipleTable" :cell-class-name="addCellClass" header-cell-class-name="table-header">
          <el-table-column prop="day" label="日期" align="center" sortable></el-table-column>
          <el-table-column prop="shopNm" label="店铺名称" align="center" sortable width="120px;">
            <template v-slot:header='scope'>
              <span>店铺名称
                <el-tooltip :aa="scope" class="item" effect="light" content="店铺名称为空，则该笔数据是所有店铺汇总的日销售额数据" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="retPro" label="日净利润" align="center">
            <template v-slot:header='scope'>
              <span>日净利润
                <el-tooltip :aa="scope" class="item" effect="light" content="日净利润 = 日销售额 - 日支出费" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="retProRat" label="日净利率" align="center">
            <template v-slot:header='scope'>
              <span>日净利率
                <el-tooltip :aa="scope" class="item" effect="light" content="日净利率 = 日净利润 / 日支出费 * 100%" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="groPro" label="日毛利润" align="center">
            <template v-slot:header='scope'>
              <span>日毛利润
                <el-tooltip :aa="scope" class="item" effect="light" content="日毛利润 = 日销售额 - 日成本费" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="groProRat" label="日毛利率" align="center">
            <template v-slot:header='scope'>
              <span>日毛利率
                <el-tooltip :aa="scope" class="item" effect="light" content="日毛利率 = 日毛利润 / 日成本费 * 100%" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="talOrd" label="日订单量" align="center">
            <template v-slot:header='scope'>
              <span>日订单量
                <el-tooltip :aa="scope" class="item" effect="light" content="本日订单数量合计" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="sucOrd" label="日成交单" align="center">
            <template v-slot:header='scope'>
              <span>日成交单
                <el-tooltip :aa="scope" class="item" effect="light" content="本日订单状态为【交易成功】、【已发货，待签收】的数量合计" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="faiOrd" label="日流失单" align="center">
            <template v-slot:header='scope'>
              <span>日流失单
                <el-tooltip :aa="scope" class="item" effect="light" content="日流失单 = 日订单量 - 日成交单" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="salAmt" label="日销售额" align="center">
            <template v-slot:header='scope'>
              <span>日销售额
                <el-tooltip :aa="scope" class="item" effect="light" content="本日订单状态为【交易成功】、【已发货，待签收】的销售额合计" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="cosAmt" label="日成本费" align="center">
            <template v-slot:header='scope'>
              <span>日成本费
                <el-tooltip :aa="scope" class="item" effect="light" content="本日订单状态为【交易成功】、【已发货，待签收】的总成本费合计" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="virAmt" label="日刷单费" align="center">
            <template v-slot:header='scope'>
              <span>日刷单费
                <el-tooltip :aa="scope" class="item" effect="light" content="本日订单状态为【刷单】的总成本费合计" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="talCos" label="日支出费" align="center">
            <template v-slot:header='scope'>
              <span>日支出费
                <el-tooltip :aa="scope" class="item" effect="light" content="日支出费 = 日成本费 + 日推广费 + 日服务费 + 日刷单费" placement="top">
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
      strDat: "",
      endDat: "",
      all: false,
      pageNum: 1,
      pageSize: 10
    });
    //日期范围选择器
    const datRange = ref([]);
    // 整个页面Loading 加载遮罩层控制
    const pageLoading = ref(false);
    // 表格数据
    const tableData = ref([]);
    // 页数
    const pageTotal = ref(0);
    // 用户的所有店铺信息
    const userShop = ref([]);
    // 查询
    const handleSearch = () => {
      query.pageNum = 1;
      findDayList();
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
      query.strDat = "";
      query.endDat = "";
      query.all = false;
      datRange.value = [];
      findDayList();
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageNum = val;
      findDayList();
    };
    // 统计日销售
    const handleCount = () => {
      pageLoading.value = true;
      axios.$http.post(request.dayCount,null).then(function (res) {
        pageLoading.value = false;
        ElMessage.success(res.data);
        findDayList();
      }).catch(err => {pageLoading.value = false;});
    };
    // 获取表格数据
    const findDayList = () => {
      pageLoading.value = true;
      if (datRange.value){
        query.strDat = datRange.value[0];
        query.endDat = datRange.value[1];
      }
      axios.$http.get(request.dayList,query).then(function (res) {
        pageLoading.value = false;
        tableData.value = res.data;
        pageTotal.value = res.totalNum;
      }).catch(err => {pageLoading.value = false;});
    };
    findDayList();
    // 查询用户的所有店铺信息
    const getUserShopArr = () => {
      axios.$http.get(request.userShop, null).then(function (res) {
        userShop.value = res.data;
      });
    };
    getUserShopArr();
    return { query,tableData,pageTotal,userShop,pageLoading,datRange,
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
