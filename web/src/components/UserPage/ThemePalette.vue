<template>
  <div class="theme-palette">
    <div class="theme-header" @click="toggleExpanded">
      <el-icon :class="{'rotate-icon': expanded}">
        <ArrowRight />
      </el-icon>
      <span>主题调色板</span>
    </div>

    <el-collapse-transition>
      <div v-show="expanded" class="theme-content">
        <div class="theme-options">
          <div
              v-for="theme in themes"
              :key="theme.name"
              class="theme-option"
              :class="{'active': currentTheme === theme.name}"
              @click="changeTheme(theme.name)"
          >
            <div class="theme-preview" :style="{'background': theme.preview}"></div>
            <span>{{ theme.label }}</span>
          </div>
        </div>

        <div class="custom-theme">
          <h4>自定义主题</h4>
          <div class="color-pickers">
            <div class="color-picker" v-for="(color, key) in customColors" :key="key">
              <label>{{ colorLabels[key] }}</label>
              <el-color-picker
                  v-model="customColors[key]"
                  show-alpha
                  :predefine="predefinedColors"
              />
            </div>
          </div>
          <el-button type="primary" size="small" @click="applyCustomTheme">
            应用自定义主题
          </el-button>
        </div>
      </div>
    </el-collapse-transition>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ArrowRight } from '@element-plus/icons-vue'

const expanded = ref(false)
const currentTheme = ref('default')
const customColors = ref({
  primaryStart: '#ff7e36',
  primaryEnd: '#ff5e3a',
  sidebarStart: '#f8f1e9',
  sidebarEnd: '#f1d4c3',
  sidebarActive: '#d16654',
  textColor: '#5a5a5a'
})

const colorLabels = {
  primaryStart: '主色调开始',
  primaryEnd: '主色调结束',
  sidebarStart: '侧边栏开始',
  sidebarEnd: '侧边栏结束',
  sidebarActive: '活动菜单色',
  textColor: '文字颜色'
}

const predefinedColors = [
  '#ff7e36',
  '#ff5e3a',
  '#d16654',
  '#f8f1e9',
  '#f1d4c3',
  '#5a5a5a',
  '#ff4757',
  '#ffeb3b',
  '#f8b195',
  '#4CAF50',
  '#2196F3',
  '#9C27B0',
  '#607D8B',
  '#FFFFFF',
  '#000000'
]

const themes = [
  {
    name: 'default',
    label: '默认主题',
    preview: 'linear-gradient(135deg, #ff7e36, #ff5e3a)'
  },
  {
    name: 'ocean',
    label: '海洋主题',
    preview: 'linear-gradient(135deg, #2196F3, #03A9F4)'
  },
  {
    name: 'forest',
    label: '森林主题',
    preview: 'linear-gradient(135deg, #4CAF50, #8BC34A)'
  },
  {
    name: 'royal',
    label: '皇家主题',
    preview: 'linear-gradient(135deg, #9C27B0, #673AB7)'
  },
  {
    name: 'sunset',
    label: '日落主题',
    preview: 'linear-gradient(135deg, #FF5722, #FF9800)'
  },
  {
    name: 'monochrome',
    label: '单色主题',
    preview: 'linear-gradient(135deg, #607D8B, #9E9E9E)'
  },
  // 新增的主题
  {
    name: 'light',
    label: '明亮主题',
    preview: 'linear-gradient(135deg, #f5f7fa, #c3cfe2)'
  },
  {
    name: 'vivid',
    label: '鲜明主题',
    preview: 'linear-gradient(135deg, #00c6ff, #0072ff)'
  },
  {
    name: 'spring',
    label: '春日主题',
    preview: 'linear-gradient(135deg, #a8e063, #56ab2f)'
  },
  {
    name: 'cotton',
    label: '棉花主题',
    preview: 'linear-gradient(135deg, #E0EAFC, #CFDEF3)'
  },
  {
    name: 'sunshine',
    label: '阳光主题',
    preview: 'linear-gradient(135deg, #f6d365, #fda085)'
  },
  {
    name: 'berry',
    label: '浆果主题',
    preview: 'linear-gradient(135deg, #8E2DE2, #4A00E0)'
  },
  {
    name: 'mint',
    label: '薄荷主题',
    preview: 'linear-gradient(135deg, #00d2ff, #3a7bd5)'
  }
]

