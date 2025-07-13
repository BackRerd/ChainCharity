<template>
  <el-descriptions :column="2" border>
    <el-descriptions-item label="名称">{{ profile.realName || '未填写' }}</el-descriptions-item>
    <el-descriptions-item label="用户类型">
      <el-tag :type="profile.userType === 'donor' ? 'success' : 'warning'">
        {{ formatUserType(profile.userType) }}
      </el-tag>
    </el-descriptions-item>
    <el-descriptions-item label="是否企业">
      <el-tag :type="profile.isEnterprise ? 'primary' : 'info'">
        {{ profile.isEnterprise ? '企业' : '个人' }}
      </el-tag>
    </el-descriptions-item>

    <template v-if="profile.userType === 'donor' && profile.isEnterprise">
      <el-descriptions-item label="企业类型">{{ formatEnterpriseType(profile.enterpriseType) }}</el-descriptions-item>
      <el-descriptions-item label="法定代表人">{{ profile.legalRepresentative || '未填写' }}</el-descriptions-item>
      <el-descriptions-item label="营业执照号">{{ profile.businessLicense || '未填写' }}</el-descriptions-item>
    </template>

    <template v-else>
      <el-descriptions-item label="性别">{{ formatGender(profile.gender) }}</el-descriptions-item>
      <el-descriptions-item label="身份证号">{{ profile.idCardNumber || '未填写' }}</el-descriptions-item>
    </template>

    <el-descriptions-item label="联系方式">{{ profile.contactPhone || '未填写' }}</el-descriptions-item>
    <el-descriptions-item label="银行账号">{{ profile.bankAccount || '未填写' }}</el-descriptions-item>
    <el-descriptions-item label="银行名称">{{ profile.bankName || '未填写' }}</el-descriptions-item>

    <template v-if="profile.userType === 'recipient'">
      <el-descriptions-item label="家庭地址">{{ profile.address || '未填写' }}</el-descriptions-item>
      <el-descriptions-item label="家庭成员">{{ profile.familyMembers || '未填写' }}</el-descriptions-item>
      <el-descriptions-item label="月收入">{{ profile.monthlyIncome ? `${profile.monthlyIncome}元` : '未填写' }}</el-descriptions-item>
      <el-descriptions-item label="贫困等级">{{ formatPovertyLevel(profile.povertyLevel) }}</el-descriptions-item>
      <el-descriptions-item label="相关文件">
        <div v-if="hasFile" class="file-download-container">
          <el-button
              type="primary"
              size="small"
              @click="handleFileDownload"
              :loading="downloading"
          >
            <el-icon><Download /></el-icon>
            下载文件
          </el-button>
          <span class="file-name">{{ displayFileName }}</span>
        </div>
        <span v-else class="no-file">未上传</span>
      </el-descriptions-item>
    </template>
  </el-descriptions>
</template>

<script setup>
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { Download } from '@element-plus/icons-vue';
import fileUploader from '@/utils/fileUploader'; // 请确保路径正确

const props = defineProps({
  profile: {
    type: Object,
    required: true,
    default: () => ({
      userType: '',
      fileLocation: '',
      fileUrl: '',
      userId: ''
    })
  }
});

const downloading = ref(false);

// 计算属性：判断是否有可下载文件
const hasFile = computed(() => {
  return !!props.profile.fileLocation || !!props.profile.fileUrl;
});

// 计算属性：显示文件名
const displayFileName = computed(() => {
  const url = props.profile.fileUrl || props.profile.fileLocation;
  if (!url) return '';

  // 从URL中提取文件名并移除查询参数
  const fileName = url.split('/').pop();
  return fileName.split('?')[0];
});

// 格式化函数
const formatUserType = (type) => {
  const types = {
    donor: '捐赠者',
    recipient: '受助者'
  };
  return types[type] || type;
};

const formatEnterpriseType = (type) => {
  const types = {
    private: '私营企业',
    state: '国有企业',
    foreign: '外资企业'
  };
  return types[type] || type || '未填写';
};

const formatGender = (gender) => {
  const genders = {
    male: '男',
    female: '女'
  };
  return genders[gender] || gender || '未填写';
};

const formatPovertyLevel = (level) => {
  const levels = {
    low: '低',
    medium: '中',
    high: '高'
  };
  return levels[level] || level || '未填写';
};

// 文件下载处理
const handleFileDownload = async () => {
  if (!hasFile.value) return;

  downloading.value = true;

  try {
    if (props.profile.fileUrl) {
      // 直接通过URL下载
      const link = document.createElement('a');
      link.href = props.profile.fileUrl;
      link.download = displayFileName.value;
      link.style.display = 'none';
      document.body.appendChild(link);
      link.click();
      setTimeout(() => document.body.removeChild(link), 100);
    } else {
      // 通过API下载
      await fileUploader.downloadFile(
          props.profile.fileLocation,
          props.profile.userId
      );
    }
    ElMessage.success('文件下载已开始');
  } catch (error) {
    console.error('文件下载失败:', error);
    ElMessage.error(`文件下载失败: ${error.message}`);
  } finally {
    downloading.value = false;
  }
};

</script>

<style scoped>
.file-download-container {
  display: flex;
  align-items: center;
  gap: 8px;
}

.file-name {
  font-size: 12px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 200px;
}

.no-file {
  color: #999;
  font-size: 12px;
  font-style: italic;
}
</style>