<template>
  <div>
    <el-row>
      <el-col :span="10">
        <!-- 用户信息-->
        <el-card shadow="hover" class="mgb20" style="height:250px;margin-right: 10px;">
          <div class="user-info">
            <img src="/static/img/img.jpg" class="user-avator" alt />
            <div class="user-info-cont">
              <el-form :model="user" :rules="editRules" ref="editForm">
                <el-form-item>
                  <span class="userNm-show" >{{ user.userId }}</span>
                </el-form-item>
                <el-form-item prop="userName">
                  <el-input class="userNm-show" v-if="openEdit" v-model="user.userName" maxlength="40" placeholder="请输入用户名称"></el-input>
                  <span class="userNm-show" v-else="openEdit">{{ user.userName }}</span>
                </el-form-item>
              </el-form>
            </div>
          </div>
          <div class="user-info-list">
            <el-button v-if="openEdit" type="primary" @click="onSubmit" class="el-icon-check" round plain> 保存</el-button>
            <el-link href="javascript:void(0);" type="primary" @click="openEdit ? openEdit = false : openEdit = true"
                     style="margin-left: 20px;" :underline=false class="el-icon-edit">{{openEdit ? '取消编辑' : '编辑'}}</el-link>
            <el-tooltip class="item" effect="light" content="根据销售数据统计后导出" placement="top">
              <el-button type="danger" class="el-icon-download" style="float: right;margin-left: 10px;" @click="handleExport" round plain> 导出报表</el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="light" content="根据销售数据统计" placement="top">
              <el-button type="success" class="el-icon-s-data" style="float: right;" @click="handleCount" round plain> 统计账簿</el-button>
            </el-tooltip>
          </div>
        </el-card>
      </el-col>
      <!--      数据分析-->
      <el-col :span="14">
        <el-card shadow="hover" style="height:250px;" class="mgr10">
          <template #header>
            <div class="clearfix">
              <span>数据分析</span>
            </div>
          </template>
          净利率
          <el-tooltip class="item" effect="light" content="净利率 = 净利润 / 支出费 * 100%" placement="top">
            <el-progress :percentage="bookData.retProRat" color="red"></el-progress>
          </el-tooltip>
          毛利率
          <el-tooltip class="item" effect="light" content="毛利率 = 毛利润 / 成本费 * 100%" placement="top">
            <el-progress :percentage="bookData.groProRat" color="#f1e05a"></el-progress>
          </el-tooltip>
          订单成交率
          <el-tooltip class="item" effect="light" content="订单成交率 = 成功订单量 / 订单量 * 100%" placement="top">
            <el-progress :percentage="bookData.sucOrdRat"></el-progress>
          </el-tooltip>
        </el-card>
      </el-col>
    </el-row>
    <!-- 账簿数据-->
    <el-row class="mgb20">
      <el-col :span="6">
        <el-tooltip class="item" effect="light" content="净利润 = 销售额 - 支出费" placement="top">
          <el-card shadow="hover" :body-style="{ padding: '0px' }" class="mgr10">
            <div class="grid-content grid-con-3">
              <i class="el-icon-lx-recharge grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{ bookData.retPro }}</div>
                <div>净利润</div>
              </div>
            </div>
          </el-card>
        </el-tooltip>
      </el-col>
      <el-col :span="6">
        <el-tooltip class="item" effect="light" content="毛利润 = 销售额 - 成本费" placement="top">
          <el-card shadow="hover" :body-style="{ padding: '0px' }" class="mgr10">
            <div class="grid-content grid-con-3">
              <i class="el-icon-lx-redpacket grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{ bookData.groPro }}</div>
                <div>毛利润</div>
              </div>
            </div>
          </el-card>
        </el-tooltip>
      </el-col>
      <el-col :span="6">
        <el-tooltip class="item" effect="light" content="销售额 = 所有店销售额合计" placement="top">
          <el-card shadow="hover" :body-style="{ padding: '0px' }" class="mgr10">
            <div class="grid-content grid-con-3">
              <i class="el-icon-sell grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{ bookData.salAmt }}</div>
                <div>销售额</div>
              </div>
            </div>
          </el-card>
        </el-tooltip>
      </el-col>
      <el-col :span="6">
        <el-tooltip class="item" effect="light" content="支出费 = 成本费 + 推广费 + 服务费 + 刷单费" placement="top">
          <el-card shadow="hover" :body-style="{ padding: '0px' }" class="mgr10">
            <div class="grid-content grid-con-3">
              <i class="el-icon-money grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{ bookData.talCos }}</div>
                <div>支出费</div>
              </div>
            </div>
          </el-card>
        </el-tooltip>
      </el-col>
    </el-row>
    <el-row class="mgb20">
      <el-col :span="6">
        <el-tooltip class="item" effect="light" content="订单量 = 所有店订单量合计" placement="top">
          <el-card shadow="hover" :body-style="{ padding: '0px' }" class="mgr10">
            <div class="grid-content grid-con-1">
              <i class="el-icon-shopping-cart-full grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{ bookData.talOrd }}</div>
                <div>订单量</div>
              </div>
            </div>
          </el-card>
        </el-tooltip>
      </el-col>
      <el-col :span="6">
        <el-tooltip class="item" effect="light" content="订单成交量 = 所有店成交单合计" placement="top">
          <el-card shadow="hover" :body-style="{ padding: '0px' }" class="mgr10">
            <div class="grid-content grid-con-1">
              <i class="el-icon-shopping-cart-1 grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{ bookData.sucOrd }}</div>
                <div>订单成交量</div>
              </div>
            </div>
          </el-card>
        </el-tooltip>
      </el-col>
      <el-col :span="6">
        <el-tooltip class="item" effect="light" content="订单流失量 = 订单量 - 订单成交量" placement="top">
          <el-card shadow="hover" :body-style="{ padding: '0px' }" class="mgr10">
            <div class="grid-content grid-con-1">
              <i class="el-icon-shopping-cart-2 grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{ bookData.faiOrd }}</div>
                <div>订单流失量</div>
              </div>
            </div>
          </el-card>
        </el-tooltip>
      </el-col>
      <el-col :span="6">
        <el-tooltip class="item" effect="light" content="根据【支出管理】统计所有店铺保证金" placement="top">
          <el-card shadow="hover" :body-style="{ padding: '0px' }" class="mgr10">
            <div class="grid-content grid-con-1">
              <i class="el-icon-lx-shop grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{ bookData.guatee }}</div>
                <div>店铺保证金</div>
              </div>
            </div>
          </el-card>
        </el-tooltip>
      </el-col>
    </el-row>
    <el-row class="mgb20">
      <el-col :span="6">
        <el-tooltip class="item" effect="light" content="成本费 = 所有店成本费合计" placement="top">
          <el-card shadow="hover" :body-style="{ padding: '0px' }" class="mgr10">
            <div class="grid-content grid-con-2">
              <i class="el-icon-sold-out grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{ bookData.cosAmt }}</div>
                <div>成本费</div>
              </div>
            </div>
          </el-card>
        </el-tooltip>
      </el-col>
      <el-col :span="6">
        <el-tooltip class="item" effect="light" content="推广费 = 所有店推广费合计" placement="top">
          <el-card shadow="hover" :body-style="{ padding: '0px' }" class="mgr10">
            <div class="grid-content grid-con-2">
              <i class="el-icon-lx-global grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{ bookData.advAmt }}</div>
                <div>推广费</div>
              </div>
            </div>
          </el-card>
        </el-tooltip>
      </el-col>
      <el-col :span="6">
        <el-tooltip class="item" effect="light" content="服务费 = 所有店服务费合计" placement="top">
          <el-card shadow="hover" :body-style="{ padding: '0px' }" class="mgr10">
            <div class="grid-content grid-con-2">
              <i class="el-icon-shopping-bag-1 grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{ bookData.serAmt }}</div>
                <div>服务费</div>
              </div>
            </div>
          </el-card>
        </el-tooltip>
      </el-col>
      <el-col :span="6">
        <el-tooltip class="item" effect="light" content="刷单费 = 所有店刷单费合计" placement="top">
          <el-card shadow="hover" :body-style="{ padding: '0px' }" class="mgr10">
            <div class="grid-content grid-con-2">
              <i class="el-icon-shopping-bag-2 grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{ bookData.virAmt }}</div>
                <div>刷单费</div>
              </div>
            </div>
          </el-card>
        </el-tooltip>
      </el-col>
    </el-row>
    <export-list ref="exportDialog"></export-list>
  </div>
