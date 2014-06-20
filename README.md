This project is based on [Ardublock](https://github.com/taweili/ardublock).

**Resources:**
*   [Ardublock Home](http://blog.ardublock.com/)
*   [Adding Blocks Tutorial](http://www.hack-e-bot.com/how-to-create-a-new-ardublock/)

**Installation:**	
1.  Download [Maven](http://maven.apache.org/download.cgi)	
2.  Download [Java](http://www.oracle.com/technetwork/java/index.html)	
3.  Follow the Maven [installation instructions](http://maven.apache.org/download.cgi)	
4.  In the project directory, run	
    * mvn validate


5.  If it isn't already included, download [Openblocks](https://github.com/taweili/openblocks) to ardublock/openblocks
6.  In ardublock/openblocks, run	
    * mvn install


7.  In the project directory, run	
    * mvn clean package
    * mvn compile exec:java -Dexec.mainClass="com.ardublock.Main"
8.  After modifying the code, run	
    * mvn clean package
9.  Copy target/ardublock-all.jar to Arduino/tools/ArduBlockTool/tool