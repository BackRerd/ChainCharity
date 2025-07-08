<template>
  <div class="user-profile-container">
    <el-card class="profile-card">
      <!-- 用户类型选择 -->
      <div v-if="!currentUser.user_type" class="user-type-selector">
        <h2>请选择您的用户类型</h2>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card shadow="hover" @click="selectUserType('donor')">
              <div class="type-card">
                <el-icon :size="50"><User /></el-icon>
                <h3>捐赠者</h3>
                <p>我想要捐赠物资帮助他人</p>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" @click="selectUserType('recipient')">
              <div class="type-card">
                <el-icon :size="50"><HelpFilled /></el-icon>
                <h3>受助者</h3>
                <p>我需要物资帮助</p>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" @click="selectUserType('enterprise')">
              <div class="type-card">
                <el-icon :size="50"><OfficeBuilding /></el-icon>
                <h3>企业</h3>
                <p>企业捐赠或申请物资</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 基本信息表单 -->
      <div v-else>
        <div class="profile-header">
          <h2>个人信息</h2>
          <el-tag :type="getStatusTagType(currentUser.user_status)">
            {{ currentUser.user_status === 'active' ? '已激活' :
              currentUser.user_status === 'suspended' ? '已停用' : '待审核' }}
          </el-tag>
          <el-tag v-if="currentUser.is_admin" type="danger">管理员</el-tag>
        </div>

        <el-form
            ref="profileForm"
            :model="formData"
            :rules="rules"
            label-width="120px"
            label-position="left"
        >
          <!-- 通用信息 -->
          <el-divider content-position="left">基本信息</el-divider>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="currentUser.username"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="currentUser.phone" disabled />
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 头像上传 -->
          <el-form-item label="头像">
            <el-upload
                class="avatar-uploader"
                :action="uploadImageUrl"
                :headers="uploadHeaders"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
            >
              <img v-if="formData.avatar" :src="getFullImageUrl(formData.avatar)" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>

          <!-- 实名信息 -->
          <el-divider content-position="left">实名信息</el-divider>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="真实姓名" prop="real_name">
                <el-input v-model="formData.real_name" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="身份证号" prop="id_card_number">
                <el-input v-model="formData.id_card_number" />
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 个人用户信息 -->
          <template v-if="currentUser.user_type === 'donor' || currentUser.user_type === 'recipient'">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="性别" prop="gender">
                  <el-radio-group v-model="formData.gender">
                    <el-radio label="male">男</el-radio>
                    <el-radio label="female">女</el-radio>
                    <el-radio label="other">其他</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="出生日期" prop="birth_date">
                  <el-date-picker
                      v-model="formData.birth_date"
                      type="date"
                      placeholder="选择日期"
                      value-format="YYYY-MM-DD"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="地址" prop="address">
              <el-input v-model="formData.address" />
            </el-form-item>
          </template>

          <!-- 受助者专属信息 -->
          <template v-if="currentUser.user_type === 'recipient'">
            <el-divider content-position="left">受助信息</el-divider>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="银行卡号" prop="bank_account">
                  <el-input v-model="formData.bank_account" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="开户银行" prop="bank_name">
                  <el-input v-model="formData.bank_name" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="家庭成员数" prop="family_members">
                  <el-input-number v-model="formData.family_members" :min="1" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="月收入" prop="monthly_income">
                  <el-input-number v-model="formData.monthly_income" :min="0" :precision="2" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="贫困等级" prop="poverty_level">
              <el-select v-model="formData.poverty_level" placeholder="请选择">
                <el-option label="低" value="low" />
                <el-option label="中" value="medium" />
                <el-option label="高" value="high" />
                <el-option label="极度" value="extreme" />
              </el-select>
            </el-form-item>
            <el-form-item label="需求说明文件" prop="needs_description_file">
              <el-upload
                  :action="uploadFileUrl"
                  :headers="uploadHeaders"
                  :limit="1"
                  :on-success="handleFileUpload"
              >
                <el-button type="primary">点击上传</el-button>
                <template #tip>
                  <div class="el-upload__tip">请上传您的需求说明文件(PDF格式)</div>
                </template>
              </el-upload>
              <div v-if="formData.needs_description_file" class="file-link">
                <a :href="getFullFileUrl(formData.needs_description_file)" target="_blank">查看已上传文件</a>
              </div>
            </el-form-item>
          </template>

          <!-- 企业信息 -->
          <template v-if="currentUser.is_enterprise">
            <el-divider content-position="left">企业信息</el-divider>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="企业名称" prop="real_name">
                  <el-input v-model="formData.real_name" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="统一信用代码" prop="id_card_number">
                  <el-input v-model="formData.id_card_number" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="法人代表" prop="legal_representative">
                  <el-input v-model="formData.legal_representative" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="营业执照号" prop="business_license">
                  <el-input v-model="formData.business_license" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="企业类型" prop="enterprise_type">
                  <el-select v-model="formData.enterprise_type" placeholder="请选择">
                    <el-option label="私营" value="private" />
                    <el-option label="国有" value="state-owned" />
                    <el-option label="外资" value="foreign" />
                    <el-option label="合资" value="joint-venture" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="所属行业" prop="industry">
                  <el-input v-model="formData.industry" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="联系人" prop="contact_person">
                  <el-input v-model="formData.contact_person" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="contact_phone">
                  <el-input v-model="formData.contact_phone" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="企业角色" prop="enterprise_role">
              <el-checkbox-group v-model="formData.enterprise_role">
                <el-checkbox label="can_donate">可捐赠</el-checkbox>
                <el-checkbox label="can_receive">可受助</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </template>

          <!-- 认证状态 -->
          <el-divider content-position="left">认证状态</el-divider>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="实名认证">
                <el-tag :type="formData.id_verified ? 'success' : 'warning'">
                  {{ formData.id_verified ? '已认证' : '未认证' }}
                </el-tag>
              </el-form-item>
            </el-col>
            <el-col v-if="currentUser.user_type === 'recipient'" :span="8">
              <el-form-item label="银行卡认证">
                <el-tag :type="formData.bank_verified ? 'success' : 'warning'">
                  {{ formData.bank_verified ? '已认证' : '未认证' }}
                </el-tag>
              </el-form-item>
            </el-col>
            <el-col v-if="currentUser.is_enterprise" :span="8">
              <el-form-item label="企业认证">
                <el-tag :type="formData.enterprise_verified ? 'success' : 'warning'">
                  {{ formData.enterprise_verified ? '已认证' : '未认证' }}
                </el-tag>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item>
            <el-button type="primary" @click="submitForm">保存修改</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { User, HelpFilled, OfficeBuilding, Plus } from '@element-plus/icons-vue'
