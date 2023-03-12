<!-- 店铺信息列表 -->
<template>
  <div>
    <div class="crumb-title">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-s-custom"></i> 店铺信息
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-card>
      <div>
        <!-- 用户信息列表查询条件-->
        <div class="handle-box">
          <el-select v-model="query.stateCd" placeholder="用户状态" class="handle-select mr10">
            <el-option v-for="item in stateCdArr" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
          <el-input v-model="query.userId" placeholder="用户ID" class="handle-input mr10"></el-input>
          <el-input v-model="query.userName" placeholder="用户名称" class="handle-input mr10"></el-input>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button type="primary" icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </div>
        <!-- 用户信息列表-->
        <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
          <el-table-column prop="suId" label="ID" width="55" align="center"></el-table-column>
          <el-table-column prop="userId" label="用户ID" align="center"></el-table-column>
          <el-table-column prop="userName" label="用户名称" align="center"></el-table-column>
          <el-table-column label="头像(查看大图)" align="center">
            <template #default="scope">
              <el-image class="table-td-thumb" :src=" scope.row.photo ? scope.row.photo : 'src/assets/img/img.jpg'" :preview-src-list="[scope.row.photo]">
              </el-image>
            </template>
          </el-table-column>
          <el-table-column prop="roleName" label="角色" align="center"></el-table-column>
          <el-table-column prop="phoneNum" label="手机号码" align="center"></el-table-column>
          <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
          <el-table-column prop="birthday" label="出生日期" align="center"></el-table-column>
          <el-table-column prop="sex" label="性别" align="center">
            <template #default="scope">
              {{ scope.row.sex === '1' ? '男' : scope.row.sex === '0' ? '女' : '未知' }}
            </template>
          </el-table-column>
          <el-table-column label="用户状态" align="center">
            <template #default="scope">
              <el-select v-model="scope.row.stateCd" disabled size="mini">
                <el-option v-for="item in stateCdArr" :key="item.value" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="是否未过期" align="center">
            <template #default="scope">
              <el-select v-model="scope.row.notExpired" disabled size="mini">
                <el-option v-for="item in yesNoArr" :key="item.value" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="账号是否未锁定" align="center">
            <template #default="scope">
              <el-select v-model="scope.row.notLocked" disabled size="mini">
                <el-option v-for="item in yesNoArr" :key="item.value" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="密码是否未过期" align="center">
            <template #default="scope">
              <el-select v-model="scope.row.credentialsNotExpired" disabled size="mini">
                <el-option v-for="item in yesNoArr" :key="item.value" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="注册时间" align="center"></el-table-column>
          <el-table-column prop="updateTime" label="更新时间" align="center"></el-table-column>
          <el-table-column label="操作" width="180" align="center">
            <template #default="scope">
              <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑 </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination">
          <el-pagination background layout="total, prev, pager, next" :current-page="query.pageNum"
                         :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange">
          </el-pagination>
        </div>
      </div>
      <!-- 编辑弹出框 -->
      <el-dialog title="编辑" v-model="editVisible" width="450px">
        <el-form label-width="120px">
          <el-form-item label="用户ID">
            <el-input v-model="form.userId" :disabled="true" style="width: 250px"></el-input>
          </el-form-item>
          <el-form-item label="用户名称">
            <el-input v-model="form.userName" :disabled="true" style="width: 250px"></el-input>
          </el-form-item>
          <el-form-item label="用户状态">
            <el-select v-model="form.stateCd" placeholder="用户状态" class="handle-select mr10" style="width: 250px">
              <el-option key="1" label="有效" value="1"></el-option>
              <el-option key="2" label="注销" value="2"></el-option>
              <el-option key="3" label="封号" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否过期">
            <el-radio v-model="form.expired" label="1">否</el-radio>
            <el-radio v-model="form.expired" label="0">是</el-radio>
          </el-form-item>
          <el-form-item label="账号是否锁定">
            <el-radio v-model="form.locked" label="1">否</el-radio>
            <el-radio v-model="form.locked" label="0">是</el-radio>
          </el-form-item>
          <el-form-item label="密码是否过期">
            <el-radio v-model="form.credentials" label="1">否</el-radio>
            <el-radio v-model="form.credentials" label="0">是</el-radio>
          </el-form-item>
        </el-form>
        <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, getCurrentInstance } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import request from '../../utils/request';

export default {
  name: "shopList",
  setup() {
    // 查询条件
    const query = reactive({
      stateCd: "",
      userId: "",
      userName: "",
      pageNum: 1,
      pageSize: 10
    });
    // 用户状态
    const stateCdArr = ref([]);
    // 是否字典
    const yesNoArr = ref([]);
    // 表格数据
    const tableData = ref([]);
    // 页数
    const pageTotal = ref(0);
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 查询数据字典
    const getCodeDataArr = () => {
      axios.$http.get(request.code + "userStatus", null).then(function (res) {
        if(res.code === 200){
          stateCdArr.value = res.data.userStatus;
        }
      });
      axios.$http.get(request.code + "yesNo", null).then(function (res) {
        if(res.code === 200){
          yesNoArr.value = res.data.yesNo;
        }
      });
    };
    // getCodeDataArr();
    // 获取表格数据
    const getData = () => {
      axios.$http.get(request.userList,query).then(function (res) {
        if(res.code === 200){
          tableData.value = res.data;
          pageTotal.value = res.totalNum;
        }
      })
    };
    // getData();
    // 查询操作
    const handleSearch = () => {
      query.pageNum = 1;
      getData();
    };
    // 重置
    const handleReset = () => {
      query.stateCd = "";
      query.userId = "";
      query.userName = "";
      getData();
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageNum = val;
      getData();
    };
    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    // 表单数据
    let form = reactive({
      userId: "",
      userName : "",
      password : "",
      phoneNum : "",
      birthday : "",
      sex : "",
      photo : "",
      email : "",
      stateCd: "",
      expired : "",
      locked : "",
      credentials : ""
    });
    //编辑
    const handleEdit = (row) => {
      form.userId = row.userId;
      form.userName = row.userName;
      form.stateCd = row.stateCd;
      form.expired = row.notExpired;
      form.locked = row.notLocked;
      form.credentials = row.credentialsNotExpired;
      editVisible.value = true;
    };
    // 编辑页面的保存按钮
    const saveEdit = () => {
      axios.$http.post(request.userState,form).then(function (res) {
        if(res.code === 200){
          editVisible.value = false;
          ElMessage.success('修改成功');
          getData();
        }else {
          ElMessage.error('修改失败');
        }
      })
    };
    return { stateCdArr,yesNoArr,query,tableData,pageTotal,editVisible,form,
      handleSearch,handleReset,handlePageChange,handleEdit,saveEdit};
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
.table-td-thumb {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}
</style>
