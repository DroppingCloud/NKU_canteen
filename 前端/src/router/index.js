import { createRouter, createWebHistory } from 'vue-router'
import { useAppModeStore } from '../store/appMode'

// 定义路由
const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/admin',
    name: 'AdminHome',
    component: () => import('../views/admin/AdminHome.vue'),
    meta: { 
      title: '管理后台',
      requiresAdmin: true
    },
    children: [
      {
        path: 'canteens',
        name: 'AdminCanteens',
        component: () => import('../components/admin/CanteenManagement.vue'),
        meta: { 
          title: '食堂管理',
          requiresAdmin: true
        }
      },
      {
        path: 'stalls',
        name: 'AdminStalls',
        component: () => import('../components/admin/StallManagement.vue'),
        meta: { 
          title: '档口管理',
          requiresAdmin: true 
        }
      },
      {
        path: 'dishes',
        name: 'AdminDishes',
        component: () => import('../components/admin/DishManagement.vue'),
        meta: { 
          title: '菜品管理',
          requiresAdmin: true 
        }
      },
      {
        path: '',
        redirect: '/admin/canteens'
      }
    ]
  },
  {
    path: '/canteen/:id',
    name: 'Canteen',
    component: () => import('../views/Canteen.vue'),
    meta: { title: '食堂' }
  },
  {
    path: '/booth/:id',
    name: 'Booth',
    component: () => import('../views/Booth.vue'),
    meta: { title: '档口' }
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫设置页面标题和管理员权限检查
router.beforeEach((to, from, next) => {
  document.title = `${to.meta.title || '南开大学食堂信息管理系统'}`
  
  // 管理员权限检查
  if (to.matched.some(record => record.meta.requiresAdmin)) {
    const appModeStore = useAppModeStore()
    if (!appModeStore.isAdminMode) {
      // 如果不是管理员模式，跳转到首页
      next({ path: '/' })
      return
    }
  }
  
  next()
})

export default router 