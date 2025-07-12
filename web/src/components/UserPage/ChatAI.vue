<template>
  <div class="chat-ai-container" :class="{ 'expanded': isExpanded }">
    <div class="chat-header" @click="toggleChat">
      <h3>AI 爱心助手</h3>
      <el-icon class="toggle-icon">
        <ArrowDown v-if="isExpanded" />
        <ChatDotRound v-else />
      </el-icon>
    </div>

    <div class="chat-body" v-if="isExpanded">
      <div class="messages-container" ref="messagesContainer">
        <div v-for="(message, index) in messages" :key="index"
             :class="['message', message.type]">
          <div class="message-content">
            <div class="message-text">
              <span v-if="message.type === 'ai' && message.isTyping">
                {{ displayedTexts[index] }}<span class="typing-cursor">|</span>
              </span>
              <span v-else>
                {{ message.text }}
              </span>
            </div>
            <div class="message-time">{{ message.time }}</div>
          </div>
          <el-avatar class="message-avatar" :size="32">
            <img v-if="message.type === 'ai'" src="@/assets/images/logo.png" alt="AI" />
            <User v-else />
          </el-avatar>
        </div>
      </div>

      <div class="input-area">
        <el-input
            v-model="inputMessage"
            placeholder="输入您的问题..."
            @keyup.enter="sendMessage"
            class="message-input"
        >
          <template #append>
            <el-button @click="sendMessage" :icon="Promotion" />
          </template>
        </el-input>

        <div class="input-actions">
          <el-tooltip content="语音输入" placement="top">
            <el-button
                circle
                :type="isListening ? 'danger' : 'primary'"
                @click="toggleVoiceInput"
                :icon="Microphone"
            />
          </el-tooltip>
          <el-button @click="clearChat" :icon="Delete" circle />
        </div>

        <div v-if="isListening" class="voice-status">
          <el-icon class="pulse-icon"><Mic /></el-icon>
          <span>正在聆听...请说话</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from 'vue'
import {
  ArrowDown,
  ChatDotRound,
  Promotion,
  Microphone,
  Delete,
  Mic,
  User
} from '@element-plus/icons-vue'

const isExpanded = ref(false)
const inputMessage = ref('')
const isListening = ref(false)
const displayedTexts = ref({})
const typingIntervals = ref({})
const messagesContainer = ref(null)

const messages = ref([
  {
    type: 'ai',
    text: '您好！我是爱心助手AI，有什么可以帮您的吗？我可以解答关于慈善捐赠、志愿活动、救助申请等方面的问题。',
    time: '刚刚',
    isTyping: false
  }
])

const toggleChat = () => {
  isExpanded.value = !isExpanded.value
  if (isExpanded.value) {
    nextTick(scrollToBottom)
  }
}

const sendMessage = () => {
  if (!inputMessage.value.trim()) return

  // 添加用户消息
  const userMessage = {
    type: 'user',
    text: inputMessage.value,
    time: '刚刚',
    isTyping: false
  }
  messages.value.push(userMessage)

  // 清空输入框
  const userInput = inputMessage.value
  inputMessage.value = ''

  // 滚动到底部
  scrollToBottom()

  // 模拟AI思考中...
  const aiMessageIndex = messages.value.length
  messages.value.push({
    type: 'ai',
    text: '',
    time: '正在输入...',
    isTyping: true
  })

  // 模拟网络请求延迟
  setTimeout(() => {
    // 获取AI回复
    const responses = [
      "感谢您的提问。关于慈善捐赠，我们有多种方式可以参与，您可以通过捐赠页面选择物资捐赠或资金捐赠。",
      "爱心活动通常每月举办一次，您可以在活动页面查看最新安排并报名参加。",
      "紧急救助申请需要填写基本信息并上传相关证明，我们会在24小时内审核并回复您。",
      "我们的捐赠资金使用情况会在官网定期公示，确保透明公开。",
      "成为志愿者非常简单，您可以在个人中心提交申请，我们会尽快与您联系。"
    ]

    const response = responses[Math.floor(Math.random() * responses.length)]

    // 更新消息内容
    messages.value[aiMessageIndex].text = response
    messages.value[aiMessageIndex].time = '刚刚'

    // 开始打字效果
    startTypingEffect(aiMessageIndex, response)
  }, 800)
}

