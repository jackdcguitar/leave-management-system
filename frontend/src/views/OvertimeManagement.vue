<template>
  <div>
    <h4 class="mb-3">加班管理</h4>

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
        <label class="form-label">日期</label>
        <input
          v-model="form.date"
          type="date"
          class="form-control"
          required
        >
      </div>
      <div class="col-md-4">
        <label class="form-label">起始</label>
        <input
          v-model="form.startTime"
          type="time"
          class="form-control"
          required
        >
      </div>
      <div class="col-md-4">
        <label class="form-label">結束</label>
        <input
          v-model="form.endTime"
          type="time"
          class="form-control"
          required
        >
      </div>
      <div class="col-12">
        <label class="form-label">事由</label>
        <input
          v-model="form.reason"
          type="text"
          class="form-control"
          placeholder="工作內容/原因"
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
        <h6 class="card-title mb-3">我的加班紀錄</h6>
        <table class="table table-sm">
          <thead>
            <tr>
              <th>日期</th>
              <th>起</th>
              <th>迄</th>
              <th>時數</th>
              <th>狀態</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="overtime in overtimes" :key="overtime.id">
              <td>{{ overtime.date }}</td>
              <td>{{ overtime.startTime }}</td>
              <td>{{ overtime.endTime }}</td>
              <td>{{ overtime.hours }}</td>
              <td>{{ overtime.status }}</td>
            </tr>
            <tr v-if="overtimes.length === 0">
              <td colspan="5" class="text-center">尚無加班記錄</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { overtimeAPI } from '../api'

export default {
  name: 'OvertimeManagement',
  data() {
    return {
      form: {
        date: '',
        startTime: '',
        endTime: '',
        reason: ''
      },
      overtimes: [],
      loading: false,
      successMessage: '',
      errorMessage: ''
    }
  },
  mounted() {
    this.loadOvertimes()
  },
  methods: {
    async loadOvertimes() {
      try {
        this.overtimes = await overtimeAPI.getMyOvertimes()
      } catch (error) {
        console.error('Failed to load overtimes:', error)
      }
    },
    async handleSubmit() {
      this.loading = true
      this.successMessage = ''
      this.errorMessage = ''

      try {
        await overtimeAPI.applyOvertime(this.form)
        this.successMessage = '加班申請已送出'
        this.resetForm()
        await this.loadOvertimes()
      } catch (error) {
        this.errorMessage = '申請失敗：' + (error.response?.data?.message || error.message)
        console.error('Failed to apply overtime:', error)
      } finally {
        this.loading = false
      }
    },
    resetForm() {
      this.form = {
        date: '',
        startTime: '',
        endTime: '',
        reason: ''
      }
    }
  }
}
</script>
