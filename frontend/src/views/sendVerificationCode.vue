<template>
  <div class="login-container">
    <div class="login-box">
      <h2>填写邮箱地址</h2>
      <br />

      <el-form ref="emailFormRef" :model="emailForm" :rules="rules" class="login-form">
        <el-form-item prop="email">
          <el-input v-model="emailForm.email" placeholder="邮箱" :prefix-icon="User" />
        </el-form-item>

        <el-button type="primary" class="login-button" :loading="loading" @click="handleSendEmail">
          <el-icon v-if="!loading"><Right /></el-icon>
          <span>{{ loading ? '发送邮件中' : '发送重置密码邮件' }}</span>
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { User, Right } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { sendVerificationCode } from '@/api/sendVerificationCode'

defineOptions({
  name: 'userLogin',
})

const router = useRouter()
const loading = ref(false)
const emailFormRef = ref(null)

const emailForm = reactive({
  email: '',
})

const rules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱', trigger: ['blur', 'change'] },
  ],
}

const handleSendEmail = async () => {
  await emailFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true

      try {
        const emailData = {
          email: emailForm.email,
          baseUrl: window.location.origin, // 获取当前页面的URL
        }

        const result = await sendVerificationCode(emailData)
        console.log('SendEmail response:', result) // 添加日志查看响应

        // 检查返回的状态码
        if (result.code === 200) {
          // 修改为 200
          ElMessage({
            message: '重置密码链接已发送到您的邮箱',
            type: 'success',
            duration: 2000,
          })

          // 延迟跳转，确保消息显示
          setTimeout(() => {
            router.push('/')
          }, 1000)
        } else {
          ElMessage({
            message: result.msg || '邮箱不存在',
            type: 'error',
            duration: 2000,
          })
        }
      } catch (error) {
        console.error('SendEmail error:', error)
        ElMessage({
          message: error.message || '重置密码失败',
          type: 'error',
          duration: 2000,
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

      &:hover,
      &:focus {
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
