<template>
  <div class="register-container">
    <div class="register-box">
      <div class="register-header">
        <img src="@/assets/whu.png" alt="Logo" class="logo" />
        <h2>用户注册</h2>
      </div>

      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="rules"
        class="register-form"
        @keyup.enter="submitRegister"
      >
        <el-form-item prop="username">
          <el-input v-model="registerForm.username" placeholder="用户名" :prefix-icon="User" />
        </el-form-item>

        <el-form-item prop="email">
          <el-input v-model="registerForm.email" placeholder="邮箱" :prefix-icon="Message" />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="确认密码"
            :prefix-icon="Lock"
            show-password
            @keyup.enter="submitRegister"
          />
        </el-form-item>

        <el-form-item prop="identity">
          <el-select v-model="registerForm.identity" placeholder="请选择角色" style="width: 100%;">
            <el-option label="学生" value="学生" />
            <el-option label="职场人士" value="职场人士" />
            <el-option label="儿童" value="儿童" />
          </el-select>
        </el-form-item>

        <!-- 验证码输入和发送按钮 -->
        <el-form-item prop="verificationCode">
          <el-input
            v-model="registerForm.verificationCode"
            placeholder="请输入验证码"
            maxlength="6"
            style="width: 60%; margin-right: 8px;"
          />
          <el-button
            :disabled="!canSendCode || countdown > 0"
            @click="handleSendCode"
            style="width: 120px;"
          >
            {{ countdown > 0 ? `${countdown}s后重发` : '发送验证码' }}
          </el-button>
        </el-form-item>

        <el-form-item prop="agreement">
          <el-checkbox v-model="registerForm.agreement">
            我已阅读并同意
            <el-button type="text" @click="showAgreement">服务协议</el-button>
            和
            <el-button type="text" @click="showPrivacy">隐私政策</el-button>
          </el-checkbox>
        </el-form-item>

        <el-button
          type="primary"
          class="register-button"
          :loading="loading"
          @click="submitRegister"
        >
          <el-icon v-if="!loading"><Right /></el-icon>
          <span>{{ loading ? '注册中...' : '注册' }}</span>
        </el-button>

        <div class="login-link">
          已有账号？
          <el-button type="text" @click="navigateTo('/login')">
            <el-icon><ArrowRight /></el-icon>立即登录
          </el-button>
        </div>
      </el-form>
    </div>

    <!-- 服务协议对话框 -->
    <el-dialog
      v-model="agreementVisible"
      title="服务协议"
      width="60%"
      class="agreement-dialog"
      align-center
      :modal="true"
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <div class="agreement-content">
        <h3>智能英语学习平台服务协议</h3>
        <p class="last-updated">最后更新日期：2025年5月</p>

        <section>
          <h4>1. 服务内容</h4>
          <p>本系统提供</p>
        </section>

        <section>
          <h4>2. 用户责任</h4>
          <ul>
            <li>保护账号安全，不得将账号借予他人使用</li>
            <li>不得利用本系统进行任何违法或不当行为</li>
          </ul>
        </section>

        <section>
          <h4>3. 知识产权</h4>
          <p>本系统的所有内容，包括但不限于文字、图片、数据库等，均受著作权法和其他知识产权法律法规的保护。</p>
        </section>

        <section>
          <h4>4. 服务变更与终止</h4>
          <p>本系统保留随时修改或终止服务的权利，修改后的服务协议将公布于网站上。</p>
        </section>
      </div>
    </el-dialog>

    <!-- 隐私政策对话框 -->
    <el-dialog
      v-model="privacyVisible"
      title="隐私政策"
      width="60%"
      class="privacy-dialog"
      align-center
      :modal="true"
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <div class="privacy-content">
        <h3>武汉大学图书馆管理系统隐私政策</h3>
        <p class="last-updated">最后更新日期：2024年3月</p>

        <section>
          <h4>1. 信息收集</h4>
          <p>我们收集的信息包括：</p>
          <ul>
            <li>基本信息：用户名、邮箱等</li>
          </ul>
        </section>

        <section>
          <h4>2. 信息使用</h4>
          <p>我们收集的信息将用于：</p>
          <ul>
            <li>系统功能优化</li>
            <li>用户体验改进</li>
            <li>安全管理与风险控制</li>
          </ul>
        </section>

        <section>
          <h4>3. 信息保护</h4>
          <p>我们采取严格的数据安全措施，包括但不限于加密存储、访问控制等，保护您的个人信息安全。</p>
        </section>

        <section>
          <h4>4. 信息共享</h4>
          <p>除法律法规规定或用户授权外，我们不会向第三方分享用户个人信息。</p>
        </section>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, Message, Right, ArrowRight } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { register } from '@/api/register'
import { debounce } from 'lodash-es'
import { sendVerificationCode } from '@/api/sendVerificationCode'

defineOptions({
  name: 'userRegister',
})

const router = useRouter()
const loading = ref(false)
const registerFormRef = ref(null)
const countdown = ref(0)
let timer = null

