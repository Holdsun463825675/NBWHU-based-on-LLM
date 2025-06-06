<template>
  <div class="not-found">
    <div class="content">
      <div class="error-code">404</div>
      <h1>页面不见了</h1>
      <p>抱歉，您访问的页面不存在或已被移除</p>
      <div class="actions">
        <el-button type="primary" size="large" @click="goBack">
          <el-icon><Back /></el-icon>返回上一页
        </el-button>
        <el-button size="large" @click="goHome">
          <el-icon><House /></el-icon>返回首页
        </el-button>
      </div>
      <div class="suggestions">
        <h3>您可以：</h3>
        <ul>
          <li>
            <el-link type="primary" @click="goHome">浏览首页</el-link>
          </li>
          <li>
            <el-link type="primary" @click="navigateTo('/login')">登录您的账号</el-link>
          </li>
        </ul>
      </div>
    </div>
    <div class="illustration">
      <img src="@/assets/404.svg" alt="404 Illustration" />
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Back, House } from '@element-plus/icons-vue'

const router = useRouter()

const goBack = () => {
  router.back()
}

const goHome = () => {
  router.push('/')
}

const navigateTo = (path) => {
  router.push(path)
}
</script>

<style lang="scss" scoped>
.not-found {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  background: linear-gradient(135deg, var(--bg-primary), var(--bg-secondary));

  .content {
    text-align: center;
    margin-right: 60px;

    .error-code {
      font-size: 120px;
      font-weight: bold;
      color: var(--primary-color);
      line-height: 1;
      margin-bottom: 24px;
      text-shadow: 4px 4px 0 var(--primary-light);
    }

    h1 {
      font-size: 36px;
      color: var(--text-primary);
      margin: 0 0 16px;
    }

    p {
      font-size: 18px;
      color: var(--text-secondary);
      margin: 0 0 32px;
    }

    .actions {
      display: flex;
      gap: 16px;
      justify-content: center;
      margin-bottom: 48px;

      .el-button {
        min-width: 160px;

        .el-icon {
          margin-right: 8px;
        }
      }
    }

    .suggestions {
      text-align: left;
      max-width: 400px;
      margin: 0 auto;

      h3 {
        color: var(--text-primary);
        margin: 0 0 16px;
      }

      ul {
        list-style: none;
        padding: 0;
        margin: 0;

        li {
          margin-bottom: 12px;

          &:last-child {
            margin-bottom: 0;
          }

          .el-link {
            font-size: 16px;
          }
        }
      }
    }
  }

  .illustration {
    img {
      max-width: 400px;
      height: auto;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .not-found {
    flex-direction: column-reverse;
    text-align: center;
    padding: 20px;

    .content {
      margin-right: 0;
      margin-top: 40px;

      .error-code {
        font-size: 80px;
      }

      h1 {
        font-size: 24px;
      }

      p {
        font-size: 16px;
      }

      .actions {
        flex-direction: column;

        .el-button {
          width: 100%;
        }
      }

      .suggestions {
        text-align: center;

        ul {
          display: flex;
          flex-direction: column;
          align-items: center;
        }
      }
    }

    .illustration {
      img {
        max-width: 280px;
      }
    }
  }
}
</style> 