const startTypingEffect = (index, fullText) => {
  // 清除已有的interval
  if (typingIntervals.value[index]) {
    clearInterval(typingIntervals.value[index])
  }

  // 初始化显示文本
  displayedTexts.value[index] = ''

  let charIndex = 0
  typingIntervals.value[index] = setInterval(() => {
    if (charIndex < fullText.length) {
      displayedTexts.value[index] += fullText.charAt(charIndex)
      charIndex++
      scrollToBottom()
    } else {
      // 打字完成
      clearInterval(typingIntervals.value[index])
      messages.value[index].isTyping = false
      delete typingIntervals.value[index]
    }
  }, 30) // 控制打字速度
}

const toggleVoiceInput = () => {
  isListening.value = !isListening.value
  if (isListening.value) {
    setTimeout(() => {
      inputMessage.value = "我想查询最近的爱心活动信息"
      isListening.value = false
    }, 2000)
  }
}

const clearChat = () => {
  // 清除所有打字效果
  Object.values(typingIntervals.value).forEach(interval => {
    clearInterval(interval)
  })
  typingIntervals.value = {}
  displayedTexts.value = {}

  // 保留第一条欢迎消息
  messages.value = [{
    type: 'ai',
    text: '您好！我是爱心助手AI，有什么可以帮您的吗？我可以解答关于慈善捐赠、志愿活动、救助申请等方面的问题。',
    time: '刚刚',
    isTyping: false
  }]
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 组件卸载时清除所有interval
onMounted(() => {
  return () => {
    Object.values(typingIntervals.value).forEach(interval => {
      clearInterval(interval)
    })
  }
})
</script>

<style scoped lang="scss">
.chat-ai-container {
  position: fixed;
  right: 20px;
  bottom: 20px;
  width: 60px;
  height: 60px;
  background-color: #d16654;
  border-radius: 50%;
  color: white;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  overflow: hidden;
  z-index: 9999;

  &.expanded {
    width: 350px;
    height: 500px;
    border-radius: 12px;
  }
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  flex-shrink: 0;
  background-color: #d16654;

  h3 {
    margin: 0;
    font-size: 16px;
    display: none;
  }

  .toggle-icon {
    font-size: 24px;
    margin-left: auto;
  }
}

.expanded .chat-header {
  h3 {
    display: block;
  }

  .toggle-icon {
    margin-left: 0;
  }
}

.chat-body {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  background-color: white;
  height: 100%;
}

.messages-container {
  flex-grow: 1;
  overflow-y: auto;
  padding: 15px;
  max-height: calc(100% - 150px);
  min-height: 100px;
}

.message {
  display: flex;
  margin-bottom: 15px;

  &.user {
    flex-direction: row-reverse;

    .message-content {
      background-color: #f8f1e9;
      border-top-right-radius: 0;
      border-top-left-radius: 12px;
    }
  }

  &.ai {
    .message-content {
      background-color: #f1d4c3;
      border-top-right-radius: 12px;
      border-top-left-radius: 0;
    }
  }
}

.message-content {
  max-width: 70%;
  padding: 10px 15px;
  border-radius: 12px;
  position: relative;
}

.message-text {
  font-size: 14px;
  color: #333;
}

.message-time {
  font-size: 10px;
  color: #888;
  text-align: right;
  margin-top: 4px;
}

.message-avatar {
  align-self: flex-end;
  margin: 0 8px;
}

.input-area {
  flex-shrink: 0;
  padding: 15px;
  background-color: white;
  border-top: 1px solid #f0f0f0;
  position: sticky;
  bottom: 0;
  box-shadow: 0 -2px 5px rgba(0,0,0,0.05);
}

.message-input {
  margin-bottom: 10px;
}

.input-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.voice-status {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 10px;
  color: #d16654;
  font-size: 14px;

  .pulse-icon {
    margin-right: 8px;
    animation: pulse 1.5s infinite;
  }
}

.typing-cursor {
  animation: blink 1s infinite;
  opacity: 1;
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}

@keyframes pulse {
  0% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.2); opacity: 0.7; }
  100% { transform: scale(1); opacity: 1; }
}

/* 滚动条样式 */
.messages-container::-webkit-scrollbar {
  width: 5px;
}

.messages-container::-webkit-scrollbar-thumb {
  background-color: rgba(209, 102, 84, 0.3);
  border-radius: 3px;

  &:hover {
    background-color: rgba(209, 102, 84, 0.5);
  }
}

@media (max-height: 600px) {
  .chat-ai-container.expanded {
    height: 70vh;
  }
}
</style>