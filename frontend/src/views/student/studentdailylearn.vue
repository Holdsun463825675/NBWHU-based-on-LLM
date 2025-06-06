<template>
  <div class="word-learning">
    <el-card class="word-card">
      <div class="study-title">每日学习</div>
      <template v-if="learnFinished">
        <div class="learn-finished-tip">
          <el-result icon="success" title="完成今日学习" sub-title="恭喜你完成了今日所有单词！" />
        </div>
      </template>
      <template v-else>
        <template v-if="words.length && currentWord">
          <!-- 学习内容 -->
          <div class="word-content">
            <div class="word-main">
              <span class="word">{{ currentWord.word }}</span>
              <span class="phonetic" v-if="currentWord.phonetic">[{{ currentWord.phonetic }}]</span>
            </div>
            <template v-if="showDetail">
              <div class="definition-list">
                <div
                  class="definition"
                  v-for="(def, idx) in currentWord.chineseDefinitions"
                  :key="idx"
                >
                  <span class="meaning">{{ def }}</span>
                </div>
              </div>
              <div class="examples" v-if="currentWord.exampleSentences && currentWord.exampleSentences.length">
                <div class="example-title">例句：</div>
                <ul>
                  <li v-for="(ex, i) in currentWord.exampleSentences" :key="i">{{ ex }}</li>
                </ul>
              </div>
              <div class="synonyms" v-if="currentWord.synonyms && currentWord.synonyms.length">
                <div class="example-title">近义词：</div>
                <ul>
                  <li v-for="(syn, i) in currentWord.synonyms" :key="i">{{ syn }}</li>
                </ul>
              </div>
              <div class="confusions" v-if="currentWord.confusions && currentWord.confusions.length">
                <div class="example-title">易混词：</div>
                <ul>
                  <li v-for="(conf, i) in currentWord.confusions" :key="i">{{ conf }}</li>
                </ul>
              </div>
            </template>
          </div>
          <div class="actions">
            <template v-if="!showDetail">
              <el-button type="success" size="large" @click="handleAction('认识')">
                认识
              </el-button>
              <el-button type="warning" size="large" @click="handleAction('太简单')">
                太简单
              </el-button>
              <el-button type="danger" size="large" @click="handleAction('不认识')">
                不认识
              </el-button>
              <el-button type="info" size="large" @click="addToNotebook">
                添加到收藏
              </el-button>
            </template>
            <template v-else>
              <el-button type="primary" size="large" @click="handleAction('下一个')">
                下一个
              </el-button>
            </template>
          </div>
        </template>
        <template v-else>
          <div class="empty-tip">正在加载今日单词...</div>
        </template>
      </template>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { dailylearn, getNextWord } from '@/api/dailylearn'
import { addnotebook } from '@/api/notebook'
import { ElMessage } from 'element-plus'

const route = useRoute()
const username = localStorage.getItem('username') || ''
let wordsArr = []

try {
  wordsArr = JSON.parse(sessionStorage.getItem('todayWords') || '[]')
} catch {
  wordsArr = []
}

const words = ref(wordsArr)
const currentIndex = ref(0)
const showDetail = ref(false)
const currentWord = computed(() => words.value[currentIndex.value])
const progress = computed(() =>
  words.value.length === 0 ? 0 : Math.round(((currentIndex.value + 1) / words.value.length) * 100)
)

const today = new Date()
const dateStr = `${today.getFullYear()}-${(today.getMonth()+1).toString().padStart(2,'0')}-${today.getDate().toString().padStart(2,'0')}`

const loading = ref(false)
const learnFinished = ref(false) // 新增

// 如果没有 todayWords，则自动请求
if (!words.value.length && username) {
  loading.value = true
  dailylearn({ username }).then(res => {
    if (res.code === 1 && Array.isArray(res.data)) {
      words.value = res.data
      sessionStorage.setItem('todayWords', JSON.stringify(res.data))
    } else {
      ElMessage.error('获取学习单词失败')
    }
    loading.value = false
  }).catch(() => {
    ElMessage.error('获取学习单词失败')
    loading.value = false
  })
}

async function handleAction(action) {
  try {
    const res = await getNextWord({
      username,
      word: currentWord.value.word,
      date: dateStr,
      action
    })
    if (action === '下一个') {
      if (res.code === 1 && res.data && typeof res.data === 'object') {
        words.value[currentIndex.value] = res.data
        showDetail.value = false
      } else if (res.code === 1 &&  res.data === null) {
        learnFinished.value = true // 标记完成
      }
      else if (res.code === 1 && typeof res.data === 'string') {
        ElMessage.success(res.data)
      }
    } else {
      showDetail.value = true
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

// 添加到收藏
async function addToNotebook() {
  try {
    // 获取 userId
    const userRes = await import('@/api/dailytest').then(m => m.getUserInfo(username))
    const userId = userRes.data?.id
    if (!userId) {
      ElMessage.error('未获取到用户信息')
      return
    }
    await addnotebook({ userId, wordId: currentWord.value.id })
    ElMessage.success('已添加到收藏')
  } catch {
    ElMessage.error('添加到收藏失败')
  }
}

function nextWord() {
  if (currentIndex.value < words.value.length - 1) {
    currentIndex.value++
    showDetail.value = false
  }
}
</script>

<style lang="scss" scoped>
.word-learning {
  min-height: 100vh;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  background: #f5f7fa;

  .word-card {
    width: 90vw;
    max-width: 1480px;
    height: auto;
    min-height: 500px;
    padding: 4vw 2vw 3vw 2vw;
    border-radius: 18px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.08);

    .study-title {
      text-align: center;
      font-size: 32px;
      font-weight: 700;
      color: #1890ff;
      margin-bottom: 36px;
      letter-spacing: 4px;
    }

    .learn-finished-tip {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 400px;
    }

    .progress-bar {
      margin-bottom: 32px;
    }

    .word-content {
      text-align: center;
      margin-bottom: 32px;

      .word-main {
        font-size: 38px;
        font-weight: 700;
        color: #1890ff;
        margin-bottom: 12px;
        display: flex;
        justify-content: center;
        align-items: baseline;
        gap: 18px;

        .phonetic {
          font-size: 20px;
          color: #888;
          font-weight: 400;
        }
      }

      .definition-list {
        .definition {
          font-size: 28px;
          margin: 8px 0;
          color: #333;
          .pos {
            color: #1890ff;
            margin-right: 8px;
            font-weight: 500;
          }
        }
      }

      .examples,
      .synonyms,
      .confusions {
        margin-top: 24px;
        .example-title {
          font-size: 28px;
          color: #666;
          margin-bottom: 8px;
          text-align: center;
        }
        ul {
          padding-left: 0;
          text-align: center;
          list-style-position: inside;
          li {
            font-size: 28px;
            color: #444;
            margin-bottom: 4px;
            text-align: center;
          }
        }
      }
    }

    .actions {
      display: flex;
      justify-content: center;
      gap: 32px;
      margin-top: 32px;
      .el-button {
        min-width: 110px;
      }
    }

    .empty-tip {
      text-align: center;
      font-size: 28px;
      color: #999;
      margin-top: 100px;
    }
  }
}
</style>