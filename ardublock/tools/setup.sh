cd ..
mvn validate
cd openblocks
mvn install
cd ../tools
mvn install:install-file -Dfile=../lib/RXTXcomm.jar -DgroupId=local -DartifactId=RXTXcomm -Dversion=1.0 -Dpackaging=jar -DlocalRepositoryPath=../repo -DcreateChecksum=true
cat maven-pom-additions