import axios from 'axios'

const profileForm = ref(null)
const currentUser = ref({
  user_id: null,
  username: 'aaa',
  phone: 'aaaaad',
  is_admin: 0,
  user_status: 'pending',
  user_type: '',
  is_enterprise: 0,
  profile: null
})

const formData = reactive({
  // 通用字段
  profile_id: null,
  real_name: '',
  id_card_number: '',
  avatar: '',
  id_verified: 0,
  bank_verified: 0,
  enterprise_verified: 0,

  // 个人用户字段
  gender: '',
  birth_date: '',
  address: '',

  // 受助者字段
  bank_account: '',
  bank_name: '',
  needs_description_file: '',
  family_members: 0,
  monthly_income: 0,
  poverty_level: '',

  // 企业字段
  legal_representative: '',
  business_license: '',
  enterprise_type: '',
  industry: '',
  contact_person: '',
  contact_phone: '',
  enterprise_role: []
})


// 表单验证规则
const rules = {
  real_name: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  id_card_number: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }],
  bank_account: [{ required: true, message: '请输入银行卡号', trigger: 'blur' }],
  bank_name: [{ required: true, message: '请输入开户银行', trigger: 'blur' }],
  legal_representative: [{ required: true, message: '请输入法人代表', trigger: 'blur' }],
  business_license: [{ required: true, message: '请输入营业执照号', trigger: 'blur' }]
}

// 计算上传URL和请求头
const uploadImageUrl = computed(() => '/api/upload/image')
const uploadFileUrl = computed(() => '/api/upload/file')
const uploadHeaders = computed(() => {
  const token = localStorage.getItem('token')
  return {
    Authorization: `Bearer ${token}`
  }
})

// 获取完整的图片URL
const getFullImageUrl = (path) => {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return `/api/images/${path}`
}

// 获取完整的文件URL
const getFullFileUrl = (path) => {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return `/api/files/${path}`
}

