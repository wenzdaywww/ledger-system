<!-- 我的账簿 -->
<template>
  <div  v-loading="pageLoading">
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
        <el-card shadow="hover" style="height:193px;">
          <template #header>
            <div class="clearfix">
              <span>数据分析</span>
            </div>
          </template>
          净利率
          <el-tooltip class="item" effect="light" content="净利率 = 净利润 / 总支出费 * 100%" placement="top">
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
      <!-- 账簿数据-->
      <el-col :span="16">
        <el-row :gutter="20" class="mgb20">
          <el-col :span="8">
            <el-tooltip class="item" effect="light" content="净利润 = 总销售额 - 总支出费" placement="top">
              <el-card shadow="hover" :body-style="{ padding: '0px' }">
                <div class="grid-content grid-con-3">
                  <i class="el-icon-data-line grid-con-icon"></i>
                  <div class="grid-cont-right">
                    <div class="grid-num">{{ bookData.retPro }}</div>
                    <div>净利润</div>
                  </div>
                </div>
              </el-card>
            </el-tooltip>
          </el-col>
          <el-col :span="8">
            <el-tooltip class="item" effect="light" content="毛利润 = 销售额 - 成本费" placement="top">
              <el-card shadow="hover" :body-style="{ padding: '0px' }">
                <div class="grid-content grid-con-3">
                  <i class="el-icon-data-analysis grid-con-icon"></i>
                  <div class="grid-cont-right">
                    <div class="grid-num">{{ bookData.groPro }}</div>
                    <div>毛利润</div>
                  </div>
                </div>
              </el-card>
            </el-tooltip>
          </el-col>
          <el-col :span="8">
            <el-tooltip class="item" effect="light" content="总销售额 = 所有店销售额合计" placement="top">
              <el-card shadow="hover" :body-style="{ padding: '0px' }">
                <div class="grid-content grid-con-3">
                  <i class="el-icon-sell grid-con-icon"></i>
                  <div class="grid-cont-right">
                    <div class="grid-num">{{ bookData.salAmt }}</div>
                    <div>总销售额</div>
                  </div>
                </div>
              </el-card>
            </el-tooltip>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mgb20">
          <el-col :span="8">
            <el-tooltip class="item" effect="light" content="总订单量 = 所有店订单量合计" placement="top">
              <el-card shadow="hover" :body-style="{ padding: '0px' }">
                <div class="grid-content grid-con-1">
                  <i class="el-icon-shopping-cart-full grid-con-icon"></i>
                  <div class="grid-cont-right">
                    <div class="grid-num">{{ bookData.talOrd }}</div>
                    <div>总订单量</div>
                  </div>
                </div>
              </el-card>
            </el-tooltip>
          </el-col>
          <el-col :span="8">
            <el-tooltip class="item" effect="light" content="总订单成交量 = 所有店成交单合计" placement="top">
              <el-card shadow="hover" :body-style="{ padding: '0px' }">
                <div class="grid-content grid-con-1">
                  <i class="el-icon-shopping-cart-1 grid-con-icon"></i>
                  <div class="grid-cont-right">
                    <div class="grid-num">{{ bookData.sucOrd }}</div>
                    <div>总订单成交量</div>
                  </div>
                </div>
              </el-card>
            </el-tooltip>
          </el-col>
          <el-col :span="8">
            <el-tooltip class="item" effect="light" content="总订单流失量 = 总订单量 - 总订单成交量" placement="top">
              <el-card shadow="hover" :body-style="{ padding: '0px' }">
                <div class="grid-content grid-con-1">
                  <i class="el-icon-shopping-cart-2 grid-con-icon"></i>
                  <div class="grid-cont-right">
                    <div class="grid-num">{{ bookData.faiOrd }}</div>
                    <div>总订单流失量</div>
                  </div>
                </div>
              </el-card>
            </el-tooltip>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mgb20">
          <el-col :span="8">
            <el-tooltip class="item" effect="light" content="总成本费 = 所有店成本费合计" placement="top">
              <el-card shadow="hover" :body-style="{ padding: '0px' }">
                <div class="grid-content grid-con-2">
                  <i class="el-icon-sold-out grid-con-icon"></i>
                  <div class="grid-cont-right">
                    <div class="grid-num">{{ bookData.cosAmt }}</div>
                    <div>总成本费</div>
                  </div>
                </div>
              </el-card>
            </el-tooltip>
          </el-col>
          <el-col :span="8">
            <el-tooltip class="item" effect="light" content="总推广费 = 所有店推广费合计" placement="top">
              <el-card shadow="hover" :body-style="{ padding: '0px' }">
                <div class="grid-content grid-con-2">
                  <i class="el-icon-monitor grid-con-icon"></i>
                  <div class="grid-cont-right">
                    <div class="grid-num">{{ bookData.advAmt }}</div>
                    <div>总推广费</div>
                  </div>
                </div>
              </el-card>
            </el-tooltip>
          </el-col>
          <el-col :span="8">
            <el-tooltip class="item" effect="light" content="总服务费 = 所有店服务费合计" placement="top">
              <el-card shadow="hover" :body-style="{ padding: '0px' }">
                <div class="grid-content grid-con-2">
                  <i class="el-icon-shopping-bag-1 grid-con-icon"></i>
                  <div class="grid-cont-right">
                    <div class="grid-num">{{ bookData.serAmt }}</div>
                    <div>总服务费</div>
                  </div>
                </div>
              </el-card>
            </el-tooltip>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mgb20">
          <el-col :span="8">
            <el-tooltip class="item" effect="light" content="总刷单费 = 所有店刷单费合计" placement="top">
              <el-card shadow="hover" :body-style="{ padding: '0px' }">
                <div class="grid-content grid-con-4">
                  <i class="el-icon-shopping-bag-2 grid-con-icon"></i>
                  <div class="grid-cont-right">
                    <div class="grid-num">{{ bookData.virAmt }}</div>
                    <div>总刷单费</div>
                  </div>
                </div>
              </el-card>
            </el-tooltip>
          </el-col>
          <el-col :span="8">
            <el-tooltip class="item" effect="light" content="总支出费 = 总成本费 + 总推广费 + 总服务费 + 总刷单费" placement="top">
              <el-card shadow="hover" :body-style="{ padding: '0px' }">
                <div class="grid-content grid-con-4">
                  <i class="el-icon-money grid-con-icon"></i>
                  <div class="grid-cont-right">
                    <div class="grid-num">{{ bookData.talCos }}</div>
                    <div>总支出费</div>
                  </div>
                </div>
              </el-card>
            </el-tooltip>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-4">
                <i class="el-icon-s-shop grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ bookData.shopNum }}</div>
                  <div>店铺数</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <!-- 最近一年销售趋势图-->
        <el-card shadow="hover">
          <schart ref="bar" class="schart" canvasId="bar" :options="shopSales"></schart>
        </el-card>
      </el-col>
      <el-col :span="12">
        <!-- 销售额最高店铺近10天销售额趋势图-->
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
    // 整个页面Loading 加载遮罩层控制
    const pageLoading = ref(false);
    // 用户信息
    const user = ref([]);
    // 账簿数据
    const bookData = ref([]);
    // 最近一年销售趋势图数据
    const shopSales = ref({
      type: "bar",
      title: {text: "近一年总销售额趋势图"},
      xRorate: 20,
      labels: [],
      datasets: [
        {
          label: "销售额（元）",
          data: [],
        }
      ]
    });
    // 销售额最高店铺近10天销售额趋势图
    const monthSales = ref({
      type: "line",
      title: {text: "近10天总销售额趋势图"},
      //labels:["2023.1.1", "2023.1.2", "2023.1.3", "2023.1.4", "2023.1.5"]
      labels: [],
      //datasets:[{label: "家电",ata: [1, 2, 3, 4, 5,6, 7, 8, 9, 10]}]
      datasets: []
    });
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
      //TODO 2023/3/28 22:15 待开发
    };
    // 统计年销售
    const handleCount = () => {
      pageLoading.value = true;
      axios.$http.post(request.bookCount,null).then(function (res) {
        pageLoading.value = false;
        ElMessage.success(res.data);
        getBookInfoData();
        getLastYeatData();
        getLast10DayData();
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
    // 查询最近一年销售趋势图
    const getLastYeatData = () => {
      axios.$http.get(request.bookYear,null).then(function (res) {
        shopSales.value.labels = [];
        shopSales.value.datasets[0].data = [];
        res.data.forEach(function (item) {
          shopSales.value.labels.push(item.month);
          shopSales.value.datasets[0].data.push(item.sales);
        });
      });
    };
    getLastYeatData();
    // 查询销售额最高店铺近10天销售额趋势图
    const getLast10DayData = () => {
      axios.$http.get(request.bookDay,null).then(function (res) {
        monthSales.value.datasets = [];
        monthSales.value.labels = [];
        let dateData = [];//日期坐标
        //res.data = [{day:2021/01/01,sales:100}]
        let shopData = {label:"销售额(元)",data:[]};
        res.data.forEach(function (item) {
          shopData.data.push(item.sales);
          dateData.push(item.day);
        });
        monthSales.value.datasets.push(shopData);
        monthSales.value.labels = dateData;
      });
    };
    getLast10DayData();
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
    return {user,openEdit,editRules,editForm,bookData,shopSales,monthSales,pageLoading,
      onSubmit,handleCount,handleExport};
  },
};
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
.schart {
  width: 100%;
  height: 300px;
}
</style>
