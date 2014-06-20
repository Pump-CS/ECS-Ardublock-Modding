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
7. Step 6 should produce `target/ardublock-all.jar`, copy this to `Arduino/tools/ArduBlockTool/tool`[^note].
8. Start the arduino application as you would normally, go to Tools -> ArduBlock.

[^note]: This path will vary depending on which OS you're running. You can find this path by starting the Ardunio application and then going to file -> preferences. The root of the path you need to copy ardublock-all.jar to will be the path under "Sketchbook location". If your Sketchbook location is /home/name/sketchbook then you need to copy ardublook-all.jar to /home/name/sketchbook/tools/ArduBlockTool/tool (you may need to create some of these directories).
