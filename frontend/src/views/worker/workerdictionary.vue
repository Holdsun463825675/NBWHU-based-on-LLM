<template>
  <div class="select-dictionary">
    <el-card class="dict-card">
      <template #header>
        <div class="dict-header">
          <h3>选择词典</h3>
        </div>
      </template>
      <div class="dict-list">
        <div
          v-for="dict in dictionaries"
          :key="dict.value"
          :class="['dict-item', form.selected === dict.value ? 'selected' : '']"
          @click="form.selected = dict.value"
        >
          <img :src="dict.cover" class="dict-cover" alt="词典封面" />
          <div class="dict-label">{{ dict.label }}</div>
        </div>
      </div>
      <el-button type="primary" @click="handleConfirm" style="margin-top: 32px;">
        确认选择
      </el-button>
    </el-card>
    <!-- 弹窗输入每天学习单词数 -->
    <el-dialog v-model="showDialog" title="设置每日学习单词数" width="350px" :close-on-click-modal="false">
      <div style="margin-bottom: 16px;">
        <el-input-number v-model="plannedCount" :min="1" :max="200" label="每日单词数" style="width: 100%;" />
      </div>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="handleDialogOk">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { dailylearnfirst } from '@/api/dailylearn'
import { useRouter } from 'vue-router'

const router = useRouter()

const dictionaries = [
  { label: '高考', value: '高考', cover: new URL('@/assets/Anya.jpg', import.meta.url).href },
  { label: '中考', value: '中考', cover: new URL('@/assets/Anya.jpg', import.meta.url).href },
  { label: '雅思', value: '雅思', cover: new URL('@/assets/Anya.jpg', import.meta.url).href },
  { label: '托福', value: '托福', cover: new URL('@/assets/Anya.jpg', import.meta.url).href },
  { label: '四级', value: '四级', cover: new URL('@/assets/Anya.jpg', import.meta.url).href },
  { label: '六级', value: '六级', cover: new URL('@/assets/Anya.jpg', import.meta.url).href }
]

const form = ref({
  selected: ''
})

const showDialog = ref(false)
const plannedCount = ref(50)

const handleConfirm = () => {
  if (!form.value.selected) {
    ElMessage.warning('请选择一个词典')
    return
  }
  showDialog.value = true
}

const handleDialogOk = async () => {
  const username = localStorage.getItem('username') || ''
  if (!plannedCount.value || plannedCount.value < 1) {
    ElMessage.warning('请输入每天要学习的单词数')
    return
  }
  showDialog.value = false
  try {
    const res = await dailylearnfirst({
      wordBook: form.value.selected,
      plannedCount: plannedCount.value,
      username
    })
    if (res.code === 1 && Array.isArray(res.data)) {
      sessionStorage.setItem('todayWords', JSON.stringify(res.data))
      router.push('/student/learn')
    } else {
      ElMessage.error('获取学习单词失败')
    }
  } catch (e) {
    ElMessage.error('获取学习单词失败')
  }
}
</script>

<style lang="scss" scoped>
.select-dictionary {
  min-height: 100vh;
  width: 100vw;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  background: #f5f7fa;
  padding: 4vw 0 0 0;

  .dict-card {
    border-radius: 18px;
    box-shadow: 0 1px 4px rgba(0,0,0,0.06);
    background: #fff;
    width: 90vw;
    max-width: 1200px;
    min-width: 320px;
    min-height: 60vh;
    display: flex;
    flex-direction: column;
    padding: 4vw 3vw 4vw 3vw;

    .dict-header {
      display: flex;
      align-items: flex-start;
      h3 {
        margin: 0 0 24px 0;
        font-size: clamp(20px, 3vw, 26px);
        font-weight: 700;
        color: #1890ff;
      }
    }
    .dict-list {
      flex: 1;
      display: flex;
      flex-wrap: wrap;
      gap: 3vw;
      justify-content: flex-start;
      align-items: flex-start;
      margin: 0 0 32px 0;
      .dict-item {
        width: clamp(120px, 18vw, 200px);
        height: clamp(180px, 27vw, 300px);
        cursor: pointer;
        border-radius: 16px;
        background: #f7faff;
        box-shadow: 0 1px 4px rgba(0,0,0,0.04);
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 2vw 1vw 1vw 1vw;
        border: 2px solid transparent;
        transition: border 0.2s, box-shadow 0.2s, background 0.2s;
        .dict-cover {
          width: clamp(80px, 12vw, 150px);
          height: clamp(100px, 16vw, 200px);
          object-fit: cover;
          border-radius: 10px;
          margin-bottom: 18px;
          background: #fff;
        }
        .dict-label {
          font-size: clamp(14px, 2vw, 17px);
          font-weight: 600;
          color: #333;
          text-align: center;
        }
        &:hover {
          border-color: #91caff;
        }
        &.selected {
          border-color: #1890ff;
          box-shadow: 0 2px 12px rgba(24,144,255,0.14);
          background: #e6f7ff;
        }
      }
    }
  }
}

// 响应式优化
@media (max-width: 700px) {
  .dict-card {
    padding: 2vw 1vw;
    min-height: 400px;
  }
  .dict-list {
    gap: 2vw !important;
  }
}

</style>