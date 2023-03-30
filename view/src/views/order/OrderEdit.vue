<template>
  <div>
    <!-- 新增/编辑订单弹出框 -->
    <el-dialog :title="dialogTitle" v-model="editVisible" width="850px">
      <el-form label-width="120px" :model="orderInfo" :rules="orderRules" ref="orderForm">
        <el-row>
          <el-col :span="12">
            <el-form-item label="订单ID" prop="ordId">
              <el-input v-model="orderInfo.ordId" style="width: 250px" maxlength="40" placeholder="不填则自动生成"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="店铺名称" prop="shopId">
              <el-select v-model="orderInfo.shopId" placeholder="请选择店铺" class="handle-select mr10" style="width: 250px">
                <el-option v-for="item in userShop" :key="item.value" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="订单日期" prop="ordDat">
              <div class="block" style="float: left; margin-right: 10px;">
                <el-date-picker style="width:250px;" v-model="orderInfo.ordDat" type="date"
                                format="YYYY年M月DD日" value-format="YYYY-MM-DD" placeholder="选择日期"></el-date-picker>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="1688订单ID" prop="supId">
              <el-input v-model="orderInfo.supId" style="width: 250px" maxlength="40" placeholder="请输入1688订单ID"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="商品ID" prop="gdsId">
              <el-input v-model="orderInfo.gdsId" style="width: 250px" maxlength="40" placeholder="请输入商品ID"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品名称" prop="gdsName">
              <el-input v-model="orderInfo.gdsName" style="width: 250px" maxlength="60" placeholder="请输入1688订单ID"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="订单状态" prop="ordSta">
              <el-select v-model="orderInfo.ordSta" placeholder="请选择订单状态" class="handle-select mr10" style="width: 250px">
                <el-option v-for="item in orderSate" :key="item.value" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品总价" prop="salAmt">
              <el-input-number v-model="orderInfo.salAmt" :precision="2" min="0" max="99999999.99"
                        style="width: 250px" maxlength="20" placeholder="请输入商品总价"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="实付金额" prop="payAmt">
              <el-input-number v-model="orderInfo.payAmt" :precision="2" min="0" max="99999999.99"
                        style="width: 250px" maxlength="20" placeholder="请输入实付金额"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品成本" prop="cosAmt">
              <el-input-number v-model="orderInfo.cosAmt" :precision="2" min="0" max="99999999.99"
                        style="width: 250px" maxlength="20" placeholder="请输入商品成本"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="其他支出" prop="othAmt">
              <el-input-number v-model="orderInfo.othAmt" :precision="2" min="0" max="99999999.99"
                        style="width: 250px" maxlength="20" placeholder="请输入其他支出"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总成本费" prop="talCos">
              <el-input v-model="orderInfo.talCos" :disabled="true" style="width: 250px" maxlength="20"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="毛利润">
              <el-input v-model="orderInfo.groPro" :disabled="true" style="width: 250px" maxlength="20"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="毛利率">
              <el-input v-model="orderInfo.groProRat" :disabled="true" style="width: 250px" maxlength="20"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="orderInfo.remark" style="width: 655px" maxlength="100" placeholder="请输入备注"></el-input>
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
import {ElLoading, ElMessage} from "element-plus";

export default {
  name: "orderEdit",
  emits: ['findOrderList'], //父组件中为子组件标签定义的@方法
  setup(props,{emit}) { //调用父组件方法，必须有props,{emit}。否则调用失败
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 新增/编辑订单弹窗控制
    const editVisible = ref(false);
    // 弹窗标题
    let dialogTitle = ref();
    // 编辑订单信息
    let orderInfo = ref({});
    // 订单信息的规则校验
    const orderRules = {
      ordId : [
        { type: 'string', message: '订单ID只能输入字母、数字、- ', trigger: 'blur',
          transform (value) {
            if (value){
              if (/[^a-zA-Z0-9-]/g.test(value)) {
                return true
              }
            }
          }
        }
      ],
      shopId : [
        { required: true, message: "店铺名称不能为空", trigger: "blur" }
      ],
      ordDat : [
        { required: true, message: "订单日期不能为空", trigger: "blur" }
      ],
      supId : [
        { type: 'string', message: '1688订单ID只能输入字母、数字、- ', trigger: 'blur',
          transform (value) {
            if (value){
              if (/[^a-zA-Z0-9-]/g.test(value)) {
                return true
              }
            }
          }
        }
      ],
      gdsId : [
        { type: 'string', message: '商品ID只能输入字母、数字', trigger: 'blur',
          transform (value) {
            if (value){
              if (/[^a-zA-Z0-9]/g.test(value)) {
                return true
              }
            }
          }
        }
      ],
      ordSta : [
        { required: true, message: "订单状态不能为空", trigger: "blur" }
      ]
    };
    // 编辑订单表单数据
    const orderForm = ref(null);
    // 用户的所有店铺信息
    const userShop = ref([]);
    // 订单状态
    const orderSate = ref([]);
    /**
     * 打开新增/编辑订单弹窗
     * @param order 订单信息
     * @param shopArr 用户的所有店铺信息
     * @param stateArr 订单状态
     */
    const openOrderDialog = (order,shopArr,stateArr) => {
      editVisible.value = true;
      userShop.value = shopArr.value;
      orderSate.value = stateArr.value;
      orderInfo.value = order ? order : {};
      dialogTitle.value = order ? "编辑订单信息" : "新增订单信息";
    }
    // 新增/编辑店铺页面的保存按钮
    const saveEdit = () => {
      orderForm.value.validate((valid) => {
        if (valid) {
          let uploadLoading = ElLoading.service({text: "保存中...",fullscreen: true});
          axios.$http.post(request.editOrder,orderInfo.value).then(function (res) {
            uploadLoading.close();
            editVisible.value = false;
            ElMessage.success(res.data);
            emit('findOrderList',null);//调用父组件OrderList.vue的findOrderList方法
          }).catch(err => {
            uploadLoading.close();
          });
        } else {
          return false;
        }
      });
    };
    return {editVisible,orderInfo,orderRules,orderForm,userShop,orderSate,dialogTitle,
      openOrderDialog,saveEdit};
  }
}
</script>
<style scoped>
.btn-save{
  text-align: center;
}
.el-dialog .el-dialog__header{
  text-align: center;
}
</style>