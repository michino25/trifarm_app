@echo off
title Quick Setup Git
set /p "message=Enter commit message: "

echo -------------------------
echo      Please wait...
echo -------------------------

cmd /c git add .
git commit -m "%message%"
git push

echo -------------------------
echo     Push successfully
echo -------------------------

timeout 5