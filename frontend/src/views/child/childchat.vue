<template>
  <div class="chat-scenario">
    <el-card class="chat-card">
      <div class="chat-title">情景对话</div>
      <div class="chat-layout">
        <!-- 动态对话侧边栏 -->
        <el-menu
          class="chat-sidebar"
          :default-active="String(activeDialogIndex)"
          @select="handleDialogSelect"
        >
          <el-menu-item
            v-for="(dialog, idx) in dialogs"
            :key="dialog.id || idx"
            :index="String(idx)"
          >
            <el-icon><ChatLineRound /></el-icon>
            <span>{{ dialog.name || `对话${idx + 1}` }}</span>
          </el-menu-item>
          <el-menu-item
            index="-1"
            @click="cancelDialogSelect"
            class="new-dialog"
          >
            <el-icon><Plus /></el-icon>
            <span>取消选择</span>
          </el-menu-item>
        </el-menu>
        <!-- 聊天内容区 -->
        <div class="chat-main">
          <div class="chat-history" v-if="currentDialog">
            <div
              v-for="(msg, idx) in currentDialog.messages"
              :key="idx"
              :class="['chat-msg', msg.role]"
            >
              <span class="msg-role">{{ msg.role === 'user' ? '我' : 'LLM' }}：</span>
              <span class="msg-content">{{ msg.content }}</span>
            </div>
          </div>
          <div class="chat-input-area">
            <el-input
              v-model="input"
              placeholder="请输入内容并回车发送"
              @keyup.enter="handleSend"
              :disabled="loading"
              clearable
            />
            <el-button
              type="primary"
              @click="handleSend"
              :loading="loading"
              :disabled="!input"
              style="margin-left: 12px;"
            >发送</el-button>
          </div>
        </div>
      </div>
    </el-card>
    <!-- 主题选择弹窗 -->
    <el-dialog v-model="themeDialogVisible" title="请选择对话主题" width="300px" :close-on-click-modal="false">
      <el-radio-group v-model="theme">
        <el-radio v-for="item in themeOptions" :key="item.value" :label="item.value">{{ item.label }}</el-radio>
      </el-radio-group>
      <template #footer>
        <el-button @click="themeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleThemeConfirm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { ChatLineRound, Plus } from '@element-plus/icons-vue'
import { createDialogue, Dialoguerecord, getdialogue, getdialoguefirst } from '@/api/dialogue'
import { getUserInfo } from '@/api/dailytest' // 引入获取用户信息API
import { situationaldialogue } from '@/api/llm'

const dialogs = ref([])
const activeDialogIndex = ref(null) // 初始为null，表示未选中任何对话
const input = ref('')
const loading = ref(false)
const themeDialogVisible = ref(false)
const theme = ref('study')
const themeOptions = [
  { label: '学习', value: 'study' },
  { label: '生活', value: 'life' }
]
const tempInput = ref('')
const userId = ref('') // 新增userId
const conversationId = ref('') // 保存当前会话id

const currentDialog = computed(() =>
  activeDialogIndex.value !== null ? dialogs.value[activeDialogIndex.value] : null
)

const username = localStorage.getItem('username') || ''

// 页面加载时获取userId
onMounted(async () => {
  if (username) {
    try {
      const res = await getUserInfo(username)
      userId.value = res.data?.id || ''
      // 获取对话列表
      if (userId.value) {
        const listRes = await getdialoguefirst(userId.value)
        dialogs.value = listRes.data || []
      }
    } catch {
      ElMessage.error('获取用户信息或对话列表失败')
      dialogs.value = []
    }
  }
})

// 选择对话
async function handleDialogSelect(idx) {
  activeDialogIndex.value = Number(idx)
  const dialog = dialogs.value[activeDialogIndex.value]
  if (dialog && dialog.id) {
    try {
      const res = await getdialogue(dialog.id)
      // 后端返回的是 data 字段，不是 messages
      dialog.messages = res.data || []
    } catch {
      dialog.messages = []
      ElMessage.error('获取对话内容失败')
    }
  }
}

// 取消对话选择
function cancelDialogSelect() {
  activeDialogIndex.value = null
}

// 发送消息
async function handleSend() {
  const text = input.value.trim()
  if (!text) return
  // 没有任何对话时，弹窗选择主题
  if (dialogs.value.length === 0 || activeDialogIndex.value === null) {
    tempInput.value = text
    themeDialogVisible.value = true
    return
  }
  await sendMessage(text)
}

