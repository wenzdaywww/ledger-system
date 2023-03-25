<template>
  <div>
    <!-- 订单导入 -->
    <el-dialog title="订单导入" v-model="editVisible" width="600px" height="800px" lock-scroll="true" top="70px">
      <el-form :model="orderInfo" :rules="orderRules" ref="orderForm">
        <el-row>
          <el-col>
            <el-form-item label="店铺名称" label-width="80px" prop="shopId">
              <el-select v-model="orderInfo.shopId" placeholder="请选择店铺" style="width: 480px">
                <el-option v-for="item in userShop" :key="item.value" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="文件密码" label-width="80px">
              <el-input v-model="orderInfo.pwd" style="width: 480px" maxlength="40" placeholder="文件有密码时必输"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-upload class="upload-demo" drag limit="1" ref="upload" accept=".xls,.xlsx,.csv"
                       :on-exceed="handleExceed"
                       :on-remove="handleRemove" :on-change="handleChange" :auto-upload="false">
              <i class="el-icon-upload"></i>
              <div slot="tip" class="el-upload__tip">仅支持单个文件但如，且只能导入xls、xlsx、csv文件，大小不超过1MB</div>
            </el-upload>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <div class="btn-save">
              <span>
                <el-button type="primary" @click="saveEdit" class="el-icon-check" round plain>导入</el-button>
                <el-button @click="editVisible = false" class="el-icon-close" round plain>取消</el-button>
              </span>
            </div>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {getCurrentInstance,  reactive, ref} from "vue";
import request from '../../utils/request';
import {ElMessage} from "element-plus";

export default {
  name: "importOrder",
  emits: ['findOrderList'], //父组件中为子组件标签定义的@方法
  setup(props,{emit}) { //调用父组件方法，必须有props,{emit}。否则调用失败
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 订单导入弹窗控制
    const editVisible = ref(false);
    // 订单导入信息
    let orderInfo = ref({
      shopId: "",
      pwd: ""
    });
    // 待导入的文件
    let importFile = ref({});
    // 订单信息的规则校验
    const orderRules = { shopId : [ { required: true, message: "店铺名称不能为空", trigger: "blur" } ] };
    // 编辑订单表单数据
    const orderForm = ref(null);
    // 文件上传对象
    const upload = ref(null);
    // 用户的所有店铺信息
    const userShop = ref([]);
    /**
     * 打开订单导入弹窗
     * @param order 订单信息
     * @param shopArr 用户的所有店铺信息
     * @param stateArr 订单状态
     */
    const openInportDialog = (shopArr) => {
      editVisible.value = true;
      orderInfo.shopId = null;
      userShop.value = shopArr.value;
      clearFile();
    }
    // 文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
    const handleChange = (file, fileList) => {
      importFile = file;
    }
    //文件列表移除文件时的钩子
    const handleRemove = (file, fileList) => {
      importFile = null;
    }
    //文件超出个数限制时的钩子
    const handleExceed = (file, fileList) => {
      if (importFile && importFile.raw){
        ElMessage.warning("请先删除已添加的文件！");
      }
    }
    // 清除文件
    const clearFile = () => {
      importFile = null;
      upload.value.clearFiles();
    }
    // 新增/编辑店铺页面的保存按钮
    const saveEdit = () => {
      orderForm.value.validate((valid) => {
        if (valid) {
          if(importFile && importFile.raw){
            if (importFile.raw.size > 1024*1024){
              ElMessage.warning("文件大小超过1MB！");
              return;
            }
            let fd = new FormData();//通过form数据格式来传
            fd.append("shopId",orderInfo.value.shopId);
            fd.append("pwd",orderInfo.value.pwd);
            fd.append("file",importFile.raw);
            axios.$http.upload(request.importOrder,fd).then(function(res){
              clearFile();
              ElMessage.success(res.data);
            }).catch(function (err)  {
              //TODO 2023/3/25 01:18 导入失败，则下载失败的数据文件，待开发
            });
          }else {
            ElMessage.error(" 请选择要导入的文件");
          }
        } else {
          return false;
        }
      });
    };
    return {editVisible,orderInfo,orderRules,orderForm,userShop,upload,
      openInportDialog,saveEdit,handleRemove,handleChange,handleExceed };
  }
}
</script>
<style >
.btn-save{
  text-align: center;
  margin-bottom: 10px;
}
.table {
  width: 100%;
  font-size: 14px;
}
.upload-demo {
  text-align: center;
  margin-bottom: 10px;
}
.upload-demo div {
  height: 80px;
  width: 100%;
}
.upload-demo .el-icon-upload {
  margin: 0;
}
</style>