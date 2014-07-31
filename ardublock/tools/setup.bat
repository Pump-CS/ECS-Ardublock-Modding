cd ..
call mvn validate
cd openblocks
call mvn install
cd ..\tools
call mvn install:install-file -Dfile=..\lib\RXTXcomm.jar -DgroupId=local -DartifactId=RXTXcomm -Dversion=1.0 -Dpackaging=jar -DlocalRepositoryPath=..\repo -DcreateChecksum=true
type maven-pom-additions
pause