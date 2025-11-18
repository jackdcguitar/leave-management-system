<<<<<<< HEAD
# Leave System Portal (Spring Boot 3 + MySQL + Security + HTMX)

## Quick Start
1. MySQL 建 DB：
   ```sql
   CREATE DATABASE leave_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
   ```
2. 在 `src/main/resources/application.properties` 修改：
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/leave_system?useSSL=false&serverTimezone=Asia/Taipei&allowPublicKeyRetrieval=true&characterEncoding=utf8
   spring.datasource.username=root
   spring.datasource.password=your_password_here
   ```
3. 啟動：
   ```bash
   mvn spring-boot:run
   ```
4. 登入：`http://localhost:8080/login`
   - admin/admin123（ADMIN+USER）
   - manager/manager123（MANAGER+USER）
   - user/user123（USER）

登入後進 `/portal`：左邊固定選單（請假、加班、〔ADMIN 才有〕人員管理），右邊 HTMX 載片段，不跳窗、不整頁刷新。

## 重點設定
- MySQL driver：8.4.0（相容 MySQL 8.0.42）
- Session：Spring Session JDBC（持久化、timeout=30m、SameSite=Lax）
- 權限：`/admin/**` 需 `ROLE_ADMIN`
=======
# leave-management-system
請假系統
>>>>>>> f4a5bd304fe80f2cb1fcf811d9bc95f60546d05b
