
<template>
  <ThemePalette />
  <div class="policy-manager" style="padding: 24px; background-color: #F5F5F5;">
    <!-- 密码策略配置 -->
    <el-card shadow="hover" style="margin-bottom: 24px; background-color: #E8F5E9; border-color: #C8E6C9;">
      <div slot="header" class="card-header" style="color: #2E7D32; font-size: 18px; font-weight: bold;">
        密码策略配置
      </div>
      <el-form :model="policy" label-width="180px">
        <el-form-item label="最低密码长度">
          <el-input-number v-model="policy.minLength" :min="8" :max="64"></el-input-number>
        </el-form-item>
        <el-form-item label="必须包含字符类型">
          <el-checkbox-group v-model="policy.requirements">
            <el-checkbox label="大写字母"></el-checkbox>
            <el-checkbox label="小写字母"></el-checkbox>
            <el-checkbox label="数字"></el-checkbox>
            <el-checkbox label="特殊字符"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="关联个人信息检查">
          <el-switch v-model="policy.checkPersonalInfo" active-text="启用" inactive-text="禁用"></el-switch>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="savePolicy">保存策略</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 弱口令筛查及锁定 -->
    <el-card shadow="hover" style="margin-bottom: 24px; background-color: #FFF3E0; border-color: #FFE0B2;">
      <div slot="header" class="card-header" style="color: #EF6C00; font-size: 18px; font-weight: bold;">
        弱口令筛查 & 锁定
      </div>
      <el-table :data="accounts" style="width: 100%">
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="lastChanged" label="上次修改日期"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.locked ? 'danger' : (scope.row.weak ? 'warning' : 'success')">
              {{ scope.row.locked ? '已锁定' : (scope.row.weak ? '弱口令' : '正常') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button v-if="scope.row.weak" type="primary" size="small" @click="resetPassword(scope.row)">强制重置密码</el-button>
            <el-button v-if="scope.row.locked" type="info" size="small" disabled>等待解锁</el-button>
            <el-button v-if="scope.row.weak && trainingEnabled" type="warning" size="small" @click="scheduleInterview(scope.row)">安排约谈</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 定期更新提醒 & 日志同步 -->
    <el-card shadow="hover" style="margin-bottom: 24px; background-color: #F3E5F5; border-color: #E1BEE7;">
      <div slot="header" class="card-header" style="color: #8E24AA; font-size: 18px; font-weight: bold;">
        定期密码更新提醒 & 日志同步
      </div>
      <el-form :model="reminder" label-width="180px">
        <el-form-item label="更新周期（天）">
          <el-input-number v-model="reminder.intervalDays" :min="30" :max="365"></el-input-number>
        </el-form-item>
        <el-form-item label="自动提醒">
          <el-switch v-model="reminder.enabled" active-text="启用" inactive-text="禁用"></el-switch>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveReminder">保存设置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 管理员双因素认证 -->
    <el-card shadow="hover" style="margin-bottom: 24px; background-color: #E3F2FD; border-color: #BBDEFB;">
      <div slot="header" class="card-header" style="color: #1565C0; font-size: 18px; font-weight: bold;">
        管理员账户安全
      </div>
      <el-form :model="adminSecurity" label-width="160px">
        <el-form-item label="启用动态令牌 2FA">
          <el-switch v-model="adminSecurity.twoFactor" active-text="开启" inactive-text="关闭"></el-switch>
        </el-form-item>
        <el-form-item label="敏感操作二次密码校验">
          <el-switch v-model="adminSecurity.requireReAuth" active-text="开启" inactive-text="关闭"></el-switch>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveAdminSecurity">保存设置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 登录异常检测 & 权限最小化审计 -->
    <el-card shadow="hover" style="margin-bottom: 24px; background-color: #FFFDE7; border-color: #FFF9C4;">
      <div slot="header" class="card-header" style="color: #F9A825; font-size: 18px; font-weight: bold;">
        登录异常检测 & 权限最小化审计
      </div>
      <el-form :model="anomaly" label-width="180px">
        <el-form-item label="异常检测启用">
          <el-switch v-model="anomaly.enabled" active-text="启用" inactive-text="禁用"></el-switch>
        </el-form-item>
        <el-form-item label="触发方式">
          <el-checkbox-group v-model="anomaly.methods">
            <el-checkbox label="异地登录"></el-checkbox>
            <el-checkbox label="非设备登录"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="验证方式">
          <el-checkbox-group v-model="anomaly.verifications">
            <el-checkbox label="短信验证"></el-checkbox>
            <el-checkbox label="邮箱验证"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveAnomaly">保存设置</el-button>
        </el-form-item>
      </el-form>
      <el-divider />
      <el-form :model="adminPerms" label-width="180px">
        <el-form-item label="最小化权限原则">
          <el-checkbox-group v-model="adminPerms.actions">
            <el-checkbox label="用户管理"></el-checkbox>
            <el-checkbox label="策略配置"></el-checkbox>
            <el-checkbox label="日志查看"></el-checkbox>
            <el-checkbox label="系统设置"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveAdminPerms">保存权限设置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 实时入侵检测 & 应急响应预案 -->
    <el-card shadow="hover" style="background-color: #E0F7FA; border-color: #B2EBF2;">
      <div slot="header" class="card-header" style="color: #00796B; font-size: 18px; font-weight: bold;">
        实时入侵检测 & 应急响应预案
      </div>
      <el-form :model="idsConfig" label-width="180px">
        <el-form-item label="IDS 启用">
          <el-switch v-model="idsConfig.enabled" active-text="启用" inactive-text="禁用"></el-switch>
        </el-form-item>
        <el-form-item label="监控行为">
          <el-checkbox-group v-model="idsConfig.behaviors">
            <el-checkbox label="高频登录尝试"></el-checkbox>
            <el-checkbox label="异常指令执行"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="告警方式">
          <el-checkbox-group v-model="idsConfig.alerts">
            <el-checkbox label="短信告警"></el-checkbox>
            <el-checkbox label="邮箱告警"></el-checkbox>
            <el-checkbox label="系统通知"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveIdsConfig">保存 IDS 设置</el-button>
        </el-form-item>
      </el-form>
      <el-divider />
      <el-form :model="responsePlan" label-width="180px">
        <el-form-item label="应急预案启用">
          <el-switch v-model="responsePlan.enabled" active-text="启用" inactive-text="禁用"></el-switch>
        </el-form-item>
        <el-form-item label="弱口令事件处置">
          <el-checkbox-group v-model="responsePlan.actions">
            <el-checkbox label="账户冻结"></el-checkbox>
            <el-checkbox label="数据备份">...恢复</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveResponsePlan">保存预案</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import ThemePalette from "@/components/UserPage/ThemePalette.vue";

// 模拟数据与状态
const policy = reactive({ minLength: 12, requirements: ['大写字母', '小写字母', '数字', '特殊字符'], checkPersonalInfo: true });
const accounts = ref([
  { username: 'user1', weak: true, locked: false, lastChanged: '2025-04-01' },
  { username: 'user2', weak: false, locked: true, lastChanged: '2025-06-10' },
  { username: 'user3', weak: false, locked: false, lastChanged: '2025-07-01' },
]);
const trainingEnabled = true;
const reminder = reactive({ intervalDays: 90, enabled: true });
const adminSecurity = reactive({ twoFactor: true, requireReAuth: true });
const anomaly = reactive({ enabled: true, methods: ['异地登录', '非设备登录'], verifications: ['短信验证', '邮箱验证'] });
const adminPerms = reactive({ actions: ['用户管理', '策略配置', '日志查看', '系统设置'] });
// 实时入侵检测
const idsConfig = reactive({ enabled: true, behaviors: ['高频登录尝试', '异常指令执行'], alerts: ['短信告警', '邮箱告警', '系统通知'] });
// 应急响应预案
const responsePlan = reactive({ enabled: true, actions: ['账户冻结', '数据备份恢复'] });

function savePolicy() { console.log('保存策略', JSON.parse(JSON.stringify(policy))); }
function resetPassword(row) { row.weak = false; row.lastChanged = new Date().toISOString().slice(0,10); logChange(row.username); }
function scheduleInterview(row) { console.log('安排约谈', row.username); }
function saveReminder() { console.log('保存提醒', JSON.parse(JSON.stringify(reminder))); }
function saveAdminSecurity() { console.log('保存管理员安全', JSON.parse(JSON.stringify(adminSecurity))); }
function saveAnomaly() { console.log('保存异常检测', JSON.parse(JSON.stringify(anomaly))); }
function saveAdminPerms() { console.log('保存权限设置', JSON.parse(JSON.stringify(adminPerms))); }
function saveIdsConfig() { console.log('保存IDS配置', JSON.parse(JSON.stringify(idsConfig))); }
function saveResponsePlan() { console.log('保存应急预案', JSON.parse(JSON.stringify(responsePlan))); }
function logChange(username) { console.log(`日志: 用户${username}于${new Date().toISOString()}修改密码，已同步审计系统`); }
function validatePassword(pwd, userInfo) { const lenOk = pwd.length >= policy.minLength; const upperOk = /[A-Z]/.test(pwd); const lowerOk = /[a-z]/.test(pwd); const digitOk = /\d/.test(pwd); const specialOk = /[^A-Za-z0-9]/.test(pwd); const personalOk = !userInfo || !new RegExp(userInfo).test(pwd); return lenOk && upperOk && lowerOk && digitOk && specialOk && personalOk; }
</script>

<style scoped>
.policy-manager .card-header { display: flex; align-items: center; }
</style>
