<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="home" :class="{ 'dark-mode': isDark }">
    <!-- 头部横幅 -->
    <section class="hero-section">
      <div class="hero-content">
        <h1 class="animate-title">探索知识的海洋</h1>
        <p class="animate-subtitle">在这里，与知识相遇，与智慧同行</p>
        <div class="hero-actions">
          <el-button size="large" class="action-button" @click="navigateTo('/login')">
            <el-icon><UserFilled /></el-icon>登录账号
          </el-button>
        </div>
      </div>
      <div class="hero-overlay"></div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  Compass,
  UserFilled,
  // Reading,
  // Document,
  // Search,
  // Management,
  // Moon,
  // Sunny,
} from '@element-plus/icons-vue'

const router = useRouter()
const isDark = ref(document.documentElement.getAttribute('data-theme') === 'dark')



// 页面跳转
const navigateTo = (path) => {
  router.push(path)
}


// 初始化主题
onMounted(() => {
  const savedTheme = localStorage.getItem('theme')
  if (savedTheme) {
    document.documentElement.setAttribute('data-theme', savedTheme)
    isDark.value = savedTheme === 'dark'
  }
})
</script>

<style lang="scss" scoped>
.home {
  --transition-timing: cubic-bezier(0.4, 0, 0.2, 1);
  --animation-duration: 0.3s;

  &.dark-mode {
    --hero-overlay: rgba(0, 0, 0, 0.7);
    --card-bg: var(--bg-primary);
    --card-hover: var(--bg-secondary);
  }

  .hero-section {
    position: relative;
    height: 100vh;
    min-height: 600px;
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    color: white;
    background-image: url('@/assets/homepage.jpg');
    background-size: cover;
    background-position: center;
    overflow: hidden;

    .hero-overlay {
      position: absolute;
      inset: 0;
      background: var(--hero-overlay, rgba(0, 0, 0, 0.5));
      z-index: 1;
    }

    .hero-content {
      position: relative;
      z-index: 2;
      max-width: 800px;
      padding: 0 20px;
    }

    .animate-title {
      font-size: clamp(2.5rem, 8vw, 4.5rem);
      font-weight: 800;
      margin-bottom: 1.5rem;
      animation: slideUp 1s var(--transition-timing);
      background: linear-gradient(
        135deg,
        rgba(255, 255, 255, 1) 0%,
        rgba(255, 255, 255, 0.9) 50%,
        rgba(255, 255, 255, 0.8) 100%
      );
      background-clip: text;
      -webkit-background-clip: text;
      color: transparent;
      letter-spacing: 0.02em;
      position: relative;
      transition: transform 0.3s ease; // 添加过渡效果

      &:hover {
        transform: scale(1.02); // 悬停时轻微放大
        cursor: default; // 添加默认光标样式
      }
    }

    .animate-subtitle {
      font-size: clamp(1.2rem, 4vw, 1.5rem);
      margin-bottom: 2rem;
      opacity: 0;
      animation: slideUp 1s var(--transition-timing) 0.2s forwards;
    }

    .hero-actions {
      opacity: 0;
      animation: slideUp 1s var(--transition-timing) 0.4s forwards;

      .action-button {
        padding: 12px 32px;
        font-size: 1.1rem;
        border-radius: 12px;
        margin: 0 8px;
        transition: all var(--animation-duration) var(--transition-timing);
        color: #fff;
        background: rgba(255, 255, 255, 0.2);
        border: none;

        &:hover {
          background: rgba(255, 255, 255, 0.3);
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
        }

        .el-icon {
          margin-right: 8px;
          color: #fff;
        }
      }
    }
  }

  .features-section {
    padding: 100px 0;
    background: var(--bg-primary);

    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 20px;
    }

    .section-title {
      text-align: center;
      font-size: 2.5rem;
      margin-bottom: 60px;
      color: var(--text-primary);
      position: relative;

      &::after {
        content: '';
        position: absolute;
        bottom: -16px;
        left: 50%;
        transform: translateX(-50%);
        width: 60px;
        height: 4px;
        background: var(--el-color-primary);
        border-radius: 2px;
      }
    }

    .features-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 30px;
      max-width: 1000px;
      margin: 0 auto;
    }

    .feature-card {
      padding: 40px;
      border-radius: 20px;
      background: var(--bg-primary);
      transition: all var(--animation-duration) var(--transition-timing);
      text-align: center;

      &:hover {
        transform: translateY(-8px);
        box-shadow: var(--box-shadow);
        background: var(--bg-secondary);
      }

      .icon-wrapper {
        width: 80px;
        height: 80px;
        border-radius: 20px;
        margin: 0 auto 24px;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: transform var(--animation-duration) var(--transition-timing);

        &:hover {
          transform: scale(1.1) rotate(5deg);
        }

        .el-icon {
          color: var(--el-color-primary);
        }
      }

      h3 {
        font-size: 1.5rem;
        margin-bottom: 16px;
        color: var(--text-primary);
      }

      p {
        color: var(--text-secondary);
        line-height: 1.6;
      }
    }
  }

  .theme-toggle {
    position: fixed;
    bottom: 30px;
    right: 30px;
    z-index: 100;

    .theme-button {
      width: 48px;
      height: 48px;
      font-size: 20px;
      backdrop-filter: blur(10px);
      background: var(--bg-primary);
      border: 1px solid var(--border-color);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      transition: all var(--animation-duration) var(--transition-timing);

      &:hover {
        transform: rotate(180deg);
      }

      .el-icon {
        color: var(--text-primary);
      }
    }
  }
}

@keyframes slideUp {
  from {
    transform: translateY(30px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .home {
    .hero-section {
      padding: 60px 20px;

      .hero-content {
        padding: 0 20px;
      }

      .action-button {
        margin: 8px;
      }
    }

    .features-section {
      padding: 60px 20px;

      .features-grid {
        grid-template-columns: 1fr;
      }
    }
  }
}
</style>