</template>

<script>
import {getCurrentInstance, ref} from "vue";
import request from "../../utils/request";
import {ElMessage} from "element-plus";
import exportList from "./ExportList.vue";

export default {
  name: "Book",
  components: { exportList },
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    //打开编辑
    const openEdit = ref(false);
    // 整个页面Loading 加载遮罩层控制
    const pageLoading = ref(false);
    //报表导出弹出框对象
    const exportDialog = ref();
    // 用户信息
    const user = ref([]);
    // 账簿数据
    const bookData = ref([]);
    // 编辑用户的规则校验
    const editRules = {
      userName : [
        { required: true, message: "用户名称不能为空", trigger: "blur" }
      ]
    };
    // 表单对象
    const editForm = ref(null);
    // 导出报表
    const handleExport = () => {
      exportDialog.value.openExportDialog();
    };
    // 统计年销售
    const handleCount = () => {
      pageLoading.value = true;
      axios.$http.post(request.bookCount,null).then(function (res) {
        pageLoading.value = false;
        ElMessage.success(res.data);
        getBookInfoData();
      }).catch(err => {pageLoading.value = false;});
    };
    // 获取用户数据
    const getUserData = () => {
      axios.$http.get(request.userInfo,null).then(function (res) {
        user.value = res.data;
      });
    };
    getUserData();
    // 用户账簿信息
    const getBookInfoData = () => {
      axios.$http.get(request.bookInfo,null).then(function (res) {
        bookData.value = res.data;
      });
    };
    getBookInfoData();
    // 保存按钮
    const onSubmit = () => {
      editForm.value.validate((valid) => {
        if (valid) {
          pageLoading.value = true;
          axios.$http.post(request.editInfo,user.value).then(function (res) {
            pageLoading.value = false;
            ElMessage.success(res.data);
            openEdit.value = false;
            getUserData();
          }).catch(err => {pageLoading.value = false;});
        } else {
          return false;
        }
      });
    };
    return {openEdit,pageLoading,user,bookData,editRules,editForm,exportDialog,
      handleExport,handleCount,onSubmit};
  }
}
</script>

