<template>
  <div>
    <!-- 新增/编辑支出信息弹出框 -->
    <el-dialog title="支出喜喜" v-model="editVisible" width="450px">
      <el-form label-width="120px" :model="payInfo" :rules="payRules" ref="payForm">
        <el-form-item label="支出日期" prop="payDat">
          <div class="block" style="float: left; margin-right: 10px;">
            <el-date-picker style="width:250px;" v-model="payInfo.payDat" type="date"
                            format="YYYY年M月DD日" value-format="YYYY-MM-DD" placeholder="选择日期"></el-date-picker>
          </div>
        </el-form-item>
        <el-form-item label="店铺名称" prop="shopId">
          <el-select v-model="payInfo.shopId" placeholder="请选择店铺名称" class="handle-select mr10" style="width: 250px">
            <el-option v-for="item in userShop" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="支出类型" prop="payTp">
          <el-select v-model="payInfo.payTp" placeholder="请选择支出类型" class="handle-select mr10" style="width: 250px">
            <el-option v-for="item in payType" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="支出金额" prop="payAmt">
          <el-input-number v-model="payInfo.payAmt" :precision="2" min="0.01" max="88888888.99"
                           style="width: 250px" maxlength="20" placeholder="请输入支出金额"></el-input-number>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="payInfo.remark" style="width: 250px" maxlength="100" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="btn-save">
          <span class="dialog-footer">
            <el-button type="primary" @click="saveEdit" class="el-icon-check" round>确 定</el-button>
            <el-button @click="editVisible = false" class="el-icon-close" round>取消</el-button>
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
  name: "payEdit",
  emits: ['findPayList'], //父组件中为子组件标签定义的@方法
  setup(props,{emit}) { //调用父组件方法，必须有props,{emit}。否则调用失败
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 新增/编辑支出信息弹出框
    const editVisible = ref(false);
    // 编辑支出信息
    let payInfo = ref({});
    // 支出信息的规则校验
    const payRules = {
      payDat : [ { required: true, message: "支出日期不能为空", trigger: "blur" } ],
      shopId : [ { required: true, message: "店铺名称不能为空", trigger: "blur" } ],
      payTp : [ { required: true, message: "支出类型不能为空", trigger: "blur" } ],
      payAmt : [ { required: true, message: "支出金额不能为空", trigger: "blur" } ]
    };
    // 编辑支出表单数据
    const payForm = ref(null);
    // 用户店铺
    const userShop = ref([]);
    // 支出类型
    const payType = ref([]);
    /**
     * 打开新增/编辑支出弹窗
     * @param shop 支出信息
     * @param shopTpArr 支出平台信息
     */
    const openPayDialog = (pay,userShopArr,payTpArr) => {
      editVisible.value = true;
      payInfo.value = pay ? pay : {};
      userShop.value = userShopArr.value;
      payType.value = payTpArr.value;
    }
    // 新增/编辑支出页面的保存按钮
    const saveEdit = () => {
      payForm.value.validate((valid) => {
        if (valid) {
          let uploadLoading = ElLoading.service({text: "保存中...",fullscreen: true});
          axios.$http.post(request.payEdit,payInfo.value).then(function (res) {
            editVisible.value = false;
            uploadLoading.close();
            ElMessage.success(res.data);
            emit('findPayList',null);//调用父组件PayList.vue的findPayList方法
          }).catch(err => {
            uploadLoading.close();
          });
        } else {
          return false;
        }
      });
    };
    return {editVisible,payInfo,payRules,payForm,userShop,payType,
      openPayDialog,saveEdit};
  }
}
</script>
<style scoped>
.btn-save{
  text-align: center;
}
</style>