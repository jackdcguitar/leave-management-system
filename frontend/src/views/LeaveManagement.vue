<template>
  <div>
    <h4 class="mb-3">請假管理</h4>

    <div v-if="successMessage" class="alert alert-success alert-dismissible fade show">
      {{ successMessage }}
      <button type="button" class="btn-close" @click="successMessage = ''"></button>
    </div>

    <div v-if="errorMessage" class="alert alert-danger alert-dismissible fade show">
      {{ errorMessage }}
      <button type="button" class="btn-close" @click="errorMessage = ''"></button>
    </div>

    <form @submit.prevent="handleSubmit" class="row g-3 mb-4">
      <div class="col-md-4">
        <label for="leaveType" class="form-label">請假種類</label>
        <select
          v-model="form.leaveType"
          class="form-select"
          id="leaveType"
          required
        >
          <option value="">-- 請選擇 --</option>
          <option value="ANNUAL">特休</option>
          <option value="SICK">病假</option>
          <option value="PERSONAL">事假</option>
        </select>
      </div>
      <div class="col-md-4">
        <label for="startDate" class="form-label">開始時間</label>
        <input
          v-model="form.startDate"
          type="datetime-local"
          class="form-control"
          id="startDate"
          required
        >
      </div>
      <div class="col-md-4">
        <label for="endDate" class="form-label">結束時間</label>
        <input
          v-model="form.endDate"
          type="datetime-local"
          class="form-control"
          id="endDate"
          required
        >
      </div>
      <div class="col-12">
        <label for="reason" class="form-label">事由</label>
        <input
          v-model="form.reason"
          type="text"
          class="form-control"
          id="reason"
          placeholder="簡述請假原因"
          required
        >
      </div>
      <div class="col-12">
        <button class="btn btn-primary" type="submit" :disabled="loading">
          {{ loading ? '送出中...' : '送出申請' }}
        </button>
      </div>
    </form>

    <div class="card">
      <div class="card-body">
        <h6 class="card-title mb-3">我的請假紀錄</h6>
        <table class="table table-sm">
          <thead>
            <tr>
              <th>類型</th>
              <th>起</th>
              <th>迄</th>
              <th>事由</th>
              <th>狀態</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="leave in leaves" :key="leave.id">
              <td>{{ getLeaveTypeName(leave.leaveType) }}</td>
              <td>{{ formatDateTime(leave.startDate) }}</td>
              <td>{{ formatDateTime(leave.endDate) }}</td>
              <td>{{ leave.reason }}</td>
              <td>
                <span :class="getStatusClass(leave.status)">
                  {{ getStatusName(leave.status) }}
                </span>
              </td>
            </tr>
            <tr v-if="leaves.length === 0">
              <td colspan="5" class="text-center">尚無請假記錄</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { leaveAPI } from '../api'

export default {
  name: 'LeaveManagement',
  data() {
    return {
      form: {
        leaveType: '',
        startDate: '',
        endDate: '',
        reason: ''
      },
      leaves: [],
      loading: false,
      successMessage: '',
      errorMessage: ''
    }
  },
  mounted() {
    this.loadLeaves()
  },
  methods: {
    async loadLeaves() {
      try {
        this.leaves = await leaveAPI.getMyLeaves()
      } catch (error) {
        console.error('Failed to load leaves:', error)
      }
    },
    async handleSubmit() {
      this.loading = true
      this.successMessage = ''
      this.errorMessage = ''

      try {
        await leaveAPI.applyLeave(this.form)
        this.successMessage = '請假申請已送出'
        this.resetForm()
        await this.loadLeaves()
      } catch (error) {
        this.errorMessage = '申請失敗：' + (error.response?.data?.message || error.message)
        console.error('Failed to apply leave:', error)
      } finally {
        this.loading = false
      }
    },
    resetForm() {
      this.form = {
        leaveType: '',
        startDate: '',
        endDate: '',
        reason: ''
      }
    },
    getLeaveTypeName(type) {
      const types = {
        'ANNUAL': '特休',
        'SICK': '病假',
        'PERSONAL': '事假'
      }
      return types[type] || type
    },
    getStatusName(status) {
      const statuses = {
        'PENDING': '審核中',
        'APPROVED': '已核准',
        'REJECTED': '已拒絕'
      }
      return statuses[status] || status
    },
    getStatusClass(status) {
      const classes = {
        'APPROVED': 'badge bg-success',
        'REJECTED': 'badge bg-danger',
        'PENDING': 'badge bg-warning'
      }
      return classes[status] || 'badge bg-secondary'
    },
    formatDateTime(datetime) {
      if (!datetime) return ''
      const date = new Date(datetime)
      return date.toLocaleString('zh-TW', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
  }
}
</script>
