import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/home.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/login.vue'),
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/register.vue'),
    },
    {
      path: '/sendVerificationCode',
      name: 'sendVerificationCode',
      component: () => import('../views/sendVerificationCode.vue'),
    },
    {
      path: '/resetPassword',
      name: 'resetPassword',
      component: () => import('../views/resetPassword.vue'),
    },
    // 儿童模块
    {
      path: '/child',
      component: () => import('../layouts/childLayout.vue'),
      children: [
        {
          path: '',
          name: 'childHome',
          component: () => import('@/views/child/childHome.vue')
        },
        {
          path: 'word',
          name: 'childWord',
          component: () => import('@/views/child/childwordsearch.vue'),
        },
        {
          path: 'learn',
          name: 'childLearn',
          component: () => import('@/views/child/childdailylearn.vue')
        },
        {
          path: 'read',
          name: 'childRead',
          component: () => import('@/views/child/childreading.vue')
        },
        {
          path: 'chat',
          name: 'childChat',
          component: () => import('@/views/child/childchat.vue')
        },
        {
          path: 'test',
          name: 'childTest',
          component: () => import('@/views/child/childdailytest.vue')
        },
        {
          path: 'note',
          name: 'childnote',
          component: () => import('@/views/child/childnotebook.vue')
        },
        {
          path: 'mean',
          name: 'childmean',
          component: () => import('@/views/child/childmean.vue')
        }
      ],
    },
    // 学生模块
    {
      path: '/student',
      component: () => import('@/layouts/studentLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'studentHome',
          component: () => import('@/views/student/studentHome.vue')
        },
        {
          path: 'word',
          name: 'studentWord',
          component: () => import('@/views/student/studentwordsearch.vue'),
        },
        {
          path: 'dictionary',
          name: 'studentdictionary',
          component: () => import('@/views/student/studentdictionary.vue')
        },
        {
          path: 'learn',
          name: 'studentLearn',
          component: () => import('@/views/student/studentdailylearn.vue')
        },
        {
          path: 'chat',
          name: 'studentChat',
          component: () => import('@/views/student/studentchat.vue')
        },
        {
          path: 'test',
          name: 'studentTest',
          component: () => import('@/views/student/studentdailytest.vue')
        },
        {
          path: 'read',
          name: 'studentRead',
          component: () => import('@/views/student/studentreading.vue')
        },
        {
          path: 'note',
          name: 'studentnote',
          component: () => import('@/views/student/studentnotebook.vue')
        },
        {
          path: 'mean',
          name: 'studentmean',
          component: () => import('@/views/student/studentmean.vue')
        }
      ]
    },
    // 职场人模块
    {
      path: '/worker',
      component: () => import('@/layouts/workerLayout.vue'),
      meta: { requiresAuth: true, requiresAdmin: true },
      children: [
        {
          path: '',
          name: 'workerHome',
          component: () => import('@/views/worker/workerHome.vue'),
        },
        {
          path: 'word',
          name: 'workerWord',
          component: () => import('@/views/worker/workerwordsearch.vue'),
        },
        {
          path: 'dictionary',
          name: 'workerdictionary',
          component: () => import('../views/worker/workerdictionary.vue'),
        },
        {
          path: 'learn',
          name: 'workerlearn',
          component: () => import('../views/worker/workerdailylearn.vue'),
        },
        {
          path: 'test',
          name: 'workerTest',
          component: () => import('../views/worker/workerdailytest.vue'),
        },
        {
          path: 'read',
          name: 'workerRead',
          component: () => import('../views/worker/workerreading.vue'),
        },
        {
          path: 'chat',
          name: 'workerChat',
          component: () => import('../views/worker/workerchat.vue'),
        },
        {
          path: 'note',
          name: 'workernote',
          component: () => import('@/views/worker/workernotebook.vue')
        },
        {
          path: 'mean',
          name: 'workermean',
          component: () => import('@/views/worker/workermean.vue')
        }
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'notFound',
      component: () => import('../views/notFound.vue'),
    },
  ],
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token')
  const userRole = localStorage.getItem('userRole')

  // 需要登录但未登录，跳转到登录页
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login')
    return
  }

  // worker模块只允许worker用户
  if (to.path.startsWith('/worker') && userRole !== 'worker') {
    next('/')
    return
  }

  // student模块只允许student用户
  if (to.path.startsWith('/student') && userRole !== 'student') {
    next('/')
    return
  }

  // child模块只允许child用户
  if (to.path.startsWith('/child') && userRole !== 'child') {
    next('/')
    return
  }

  next()
})

export default router
