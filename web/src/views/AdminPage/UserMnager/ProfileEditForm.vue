<template>
  <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
    <!-- 公共字段 -->
    <el-form-item label="用户类型" prop="userType">
      <el-tag :type="form.userType === 'donor' ? 'success' : 'warning'">
        {{ formatUserType(form.userType) }}
      </el-tag>
    </el-form-item>

    <el-form-item label="是否企业" prop="isEnterprise">
      <el-tag :type="form.isEnterprise ? 'primary' : 'info'">
        {{ form.isEnterprise ? '企业' : '个人' }}
      </el-tag>
    </el-form-item>

    <template v-if="form.userType === 'donor'">
      <!-- 捐赠者字段 -->
      <el-form-item
          label="捐赠者名称"
          prop="realName"
          :rules="[{ required: true, message: '请输入名称', trigger: 'blur' }]"
      >
        <el-input v-model="form.realName" :placeholder="`请输入${form.isEnterprise ? '企业' : '个人'}名称`" />
      </el-form-item>

      <template v-if="form.isEnterprise">
        <!-- 企业捐赠者字段 -->
        <el-form-item label="企业类型" prop="enterpriseType">
          <el-select v-model="form.enterpriseType" placeholder="请选择企业类型">
            <el-option label="私营企业" value="private" />
            <el-option label="国有企业" value="state" />
            <el-option label="外资企业" value="foreign" />
          </el-select>
        </el-form-item>
        <el-form-item label="法定代表人" prop="legalRepresentative">
          <el-input v-model="form.legalRepresentative" placeholder="请输入法定代表人" />
        </el-form-item>
        <el-form-item label="营业执照号" prop="businessLicense">
          <el-input v-model="form.businessLicense" placeholder="请输入营业执照号" />
        </el-form-item>
      </template>
      <template v-else>
        <!-- 个人捐赠者字段 -->
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender" placeholder="请选择性别">
            <el-option label="男" value="male" />
            <el-option label="女" value="female" />
          </el-select>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCardNumber">
          <el-input v-model="form.idCardNumber" placeholder="请输入身份证号" />
        </el-form-item>
      </template>
    </template>

    <template v-else>
      <!-- 受助者字段 -->
      <el-form-item
          label="受助者名称"
          prop="realName"
          :rules="[{ required: true, message: '请输入受助者名称', trigger: 'blur' }]"
      >
        <el-input v-model="form.realName" placeholder="请输入受助者名称" />
      </el-form-item>

      <el-form-item label="性别" prop="gender">
        <el-select v-model="form.gender" placeholder="请选择性别">
          <el-option label="男" value="male" />
          <el-option label="女" value="female" />
        </el-select>
      </el-form-item>
      <el-form-item label="身份证号" prop="idCardNumber">
        <el-input v-model="form.idCardNumber" placeholder="请输入身份证号" />
      </el-form-item>
      <el-form-item label="家庭地址" prop="address">
        <el-input v-model="form.address" placeholder="请输入家庭地址" />
      </el-form-item>
      <el-form-item label="家庭成员数量" prop="familyMembers">
        <el-input-number v-model="form.familyMembers" :min="1" />
      </el-form-item>
      <el-form-item label="月收入" prop="monthlyIncome">
        <el-input-number v-model="form.monthlyIncome" :min="0" :precision="2" />
        <span style="margin-left: 5px">元</span>
      </el-form-item>
      <el-form-item label="贫困等级" prop="povertyLevel">
        <el-select v-model="form.povertyLevel" placeholder="请选择贫困等级">
          <el-option label="低" value="low" />
          <el-option label="中" value="medium" />
          <el-option label="高" value="high" />
        </el-select>
      </el-form-item>
    </template>

    <!-- 公共字段 -->
    <el-form-item label="联系方式" prop="contactPhone">
      <el-input v-model="form.contactPhone" placeholder="请输入联系方式" />
    </el-form-item>

    <el-form-item label="银行账号" prop="bankAccount">
      <el-input v-model="form.bankAccount" placeholder="请输入银行账号" />
    </el-form-item>
    <el-form-item label="银行名称" prop="bankName">
      <el-input v-model="form.bankName" placeholder="请输入银行名称" />
    </el-form-item>

    <el-form-item>
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSubmit">提交</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  profile: {
    type: Object,
    required: true
  },
  userType: {
    type: String,
    default: 'donor'
  },
  isEnterprise: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['submit', 'cancel'])

const formRef = ref(null)
const form = ref(JSON.parse(JSON.stringify(props.profile)))

// 初始化表单
if (!form.value.userType) {
  form.value.userType = props.userType
  form.value.isEnterprise = props.isEnterprise
}

const formatUserType = (type) => {
  switch (type) {
    case 'donor': return '捐赠者'
    case 'recipient': return '受助者'
    default: return type
  }
}

const handleSubmit = async () => {
  await formRef.value.validate((valid) => {
    if (valid) {
      emit('submit', form.value)
    }
  })
}

const handleCancel = () => {
  emit('cancel')
}
</script>