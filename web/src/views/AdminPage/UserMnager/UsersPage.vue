<template>
  <div class="user-profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <el-button
              v-if="!hasProfile"
              type="primary"
              size="small"
              @click="submitUserType"
          >
            确认身份
          </el-button>
          <el-button
              v-else
              type="primary"
              size="small"
              @click="saveProfile"
              :loading="saving"
          >
            保存信息
          </el-button>
        </div>
      </template>

      <!-- 基本信息 -->
      <div class="section">
        <h3>基本信息</h3>
        <el-form :model="userForm" label-width="120px">
          <el-form-item label="用户名">
            <el-input v-model="userForm.username" disabled></el-input>
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="userForm.phone"></el-input>
          </el-form-item>
          <el-form-item label="用户状态">
            <el-tag :type="statusTagType">
              {{ userForm.userStatus }}
            </el-tag>
          </el-form-item>
        </el-form>
      </div>

      <!-- 身份选择（仅在没有档案时显示） -->
      <div class="section" v-if="!hasProfile">
        <h3>请选择您的身份</h3>
        <el-radio-group v-model="selectedUserType">
          <el-radio label="donor">捐赠者</el-radio>
          <el-radio label="recipient">受助者</el-radio>
          <el-radio label="enterprise" v-if="user.isEnterprise">企业用户</el-radio>
        </el-radio-group>
      </div>

      <!-- 档案信息（在有档案时显示） -->
      <div class="section" v-else>
        <h3>档案信息</h3>

        <!-- 头像上传 -->
        <el-form :model="profileForm" label-width="120px">
          <el-form-item label="头像">
            <el-upload
                class="avatar-uploader"
                action="#"
                :show-file-list="false"
                :auto-upload="false"
                :on-change="handleAvatarChange"
            >
              <img v-if="avatarUrl" :src="avatarUrl" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>

          <!-- 通用信息 -->
          <el-form-item label="真实姓名" prop="realName">
            <el-input v-model="profileForm.realName"></el-input>
          </el-form-item>

          <el-form-item label="证件号码" prop="idCardNumber">
            <el-input v-model="profileForm.idCardNumber"></el-input>
          </el-form-item>

          <!-- 个人用户信息 -->
          <template v-if="profileForm.userType !== 'enterprise'">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="profileForm.gender">
                <el-radio label="male">男</el-radio>
                <el-radio label="female">女</el-radio>
                <el-radio label="other">其他</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker
                  v-model="profileForm.birthDate"
                  type="date"
                  placeholder="选择日期"
                  value-format="YYYY-MM-DD"
              />
            </el-form-item>

            <el-form-item label="地址" prop="address">
              <el-input v-model="profileForm.address"></el-input>
            </el-form-item>
          </template>

          <!-- 受助者专属字段 -->
          <template v-if="profileForm.userType === 'recipient'">
            <el-form-item label="银行卡号" prop="bankAccount">
              <el-input v-model="profileForm.bankAccount"></el-input>
            </el-form-item>

            <el-form-item label="开户银行" prop="bankName">
              <el-input v-model="profileForm.bankName"></el-input>
            </el-form-item>

            <el-form-item label="家庭成员数" prop="familyMembers">
              <el-input-number v-model="profileForm.familyMembers" :min="1" />
            </el-form-item>

            <el-form-item label="月收入" prop="monthlyIncome">
              <el-input-number
                  v-model="profileForm.monthlyIncome"
                  :precision="2"
                  :step="0.1"
                  :min="0"
              />
              <span style="margin-left: 8px">元</span>
            </el-form-item>

            <el-form-item label="贫困等级" prop="povertyLevel">
              <el-select v-model="profileForm.povertyLevel" placeholder="请选择">
                <el-option label="低" value="low"></el-option>
                <el-option label="中" value="medium"></el-option>
                <el-option label="高" value="high"></el-option>
                <el-option label="极端" value="extreme"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="需求说明文件">
              <file-uploader
                  ref="needsFileUploader"
                  :max-file-size="10 * 1024 * 1024"
                  @file-uploaded="handleNeedsFileUploaded"
              />
              <el-button
                  v-if="profileForm.needs_description_file"
                  type="text"
                  @click="downloadNeedsFile"
              >
                下载当前文件
              </el-button>
            </el-form-item>
          </template>

          <!-- 企业专属字段 -->
          <template v-if="profileForm.userType === 'enterprise'">
            <el-form-item label="法人代表" prop="legalRepresentative">
              <el-input v-model="profileForm.legalRepresentative"></el-input>
            </el-form-item>

            <el-form-item label="营业执照号" prop="businessLicense">
              <el-input v-model="profileForm.businessLicense"></el-input>
            </el-form-item>

            <el-form-item label="企业类型" prop="enterpriseType">
              <el-select v-model="profileForm.enterpriseType" placeholder="请选择">
                <el-option label="私营" value="private"></el-option>
                <el-option label="国有" value="state-owned"></el-option>
                <el-option label="外资" value="foreign"></el-option>
                <el-option label="合资" value="joint-venture"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="所属行业" prop="industry">
              <el-input v-model="profileForm.industry"></el-input>
            </el-form-item>

            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="profileForm.contactPerson"></el-input>
            </el-form-item>

            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="profileForm.contactPhone"></el-input>
            </el-form-item>

            <el-form-item label="企业角色" prop="enterpriseRole">
              <el-checkbox-group v-model="profileForm.enterpriseRole">
                <el-checkbox label="can_donate">可捐赠</el-checkbox>
                <el-checkbox label="can_receive">可受助</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </template>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import axios from 'axios';
import fileUploader from '@/utils/fileUploader.js';

