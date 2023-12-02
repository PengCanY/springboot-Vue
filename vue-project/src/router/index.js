import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/Manage.vue'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import(/* webpackChunkName: "about" */ '../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import(/* webpackChunkName: "about" */ '../views/Register.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import(/* webpackChunkName: "about" */ '../views/404.vue')
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})
//提供一个重置路由的方法
export const resetRouter = () => {
 routes.matcher = new VueRouter({
   mode: 'history',
   base: process.env.BASE_URL,
   routes
 })
}

//刷新会导致路由重置，因为只再登录set了一下
export const setRoutes = () => {
  //第一从浏览器中取出集合
 const storeMenus = localStorage.getItem("menus");
 //判断有没有
 if(storeMenus){
   //拼装动态路由
 const manageRoute = { path: '/', name: 'Manage', component: () => import( '../views/Manage.vue'), redirect: "/home", children: [
     {path: 'person', name: '个人信息', component: () => import( '../views/Person.vue')}
   ] }
    const menus = JSON.parse(storeMenus)
   menus.forEach(item =>  {
     if(item.path) {//当且仅当path不为空的时候去设置路由
       let itemMenu = { path: item.path.replace("/", ""), name: item.name, component:() => import('../views/' + item.pagePath + '.vue')}
       manageRoute.children.push(itemMenu)
     }else if(item.children.length){
       item.children.forEach(item => {
         if(item.path) {
           let itemMenu = { path: item.path.replace("/",""), name: item.name, component:() => import('../views/' + item.pagePath + '.vue')}
           manageRoute.children.push(itemMenu)

         }
       })
     }
   })
   //获取当前的路由对象名称数组
   const currentRouteNames = router.getRoutes().map(v =>v.name)
   if(!currentRouteNames.includes('Manage')){
     //动态添加到现在路由对象中去
     router.addRoute(manageRoute)
   }
 }
}
//重置就再set一下
setRoutes()

//路由守卫
router.beforeEach((to,from,next)=>{
  localStorage.setItem("currentPathName",to.name)//设置当前路由名称，为了在Header组件中去使用
  store.commit("setPath")//触发store的数据更新

  //未找到路由的情况
  if(!to.matched.length){
    const storeMenus = localStorage.getItem("menus")
    if(storeMenus){
      next("/404")
    }else {
      //跳回登陆界面
      next("/login")
    }
  }
  //其他情况放行路由
  next()
})

export default router
