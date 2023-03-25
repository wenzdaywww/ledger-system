<!-- 商品信息列表 -->
<template>
  <div>
    <div class="crumb-title">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-s-goods"></i> 商品信息
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-card>
      <div>
        <!-- 店铺信息列表查询条件-->
        <div class="handle-box">
          <el-input v-model="query.shopId" placeholder="店铺ID" class="handle-input mr10"></el-input>
          <el-input v-model="query.shopNm" placeholder="店铺名称" class="handle-input mr10"></el-input>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch" round>搜索</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset" round>重置</el-button>
          <el-button type="danger" icon="el-icon-plus" @click="handleAdd" round>新增店铺</el-button>
        </div>
        <!-- 店铺信息列表-->
        <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
          <el-table-column prop="shopId" label="店铺ID" align="center"></el-table-column>
          <el-table-column prop="shopNm" label="店铺名称" align="center"></el-table-column>
          <el-table-column prop="shopTp" label="店铺平台" align="center"></el-table-column>
          <el-table-column prop="retPro" label="店净利润" align="center"></el-table-column>
          <el-table-column prop="retProRat" label="店净利率" align="center"></el-table-column>
          <el-table-column prop="groPro" label="店毛利润" align="center"></el-table-column>
          <el-table-column prop="groProRat" label="店毛利率" align="center"></el-table-column>
          <el-table-column prop="talOrd" label="店订单数" align="center"></el-table-column>
          <el-table-column prop="sucOrd" label="店成交单数" align="center"></el-table-column>
          <el-table-column prop="faiOrd" label="店失败单数" align="center"></el-table-column>
          <el-table-column prop="salAmt" label="店销售额" align="center"></el-table-column>
          <el-table-column prop="cosAmt" label="店成本费" align="center"></el-table-column>
          <el-table-column prop="advAmt" label="店推广费" align="center"></el-table-column>
          <el-table-column prop="serAmt" label="店服务费" align="center"></el-table-column>
          <el-table-column prop="virAmt" label="店刷单费" align="center"></el-table-column>
          <el-table-column label="操作" align="center">
            <template #default="scope">
              <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑 </el-button>
              <el-button type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.row)">删除 </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination">
          <el-pagination background layout="total, prev, pager, next" :current-page="query.pageNum"
                         :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange">
          </el-pagination>
        </div>
      </div>
      <!-- 新增/编辑店铺弹出框 -->
      <el-dialog title="修改店铺" v-model="editVisible" width="450px">
        <el-form label-width="120px" :model="shopInfo" :rules="shopRules" ref="shopForm">
          <el-form-item label="店铺ID">
            <el-input v-model="shopInfo.shopId" :disabled="true" style="width: 250px"></el-input>
          </el-form-item>
          <el-form-item label="店铺名称" prop="shopNm">
            <el-input v-model="shopInfo.shopNm" style="width: 250px" maxlength="20" placeholder="请输入店铺名称"></el-input>
          </el-form-item>
          <el-form-item label="店铺平台" prop="shopTp">
            <el-select v-model="shopInfo.shopTp" placeholder="请选择店铺平台" class="handle-select mr10" style="width: 250px">
              <el-option v-for="item in shopTpCode" :key="item.value" :label="item.name" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
        <div class="btn-save">
          <span class="dialog-footer">
            <el-button type="primary" @click="saveEdit" class="el-icon-check" round>确 定</el-button>
            <el-button @click="editVisible = false" class="el-icon-close" round>取 消</el-button>
          </span>
        </div>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, getCurrentInstance } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import request from '../../utils/request';
import utils from "../../utils/utils";

export default {
  name: "shopList",
  setup() {
    // 查询条件
    const query = reactive({
      shopId: "",
      shopNm: "",
      pageNum: 1,
      pageSize: 10
    });
    // 编辑店铺信息
    let shopInfo = reactive({
      shopId: "",
      shopNm : "",
      shopTp: ""
    });
    // 店铺信息的规则校验
    const shopRules = {
      shopNm : [
        { required: true, message: "店铺名称不能为空", trigger: "blur" }
      ],
      shopTp : [
        { required: true, message: "店铺平台不能为空", trigger: "blur" }
      ]
    };
    // 表格数据
    const tableData = ref([]);
    // 页数
    const pageTotal = ref(0);
    // 店铺平台
    const shopTpCode = ref([]);
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 新增/编辑店铺弹窗控制
    const editVisible = ref(false);
    // 编辑店铺表单数据
    const shopForm = ref(null);
    // 获取表格数据
    const getData = () => {
      axios.$http.get(request.shopList,query).then(function (res) {
        tableData.value = res.data;
        pageTotal.value = res.totalNum;
      })
    };
    getData();
    // 查询
    const handleSearch = () => {
      query.pageNum = 1;
      getData();
    };
    // 重置
    const handleReset = () => {
      query.shopId = "";
      query.shopNm = "";
      getData();
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageNum = val;
      getData();
    };
    //编辑店铺
    const handleEdit = (row) => {
      shopInfo.shopId = row.shopId;
      shopInfo.shopNm = row.shopNm;
      editVisible.value = true;
    };
    //删除店铺
    const handleDelete = (row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示",
          {confirmButtonText: '确定',cancelButtonText: '关闭',type: 'warning',center: true,roundButton: true}
      ).then(() => {
        axios.$http.post(request.delShop+row.shopId, null).then(function (res) {
          ElMessage.success('删除成功');
          getData();
        });
      }).catch(() => {});
    };
    //新增店铺
    const handleAdd = () => {
      shopInfo.shopId = null;
      shopInfo.shopNm = null;
      editVisible.value = true;
    };
    // 查询数据字典
    const getCodeDataArr = () => {
      axios.$http.get(request.code + "ShopPlatform", null).then(function (res) {
        shopTpCode.value = res.data.ShopPlatform;
      });
    };
    getCodeDataArr();
    // 新增/编辑店铺页面的保存按钮
    const saveEdit = () => {
      shopForm.value.validate((valid) => {
        if (valid) {
          axios.$http.post(shopInfo.shopId ? request.shopEdit : request.newShop,shopInfo).then(function (res) {
            editVisible.value = false;
            ElMessage.success(shopInfo.shopId ? '修改成功' : '新增成功');
            getData();
          });
        } else {
          return false;
        }
      });
    };
    return { query,tableData,pageTotal,editVisible,shopForm,shopRules,shopInfo,shopTpCode,
      handleSearch,handleReset,handlePageChange,handleEdit,saveEdit,handleAdd,handleDelete};
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
</style>
