@echo off

echo Start ServiceDiscovery
echo ########################################################## & echo.

cd ../../ServiceDiscovery
call gradlew bootRun

pause