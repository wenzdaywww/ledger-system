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
        <el-form-item label-width="20%" label="性别：">
          <el-radio v-if="openEdit" v-model="user.sex" label="1">男</el-radio>
          <el-radio v-if="openEdit" v-model="user.sex" label="0">女</el-radio>
          <span v-else="openEdit">{{ user.sex ? (user.sex === '1' ? '男' : '女') : '未知'}}</span>
        </el-form-item>
        <el-form-item label-width="20%" label="手机号：" prop="phoneNum">
          <el-input v-if="openEdit" v-model="user.phoneNum" maxlength="11" placeholder="请输入手机号码"
                    οninput="value=value.replace(/[^\d]/g,'');if(value.length > 11)value = value.slice(0, 11)"></el-input>
          <span v-else="openEdit">{{ user.phoneNum }}</span>
        </el-form-item>
        <el-form-item label-width="20%" label="生日：">
          <el-date-picker v-if="openEdit" v-model="user.birthday" type="date" value-format="YYYY-MM-DD" format="YYYY年MM月DD日" placeholder="请选择出生日期" style="width: 100%">
          </el-date-picker>
          <span v-else="openEdit">{{ user.birthday }}</span>
        </el-form-item>
        <el-form-item label-width="20%" label="邮箱：" prop="email">
          <el-input v-if="openEdit" v-model="user.email" maxlength="100" placeholder="请输入邮箱地址"></el-input>
          <span v-else="openEdit">{{ user.email }}</span>
        </el-form-item>
        <el-form-item label-width="20%" label="个性签名：">
          <el-input v-if="openEdit" v-model="user.brief" maxlength="100" placeholder="请输入个性签名"></el-input>
          <span v-else="openEdit">{{ user.brief }}</span>
        </el-form-item>
        <el-form-item>
          <el-button v-if="openEdit" type="primary" @click="onSubmit">保存</el-button>
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
      ],
      phoneNum: [
        { min: 11, message: "手机号格式不正确", trigger: "blur" },
        { type: 'number', message: '手机号格式不正确', trigger: 'blur',
          transform (value) {
            if(value){
              var phonereg = 11 && /^((13|14|15|16|17|18|19)[0-9]{1}\d{8})$/
              if (!phonereg.test(value)) {
                return false
              }else{
                return Number(value)
              }
            }
          }
        }
      ],
      email: [
        { type: 'string', message: '长度不能超过100位', trigger: 'blur', max: 100 },
        { type: 'string', message: '邮箱格式不正确', trigger: 'blur',
          transform (value) {
            if (value){
              if (!/^\w+((-\w+)|(\.\w+))*@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(value)) {
                return true
              }else{
              }
            }
          }
        }
      ]
    };
    // 表单对象
    const editForm = ref(null);
    // 用户信息
    let user = reactive({
      userId: "",
      userName : "",
      phoneNum : "",
      birthday : "",
      sex : "",
      photo : "static/img/img.jpg",
      email : "",
      brief : ""
    });
    //打开编辑
    const openEdit = ref(false);
    // 获取用户数据
    const getUserData = () => {
      axios.$http.get(request.userInfo,null).then(function (res) {
        if(res.code === 200){
          user.userId = res.data.userId;
          user.userName = res.data.userName;
          user.phoneNum = res.data.phoneNum;
          user.birthday = res.data.birthday;
          user.sex = res.data.sex;
          if(res.data.photo){
            user.photo = res.data.photo;
          }
          user.email = res.data.email;
          user.brief = res.data.brief;
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