async function sendMessage(text) {
  loading.value = true
  const now = new Date().toISOString()
  // 1. 本地显示
  currentDialog.value.messages.push({ role: 'user', content: text })
  // 2. 保存用户消息到后端
  try {
    await Dialoguerecord({
      dialogueId: currentDialog.value.id,
      content: text,
      timestamp: now,
      role: 'user'
    })
    // 3. 向llm发起对话
    const llmRes = await situationaldialogue({
      inputs: {
        user_identity: 'child',
        selected_topic: currentDialog.value.theme
      },
      query: text,
      response_mode: 'blocking',
      conversation_id: conversationId.value,
      user: userId.value
    })
    // 4. 保存AI回复
    if (llmRes.answer) {
      currentDialog.value.messages.push({ role: 'LLM', content: llmRes.answer })
      await Dialoguerecord({
        dialogueId: currentDialog.value.id,
        content: llmRes.answer,
        timestamp: new Date().toISOString(),
        role: 'LLM'
      })
    }
    // 6. 更新conversation_id
    if (llmRes.conversation_id) {
      conversationId.value = llmRes.conversation_id
    }
  } catch (e) {
    ElMessage.error('消息发送失败')
  }
  loading.value = false
  input.value = ''
}

// 主题选择确认
async function handleThemeConfirm() {
  themeDialogVisible.value = false
  loading.value = true
  const now = new Date().toISOString()
  try {
    const llmRes = await situationaldialogue({
      inputs: {
        user_identity: 'child',
        selected_topic: theme.value
      },
      query: 'child/' + theme.value,
      response_mode: 'blocking',
      conversation_id: '',
      user: userId.value
    })
    console.log('llmRes:', llmRes)
    // 这里强类型判断
    const conversation_id = typeof llmRes.conversation_id === 'string'
      ? llmRes.conversation_id
      : ''
    if (!conversation_id) {
      ElMessage.error('未获取到会话ID')
      loading.value = false
      return
    }
    conversationId.value = conversation_id

    await createDialogue({
      id: conversation_id,
      userId: userId.value,
      name: `对话${dialogs.value.length + 1}`,
      theme: theme.value,
      buildTime: now
    })

    await Dialoguerecord({
      dialogueId: conversation_id,
      content: tempInput.value,
      timestamp: now,
      role: 'user'
    })

    if (llmRes.answer) {
      await Dialoguerecord({
        dialogueId: conversation_id,
        content: llmRes.answer,
        timestamp: new Date().toISOString(),
        role: 'LLM'
      })
    }

    dialogs.value.push({
      id: conversation_id,
      messages: [
        { role: 'user', content: tempInput.value },
        { role: 'LLM', content: llmRes.answer || '' }
      ],
      name: `对话${dialogs.value.length + 1}`,
      theme: theme.value,
      buildTime: now
    })
    activeDialogIndex.value = dialogs.value.length - 1
    input.value = ''
  } catch (e) {
    ElMessage.error('对话创建失败')
  }
  loading.value = false
  tempInput.value = ''
}

watch(dialogs, val => {
  localStorage.setItem('dialogs', JSON.stringify(val))
}, { deep: true })
</script>

<style lang="scss" scoped>
.chat-scenario {
  min-height: 100vh;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  background: #f5f7fa;

  .chat-card {
    width: 1480px;
    min-height: 800px;
    padding: 32px 0 28px 0;
    border-radius: 18px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.08);

    .chat-title {
      text-align: center;
      font-size: 32px;
      font-weight: 700;
      color: #1890ff;
      margin-bottom: 36px;
      letter-spacing: 4px;
    }

    .chat-layout {
      display: flex;
      flex-direction: row;
      height: 600px;

      .chat-sidebar {
        width: 180px;
        border-right: 1px solid #f0f0f0;
        min-height: 100%;
        .el-menu-item {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 16px;
        }
        .new-dialog {
          color: #1890ff;
        }
      }

      .chat-main {
        flex: 1;
        display: flex;
        flex-direction: column;
        padding: 0 36px;

        .chat-history {
          flex: 1;
          overflow-y: auto;
          margin-bottom: 24px;
          padding: 16px 0;
          .chat-msg {
            margin-bottom: 14px;
            display: flex;
            align-items: flex-start;
            .msg-role {
              font-weight: 600;
              margin-right: 8px;
              color: #1890ff;
            }
            &.LLM .msg-role {
              color: #67c23a;
            }
            .msg-content {
              word-break: break-all;
            }
          }
        }

        .chat-input-area {
          display: flex;
          align-items: center;
          padding: 12px 0;
          border-top: 1px solid #f0f0f0;
          background: #fff;
          .el-input {
            flex: 1;
          }
        }
      }
    }
  }
}
</style>