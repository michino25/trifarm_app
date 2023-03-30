@echo off
title Quick Setup Git
set /p "link=Enter link repository GitHub: "

echo -------------------------
echo      Please wait...
echo -------------------------

set nameapp=%link:*com/=%
set nameapp=%nameapp:*/=%
set nameapp=%nameapp:.git=%

echo # %nameapp% >> README.md
cmd /c git init
git add .
git commit -m "first commit"
git branch -M main
git remote add origin %link%
git push -u -f origin main

echo -------------------------
echo  First push successfully
echo -------------------------

pause
