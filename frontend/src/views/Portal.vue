<template>
  <div>
    <aside class="sidebar">
      <h5 class="mb-3">功能選單</h5>

      <button
        class="btn btn-outline-primary nav-btn"
        :class="{ active: currentRoute === 'leave' }"
        @click="navigate('leave')"
      >
        請假管理
      </button>

      <button
        class="btn btn-outline-primary nav-btn"
        :class="{ active: currentRoute === 'overtime' }"
        @click="navigate('overtime')"
      >
        加班管理
      </button>

      <button
        v-if="isAdmin"
        class="btn btn-outline-warning nav-btn"
        :class="{ active: currentRoute === 'users' }"
        @click="navigate('users')"
      >
        人員管理
      </button>

      <hr>
      <button class="btn btn-outline-secondary nav-btn" @click="handleLogout">
        登出
      </button>
    </aside>

    <main class="main-content">
      <div class="container-fluid">
        <router-view></router-view>
      </div>
    </main>
  </div>
</template>

<script>
import { authAPI } from '../api'

export default {
  name: 'Portal',
  data() {
    return {
      currentUser: null,
      isAdmin: false
    }
  },
  computed: {
    currentRoute() {
      return this.$route.name?.toLowerCase()
    }
  },
  async mounted() {
    try {
      const user = await authAPI.getCurrentUser()
      this.currentUser = user
      this.isAdmin = user.roles?.includes('ROLE_ADMIN') || false
    } catch (error) {
      console.error('Failed to get current user:', error)
      this.$router.push('/login')
    }
  },
  methods: {
    navigate(route) {
      this.$router.push(`/portal/${route}`)
    },
    async handleLogout() {
      try {
        await authAPI.logout()
        this.$router.push('/login')
      } catch (error) {
        console.error('Logout error:', error)
        this.$router.push('/login')
      }
    }
  }
}
</script>
