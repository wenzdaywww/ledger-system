<template>
  <div class="header">
    <!-- 折叠按钮 -->
    <div v-if="isAdmin" class="collapse-btn" @click="collapseChage">
      <i v-if="!collapse" class="el-icon-s-fold"></i>
      <i v-else class="el-icon-s-unfold"></i>
    </div>
    <div class="logo header-item" @click="handleCommand('index')">首页</div>
    <div class="header-right">
      <!-- 未登录 -->
      <div v-if="isLogin == false" style="margin-top: 20px">
        <span class="regist-span" @click="shwoLogin">登录</span>
        <span class="regist-span" > / </span>
        <span class="regist-span" @click="shwoRegister">注册</span>
      </div>
      <!-- 已登录 -->
      <div class="header-user-con" v-if="isLogin == true">
        <!-- 用户名下拉菜单 -->
        <el-dropdown class="user-name" trigger="hover" @command="handleCommand">
          <!-- 用户头像 -->
          <div class="user-avator">
            <img :src="form.photo" />
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="book">我的账簿</el-dropdown-item>
              <el-dropdown-item command="pwd">修改密码</el-dropdown-item>
              <el-dropdown-item divided command="loginout" class="login-out">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <!-- 修改密码弹出框 -->
      <password ref="passwordDialog" v-if="isLogin == true"/>
    </div>
  </div>
</template>
<script>
import {computed, getCurrentInstance, inject, onMounted, reactive, ref} from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import utils from "../utils/utils";
import {clearUserRouter, initUserRouter} from "../router";
import request from "../utils/request";
import {ElMessage} from "element-plus";
import Password from "../views/user/info/Password.vue";

export default {
  name: "Header",
  components: {Password},
  mounted() {
    //定义可被同级页面调用的方法
    window['loginSuccess'] = () => {
      this.loginSuccess();
    }
  },
  setup(props, context) {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    // 路由
    const router = useRouter();
    //store存储对象
    const store = useStore();
    //从父组件获取传值
    const isAdmin = inject("isAdmin"); //是否显示顶部栏折叠按钮
    // 是否已登录
    const isLogin = ref(false);
    //侧边栏是否收缩
    const collapse = computed(() => store.state.collapse);
    // 表单数据
    let form = reactive({
      photo : "static/img/img.jpg"
    });
    //用户注册弹窗对象
    const passwordDialog = ref();
    // 登入成功方法调用
    const loginSuccess = () => {
      getToken();
    };
    // 侧边栏折叠
    const collapseChage = () => {
      store.commit("handleCollapse", !collapse.value);
    };
    // 显示用户登录弹窗
    const shwoLogin = () => {
      router.push("/login");
    };
    // 显示用户注册弹窗
    const shwoRegister = () => {
      router.push("/register");
    };
    // 侧边栏缩放点击
    onMounted(() => {
      if (document.body.clientWidth < 1500) {
        collapseChage();
      }
    });
    //获取token
    const getToken = () => {
      let user = utils.getUser();
      if (utils.isLogin()){
        initUserRouter(function (){
          isLogin.value = true;
        });
      }else{
        isLogin.value = false;
      }
    };
    getToken();
    // 用户头像点击方法
    const handleCommand = (command) => {
      // 退出
      if (command == "loginout") {
        axios.$http.post(request.logout,null).then(function (res) {
          ElMessage.success("退出成功");
          clearUserRouter();
          isLogin.value = false;
          router.push("/index");
        }).catch(function (res) {
          ElMessage.error("退出失败");
        });
      } else if (command == "book") { // 我的账簿
        router.push("/book");
      } else if (command == "pwd") { // 修改密码
        passwordDialog.value.shwoDialog();
      }else if(command == "index"){ //首页
        router.push("/index");
      }
    };
    return { isAdmin,isLogin,form, collapse,passwordDialog,
      shwoLogin,collapseChage, handleCommand,shwoRegister,loginSuccess};
  },
};
</script>
<style scoped>
.header {
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  font-size: 22px;
  color: #fff;
}
.collapse-btn {
  float: left;
  padding: 0 21px;
  cursor: pointer;
  line-height: 70px;
}
.header .logo {
  float: left;
  width: 50px;
  line-height: 70px;
}
.header-right {
  float: right;
  padding-right: 50px;
}
.header-user-con {
  display: flex;
  height: 70px;
  align-items: center;
}
.header-item{
  color: white;
  font-size: 18px;
  padding-left: 8px;
  padding-right: 8px;
}
.header-item:hover {
  background: rgb(40,52,70);
}
.btn-bell .el-icon-bell {
  color: #fff;
}
.user-name {
  margin-left: 10px;
}
.user-avator {
  margin-left: 20px;
}
.login-out{
  color: red;
}
.regist-span{
  color: red;
  font-size: 14px;
}
.regist-span:hover{
  color: #d97d7d;
  font-size: 14px;
  text-decoration: underline;
  padding-bottom: 1px;
  cursor:default;
}
.user-avator img {
  display: block;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
</style>
