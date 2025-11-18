# 快速啟動指南（無需 Nginx）

## 方式一：開發模式（前後端分離運行）

適合開發時使用，支援熱更新。

### 1. 啟動後端
```bash
# 在專案根目錄
mvn spring-boot:run
```
後端運行在: http://localhost:8080

### 2. 啟動前端
```bash
cd frontend
npm install   # 首次需要
npm run dev
```
前端運行在: http://localhost:3000

✅ **訪問**: http://localhost:3000

---

## 方式二：生產模式（前端內嵌到後端，單一應用）

適合部署，只需運行一個 Spring Boot 應用，**不需要 Nginx**！

### 1. 構建前端
```bash
cd frontend
npm install   # 首次需要
npm run build
```

這會將前端打包到 `src/main/resources/static/` 目錄

### 2. 啟動 Spring Boot
```bash
# 回到專案根目錄
cd ..
mvn spring-boot:run
```

✅ **訪問**: http://localhost:8080

> 只需一個端口！前端和後端都在 8080

---

## 打包成 JAR（生產部署）

### 1. 構建前端
```bash
cd frontend
npm run build
```

### 2. 打包整個應用
```bash
cd ..
mvn clean package
```

### 3. 運行 JAR
```bash
java -jar target/leave-system-0.0.1-SNAPSHOT.jar
```

✅ **訪問**: http://localhost:8080

---

## 架構說明

### 開發模式
```
瀏覽器
  ↓
http://localhost:3000 (Vite Dev Server)
  ↓ 代理 /api 請求
http://localhost:8080 (Spring Boot)
```

### 生產模式
```
瀏覽器
  ↓
http://localhost:8080 (Spring Boot)
  ├── / → index.html (Vue 前端)
  ├── /assets/* → JS/CSS
  └── /api/* → REST API
```

前端被打包成靜態文件，直接由 Spring Boot 提供服務，無需額外的 Nginx！

---

## 測試帳號

- admin / admin123（管理員）
- manager / manager123（主管）
- user / user123（員工）

---

## 常見問題

### Q: 為什麼有兩種運行方式？

**開發模式** (`npm run dev`):
- ✅ 修改代碼立即看到效果（熱更新）
- ✅ 前後端分離，方便調試
- ❌ 需要運行兩個服務

**生產模式** (`npm run build` + Spring Boot):
- ✅ 只需一個應用，部署簡單
- ✅ 性能更好
- ❌ 修改前端需要重新構建

### Q: 我可以只用開發模式嗎？

可以，但不建議用於正式環境。開發模式的 Vite Dev Server 不適合生產環境。

### Q: 還需要 Nginx 嗎？

**不需要！** 除非你想要：
- 負載均衡
- SSL 終端
- 極致的靜態文件性能

對於中小型項目，Spring Boot 直接提供靜態文件已經足夠。

### Q: 前端代碼改了，為什麼沒變化？

**開發模式**: 應該自動更新，檢查 Vite 是否運行正常
**生產模式**: 需要重新執行 `npm run build`

### Q: 打包後訪問 /portal 返回 404？

確認 `SpaController.java` 已創建，它負責處理 Vue Router 的路由。