export default {
  components: {
    Plus,
    fileUploader
  },
  setup() {
    const userId = JSON.parse(localStorage.getItem("user")).userId;
    const user = ref({
      userId: userId,
      username: '',
      phone: '',
      userStatus: '',
      isEnterprise: false
    });

    const profileForm = ref({
      userType: '',
      realName: '',
      idCardNumber: '',
      avatar: '',
      gender: '',
      birthDate: '',
      address: '',
      bankAccount: '',
      bankName: '',
      needs_description_file: '',
      familyMembers: 1,
      monthlyIncome: 0,
      povertyLevel: '',
      legalRepresentative: '',
      businessLicense: '',
      enterpriseType: '',
      industry: '',
      contactPerson: '',
      contactPhone: '',
      enterpriseRole: []
    });

    const userForm = ref({
      username: '',
      phone: '',
      userStatus: ''
    });

    const selectedUserType = ref('');
    const hasProfile = ref(false);
    const saving = ref(false);
    const avatarFile = ref(null);
    const avatarUrl = ref('');
    const needsFileUploader = ref(null);

    // 计算用户状态标签类型
    const statusTagType = computed(() => {
      switch(user.value.userStatus) {
        case 'active': return 'success';
        case 'pending': return 'warning';
        case 'suspended': return 'danger';
        default: return 'info';
      }
    });

    // 加载用户数据
    const loadUserData = async () => {
      try {
        // 获取用户基本信息
        const userResponse = await axios.get(`/users/${userId}`);
        user.value = userResponse.data.data;
        userForm.value = {
          username: userResponse.data.username,
          phone: userResponse.data.phone,
          userStatus: userResponse.data.userStatus
        };

        // 尝试获取用户档案
        try {
          const profileResponse = await axios.get(`/user-profiles/${userId}`);
          profileForm.value = profileResponse.data;
          hasProfile.value = true;

          // 设置头像URL
          if (profileForm.value.avatar) {
            avatarUrl.value = fileUploader.getImageUrl(profileForm.value.avatar, userId);
          }
        } catch (error) {
          if (error.response && error.response.status === 404) {
            hasProfile.value = false;
          } else {
            throw error;
          }
        }
      } catch (error) {
        ElMessage.error('加载用户信息失败: ' + (error.response?.data?.message || error.message));
        console.error(error);
      }
    };

    // 提交身份选择
    const submitUserType = async () => {
      if (!selectedUserType.value) {
        ElMessage.warning('请选择您的身份');
        return;
      }

      try {
        saving.value = true;

        // 创建用户档案
        const newProfile = {
          userId: userId,
          userType: selectedUserType.value,
          // 设置一些默认值
          realName: '',
          idCardNumber: '',
          avatar: '',
          // 如果是企业用户，初始化企业相关字段
          ...(selectedUserType.value === 'enterprise' && {
            enterpriseRole: ['can_donate']
          })
        };

        const response = await axios.post('/user-profiles', newProfile);
        profileForm.value = response.data;
        hasProfile.value = true;

        // 更新用户表中的user_type
        await axios.put('/users', {
          userId: userId,
          userType: selectedUserType.value
        });

        ElMessage.success('身份设置成功');
        loadUserData(); // 重新加载数据
      } catch (error) {
        ElMessage.error('设置身份失败: ' + (error.response?.data?.message || error.message));
        console.error(error);
      } finally {
        saving.value = false;
      }
    };

    // 保存档案信息
    const saveProfile = async () => {
      try {
        saving.value = true;

        // 如果有新头像，先上传头像
        if (avatarFile.value) {
          const avatarUrl = await fileUploader.uploadImage(avatarFile.value, { userId });
          profileForm.value.avatar = avatarUrl;
        }

        // 更新档案信息
        await axios.put('/user-profiles', profileForm.value);

        // 更新用户基本信息
        await axios.put('/users', {
          userId: userId,
          phone: userForm.value.phone
        });

        ElMessage.success('保存成功');
        loadUserData(); // 重新加载数据
      } catch (error) {
        ElMessage.error('保存失败: ' + (error.response?.data?.message || error.message));
        console.error(error);
      } finally {
        saving.value = false;
      }
    };

    // 处理头像变更
    const handleAvatarChange = (file) => {
      const isImage = file.raw.type.startsWith('image/');
      const isLt2M = file.raw.size / 1024 / 1024 < 2;

      if (!isImage) {
        ElMessage.error('只能上传图片文件!');
        return;
      }
      if (!isLt2M) {
        ElMessage.error('头像图片大小不能超过 2MB!');
        return;
      }

      avatarFile.value = file.raw;
      avatarUrl.value = URL.createObjectURL(file.raw);
    };

    // 处理需求文件上传
    const handleNeedsFileUploaded = (fileUrl) => {
      profileForm.value.needs_description_file = fileUrl;
    };

    // 下载需求文件
    const downloadNeedsFile = () => {
      if (profileForm.value.needs_description_file) {
        const filename = profileForm.value.needs_description_file.split('/').pop();
        fileUploader.downloadFile(filename, userId);
      }
    };

    onMounted(() => {
      loadUserData();
    });

    return {
      user,
      userForm,
      profileForm,
      selectedUserType,
      hasProfile,
      saving,
      avatarUrl,
      needsFileUploader,
      statusTagType,
      submitUserType,
      saveProfile,
      handleAvatarChange,
      handleNeedsFileUploaded,
      downloadNeedsFile
    };
  }
};
</script>

<style scoped>
.user-profile-container {
  max-width: 900px;
  margin: 20px auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section {
  margin-bottom: 30px;
}

.section h3 {
  margin-bottom: 20px;
  color: #409EFF;
}

.avatar-uploader {
  width: 150px;
  height: 150px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader:hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 150px;
  text-align: center;
  line-height: 150px;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>