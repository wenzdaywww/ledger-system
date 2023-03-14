<!-- 用户信息 -->
<template>
  <div>
    <el-card>
      <template #header>
        <div>
          <span>信息修改</span>
        </div>
      </template>
      <el-form label-width="90px" :model="user" :rules="editRules" ref="editForm">
        <el-form-item label-width="20%" label="用户ID："> {{ user.userId }} </el-form-item>
        <el-form-item label-width="20%" label="用户名称：" prop="userName">
          <el-input v-if="openEdit" v-model="user.userName" maxlength="100" placeholder="请输入用户名称"></el-input>
          <span v-else="openEdit">{{ user.userName }}</span>
        </el-form-item>
        <el-form-item>
          <el-button v-if="openEdit" type="primary" @click="onSubmit" round>保存</el-button>
          <el-link href="javascript:void(0);" type="primary" @click="openEdit ? openEdit = false : openEdit = true"
                   style="margin-left: 20px;" :underline=false>{{openEdit ? '取消编辑' : '编辑'}}</el-link>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import request from "../../../utils/request";

export default {
  name: "Info",
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 编辑用户的规则校验
    const editRules = {
      userName : [
        { required: true, message: "用户名称不能为空", trigger: "blur" }
      ]
    };
    // 表单对象
    const editForm = ref(null);
    // 用户信息
    let user = reactive({
      userId: "",
      userName : ""
    });
    //打开编辑
    const openEdit = ref(false);
    // 获取用户数据
    const getUserData = () => {
      axios.$http.get(request.userInfo,null).then(function (res) {
        if(res.code === 200){
          user.userId = res.data.userId;
          user.userName = res.data.userName;
        }else {
          ElMessage.success('查询用户信息失败');
        }
      });
    };
    getUserData();
    // 保存按钮
    const onSubmit = () => {
      editForm.value.validate((valid) => {
        if (valid) {
          axios.$http.post(request.editInfo,user).then(function (res) {
            if(res.code === 200){
              ElMessage.success('修改成功');
              getUserData();
            }else {
              ElMessage.error(res.data);
            }
          });
        } else {
          return false;
        }
      });
    };
    return {openEdit,editRules, editForm, user, onSubmit};
  }
}
</script>

<style scoped>
</style>