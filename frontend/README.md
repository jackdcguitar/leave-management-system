# 請假管理系統 - Vue 3 前端

這是請假管理系統的 Vue 3 前端專案，使用 Options API 風格開發。

## 技術棧

- Vue 3.4
- Vue Router 4.2
- Axios 1.6
- Bootstrap 5.3
- Vite 5.0

## 開發環境要求

- Node.js 18+
- npm 或 yarn

## 安裝與啟動

### 1. 安裝依賴

```bash
cd frontend
npm install
```

### 2. 啟動開發伺服器

```bash
npm run dev
```

前端將在 http://localhost:3000 啟動

### 3. 建置生產版本

```bash
npm run build
```

建置後的檔案將輸出到 `dist` 目錄

## 專案結構

```
frontend/
├── public/              # 靜態資源
├── src/
│   ├── api/            # API 接口定義
│   ├── assets/         # 樣式文件、圖片等
│   ├── components/     # 可重用組件
│   ├── router/         # 路由配置
│   ├── views/          # 頁面組件
│   ├── App.vue         # 根組件
│   └── main.js         # 入口文件
├── index.html
├── package.json
└── vite.config.js
```

## 功能模組

### 1. 登入系統
- 路徑: `/login`
- 支援帳號密碼登入
- 自動跳轉到主頁面

### 2. 請假管理
- 路徑: `/portal/leave`
- 申請請假（特休/病假/事假）
- 查看個人請假記錄

### 3. 加班管理
- 路徑: `/portal/overtime`
- 申請加班登記
- 查看個人加班記錄

### 4. 人員管理（管理員專用）
- 路徑: `/portal/users`
- 需要 ADMIN 權限
- 查看所有使用者
- 新增/更新使用者

## API 代理配置

開發環境下，Vite 會自動將 `/api` 請求代理到後端 `http://localhost:8080`

生產環境需要在 Nginx 或其他 Web Server 配置反向代理。

## 測試帳號

- **管理員**: admin / admin123
- **主管**: manager / manager123
- **員工**: user / user123

## 注意事項

1. 確保後端服務已在 8080 端口啟動
2. 瀏覽器需支援 ES6+ 語法
3. 開發時請使用 Chrome/Firefox/Edge 等現代瀏覽器
