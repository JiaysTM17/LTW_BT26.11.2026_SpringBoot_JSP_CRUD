@echo off
chcp 65001 >nul
title Spring Boot 3 - 24110032 Truong Gia Kiet
echo ==============================================================================
echo   KHOI DONG HE THONG WEB SPRING BOOT 3 (CONG 8090)
echo   Sinh vien: Truong Gia Kiet - MSSV: 24110032
echo ==============================================================================
echo Dang khoi chay ung dung... Vui long doi trong giay lat...
echo.

if exist "%~dp0Source" (
    cd /d "%~dp0Source"
) else (
    cd /d "%~dp0"
)

set "JAVA_EXE=C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\java.exe"
if not exist "%JAVA_EXE%" set "JAVA_EXE=java"

"%JAVA_EXE%" -Dfile.encoding=UTF-8 -jar "target\24110032_TruongGiaKiet_BT26.11.2026-1.0.0.jar"

pause
