This project is based on [Ardublock](https://github.com/taweili/ardublock).

####Resources:
*   [Ardublock Home](http://blog.ardublock.com/)
*   [Adding Blocks Tutorial](http://www.hack-e-bot.com/how-to-create-a-new-ardublock/)

####Installation:
1. Download [Maven](http://maven.apache.org/download.cgi)
2. Download [JavaJDK](http://www.oracle.com/technetwork/java/index.html)
3. Follow the Maven [installation instructions](http://maven.apache.org/download.cgi)
4. In the project directory, run `mvn validate` and then `./install_openblocks`.
5. In ardublock/openblocks, run `mvn install`.
6. After modifying the code, run `mvn clean package`.
7. Step 6 should produce `target/ardublock-all.jar`, copy this to 
  * Mac OS X & Windows: `Documents/Arduino/tools/ArduBlockTool/tool`
  * Linux: `~/sketchbook/tools/ArduBlockTool/tool`
  * If these paths don't work, then use the path found in File > Preferences in the Arduino application as the root and append `tools/ArduBlockTool/tool`
8. Start the arduino application as you would normally and navigate to Tools -> ArduBlock.
