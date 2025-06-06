<template>
    <div class="login-container">
      <div class="login-box">
       <h2>重制{{username || '未知用户'}}的密码</h2>
       <br>

        <el-form
          ref="resetFormRef"
          :model="resetForm"
          :rules="rules"
          class="login-form"
        >
          <el-form-item prop="password">
            <el-input
              v-model="resetForm.password"
              placeholder="新的密码"
              :prefix-icon="User"
            />
          </el-form-item>

          <el-form-item prop="repeat">
            <el-input
              v-model="resetForm.repeat"
              placeholder="再次输入新的密码"
              :prefix-icon="User"
            />
          </el-form-item>


          <el-button
            type="primary"
            class="login-button"
            :loading="loading"
            @click="handleResetPassword"
          >
            <el-icon v-if="!loading"><Right /></el-icon>
            <span>{{ loading ? '密码重置中...' : '重置密码' }}</span>
          </el-button>


        </el-form>
      </div>
    </div>
  </template>

  <script setup>
  import { ref, reactive } from 'vue'
  import { useRouter, useRoute } from 'vue-router'
  import {
    User,
    Right,
  } from '@element-plus/icons-vue'
  import { ElMessage } from 'element-plus'
  import { resetPassword } from '@/api/resetPassword'

  defineOptions({
    name: 'userLogin'
  })


  const router = useRouter()
  const loading = ref(false)
  const resetFormRef = ref(null)

  const route = useRoute()
  const query = route.query
  const code = query.verificationCode || ''

  const username = query.username || ''
  const userId = query.userId || ''
  console.log('route', route.query)


  const resetForm = reactive({
    password: '',
    repeat: '',
  })

  const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (resetForm.repeat !== '') {
      resetForm.value?.validateField('repeat')
    }
    callback()
  }
}

const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== resetForm.password) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}


const rules = {
  password: [
    { required: true, validator: validatePass, trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  repeat: [
    { required: true, validator: validatePass2, trigger: 'blur' }
  ],
}

  // 在 login.vue 的 handleLogin 函数中修改跳转逻辑
  const handleResetPassword = async () => {
    if (code === '' || userId === '') {
      ElMessage({
        message: '无效的重置密码链接',
        type: 'error',
        duration: 2000
      })
      return
    }

    await resetFormRef.value.validate(async (valid) => {
      if (valid) {
        loading.value = true

        try {
          const resetData = {
            newPassword: resetForm.password,
            verificationCode: code,
            id: userId
          }

          const result = await resetPassword(resetData)
          console.log('ResetPassword response:', result) // 添加日志查看响应

          // 检查返回的状态码
          if (result.code === 200) {  // 修改为 200

            ElMessage({
              message: '密码重置成功',
              type: 'success',
              duration: 2000
            })

            // 延迟跳转，确保消息显示
            setTimeout(() => {

                router.push('/login')
            }, 1000)
          } else {
            ElMessage({
              message: result.msg || '密码重置失败，请联系管理员',
              type: 'error',
              duration: 2000
            })
          }
        } catch (error) {
          console.error('SendEmail error:', error)
          ElMessage({
            message: error.message || '重置密码失败',
            type: 'error',
            duration: 2000
          })
        } finally {
          loading.value = false
        }
      }
    })
  }

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
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 12px;

    .el-button {
      height: 44px;
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
