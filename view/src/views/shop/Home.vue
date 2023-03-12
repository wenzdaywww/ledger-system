<!-- 店铺中心Home父页面-->
<template>
  <div>
    <v-header />
    <div>
      <el-row>
        <el-col :span="4"><v-sidebar /></el-col>
        <el-col :span="20">
          <div class="content-box">
            <div class="content">
              <router-view v-slot="{ Component }">
                <transition name="move" mode="out-in">
                  <keep-alive :include="tagsList">
                    <component :is="Component" />
                  </keep-alive>
                </transition>
              </router-view>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import {computed, ref,provide } from "vue";
import { useStore } from "vuex";
import vHeader from "../../components/Header.vue";
import vSidebar from "../../components/Sidebar.vue";
import vTags from "../../components/Tags.vue";
export default {
  components: {
    vHeader,
    vSidebar,
    vTags,
  },
  setup() {
    //向子组件传值
    provide("isAdmin",true);
    const store = useStore();
    const tagsList = computed(() =>
        store.state.tagsList.map((item) => item.name)
    );
    const collapse = computed(() => store.state.collapse);
    return {
      tagsList,
      collapse,
    };
  },
};
</script>
<style scoped>
</style>