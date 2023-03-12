<!-- 密码修改 -->
<template>
  <el-dialog title="修改密码" v-model="editVisible" width="20%" destroy-on-close="true">
    <el-form label-width="120px" :model="form" :rules="editRules" ref="editForm">
      <el-form-item label="旧密码：" prop="password">
        <el-input type="password" v-model="form.password" maxlength="20" placeholder="请输入旧密码"></el-input>
      </el-form-item>
      <el-form-item label="新密码：" prop="newPassWord">
        <el-input type="password" v-model="form.newPassWord" maxlength="20" placeholder="请输入新密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码：" prop="cfmPassWord">
        <el-input type="password" v-model="form.cfmPassWord" maxlength="20" placeholder="请重新输入新密码"></el-input>
      </el-form-item>
    </el-form>
    <div class="btn-save">
      <span class="dialog-footer">
        <el-button @click="editVisible = false">取 消</el-button>
        <el-button type="primary" @click="savePwd">确 定</el-button>
      </span>
    </div>
  </el-dialog>
</template>

<script>
import {getCurrentInstance, reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import request from '../../utils/request';

export default {
  name: "Password",
  setup(){
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    //修改密码弹出框控制位
    const editVisible = ref(false);
    // 修改密码的规则校验
    const editRules = {
      password : [
        { required: true, message: "旧密码不能为空", trigger: "blur" },
        { min: 6, message: "密码不能小于6位数", trigger: "blur" }
      ],
      newPassWord : [
        { required: true, message: "新密码不能为空", trigger: "blur" },
        { min: 6, message: "密码不能小于6位数", trigger: "blur" }
      ],
      cfmPassWord : [
        { required: true, message: "确认密码不能为空", trigger: "blur" },
        { min: 6, message: "密码不能小于6位数", trigger: "blur" },
        { type: 'string', message: '与新密码不一致', trigger: 'blur',
          transform (value) {
            if (value){
              if (value !== form.newPassWord) {
                return true
              }
            }
          }
        }
      ]
    };
    // 表单数据
    let form = reactive({
      newPassWord: "",
      cfmPassWord: "",
      password : ""
    });
    // 表单对象
    const editForm = ref(null);
    // 保存
    const savePwd = () => {
      editForm.value.validate((valid) => {
        if (valid) {
          axios.$http.post(request.modifyPwd,form).then(function (res) {
            if(res.code === 200){
              ElMessage.success('修改成功');
              editVisible.value = false;
            }else {
              ElMessage.error(res.data);
            }
          });
        } else {
          return false;
        }
      });
    };
    // 用户注册弹窗显示
    const shwoDialog = () => {
      editVisible.value = true;
    };
    return {editVisible,form,editRules,editForm,savePwd,shwoDialog};
  }
}
</script>

<style scoped>
.btn-save{
  text-align: center;
}
</style>