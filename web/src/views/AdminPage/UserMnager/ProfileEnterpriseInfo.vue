<template>
  <el-descriptions :column="2" border>
    <el-descriptions-item label="企业名称">{{ profile.realName || '未填写' }}</el-descriptions-item>
    <el-descriptions-item label="企业类型">{{ formatEnterpriseType(profile.enterpriseType) }}</el-descriptions-item>
    <el-descriptions-item label="法定代表人">{{ profile.legalRepresentative || '未填写' }}</el-descriptions-item>
    <el-descriptions-item label="营业执照号">{{ profile.businessLicense || '未填写' }}</el-descriptions-item>
    <el-descriptions-item label="所属行业">{{ profile.industry || '未填写' }}</el-descriptions-item>
    <el-descriptions-item label="联系人">{{ profile.contactPerson || '未填写' }}</el-descriptions-item>
    <el-descriptions-item label="联系电话">{{ profile.contactPhone || '未填写' }}</el-descriptions-item>
    <el-descriptions-item label="企业角色">{{ formatEnterpriseRole(profile.enterpriseRole) }}</el-descriptions-item>
  </el-descriptions>
</template>

<script setup>
defineProps({
  profile: {
    type: Object,
    required: true
  }
})

const formatEnterpriseType = (type) => {
  switch (type) {
    case 'private': return '私营企业'
    case 'state': return '国有企业'
    case 'foreign': return '外资企业'
    default: return type || '未填写'
  }
}

const formatEnterpriseRole = (role) => {
  switch (role) {
    case 'supplier': return '供应商'
    case 'buyer': return '采购商'
    case 'both': return '供应商和采购商'
    default: return role || '未填写'
  }
}
</script>