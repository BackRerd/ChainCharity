<template>
  <el-card class="profile-card animate__animated animate__fadeIn">
    <div class="header-section">
      <h2 class="title-gradient">完善个人信息</h2>
      <div class="wave-divider"></div>
    </div>

    <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="140px"
        class="profile-form"
    >
      <!-- 个人基础信息 -->
      <div class="form-section">
        <h3 class="section-title"><i class="icon-user"></i> 基础信息</h3>
        <el-form-item label="用户名">
          <el-input v-model="user.username" disabled class="custom-input" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="user.phone" disabled class="custom-input" />
        </el-form-item>

        <!-- 是否为企业 -->
        <el-form-item label="是否企业" v-if="!hasProfile">
          <el-switch v-model="isEnterprise" />
        </el-form-item>

        <!-- 选择档案身份 -->
        <el-form-item label="档案身份" prop="userType" v-if="!hasProfile">
          <el-select
              v-model="form.userType"
              placeholder="请选择档案身份"
          >
            <el-option label="捐赠者" value="donor" />
            <el-option label="受助者" value="recipient" />
          </el-select>
        </el-form-item>
      </div>

      <!-- 通用信息 -->
      <div class="form-section">
        <h3 class="section-title"><i class="icon-id-card"></i> 身份信息</h3>
        <el-form-item label="真实姓名/企业名称" prop="realName">
          <el-input v-model="form.realName" class="custom-input" />
        </el-form-item>
        <el-form-item label="身份证/统一社会信用代码" prop="idCardNumber">
          <el-input v-model="form.idCardNumber" class="custom-input" />
        </el-form-item>
        <el-form-item label="头像/Logo" class="avatar-upload">
          <el-upload
              class="avatar-uploader"
              action="#"
              :show-file-list="false"
              :on-change="onAvatarChange"
              :auto-upload="false"
              accept="image/*"
          >
            <img v-if="form.avatar" :src="form.avatar" class="avatar" />
            <div v-else class="upload-placeholder">
              <i class="icon-camera"></i>
              <p>点击上传</p>
            </div>
          </el-upload>
          <div class="upload-hint">建议尺寸 200×200px</div>
        </el-form-item>
      </div>

      <!-- 个人用户信息 -->
      <template v-if="form.userType === 'donor' || form.userType === 'recipient'">
        <div class="form-section">
          <h3 class="section-title"><i class="icon-user-circle"></i> 个人信息</h3>
          <el-form-item label="性别" class="gender-radio">
            <el-radio-group v-model="form.gender">
              <el-radio label="male" class="custom-radio">男</el-radio>
              <el-radio label="female" class="custom-radio">女</el-radio>
              <el-radio label="other" class="custom-radio">其他</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="出生日期">
            <el-date-picker
                v-model="form.birthDate"
                type="date"
                placeholder="选择日期"
                class="custom-datepicker"
            />
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="form.address" class="custom-input" />
          </el-form-item>
        </div>
      </template>

      <!-- 受助者字段 -->
      <template v-if="form.userType === 'recipient'">
        <div class="form-section">
          <h3 class="section-title"><i class="icon-hand-holding-heart"></i> 受助信息</h3>
          <el-form-item label="银行卡号">
            <el-input v-model="form.bankAccount" class="custom-input" />
          </el-form-item>
          <el-form-item label="开户行">
            <el-input v-model="form.bankName" class="custom-input" />
          </el-form-item>
          <el-form-item label="需求说明文件" class="file-upload">
            <el-upload
                class="file-uploader"
                action="#"
                :show-file-list="false"
                :on-change="onFileChange"
                :auto-upload="false"
            >
              <el-button type="primary" plain class="upload-btn">
                <i class="icon-upload"></i> 上传文件
              </el-button>
            </el-upload>
            <el-link
                v-if="form.needsDescriptionFile"
                :href="form.needsDescriptionFile"
                target="_blank"
                class="file-link"
            >
              <i class="icon-file"></i> 查看已上传文件
            </el-link>
          </el-form-item>
          <el-form-item label="家庭成员数">
            <el-input-number
                v-model="form.familyMembers"
                :min="0"
                controls-position="right"
                class="custom-number-input"
            />
          </el-form-item>
          <el-form-item label="月收入">
            <el-input-number
                v-model="form.monthlyIncome"
                :min="0"
                :step="0.01"
                controls-position="right"
                class="custom-number-input"
            />
          </el-form-item>
          <el-form-item label="贫困等级">
            <el-select
                v-model="form.povertyLevel"
                placeholder="请选择贫困等级"
                class="custom-select"
            >
              <el-option label="低" value="low" class="poverty-option poverty-low" />
              <el-option label="中" value="medium" class="poverty-option poverty-medium" />
              <el-option label="高" value="high" class="poverty-option poverty-high" />
              <el-option label="极度" value="extreme" class="poverty-option poverty-extreme" />
            </el-select>
          </el-form-item>
        </div>
      </template>

      <!-- 企业字段 -->
      <template v-if="isEnterprise">
        <div class="form-section">
          <h3 class="section-title"><i class="icon-building"></i> 企业信息</h3>
          <el-form-item label="企业法人">
            <el-input v-model="form.legalRepresentative" class="custom-input" />
          </el-form-item>
          <el-form-item label="营业执照号">
            <el-input v-model="form.businessLicense" class="custom-input" />
          </el-form-item>
          <el-form-item label="企业类型">
            <el-select
                v-model="form.enterpriseType"
                class="custom-select"
            >
              <el-option label="私营" value="private" />
              <el-option label="国有" value="state-owned" />
              <el-option label="外资" value="foreign" />
              <el-option label="合资" value="joint-venture" />
            </el-select>
          </el-form-item>
          <el-form-item label="所属行业">
            <el-input v-model="form.industry" class="custom-input" />
          </el-form-item>
          <el-form-item label="联系人">
            <el-input v-model="form.contactPerson" class="custom-input" />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="form.contactPhone" class="custom-input" />
          </el-form-item>
          <el-form-item label="企业角色" class="role-checkbox">
            <el-checkbox-group v-model="form.enterpriseRole">
              <el-checkbox label="can_donate" class="custom-checkbox">
                <i class="icon-heart"></i> 可捐赠
              </el-checkbox>
              <el-checkbox label="can_receive" class="custom-checkbox">
                <i class="icon-handshake"></i> 可受助
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </div>
      </template>

      <div class="form-actions">
        <el-button
            type="primary"
            @click="onSubmit"
            class="submit-btn"
            :loading="loading"
        >
          <i class="icon-save"></i> 保存信息
        </el-button>
      </div>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import fileUploader from '@/utils/fileUploader';
