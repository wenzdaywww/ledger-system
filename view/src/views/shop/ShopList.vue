<!-- 店铺信息列表 -->
<template>
  <div>
    <el-card>
      <div>
        <!-- 店铺信息列表查询条件-->
        <div class="handle-box">
          <el-input v-model="query.shopId" placeholder="店铺ID" class="handle-input mr10"></el-input>
          <el-input v-model="query.shopNm" placeholder="店铺名称" class="handle-input mr10"></el-input>
          <el-select v-model="query.shopTp" placeholder="请选择店铺平台" class="handle-select mr10" style="width: 250px">
            <el-option v-for="item in shopTpCode" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch" round plain>搜索</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset" round plain>重置</el-button>
          <el-button type="danger" icon="el-icon-plus" @click="handleAdd" round plain>新增店铺</el-button>
          <el-tooltip class="item" effect="light" content="根据年销售统计店销售额" placement="top">
            <el-button type="success" icon="el-icon-s-data" @click="handleCount" round plain>统计店销售</el-button>
          </el-tooltip>
        </div>
        <!-- 店铺信息列表-->
        <el-table :data="tableData" border class="table" ref="multipleTable" :cell-class-name="addCellClass"
                  :row-style="{height:'55px'}" :cell-style="{padding:'0px'}" header-cell-class-name="table-header">
          <el-table-column prop="shopId" label="店铺ID" align="center" sortable width="90px"></el-table-column>
          <el-table-column prop="shopNm" label="店铺名称" align="center" sortable></el-table-column>
          <el-table-column prop="shopTp" label="店铺平台ID" v-if="false" align="center"></el-table-column>
          <el-table-column prop="shopTpNm" label="店铺平台" align="center"></el-table-column>
          <el-table-column prop="retPro" label="店净利润" align="center">
            <template v-slot:header='scope'>
              <span>店净利润
                <el-tooltip :aa="scope" class="item" effect="light" content="店净利润=店销售额 - 店支出费" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="retProRat" label="店净利率" align="center">
            <template v-slot:header='scope'>
              <span>店净利率
                <el-tooltip :aa="scope" class="item" effect="light" content="店净利率=店净利润 / 店支出费 * 100%" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="groPro" label="店毛利润" align="center">
            <template v-slot:header='scope'>
              <span>店毛利润
                <el-tooltip :aa="scope" class="item" effect="light" content="店毛利润=店销售额 - 店成本费" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="groProRat" label="店毛利率" align="center">
            <template v-slot:header='scope'>
              <span>店毛利率
                <el-tooltip :aa="scope" class="item" effect="light" content="店毛利率=店毛利润 / 店成本费 * 100%" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="talOrd" label="店订单数" align="center"></el-table-column>
          <el-table-column prop="sucOrd" label="店成交单数" align="center"></el-table-column>
          <el-table-column prop="faiOrd" label="店失败单数" align="center"></el-table-column>
          <el-table-column prop="salAmt" label="店销售额" align="center"></el-table-column>
          <el-table-column prop="cosAmt" label="店成本费" align="center"></el-table-column>
          <el-table-column prop="advAmt" label="店推广费" align="center"></el-table-column>
          <el-table-column prop="serAmt" label="店服务费" align="center"></el-table-column>
          <el-table-column prop="virAmt" label="店刷单费" align="center"></el-table-column>
          <el-table-column prop="talCos" label="店支出费" align="center">
            <template v-slot:header='scope'>
              <span>店支出费
                <el-tooltip :aa="scope" class="item" effect="light" content="店支出费=店成本费 + 店推广费 + 店服务费 + 店刷单费" placement="top">
                 <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template #default="scope">
              <el-tooltip class="item" effect="light" content="新增" placement="top">
                <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)"></el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="light" content="删除" placement="top">
                <el-button type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.row)"></el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination">
          <el-pagination background layout="total, prev, pager, next" :current-page.sync="query.pageNum"
                         :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange">
          </el-pagination>
        </div>
      </div>
      <!-- 新增/编辑店铺弹出框 -->
      <shop-edit ref="shopDialog" @findShopList="findShopList"></shop-edit>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, getCurrentInstance } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import request from '../../utils/request';
import shopEdit from "./ShopEdit.vue";

export default {
  name: "shopList",
  components: { shopEdit },
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 查询条件
    let query = reactive({
      shopId: "",
      shopNm: "",
      shopTp: "",
      pageNum: 1,
      pageSize: 10
    });
    //店铺弹出窗对象
    const shopDialog = ref();
    // 表格数据
    const tableData = ref([]);
    // 页数
    const pageTotal = ref(0);
    // 店铺平台
    const shopTpCode = ref([]);
    // 查询
    const handleSearch = () => {
      query.pageNum = 1;
      findShopList();
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
      query.shopNm = "";
      query.shopTp = "";
      findShopList();
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageNum = val;
      findShopList();
    };
    //编辑店铺
    const handleEdit = (row) => {
      shopDialog.value.openShopDialog(row,shopTpCode);//调用子组件方法
    };
    //新增店铺
    const handleAdd = () => {
      shopDialog.value.openShopDialog(null,shopTpCode);//调用子组件方法
    };
    // 获取表格数据
    const findShopList = () => {
      axios.$http.get(request.shopList,query).then(function (res) {
        tableData.value = res.data;
        pageTotal.value = res.totalNum;
      })
    };
    findShopList();
    //统计店铺销售额
    const handleCount = () => {
      axios.$http.post(request.shopCount, null).then(function (res) {
        ElMessage.success(res.data);
        findShopList();
      });
    };
    //删除店铺
    const handleDelete = (row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除店铺【" + row.shopNm + "】及其所有销售数据吗？", "提示", {type: "warning"}).then(() => {
        axios.$http.post(request.delShop+row.shopId, null).then(function (res) {
          ElMessage.success('删除成功');
          findShopList();
        });
      }).catch(() => {});
    };
    // 查询数据字典
    const getCodeDataArr = () => {
      axios.$http.get(request.code+"ShopPlatform", null).then(function (res) {
        shopTpCode.value = res.data.ShopPlatform;
      });
    };
    getCodeDataArr();
    return { query,tableData,pageTotal,shopTpCode,shopDialog,
      handleSearch,handleReset,handlePageChange,handleEdit,handleAdd,handleDelete,handleCount,findShopList,addCellClass};
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
