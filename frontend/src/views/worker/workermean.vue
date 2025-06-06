<template>
  <div class="semantic-word-exploration">
    <el-card class="mean-card">
      <div class="mean-title">语义探词</div>
      <el-input
        v-model="input"
        placeholder="请输入要探查的单词或短语，多个请用空格分隔"
        @keyup.enter="handleSearch"
        clearable
        style="width: 400px; margin-bottom: 16px;"
      >
        <template #append>
          <el-button type="primary" @click="handleSearch" :loading="loading">探词</el-button>
        </template>
      </el-input>
      <div v-if="resultList.length > 0" class="result-list">
        <el-card
          v-for="item in resultList"
          :key="item.word"
          class="result-item"
          :class="item.type"
          @click="handleWordClick(item.word)"
          style="cursor:pointer"
        >
          <div class="word-header">
            <span class="word">{{ item.word }}</span>
            <span v-if="item.type === 'word'" class="type-tag">词库词</span>
            <span v-else-if="item.type === 'synonym'" class="type-tag synonym">同义/易混</span>
            <span v-else class="type-tag not-found">未收录</span>
          </div>
          <div v-if="item.info">
            <div v-if="item.info.phonetic" class="phonetic">[{{ item.info.phonetic }}]</div>
            <div v-if="item.info.chineseDefinition1" class="definition">
              {{ item.info.chineseDefinition1 }}
            </div>
            <div v-if="item.info.chineseDefinition2">{{ item.info.chineseDefinition2 }}</div>
            <div v-if="item.info.chineseDefinition3">{{ item.info.chineseDefinition3 }}</div>
            <div v-if="item.info.chineseDefinition4">{{ item.info.chineseDefinition4 }}</div>
            <div v-if="item.info.chineseDefinition5">{{ item.info.chineseDefinition5 }}</div>
            <div v-if="item.info.exampleSentence1" class="example">
              例句：{{ item.info.exampleSentence1 }}
            </div>
          </div>
          <div v-else-if="item.type === 'not_found'" class="not-found-tip">
            未收录该词
          </div>
        </el-card>
      </div>
      <div v-else-if="searched" class="empty-tip">未查询到相关结果</div>
    </el-card>

    <!-- 单词详细信息弹窗 -->
    <el-dialog v-model="detailVisible" title="单词详细信息" width="420px" :close-on-click-modal="false" class="word-detail-dialog">
      <div v-if="wordDetail" class="word-detail-content">
        <div class="detail-header">
          <span class="detail-word">{{ wordDetail.word }}</span>
          <span v-if="wordDetail.phonetic" class="detail-phonetic">[{{ wordDetail.phonetic }}]</span>
          <span v-if="wordDetail.difficultyLevel" class="detail-level">{{ wordDetail.difficultyLevel }}</span>
        </div>
        <div v-if="wordDetail.tag1 || wordDetail.tag2 || wordDetail.tag3" class="detail-tags">
          <el-tag v-if="wordDetail.tag1" size="small" effect="plain" type="info">{{ wordDetail.tag1 }}</el-tag>
          <el-tag v-if="wordDetail.tag2" size="small" effect="plain" type="success">{{ wordDetail.tag2 }}</el-tag>
          <el-tag v-if="wordDetail.tag3" size="small" effect="plain" type="warning">{{ wordDetail.tag3 }}</el-tag>
        </div>
        <el-divider />
        <div v-if="wordDetail.chineseDefinition1" class="detail-def"><b>释义：</b>{{ wordDetail.chineseDefinition1 }}</div>
        <div v-if="wordDetail.chineseDefinition2" class="detail-def">{{ wordDetail.chineseDefinition2 }}</div>
        <div v-if="wordDetail.chineseDefinition3" class="detail-def">{{ wordDetail.chineseDefinition3 }}</div>
        <div v-if="wordDetail.chineseDefinition4" class="detail-def">{{ wordDetail.chineseDefinition4 }}</div>
        <div v-if="wordDetail.chineseDefinition5" class="detail-def">{{ wordDetail.chineseDefinition5 }}</div>
        <el-divider v-if="wordDetail.exampleSentence1"/>
        <div v-if="wordDetail.exampleSentence1" class="detail-example">
          <b>例句：</b>{{ wordDetail.exampleSentence1 }}
        </div>
      </div>
      <div v-else class="no-detail">暂无详细信息</div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { semanticwordexploration } from '@/api/llm'
