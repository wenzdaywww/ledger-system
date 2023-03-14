<template>
  <div class="loginbody">
    <div class="logindata">
      <div class="logintext">
        <h2>Welcome</h2>
      </div>
      <div class="formdata">
        <el-form label-width="120px" :model="form" :rules="addRules" ref="addForm">
          <el-form-item label="用户ID" prop="userId">
            <el-input v-model="form.userId" maxlength="20" placeholder="请输入用户ID"></el-input>
          </el-form-item>
          <el-form-item label="用户名称" prop="userName">
            <el-input v-model="form.userName" maxlength="40" placeholder="请输入用户名称"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" type="password" maxlength="20" placeholder="请输入密码"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div class="butt">
        <el-button type="primary" @click="saveAdd" round>注册</el-button>
      </div>
      <div class="butt">
        <span class="back_home" @click="backHome">返回首页</span>
      </div>
    </div>
  </div>
</template>

<script>

import {getCurrentInstance, reactive, ref} from "vue";
import request from "../../utils/request";
import {ElMessage} from "element-plus";
import router from "../../router";

export default {
  name: "Register",
  setup(){
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 新增用户的规则校验
    const addRules = {
      userId : [
        { required: true, message: "账号不能为空", trigger: "blur" },
        { type: 'string', message: '只能输入字母和数字', trigger: 'blur',
          transform (value) {
            if (value){
              if (/[^A-Za-z0-9]/.test(value)) {
                return true
              }else{
              }
            }
          }
        }
      ],
      userName : [
        { required: true, message: "用户名称不能为空", trigger: "blur" }
      ],
      password : [
        { required: true, message: "密码不能为空", trigger: "blur" }
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
      ]
    };
    // 表单数据
    let form = reactive({
      userId: "",
      userName : "",
      password : ""
    });
    // 表单校验
    const addForm = ref(null);
    // 新增页面的保存按钮
    const saveAdd = () => {
      addForm.value.validate((valid) => {
        if (valid) {
          axios.$http.post(request.register, form).then(function (res) {
            if(res.code === 200){
              ElMessage.success('注册成功');
              router.push("/login");
            }else {
              ElMessage.error(res.data);
            }
          });
        } else {
          return false;
        }
      });
    };
    // 首页页面跳转
    const backHome = () => {
      router.push("/index");
    };
    return {addRules,addForm,backHome,form,saveAdd};
  }
};
</script>

<style scoped>
  .loginbody {
    width: 100%;
    height: 100%;
    min-width: 1000px;
    background-image: url("../../static/img/pm.jpg");
    background-size: 100% 100%;
    background-position: center center;
    overflow: auto;
    background-repeat: no-repeat;
    position: fixed;
    line-height: 100%;
    padding-top: 150px;
  }
  .logintext {
    margin-bottom: 20px;
    line-height: 50px;
    text-align: center;
    font-size: 30px;
    font-weight: bolder;
    color: white;
    text-shadow: 2px 2px 4px #000000;
  }
  .logindata {
    width: 400px;
    height: 300px;
    transform: translate(-50%);
    margin-left: 50%;
  }
  .tool {
    display: flex;
    justify-content: space-between;
    color: #606266;
  }
  .butt {
    margin-top: 10px;
    text-align: center;
  }
  .shou {
    cursor: pointer;
    color: #606266;
  }
  .back_home:hover{
    color: #1778f1;
    font-size: 14px;
    text-decoration: underline;
    padding-bottom: 1px;
    cursor:default;
  }
  .back_home{
    color: #383e44;
    font-size: 14px;
    text-decoration: underline;
    padding-bottom: 1px;
    cursor:default;
  }
</style>