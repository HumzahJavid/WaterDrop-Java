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
    
