These are my (Haaris') general notes on how libGDX works -- useful for both me and my team to understand how it works and to be able to refer to in the future -> Especially useful as it isn't the usual main() entry.

(This file could be considered a starting point for the documentation itself but for now just helps show my current understanding/ learning of the game loop - can be converted in future)

### LibGDX Execution Process
Running "./gradlew run":
    1 - Builds and compiles the .java code (we only need to worry about the java code in the core directory as thats where our mazeGame is)
    2 - Starts the desktop launcher -> Creates the application window for our java to run in.
    3 - After Window created, Game Loop begins.

### LibGDX Game Loop
LibGDX manages our game using 3 MAIN LIFECYCLE METHODS (there are more but these are the main ones):

    1 - Create()
    2 - Render()
    3 - Dispose()

These are pre-written in the libGDX template (close to empty, allowing us to change and build within them.)

#### How are the 3 Lifecycle Methods run?

1 - Create():
    Called ONCE when the window opens. THIS IS WHERE YOU LOAD RESOURCES.
    Init methods, classes, variables, images, audio, etc. all here to be initialised.


2 - Render()
    This is what renders EACH FRAME (~60fps).
    Clears the screen, handles input (arrow keys etc.), update game logic (collisions, movement, etc.), Draw everything needed.


3 - Dispose()
    Flushes GPU/ sound system memory on exit/ closure of game window.


### FAQ

#### Where can/ should we put our classes?
Make your own .java classes for different parts of the game as needed. This is dependant on how we want to format but modular code is a high priority.

#### The asset wants me to state their name/ link in order to use it, how and where should we do this?
Important we have a collated place to hold this info, use "assets/usedAssetReferences.md."
