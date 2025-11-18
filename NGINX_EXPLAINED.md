# Nginx 在哪裡？為什麼需要它？

## 🎯 簡單回答

**你現在不需要 Nginx！**

我已經幫你配置了兩種運行方式：
1. **開發模式**: 直接用 Vite（前端）+ Spring Boot（後端）
2. **生產模式**: 前端打包進 Spring Boot，只運行一個應用

---

## 📚 詳細解釋

### Nginx 是什麼？

Nginx 是一個 **Web 伺服器軟體**，類似於：
- Apache（你可能聽過）
- IIS（Windows 的 Web Server）
- Tomcat（但 Nginx 更快）

### Nginx 的作用

```
                    [傳統部署方式]

用戶瀏覽器 → Nginx (Port 80) → 分發請求
              ↓                    ↓
        靜態文件(.html/.js)    反向代理 → Spring Boot (Port 8080)
        (前端)                              (後端 API)
```

**為什麼要這樣做？**
- Nginx 處理靜態文件（HTML/CSS/JS）速度快
- Spring Boot 專注處理業務邏輯（API）
- 前後端完全分離，可以獨立部署

### Nginx 會裝在哪裡？

**選項 1: 同一台伺服器**
```
[你的伺服器 - Ubuntu/CentOS]
├── Nginx (80 端口) ← 用戶訪問這裡
│   ├── /usr/share/nginx/html/index.html (前端)
│   └── 代理 /api → localhost:8080
└── Spring Boot (8080 端口)
```

**選項 2: 不同伺服器**
```
[伺服器 A]              [伺服器 B]
Nginx (前端)    →      Spring Boot (API)
```

---

## 🚀 你的專案配置（無需 Nginx）

我已經設定了更簡單的方案：

### 方案：前端內嵌到 Spring Boot

```
用戶瀏覽器 → Spring Boot (8080) → 處理所有請求
              ↓                       ↓
        靜態文件 (前端)           API (後端)
        src/main/resources/static/
```

**優點：**
- ✅ 無需安裝 Nginx
- ✅ 只需部署一個 JAR 檔案
- ✅ 部署超級簡單

**缺點：**
- ❌ 靜態文件效率比 Nginx 稍低（但對中小型項目無影響）
- ❌ 前端更新需要重新打包

---

## 📂 文件位置對比

### 使用 Nginx 的方式

**前端部署位置：**
```
Linux: /usr/share/nginx/html/
       /var/www/html/
Windows: C:\nginx\html\
```

**Nginx 配置文件：**
```
Linux: /etc/nginx/nginx.conf
       /etc/nginx/sites-available/
Windows: C:\nginx\conf\nginx.conf
```

**Spring Boot：**
```
任意位置運行 JAR
```

### 你現在的方式（無 Nginx）

**前端構建後位置：**
```
src/main/resources/static/
├── index.html
└── assets/
    ├── index-xxx.js
    └── index-xxx.css
```

**Spring Boot 打包後：**
```
target/leave-system.jar
└── 包含前端和後端所有代碼
```

只需要這一個檔案！

---

## 🔄 何時需要 Nginx？

### 不需要 Nginx 的情況
- ✅ 中小型專案（日訪問量 < 10萬）
- ✅ 企業內部系統
- ✅ 快速原型/MVP
- ✅ 個人項目

### 建議使用 Nginx 的情況
- 📈 大流量網站（日訪問量 > 100萬）
- 🔒 需要 SSL/HTTPS（雖然 Spring Boot 也能做）
- ⚖️ 需要負載均衡（多台後端伺服器）
- 🌐 需要 CDN 加速靜態資源
- 🚀 極致性能優化

---

## 💡 總結

### 你的問題："為何要透過 nginx 你把 nginx放在哪"

**答案 1**: 你不需要 Nginx！我已經配置好前端內嵌到 Spring Boot 了。

**答案 2**: 如果未來需要 Nginx，它會安裝在：
- **Linux 伺服器**: `/usr/share/nginx/` 或 `/etc/nginx/`
- **Windows 伺服器**: `C:\nginx\`
- **Docker**: 作為容器運行

**答案 3**: Nginx 只是一個可選的優化方案，不是必需的。

---

## 🎓 類比說明

想像你開一家餐廳：

### 方式 1: 無 Nginx（你現在的設置）
```
餐廳 (Spring Boot)
├── 接待員（提供菜單 = 前端 HTML）
└── 廚師（做菜 = 後端 API）
```
一個地方搞定，簡單直接。

### 方式 2: 有 Nginx
```
大廳 (Nginx)
├── 接待員（只提供菜單 = 前端）
└── 轉達訂單給 →

廚房 (Spring Boot)
└── 廚師（專心做菜 = 後端 API）
```
分工明確，效率更高，但需要兩個地方。

對於小餐廳（小專案），方式 1 足夠了！

---

## 📋 快速指令

### 開發（前後端分離）
```bash
# 後端
mvn spring-boot:run

# 前端（另一個終端）
cd frontend
npm run dev
```

### 生產（前端內嵌，無需 Nginx）
```bash
# 一鍵構建
./build-production.bat   # Windows
./build-production.sh    # Linux/Mac

# 運行
java -jar target/leave-system.jar
```

就這麼簡單！
