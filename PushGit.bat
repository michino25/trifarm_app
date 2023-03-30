@echo off
title Quick Setup Git
set /p "message=Enter commit message: "

echo .
echo -------------------------
echo      Please wait...
echo -------------------------
echo .

cmd /c git add .
git commit -m "%message%"
git push

echo .
echo -------------------------
echo     Push successfully
echo -------------------------
echo .

timeout 5