@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion
title B端客户标注与智能评估系统

cd /d "%~dp0"

echo ============================================================
echo    B端客户标注与智能评估系统 - 启动
echo ============================================================
echo.

REM ---------- 1. 读取本地敏感配置（数据库密码 / API Key） ----------
if not exist "config.local.bat" (
    echo [错误] 未找到 config.local.bat
    echo.
    echo   请把 config.local.bat.example 复制一份并改名为 config.local.bat，
    echo   然后填入本机 MySQL 密码和 DeepSeek API Key。
    echo.
    pause
    exit /b 1
)
call "config.local.bat"

if "%SERVER_PORT%"=="" set "SERVER_PORT=8080"

REM ---------- 2. 检查 Java ----------
java -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到 Java，请先安装 JDK 17 或更高版本。
    echo        下载地址: https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM ---------- 3. 取本机局域网 IP ----------
set "LOCAL_IP="
for /f "tokens=2 delims=:" %%a in ('ipconfig ^| findstr /i "IPv4"') do (
    if not defined LOCAL_IP set "LOCAL_IP=%%a"
)
set "LOCAL_IP=%LOCAL_IP: =%"

echo   本机浏览器访问:   http://localhost:%SERVER_PORT%
if defined LOCAL_IP echo   其他电脑访问:     http://%LOCAL_IP%:%SERVER_PORT%
echo.
echo   首次启动需要编译，看到 "Started ...Application" 就是成功了。
echo   关闭本窗口或按 Ctrl+C 可以停止服务。
echo ------------------------------------------------------------
echo.

REM ---------- 4. 启动服务：优先用已打包的 jar，否则用 Maven Wrapper ----------
if exist "target\deepseek-crm-system-0.0.1-SNAPSHOT.jar" (
    java -jar "target\deepseek-crm-system-0.0.1-SNAPSHOT.jar" --server.port=%SERVER_PORT%
) else (
    call mvnw.cmd spring-boot:run -Dmaven.compiler.proc=full -Dspring-boot.run.arguments=--server.port=%SERVER_PORT%
)

echo.
echo 服务已停止。
pause
