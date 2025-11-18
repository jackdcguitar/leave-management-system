# 請假管理系統 (前後端分離版本)

基於 Spring Boot 3 + Vue 3 的企業請假管理系統，採用前後端分離架構。

## 技術架構

### 後端
- Spring Boot 3.3.2
- Spring Security (Session-based 認證)
- Spring Data JPA + Hibernate
- MySQL 8
- Java 17

### 前端
- Vue 3 (Options API)
- Vue Router 4
- Axios
- Bootstrap 5
- Vite

## 快速開始

### 前置要求

- JDK 17+
- Maven 3.6+
- Node.js 18+
- MySQL 8.0+

### 1. 資料庫設置

```sql
CREATE DATABASE leave_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
```

### 2. 後端配置

編輯 `src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/leave_system?useSSL=false&serverTimezone=Asia/Taipei&allowPublicKeyRetrieval=true&characterEncoding=utf8
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. 啟動後端

```bash
# 在專案根目錄執行
mvn spring-boot:run
```

後端將在 `http://localhost:8080` 啟動

### 4. 啟動前端

```bash
# 進入前端目錄
cd frontend

# 安裝依賴（首次需要）
npm install

# 啟動開發伺服器
npm run dev
```

前端將在 `http://localhost:3000` 啟動

### 5. 訪問系統

打開瀏覽器訪問: http://localhost:3000

**測試帳號:**
- 管理員: `admin` / `admin123`
- 主管: `manager` / `manager123`
- 員工: `user` / `user123`

## 專案結構

```
leave-management-system/
├── src/main/java/              # 後端 Java 代碼
│   └── com/example/leavesystem/
│       ├── config/             # 配置類（Security, CORS）
│       ├── controller/         # REST API 控制器
│       ├── entity/             # JPA 實體類
│       ├── model/              # 資料模型
│       ├── repository/         # 資料存取層
│       └── service/            # 業務邏輯層
├── src/main/resources/
│   ├── templates/              # Thymeleaf 模板（舊版，可選）
│   ├── schema.sql              # 資料庫結構
│   ├── data.sql                # 初始資料
│   └── application.properties  # Spring Boot 配置
├── frontend/                   # Vue 3 前端專案
│   ├── src/
│   │   ├── api/               # API 接口定義
│   │   ├── components/        # Vue 組件
│   │   ├── views/             # 頁面組件
│   │   ├── router/            # 路由配置
│   │   └── assets/            # 靜態資源
│   ├── package.json
│   └── vite.config.js
└── pom.xml                     # Maven 配置
```

## API 端點

### 認證 API
- `POST /api/auth/login` - 登入
- `POST /api/auth/logout` - 登出
- `GET /api/auth/current-user` - 獲取當前用戶信息

### 請假 API
- `GET /api/leaves` - 獲取我的請假記錄
- `POST /api/leaves` - 申請請假
- `PUT /api/leaves/{id}/status` - 更新請假狀態（待實作）

### 加班 API
- `GET /api/overtimes` - 獲取我的加班記錄（待實作）
- `POST /api/overtimes` - 申請加班（待實作）

### 人員管理 API（需 ADMIN 權限）
- `GET /api/admin/users` - 獲取所有使用者
- `POST /api/admin/users` - 新增使用者（待實作）
- `PUT /api/admin/users/{id}` - 更新使用者（待實作）

## 功能特性

### 已完成
- ✅ 使用者登入/登出
- ✅ 請假申請與記錄查詢
- ✅ 前後端分離架構
- ✅ CORS 跨域支援
- ✅ Session-based 認證
- ✅ 角色權限控制（USER/MANAGER/ADMIN）
- ✅ 響應式 UI（Bootstrap 5）

### 待開發
- ⏳ 請假審核功能（主管/管理員核准/拒絕）
- ⏳ 加班管理完整實作
- ⏳ 人員管理的新增/編輯功能
- ⏳ 請假額度管理
- ⏳ 報表統計功能
- ⏳ 輸入驗證與業務規則
- ⏳ 單元測試

## 開發說明

### 後端開發
- 所有 REST API 控制器位於 `controller` 包
- API 路徑統一使用 `/api` 前綴
- 使用 `@CrossOrigin` 支援跨域請求
- Spring Security 配置在 `SecurityConfig.java`

### 前端開發
- 使用 Vue 3 Options API 風格
- API 調用統一在 `src/api/index.js` 定義
- 路由配置在 `src/router/index.js`
- Vite 開發模式下自動代理 `/api` 到後端

### CORS 配置
後端已配置允許來自 `http://localhost:3000` 的跨域請求，生產環境需修改 `SecurityConfig.java` 中的 `allowedOrigins`。

## 生產部署

### 後端
```bash
mvn clean package
java -jar target/leave-system-0.0.1-SNAPSHOT.jar
```

### 前端
```bash
cd frontend
npm run build
# 將 dist/ 目錄部署到 Nginx 或其他 Web Server
```

### Nginx 配置範例
```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端靜態文件
    location / {
        root /path/to/frontend/dist;
        try_files $uri $uri/ /index.html;
    }

    # API 反向代理
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 常見問題

### 1. 前端無法連接後端
- 確認後端已啟動在 8080 端口
- 檢查 CORS 配置是否正確
- 查看瀏覽器控制台的錯誤訊息

### 2. 登入後無法訪問其他頁面
- 確認 Cookie 設置正確（withCredentials: true）
- 檢查 Session 是否正常保存

### 3. ADMIN 權限頁面無法訪問
- 確認使用管理員帳號登入（admin/admin123）
- 檢查後端角色配置是否正確

## 授權

MIT License

## 聯絡方式

如有問題請提交 Issue