import { exam, getWordInfo } from '@/api/wordsearch'

const input = ref('')
const loading = ref(false)
const resultList = ref([])
const searched = ref(false)

const detailVisible = ref(false)
const wordDetail = ref(null)

async function handleSearch() {
  const query = input.value.trim()
  if (!query) {
    ElMessage.warning('请输入要探查的单词')
    return
  }
  loading.value = true
  searched.value = false
  resultList.value = []
  try {
    const user = localStorage.getItem('username')
    const res = await semanticwordexploration({
      inputs: {
        query_text: query,
        word_count: '20'
      },
      response_mode: 'blocking',
      user
    })
    let words = []
    if (typeof res.answer === 'string') {
      words = res.answer.split('\n').map(w => w.trim()).filter(Boolean)
    } else if (Array.isArray(res.answer)) {
      words = res.answer
    } else {
      words = query.split(/\s+/)
    }
    const examRes = await exam(words.join('\n'))
    if (examRes.code === 1 && Array.isArray(examRes.data)) {
      resultList.value = examRes.data
    } else {
      resultList.value = []
    }
    searched.value = true
  } catch (e) {
    ElMessage.error('查询失败')
    resultList.value = []
    searched.value = true
  }
  loading.value = false
}

// 点击单词卡片获取详细信息
async function handleWordClick(word) {
  try {
    const res = await getWordInfo(word)
    if (res.code === 1 && res.data) {
      wordDetail.value = res.data
    } else {
      wordDetail.value = null
    }
    detailVisible.value = true
  } catch {
    ElMessage.error('获取单词详细信息失败')
    wordDetail.value = null
    detailVisible.value = true
  }
}
</script>

<style scoped>
.semantic-word-exploration {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}
.mean-card {
  width: 600px;
  padding: 24px;
}
.mean-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 18px;
  text-align: center;
}
.result-list {
  margin-top: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.result-item {
  border-left: 4px solid #1890ff;
}
.result-item.synonym {
  border-left: 4px solid #e6a23c;
}
.result-item.not_found {
  border-left: 4px solid #f56c6c;
}
.word-header {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 6px;
  display: flex;
  align-items: center;
  gap: 12px;
}
.type-tag {
  font-size: 13px;
  padding: 2px 8px;
  border-radius: 8px;
  background: #e6f7ff;
  color: #1890ff;
}
.type-tag.synonym {
  background: #fff7e6;
  color: #e6a23c;
}
.type-tag.not-found {
  background: #fff1f0;
  color: #f56c6c;
}
.phonetic {
  color: #888;
  margin-bottom: 4px;
}
.definition {
  margin-bottom: 4px;
}
.example {
  color: #67c23a;
  margin-top: 4px;
}
.not-found-tip {
  color: #f56c6c;
}
.empty-tip {
  text-align: center;
  color: #aaa;
  margin-top: 20px;
}
.word-detail-dialog .el-dialog__body {
  padding: 18px 24px 24px 24px;
}
.word-detail-content {
  font-size: 16px;
  color: #222;
}
.detail-header {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 6px;
}
.detail-word {
  font-size: 22px;
  font-weight: bold;
  color: #1890ff;
}
.detail-phonetic {
  font-size: 16px;
  color: #888;
}
.detail-level {
  font-size: 14px;
  color: #fff;
  background: #52c41a;
  border-radius: 6px;
  padding: 2px 8px;
  margin-left: 8px;
}
.detail-tags {
  margin-bottom: 8px;
  display: flex;
  gap: 8px;
}
.detail-def {
  margin-bottom: 2px;
  color: #444;
}
.detail-example {
  color: #67c23a;
  margin-top: 6px;
  font-size: 15px;
}
.no-detail {
  color: #aaa;
  text-align: center;
  font-size: 15px;
  padding: 24px 0;
}
</style>