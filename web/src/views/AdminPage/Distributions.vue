<template>
  <div class="charity-container">
    <!-- 统计卡片区域 -->
    <div class="stat-cards">
      <el-card shadow="hover" class="stat-card" v-for="stat in stats" :key="stat.title">
        <div class="stat-content">
          <div class="stat-icon" :style="{backgroundColor: stat.color}">
            <i :class="stat.icon"></i>
          </div>
          <div class="stat-info">
            <h3>{{ stat.title }}</h3>
            <p class="stat-value">{{ stat.value }}</p>
            <p class="stat-desc">{{ stat.desc }}</p>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 主卡片区域 -->
    <el-card class="main-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h2>分发记录管理</h2>
          <div class="header-actions">
            <el-input
                v-model="searchQuery"
                placeholder="搜索接收者ID"
                class="search-input"
                clearable
                @clear="handleSearchClear"
                @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-date-picker
                v-model="dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @change="handleDateChange"
                class="date-picker"
            />
            <el-button type="primary" size="small" @click="openDialog()" class="add-btn">
              <el-icon><Plus /></el-icon>新增分发
            </el-button>
          </div>
        </div>
      </template>

      <!-- 数据表格 -->
      <el-table
          :data="distributions"
          style="width: 100%"
          stripe
          v-loading="loading"
          @sort-change="handleSortChange"
      >
        <el-table-column prop="distributionId" label="ID" width="80" sortable />
        <el-table-column prop="activityId" label="活动ID" width="100" sortable />
        <el-table-column prop="recipientId" label="接收者ID" width="120" sortable />
        <el-table-column prop="donationType" label="捐赠类型" width="120">
          <template #default="{row}">
            <el-tag :type="row.donationType === 'item' ? 'success' : 'warning'">
              {{ row.donationType === 'item' ? '物品捐赠' : '资金捐赠' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="itemDonationId" label="物品ID" width="100" v-if="showItemColumn" />
        <el-table-column prop="moneyDonationId" label="资金ID" width="100" v-if="showMoneyColumn" />
        <el-table-column prop="distributedQuantity" label="分发数量" width="120" v-if="showItemColumn">
          <template #default="{row}">
            <span v-if="row.distributedQuantity">{{ row.distributedQuantity }}件</span>
          </template>
        </el-table-column>
        <el-table-column prop="distributedAmount" label="分发金额" width="120" v-if="showMoneyColumn">
          <template #default="{row}">
            <span v-if="row.distributedAmount">¥{{ row.distributedAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="distributionTime" label="分发时间" width="180" sortable>
          <template #default="{row}">
            {{ formatDate(row.distributionTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
                type="primary"
                size="small"
                @click="openDialog(row)"
                circle
                title="编辑"
            >
              <el-icon><Edit /></el-icon>
            </el-button>
            <el-button
                type="danger"
                size="small"
                @click="remove(row.distributionId)"
                circle
                title="删除"
            >
              <el-icon><Delete /></el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            v-model:page-size="size"
            v-model:current-page="currentPage"
            :page-sizes="[5, 10, 20, 50]"
            @size-change="handleSizeChange"
            @current-change="fetchDistributions"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
        :title="isEdit ? '编辑分发记录' : '新增分发记录'"
        v-model="dialogVisible"
        width="700px"
        :close-on-click-modal="false"
        @closed="resetForm"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" label-position="left">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="活动ID" prop="activityId">
              <el-input v-model="form.activityId" type="number" placeholder="请输入活动ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="接收者ID" prop="recipientId">
              <el-input v-model="form.recipientId" type="number" placeholder="请输入接收者ID" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="捐赠类型" prop="donationType">
          <el-select
              v-model="form.donationType"
              placeholder="请选择捐赠类型"
              @change="onTypeChange"
              class="type-selector"
          >
            <el-option label="物品捐赠" value="item">
              <div class="option-content">
                <el-icon class="option-icon"><Box /></el-icon>
                <span>物品捐赠</span>
              </div>
            </el-option>
            <el-option label="资金捐赠" value="money">
              <div class="option-content">
                <el-icon class="option-icon"><Money /></el-icon>
                <span>资金捐赠</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>

        <!-- 物品捐赠部分 -->
        <div v-if="form.donationType==='item'" class="donation-section">
          <el-form-item label="物品来源" prop="itemDonationId">
            <el-select
                v-model="form.itemDonationId"
                placeholder="请选择物品来源"
                @change="onItemSelect"
                filterable
                class="donation-select"
            >
              <el-option
                  v-for="item in itemDonations"
                  :key="item.donationId"
                  :label="`${item.itemType}（剩余: ${item.quantity}件）`"
                  :value="item.donationId"
              >
                <div class="item-option">
                  <span class="item-type">{{ item.itemType }}</span>
                  <span class="item-quantity">剩余: {{ item.quantity }}件</span>
                </div>
              </el-option>
            </el-select>
            <el-pagination
                layout="prev, pager, next"
                :current-page="itemPage"
                :page-size="itemSize"
                :total="itemTotal"
                @current-change="fetchItemDonations"
                class="select-pagination"
                small
            />
          </el-form-item>

          <el-form-item label="分发数量" prop="distributedQuantity">
            <el-input-number
                v-model="form.distributedQuantity"
                :min="1"
                :max="maxItemQuantity"
                controls-position="right"
                placeholder="请输入分发数量"
            />
            <span class="quantity-tip" v-if="selectedItem">
              最多可分发 {{ selectedItem.quantity }} 件
            </span>
          </el-form-item>
        </div>

        <!-- 资金捐赠部分 -->
        <div v-if="form.donationType==='money'" class="donation-section">
          <el-form-item label="资金来源" prop="moneyDonationId">
            <el-select
                v-model="form.moneyDonationId"
                placeholder="请选择资金来源"
                @change="onMoneySelect"
                filterable
                class="donation-select"
            >
              <el-option
                  v-for="money in moneyDonations"
                  :key="money.donationId"
                  :label="`¥${money.amount}（${money.donorName || '匿名'}捐赠）`"
                  :value="money.donationId"
              >
                <div class="money-option">
                  <span class="money-amount">¥{{ money.amount }}</span>
                  <span class="money-donor">{{ money.donorName || '匿名' }}捐赠</span>
                </div>
              </el-option>
            </el-select>
            <el-pagination
                layout="prev, pager, next"
                :current-page="moneyPage"
                :page-size="moneySize"
                :total="moneyTotal"
                @current-change="fetchMoneyDonations"
                class="select-pagination"
                small
            />
          </el-form-item>

          <el-form-item label="分发金额" prop="distributedAmount">
            <el-input
                v-model="form.distributedAmount"
                type="number"
                :disabled="!form.moneyDonationId"
                placeholder="自动填充金额"
            >
              <template #prefix>
                <span class="currency-symbol">¥</span>
              </template>
            </el-input>
          </el-form-item>
        </div>

        <el-form-item label="分发时间" prop="distributionTime">
          <el-date-picker
              v-model="form.distributionTime"
              type="datetime"
              placeholder="选择分发时间"
              value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">
          {{ isEdit ? '更新' : '创建' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import {
  Search, Plus, Edit, Delete, Box, Money
} from '@element-plus/icons-vue';
import {ElMessage, ElMessageBox} from "element-plus";

// 数据状态
const distributions = ref([]);
const total = ref(0);
const size = ref(10);
const currentPage = ref(1);
const loading = ref(false);
const submitting = ref(false);

// 搜索相关
const searchQuery = ref('');
const dateRange = ref([]);
const sortParams = ref({});

const stats = ref([
  { title: '总分发记录', value: '0', desc: '', icon: 'el-icon-document', color: '#409EFF' },
  { title: '物品分发', value: '0', desc: '', icon: 'el-icon-box', color: '#67C23A' },
  { title: '资金分发', value: '0', desc: '', icon: 'el-icon-money', color: '#E6A23C' },
  { title: '受益人数', value: '0', desc: '', icon: 'el-icon-user', color: '#F56C6C' }
]);
const fetchStatistics = async () => {
  try {
    const res = await axios.get('/distributions/statistics');
    const data = res.data?.data || {};

    // 更新 stats 的 value 和 desc
    stats.value[0].value = data.total_records?.toLocaleString() || '0';
    stats.value[0].desc = `同比增长${(data.growth_rate * 100).toFixed(1)}%`;

    stats.value[1].value = data.total_items?.toLocaleString() || '0';
    stats.value[1].desc = `共${data.total_items?.toLocaleString() || 0}件`;

    stats.value[2].value = data.total_amount ? `¥${Number(data.total_amount).toLocaleString()}` : '¥0';
    stats.value[2].desc = `共${stats.value[2].value}`;

    stats.value[3].value = data.total_recipients?.toLocaleString() || '0';
    stats.value[3].desc = `覆盖${data.covered_regions}个地区`;
  } catch (error) {
    console.error('获取统计数据失败', error);
  }
};


// 物品捐赠分页
const itemDonations = ref([]);
const itemPage = ref(1);
const itemSize = ref(5);
const itemTotal = ref(0);
const selectedItem = ref(null);

// 资金捐赠分页
const moneyDonations = ref([]);
const moneyPage = ref(1);
const moneySize = ref(5);
const moneyTotal = ref(0);
const selectedMoney = ref(null);

// 对话框相关
const dialogVisible = ref(false);
const isEdit = ref(false);
const formRef = ref(null);
const form = ref({
  distributionId: null,
  activityId: null,
  recipientId: null,
  donationType: '',
  itemDonationId: null,
  moneyDonationId: null,
  distributedQuantity: null,
  distributedAmount: null,
  distributionTime: new Date()
});

const rules = {
  activityId: [{ required: true, message: '请输入活动ID', trigger: 'blur' }],
  recipientId: [{ required: true, message: '请输入接收者ID', trigger: 'blur' }],
  donationType: [{ required: true, message: '请选择类型', trigger: 'change' }],
  itemDonationId: [{
    required: () => form.value.donationType==='item',
    message: '请选择物品来源',
    trigger: 'change'
  }],
  moneyDonationId: [{
    required: () => form.value.donationType==='money',
    message: '请选择资金来源',
    trigger: 'change'
  }],
  distributedQuantity: [{
    required: () => form.value.donationType==='item',
    message: '请输入分发数量',
    trigger: 'blur'
  }],
  distributedAmount: [{
    required: () => form.value.donationType==='money',
    message: '分发金额不能为空',
    trigger: 'blur'
  }],
  distributionTime: [{
    required: true,
    message: '请选择分发时间',
    trigger: 'change'
  }]
};

// 计算属性
const showItemColumn = computed(() => distributions.value.some(d => d.donationType === 'item'));
const showMoneyColumn = computed(() => distributions.value.some(d => d.donationType === 'money'));
const maxItemQuantity = computed(() => selectedItem.value ? selectedItem.value.quantity : Infinity);

// 方法
function formatDate(dateString) {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleString();
}

async function fetchDistributions(page = 1) {
  try {
    loading.value = true;
    currentPage.value = page;

    let url = `/distributions/page?page=${page}&size=${size.value}`;

    // 添加搜索条件
    if (searchQuery.value) {
      url += `&recipientId=${searchQuery.value}`;
    }

    // 添加日期范围条件
    if (dateRange.value && dateRange.value.length === 2) {
      const [start, end] = dateRange.value;
      url += `&startTime=${start}&endTime=${end}`;
    }

    // 添加排序条件
    if (sortParams.value.prop) {
      url += `&sortField=${sortParams.value.prop}&sortOrder=${sortParams.value.order}`;
    }

    const { data } = await axios.get(url);
    distributions.value = data.data.records;
    total.value = data.data.total;
  } catch (error) {
    console.error('获取分发记录失败:', error);
    ElMessage.error('获取分发记录失败');
  } finally {
    loading.value = false;
  }
}

async function fetchItemDonations(page = itemPage.value) {
  itemPage.value = page;
  const { data } = await axios.get(`/item-donations/page?page=${page}&size=${itemSize.value}`);
  itemDonations.value = data.data.records;
  itemTotal.value = data.data.total;
}

async function fetchMoneyDonations(page = moneyPage.value) {
  moneyPage.value = page;
  const { data } = await axios.get(`/money-donations/page?page=${page}&size=${moneySize.value}`);
  moneyDonations.value = data.data.records;
  moneyTotal.value = data.data.total;
}

function openDialog(row) {
  if (row) {
    isEdit.value = true;
    form.value = { ...row };
  } else {
    isEdit.value = false;
    form.value = {
      distributionId: null,
      activityId: null,
      recipientId: null,
      donationType: '',
      itemDonationId: null,
      moneyDonationId: null,
      distributedQuantity: null,
      distributedAmount: null,
      distributionTime: new Date()
    };
  }
  dialogVisible.value = true;
}

function onTypeChange() {
  form.value.itemDonationId = null;
  form.value.moneyDonationId = null;
  form.value.distributedQuantity = null;
  form.value.distributedAmount = null;
  selectedItem.value = null;
  selectedMoney.value = null;
}

function onMoneySelect(selectedId) {
  selectedMoney.value = moneyDonations.value.find(m => m.donationId === selectedId);
  form.value.distributedAmount = selectedMoney.value ? selectedMoney.value.amount : null;
}

function onItemSelect(selectedId) {
  selectedItem.value = itemDonations.value.find(i => i.donationId === selectedId);
  if (selectedItem.value && !form.value.distributedQuantity) {
    form.value.distributedQuantity = 1;
  }
}

async function submitForm() {
  try {
    submitting.value = true;
    await formRef.value.validate();
    form.value.distributionTime = new Date(form.value.distributionTime)

    if (isEdit.value) {
      await axios.put('/distributions', form.value);
      if (form.value.itemDonationId !== null){
        axios.get(`/item-donations/${form.value.itemDonationId}`).then(res => {
          res.data.data.status = "distributed"
          console.log(res)
          axios.put('/item-donations',res.data.data).catch(res => {
            ElMessage.error("发现了错误，请查看控制台")
            console.log(res)
          })
        })
      }
      if (form.value.moneyDonationId !== null){
        axios.get(`/money-donations/${form.value.moneyDonationId}`).then(res => {
          res.data.data.status = "distributed"
          console.log(res)
          axios.put('/money-donations',res.data.data).catch(res => {
            ElMessage.error("发现了错误，请查看控制台")
            console.log(res)
          })
        })
      }

      ElMessage.success('更新成功');
    } else {
      await axios.post('/distributions', form.value).then(res => {
        console.log(res)
        console.log(form.value)
      })
      ElMessage.success('创建成功');
    }

    dialogVisible.value = false;
    await fetchDistributions(currentPage.value);
  } catch (error) {
    if (error.response) {
      ElMessage.error(`操作失败: ${error.response.data.message || '未知错误'}`);
    } else if (!error.message.includes('validate')) {
      ElMessage.error('操作失败');
    }
  } finally {
    submitting.value = false;
  }
}

async function remove(id) {
  try {
    await ElMessageBox.confirm('确定要删除这条分发记录吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });

    await axios.delete(`/distributions/${id}`);
    ElMessage.success('删除成功');
    await fetchDistributions(currentPage.value);
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败');
    }
  }
}

function resetForm() {
  formRef.value?.resetFields();
}

function handleSizeChange(newSize) {
  size.value = newSize;
  fetchDistributions(1);
}

function handleSearch() {
  fetchDistributions(1);
}

function handleSearchClear() {
  searchQuery.value = '';
  fetchDistributions(1);
}

function handleDateChange() {
  fetchDistributions(1);
}

function handleSortChange({ prop, order }) {
  sortParams.value = { prop, order };
  fetchDistributions(1);
}

onMounted(() => {
  fetchDistributions();
  fetchItemDonations();
  fetchMoneyDonations();
  fetchStatistics();
});
</script>

<style scoped>
.charity-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.stat-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 10px;
  transition: transform 0.3s, box-shadow 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 15px;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  color: white;
  font-size: 24px;
}

.stat-info h3 {
  margin: 0;
  font-size: 16px;
  color: #666;
}

.stat-value {
  margin: 5px 0;
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.stat-desc {
  margin: 0;
  font-size: 12px;
  color: #999;
}

.main-card {
  border-radius: 10px;
  transition: all 0.3s;
}

.main-card:hover {
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
}

.card-header h2 {
  margin: 0;
  color: #333;
  font-size: 20px;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.search-input {
  width: 200px;
}

.date-picker {
  width: 250px;
}

.add-btn {
  background-color: #ff6b6b;
  border-color: #ff6b6b;
}

.add-btn:hover {
  background-color: #ff5252;
  border-color: #ff5252;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.donation-section {
  background-color: #f9f9f9;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 15px;
}

.type-selector {
  width: 100%;
}

.donation-select {
  width: 100%;
}

.select-pagination {
  margin-top: 10px;
  justify-content: center;
}

.quantity-tip {
  margin-left: 10px;
  font-size: 12px;
  color: #666;
}

.currency-symbol {
  color: #e6a23c;
}

.option-content {
  display: flex;
  align-items: center;
}

.option-icon {
  margin-right: 8px;
  font-size: 16px;
}

.item-option, .money-option {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.item-type, .money-amount {
  font-weight: bold;
}

.item-quantity, .money-donor {
  color: #999;
  font-size: 12px;
}

/* 动画效果 */
.el-table__row {
  transition: all 0.3s;
}

.el-table__row:hover {
  transform: translateX(5px);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .header-actions {
    width: 100%;
  }

  .search-input, .date-picker {
    width: 100%;
  }

  .stat-cards {
    grid-template-columns: 1fr 1fr;
  }
}

@media (max-width: 480px) {
  .stat-cards {
    grid-template-columns: 1fr;
  }

  .el-table__body-wrapper {
    overflow-x: auto;
  }
}
</style>