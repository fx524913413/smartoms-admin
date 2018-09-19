

SET runpath=%~dp0
SET runpath1=%runpath:~0,2%
rem cd %runpath1%
rem %runpath%
pushd %runpath%

SET CURRENT=%~dp0%
SET LIB=%CURRENT%lib
SET CLASSPATH=%CURRENT%conf;%CURRENT%lib;%CURRENT%res;

:: ---------------------------------------------------------------------
:: Collect JVM options and properties.
:: ---------------------------------------------------------------------
SET OPTION=-Xms128m -Xmx256m -ms128m -mx256m -XX:MaxPermSize=128m -Duser.timezone=GMT+8


java %OPTION%  -jar %runpath%center-gate.jar -Dspring.config.location=%runpath%application.yml>log
