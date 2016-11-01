@echo off

echo Installation and boot IPPR2016
echo ########################################################## & echo.


echo Installation ProcessModelStorage
echo ########################################################## & echo.

cd ../ProcessModelStorage
call gradlew clean
cd ../Setup
start start_ProcessModelStorage.bat

echo. & echo ##########################################################


echo Installation ProcessModelStorage
echo ########################################################## & echo.

cd ../ProcessEngine
call gradlew clean
cd ../Setup
start start_ProcessEngine.bat

echo. & echo ##########################################################
echo Installation and boot finished
echo ########################################################## & echo.

pause