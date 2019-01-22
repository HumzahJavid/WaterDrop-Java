# WaterDrop-Java
WaterDrop 2D puzzle game (Java port):
This is a Java port of a 2D puzzle game I created in JavaScript which [can be found in one of my github repositories](https://github.com/HumzahJavid/portfolio/tree/master/Websites/%233%20WaterDrop). The aim is to fully implement the features in the game I originally created. 

The game levels consists of the following "pipes"
**Non-rotatable**: Start and End. 
**Rotatable**: Of which there are Three different styles of pipe. 

### Aim of the game
The aim is to click the rotatable pipes (which causes it to rotate 90 degrees) and to create a path of pipes between the Start and End pipes. Once the path has been generated the level is complete. The next level will have an additional row of rotatable pipes. This is designed to be an endless game. 

### Random levels
The levels are generated randomly (each pipe placement is randomly generated) , as such some of these levels cannot be completed, In the original JavaScript version, When a level is completed it is stored in its unsolved state, when an unsolveable level is skipped a previously stored(solveable) level is loaded. 

### Intended improvements
In addition to all of the features from the original game, I intend to implement a way to detect whether a level is solveable or not without relying on the user. This will most likely be done through a search algorithm such as Djikstra or A* (potentially a classification to try and understand what makes a level unsolveable)

### Extract of the manual from the original JavaScript pipe game

<p> <strong> Aim: </strong> The aim of the game is to create a path between the <a class="start">START</a> pipe, and the <a class="end">END</a> pipe using the other pipes on the screen. You create a path between the two pipes by clicking on the other pipes to rotate them and make sure they are connected to the neighbouring pipes.</p>
    <p> <strong> Next level:</strong> Once you have created a path, click on the <a class="start"> START </a> pipe to proceed to the next level.</p><!--If you attempt to click on the <a class="start"> START </a> pipe without a valid path between the two pipes    , nothing will happen-->
    <p> <strong> Skipping a level:</strong> Click on the <a class="end">END</a> pipe at any time to skip the current level. You will replay a level with the same level number, but it will be a randomly generated level.</p>
    <p> <strong> Exiting the game:</strong> Press the Escape key on the keyboard (you may need to click on outside the game screen)</p>
    <p> <strong> Scoring:</strong> Your score is calculated using the number of moves (rotations of a pipe) and time taken to complete a level, the faster you complete you a level, and the less moves you take means its a higher ranking score</p>
    <p> The <a class="start">START</a> and <a class="end">END</a> pipes cannot be rotated, and do not contribute to your score</p> 

## Update regarding Java OpenJDK and OpenJFX (11)
1. The compile instructions for windows 7/10 do not work for the latest version of the open source java implementations
### Attempt at compiling and running so far:
1. Copy the repository files to the javafx-sdk-11.X.X folder (OpenJFX download). Note: *Merge the contents of the lib folders*
1. The following commands are shown with the prompt and run from ```C:\javafx-sdk-11.0.2>```
1. Compiling the source code, (both commands successfully compile, but would prefer the second option, as it's similar to the older compilation instructions))
```
C:\javafx-sdk-11.0.2>javac -cp ".;lib/*" WaterDrop.java
C:\javafx-sdk-11.0.2>javac -cp ".;lib/*" *.java
```

1. Attempts to run the program (using older instruction) gives the following output
```
C:\javafx-sdk-11.0.2>java -cp ".;lib/*" WaterDrop
Error: JavaFX runtime components are missing, and are required to run this application
```
1. Investigating dependancy list 
```
C:\javafx-sdk-11.0.2>jdeps --module-path lib -s WaterDrop.class
WaterDrop.class -> gson
WaterDrop.class -> java.base
WaterDrop.class -> javafx.base
WaterDrop.class -> javafx.graphics
com -> java.base
com -> objenesis
gson -> java.base
gson -> java.sql
javafx.base -> java.base
javafx.base -> java.desktop
javafx.controls -> java.base
javafx.controls -> javafx.base
javafx.controls -> javafx.graphics
javafx.fxml -> java.base
javafx.fxml -> java.scripting
javafx.fxml -> java.xml
javafx.fxml -> javafx.base
javafx.fxml -> javafx.graphics
javafx.graphics -> java.base
javafx.graphics -> java.desktop
javafx.graphics -> java.xml
javafx.graphics -> javafx.base
javafx.graphics -> jdk.unsupported
javafx.media -> JDK removed internal API
javafx.media -> java.base
javafx.media -> javafx.base
javafx.media -> javafx.graphics
javafx.swing -> java.base
javafx.swing -> java.datatransfer
javafx.swing -> java.desktop
javafx.swing -> javafx.base
javafx.swing -> javafx.graphics
javafx.swing -> jdk.unsupported.desktop
javafx.swt -> java.base
javafx.swt -> javafx.base
javafx.swt -> javafx.graphics
javafx.swt -> not found
javafx.web -> java.base
javafx.web -> java.desktop
javafx.web -> java.xml
javafx.web -> javafx.base
javafx.web -> javafx.controls
javafx.web -> javafx.graphics
javafx.web -> javafx.media
javafx.web -> jdk.jsobject
javafx.web -> jdk.xml.dom
objenesis -> java.base
objenesis -> jdk.unsupported
```

1. Running add modules (inspired by source) allows part of the code to run
```
C:\javafx-sdk-11.0.2>java -p lib --add-modules javafx.controls WaterDrop
numColumns 14
numRows 7
numPipes62
Exception in Application start method
java.lang.reflect.InvocationTargetException
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.base/java.lang.reflect.Method.invoke(Method.java:566)
        at javafx.graphics/com.sun.javafx.application.LauncherImpl.launchApplicationWithArgs(LauncherImpl.java:464)
        at javafx.graphics/com.sun.javafx.application.LauncherImpl.launchApplication(LauncherImpl.java:363)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.base/java.lang.reflect.Method.invoke(Method.java:566)
        at java.base/sun.launcher.LauncherHelper$FXHelper.main(LauncherHelper.java:1051)
Caused by: java.lang.RuntimeException: Exception in Application start method
        at javafx.graphics/com.sun.javafx.application.LauncherImpl.launchApplication1(LauncherImpl.java:900)
        at javafx.graphics/com.sun.javafx.application.LauncherImpl.lambda$launchApplication$2(LauncherImpl.java:195)
        at java.base/java.lang.Thread.run(Thread.java:834)
Caused by: java.lang.NoClassDefFoundError: com/rits/cloning/Cloner
        at Grid.<init>(Grid.java:38)
        at WaterDrop.start(WaterDrop.java:120)
        at javafx.graphics/com.sun.javafx.application.LauncherImpl.lambda$launchApplication1$9(LauncherImpl.java:846)
        at javafx.graphics/com.sun.javafx.application.PlatformImpl.lambda$runAndWait$12(PlatformImpl.java:455)
        at javafx.graphics/com.sun.javafx.application.PlatformImpl.lambda$runLater$10(PlatformImpl.java:428)
        at java.base/java.security.AccessController.doPrivileged(Native Method)
        at javafx.graphics/com.sun.javafx.application.PlatformImpl.lambda$runLater$11(PlatformImpl.java:427)
        at javafx.graphics/com.sun.glass.ui.InvokeLaterDispatcher$Future.run(InvokeLaterDispatcher.java:96)
        at javafx.graphics/com.sun.glass.ui.win.WinApplication._runLoop(Native Method)
        at javafx.graphics/com.sun.glass.ui.win.WinApplication.lambda$runLoop$3(WinApplication.java:174)
        ... 1 more
Caused by: java.lang.ClassNotFoundException: com.rits.cloning.Cloner
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:583)
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:521)
        ... 11 more
Exception running application WaterDrop
```
1. possible fix something like this:
```
C:\javafx-sdk-11.0.2>java -p lib --add-modules javafx.controls,Cloner WaterDrop
```
2. Or

```
C:\javafx-sdk-11.0.2>java -p lib --add-modules * WaterDrop
```


1. Source (inspiration): https://skrb.hatenablog.com/entry/2018/05/31/210000
# Old compilation instructions :
## Compile instructions (windows 7/11)
### Using Command Prompt/PowerShell:
1. Download the repository, extract the files(if necessary)
```
git clone https://github.com/HumzahJavid/WaterDrop-Java
```
1. Navigate to the folder of the downloaded repository
1. Enter the following Two commands to compile and run the game in chosen command line
```
javac -cp ".;lib/*" *.java
java -cp ".;lib/*" WaterDrop
```
## Compile instructions (macOS High Sierra)
### Using Terminal:
1. Download the repository, extract the files(if necessary)
```
git clone https://github.com/HumzahJavid/WaterDrop-Java
```
1. Navigate to the folder of the downloaded repository
1. Enter the following Two commands to compile and run the game in chosen command line
```
javac -cp ".:lib/*" *.java
java -cp ".:lib/*" WaterDrop
```      

Note: The latest Java version which has sucessfully compiled the code is "1.8.0_181" (JDK 8), JDK 10 is to be tested