// 选择用户类型
const selectUserType = async (type) => {
  try {
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user.userId
    currentUser.value.user_type = type
    currentUser.value.is_enterprise = type === 'enterprise' ? 1 : 0
    currentUser.value.user_id = userId
    currentUser.value.username = user.username
    currentUser.value.phone = user.phone

    console.log(currentUser)

    // 更新用户类型到后端
    await axios.put('/users', {
      userId: userId,
      userType: type,
      isEnterprise: type === 'enterprise' ? 1 : 0
    })

    // 初始化表单数据
    initFormData()
    ElMessage.success('用户类型设置成功')
  } catch (error) {
    console.error('设置用户类型失败:', error)
    ElMessage.error('设置用户类型失败')
  }
}

// 初始化表单数据
const initFormData = () => {
  // 重置表单数据
  Object.keys(formData).forEach(key => {
    if (Array.isArray(formData[key])) {
      formData[key] = []
    } else if (typeof formData[key] === 'number') {
      formData[key] = 0
    } else {
      formData[key] = ''
    }
  })

  // 设置用户类型
  formData.user_type = currentUser.value.user_type

  // 如果是企业用户，初始化企业角色
  if (currentUser.value.is_enterprise) {
    formData.enterprise_role = ['can_donate']
  }
}

// 加载用户数据
const loadUserData = async () => {
  try {
    // 获取用户基本信息
    const user = JSON.parse(localStorage.getItem('user'))
    if (user) {
      currentUser.value = user
      console.log(currentUser)

      // 如果用户已经有类型，初始化表单数据
      if (user.user_type) {
        initFormData()
      }

      // 获取用户档案信息
      if (user.user_id) {
        const response = await axios.get(`/user-profiles/${user.user_id}`)
        console.log(response)
        if (response.data.code === '200' && response.data.data) {

          Object.assign(formData, response.data.data)
        }
      }
    }
  } catch (error) {
    ElMessage.error('加载用户数据失败')
    console.error(error)
  }
}

// 头像上传处理
const handleAvatarSuccess = async (response, file) => {
  try {
    const userId = JSON.parse(localStorage.getItem('user')).userId
    formData.avatar = response // 假设返回的是图片路径
    ElMessage.success('头像上传成功')
  } catch (error) {
    ElMessage.error('头像上传失败')
  }
}

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('头像图片只能是 JPG/PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 文件上传处理
const handleFileUpload = async (response, file) => {
  try {
    formData.needs_description_file = response // 假设返回的是文件路径
    ElMessage.success('文件上传成功')
  } catch (error) {
    ElMessage.error('文件上传失败')
  }
}

// 提交表单
const submitForm = async () => {
  try {
    await profileForm.value.validate()

    const userId = JSON.parse(localStorage.getItem('user')).userId
    const data = {
      ...formData,
      userId: userId,
      profileId: formData.profile_id || null
    }

    if (formData.profile_id) {
      // 更新现有档案
      await axios.put('/user-profiles', data)
      ElMessage.success('档案更新成功')
    } else {
      // 创建新档案
      const response = await axios.post('/user-profiles', data)
      formData.profile_id = response.data.data.profileId
      ElMessage.success('档案创建成功')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('保存失败: ' + (error.response?.data?.msg || error.message))
  }
}

// 重置表单
const resetForm = () => {
  if (formData.profile_id) {
    // 如果有profile_id，重新加载原始数据
    loadUserData()
  } else {
    // 否则重置为初始状态
    initFormData()
  }
}

// 获取状态标签类型
const getStatusTagType = (status) => {
  return status === 'active' ? 'success' :
      status === 'suspended' ? 'danger' : 'warning'
}

onMounted(() => {
  loadUserData()
})
</script>

<style scoped>
.user-profile-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.profile-card {
  margin-top: 20px;
}

.profile-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.profile-header h2 {
  margin-right: 15px;
  margin-bottom: 0;
}

.type-card {
  text-align: center;
  padding: 20px;
  cursor: pointer;
}

.type-card:hover {
  color: #409EFF;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 120px;
  height: 120px;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}

.avatar {
  width: 120px;
  height: 120px;
  display: block;
}

.file-link {
  margin-top: 10px;
}

.file-link a {
  color: #409EFF;
  text-decoration: none;
}

.file-link a:hover {
  text-decoration: underline;
}
</style>