const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  identity: '', 
  verificationCode: '',
  agreement: false,
})

const agreementVisible = ref(false)
const privacyVisible = ref(false)

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (registerForm.confirmPassword !== '') {
      registerFormRef.value?.validateField('confirmPassword')
    }
    callback()
  }
}

const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' },
  ],
  password: [
    { required: true, validator: validatePass, trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
  ],
  confirmPassword: [{ required: true, validator: validatePass2, trigger: 'blur' }],
  verificationCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码为6位', trigger: 'blur' }
  ],
  agreement: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请阅读并同意服务协议和隐私政策'))
        } else {
          callback()
        }
      },
      trigger: 'change',
    },
  ],
}

// 只有所有必填项填写且校验通过后才能发送验证码
const canSendCode = computed(() => {
  return (
    registerForm.username &&
    registerForm.email &&
    registerForm.password &&
    registerForm.confirmPassword &&
    registerForm.agreement &&
    !loading.value
  )
})

// 发送验证码
const handleSendCode = async () => {
  if (!canSendCode.value) {
    ElMessage.error('请先填写完整注册信息')
    return
  }
  // 可选：可先校验表单
  try {
    await registerFormRef.value.validateField(['username', 'email', 'password', 'confirmPassword', 'agreement'])
  } catch {
    ElMessage.error('请先填写完整注册信息')
    return
  }
  try {
    // 这里传递 username 和 email
    await sendVerificationCode({
      username: registerForm.username,
      email: registerForm.email
    })
    ElMessage.success('验证码已发送到您的邮箱，请查收')
    countdown.value = 60
    timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(timer)
        timer = null
      }
    }, 1000)
  } catch (error) {
    ElMessage.error('验证码发送失败：' + (error.msg || error.message))
  }
}

// 页面卸载时清理定时器
onUnmounted(() => {
  if (timer) clearInterval(timer)
})

// 注册时将验证码一并提交
const submitRegister = async () => {
  if (!registerFormRef.value) return
  if (loading.value) return

  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const response = await register({
          ...registerForm
        })
        if (response.code === 1) {
          ElMessage.success('注册成功')
          router.push('/login')
        } else {
          ElMessage.error(response.message || '')
        }
      } catch (error) {
        ElMessage.error('注册失败：' + error.message)
      } finally {
        loading.value = false
      }
    }
  })
}

const showAgreement = () => {
  agreementVisible.value = true
}

const showPrivacy = () => {
  privacyVisible.value = true
}

const navigateTo = (path) => {
  router.push(path)
}
</script>

<style lang="scss" scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--el-color-primary-light-7), var(--el-color-primary));
  padding: 20px;
}

.register-box {
  width: 100%;
  max-width: 400px;
  background: var(--el-bg-color);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  animation: slideUp 0.5s ease-out;
}

.register-header {
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
}

.register-form {
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

  .el-form-item {
    margin-bottom: 20px;

    &:last-child {
      margin-bottom: 0;
    }
  }
}

.register-button {
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

.login-link {
  text-align: center;
  margin-top: 16px;
  color: var(--el-text-color-secondary);

  .el-button {
    padding: 0 4px;
    font-weight: 500;

    &:hover {
      color: var(--el-color-primary);
    }
  }
}

// 协议复选框样式
.el-checkbox {
  margin-top: 8px;
  color: var(--el-text-color-secondary);

  :deep(.el-checkbox__label) {
    display: inline-flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 4px;
  }

  .el-button {
    padding: 0 4px;
    height: auto;
    font-weight: 500;

    &:hover {
      color: var(--el-color-primary);
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
  .register-box {
    padding: 30px 20px;
  }
}

.agreement-dialog,
.privacy-dialog {
  :deep(.el-dialog) {
    margin: 0 auto !important;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }

  :deep(.el-dialog__body) {
    padding: 24px 32px;
    max-height: 60vh;
    overflow-y: auto;
  }

  .agreement-content,
  .privacy-content {
    h3 {
      text-align: center;
      color: var(--el-text-color-primary);
      margin-bottom: 8px;
    }

    .last-updated {
      text-align: center;
      color: var(--el-text-color-secondary);
      font-size: 14px;
      margin-bottom: 24px;
    }

    section {
      margin-bottom: 24px;

      h4 {
        color: var(--el-text-color-primary);
        margin-bottom: 12px;
        font-size: 16px;
      }

      p {
        color: var(--el-text-color-regular);
        line-height: 1.6;
        margin-bottom: 12px;
      }

      ul {
        list-style: disc;
        padding-left: 20px;
        color: var(--el-text-color-regular);

        li {
          margin-bottom: 8px;
          line-height: 1.6;

          &:last-child {
            margin-bottom: 0;
          }
        }
      }
    }
  }
}

// 响应式设计补充
@media (max-width: 768px) {
  .agreement-dialog,
  .privacy-dialog {
    :deep(.el-dialog) {
      width: 90% !important;
      margin: 0 auto !important;
    }
  }
}
</style>
