@echo off

echo Start IPPR2016
echo ########################################################## & echo.

echo Start ProcessModelStorage
echo ########################################################## & echo.
start start_pms.bat
echo. & echo ##########################################################


echo Start ProcessEngine
echo ########################################################## & echo.
start start_engine.bat
echo. & echo ##########################################################

echo Start Gateway
echo ########################################################## & echo.
start start_gateway.bat
echo. & echo ##########################################################

echo Start GUI
echo ########################################################## & echo.
start start_gui.bat
echo. & echo ##########################################################

echo Start ModellingPlatform
echo ########################################################## & echo.
start start_mpf.bat
echo. & echo ##########################################################

echo Installation and boot finished
echo ########################################################## & echo.

pause