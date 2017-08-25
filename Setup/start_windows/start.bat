@echo off

echo Start IPPR2016
echo ########################################################## & echo.

echo Start ConfigurationService
echo ########################################################## & echo.
start start_configuration_service.bat
echo. & echo ##########################################################

echo Start ServiceDiscovery
echo ########################################################## & echo.
start start_service_discovery.bat
echo. & echo ##########################################################

echo Start ProcessModelStorage
echo ########################################################## & echo.
start start_pms.bat
echo. & echo ##########################################################

echo Start Gateway
echo ########################################################## & echo.
start start_gateway.bat
echo. & echo ##########################################################

echo Start ProcessEngine
echo ########################################################## & echo.
start start_engine.bat
echo. & echo ##########################################################

echo Start ExternalCommunicator
echo ########################################################## & echo.
start start_ec.bat
echo. & echo ##########################################################

echo Start EventLogger
echo ########################################################## & echo.
start start_event_logger.bat
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