import 'animate.css';

const formRef = ref();
const userId = JSON.parse(localStorage.getItem("user")).userId;
const loading = ref(false);

const user = reactive({});
const form = reactive({});
const isEnterprise = ref(false);

const hasProfile = computed(() => !!form.profileId);

onMounted(async () => {
  try {
    const userRes = await axios.get(`/users/${userId}`);
    Object.assign(user, userRes.data.data);

    const profileRes = await axios.get(`/user-profiles/${userId}`);
    Object.assign(form, profileRes.data.data);
    isEnterprise.value = form.userType === 'enterprise';
  } catch (error) {
    if (error.response?.status === 404) {
      ElMessage.info('您还没有创建个人档案，请完善信息');
      form.userId = userId;
    }
  }
});

const onAvatarChange = async (file) => {
  const url = await fileUploader.uploadImage(file.raw, { userId });
  form.avatar = url;
};

const onFileChange = async (file) => {
  const url = await fileUploader.uploadFile(file.raw, { userId });
  form.needsDescriptionFile = url;
};

const onSubmit = async () => {
  form.userType = isEnterprise.value ? 'enterprise' : form.userType;
  form.userId = userId;

  formRef.value.validate(async (valid) => {
    if (!valid) return;

    loading.value = true;
    try {
      if (hasProfile.value) {
        await axios.put('/user-profiles', form);
      } else {
        await axios.post('/user-profiles', form);
      }
      ElMessage.success({
        message: '信息保存成功',
        customClass: 'success-message'
      });
    } catch (err) {
      ElMessage.error({
        message: '保存失败',
        customClass: 'error-message'
      });
    } finally {
      loading.value = false;
    }
  });
};

