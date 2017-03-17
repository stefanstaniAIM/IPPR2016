@echo off

echo Start ConfigurationService
echo ########################################################## & echo.

cd ../../ConfigurationService
call gradlew bootRun

pause