<template>
  <div class="profile-home">
    <!-- 个人信息卡片 -->
    <div class="profile-card">
      <img class="avatar" :src="user.avatar" alt="avatar" />
      <div class="info">
        <h2>{{ user.username }}</h2>
        <div class="role">{{ user.role }}</div>
        <div class="email">{{ user.email }}</div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="24" class="statistics-cards">
      <el-col :xs="24" :sm="12" :md="6" v-for="card in statisticsCards" :key="card.title">
        <div class="stat-card" :class="card.type">
          <div class="card-icon">
            <el-icon><component :is="card.icon" /></el-icon>
          </div>
          <div class="card-content">
            <h3>{{ card.title }}</h3>
            <div class="number">{{ card.number }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 日历活动 -->
    <div class="activities-section">
      <div class="section-header">
        <h3>学习活动日历</h3>
      </div>
      <el-calendar v-model="calendarDate">
        <template #date-cell="{ data }">
          <div
            :class="['calendar-cell', isActiveDay(data.day) ? 'active' : '']"
          >
            <span>{{ data.day.split('-')[2] }}</span>
            <div v-if="isActiveDay(data.day)" class="dot"></div>
          </div>
        </template>
      </el-calendar>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { User, Star, Reading, CollectionTag } from '@element-plus/icons-vue'
import { getUserInfo } from '@/api/dailytest'
import { learnedword } from '@/api/auth' // 引入learnedword

// 用户信息
const user = ref({
  avatar: 'https://api.dicebear.com/7.x/bottts/svg?seed=User',
  username: '',
  identity: '',
  email: ''
})

const username = localStorage.getItem('username') || ''
const today = new Date()
const todayStr = `${today.getFullYear()}-${(today.getMonth()+1).toString().padStart(2,'0')}-${today.getDate().toString().padStart(2,'0')}`

// mock 统计数据
const statisticsCards = ref([
  {
    title: '学习天数',
    number: 23,
    icon: Reading,
    type: 'primary',
  },
  {
    title: '今日学习词数',
    number: 0, // 初始为0，后面动态赋值
    icon: Star,
    type: 'success',
  }
])

onMounted(async () => {
  if (username) {
    const res = await getUserInfo(username)
    if (res.code === 1 && res.data) {
      user.value = {
        avatar: res.data.avatar || user.value.avatar,
        username: res.data.username || '',
        role: res.data.identity || '',
        email: res.data.email || '',
      }
      // 获取今日学习词数
      try {
        const learnedRes = await learnedword(res.data.id, todayStr)
        if (learnedRes.code === 1 && typeof learnedRes.data === 'number') {
          // 找到“今日学习词数”卡片并赋值
          const card = statisticsCards.value.find(c => c.title === '今日学习词数')
          if (card) card.number = learnedRes.data
        }
      } catch {
        // 可选：错误处理
      }
    }
  }
})

// mock 活动日期（YYYY-MM-DD）
const activityDays = [
  '2025-05-18',
  '2025-05-16',
  '2025-05-10',
  '2025-05-09',
  '2025-06-05',
]
const calendarDate = ref(new Date())

// 判断某天是否有活动
function isActiveDay(day) {
  return activityDays.includes(day)
}
</script>

<style lang="scss" scoped>
.profile-home {
  padding: 24px;
  max-width: 900px;
  margin: 0 auto;

  .profile-card {
    display: flex;
    align-items: center;
    background: #fff;
    border-radius: 20px;
    padding: 32px 24px;
    box-shadow: 0 1px 4px rgba(0,0,0,0.06);
    margin-bottom: 32px;

    .avatar {
      width: 96px;
      height: 96px;
      border-radius: 50%;
      margin-right: 32px;
      border: 2px solid #eee;
      object-fit: cover;
    }
    .info {
      h2 {
        margin: 0 0 8px 0;
        font-size: 28px;
        font-weight: 600;
      }
      .role {
        color: #1890ff;
        font-size: 16px;
        margin-bottom: 8px;
      }
      .email, .register-time {
        color: #888;
        font-size: 14px;
        margin-bottom: 4px;
      }
    }
  }

  .statistics-cards {
    margin-bottom: 32px;

    .stat-card {
      background: #fff;
      border-radius: 16px;
      padding: 24px;
      height: 120px;
      display: flex;
      align-items: center;
      box-shadow: 0 1px 2px rgba(0,0,0,0.04);
      margin-bottom: 16px;

      .card-icon {
        font-size: 40px;
        margin-right: 20px;
        opacity: 0.18;
      }
      .card-content {
        h3 {
          font-size: 16px;
          margin: 0 0 8px 0;
        }
        .number {
          font-size: 28px;
          font-weight: 600;
        }
      }
      &.primary { border-left: 4px solid #1890ff; }
      &.success { border-left: 4px solid #52c41a; }
    }
  }

  .activities-section {
    background: #fff;
    border-radius: 20px;
    padding: 24px;
    box-shadow: 0 1px 2px rgba(0,0,0,0.04);

    .section-header {
      margin-bottom: 24px;
      h3 {
        font-size: 20px;
        margin: 0;
        font-weight: 600;
      }
    }
    .calendar-cell {
      position: relative;
      text-align: center;
      padding: 4px 0;
      .dot {
        width: 6px;
        height: 6px;
        background: #1890ff;
        border-radius: 50%;
        margin: 2px auto 0;
      }
      &.active {
        color: #1890ff;
        font-weight: bold;
      }
    }
  }
}
</style>
