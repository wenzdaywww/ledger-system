<template>
  <div>
    <!-- 增加/减少月销售费用弹出框 -->
    <el-dialog :title="monthInfo.isAdd == true ? '增加费用' : '减少费用'" v-model="editVisible" width="850px">
      <el-form label-width="120px" :model="monthInfo">
        <el-row>
          <el-col :span="12">
            <el-form-item label="月份">
              <el-input v-model="monthInfo.month" style="width: 250px" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="店铺名称">
              <el-input v-model="monthInfo.shopNm" style="width: 250px" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="现月推广费">
              <el-input-number v-model="monthInfo.advAmt" style="width: 250px" :disabled="true"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="monthInfo.isAdd == true ? '增加推广费' : '减少推广费'">
              <el-input-number v-model="monthInfo.advStep" :precision="2" min="0" max="99999999.99"
                               style="width: 250px" maxlength="20" :placeholder="monthInfo.isAdd == true ? '请输入要增加的服务费' : '请输入要减少的服务费'"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="现月服务费">
              <el-input-number v-model="monthInfo.serAmt" style="width: 250px" :disabled="true"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="monthInfo.isAdd == true ? '增加推广费' : '减少推广费'">
              <el-input-number v-model="monthInfo.serStep" :precision="2" min="0" max="99999999.99"
                               style="width: 250px" maxlength="20" :placeholder="monthInfo.isAdd == true ? '请输入要增加的服务费' : '请输入要减少的服务费'"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="btn-save">
            <span class="dialog-footer">
              <el-button type="primary" @click="saveEdit" class="el-icon-check" round plain>保存</el-button>
              <el-button @click="editVisible = false" class="el-icon-close" round plain>取消</el-button>
            </span>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {getCurrentInstance,  reactive, ref} from "vue";
import request from '../../utils/request';
import {ElMessage} from "element-plus";

export default {
  name: "monthAmtEdit",
  emits: ['findMonthList'], //父组件中为子组件标签定义的@方法
  setup(props,{emit}) { //调用父组件方法，必须有props,{emit}。否则调用失败
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 新增/编辑店铺弹窗控制
    const editVisible = ref(false);
    // 编辑月销售数据
    let monthInfo = ref({});
    /**
     * 打开新增/编辑订单弹窗
     * @param month 月销售数据
     * @param isAdd ture增加金额，fasle减少金额
     */
    const openMonthAmtDialog = (month,isAdd) => {
      editVisible.value = true;
      monthInfo.value = month;
      monthInfo.value.isAdd = isAdd;
      monthInfo.value.type = isAdd ? 1 : 0;
    }
    // 新增/编辑店铺页面的保存按钮
    const saveEdit = () => {
      axios.$http.post(request.amtStep,monthInfo.value).then(function (res) {
        if(res.code === 200){
          editVisible.value = false;
          ElMessage.success('修改成功');
          emit('findMonthList',null);//调用父组件OrderList.vue的findOrderList方法
        }else {
          ElMessage.error(res.data);
        }
      });
    };
    return {editVisible,monthInfo,
      openMonthAmtDialog,saveEdit};
  }
}
</script>

<style scoped>
.btn-save{
  text-align: center;
}
</style>