<template>
   <!--侧边栏-->
   <el-container style="min-height: 100vh">
     <el-aside :width="sideWidth + 'px' " style="background-color: rgb(238, 241, 246); box-shadow: 2px 0 6px rgb(0 21 41 / 35%) ">
       <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow"/>
     </el-aside>
     <el-container>
       <el-header style=" font-size: 12px ;border-bottom:1px solid #ccc ; ">
       <Header :collapseBtnClass="collapseBtnClass" :collapse="collapses" :user="user"></Header>
       </el-header>
      <!-- el-icon-message 邮箱  el-icon-map-location 地址-->
       <el-main>
<!--         表示当前页面的子路由会在<router-view/>里面展示-->
         <router-view @refreshUser="getUser"></router-view>
       </el-main>
     </el-container>
   </el-container>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import request from "@/utils/request";
import Aside from "@/components/Aside.vue";
import Header from "@/components/Header.vue";
export default {
  name: 'HomeView',
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth : 200,
      logoTextShow :true,
      user:{},

    }
  },
//引进组件
  components:{
    Aside,
    Header
  },
  created() {
    //从后台过去最新的User
    this.getUser()
  },
  methods: {
    collapses()
    {
    this.isCollapse= !this.isCollapse
      if(this.isCollapse){//收缩
        this.sideWidth=64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      }else {//展开
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    getUser() {
      let username =localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      //从后台获取数据
       this.request.get("/user/username/" + username).then(res => {
         //重新赋值后台的最新User数据
        this.user = res.data
       })
    }
  }
}
</script>