const toggleExpanded = () => {
  expanded.value = !expanded.value
}

const changeTheme = (themeName) => {
  currentTheme.value = themeName
  const root = document.documentElement

  switch(themeName) {
    case 'ocean':
      root.style.setProperty('--primary-gradient-start', '#2196F3')
      root.style.setProperty('--primary-gradient-end', '#03A9F4')
      root.style.setProperty('--sidebar-bg-start', '#e8f4fc')
      root.style.setProperty('--sidebar-bg-end', '#c2e0f5')
      root.style.setProperty('--sidebar-active', '#1976D2')
      root.style.setProperty('--sidebar-icon', '#1976D2')
      root.style.setProperty('--progress-start', '#64B5F6')
      root.style.setProperty('--progress-end', '#1976D2')
      root.style.setProperty('--donate-btn-bg', '#2196F3')
      root.style.setProperty('--donate-btn-hover', '#1976D2')
      break

    case 'forest':
      root.style.setProperty('--primary-gradient-start', '#4CAF50')
      root.style.setProperty('--primary-gradient-end', '#8BC34A')
      root.style.setProperty('--sidebar-bg-start', '#edf7ed')
      root.style.setProperty('--sidebar-bg-end', '#d8ebd8')
      root.style.setProperty('--sidebar-active', '#388E3C')
      root.style.setProperty('--sidebar-icon', '#388E3C')
      root.style.setProperty('--progress-start', '#81C784')
      root.style.setProperty('--progress-end', '#388E3C')
      root.style.setProperty('--donate-btn-bg', '#4CAF50')
      root.style.setProperty('--donate-btn-hover', '#388E3C')
      break

    case 'royal':
      root.style.setProperty('--primary-gradient-start', '#9C27B0')
      root.style.setProperty('--primary-gradient-end', '#673AB7')
      root.style.setProperty('--sidebar-bg-start', '#f3e5f5')
      root.style.setProperty('--sidebar-bg-end', '#e1bee7')
      root.style.setProperty('--sidebar-active', '#7B1FA2')
      root.style.setProperty('--sidebar-icon', '#7B1FA2')
      root.style.setProperty('--progress-start', '#BA68C8')
      root.style.setProperty('--progress-end', '#7B1FA2')
      root.style.setProperty('--donate-btn-bg', '#9C27B0')
      root.style.setProperty('--donate-btn-hover', '#7B1FA2')
      break

    case 'sunset':
      root.style.setProperty('--primary-gradient-start', '#FF5722')
      root.style.setProperty('--primary-gradient-end', '#FF9800')
      root.style.setProperty('--sidebar-bg-start', '#fbe9e7')
      root.style.setProperty('--sidebar-bg-end', '#ffccbc')
      root.style.setProperty('--sidebar-active', '#E64A19')
      root.style.setProperty('--sidebar-icon', '#E64A19')
      root.style.setProperty('--progress-start', '#FFAB91')
      root.style.setProperty('--progress-end', '#E64A19')
      root.style.setProperty('--donate-btn-bg', '#FF5722')
      root.style.setProperty('--donate-btn-hover', '#E64A19')
      break
    case 'light':
      root.style.setProperty('--primary-gradient-start', '#f5f7fa')
      root.style.setProperty('--primary-gradient-end', '#c3cfe2')
      root.style.setProperty('--sidebar-bg-start', '#ffffff')
      root.style.setProperty('--sidebar-bg-end', '#f1f5f9')
      root.style.setProperty('--sidebar-active', '#64748b')
      root.style.setProperty('--sidebar-icon', '#334155')
      root.style.setProperty('--progress-start', '#e2e8f0')
      root.style.setProperty('--progress-end', '#64748b')
      root.style.setProperty('--donate-btn-bg', '#475569')
      root.style.setProperty('--donate-btn-hover', '#334155')
      break

    case 'vivid':
      root.style.setProperty('--primary-gradient-start', '#00c6ff')
      root.style.setProperty('--primary-gradient-end', '#0072ff')
      root.style.setProperty('--sidebar-bg-start', '#e6f2ff')
      root.style.setProperty('--sidebar-bg-end', '#cce5ff')
      root.style.setProperty('--sidebar-active', '#0056b3')
      root.style.setProperty('--sidebar-icon', '#003d82')
      root.style.setProperty('--progress-start', '#66b0ff')
      root.style.setProperty('--progress-end', '#0056b3')
      root.style.setProperty('--donate-btn-bg', '#0072ff')
      root.style.setProperty('--donate-btn-hover', '#0056b3')
      break

    case 'spring':
      root.style.setProperty('--primary-gradient-start', '#a8e063')
      root.style.setProperty('--primary-gradient-end', '#56ab2f')
      root.style.setProperty('--sidebar-bg-start', '#f0f7e6')
      root.style.setProperty('--sidebar-bg-end', '#d9efc4')
      root.style.setProperty('--sidebar-active', '#3d8b1a')
      root.style.setProperty('--sidebar-icon', '#2b6312')
      root.style.setProperty('--progress-start', '#8bc34a')
      root.style.setProperty('--progress-end', '#3d8b1a')
      root.style.setProperty('--donate-btn-bg', '#56ab2f')
      root.style.setProperty('--donate-btn-hover', '#3d8b1a')
      break

    case 'cotton':
      root.style.setProperty('--primary-gradient-start', '#E0EAFC')
      root.style.setProperty('--primary-gradient-end', '#CFDEF3')
      root.style.setProperty('--sidebar-bg-start', '#ffffff')
      root.style.setProperty('--sidebar-bg-end', '#f8fafc')
      root.style.setProperty('--sidebar-active', '#94a3b8')
      root.style.setProperty('--sidebar-icon', '#475569')
      root.style.setProperty('--progress-start', '#e2e8f0')
      root.style.setProperty('--progress-end', '#94a3b8')
      root.style.setProperty('--donate-btn-bg', '#CBD5E1')
      root.style.setProperty('--donate-btn-hover', '#94A3B8')
      break

    case 'sunshine':
      root.style.setProperty('--primary-gradient-start', '#f6d365')
      root.style.setProperty('--primary-gradient-end', '#fda085')
      root.style.setProperty('--sidebar-bg-start', '#fff7ed')
      root.style.setProperty('--sidebar-bg-end', '#ffedd5')
      root.style.setProperty('--sidebar-active', '#ea580c')
      root.style.setProperty('--sidebar-icon', '#9a3412')
      root.style.setProperty('--progress-start', '#fdba74')
      root.style.setProperty('--progress-end', '#ea580c')
      root.style.setProperty('--donate-btn-bg', '#f97316')
      root.style.setProperty('--donate-btn-hover', '#ea580c')
      break

    case 'berry':
      root.style.setProperty('--primary-gradient-start', '#8E2DE2')
      root.style.setProperty('--primary-gradient-end', '#4A00E0')
      root.style.setProperty('--sidebar-bg-start', '#f3e8ff')
      root.style.setProperty('--sidebar-bg-end', '#e9d5ff')
      root.style.setProperty('--sidebar-active', '#7e22ce')
      root.style.setProperty('--sidebar-icon', '#581c87')
      root.style.setProperty('--progress-start', '#a855f7')
      root.style.setProperty('--progress-end', '#7e22ce')
      root.style.setProperty('--donate-btn-bg', '#9333ea')
      root.style.setProperty('--donate-btn-hover', '#7e22ce')
      break

    case 'mint':
      root.style.setProperty('--primary-gradient-start', '#00d2ff')
      root.style.setProperty('--primary-gradient-end', '#3a7bd5')
      root.style.setProperty('--sidebar-bg-start', '#ecfdf5')
      root.style.setProperty('--sidebar-bg-end', '#d1fae5')
      root.style.setProperty('--sidebar-active', '#0d9488')
      root.style.setProperty('--sidebar-icon', '#115e59')
      root.style.setProperty('--progress-start', '#5eead4')
      root.style.setProperty('--progress-end', '#0d9488')
      root.style.setProperty('--donate-btn-bg', '#14b8a6')
      root.style.setProperty('--donate-btn-hover', '#0d9488')
      break
    case 'monochrome':
      root.style.setProperty('--primary-gradient-start', '#607D8B')
      root.style.setProperty('--primary-gradient-end', '#9E9E9E')
      root.style.setProperty('--sidebar-bg-start', '#f5f5f5')
      root.style.setProperty('--sidebar-bg-end', '#e0e0e0')
      root.style.setProperty('--sidebar-active', '#455A64')
      root.style.setProperty('--sidebar-icon', '#455A64')
      root.style.setProperty('--progress-start', '#90A4AE')
      root.style.setProperty('--progress-end', '#455A64')
      root.style.setProperty('--donate-btn-bg', '#607D8B')
      root.style.setProperty('--donate-btn-hover', '#455A64')
      break

    default: // default theme
      root.style.setProperty('--primary-gradient-start', '#ff7e36')
      root.style.setProperty('--primary-gradient-end', '#ff5e3a')
      root.style.setProperty('--sidebar-bg-start', '#f8f1e9')
      root.style.setProperty('--sidebar-bg-end', '#f1d4c3')
      root.style.setProperty('--sidebar-active', '#d16654')
      root.style.setProperty('--sidebar-icon', '#d16654')
      root.style.setProperty('--progress-start', '#f8b195')
      root.style.setProperty('--progress-end', '#d16654')
      root.style.setProperty('--donate-btn-bg', '#ff4757')
      root.style.setProperty('--donate-btn-hover', '#ff2d42')
  }

  // 保存主题到本地存储
  localStorage.setItem('currentTheme', themeName)
}

