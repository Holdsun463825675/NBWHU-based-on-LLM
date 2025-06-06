<template>
  <div class="user-layout">
    <!-- 顶部导航栏 -->
    <el-header class="user-header">
      <div class="header-left">
        <img src="@/assets/whu.png" alt="Logo" class="logo">
        <h2>智能英语学习平台</h2>
      </div>
      <div class="header-right">
        <el-dropdown trigger="click">
          <span class="user-info">
            <el-avatar :size="40" :src="userAvatar" />
            <div class="user-details">
              <span class="username">{{ username }}</span>
              <span class="role">儿童</span>
            </div>
            <el-icon class="el-icon--right"><CaretBottom /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="getinfo">
                <el-icon><User /></el-icon>个人信息
              </el-dropdown-item>
              <el-dropdown-item @click="handleLogout">
                <el-icon><SwitchButton /></el-icon>退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <!-- 主要内容区域 -->
    <el-container class="main-container">
      <el-aside :width="isCollapse ? '80px' : '160px'" class="sidebar-container">
        <div class="sidebar-header">
          <el-button class="collapse-btn" @click="toggleSidebar">
            <el-icon><Fold v-if="!isCollapse" /><Expand v-else /></el-icon>
          </el-button>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="user-menu"
          :collapse="isCollapse"
          @select="handleSelect"
        >
          <el-menu-item index="/child">
            <el-icon><User /></el-icon>
            <template #title>个人主页</template>
          </el-menu-item>
          <el-menu-item index="/child/word">
            <el-icon><Search /></el-icon>
            <template #title>单词查询</template>
          </el-menu-item>
          <el-menu-item index="/child/learn">
            <el-icon><Notebook /></el-icon>
            <template #title>每日学习</template>
          </el-menu-item>
          <el-menu-item index="/child/read">
            <el-icon><Reading /></el-icon>
            <template #title>语境阅读</template>
          </el-menu-item>
          <el-menu-item index="/child/chat">
            <el-icon><ChatLineRound /></el-icon>
            <template #title>情景对话</template>
          </el-menu-item>
          <el-menu-item index="/child/test">
            <el-icon><EditPen /></el-icon>
            <template #title>测试</template>
          </el-menu-item>
          <el-menu-item index="/child/note">
            <el-icon><Star /></el-icon>
            <template #title>收藏</template>
          </el-menu-item>
          <el-menu-item index="/child/mean">
            <el-icon><Promotion /></el-icon>
            <template #title>语义探词</template>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-main>
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  User,
  Reading,
  Notebook,
  Search,
  SwitchButton,
  CaretBottom,
  Fold,
  Expand,
  ChatLineRound,
  EditPen,
  Star,
  Promotion
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const username = computed(() => localStorage.getItem('username') || '儿童')
const userAvatar = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png')
const activeMenu = computed(() => route.path)
const isCollapse = ref(true)

const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

const handleSelect = (path) => {
  router.push(path)
}

const getinfo = () => {
  router.push('/child')
}

const handleLogout = () => {
  localStorage.clear()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style lang="scss" scoped>
.user-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f6f8fc;
}

.user-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: #ffffff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.06);
  height: 64px;
  position: relative;
  z-index: 10;
  backdrop-filter: blur(10px);

  .header-left {
    display: flex;
    align-items: center;
    gap: 16px;

    .logo {
      height: 44px;
      transition: transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

      &:hover {
        transform: scale(1.05);
      }
    }

    h2 {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
      background: linear-gradient(45deg, #1a1a1a, #4a4a4a);
      background-clip: text;
      -webkit-background-clip: text;
      color: transparent;
      letter-spacing: 0.5px;
    }
  }

  .header-right {
    .user-info {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 6px 12px;
      border-radius: 12px;
      transition: all 0.3s ease;
      cursor: pointer;

      &:hover {
        background: #f6f8fc;
      }

      .user-details {
        display: flex;
        flex-direction: column;
        line-height: 1.2;

        .username {
          font-size: 15px;
          font-weight: 500;
          color: #1a1a1a;
        }

        .role {
          font-size: 12px;
          color: #8c8c8c;
        }
      }
    }
  }
}

.sidebar-container {
  background: #ffffff;
  box-shadow: 1px 0 2px rgba(0, 0, 0, 0.06);
  position: relative;
  z-index: 9;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);

  .sidebar-header {
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding: 0 8px;

    .collapse-btn {
      width: 100%;
      height: 50px;
      padding: 0 20px !important;
      font-size: 22px;
      border: none;
      background: transparent;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      border-radius: 8px;

      .el-icon {
        width: 24px;
      }

      &:hover {
        background: #f6f8fc;
      }
    }
  }

  .user-menu {
    border-right: none;
    padding: 8px;

    :deep(.el-menu-item) {
      height: 50px;
      line-height: 50px;
      border-radius: 8px;
      margin: 4px 0;
      padding: 0 20px !important;
      display: flex;
      align-items: center;

      .el-icon {
        font-size: 22px;
        width: 24px;
        display: flex;
        align-items: center;
      }

      &:hover {
        background: #f6f8fc;
      }

      &.is-active {
        background-color: var(--el-color-primary-light-9);
      }
    }

    &.el-menu--collapse {
      width: 80px;

      :deep(.el-menu-item) {
        padding: 0 20px !important;
        justify-content: flex-start;

        .el-icon {
          margin: 0;
          width: 24px;
        }

        span {
          display: none;
        }
      }
    }
  }
}

.el-main {
  padding: 24px;
  background-color: var(--el-bg-color-page);
  overflow-y: auto;
  transition: all 0.3s ease;
}

// 过渡动画
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}

// 响应式设计
@media (max-width: 768px) {
  .user-header {
    padding: 0 16px;

    .header-left h2 {
      display: none;
    }

    .user-info .user-details {
      display: none;
    }
  }

  .sidebar-container {
    position: fixed;
    height: 100vh;
    left: 0;
    top: 64px;
  }

  .el-main {
    margin-left: 64px;
    padding: 16px;
  }
}
</style>