<style scoped>
.userNm-show{
  font-size: 30px;
  color: #606266;
}
.el-row {
  margin-bottom: -3px;
}
.grid-content {
  display: flex;
  align-items: center;
  height: 100px;
}
.grid-cont-right {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #999;
}
.grid-num {
  font-size: 30px;
  font-weight: bold;
}
.grid-con-icon {
  font-size: 50px;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
  color: #fff;
}
.grid-con-1 .grid-con-icon {
  background: rgb(45, 140, 240);
}
.grid-con-1 .grid-num {
  color: rgb(45, 140, 240);
}
.grid-con-2 .grid-con-icon {
  background: rgb(100, 213, 114);
}
.grid-con-2 .grid-num {
  color: rgb(45, 140, 240);
}
.grid-con-3 .grid-con-icon {
  background: rgb(242, 94, 67);
}
.grid-con-3 .grid-num {
  color: rgb(242, 94, 67);
}
.grid-con-4 .grid-con-icon {
  background: rgb(242, 157, 67);
}
.grid-con-4 .grid-num {
  color: rgb(242, 157, 67);
}
.user-info {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #ccc;
  margin-bottom: 20px;
}
.user-avator {
  width: 120px;
  height: 120px;
  border-radius: 50%;
}
.user-info-cont {
  padding-left: 50px;
  flex: 1;
  font-size: 20px;
  color: #000000;
}
.user-info-cont div:first-child {
  font-size: 30px;
  color: #222;
}
.user-info-list {
  font-size: 14px;
  color: #999;
  line-height: 25px;
}
.user-info-list span {
  margin-left: 70px;
}
.mgb20 {
  margin-bottom: 20px;
}
.mgr10 {
  margin-right: 10px;
}
</style>