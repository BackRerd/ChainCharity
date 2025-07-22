<template>
  <div class="donation-container">
    <!-- 感谢信息 - 添加动画效果 -->
    <transition name="fade">
      <div class="thank-you-message" v-if="showThankYou">
        <h3><i class="el-icon-success"></i> 感谢您的慷慨捐赠！</h3>
        <p>您的善举将帮助更多需要帮助的人</p>
        <div class="heart-animation">❤️</div>
      </div>
    </transition>

    <!-- 主内容区域 -->
    <div class="donation-content">
      <!-- 标题区 -->
      <div class="header-section">
        <h2 class="page-title">
          <span class="title-text">爱心捐赠平台</span>
          <span class="title-icon">❤️</span>
        </h2>
        <p class="page-subtitle">每一份捐赠都是希望的种子</p>
      </div>

      <!-- 标签页 - 添加卡片效果 -->
      <el-tabs v-model="activeTab" type="card" class="animated-tabs">
        <!-- 物品捐赠 -->
        <el-tab-pane label="物品捐赠" name="item">
          <div class="tab-content">
            <el-card shadow="hover" class="donation-card animated-card">
              <div class="donation-title">
                <i class="el-icon-box icon-highlight"></i>
                <span>物品捐赠信息</span>
              </div>
              <el-form
                  ref="itemFormRef"
                  :model="itemForm"
                  :rules="itemRules"
                  label-width="120px"
                  class="form-container"
              >
                <el-form-item label="活动ID">
                  <el-input v-model="itemForm.activityId" disabled />
                </el-form-item>
                <el-form-item label="捐赠者ID">
                  <el-input v-model="itemForm.donorId" disabled />
                </el-form-item>
                <el-form-item label="物品类型" prop="itemType">
                  <el-input
                      v-model="itemForm.itemType"
                      placeholder="如：书本、衣物等"
                      clearable
                  />
                </el-form-item>
                <el-form-item label="数量" prop="quantity">
                  <el-input-number
                      v-model="itemForm.quantity"
                      :min="1"
                      controls-position="right"
                  />
                </el-form-item>
                <el-form-item label="物品图片">
                  <el-upload
                      action="/api/upload"
                      :limit="1"
                      :file-list="itemFileList"
                      :on-success="handleItemUploadSuccess"
                      :before-upload="beforeUpload"
                  >
                    <el-button type="primary">点击上传</el-button>
                    <template #tip>
                      <div class="el-upload__tip">
                        支持 JPG/PNG 格式，大小不超过 2MB
                      </div>
                    </template>
                  </el-upload>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" class="submit-btn" @click="submitItemForm">
                    提交捐赠
                  </el-button>
                  <el-button class="reset-btn" @click="resetItemForm">重置</el-button>
                </el-form-item>
              </el-form>
            </el-card>
            <div class="floating-icons">
              <i class="el-icon-box"></i>
              <i class="el-icon-t-shirt"></i>
              <i class="el-icon-notebook-2"></i>
            </div>
          </div>
        </el-tab-pane>

        <!-- 资金捐赠 -->
        <el-tab-pane label="资金捐赠" name="money">
          <div class="tab-content">
            <el-card shadow="hover" class="donation-card animated-card">
              <div class="donation-title">
                <i class="el-icon-money icon-highlight"></i>
                <span>资金捐赠信息</span>
              </div>
              <el-form
                  ref="moneyFormRef"
                  :model="moneyForm"
                  :rules="moneyRules"
                  label-width="120px"
                  class="form-container"
              >
                <el-form-item label="活动ID">
                  <el-input v-model="moneyForm.activityId" disabled />
                </el-form-item>
                <el-form-item label="捐赠者ID">
                  <el-input v-model="moneyForm.donorId" disabled />
                </el-form-item>
                <el-form-item label="捐赠金额" prop="amount">
                  <el-input-number
                      v-model="moneyForm.amount"
                      :min="1"
                      :precision="2"
                      :step="100"
                      controls-position="right"
                  />
                  <span class="unit">元</span>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" class="submit-btn" @click="submitMoneyForm">
                    提交捐赠
                  </el-button>
                  <el-button class="reset-btn" @click="resetMoneyForm">重置</el-button>
                </el-form-item>
              </el-form>
            </el-card>
            <div class="floating-icons">
              <i class="el-icon-coin"></i>
              <i class="el-icon-wallet"></i>
              <i class="el-icon-bank-card"></i>
            </div>
          </div>
        </el-tab-pane>

        <!-- 捐赠记录 -->
        <el-tab-pane label="我的捐赠记录" name="records">
          <div class="tab-content">
            <el-tabs v-model="recordTab" class="nested-tabs">
              <el-tab-pane label="物品捐赠" name="itemRecords">
                <el-table :data="itemRecords" border style="width: 100%">
                  <el-table-column prop="itemType" label="物品类型" />
                  <el-table-column prop="quantity" label="数量" width="100" />
                  <el-table-column label="审核状态" width="120">
                    <template #default="{ row }">
                      <el-tag :type="getStatusTagType(row.approvalStatus)">
                        {{ formatStatus(row.approvalStatus) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="recipientIds" label="分发人员ID" width="150" />
                  <el-table-column prop="donationTime" label="捐赠时间" width="180">
                    <template #default="{ row }">
                      {{ formatDate(row.donationTime) }}
                    </template>
                  </el-table-column>
                </el-table>
                <el-pagination
                    v-model:current-page="itemPage.current"
                    :page-size="itemPage.size"
                    :total="itemPage.total"
                    @current-change="fetchItemRecords"
                    layout="prev, pager, next"
                    class="pagination"
                />
              </el-tab-pane>

              <el-tab-pane label="资金捐赠" name="moneyRecords">
                <el-table :data="moneyRecords" border style="width: 100%">
                  <el-table-column label="金额" width="120">
                    <template #default="{ row }">
                      {{ row.amount }} 元
                    </template>
                  </el-table-column>
                  <el-table-column label="审核状态" width="120">
                    <template #default="{ row }">
                      <el-tag :type="getStatusTagType(row.approvalStatus)">
                        {{ formatStatus(row.approvalStatus) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="recipientIds" label="分配人员ID" width="120" />
                  <el-table-column prop="donationTime" label="捐赠时间" width="180">
                    <template #default="{ row }">
                      {{ formatDate(row.donationTime) }}
                    </template>
                  </el-table-column>

                </el-table>
                <el-pagination
                    v-model:current-page="moneyPage.current"
                    :page-size="moneyPage.size"
                    :total="moneyPage.total"
                    @current-change="fetchMoneyRecords"
                    layout="prev, pager, next"
                    class="pagination"
                />
              </el-tab-pane>
            </el-tabs>
            <div class="floating-icons">
              <i class="el-icon-document"></i>
              <i class="el-icon-notebook-1"></i>
              <i class="el-icon-collection"></i>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 页脚 -->
    <div class="footer-section">
      <p>让世界因你而更美好 ❤️</p>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted, computed} from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

// 用户信息
const user = JSON.parse(localStorage.getItem('user')) || {}
const userId = ref(user.userId || null)

// 标签页控制
const activeTab = ref('item')
const recordTab = ref('itemRecords')
const showThankYou = ref(false)

// 物品捐赠表单
const itemFormRef = ref(null)
const itemForm = reactive({
  activityId: 0,
  donorId: userId.value,
  itemType: '',
  quantity: 1,
  imagePath: ''
})
const itemFileList = ref([])
const itemRules = {
  itemType: [{ required: true, message: '请输入物品类型', trigger: 'blur' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }]
}

// 资金捐赠表单
const moneyFormRef = ref(null)
const moneyForm = reactive({
  activityId: 0,
  donorId: userId.value,
  amount: 100
})
const moneyRules = {
  amount: [
    { required: true, message: '请输入金额', trigger: 'blur' },
    {
      validator: (_, value, callback) => {
        if (value <= 0) {
          callback(new Error('金额必须大于0'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 捐赠记录
const itemRecords = ref([])
const moneyRecords = ref([])
const itemPage = reactive({ current: 1, size: 5, total: 0 })
const moneyPage = reactive({ current: 1, size: 5, total: 0 })

// 表单提交
const submitItemForm = async () => {
  try {
    await itemFormRef.value.validate()
    const response = await axios.post('/item-donations', itemForm)
    if (response.data.code === "200") {
      ElMessage.success({
        message: '物品捐赠提交成功！感谢您的爱心',
        duration: 3000
      })
      showThankYou.value = true
      activeTab.value = 'records'
      await fetchItemRecords()
      resetItemForm()
      setTimeout(() => {
        showThankYou.value = false
      }, 5000)
    }
  } catch (error) {
    ElMessage.error(`提交失败: ${error.message}`)
  }
}

const submitMoneyForm = async () => {
  try {
    await moneyFormRef.value.validate()
    const response = await axios.post('/money-donations', {
      ...moneyForm,
      amount: Number(moneyForm.amount).toFixed(2)
    })
    if (response.data.code === "200") {
      ElMessage.success({
        message: '资金捐赠提交成功！感谢您的爱心',
        duration: 3000
      })
      showThankYou.value = true
      activeTab.value = 'records'
      await fetchMoneyRecords()
      resetMoneyForm()
      setTimeout(() => {
        showThankYou.value = false
      }, 5000)
    }
  } catch (error) {
    ElMessage.error(`提交失败: ${error.message}`)
  }
}

// 表单重置
const resetItemForm = () => {
  itemFormRef.value?.resetFields()
  itemForm.quantity = 1
  itemFileList.value = []
  itemForm.imagePath = ''
}

const resetMoneyForm = () => {
  moneyFormRef.value?.resetFields()
  moneyForm.amount = 100
}

// 图片上传处理
const beforeUpload = (file) => {
  const isImage = ['image/jpeg', 'image/png'].includes(file.type)
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传 JPG/PNG 格式的图片!')
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
  }

  return isImage && isLt2M
}

const handleItemUploadSuccess = (response) => {
  if (response.code === "200") {
    itemForm.imagePath = response.data.path
    ElMessage.success('图片上传成功')
  }
}

// 获取捐赠记录
const fetchItemRecords = async () => {
  if (!userId.value) return

  try {
    const response = await axios.get('/item-donations/page-i', {
      params: {
        page: itemPage.current,
        size: itemPage.size,
        userId: userId.value
      }
    })

    if (response.data.code === "200") {
      itemRecords.value = processData({
        page: response.data.data.page,
        distributions: response.data.data.distributions
      }).page.records
      itemPage.total = response.data.data.page.total
    }
  } catch (error) {
    ElMessage.error('获取物品捐赠记录失败')
  }
}
const processData = (data) => {
  const { page, distributions } = data

  const newRecords = page.records.map(record => ({
    ...record,
    recipientIds: getRecipientIdsByDonationId(distributions, record.donationId)
  }))

  return {
    ...data,
    page: {
      ...page,
      records: newRecords
    }
  }
}
// 普通函数：建立 donationId -> recipientIds 的映射
const buildRecipientIdMap = (distributions) => {
  const map = {};
  distributions.forEach((dist) => {
    const donationId = Object.keys(dist)[0];
    map[donationId] = dist[donationId].map((d) => d.recipientId);
  });
  return map;
};

const getRecipientIdsByDonationId = (distributions, donationId) => {
  const map = buildRecipientIdMap(distributions);
  const recipientIds = map[donationId] || [];

  // 去重处理（使用 Set 去除重复项）
  const uniqueRecipientIds = [...new Set(recipientIds)];

  if (uniqueRecipientIds.length === 0) {
    return "未分配";
  } else if (uniqueRecipientIds.length === 1) {
    return uniqueRecipientIds[0].toString();
  } else {
    return uniqueRecipientIds.join(",");
  }
};

const fetchMoneyRecords = async () => {
  if (!userId.value) return

  try {
    const response = await axios.get('/money-donations/page-i', {
      params: {
        page: moneyPage.current,
        size: moneyPage.size,
        userId: userId.value
      }
    })

    if (response.data.code === "200") {
      console.log(response.data.data)
      moneyRecords.value = moneyRecords.value = processData({
        page: response.data.data.page,
        distributions: response.data.data.distributions
      }).page.records
      console.log(moneyRecords.value)
      moneyPage.total = response.data.data.page.total
    }
  } catch (error) {
    ElMessage.error('获取资金捐赠记录失败')
  }
}

// 辅助函数
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

const formatStatus = (status) => {
  const statusMap = {
    pending: '待审核',
    approved: '已通过',
    rejected: '已拒绝'
  }
  return statusMap[status] || status
}

const getStatusTagType = (status) => {
  switch (status) {
    case 'approved': return 'success'
    case 'rejected': return 'danger'
    default: return 'warning'
  }
}

// 初始化
onMounted(() => {
  fetchItemRecords()
  fetchMoneyRecords()
})
</script>

<style scoped>
/* 基础样式 */
.donation-container {
  max-width: 900px;
  margin: 30px auto;
  padding: 30px;
  background: linear-gradient(135deg, #f9f9f7 0%, #f0f7f0 100%);
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(46, 139, 87, 0.15);
  position: relative;
  overflow: hidden;
}

.donation-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 5px;
  background: linear-gradient(90deg, #2E8B57, #FF7F50, #2E8B57);
  background-size: 200% 100%;
  animation: gradientBG 5s ease infinite;
}

@keyframes gradientBG {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

/* 标题样式 */
.header-section {
  text-align: center;
  margin-bottom: 30px;
  animation: fadeInDown 0.8s ease;
}

.page-title {
  color: #2E8B57;
  font-size: 28px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.title-text {
  position: relative;
}

.title-text::after {
  content: "";
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 100%;
  height: 3px;
  background: #FF7F50;
  transform: scaleX(0);
  transform-origin: right;
  transition: transform 0.5s ease;
}

.page-title:hover .title-text::after {
  transform: scaleX(1);
  transform-origin: left;
}

.title-icon {
  margin-left: 10px;
  animation: pulse 2s infinite;
}

.page-subtitle {
  color: #666;
  font-size: 16px;
  font-style: italic;
}

/* 标签页样式 */
:deep(.el-tabs__item) {
  font-size: 16px;
  color: #666;
  transition: all 0.3s ease;
  padding: 0 25px;
}

:deep(.el-tabs__item.is-active) {
  color: #2E8B57;
  font-weight: 500;
  transform: translateY(-2px);
}

:deep(.el-tabs__active-bar) {
  background-color: #2E8B57;
  height: 3px;
  border-radius: 2px;
}

.animated-tabs {
  transition: all 0.5s ease;
}

/* 卡片样式 */
.donation-card {
  margin-bottom: 20px;
  border-radius: 12px;
  border: none;
  overflow: hidden;
  transition: all 0.3s ease;
  position: relative;
  z-index: 1;
}

.donation-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(46, 139, 87, 0.2);
}

.donation-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(46, 139, 87, 0.1) 0%, rgba(255, 255, 255, 0) 100%);
  z-index: -1;
}

.animated-card {
  animation: fadeInUp 0.8s ease;
}

.donation-title {
  color: #2E8B57;
  font-size: 20px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  padding-bottom: 10px;
  border-bottom: 1px dashed #e0e0e0;
}

.icon-highlight {
  margin-right: 10px;
  font-size: 24px;
  color: #FF7F50;
  transition: all 0.3s ease;
}

.donation-title:hover .icon-highlight {
  transform: rotate(15deg) scale(1.1);
}

/* 表单样式 */
.form-container {
  margin-top: 20px;
  padding: 0 20px;
}

.unit {
  margin-left: 10px;
  color: #666;
}

.pagination {
  margin-top: 25px;
  justify-content: center;
}

.el-upload__tip {
  font-size: 13px;
  color: #888;
  margin-top: 8px;
}

/* 按钮样式 */
.submit-btn {
  background-color: #2E8B57;
  border-color: #2E8B57;
  padding: 10px 25px;
  border-radius: 20px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 6px rgba(46, 139, 87, 0.2);
}

.submit-btn:hover {
  background-color: #3CB371;
  border-color: #3CB371;
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(46, 139, 87, 0.3);
}

.reset-btn {
  padding: 10px 25px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.reset-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 感谢信息样式 */
.thank-you-message {
  background-color: #F0FFF0;
  border: 1px solid #98FB98;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 30px;
  text-align: center;
  color: #2E8B57;
  position: relative;
  overflow: hidden;
}

.thank-you-message h3 {
  font-size: 22px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.thank-you-message h3 i {
  margin-right: 10px;
  font-size: 28px;
}

.thank-you-message p {
  font-size: 16px;
  margin-bottom: 0;
}

.heart-animation {
  position: absolute;
  font-size: 24px;
  animation: float 3s ease-in-out infinite;
  opacity: 0.6;
}

.heart-animation:nth-child(1) {
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.heart-animation:nth-child(2) {
  top: 60%;
  left: 80%;
  animation-delay: 0.5s;
}

.heart-animation:nth-child(3) {
  top: 30%;
  left: 50%;
  animation-delay: 1s;
}

/* 浮动图标 */
.floating-icons {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  pointer-events: none;
  z-index: 0;
}

.floating-icons i {
  position: absolute;
  font-size: 24px;
  color: rgba(46, 139, 87, 0.1);
  animation: float 6s ease-in-out infinite;
}

.floating-icons i:nth-child(1) {
  top: 20%;
  left: 10%;
}

.floating-icons i:nth-child(2) {
  top: 60%;
  left: 80%;
  animation-delay: 1s;
}

.floating-icons i:nth-child(3) {
  top: 30%;
  left: 50%;
  animation-delay: 2s;
}

/* 页脚样式 */
.footer-section {
  text-align: center;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #e0e0e0;
  color: #666;
  font-size: 14px;
  animation: fadeIn 1s ease;
}

/* 动画定义 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

@keyframes float {
  0% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(10deg); }
  100% { transform: translateY(0) rotate(0deg); }
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.5s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .donation-container {
    padding: 20px;
    margin: 15px;
  }

  .page-title {
    font-size: 24px;
  }

  :deep(.el-tabs__item) {
    padding: 0 15px;
    font-size: 14px;
  }

  .form-container {
    padding: 0 10px;
  }
}
</style>