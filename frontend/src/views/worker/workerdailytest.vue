<template>
  <div class="daily-quiz">
    <el-card class="quiz-card">
      <div class="quiz-title">每日测试</div>
      <div v-if="showStart" class="start-btn-wrap">
        <el-button type="primary" size="large" @click="startTest">开始测试</el-button>
      </div>
      <template v-else>
        <div v-if="testFinished" class="test-finished-tip">
          <el-result icon="success" title="测试已完成" sub-title="恭喜你完成了本次测试！" />
        </div>
        <div v-else>
          <div class="progress-bar">
            <el-progress
              :percentage="progress"
              :text-inside="true"
              :stroke-width="22"
              status="success"
            >
              <span>{{ currentIndex + 1 }} / {{ words.length }}</span>
            </el-progress>
          </div>
          <div class="quiz-content">
            <div class="quiz-word">
              <span class="word">{{ currentWord.word }}</span>
              <span class="phonetic" v-if="currentWord.phonetic">[{{ currentWord.phonetic }}]</span>
            </div>
            <template v-if="!showDetail">
              <div class="options">
                <el-button
                  v-for="(opt, idx) in currentOptions"
                  :key="idx"
                  class="option-btn"
                  :type="selectedIndex === idx ? (isCorrect(idx) ? 'success' : 'danger') : 'default'"
                  :disabled="selectedIndex !== null"
                  @click="handleSelect(idx)"
                  size="large"
                >
                  {{ opt }}
                </el-button>
              </div>
            </template>
            <template v-else>
              <div class="definition-list">
                <div
                  class="definition"
                  v-for="(def, idx) in currentWord.definitions"
                  :key="idx"
                >
                  <span class="pos">{{ def.pos }}</span>
                  <span class="meaning">{{ def.meaning }}</span>
                </div>
              </div>
              <div class="examples" v-if="currentWord.examples && currentWord.examples.length">
                <div class="example-title">例句：</div>
                <ul>
                  <li v-for="(ex, i) in currentWord.examples" :key="i">{{ ex }}</li>
                </ul>
              </div>
            </template>
          </div>
          <div class="actions">
            <el-button
              v-if="showDetail || selectedIndex === correctIndex"
              type="primary"
              size="large"
              @click="nextWord"
            >
              下一个
            </el-button>
          </div>
        </div>
      </template>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onBeforeUnmount } from 'vue'
import { dailytest, testreport, getUserInfo } from '@/api/dailytest'
import { addnotebook } from '@/api/notebook'

const showStart = ref(true)
const rawWords = ref([])
const words = ref([])
const currentIndex = ref(0)
const showDetail = ref(false)
const selectedIndex = ref(null)
const correctIndex = ref(0)
const currentOptions = ref([])
const testResults = ref([])
const testFinished = ref(false) // 新增

const username = localStorage.getItem('username') || ''

async function startTest() {
  showStart.value = false
  // 1. 先获取 userId
  let userId = ''
  try {
    const userRes = await getUserInfo(username)
    if (userRes.code === 1 && userRes.data && userRes.data.id) {
      userId = userRes.data.id
    } else {
      throw new Error('获取用户信息失败')
    }
  } catch {
    showStart.value = true
    return
  }
  // 2. 再用 userId 获取测试数据
  const res = await dailytest(userId)
  if (res.code === 1 && Array.isArray(res.data)) {
    rawWords.value = res.data
    words.value = res.data.map(item => {
      const w = item.word
      const options = [
        w.chineseDefinition,
        w.confusion1Chinese,
        w.confusion2Chinese,
        w.confusion3Chinese
      ]
      return {
        wordId: w.id,
        word: w.word,
        phonetic: w.phonetic,
        options,
        correct: w.chineseDefinition,
        definitions: [{ pos: '', meaning: w.chineseDefinition }],
        examples: w.exampleSentences || []
      }
    })
    currentIndex.value = 0
    showDetail.value = false
    selectedIndex.value = null
    testResults.value = []
    shuffleCurrentOptions()
  }
}

// 乱序选项并记录正确答案下标
function shuffleCurrentOptions() {
  const options = words.value[currentIndex.value].options
  const arr = options.map((opt, idx) => ({ opt, idx }))
  for (let i = arr.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1))
    ;[arr[i], arr[j]] = [arr[j], arr[i]]
  }
  correctIndex.value = arr.findIndex(item => item.idx === 0)
  currentOptions.value = arr.map(item => item.opt)
}

async function handleSelect(idx) {
  selectedIndex.value = idx
  // 记录本题结果
  const word = words.value[currentIndex.value]
  testResults.value.push({
    wordId: word.wordId,
    testType: '看英选中',
    status: idx === correctIndex.value ? '正确' : '错误'
  })
  if (idx === correctIndex.value) {
    showDetail.value = false
  } else {
    showDetail.value = true
    // 选错时加入错题本
    try {
      const userRes = await getUserInfo(username)
      const userId = userRes.data?.id
      if (userId) {
        await addnotebook({ userId, wordId: word.wordId })
      }
    } catch {
      // 可选：ElMessage.error('加入错题本失败')
    }
  }
}

function isCorrect(idx) {
  return idx === correctIndex.value
}

function nextWord() {
  if (currentIndex.value < words.value.length - 1) {
    currentIndex.value++
    showDetail.value = false
    selectedIndex.value = null
    shuffleCurrentOptions()
  } else {
    testFinished.value = true // 到最后一题时标记完成
  }
}

// 上报测试结果
async function reportTest() {
  if (!testResults.value.length) return
  await testreport({
    userId,
    wordTests: testResults.value,
    testDate: new Date().toISOString()
  })
}

// 页面卸载时自动上报
onBeforeUnmount(() => {
  reportTest()
})

const currentWord = computed(() => words.value[currentIndex.value] || {})
const progress = computed(() =>
  words.value.length === 0 ? 0 : Math.round(((currentIndex.value + 1) / words.value.length) * 100)
)
</script>

<style lang="scss" scoped>
.daily-quiz {
  min-height: 100vh;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  background: #f5f7fa;

  .quiz-card {
    width: 1480px;
    min-height: 800px;
    padding: 32px 36px 28px 36px;
    border-radius: 18px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.08);

    .quiz-title {
      text-align: center;
      font-size: 32px;
      font-weight: 700;
      color: #1890ff;
      margin-bottom: 36px;
      letter-spacing: 4px;
    }

    .progress-bar {
      margin-bottom: 32px;
    }

    .quiz-content {
      text-align: center;
      margin-bottom: 32px;

      .quiz-word {
        font-size: 38px;
        font-weight: 700;
        color: #1890ff;
        margin-bottom: 24px;
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

      .options {
        display: flex;
        flex-direction: column;
        gap: 18px;
        align-items: center;
        .option-btn {
          width: 80%;
          min-width: 220px;
          max-width: 400px;
          font-size: 18px;
        }
      }

      .definition-list {
        margin-top: 24px;
        .definition {
          font-size: 18px;
          margin: 8px 0;
          color: #333;
          .pos {
            color: #1890ff;
            margin-right: 8px;
            font-weight: 500;
          }
        }
      }

      .examples {
        margin-top: 24px;
        .example-title {
          font-size: 17px;
          color: #666;
          margin-bottom: 8px;
          text-align: center;
        }
        ul {
          padding-left: 0;
          text-align: center;
          list-style-position: inside;
          li {
            font-size: 16px;
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
      margin-top: 32px;
    }
  }
}

.start-btn-wrap {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.test-finished-tip {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}
</style>