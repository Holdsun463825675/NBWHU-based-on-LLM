<template>
  <el-container class="layout-container">
    <el-header height="60px" class="layout-header">
      <div class="header-left">
        <img src="@/assets/logo.svg" alt="Logo" class="logo" />
        <h1 class="site-title">智能英语学习平台</h1>
      </div>
      <div class="header-right">
        <el-button :icon="Sunny" circle @click="toggleTheme" class="theme-toggle" />
        <template v-if="isLoggedIn">
          <el-dropdown>
            <el-avatar :src="userAvatar" />
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="navigateTo('/user')">个人中心</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button type="primary" @click="navigateTo('/login')">登录</el-button>
          <el-button @click="navigateTo('/register')">注册</el-button>
        </template>
      </div>
    </el-header>

    <el-container class="main-container">
      <slot></slot>
    </el-container>

    <el-footer height="60px" class="layout-footer">
      <p>© 2024 图书馆管理系统 All Rights Reserved.</p>
    </el-footer>
  </el-container>
</template>

<script setup>
import { ref, inject } from 'vue'
import { useRouter } from 'vue-router'
import { Sunny } from '@element-plus/icons-vue'

const router = useRouter()
const isLoggedIn = ref(false)
const userAvatar = ref('')
const toggleTheme = inject('toggleTheme')

const navigateTo = (path) => {
  router.push(path)
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userRole')
  router.push('/login')
}
</script>

<style lang="scss" scoped>
.layout-container {
  min-height: 100vh;
}

.layout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background: var(--bg-primary);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;

  .header-left {
    display: flex;
    align-items: center;
    gap: 12px;

    .logo {
      height: 40px;
    }

    .site-title {
      font-size: 1.5rem;
      color: var(--text-primary);
      margin: 0;
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 16px;
  }
}

.main-container {
  padding: 20px;
  background: var(--bg-secondary);
}

.layout-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-primary);
  color: var(--text-secondary);
}

.theme-toggle {
  background: transparent;
  border: none;

  &:hover {
    background: var(--bg-secondary);
  }
}
</style>
