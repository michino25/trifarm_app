ECHO OFF
CLS

:MENU
ECHO.
ECHO ...............................................
ECHO Quick Setup GitHub
ECHO ...............................................
ECHO.
ECHO 1 - Push project lên GitHub lần đầu tiên
ECHO 2 - Push project lên GitHub thông thường
ECHO 3 - Pull project GitHub về máy
ECHO 4 - Clone project GitHub về máy
ECHO 5 - Thoát
ECHO.

SET /P M=Type 1, 2, 3, 4, or 5 then press ENTER:
IF %M%==1 GOTO PUSHFIRST
IF %M%==2 GOTO PUSH
IF %M%==3 GOTO PULL
IF %M%==3 GOTO CLONE
IF %M%==4 GOTO EOF

:PUSHFIRST
cd %windir%\system32\notepad.exe
start notepad.exe
GOTO MENU

:CALC
cd %windir%\system32\calc.exe
start calc.exe
GOTO MENU

:BOTH
cd %windir%\system32\notepad.exe
start notepad.exe
cd %windir%\system32\calc.exe
start calc.exe
GOTO MENU
