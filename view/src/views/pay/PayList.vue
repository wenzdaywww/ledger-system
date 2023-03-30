<!-- 支出信息列表 -->
<template>
  <div>
    <el-card>
      <div v-loading="pageLoading">
        <!-- 店铺信息列表查询条件-->
        <div class="handle-box">
          <el-row>
            <el-col :span="4">
              <div class="block">
                <el-date-picker v-model="datRange" type="daterange" style="width:270px;" format="YYYY-MM-DD" value-format="YYYY-MM-DD"
                                nge-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
                </el-date-picker>
              </div>
            </el-col>
            <el-col :span="2.5">
              <el-select v-model="query.shopId" placeholder="请选择店铺" class="handle-select mr10" style="width: 250px" clearable="true">
                <el-option v-for="item in userShop" :key="item.value" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="2.5">
              <el-select v-model="query.payTp" placeholder="支出类型" class="handle-select mr10" style="width: 250px" clearable="true">
                <el-option v-for="item in payType" :key="item.value" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" icon="el-icon-search" @click="handleSearch" round plain>查询</el-button>
              <el-button icon="el-icon-refresh-left" @click="handleReset" round plain>重置</el-button>
              <el-button type="danger" icon="el-icon-plus" @click="handleAdd" round plain>新增支出</el-button>
            </el-col>
          </el-row>
        </div>
        <!-- 店铺信息列表-->
        <el-table :data="tableData" border class="table" ref="multipleTable"
                  :row-style="{height:'55px'}" :cell-style="{padding:'0px'}" header-cell-class-name="table-header">
          <el-table-column prop="payDat" label="支出日期" align="center" sortable></el-table-column>
          <el-table-column prop="shopNm" label="店铺名称" align="center" sortable></el-table-column>
          <el-table-column prop="payTp" label="支付类型" v-if="false" align="center" sortable></el-table-column>
          <el-table-column prop="payNm" label="支付类型" align="center" sortable></el-table-column>
          <el-table-column prop="payAmt" label="支付金额" align="center"></el-table-column>
          <el-table-column prop="remark" label="备注" align="center">
            <template #default="scope">
              <el-tooltip class="item" effect="light" :content="scope.row.remark" placement="top">
                <span class="ellipsis-line1">{{scope.row.remark}}</span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template #default="scope">
              <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑 </el-button>
              <el-button type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.row)">删除 </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination">
          <el-pagination background layout="total, prev, pager, next" :current-page.sync="query.pageNum"
                         :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange">
          </el-pagination>
        </div>
      </div>
      <!-- 新增/编辑支出信息弹出框 -->
      <pay-edit ref="payDialog" @findPayList="findPayList"></pay-edit>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, getCurrentInstance } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import request from '../../utils/request';
import payEdit from "./PayEdit.vue";

export default {
  name: "payList",
  components: { payEdit },
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 查询条件
    let query = reactive({
      shopId: "",
      payTp: "",
      strDat: "",
      endDat: "",
      pageNum: 1,
      pageSize: 10
    });
    //日期范围选择器
    const datRange = ref([]);
    // 整个页面Loading 加载遮罩层控制
    const pageLoading = ref(false);
    // 表格数据
    const tableData = ref([]);
    //支出信息弹出框对象
    const payDialog = ref();
    // 页数
    const pageTotal = ref(0);
    // 用户的所有店铺信息
    const userShop = ref([]);
    // 支出类型
    const payType = ref([]);
    // 查询
    const handleSearch = () => {
      query.pageNum = 1;
      findPayList();
    };
    // 重置
    const handleReset = () => {
      query.shopId = "";
      query.payTp = "";
      query.strDat = "";
      query.endDat = "";
      datRange.value = [];
      findPayList();
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageNum = val;
      findPayList();
    };
    //编辑店铺
    const handleEdit = (row) => {
      payDialog.value.openPayDialog(row,userShop,payType);//调用子组件方法
    };
    //新增店铺
    const handleAdd = () => {
      payDialog.value.openPayDialog(null,userShop,payType);//调用子组件方法
    };
    // 获取表格数据
    const findPayList = () => {
      pageLoading.value = true;
      if (datRange.value){
        query.strDat = datRange.value[0];
        query.endDat = datRange.value[1];
      }
      axios.$http.get(request.payList,query).then(function (res) {
        pageLoading.value = false;
        tableData.value = res.data;
        pageTotal.value = res.totalNum;
      }).catch(err => {pageLoading.value = false;});
    };
    findPayList();
    //删除支出信息
    const handleDelete = (row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除店铺【" + row.shopNm + "】"+ row.payDat + "的支出信息吗？", "提示",
          {confirmButtonText: '确定',cancelButtonText: '关闭',type: 'warning',center: true,roundButton: true}
      ).then(() => {
        pageLoading.value = true;
        axios.$http.post(request.payDel+row.payId, null).then(function (res) {
          pageLoading.value = false;
          ElMessage.success(res.data);
          findPayList();
        }).catch(err => {pageLoading.value = false;});
      }).catch(() => {});
    };
    // 查询用户的所有店铺信息
    const getUserShopArr = () => {
      axios.$http.get(request.userShop, null).then(function (res) {
        userShop.value = res.data;
      });
    };
    getUserShopArr();
    // 查询数据字典
    const getCodeDataArr = () => {
      axios.$http.get(request.code + "PayType", null).then(function (res) {
        payType.value = res.data.PayType;
      });
    };
    getCodeDataArr();
    return { query,tableData,pageTotal,pageLoading,datRange,userShop,payType,payDialog,
      handleSearch,handleReset,handlePageChange,handleEdit,handleAdd,handleDelete,findPayList};
  }
};
</script>

<style scoped>
.btn-save{
  text-align: center;
}
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
