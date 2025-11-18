# 前後端分離部署方案

## 方案 1: Spring Boot 內嵌前端（推薦：最簡單）

### 優點
- ✅ 只需運行一個 Spring Boot 應用
- ✅ 不需要 Nginx
- ✅ 部署最簡單
- ✅ 適合中小型項目

### 缺點
- ❌ 前端更新需要重新打包整個應用
- ❌ Spring Boot 處理靜態文件效率較低

### 配置步驟

#### 1. 修改前端構建輸出路徑

編輯 `frontend/vite.config.js`：
```javascript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  base: '/',
  build: {
    outDir: '../src/main/resources/static',  // 輸出到 Spring Boot 靜態資源目錄
    emptyOutDir: true
  }
})
```

#### 2. 前端構建
```bash
cd frontend
npm run build
```

#### 3. 修改 Spring Security 配置

編輯 `SecurityConfig.java`，允許訪問靜態資源：
```java
.authorizeHttpRequests(auth -> auth
    // 允許訪問前端靜態文件
    .requestMatchers("/", "/index.html", "/assets/**").permitAll()
    // API endpoints
    .requestMatchers("/api/auth/**").permitAll()
    .requestMatchers("/api/admin/**").hasRole("ADMIN")
    .requestMatchers("/api/**").authenticated()
    .anyRequest().authenticated()
)
```

#### 4. 添加 SPA 路由支持

創建 `src/main/java/com/example/leavesystem/controller/SpaController.java`：
```java
package com.example.leavesystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {
    // 處理 Vue Router 的 History 模式路由
    @GetMapping(value = {"/", "/login", "/portal/**"})
    public String spa() {
        return "forward:/index.html";
    }
}
```

#### 5. 啟動應用
```bash
mvn spring-boot:run
```

訪問: http://localhost:8080 （只需一個端口！）

---

## 方案 2: Nginx 反向代理（推薦：生產環境）

### 優點
- ✅ Nginx 處理靜態文件效率高
- ✅ 前後端完全解耦，獨立部署
- ✅ 可配置負載均衡、SSL、緩存
- ✅ 適合大型項目

### 缺點
- ❌ 需要額外安裝和配置 Nginx
- ❌ 部署稍複雜

### Nginx 安裝位置

**選項 A: 與後端同一台伺服器**
```
[伺服器]
├── Nginx (Port 80/443)
│   ├── 提供前端靜態文件
│   └── 反向代理 /api → localhost:8080
└── Spring Boot (Port 8080)
```

**選項 B: 分離部署**
```
[前端伺服器]          [後端伺服器]
Nginx (Port 80)  →  Spring Boot (Port 8080)
提供靜態文件          提供 API
反向代理 /api
```

### 配置步驟

#### 1. 前端構建
```bash
cd frontend
npm run build
# 生成 dist/ 目錄
```

#### 2. 部署前端靜態文件
```bash
# 複製到 Nginx 目錄
sudo cp -r frontend/dist/* /usr/share/nginx/html/
# 或自定義目錄
sudo cp -r frontend/dist/* /var/www/leave-system/
```

#### 3. Nginx 配置文件

創建 `/etc/nginx/sites-available/leave-system.conf`：
```nginx
server {
    listen 80;
    server_name your-domain.com;  # 或使用 IP

    # 前端靜態文件
    root /usr/share/nginx/html;
    index index.html;

    # Vue Router History 模式支持
    location / {
        try_files $uri $uri/ /index.html;
    }

    # API 反向代理到後端
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 靜態資源緩存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

#### 4. 啟用配置
```bash
sudo ln -s /etc/nginx/sites-available/leave-system.conf /etc/nginx/sites-enabled/
sudo nginx -t  # 測試配置
sudo systemctl reload nginx
```

#### 5. 啟動後端
```bash
java -jar target/leave-system.jar
```

訪問: http://your-domain.com

---

## 方案 3: 使用 Docker Compose（推薦：容器化）

### 優點
- ✅ 一鍵部署
- ✅ 環境一致性
- ✅ 易於擴展

### Docker Compose 配置

創建 `docker-compose.yml`：
```yaml
version: '3.8'

services:
  # 後端
  backend:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/leave_system
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=password
    depends_on:
      - mysql

  # 前端 + Nginx
  frontend:
    image: nginx:alpine
    ports:
      - "80:80"
    volumes:
      - ./frontend/dist:/usr/share/nginx/html
      - ./nginx.conf:/etc/nginx/conf.d/default.conf
    depends_on:
      - backend

  # 資料庫
  mysql:
    image: mysql:8
    environment:
      - MYSQL_ROOT_PASSWORD=password
      - MYSQL_DATABASE=leave_system
    volumes:
      - mysql-data:/var/lib/mysql

volumes:
  mysql-data:
```

啟動:
```bash
docker-compose up -d
```

---

## 方案對比

| 特性 | 方案1: 內嵌 | 方案2: Nginx | 方案3: Docker |
|------|------------|--------------|---------------|
| 部署難度 | ⭐ 簡單 | ⭐⭐ 中等 | ⭐⭐⭐ 複雜 |
| 性能 | 中 | 高 | 高 |
| 獨立部署 | ❌ | ✅ | ✅ |
| 適用場景 | 開發/小型項目 | 生產環境 | 企業級 |
| 需要工具 | 無 | Nginx | Docker |

---

## 我的推薦

### 如果你是...

**1. 學習/個人項目** → 方案1（內嵌）
- 最簡單，一個命令啟動

**2. 公司生產環境** → 方案2（Nginx）
- 標準做法，性能好

**3. 有 DevOps 團隊** → 方案3（Docker）
- 專業化，易維護

---

## Windows 開發環境 Nginx 安裝（可選）

如果你想在 Windows 上測試 Nginx：

### 1. 下載 Nginx
https://nginx.org/en/download.html

### 2. 解壓到目錄
```
C:\nginx\
```

### 3. 修改配置
編輯 `C:\nginx\conf\nginx.conf`（參考方案2的配置）

### 4. 啟動
```cmd
cd C:\nginx
start nginx
```

### 5. 停止
```cmd
nginx -s stop
```

但開發環境其實不需要 Nginx，Vite Dev Server 已經夠用了！
