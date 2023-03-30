<template>
  <div>
    <!-- 新增/编辑店铺弹出框 -->
    <el-dialog :title="dialogTitle" v-model="editVisible" width="450px">
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
  </div>
</template>

<script>
import {getCurrentInstance,  reactive, ref} from "vue";
import request from '../../utils/request';
import {ElLoading, ElMessage} from "element-plus";

export default {
  name: "shopEdit",
  emits: ['findOrderList'], //父组件中为子组件标签定义的@方法
  setup(props,{emit}) { //调用父组件方法，必须有props,{emit}。否则调用失败
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 新增/编辑店铺弹窗控制
    const editVisible = ref(false);
    // 弹窗标题
    let dialogTitle = ref();
    // 编辑店铺信息
    let shopInfo = ref({});
    // 店铺信息的规则校验
    const shopRules = {
      shopNm : [
        { required: true, message: "店铺名称不能为空", trigger: "blur" }
      ],
      shopTp : [
        { required: true, message: "店铺平台不能为空", trigger: "blur" }
      ]
    };
    // 编辑店铺表单数据
    const shopForm = ref(null);
    // 店铺平台
    const shopTpCode = ref({});
    /**
     * 打开新增/编辑店铺弹窗
     * @param shop 店铺信息
     * @param shopTpArr 店铺平台信息
     */
    const openShopDialog = (shop,shopTpArr) => {
      editVisible.value = true;
      shopInfo.value = shop ? shop : {};
      shopTpCode.value = shopTpArr.value;
      dialogTitle.value = shop ? "编辑店铺信息" : "新增店铺信息";
    }
    // 新增/编辑店铺页面的保存按钮
    const saveEdit = () => {
      shopForm.value.validate((valid) => {
        if (valid) {
          let uploadLoading = ElLoading.service({text: "保存中...",fullscreen: true});
          axios.$http.post(shopInfo.value.shopId ? request.shopEdit : request.newShop,shopInfo.value).then(function (res) {
            editVisible.value = false;
            uploadLoading.close();
            ElMessage.success(res.data);
            emit('findShopList',null);//调用父组件OrderList.vue的findOrderList方法
          }).catch(err => {
            uploadLoading.close();
          });
        } else {
          return false;
        }
      });
    };
    return {editVisible,shopInfo,shopRules,shopForm,shopTpCode,dialogTitle,
      openShopDialog,saveEdit};
  }
}
</script>
<style>
.btn-save{
  text-align: center;
}
.el-dialog .el-dialog__header{
  text-align: center;
}
</style>