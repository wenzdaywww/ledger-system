<!-- Home父页面 -->
<template>
  <v-header />
  <div class="control-box main-div">
    <div class="content content-div">
      <router-view v-slot="{ Component }"></router-view>
    </div>
  </div>
</template>
<script>
import {provide} from "vue";
import vHeader from "../../components/Header.vue";

export default {
  components: {
    vHeader
  },
  setup() {
    //向Header子组件传值
    provide("isAdmin",false);
    return {};
  },
};
</script>
<style scoped>
  .content {
    background-image: url("/static/img/pm.jpg");
  }
  .main-div {
    height: 100%;
  }
  .content-div {
    overflow-y:hidden;
  }
</style>