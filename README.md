# Mafia++
A flexible and lightweight engine for the party game Mafia. It is unique in that it allows player-provided role scripts coded in JavaScript, giving it great extensibility, while having enough security features to protect the server running it.

## Usage
The code is fairly well documented. Create an array of Players with corresponding role scripts, then get a global script and create an instance of Game with it. Read the comments and JavaDoc for more info on how exactly to interface with the class, and the listeners to implement.

## Creating Roles
Creating roles requires a decent knowledge of JavaScript. Roles are written with the Rhino implementation of JavaScript, and have access to java.lang.\* and com.sc2mafia.mafiaplusplus.\*. Roles are executed in the global context, so they can access variables and methods in themselves as well as in the global script, while accessing other roles is done through calling Java methods on Player instances or by calling getJSObject(), which directly accesses the JavaScript object for that Player. Each method in Player directly corresponds to a method in the role script, read the JavaDoc for a list of methods that you should and can implement. For security reasons, all role functions must be completed within **five** seconds. What happens after five seconds is yet to be determined. More documentation will come as the project advances.

## Notes
This project is far from complete. The specifications and methods may change at any time, so early adopters should be prepared to modify their roles accordingly. Consider the project volatile, and don't invest too much of your time in coding for it.

## Todo
- [ ] Add some way of checking that a role script is valid before creating the Player
- [ ] \(Possibly?\) move lynching to the global script, to allow games to have control over that aspect
- [ ] Write an actual server
