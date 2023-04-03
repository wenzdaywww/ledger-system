<template>
  <div>
    <el-dialog title="报表导出" v-model="editVisible" width="1000px">
      <div>
        <div>
          <el-row style="margin-bottom: 20px;">
            <el-col>
              <div style="margin-bottom: 10px;">
                请选择要导出的数据：
              </div>
              <el-checkbox-group v-model="select.sheets" size="mini" min="1">
                <el-checkbox-button v-for="sheet in sheetArr" checked="true" :label="sheet.key">{{sheet.label}}</el-checkbox-button>
              </el-checkbox-group>
            </el-col>
          </el-row>
          <el-row style="margin-bottom: 20px;">
            <el-col style="text-align: center;">
              <el-button type="success" class="el-icon-document" @click="createExport" round plain> 生成报表</el-button>
              <el-button type="primary" class="el-icon-search" @click="findDocList" round plain> 查询报表</el-button>
            </el-col>
          </el-row>
        </div>
        <el-table :data="tableData" border class="table" ref="multipleTable"
                  :row-style="{height:'55px'}" :cell-style="{padding:'0px'}" header-cell-class-name="table-header">
          <el-table-column prop="name" label="报表名称" align="center" sortable  width="180px;"></el-table-column>
          <el-table-column prop="sheet" label="导出的数据" align="center" sortable>
            <template #default="scope">
              <el-tooltip class="item" effect="light" :content="scope.row.sheet" placement="top">
                <span class="ellipsis-line1">{{scope.row.sheet}}</span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column prop="date" label="生成时间" align="center" sortable  width="180px;"></el-table-column>
          <el-table-column prop="state" label="报告状态" align="center" sortable width="120px;"></el-table-column>
          <el-table-column label="操作" align="center" width="120px;">
            <template #default="scope">
              <el-button type="text" v-if="scope.row.url" icon="el-icon-download" @click="handleDown(scope.row)">下载</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination">
          <el-pagination background layout="total, prev, pager, next" :current-page.sync="query.pageNum"
                         :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange">
          </el-pagination>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, getCurrentInstance } from "vue";
import {ElLoading, ElMessage, ElMessageBox} from "element-plus";
import utils from "../../utils/utils";
import request from "../../utils/request";

export default {
  name: "exportList",
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    //环境配置
    const ENV = import.meta.env;
    // 查询条件
    let query = reactive({
      pageNum: 1,
      pageSize: 10
    });
    // 要导出的数据选择后保存对象
    let select = reactive({
      sheets:[]
    });
    // 报表导出弹出框控制标志
    const editVisible = ref(false);
    // 表格数据
    const tableData = ref([]);
    // 页数
    const pageTotal = ref(0);
    // 要导出的数据
    const sheetArr = [
      {key:0,label:"我的账簿"},{key:1,label:"我的店铺"},{key:2,label:"店铺年销售额"},
      {key:3,label:"汇总年销售额"},{key:4,label:"店铺月销售额"},{key:5,label:"汇总月销售额"},
      {key:6,label:"店铺日销售额"},{key:7,label:"汇总日销售额"},{key:8,label:"订单信息"},
      {key:9,label:"支出管理"},
    ];
    //打开报表导出弹窗
    const openExportDialog = () => {
      editVisible.value = true;
    }
    // 获取表格数据
    const findDocList = () => {
      let uploadLoading = ElLoading.service({text: "加载中...",fullscreen: true});
      axios.$http.get(request.docList,query).then(function (res) {
        uploadLoading.close();
        tableData.value = res.data;
        pageTotal.value = res.totalNum;
      }).catch(err => {uploadLoading.close();});
    };
    findDocList();
    // 生成报表
    const createExport = () => {
      let uploadLoading = ElLoading.service({text: "生成中...",fullscreen: true});
      axios.$http.post(request.createReport,select).then(function (res) {
        uploadLoading.close();
        ElMessage.success(res.data);
        findDocList();
      }).catch(err => {
        uploadLoading.close();
      });
    };
    // 下载报表
    const handleDown = (row) => {
      utils.downloadFile(ENV.VITE_API_URL + row.url);
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageNum = val;
    };
    return { query,tableData,pageTotal,editVisible,sheetArr,select,
      openExportDialog,handlePageChange,createExport,handleDown,findDocList};
  }
};
</script>

<style>
.ellipsis-line1{
  display: -webkit-box;/*作为弹性伸缩盒子模型显示*/
  -webkit-line-clamp: 1; /*显示的行数；如果要设置2行加...则设置为2*/
  overflow: hidden; /*超出的文本隐藏*/
  text-overflow: ellipsis; /* 溢出用省略号*/
  -webkit-box-orient: vertical;/*伸缩盒子的子元素排列：从上到下*/
}
.el-dialog .el-dialog__header{
  text-align: center;
}
</style>