const rules = {
  userType: [
    { required: true, message: '请选择身份', trigger: 'change' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名或企业名称', trigger: 'blur' }
  ],
  idCardNumber: [
    { required: true, message: '请输入身份证号或统一社会信用代码', trigger: 'blur' }
  ]
};
</script>

<style lang="scss" scoped>
@use "sass:color";

// 慈善主题配色
$primary-color: #5cb3cc;  // 慈善蓝
$secondary-color: #ff9966; // 温暖橙
$accent-color: #8fc742;   // 希望绿
$light-bg: #f8f9fa;
$text-color: #495057;
$border-color: #dee2e6;

// 全局样式
:deep() {
  .el-form-item__label {
    color: $text-color;
    font-weight: 500;
  }

  .el-input__inner, .el-textarea__inner {
    border-radius: 4px;
    transition: all 0.3s ease;

    &:focus {
      border-color: $primary-color;
      box-shadow: 0 0 0 2px color.scale($primary-color, $alpha: -80%);
    }
  }

  .el-radio__input.is-checked .el-radio__inner {
    background-color: $primary-color;
    border-color: $primary-color;
  }

  .el-checkbox__input.is-checked .el-checkbox__inner {
    background-color: $primary-color;
    border-color: $primary-color;
  }

  .el-button--primary {
    background-color: $primary-color;
    border-color: $primary-color;

    &:hover {
      background-color: color.scale($primary-color, $lightness: -10%);
      border-color: color.scale($primary-color, $lightness: -10%);
    }
  }
}

.profile-card {
  max-width: 900px;
  margin: 2rem auto;
  border-radius: 12px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  border: none;
  overflow: hidden;
  background-color: #fff;

  .header-section {
    padding: 1.5rem 2rem 0;
    text-align: center;

    .title-gradient {
      background: linear-gradient(90deg, $primary-color, $secondary-color);
      -webkit-background-clip: text;
      background-clip: text;
      color: transparent;
      font-size: 1.8rem;
      font-weight: 600;
      margin-bottom: 1rem;
    }

    .wave-divider {
      height: 10px;
      background: url('data:image/svg+xml;utf8,<svg viewBox="0 0 1200 120" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="none"><path d="M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V0Z" opacity=".25" fill="%235cb3cc"/><path d="M0,0V15.81C13,36.92,27.64,56.86,47.69,72.05,99.41,111.27,165,111,224.58,91.58c31.15-10.15,60.09-26.07,89.67-39.8,40.92-19,84.73-46,130.83-49.67,36.26-2.85,70.9,9.42,98.6,31.56,31.77,25.39,62.32,62,103.63,73,40.44,10.79,81.35-6.69,119.13-24.28s75.16-39,116.92-43.05c59.73-5.85,113.28,22.88,168.9,38.84,30.2,8.66,59,6.17,87.09-7.5,22.43-10.89,48-26.93,60.65-49.24V0Z" opacity=".5" fill="%235cb3cc"/><path d="M0,0V5.63C149.93,59,314.09,71.32,475.83,42.57c43-7.64,84.23-20.12,127.61-26.46,59-8.63,112.48,12.24,165.56,35.4C827.93,77.22,886,95.24,951.2,90c86.53-7,172.46-45.71,248.8-84.81V0Z" fill="%235cb3cc"/></svg>');
      background-size: cover;
      margin: 0 -20px;
    }
  }

  .profile-form {
    padding: 1.5rem 2rem 2rem;

    .form-section {
      margin-bottom: 2rem;
      padding: 1.5rem;
      background-color: color.scale($light-bg, $alpha: -50%);
      border-radius: 8px;
      border-left: 4px solid $primary-color;
      transition: all 0.3s ease;

      &:hover {
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
      }
    }

    .section-title {
      color: $primary-color;
      font-size: 1.2rem;
      margin-bottom: 1.5rem;
      padding-bottom: 0.5rem;
      border-bottom: 1px dashed $border-color;
      display: flex;
      align-items: center;

      i {
        margin-right: 0.5rem;
        font-size: 1.1em;
      }
    }
  }
}

// [其他样式规则保持不变...]
// 只修改颜色函数部分，其他样式规则不变

.upload-hint {
  font-size: 0.8rem;
  color: color.scale($text-color, $lightness: 30%);
  margin-top: 0.5rem;
}

.success-message {
  background-color: color.scale($accent-color, $lightness: 20%);
}

.error-message {
  background-color: color.scale(#dc3545, $lightness: -10%);
}

// 响应式调整
@media (max-width: 768px) {
  .profile-card {
    margin: 1rem;
    border-radius: 8px;

    .header-section {
      padding: 1rem 1rem 0;

      .title-gradient {
        font-size: 1.5rem;
      }
    }

    .profile-form {
      padding: 1rem;

      .form-section {
        padding: 1rem;
      }
    }
  }
}
</style>