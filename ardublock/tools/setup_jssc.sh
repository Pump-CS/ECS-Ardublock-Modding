cd ..
mvn validate
cd openblocks
mvn install
cd ../tools
mvn install:install-file -Dfile=../lib/jssc-2.8.0.jar -DgroupId=local -DartifactId=jssc -Dversion=1.0 -Dpackaging=jar -DlocalRepositoryPath=../repo -DcreateChecksum=true
cat maven-pom-additions
