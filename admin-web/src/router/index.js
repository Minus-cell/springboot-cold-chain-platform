import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store'

Vue.use(Router)

import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index'),
        meta: { title: '仪表盘', icon: 'el-icon-data-line' }
      }
    ]
  },
  {
    path: '/order',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Order',
        component: () => import('@/views/order/index'),
        meta: { title: '运单管理', icon: 'el-icon-document' }
      }
    ]
  },
  {
    path: '/vehicle',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Vehicle',
        component: () => import('@/views/vehicle/index'),
        meta: { title: '车辆管理', icon: 'el-icon-truck' }
      }
    ]
  },
  {
    path: '/temperature',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Temperature',
        component: () => import('@/views/temperature/index'),
        meta: { title: '温控管理', icon: 'el-icon-thermometer' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    meta: { title: '系统管理', icon: 'el-icon-setting' },
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/system/user'),
        meta: { title: '用户管理' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/system/role'),
        meta: { title: '角色管理' }
      }
    ]
  }
]

const createRouter = () => new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    next()
  } else {
    if (store.state.token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router
