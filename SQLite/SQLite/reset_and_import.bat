@echo off
echo ===============================
echo  DatLich App Database Reset Tool
echo ===============================
echo.

set DB_FILE=datlichmonan_app.db

if exist %DB_FILE% (
    echo Xoa database cu...
    del %DB_FILE%
)

echo Dang tao database moi va nap du lieu...
sqlite3 %DB_FILE% ".read schema.sql" ".read insert_data.sql"

echo.
echo Da hoan thanh! Database %DB_FILE% da duoc tao lai.
pause
