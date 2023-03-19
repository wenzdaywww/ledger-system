<template>
  <div>
    <!-- 新增/编辑月销售额弹出框 -->
    <el-dialog :title="monthInfo.title" v-model="editVisible" width="850px">
      <el-form label-width="120px" :model="monthInfo" :rules="monthRules" ref="monthForm">
        <el-row>
          <el-col :span="12">
            <el-form-item label="月份" prop="month">
              <div class="block" style="float: left; margin-right: 10px;">
                <el-date-picker style="width:250px;" v-model="monthInfo.month" type="month"
                                format="YYYY-MM" value-format="YYYY-MM" placeholder="月份"></el-date-picker>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="店铺名称" prop="shopId">
              <el-select v-model="monthInfo.shopId" placeholder="请选择店铺" class="handle-select mr10" style="width: 250px">
                <el-option v-for="item in userShop" :key="item.value" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="月净利润">
              <el-input v-model="monthInfo.retPro" style="width: 250px" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="月净利率">
              <el-input v-model="monthInfo.retProRat" style="width: 250px" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="月毛利润">
              <el-input v-model="monthInfo.groPro" style="width: 250px" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="月毛利率">
              <el-input v-model="monthInfo.groProRat" style="width: 250px" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="月订单数">
              <el-input v-model="monthInfo.talOrd" style="width: 250px" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="月成交单数">
              <el-input v-model="monthInfo.sucOrd" style="width: 250px" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="月失败单数">
              <el-input v-model="monthInfo.faiOrd" style="width: 250px" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="月销售额">
              <el-input v-model="monthInfo.salAmt" style="width: 250px" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="月成本费">
              <el-input v-model="monthInfo.cosAmt" style="width: 250px" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="月推广费" prop="advAmt">
              <el-input-number v-model="monthInfo.advAmt" :precision="2" min="0" max="99999999.99"
                               style="width: 250px" maxlength="20" placeholder="请输入月推广费"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="月服务费" prop="serAmt">
              <el-input-number v-model="monthInfo.serAmt" :precision="2" min="0" max="99999999.99"
                               style="width: 250px" maxlength="20" placeholder="请输入月服务费"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="月刷单费">
              <el-input v-model="monthInfo.groProRat" :disabled="true" style="width: 250px"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="btn-save">
            <span class="dialog-footer">
              <el-button type="primary" @click="saveEdit" class="el-icon-check" round plain>确定</el-button>
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
  name: "monthEdit",
  emits: ['findMonthList'], //父组件中为子组件标签定义的@方法
  setup(props,{emit}) { //调用父组件方法，必须有props,{emit}。否则调用失败
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 新增/编辑店铺弹窗控制
    const editVisible = ref(false);
    // 编辑月销售数据
    let monthInfo = ref({});
    // 月销售数据的规则校验
    const monthRules = {
      month : [
        { required: true, message: "月份不能为空", trigger: "blur" }
      ],
      shopId : [
        { required: true, message: "店铺名称不能为空", trigger: "blur" }
      ]
    };
    // 编辑店铺表单数据
    const monthForm = ref(null);
    // 用户的所有店铺信息
    const userShop = ref([]);
    /**
     * 打开新增/编辑订单弹窗
     * @param titleDialog 弹窗名称
     * @param month 月销售数据
     * @param shopArr 用户的所有店铺信息
     */
    const openMonthDialog = (titleDialog,month,shopArr) => {
      editVisible.value = true;
      userShop.value = shopArr.value;
      monthInfo.value = month ? month : {};
      monthInfo.value.title = titleDialog;
    }
    // 新增/编辑店铺页面的保存按钮
    const saveEdit = () => {
      monthForm.value.validate((valid) => {
        if (valid) {
          axios.$http.post(request.editMonth,monthInfo.value).then(function (res) {
            if(res.code === 200){
              editVisible.value = false;
              ElMessage.success(monthInfo.value.msId ? '修改成功' : '新增成功');
              emit('findMonthList',null);//调用父组件OrderList.vue的findOrderList方法
            }else {
              ElMessage.error(res.data);
            }
          });
        } else {
          return false;
        }
      });
    };
    return {editVisible,monthInfo,monthRules,monthForm,userShop,
      openMonthDialog,saveEdit};
  }
}
</script>

<style scoped>
.btn-save{
  text-align: center;
}
</style>