<template>
  <div class="context-reading">
    <el-card class="reading-card">
      <div class="reading-title">语境阅读</div>
      <div style="text-align:center;margin-bottom:24px;">
        <el-button
          v-if="showButton"
          type="primary"
          size="large"
          :loading="loading"
          @click="handleStartReading"
        >
          开始语境阅读
        </el-button>
      </div>
      <div class="reading-content" v-if="loading">
        <el-skeleton rows="5" animated />
      </div>
      <div class="reading-content" v-else>
        <h3 class="article-title">{{ article.title }}</h3>
        <div class="article-body" v-html="article.content"></div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { contextualreading } from '@/api/llm'
import { ElMessage } from 'element-plus'

const article = ref({ title: '', content: '' })
const loading = ref(false)
const username = localStorage.getItem('username') || ''
const showButton = ref(true) // 新增

function getTodayWords() {
  try {
    const arr = JSON.parse(sessionStorage.getItem('todayWords') || '[]')
    return arr.map(w => w.word).join(', ')
  } catch {
    return ''
  }
}

async function fetchArticle() {
  const wordList = getTodayWords()
  const wordArr = wordList ? wordList.split(',').map(w => w.trim()).filter(Boolean) : []
  const wordCount = wordArr.length

  if (!wordCount) {
    ElMessage.error('未获取到今日学习单词')
    return { title: '', content: '' }
  }

  const payload = {
    inputs: {
      word_list: wordList,
      word_count: wordCount,
      minimum_inclusion_percentage: 20
    },
    response_mode: 'blocking',
    user: username
  }

  try {
    const res = await contextualreading(payload)
    // 只显示 answer 字段
    if (res && res.answer) {
      return { title: '', content: res.answer }
    } else {
      ElMessage.error('获取阅读内容失败')
      return { title: '', content: '' }
    }
  } catch (e) {
    ElMessage.error('获取阅读内容失败')
    return { title: '', content: '' }
  }
}

async function handleStartReading() {
  loading.value = true
  article.value = await fetchArticle()
  loading.value = false
  showButton.value = false // 新增
}
</script>

<style lang="scss" scoped>
.context-reading {
  min-height: 100vh;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  background: #f5f7fa;

  .reading-card {
    width: 1480px;
    min-height: 800px;
    padding: 32px 36px 28px 36px;
    border-radius: 18px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.08);

    .reading-title {
      text-align: center;
      font-size: 32px;
      font-weight: 700;
      color: #1890ff;
      margin-bottom: 36px;
      letter-spacing: 4px;
    }

    .article-title {
      font-size: 22px;
      font-weight: 600;
      color: #222;
      margin-bottom: 18px;
      text-align: center;
    }

    .article-body {
      font-size: 18px;
      color: #333;
      line-height: 2;
      text-align: left;
      p {
        margin-bottom: 16px;
      }
    }
  }
}
</style>