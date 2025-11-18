import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  base: '/',
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  build: {
    // 構建時輸出到 Spring Boot 靜態資源目錄
    outDir: path.resolve(__dirname, '../src/main/resources/static'),
    emptyOutDir: true
  }
})
