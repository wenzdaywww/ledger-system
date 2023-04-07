<!-- 页面布局-侧边菜单栏 -->
<template>
  <div class="sidebar">
    <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" background-color="#324157"
             text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router>
      <template v-for="item in items">
        <template v-if="item.subs">
          <el-submenu :index="item.index" :key="item.index">
            <template #title>
              <i :class="item.icon"></i>
              <span>{{ item.title }}</span>
            </template>
            <template v-for="subItem in item.subs">
              <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.index">
                <template #title>{{ subItem.title }}</template>
                <el-menu-item v-for="(threeItem, i) in subItem.subs" :key="i" :index="threeItem.index">
                  {{ threeItem.title }}</el-menu-item>
              </el-submenu>
              <el-menu-item v-else :index="subItem.index" :key="subItem.index">{{ subItem.title }}
              </el-menu-item>
            </template>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item :index="item.index" :key="item.index">
            <i :class="item.icon"></i>
            <template #title>{{ item.title }}</template>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
import {computed} from "vue";
import {useStore} from "vuex";
import {useRoute, useRouter} from "vue-router";

export default {
  setup() {
    const items = [
      {
        icon: "el-icon-notebook-1",
        index: "/book",
        title: "我的账簿",
      },{
        icon: "el-icon-s-shop",
        index: "/shop",
        title: "我的店铺",
      },{
        icon: "el-icon-data-analysis",
        index: "/year",
        title: "年销售额",
      },{
        icon: "el-icon-data-line",
        index: "/month",
        title: "月销售额",
      },{
        icon: "el-icon-date",
        index: "/day",
        title: "日销售额",
      },{
        icon: "el-icon-shopping-cart-full",
        index: "/order",
        title: "订单信息",
      },{
        icon: "el-icon-bank-card",
        index: "/pay",
        title: "支出管理",
      }
    ];
    // 路由
    const route = useRoute();
    const store = useStore();
    const collapse = computed(() => store.state.collapse);
    //路由跳转
    const onRoutes = computed(() => {
      return route.path;
    });
    return {items,onRoutes,collapse};
  },
};
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 70px;
  bottom: 0;
  overflow-y: scroll;
}
.sidebar::-webkit-scrollbar {
  width: 0;
}
.sidebar-el-menu:not(.el-menu--collapse) {
  width: 200px;
}
.sidebar > ul {
  height: 100%;
}
</style>
