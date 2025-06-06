<template>
  <div class="word-search">
    <el-card class="search-card">
      <div class="search-title">单词查询</div>
      <el-form :inline="true" @submit.prevent>
        <el-form-item>
          <el-input
            v-model="query"
            placeholder="请输入要查询的英文单词"
            clearable
            @keyup.enter="handleSearch"
            style="width: 320px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :loading="loading">查询</el-button>
        </el-form-item>
      </el-form>

      <!-- 单词列表 -->
      <div v-if="wordList.length" class="word-list">
        <div class="list-title">查询结果：</div>
        <el-table
          :data="pagedWords"
          style="width: 100%; margin-bottom: 24px;"
          @row-click="handleSelectWord"
          v-loading="listLoading"
          highlight-current-row
        >
          <el-table-column prop="word" label="单词" width="180" />
          <el-table-column prop="phonetic" label="音标" width="180" />
        </el-table>
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="wordList.length"
          layout="prev, pager, next"
          style="margin-top: 16px; text-align: center;"
        />
        <div style="color:#888;font-size:14px;">点击单词查看详细释义</div>
      </div>

      <!-- 单词详情 -->
      <div class="result-area" v-if="result">
        <div class="word-main">
          <span class="word">{{ result.word }}</span>
          <span class="phonetic" v-if="result.phonetic">[{{ result.phonetic }}]</span>
        </div>
        <div class="definition-list" v-if="result.chineseDefinitions && result.chineseDefinitions.length">
          <div
            class="definition"
            v-for="(def, idx) in result.chineseDefinitions"
            :key="idx"
          >
            <span class="meaning">{{ def }}</span>
          </div>
        </div>
        <div class="examples" v-if="result.exampleSentences && result.exampleSentences.length">
          <div class="example-title">例句：</div>
          <ul>
            <li v-for="(ex, i) in result.exampleSentences" :key="i">{{ ex }}</li>
          </ul>
        </div>
        <div class="synonyms" v-if="result.synonyms && result.synonyms.length">
          <div class="example-title">近义词：</div>
          <ul>
            <li v-for="(syn, i) in result.synonyms" :key="i">{{ syn }}</li>
          </ul>
        </div>
        <div class="confusions" v-if="result.confusions && result.confusions.length">
          <div class="example-title">易混词：</div>
          <ul>
            <li v-for="(conf, i) in result.confusions" :key="i">{{ conf }}</li>
          </ul>
        </div>
      </div>
      <div class="no-result" v-else-if="searched && !wordList.length">未找到该单词</div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { searchWords, getWordDetail } from '@/api/wordsearch'

const query = ref('')
const wordList = ref([])
const result = ref(null)
const searched = ref(false)
const loading = ref(false)
const listLoading = ref(false)

const currentPage = ref(1)
const pageSize = ref(10) // 每页显示10条

const pagedWords = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return wordList.value.slice(start, start + pageSize.value)
})

async function handleSearch() {
  loading.value = true
  const q = query.value.trim()
  if (!q) {
    wordList.value = []
    result.value = null
    searched.value = false
    loading.value = false
    return
  }
  try {
    const response = await searchWords(q, 2, 50)
    wordList.value = (response.data && response.data.words) ? response.data.words : []
    currentPage.value = 1 // 搜索后重置到第一页
  } catch (error) {
    wordList.value = []
  } finally {
    searched.value = true
    loading.value = false
  }
}

async function handleSelectWord(word) {
  listLoading.value = true
  try {
    const detail = await getWordDetail(word.id)
    result.value = detail.data
  } catch (e) {
    result.value = null
  }
  listLoading.value = false
}
</script>

<style lang="scss" scoped>
.word-search {
  min-height: 100vh;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  background: #f5f7fa;

  .search-card {
    width: 1480px;
    min-height: 800px;
    padding: 32px 36px 28px 36px;
    border-radius: 18px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.08);

    .search-title {
      text-align: center;
      font-size: 32px;
      font-weight: 700;
      color: #1890ff;
      margin-bottom: 36px;
      letter-spacing: 4px;
    }
    .word-list {
      margin-top: 32px;
      .list-title {
        font-size: 18px;
        font-weight: 600;
        margin-bottom: 12px;
      }
    }
    .result-area {
      margin-top: 32px;
      .word-main {
        font-size: 30px;
        font-weight: 700;
        color: #1890ff;
        margin-bottom: 12px;
        display: flex;
        justify-content: center;
        align-items: baseline;
        gap: 18px;

        .phonetic {
          font-size: 18px;
          color: #888;
          font-weight: 400;
        }
      }
      .definition-list {
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
      .examples,
      .synonyms,
      .confusions {
        margin-top: 18px;
        .example-title {
          font-size: 16px;
          color: #666;
          margin-bottom: 6px;
          text-align: center;
        }
        ul {
          padding-left: 0;
          text-align: center;
          list-style-position: inside;
          li {
            font-size: 15px;
            color: #444;
            margin-bottom: 4px;
            text-align: center;
          }
        }
      }
    }
    .no-result {
      margin-top: 32px;
      color: #f56c6c;
      text-align: center;
      font-size: 18px;
    }
  }
}
</style>