<template>
  <div class="user-register-container">
    <!-- 步骤条 -->
    <el-steps :active="step" finish-status="success" simple>
      <el-step title="选择角色"></el-step>
      <el-step title="填写信息"></el-step>
      <el-step title="完成"></el-step>
    </el-steps>

    <!-- 第一步：选择捐赠者/受助者 -->
    <div v-if="step === 1" class="step-content">
      <h2>请选择您的用户角色</h2>
      <div class="type-cards">
        <el-card
            class="type-card"
            :class="{ 'active': formData.user_type === 'donor' }"
            @click="formData.user_type = 'donor'"
        >
          <div class="card-content">
            <el-icon :size="60"><Opportunity /></el-icon>
            <h3>捐赠者</h3>
            <p>我要捐赠物资或资金</p>
          </div>
        </el-card>

        <el-card
            class="type-card"
            :class="{ 'active': formData.user_type === 'recipient' }"
            @click="formData.user_type = 'recipient'"
        >
          <div class="card-content">
            <el-icon :size="60"><User /></el-icon>
            <h3>受助者</h3>
            <p>我需要申请帮助</p>
          </div>
        </el-card>
      </div>

      <div class="action-buttons">
        <el-button
            type="primary"
            size="large"
            @click="goToStep(2)"
            :disabled="!formData.user_type"
        >
          下一步
        </el-button>
      </div>
    </div>

    <!-- 第二步：填写详细信息 -->
    <div v-if="step === 2" class="step-content">
      <h2>{{ formTitle }}</h2>

      <!-- 通用表单部分 -->
      <el-form ref="formRef" :model="formData" label-width="120px">
        <el-form-item label="用户名" prop="username" required>
          <el-input v-model="formData.username" />
        </el-form-item>

<!--        <el-form-item label="密码" prop="password" required>-->
<!--          <el-input v-model="formData.password" type="password" />-->
<!--        </el-form-item>-->

        <el-form-item label="手机号" prop="phone" required>
          <el-input v-model="formData.phone" />
        </el-form-item>

        <!-- 个人/企业选择 -->
        <el-form-item label="账户类型">
          <el-radio-group v-model="formData.is_enterprise">
            <el-radio :label="0">个人账户</el-radio>
            <el-radio :label="1">企业账户</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 实名信息 -->
        <el-form-item label="真实姓名" prop="real_name" required>
          <el-input v-model="formData.real_name" />
        </el-form-item>

        <el-form-item label="身份证号" prop="id_card_number" required>
          <el-input v-model="formData.id_card_number" />
        </el-form-item>

        <!-- 受助者特有字段 -->
        <template v-if="formData.user_type === 'recipient'">
          <el-form-item label="银行卡号" prop="bank_account" required>
            <el-input v-model="formData.bank_account" />
          </el-form-item>

          <el-form-item label="开户银行" prop="bank_name" required>
            <el-input v-model="formData.bank_name" />
          </el-form-item>

          <el-form-item label="需求说明">
            <el-upload
                action="/api/upload"
                :limit="1"
                :on-success="handleFileUpload"
            >
              <el-button type="primary">上传需求文件</el-button>
            </el-upload>
          </el-form-item>
        </template>

        <!-- 企业特有字段 -->
        <template v-if="formData.is_enterprise">
          <el-form-item label="企业名称" prop="enterprise_name" required>
            <el-input v-model="formData.enterprise_name" />
          </el-form-item>

          <el-form-item label="营业执照号" prop="business_license" required>
            <el-input v-model="formData.business_license" />
          </el-form-item>

          <el-form-item label="企业角色">
            <el-checkbox-group v-model="formData.enterprise_role">
              <el-checkbox label="can_donate" v-if="formData.user_type === 'donor'">可捐赠</el-checkbox>
              <el-checkbox label="can_receive" v-if="formData.user_type === 'recipient'">可受助</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </template>
      </el-form>

      <div class="action-buttons">
        <el-button @click="goToStep(1)">上一步</el-button>
        <el-button type="primary" @click="submitForm">提交注册</el-button>
      </div>
    </div>

    <!-- 第三步：注册完成 -->
    <div v-if="step === 3" class="step-content">
      <el-result icon="success" title="注册成功" subTitle="请等待管理员审核">
        <template #extra>
          <el-button type="primary" @click="goToHome">返回首页</el-button>
        </template>
      </el-result>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Opportunity, User } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import axios from "axios";

const router = useRouter()
const step = ref(1)
const formRef = ref(null)

// 表单数据 - 结合您原来的数据表结构
const formData = ref({
  userId: '',
  user_type: '', // 'donor' 或 'recipient'
  is_enterprise: 0, // 0-个人, 1-企业
  username: '',
  password: '',
  phone: '',
  real_name: '',
  id_card_number: '',
  bank_account: '',
  bank_name: '',
  needs_description_file: '',
  enterprise_name: '',
  business_license: '',
  enterprise_role: [],
  is_admin: 0,
  user_status: 'pending'
})
let userInfos = ref({

})

const formTitle = computed(() => {
  const typeMap = {
    donor: '捐赠者',
    recipient: '受助者'
  }
  const enterpriseText = formData.value.is_enterprise ? '企业' : '个人'
  return `${enterpriseText}${typeMap[formData.value.user_type]}注册`
})

const goToStep = (stepNum) => {
  step.value = stepNum
}

const handleFileUpload = (response) => {
  formData.value.needs_description_file = response.fileUrl
}
const getInfo = () => {
  axios.get(`/users/${JSON.parse(localStorage.getItem("user")).userId}`).then(res => {
    formData.value.username = res.data.data.username
    formData.value.phone = res.data.data.phone
    formData.value.userId =res.data.data.userId
    userInfos = res.data.data
  })
}
getInfo()

const submitForm = async () => {
    // 验证表单
    await formRef.value.validate()

    // 处理企业角色数据
    if (formData.value.is_enterprise) {
      formData.value.enterprise_role = formData.value.enterprise_role.join(',')
    }
  console.log(formData.value)
    axios.post('/user-profiles',formData.value).then(res => {
      console.log(res)
    }).catch(res => {
      console.log(res)
    })

    // 调用注册API
    // await registerUser(formData.value)

    // 跳转到完成页
    step.value = 3
}

const goToHome = () => {
  router.push('/')
}

</script>

<style scoped>
.user-register-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.step-content {
  margin-top: 30px;
}

.type-cards {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin: 40px 0;
}

.type-card {
  width: 300px;
  cursor: pointer;
  transition: all 0.3s;
}

.type-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.type-card.active {
  border-color: var(--el-color-primary);
  background-color: rgba(64, 158, 255, 0.05);
}

.card-content {
  padding: 30px;
  text-align: center;
}

.card-content h3 {
  margin: 15px 0 10px;
}

.action-buttons {
  margin-top: 30px;
  text-align: center;
}

.el-form {
  margin-top: 30px;
}
</style>