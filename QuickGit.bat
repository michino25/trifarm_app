ECHO OFF
title Quick Git
cmd /c git config --global --unset-all remote.origin.proxy
%SystemRoot%\System32\chcp.com 65001 >nul
CLS

:MENU
ECHO.
ECHO -----------------------------------------------
ECHO                    Quick Git                   
ECHO -----------------------------------------------
ECHO.
ECHO   1 - Push project lên GitHub lần đầu
ECHO   2 - Push project lên GitHub
ECHO.
ECHO   3 - Pull dự án trên GitHub về máy
ECHO   4 - Clone dự án trên GitHub về máy
ECHO.
ECHO   5 - Hướng dẫn
ECHO   0 - Thoát
ECHO.

SET /P M=Chọn chức năng: 
echo.
IF %M%==1 GOTO PUSHFIRST
IF %M%==2 GOTO PULLPUSH
IF %M%==3 GOTO PULL
IF %M%==4 GOTO CLONE
IF %M%==5 GOTO HELP
IF %M%==0 GOTO EOF

:PUSHFIRST
echo Tạo repository GitHub mới
set /p "link=Nhập link repository GitHub đó: "

echo.
echo -----------------------------------------------
echo                   Đang tải...                  
echo -----------------------------------------------
echo.

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

echo.
echo -----------------------------------------------
echo             Push lần đầu thành công            
echo -----------------------------------------------
echo.

GOTO MENU

:PUSH
set /p "message=Nhập commit message: "

echo.
echo -----------------------------------------------
echo                   Đang tải...                  
echo -----------------------------------------------
echo.

cmd /c git add .
git commit -m "%message%"
git push

echo.
echo -----------------------------------------------
echo                 Push thành công                
echo -----------------------------------------------
echo.

GOTO MENU

:PULLPUSH
set /p "message=Nhập commit message: "

echo.
echo -----------------------------------------------
echo                   Đang tải...                  
echo -----------------------------------------------
echo.

cmd /c git add .
git commit -m "%message%"
git pull
git push

echo.
echo -----------------------------------------------
echo                 Push thành công                
echo -----------------------------------------------
echo.

GOTO MENU

:PULL

echo.
echo -----------------------------------------------
echo                   Đang tải...                  
echo -----------------------------------------------
echo.

cmd /c git add .
git commit -m "commit to pull"
git pull

echo.
echo -----------------------------------------------
echo                 Pull thành công                
echo -----------------------------------------------
echo.

GOTO MENU

:CLONE

set /p "link=Nhập link GitHub: "

echo.
echo -----------------------------------------------
echo                   Đang tải...                  
echo -----------------------------------------------
echo.

cmd /c git clone %link%

echo.
echo -----------------------------------------------
echo                 Clone thành công               
echo -----------------------------------------------
echo.

GOTO MENU

:HELP

echo.
echo -----------------------------------------------
echo                    Hướng dẫn                   
echo -----------------------------------------------
echo.

echo Cây thư mục demo
echo ^|_ workspace
echo 	^|_ project_a
echo 	^|_ project_b
echo 	^|_ demo_project
echo 	^|_ ...

echo.
echo Khi muốn clone, đặt file QuickGit vào thư mục cha.
echo Ví dụ: Muốn clone 'demo_project' từ GitHub về và đặt folder dự án vào folder 'workspace'
echo ^=^> Đặt file QuickGit vào thư mục 'workspace' và dùng chức năng clone (4)
echo.
echo Khi muốn pull hoặc push, đặt file QuickGit vào thư mục dự án.
echo Ví dụ: Muốn push demo_project lên GitHub
echo ^=^> Đặt file QuickGit vào thư mục 'demo_project' và dùng chức năng push (2)
echo.
echo Lưu ý: Phần mềm chưa xử lý được các trường hợp conflict khi pull code

echo.
echo -----------------------------------------------
echo             Phát triển bởi Michittio           
echo             github.com/michittio
echo -----------------------------------------------
echo.

pause
GOTO MENU
