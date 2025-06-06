<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <img src="@/assets/whu.png" alt="Logo" class="logo">
        <h2>欢迎登录</h2>
        <p class="subtitle">智能英语学习平台</p>
      </div>

      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="rules"
        class="login-form"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            :prefix-icon="User"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            :prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <div class="remember-forgot">
          <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
          <el-button type="text" @click="handleForgotPassword">忘记密码？</el-button>
        </div>

        <el-button
          type="primary"
          class="login-button"
          :loading="loading"
          @click="handleLogin"
        >
          <el-icon v-if="!loading"><Right /></el-icon>
          <span>{{ loading ? '登录中...' : '登录' }}</span>
        </el-button>

        <div class="divider">
          <span>或者</span>
        </div>

        <div class="action-buttons">
          <el-button @click="handleNavigate('/register')">
            <el-icon><UserFilled /></el-icon>注册账号
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  User,
  Lock,
  Right,
  Reading,
  UserFilled
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { login } from '@/api/auth'
import { debounce } from 'lodash-es'

defineOptions({
  name: 'userLogin'
})

const router = useRouter()
const loading = ref(false)
const loginFormRef = ref(null)

const loginForm = reactive({
  username: '',
  password: '',
  remember: false
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}


const handleLogin = debounce(async () => {
  if (!loginFormRef.value) return

  // 如果正在加载中，不要重复提交
  if (loading.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      // 测试账号直通逻辑
      if (
        loginForm.username === 'test' &&
        loginForm.password === '123456'
      ) {
        localStorage.setItem('token', 'test-token')
        localStorage.setItem('userRole', 'worker')
        localStorage.setItem('username', 'test')
        if (loginForm.remember) {
          localStorage.setItem('rememberedUsername', 'test')
        }
        ElMessage({
          message: '测试账号登录成功',
          type: 'success',
          duration: 2000
        })
        setTimeout(() => {
          router.push('/worker')
        }, 1000)
        loading.value = false
        return
      }
      if (
        loginForm.username === 'test1' &&
        loginForm.password === '123456'
      ) {
        localStorage.setItem('token', 'test-token')
        localStorage.setItem('userRole', 'student')
        localStorage.setItem('username', 'test1')
        if (loginForm.remember) {
          localStorage.setItem('rememberedUsername', 'test1')
        }
        ElMessage({
          message: '测试账号登录成功',
          type: 'success',
          duration: 2000
        })
        setTimeout(() => {
          router.push('/student')
        }, 1000)
        loading.value = false
        return
      }
      if (
        loginForm.username === 'test2' &&
        loginForm.password === '123456'
      ) {
        localStorage.setItem('token', 'test-token')
        localStorage.setItem('userRole', 'child')
        localStorage.setItem('username', 'test2')
        if (loginForm.remember) {
          localStorage.setItem('rememberedUsername', 'test2')
        }
        ElMessage({
          message: '测试账号登录成功',
          type: 'success',
          duration: 2000
        })
        setTimeout(() => {
          router.push('/child')
        }, 1000)
        loading.value = false
        return
      }
      // 正常登录逻辑
      try {
        const loginData = {
          username: loginForm.username,
          password: loginForm.password
        }

        const result = await login(loginData)
        console.log('Login response:', result)

        if (result.code === 1) {
          const { data, message } = result
          // 角色映射
          let role = ''
          if (data.identity && data.identity.includes('学生')) role = 'student'
          else if (data.identity && data.identity.includes('职场人士')) role = 'worker'
          else if (data.identity && data.identity.includes('儿童')) role = 'child'

          // 保存用户信息到 localStorage
          localStorage.setItem('token', message || '')
          localStorage.setItem('userRole', role)
          localStorage.setItem('username', data.username)
          sessionStorage.removeItem('todayWords')
          if (loginForm.remember) {
            localStorage.setItem('rememberedUsername', data.username)
          }

          ElMessage({
            message: '登录成功',
            type: 'success',
            duration: 2000
          })

          setTimeout(() => {
            const roleRouteMap = {
              'worker': '/worker',
              'student': '/student',
              'child': '/child'
            }
            const targetRoute = roleRouteMap[role]
            if (targetRoute) {
              router.push(targetRoute)
            } else {
              ElMessage.warning('未知的用户角色')
              router.push('/')
            }
          }, 1000)
        } else {
          ElMessage({
            message: result.message || '登录失败',
            type: 'error',
            duration: 2000
          })
        }
      } catch (error) {
        console.error('Login error:', error)
        ElMessage({
          message: error.message || '登录失败',
          type: 'error',
          duration: 2000
        })
      } finally {
        loading.value = false
      }
    }
  })
}, 300)

const handleForgotPassword = () => {
  router.push('/sendVerificationCode')
}

const handleNavigate = (path) => {
  router.push(path)
}

// 组件挂载时检查是否有记住的用户名
onMounted(() => {
  const rememberedUsername = localStorage.getItem('rememberedUsername')
  if (rememberedUsername) {
    loginForm.username = rememberedUsername
    loginForm.remember = true
  }
})
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--el-color-primary-light-7), var(--el-color-primary));
  padding: 20px;
}

.login-box {
  width: 100%;
  max-width: 400px;
  background: var(--el-bg-color);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  animation: slideUp 0.5s ease-out;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;

  .logo {
    width: 80px;
    height: 80px;
    margin-bottom: 16px;
    animation: fadeIn 1s ease-out;
  }

  h2 {
    font-size: 28px;
    color: var(--el-text-color-primary);
    margin: 0 0 8px;
  }

  .subtitle {
    color: var(--el-text-color-secondary);
    font-size: 16px;
    margin: 0;
  }
}

.login-form {
  .el-input {
    --el-input-height: 44px;
    margin-bottom: 16px;

    :deep(.el-input__wrapper) {
      border-radius: 12px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
      transition: all 0.3s ease;

      &:hover, &:focus {
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }
    }

    :deep(.el-input__prefix) {
      font-size: 18px;
    }
  }
}

.remember-forgot {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 16px 0;

  .el-checkbox {
    color: var(--el-text-color-secondary);
  }

  .el-button {
    padding: 0;
    font-size: 14px;
  }
}

.login-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  border-radius: 12px;
  margin: 24px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(var(--el-color-primary-rgb), 0.4);
  }
}

.divider {
  display: flex;
  align-items: center;
  margin: 24px 0;
  color: var(--el-text-color-secondary);

  &::before,
  &::after {
    content: '';
    flex: 1;
    height: 1px;
    background: var(--el-border-color-lighter);
  }

  span {
    padding: 0 16px;
    font-size: 14px;
  }
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 12px;

  .el-button {
    height: 44px;
    width: 200px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
    }

    .el-icon {
      font-size: 18px;
    }
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

// 响应式设计
@media (max-width: 480px) {
  .login-box {
    padding: 30px 20px;
  }

  .action-buttons {
    grid-template-columns: 1fr;
  }
}
</style>
