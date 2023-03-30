@echo off
title Quick Setup Git
set /p "link=Enter link repository GitHub: "

echo .
echo -------------------------
echo      Please wait...
echo -------------------------
echo .

set nameapp=%link:*com/=%
set nameapp=%nameapp:*/=%
set nameapp=%nameapp:.git=%

if not exist README.md echo # %nameapp% >> README.md

cmd /c git init
git add .
git commit -m "first commit"
git branch -M main
git remote add origin %link%
git push -u -f origin main

echo .
echo -------------------------
echo  First push successfully
echo -------------------------
echo .

pause
