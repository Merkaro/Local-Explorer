@echo off

REM Set the directory paths
set secrets_file=%~dp0secrets.txt
set backend_dir=%~dp0local-explorer-backend
set frontend_dir=%~dp0local-explorer-frontend

REM Read secrets from the file
set APIKEY_OPENAI=
set APIKEY_OPENWEATHER=
set APIKEY_GOOGLE=
for /f "tokens=1,* delims=:" %%a in (%secrets_file%) do (
    if "%%a"=="APIKEY_OPENAI" (
        set "APIKEY_OPENAI=%%b"
    ) else if "%%a"=="APIKEY_OPENWEATHER" (
        set "APIKEY_OPENWEATHER=%%b"
    ) else if "%%a"=="APIKEY_GOOGLE" (
        set "APIKEY_GOOGLE=%%b"
    )
)

REM Read secrets from the file
set APIKEY_OPENAI=
set APIKEY_OPENWEATHER=
set APIKEY_GOOGLE=
for /f "tokens=1,* delims=:" %%a in (%secrets_file%) do (
    if "%%a"=="APIKEY_OPENAI" (
        set "APIKEY_OPENAI=%%b"
    ) else if "%%a"=="APIKEY_OPENWEATHER" (
        set "APIKEY_OPENWEATHER=%%b"
    ) else if "%%a"=="APIKEY_GOOGLE" (
        set "APIKEY_GOOGLE=%%b"
    )
)

echo APIKEY_OPENAI: %APIKEY_OPENAI%
echo APIKEY_OPENWEATHER: %APIKEY_OPENWEATHER%
echo APIKEY_GOOGLE: %APIKEY_GOOGLE%

REM Replace placeholders in application.properties
set "app_properties=%backend_dir%\src\main\resources\application.properties"
if exist "%app_properties%" (
    (
        for /f "usebackq delims=" %%i in ("%app_properties%") do (
            set "line=%%i"
            setlocal enabledelayedexpansion
            set "line=!line:${APIKEY_OPENAI}=%APIKEY_OPENAI%!"
            set "line=!line:${APIKEY_OPENWEATHER}=%APIKEY_OPENWEATHER%!"
            set "line=!line:${APIKEY_GOOGLE}=%APIKEY_GOOGLE%!"
            echo !line!
            endlocal
        )
    ) > "%temp%\temp_application.properties"

    move /y "%temp%\temp_application.properties" "%app_properties%"
    echo Secrets have been updated in %app_properties%
) else (
    echo application.properties not found in %app_properties%
)

REM Replace placeholders in frontend 
set "const_js=%frontend_dir%\src\utils\constants.js"
if exist "%const_js%" (
    (
        for /f "usebackq delims=" %%i in ("%const_js%") do (
            set "line=%%i"
            setlocal enabledelayedexpansion
            set "line=!line:${APIKEY_GOOGLE}=%APIKEY_GOOGLE%!"
            echo !line!
            endlocal
        )
    ) > "%temp%\temp_application.properties"

    move /y "%temp%\temp_application.properties" "%const_js%"
    echo Secrets have been updated in %const_js%
) else (
    echo application.properties not found in %const_js%
)

REM Run the backend
call :run_backend

REM Run the frontend
call :run_frontend

REM Echo the URL for the frontend
echo Frontend running at: http://localhost:3000

goto :eof

:run_backend
cd /d "%backend_dir%"
start cmd /k mvn spring-boot:run
goto :eof

:run_frontend
cd /d "%frontend_dir%"
start cmd /k npm start
goto :eof