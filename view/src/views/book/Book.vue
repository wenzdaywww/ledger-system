<template>
  <div>
    <div class="crumb-title">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-notebook-1"></i> 我的账簿
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-row :gutter="20">
      <el-col :span="8">
        <!-- 用户信息-->
        <el-card shadow="hover" class="mgb20" style="height:252px;">
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
            <el-button v-if="openEdit" type="primary" @click="onSubmit" class="el-icon-check" round> 保存</el-button>
            <el-link href="javascript:void(0);" type="primary" @click="openEdit ? openEdit = false : openEdit = true"
                     style="margin-left: 20px;" :underline=false class="el-icon-edit">{{openEdit ? '取消编辑' : '编辑'}}</el-link>
            <el-button type="primary" class="el-icon-edit-outline" style="float: right;" round> 统计</el-button>
          </div>
        </el-card>
        <el-card shadow="hover" style="height:252px;">
          <template #header>
            <div class="clearfix">
              <span>数据分析</span>
            </div>
          </template>
          净利率<el-progress :percentage="bookData.retProRat" color="#42b983"></el-progress>
          毛利率<el-progress :percentage="bookData.groProRat" color="#f1e05a"></el-progress>
          订单成功率<el-progress :percentage="bookData.orderRat"></el-progress>
        </el-card>
      </el-col>
      <!-- 账簿数据-->
      <el-col :span="16">
        <el-row :gutter="20" class="mgb20">
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-3">
                <i class="el-icon-data-line grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ bookData.retPro }}</div>
                  <div>净利润</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-3">
                <i class="el-icon-data-analysis grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ bookData.groPro }}</div>
                  <div>毛利润</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-3">
                <i class="el-icon-sell grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ bookData.salAmt }}</div>
                  <div>销售额</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mgb20">
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-1">
                <i class="el-icon-shopping-cart-full grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ bookData.talOrd }}</div>
                  <div>订单量</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-1">
                <i class="el-icon-shopping-cart-1 grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ bookData.sucOrd }}</div>
                  <div>成功订单量</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-1">
                <i class="el-icon-shopping-cart-2 grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ bookData.faiOrd }}</div>
                  <div>失败订单量</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mgb20">
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-2">
                <i class="el-icon-sold-out grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ bookData.cosAmt }}</div>
                  <div>成本费</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-2">
                <i class="el-icon-monitor grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ bookData.advAmt }}</div>
                  <div>推广费</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-2">
                <i class="el-icon-shopping-bag-1 grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ bookData.serAmt }}</div>
                  <div>服务费</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mgb20">
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-2">
                <i class="el-icon-shopping-bag-2 grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ bookData.virAmt }}</div>
                  <div>刷单费</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <schart ref="bar" class="schart" canvasId="bar" :options="shopSales"></schart>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <schart ref="line" class="schart" canvasId="line" :options="monthSales"></schart>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Schart from "vue-schart";
import {getCurrentInstance, reactive, ref} from "vue";
import request from "../../utils/request";
import {ElMessage} from "element-plus";
export default {
  name: "Book",
  components: { Schart },
  setup() {
    // 接口请求
    const axios = getCurrentInstance().appContext.config.globalProperties;
    //打开编辑
    const openEdit = ref(false);
    // 用户信息
    let user = reactive({
      userId: "",
      userName : ""
    });
    // 账簿数据
    let bookData = reactive({
      retPro: 0,
      retProRat: 10,
      groPro : 0,
      groProRat : 20,
      talOrd: 0,
      sucOrd: 0,
      faiOrd: 0,
      orderRat : 30,
      salAmt : 0,
      cosAmt : 0,
      advAmt : 0,
      serAmt : 0,
      virAmt : 0
    });
    // 编辑用户的规则校验
    const shopSales = {
      type: "bar",
      title: {
        text: "最近一年销售额前3名店铺销售趋势图",
      },
      xRorate: 25,
      labels: ["1月", "2月", "3月",  "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
      datasets: [
        {
          label: "店铺1",
          data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
        },
        {
          label: "店铺2",
          data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
        },
        {
          label: "店铺3",
          data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
        }
      ],
    };
    // 编辑用户的规则校验
    const monthSales = {
      type: "line",
      title: {
        text: "近10天销售额前3名店铺销售额趋势图",
      },
      labels: [
          "2023.1.1", "2023.1.2", "2023.1.3", "2023.1.4", "2023.1.5",
          "2023.1.6", "2023.1.7", "2023.1.8", "2023.1.9", "2023.1.10"
      ],
      datasets: [
        {
          label: "家电",
          data: [
            1, 2, 3, 4, 5,
            6, 7, 8, 9, 10
          ]
        },
        {
          label: "百货",
          data: [
            11, 12, 13, 14, 15,
            16, 17, 18, 19, 20
          ]
        },
        {
          label: "食品",
          data: [
            21, 22, 23, 24, 25,
            26, 27, 28, 29, 30
          ]
        }
      ],
    };
    // 编辑用户的规则校验
    const editRules = {
      userName : [
        { required: true, message: "用户名称不能为空", trigger: "blur" }
      ]
    };
    // 表单对象
    const editForm = ref(null);
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
              openEdit.value = false;
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
    return {user,openEdit,editRules,editForm,bookData,shopSales,monthSales,
      onSubmit};
  },
};
</script>

<style scoped>
.userNm-show{
  font-size: 30px;
  color: #606266;
}

.el-row {
  margin-bottom: 20px;
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

.todo-item {
  font-size: 14px;
}

.todo-item-del {
  text-decoration: line-through;
  color: #999;
}

.schart {
  width: 100%;
  height: 300px;
}
</style>
