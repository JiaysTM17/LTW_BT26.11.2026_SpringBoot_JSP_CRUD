@echo off
chcp 65001 >nul
title Spring Boot 3 - 24110032 Truong Gia Kiet
echo ==============================================================================
echo   KHOI DONG HE THONG WEB SPRING BOOT 3 (CONG 8088)
echo   Sinh vien: Truong Gia Kiet - MSSV: 24110032
echo ==============================================================================
echo Dang khoi chay ung dung... Vui long doi trong giay lat...
echo.

set "JAVA_EXE=C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\java.exe"
if not exist "%JAVA_EXE%" set "JAVA_EXE=java"

"%JAVA_EXE%" -Dfile.encoding=UTF-8 -jar "%~dp0target\24110032_TruongGiaKiet_BT26.11.2026-1.0.0.jar"

pause
