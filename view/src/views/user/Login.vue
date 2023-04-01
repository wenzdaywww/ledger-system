<template>
  <div class="loginbody">
    <div class="logindata">
      <div class="logintext">
        <h2>Welcome</h2>
      </div>
      <div class="formdata">
        <el-form ref="loginForm" :model="form" :rules="rules">
          <el-form-item prop="id">
            <el-input v-model="form.id" placeholder="请输入账号" ></el-input>
          </el-form-item>
          <el-form-item prop="pwd">
            <el-input v-model="form.pwd" clearable placeholder="请输入密码" show-password></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div class="butt">
        <el-button type="primary" @click="saveToLogin" round>登录</el-button>
      </div>
      <div class="butt">
        <span class="back_home" @click="backHome">返回首页</span>
      </div>
    </div>
  </div>
</template>

<script>

import {getCurrentInstance, provide, reactive, ref} from "vue";
import request from "../../utils/request";
import router from "../../router";
import {ElMessage} from "element-plus";

export default {
  name: "Login",
  inject:['loginSuccess'],
  setup(){
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 用户的规则校验
    const rules = {
      id : [
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
      pwd : [
        { required: true, message: "密码不能为空", trigger: "blur" }
      ]
    };
    // 表单数据
    let form = reactive({
      id: "",
      pwd : ""
    });
    // 表单校验
    const loginForm = ref(null);
    // 新增页面的保存按钮
    const saveToLogin = () => {
      loginForm.value.validate((valid) => {
        if (valid) {
          axios.$http.post(request.login, form).then(function (res) {
            ElMessage.success("登入成功");
            router.push("/");
            //调用同级页面的Header方法loginSuccess
            window.parent.loginSuccess('Header');
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
    return {rules,loginForm,form,saveToLogin,backHome};
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