# Introduction #

The How, What, Why and Where.


# Details #

PacMan Multiplayer (also known as PacMan Must Die) is a multiplayer PacMan game. The game's purpose to the general public is to provide a new entertaining way of playing the game, this solving the problem of perpetual boredom most Facebook users suffer from when browsing the website. For the first time in the history of the game the question: "How long can I last against SMART opponents?" can be answered. For the people writing the project the purpose was to acquire experience in working with a working piece of software written by someone else, to add functionality to that software that was not intended for by the original author and to integrate it into a framework.

The application was mainly tested by the developers. Due to the fact that it is a game, and an applet program at that (that means it can only run in the browser), our ability to run automated tests was rather limited. So for most part to test the functionality the we as the developers were forced to play the game to see if it functioned properly. Due to the visual nature of the game, and the fact that you see everything that the game is doing at any given point in time, errors are very easy to spot because they appear as unexpected and unintended game behaviour.

The architectural design of the project can be described as a blend of two patterns object-oriented and event-driven. The individual program itself utilizes object-oriented design. Each of the objects in the game (such as: ghosts, fruit, player, data packets, etc) are separate objects that are defined in separate files and are created when they are needed. However when the program is running everything that happens has to have an event precede it. A new game only starts after the user has pressed the new game button; pacman's character will only change direction after the player presses a key, etc. As well on a larger scale when the game is multiplayer the overall system is also event-driven. There is a single controller that generates game state packets and sends them to the other machines which then use those to adjust their own game state.

The deployment of the game was relatively simple. Since it is written in java and is not compiled as an executive file the game can't really be distributed on movable media, also due to the constraints of the project we really had no other option but to make it a Facebook app.

The work was divided nearly evenly, however the work was distributed such that each team member worked on what he was best at. Aleks and Dror worked on the java coding inplementing local multiplayer, network multiplayer and other features, while KJ worked on Facebook integration and scoring system. Alfred did all the testing and test cases.

If more information is required please visit us on facebook at http://apps.facebook.com/pacmanmd/ and e-mail the creator.