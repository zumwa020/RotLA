# RotLA
Raiders of the Lost Arctangent: a game project based on Temple of the Beastmen

***********
NAMES
***********
Lukas Zumwalt
Marie Hargan

***********
JAVA VERSION
***********
IntelliJ IDEA 2022.2.1 (Community Edition)<br>
Build #IC-222.3739.54, build on August 15,2022<br>
Runtime version: 17.0.3+7-b469.37 amd64<br>
VM: OpenJDK 64-bit Server VVm by JetBrains s.r.o.<br>
Windows 10 10.0<br>
GC: G1 Young Generation, G1 Old Generation<br>
Memory: 996M<br>
Cores: 4<br>

Kotlin: 222-1.7.10-release-334-IJ3739.54<br>

openjdk 17.0.4.1 2022-08-12 <br>
OpenJDK Runtime Environment Temurin-17.0.4.1+1 (build 17.0.4.1+1) <br>
OpenJDK 64-Bit Server VM Temurin-17.0.4.1+1 (build 17.0.4.1+1, mixed mode, sharing) <br>

***********
COMMENTS
***********
The Raiders of Lost Arctangent program is a fully simulated game adaptation of Temple of the Beastmen, made by Lester Smith of Game Designer’s Workshop. By running this program, the game will autonomously run until the game is won, either by all treasure being found, all adventurers being killed, or all creatures being killed. In this repository, you will find the code to run the game autonomously, as well as, the terminal output of one completed game and another output of 30 completed games within corresponding text files.<br>
An example of inheritance can be found in the Brawler.java file at line 14. All of the Adventurer subclasses inherit from the Adventurer superclass.<br>
An example of encapsulation can be found in the Adventurer.java file at line 10. The example of the get function for the encapsulation can be found [insert line]. This shows encapsulation by encapsulating the member data, which is private, and making it accessible via a getter method.<br>
An example of abstraction can be found in the Orbiter.java at line 7. All of the entity classes, creatures and adventurers extend their corresponding superclasses. For example, Orbiter extends Creature that implements Entity.<br>
An example of polymorphism can be found in Orbiter.java, Blinker.java, and Seeker.java as they all are extensions of the Creature superclass. The associated code lines are Orbiter.java at line 7, Blinker.java at line 7, and Seeker.java at line 7.<br>
An example of cohesion can be found in Render.java. This is a good example because Render.java holds all the print statements and methods for the game simulation. This is considered high cohesion since they are highly related to the class they are within.<br>
An example of identity can be found in Render.java in line 81. We can see how the Adventurer a is set equal to Adventurer a0. <br>
