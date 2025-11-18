import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Portal from '../views/Portal.vue'
import LeaveManagement from '../views/LeaveManagement.vue'
import OvertimeManagement from '../views/OvertimeManagement.vue'
import UserManagement from '../views/UserManagement.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/portal',
    name: 'Portal',
    component: Portal,
    children: [
      {
        path: '',
        redirect: '/portal/leave'
      },
      {
        path: 'leave',
        name: 'Leave',
        component: LeaveManagement
      },
      {
        path: 'overtime',
        name: 'Overtime',
        component: OvertimeManagement
      },
      {
        path: 'users',
        name: 'Users',
        component: UserManagement,
        meta: { requiresAdmin: true }
      }
    ]
  },
  {
    path: '/',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守衛 - 簡化版本
router.beforeEach((to, from, next) => {
  // 登入頁面直接放行
  if (to.path === '/login') {
    next()
  } else {
    // 其他頁面也放行，讓組件自己檢查認證
    // Portal.vue 會在 mounted 時檢查
    next()
  }
})

export default router
