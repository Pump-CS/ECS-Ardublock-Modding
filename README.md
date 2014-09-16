This project is based on [Ardublock](https://github.com/taweili/ardublock).

####Resources:
*   [Ardublock Home](http://blog.ardublock.com/)
*   [Adding Blocks Tutorial 1](http://www.hack-e-bot.com/how-to-create-a-new-ardublock/)
*   [Adding Blocks Tutorial 2](http://blog.ardublock.com/2012/05/04/how-to-hack-ardublock/)

####Installation:
1. Download [Maven](http://maven.apache.org/download.cgi)
2. Download [JavaJDK](http://www.oracle.com/technetwork/java/index.html)
3. Follow the Maven [installation instructions](http://maven.apache.org/download.cgi)
4. In the ardublock directory, run `mvn validate`.
5. Add execute permissions to `install_openblocks` (`chmod u+x install_openblocks`) and then run `./install_openblocks`.
6. In ardublock/openblocks, run `mvn install`.
7. After modifying the code, run `mvn clean package` in the `ardublock` directory.
8. Step 6 should produce `target/ardublock-all.jar`, copy this to 
  * Mac OS X & Windows: `Documents/Arduino/tools/ArduBlockTool/tool`
  * Linux: `~/sketchbook/tools/ArduBlockTool/tool`
  * If these paths don't work, then use the path found in `File` > `Preferences` in the Arduino application as the root and append `tools/ArduBlockTool/tool`
9. Start the arduino application as you would normally and navigate to `Tools` > `ArduBlock`.
