# Team10Maze
Maze game created using Java for ENG1.

### To run:
cd into this directory and type "./gradlew run" -- if on windows: "./gradlew.bat run"

### Relative path for the game.java itself (the game is stored here)
#### core/src/main/java/com/Team10/Team10Maze/mazeGame.java
mazeGame.java is our main entrypoint to game.
Made it modular so can create classes for mainMenu, mazeMap1, etc.

### References and Notes
References to the creators of assets in assets/usedAssetReferences.md

** Overall notes on libGDX and how the game loop works (moreso for devs) is in libGDXNotes.md -- Starting of documentation


### TODO:
These are the current tasks in individual functions/ files we want to tackle next. Can be vague ideas but important to keep up-to-date on what we need to do so no double-coding can be done, wasting time.

- Currently implementing the main menu to be able to go into the game itself.
    - Implemented the background image which sizes correctly - could in future implement with resize() game loop method.
    - halfway done - start button done: Need to add buttons to quit or start game.
    - Have clickable button size resize with window (use getBackgroundSize() in resize())
    - Maze creation -- Kamso creating what maze will look like then whoever is most comfortable converting that to code will do
         - Modularity for maze creation will be important as that will allow for us to just pass through the "maze" in whatever format we need for that map (eg comp sci building, piazza, etc.) then have the background change accordingly.
