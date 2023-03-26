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
                       :on-exceed="handleExceed" :on-remove="handleRemove" :on-change="handleChange" :auto-upload="false">
              <i class="el-icon-upload"></i>
              <div slot="tip" class="el-upload__tip">仅支持单个文件导入，且只能导入xls、xlsx、csv文件，大小不超过1MB</div>
            </el-upload>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <div class="btn-save">
              <span>
                <el-button type="primary" @click="saveEdit" class="el-icon-upload2" round plain>导入</el-button>
                <el-button @click="editVisible = false" class="el-icon-close" round plain>取消</el-button>
                <el-button type="text" @click="showExplain">导入说明</el-button>
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
import utils from "../../utils/utils";
import {ElMessage, ElMessageBox,ElLoading } from "element-plus";

export default {
  name: "importOrder",
  emits: ['findOrderList'], //父组件中为子组件标签定义的@方法
  setup(props,{emit}) { //调用父组件方法，必须有props,{emit}。否则调用失败
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    //环境配置
    const ENV = import.meta.env;
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
    //关闭订单导入弹窗
    const closeInportDialog = () => {
      editVisible.value = false;
      orderInfo.shopId = null;
      userShop.value = null;
      clearFile();
      emit('findOrderList',null);//调用父组件OrderList.vue的findOrderList方法
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
            let uploadLoading = ElLoading.service({text: "导入中...",fullscreen: true});
            let fd = new FormData();//通过form数据格式来传
            fd.append("shopId",orderInfo.value.shopId);
            fd.append("pwd",orderInfo.value.pwd);
            fd.append("file",importFile.raw);
            axios.$http.upload(request.importOrder,fd).then(function(res){
              uploadLoading.close();
              ElMessageBox.confirm(res.data.msg, "导入提示",
                  {confirmButtonText: res.data.url ? '下载错误订单信息' : '确定',cancelButtonText: '关闭',type: 'warning',center: true,roundButton: true}
              ).then(() => {
                if (res.data.url){
                  //下载文件
                  utils.downloadFile(ENV.VITE_API_URL + res.data.url);
                  closeInportDialog();
                }else {
                  closeInportDialog();
                }
              }).catch(() => {
                closeInportDialog();
              });
            }).catch(err => {
              uploadLoading.close();
            });
          }else {
            ElMessage.error(" 请选择要导入的文件");
          }
        } else {
          return false;
        }
      });
    };
    //显示导入说明
    const showExplain = () => {
      let explainText =
          "<span style='text-align: left;'>1、订单文件为平台导出的订单报表文件，导出后最好不要编辑，否则容易导入失败。店铺平台需与实际订单平台一致，否则报错。</span><br/>" +
          "<span style='text-align: left;'>2、根据订单号和店铺导入数据，已存在同订单号更新，反之则新增。</span><br/>" +
          "<span style='text-align: left;'>3、已存在的订单的【其他支出】列不会覆盖更新。</span><br/>" +
          "<span style='text-align: left;'>4、订单文件的备注字段，满足以下格式则能保存到对应列：</span><br/>" +
          "<span style='text-align: left;'>4.1、备注满足格式：<span style='color:red;'>值1#值2#值3</span>&nbsp;&nbsp;&nbsp;<span style='color:red;'>#</span>为分隔符。" +
          "<span style='color:red;'>值1</span>可选值为：1688订单ID和刷单，如果是刷单，则这笔订单导入后订单状态为【刷单】，反之则是为【1688订单】列取数来源；</span>" +
          "<span style='text-align: left;'><span style='color:red;'>值2</span>为【商品成本】取数来源；" +
          "<span style='color:red;'>值3</span>为【备注】列取数来源。</span><br/>" +
          "<span style='text-align: left;'>4.2、备注不满足以上格式，则将保存到【备注】列。</span>";
      ElMessageBox.confirm(explainText, "导入说明",
          {dangerouslyUseHTMLString: true,roundButton: true}).then(() => {}).catch(() => {});
    }
    return {editVisible,orderInfo,orderRules,orderForm,userShop,upload,
      openInportDialog,saveEdit,handleRemove,handleChange,handleExceed,showExplain };
  }
}
</script>
<style >
body {
  margin: 0;
}
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
.el-message-box .el-message-box__btns {
  text-align: center;
}
.el-message-box .el-message-box__title {
  text-align: center;
}
.el-dialog .el-dialog__header{
  text-align: center;
}
.el-message-box {
  width: 700px;
}
</style>