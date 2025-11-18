#!/bin/bash

echo "========================================"
echo "正在構建生產版本（前端內嵌到後端）"
echo "========================================"
echo ""

echo "[1/3] 構建 Vue 前端..."
cd frontend
npm install
npm run build

if [ $? -ne 0 ]; then
    echo "前端構建失敗！"
    exit 1
fi
echo "前端構建完成 ✓"
echo ""

echo "[2/3] 打包 Spring Boot 應用..."
cd ..
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "後端打包失敗！"
    exit 1
fi
echo "後端打包完成 ✓"
echo ""

echo "[3/3] 構建成功！"
echo ""
echo "JAR 檔案位置: target/leave-system-0.0.1-SNAPSHOT.jar"
echo ""
echo "啟動指令: java -jar target/leave-system-0.0.1-SNAPSHOT.jar"
echo "訪問網址: http://localhost:8080"
echo ""
