@echo off
echo ========================================
echo   Smart Job Portal Search System
echo ========================================
echo.
echo Starting Backend (Spring Boot)...
cd backend
start "Smart Job Portal Backend" cmd /c "mvn spring-boot:run"
timeout /t 5 /nobreak > nul
echo.
echo Starting Frontend (React)...
cd ..\frontend
start "Smart Job Portal Frontend" cmd /c "npm start"
echo.
echo ========================================
echo   Both services are starting...
echo   Backend: http://localhost:8080
echo   Frontend: http://localhost:3000
echo ========================================
pause
