<template>
  <div class="notebook-container">
    <el-card class="notebook-card">
      <div class="notebook-title">收藏</div>
      <el-table :data="words" style="width: 100%">
        <el-table-column prop="word" label="单词">
          <template #default="scope">
            <span class="word-link" @click="handleWordClick(scope.row.word)">{{ scope.row.word }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div v-if="words.length === 0" class="empty-tip">暂无收藏</div>
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
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getnotebook, deletenotebook } from '@/api/notebook'
import { getUserInfo } from '@/api/dailytest'
import { getWordInfo } from '@/api/wordsearch'

const words = ref([])
const userId = ref('')
const username = localStorage.getItem('username') || ''

const detailVisible = ref(false)
const wordDetail = ref(null)

async function fetchNotebook() {
  try {
    const userRes = await getUserInfo(username)
    userId.value = userRes.data?.id || ''
    if (!userId.value) {
      ElMessage.error('未获取到用户信息')
      return
    }
    const res = await getnotebook(userId.value)
    words.value = res.data || []
  } catch {
    ElMessage.error('获取收藏失败')
    words.value = []
  }
}

async function handleDelete(row) {
  try {
    if (!userId.value) {
      ElMessage.error('未获取到用户信息')
      return
    }
    await deletenotebook({ userId: userId.value, wordId: row.id })
    ElMessage.success('删除成功')
    words.value = words.value.filter(word => word.id !== row.id)
  } catch {
    ElMessage.error('删除失败')
  }
}

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

onMounted(fetchNotebook)
</script>

<style scoped>
.notebook-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}
.notebook-card {
  width: 600px;
  padding: 24px;
}
.notebook-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 18px;
  text-align: center;
}
.empty-tip {
  text-align: center;
  color: #aaa;
  margin-top: 20px;
}
.word-link {
  color: #1890ff;
  cursor: pointer;
  font-weight: 500;
  transition: text-decoration 0.2s;
}
.word-link:hover {
  text-decoration: underline;
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