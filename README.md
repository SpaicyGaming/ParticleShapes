# ParticleShapes

ParticleShapes is a trivial plugin for Minecraft servers. It allows players to draw triangles made of particles.

Create a vertex whenever a player clicks while holding the gadget item in his main hand. Once three vertices are ‘set’, 
lines are drawn to form a triangle. If enabled, the particle effect fills in the triangle area.

Plugin tested on a PaperSpigot 1.16.3 server.

#### Main Features
* Ability to fill the triangle area with particles
* Multiple worlds support
* Widget item customizable from the configuration file

#### Commands
* _/triangle_ -> Gets the widget item
* _/triangle togglefill_ -> Toggles whether the particle effect fills in the area of triangle when drawn

## Building
To build ParticleShapes, you need JDK 8 or higher and Maven installed on your system. Then, run the following command:
```sh
mvn clean install
```


