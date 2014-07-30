cd ..
call mvn clean package
copy target\ardublock-all.jar %USERPROFILE%\Documents\Arduino\tools\ArduBlockTool\tool
pause