const applyCustomTheme = () => {
  const root = document.documentElement
  root.style.setProperty('--primary-gradient-start', customColors.value.primaryStart)
  root.style.setProperty('--primary-gradient-end', customColors.value.primaryEnd)
  root.style.setProperty('--sidebar-bg-start', customColors.value.sidebarStart)
  root.style.setProperty('--sidebar-bg-end', customColors.value.sidebarEnd)
  root.style.setProperty('--sidebar-active', customColors.value.sidebarActive)
  root.style.setProperty('--sidebar-icon', customColors.value.sidebarActive)
  root.style.setProperty('--text-gray', customColors.value.textColor)

  // 保存自定义颜色到本地存储
  localStorage.setItem('customThemeColors', JSON.stringify(customColors.value))
  localStorage.setItem('currentTheme', 'custom')
  currentTheme.value = 'custom'
}

onMounted(() => {
  // 从本地存储加载主题
  const savedTheme = localStorage.getItem('currentTheme')
  if (savedTheme) {
    currentTheme.value = savedTheme
    if (savedTheme === 'custom') {
      const savedColors = localStorage.getItem('customThemeColors')
      if (savedColors) {
        customColors.value = JSON.parse(savedColors)
      }
    }
    changeTheme(savedTheme)
  }
})
</script>

