<template>
  <div class="bg-light" style="min-height: 100vh;">
    <div class="container py-5">
      <div class="row justify-content-center">
        <div class="col-md-4">
          <div class="card shadow-sm">
            <div class="card-body">
              <h4 class="mb-3">登入</h4>

              <div v-if="errorMessage" class="alert alert-danger">
                {{ errorMessage }}
              </div>

              <form @submit.prevent="handleLogin">
                <div class="mb-3">
                  <label class="form-label">帳號</label>
                  <input
                    v-model="username"
                    class="form-control"
                    type="text"
                    required
                  >
                </div>
                <div class="mb-3">
                  <label class="form-label">密碼</label>
                  <input
                    v-model="password"
                    class="form-control"
                    type="password"
                    required
                  >
                </div>
                <button
                  class="btn btn-primary w-100"
                  type="submit"
                  :disabled="loading"
                >
                  {{ loading ? '登入中...' : '登入' }}
                </button>
              </form>

              <div class="mt-3">
                <small class="text-muted">
                  示範帳號：admin/admin123、manager/manager123、user/user123
                </small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { authAPI } from '../api'

export default {
  name: 'Login',
  data() {
    return {
      username: '',
      password: '',
      errorMessage: '',
      loading: false
    }
  },
  methods: {
    async handleLogin() {
      this.loading = true
      this.errorMessage = ''

      try {
        const response = await authAPI.login(this.username, this.password)
        console.log('Login response:', response)

        // 登入成功，等待一下讓 Cookie 設置完成
        await new Promise(resolve => setTimeout(resolve, 100))

        // 跳轉到主頁面
        this.$router.push('/portal')
      } catch (error) {
        this.errorMessage = '登入失敗，請檢查帳號密碼'
        console.error('Login error:', error)
        console.error('Error details:', error.response)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>
