# NeoTextureEdit

This is Peter LaValle's (latest) fork of Holger Dammertz's NeoTextureEdit.

... or should be ...

I've converted the software to build and run from a [Gradle](http://gradle.org/) [Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html)
I want to extract the graph API for use in other pursuits.
As I got it - there's a bug; saving and loading a TGR file with everything doesn't work - something is trying to convert a boolean to a float

# Components

The texture editor has six parts;

* Menus
* Project Area ; ... which NTE uses for some predefined ones
	* I'd probably use it for a file-tree or something
* Workbench ; The big view thing of the graph and nodes
* Inspector ; Shows details of the currently selected node
* 2D Preview (technically part of the inspector)
* 3D Preview ; shows a 3D object with the textures applied


