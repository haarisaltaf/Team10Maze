These are my (Haaris') general notes on how libGDX works -- useful for both me and my team to understand how it works and to be able to refer to in the future -> Especially useful as it isn't the usual main() entry.

(This file could be considered a starting point for the documentation itself but for now just helps show my current understanding/ learning of the game loop - can be converted in future)

### LibGDX Execution Process
Running "./gradlew run":
    1 - Builds and compiles the .java code (we only need to worry about the java code in the core directory as thats where our mazeGame is)
    2 - Starts the desktop launcher -> Creates the application window for our java to run in.
    3 - After Window created, Game Loop begins.

### LibGDX Game Loop
LibGDX manages our game using 3 MAIN LIFECYCLE METHODS (there are more but these are the main ones):

    1 - Create() -- Changes to Show() for implementing Screen
    2 - Render()
    3 - Dispose()

These are pre-written in the libGDX template (close to empty, allowing us to change and build within them.)

#### How are the 3 Lifecycle Methods run?

1 - Create():
    Called ONCE when the window opens. THIS IS WHERE YOU LOAD RESOURCES.
    Init methods, classes, variables, images, audio, etc. all here to be initialised.


2 - Render()
    This is what renders EACH FRAME (~60fps). Called every frame.
    Clears the screen, handles input (arrow keys etc.), update game logic (collisions, movement, etc.), Draw everything needed.


3 - Dispose()
    Called at exit.
    Flushes GPU/ sound system memory on exit/ closure of game window.


##### What are the other Lifecycle Methods?
4 - Resize() - Called whenever screensize changes. (Could be used with getBackgroundSize)

5 - Pause() - Called when app is paused ie switching apps. (Could incorporate a pause menu)

6 - Resume() - Called when focus is back on app. (To go back into game from pausing)


### How to have separate classes for different scenes?
CHANGED TO INHERIT Game in mainMaze.java (main entry point) and IMPLEMENT Screen. This allows for easier switching between scenes/ classes.

### Key Classes we will need
Stage - Holds all UI elements on a screen: handles input and rendering.
Skin - Defines visual style of elements: text, image, font, etc.
Button, TextButton, and ClickListener - Button element, subclass of button that shows text as a button, clickListener to handle click events

#### Flow of Stage, Skin and Button to show how he setup flow would work for ui.
1 - Create a stage and setInputProcessor so stage can recieve clicks.
2 - Load Skin for the button, has prebuilt buttons but can use own assets.
3 - Make the Button, setSize()/ setPosition()
4 - stage.addActor(Button)
5 - addListener() -- To capture signals such as clicking of button etc.

#### How to call the stage?
Stage draws itself automatically, just call in render()


### FAQ

#### Where can/ should we put our classes?
Make your own .java classes for different parts of the game as needed. This is dependant on how we want to format but modular code is a high priority.

#### The asset wants me to state their name/ link in order to use it, how and where should we do this?
Important we have a collated place to hold this info, use "assets/usedAssetReferences.md."


#### Why does Screen.render() need float delta passed-through?
Needs delta to have the time since previous frame --  improves smoothness by using when previous frame was shown
