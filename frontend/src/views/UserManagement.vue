<template>
  <div>
    <h4 class="mb-3">人員管理</h4>

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
        <label class="form-label">帳號</label>
        <input
          v-model="form.username"
          type="text"
          class="form-control"
          required
        >
      </div>
      <div class="col-md-4">
        <label class="form-label">姓名</label>
        <input
          v-model="form.displayName"
          type="text"
          class="form-control"
          required
        >
      </div>
      <div class="col-md-4">
        <label class="form-label">角色</label>
        <select v-model="form.role" class="form-select" required>
          <option value="USER">USER</option>
          <option value="MANAGER">MANAGER</option>
          <option value="ADMIN">ADMIN</option>
        </select>
      </div>
      <div class="col-12">
        <button class="btn btn-warning" type="submit" :disabled="loading">
          {{ loading ? '處理中...' : '新增/更新' }}
        </button>
      </div>
    </form>

    <div class="card">
      <div class="card-body">
        <h6 class="card-title mb-3">使用者清單</h6>
        <table class="table table-sm">
          <thead>
            <tr>
              <th>帳號</th>
              <th>姓名</th>
              <th>角色</th>
              <th>狀態</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.id">
              <td>{{ user.username }}</td>
              <td>{{ user.displayName }}</td>
              <td>{{ user.roles?.join(', ') || 'N/A' }}</td>
              <td>{{ user.enabled ? '啟用' : '停用' }}</td>
            </tr>
            <tr v-if="users.length === 0">
              <td colspan="4" class="text-center">尚無使用者資料</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { userAPI } from '../api'

export default {
  name: 'UserManagement',
  data() {
    return {
      form: {
        username: '',
        displayName: '',
        role: 'USER'
      },
      users: [],
      loading: false,
      successMessage: '',
      errorMessage: ''
    }
  },
  mounted() {
    this.loadUsers()
  },
  methods: {
    async loadUsers() {
      try {
        this.users = await userAPI.getAllUsers()
      } catch (error) {
        console.error('Failed to load users:', error)
      }
    },
    async handleSubmit() {
      this.loading = true
      this.successMessage = ''
      this.errorMessage = ''

      try {
        await userAPI.createUser(this.form)
        this.successMessage = '使用者已新增/更新'
        this.resetForm()
        await this.loadUsers()
      } catch (error) {
        this.errorMessage = '操作失敗：' + (error.response?.data?.message || error.message)
        console.error('Failed to create/update user:', error)
      } finally {
        this.loading = false
      }
    },
    resetForm() {
      this.form = {
        username: '',
        displayName: '',
        role: 'USER'
      }
    }
  }
}
</script>