<style scoped>
.theme-palette {
  background-color: var(--dropdown-bg);
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin: 15px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.theme-header {
  display: flex;
  align-items: center;
  padding: 12px 15px;
  background-color: var(--sidebar-active);
  color: white;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.theme-header:hover {
  background-color: var(--donate-btn-hover);
}

.theme-header .el-icon {
  margin-right: 10px;
  transition: transform 0.3s ease;
}

.rotate-icon {
  transform: rotate(90deg);
}

.theme-content {
  padding: 15px;
}

.theme-options {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  margin-bottom: 20px;
}

.theme-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  padding: 8px;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.theme-option:hover {
  background-color: var(--sidebar-hover-bg);
}

.theme-option.active {
  background-color: var(--sidebar-active-bg);
  border: 1px solid var(--sidebar-active);
}

.theme-preview {
  width: 100%;
  height: 40px;
  border-radius: 4px;
  margin-bottom: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.custom-theme {
  border-top: 1px solid var(--sidebar-border);
  padding-top: 15px;
}

.custom-theme h4 {
  margin: 0 0 15px 0;
  color: var(--sidebar-active);
}

.color-pickers {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  margin-bottom: 15px;
}

.color-picker {
  display: flex;
  align-items: center;
}

.color-picker label {
  margin-right: 8px;
  font-size: 13px;
  color: var(--text-gray);
  min-width: 80px;
}

:deep(.el-color-picker__trigger) {
  width: 30px;
  height: 30px;
}
</style>