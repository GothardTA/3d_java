# Java 3D Engine (if you can call it that)
This is a "3d" engine written entirely in java using swing (ew). The engine supports opening any obj files and displaying all the triangles that make it up. You can move around using WASD and use the mouse to look.
## To run
You will need java to run this (obviously). I use java 17 but it should work with older or newer versions.<br>
All libraries used come with java by default. If you don't have these for some reason, they are listed below:<br>
<ul>
<li>Java awt graphics</li>
<li>Javax swing</li>
<li>Standard Java utils</li>
<li>Java io</li>
</ul>

### Linux
Run `./run.sh`, it should automatically build and run the code.<br>
If you get a build issue, it is most likely because java-devel is not installed.

### Windows
Run `.\run.bat`, it should automatically build and run the code.<br>
If you get a build issue, java might not be configured/installed correctly.