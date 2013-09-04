package com.sc2mafia.mafiaplusplus;

import org.mozilla.javascript.*;

/**
 * This class contains the user-loaded JavaScript script that controls the role
 * and player data. It provides a wrapper around said script, exposing the
 * JavaScript functions to the Game class as well as other players. The
 * JavaScript script is responsible for controlling the player functions,
 * including night actions, chat functions, and determining when the player is
 * dead.
 */
public class Player {

    private String script;
    private Context cx;
    private Scriptable scope;
    private String scriptName;

    /**
     * Instantiates a new player. This does not initialise the role, and using
     * any functions will cause issues.
     * 
     * @param script
     *            the JavaScript script that controls the role
     * @param scriptName
     *            the name of the script, used to pinpoint the source of errors
     */
    public Player(String script, String scriptName) {
	this.script = script;
	this.scriptName = scriptName;
    }

    /**
     * Initialises the role. This runs the role script once, calls the
     * init(player) function in the role script, and initialises the JavaScript
     * context so that functions can be called. Roles should record the Player
     * object passed in this function, so that they can use it later.
     * 
     * @param cx
     *            the JavaScript context
     * @param globalScope
     *            the global script scope
     */
    void initRole(Context cx, Scriptable globalScope) {
	this.cx = cx;
	this.scope = cx.newObject(globalScope);
	this.cx.evaluateString(scope, script, scriptName, 1, null);
	Object fObj = scope.get("init", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function) fObj;
	f.call(cx, scope, scope, new Object[] { this });
    }

    /**
     * Gets the role name.
     * 
     * The variable "roleName" <b>MUST</b> be in the role script.
     * 
     * @return the role name
     */
    public String getRoleName() {
	return (String) scope.get("roleName", scope);
    }

    /**
     * Gets the role's alignment.
     * 
     * The variable "alignment" <b>MUST</b> be in the role script.
     * 
     * @return the alignment
     */
    public String getAlignment() {
	return (String) scope.get("alignment", scope);
    }

    /**
     * Checks if the player is alive.
     * 
     * This function <b>MUST</b> be implemented in the role script.
     * 
     * @return true, if the player is alive
     */
    public boolean isAlive() {
	Function f = (Function) scope.get("isAlive", scope);
	return (Boolean) f.call(cx, scope, scope, new Object[] {});
    }

    /**
     * Gets the priority of the player. The priority is the order in which the
     * phase change functions are called, from low to high.
     * 
     * This function <b>MUST</b> be implemented in the role script.
     * 
     * @return the priority
     */
    public int getPriority() {
	Function f = (Function) scope.get("getPriority", scope);
	return ((Double) f.call(cx, scope, scope, new Object[] {})).intValue();
    }

    /**
     * This function is called upon day start. This is where roles should
     * perform any actions that occur after the night actions have been carried
     * out, such as checking if they are still alive and responding accordingly.
     * 
     * This function may be implemented in the role script.
     * 
     * @param game
     *            the game instance the player belongs to
     */
    void dayStart(Game game) {
	Object fObj = scope.get("dayStart", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function) fObj;
	f.call(cx, scope, scope, new Object[] { game });
    }

    /**
     * This function is called upon day end. This is where roles should perform
     * any actions that occur after the lynching has occurred.
     * 
     * This function may be implemented in the role script.
     * 
     * @param game
     *            the game instance the player belongs to
     */
    void dayEnd(Game game) {
	Object fObj = scope.get("dayEnd", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function) fObj;
	f.call(cx, scope, scope, new Object[] { game });
    }

    /**
     * This function is called upon night start. This is where roles should
     * perform any actions that the role needs to do to prepare themselves for
     * the night phase.
     * 
     * This function may be implemented in the role script.
     * 
     * @param game
     *            the game instance the player belongs to
     */
    void nightStart(Game game) {
	Object fObj = scope.get("nightStart", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function) fObj;
	f.call(cx, scope, scope, new Object[] { game });
    }

    /**
     * This function is called upon night end. This is where roles should
     * perform any night actions.
     * 
     * This function may be implemented in the role script.
     * 
     * @param game
     *            the game instance the player belongs to
     */
    void nightEnd(Game game) {
	Object fObj = scope.get("nightEnd", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function) fObj;
	f.call(cx, scope, scope, new Object[] { game });
    }

    /**
     * This function is called when another player attacks this one. This is
     * where players handle the attack and respond to it.
     * 
     * This function may be implemented in the role script.
     * 
     * @param attacker
     *            the attacker
     * @param game
     *            the game instance the player belongs to
     */
    public void attack(Player attacker, Game game) {
	Object fObj = scope.get("attack", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function) fObj;
	f.call(cx, scope, scope, new Object[] { attacker, game });
    }

    /**
     * This function is called when another player heals this one. This is where
     * players handle the healing and respond to it.
     * 
     * This function may be implemented in the role script.
     * 
     * @param healer
     *            the healer
     * @param game
     *            the game instance the player belongs to
     */
    public void heal(Player healer, Game game) {
	Object fObj = scope.get("heal", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function) fObj;
	f.call(cx, scope, scope, new Object[] { healer, game });
    }

    /**
     * This function is called when the player is lynched.
     * 
     * This function may be implemented in the role script.
     * 
     * @param game
     *            the game
     */
    void lynched(Game game) {
	Object fObj = scope.get("lynched", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function) fObj;
	f.call(cx, scope, scope, new Object[] { game });
    }

    /**
     * This function is called when the player is roleblocked.
     * 
     * This function may be implemented in the role script.
     * 
     * @param roleblocker
     *            the roleblocker
     * @param game
     *            the game instance the player belongs to
     */
    void roleblocked(Player roleblocker, Game game) {
	Object fObj = scope.get("roleblocked", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function) fObj;
	f.call(cx, scope, scope, new Object[] { roleblocker, game });
    }

    /**
     * Gets the targets of this player. The context of this is up to the role to
     * decide, but in general it refers to the role's night targets.
     * 
     * This function may be implemented in the role script.
     * 
     * @return the targets, or an empty array if the function is not implemented
     *         or no targets were selected
     */
    public Player[] getTargets() {
	Object fObj = scope.get("getTargets", scope);
	if (!(fObj instanceof Function)) {
	    return new Player[] {};
	}
	Function f = (Function) fObj;
	Object result = f.call(cx, scope, scope, new Object[] {});
	if (result instanceof Player[]) {
	    return (Player[]) result;
	}
	return new Player[] {};
    }

    /**
     * Sets the targets of the player.
     * 
     * This function may be implemented in the role script.
     * 
     * @param targets
     *            the targets
     * @param game
     *            the game instance the player belongs to
     */
    public void setTargets(Player[] targets, Game game) {
	Object fObj = scope.get("setTargets", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function) fObj;
	f.call(cx, scope, scope, new Object[] { targets, game });
    }

    /**
     * Handles a message. This method is called by the game whenever any player
     * sends a message. The role is then free to manipulate the message in
     * various ways, or perform actions based on the message contents.
     * 
     * This function may be implemented in the role script.
     * 
     * @param message
     *            the message
     * @param game
     *            the game instance the player belongs to
     */
    public void handleMessage(Message message, Game game) {
	Object fObj = scope.get("handleMessage", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function) fObj;
	f.call(cx, scope, scope, new Object[] { message, game });
    }

    /**
     * Gets the role's killers. This method should return the list of players
     * who killed the player, or an empty array. It is up to the role to keep
     * track of the killers.
     * 
     * This function <b>MUST</b> be implemented in the role script.
     * 
     * @return the killers
     */
    public Player[] getKillers() {
	Function f = (Function) scope.get("getKillers", scope);
	NativeArray result = (NativeArray) f.call(cx, scope, scope,
		new Object[] {});
	if (result.size() == 0) {
	    return new Player[] {};
	}
	Player[] returnArray = new Player[(int) result.getLength()];
	for (Object o : result.getIds()) {
	    int index = (Integer) o;
	    returnArray[index] = (Player) result.get(index);
	}
	return returnArray;
    }

    /**
     * This method is called when the player votes to lynch someone else. The
     * role script should store this for later use.
     * 
     * This function may be implemented in the role script.
     * 
     * @param vote
     *            the player who this player voted for
     */
    public void setLynchVote(Player vote) {
	Object fObj = scope.get("setLynchVote", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function) fObj;
	f.call(cx, scope, scope, new Object[] { vote });
    }

    /**
     * Gets the lynch vote. This should return the player that this player is
     * currently voting to be lynched.
     * 
     * This function <b>MUST</b> be implemented in the role script.
     * 
     * @return the player that this player has voted for
     */
    public Player getLynchVote() {
	Function f = (Function) scope.get("getLynchVote", scope);
	Object result = f.call(cx, scope, scope, new Object[] {});
	if (result instanceof Undefined) {
	    return null;
	}
	return (Player) result;
    }

    /**
     * Gets the vote weight. This is the number of votes that this player's vote
     * counts for in a lynch.
     * 
     * This function <b>MUST</b> be implemented in the role script.
     * 
     * @return the vote weight
     */
    public int voteWeight() {
	Function f = (Function) scope.get("voteWeight", scope);
	return ((Double) f.call(cx, scope, scope, new Object[] {})).intValue();
    }

    /**
     * This method is called at every cycle change. It should return true if the
     * role is "okay" with the game ending at that point (i.e. if the role can
     * determine whether or not it has won or lost), and false if it isn't. The
     * game will run until every single player returns true for this method.
     * 
     * This function <b>MUST</b> be implemented in the role script.
     * 
     * @param game
     *            the game instance the player belongs to
     * @return true, if the role can decide if it wins or loses
     */
    public boolean canGameEnd(Game game) {
	Function f = (Function) scope.get("canGameEnd", scope);
	return (boolean) f.call(cx, scope, scope, new Object[] { game });
    }

    /**
     * Checks if the player has won. This method is only called after all
     * players have returned true for canGameEnd.
     * 
     * This function <b>MUST</b> be implemented in the role script.
     * 
     * @param game
     *            the game instance the player belongs to
     * @return true, if the player has won
     */
    public boolean isWinner(Game game) {
	Function f = (Function) scope.get("isWinner", scope);
	return (boolean) f.call(cx, scope, scope, new Object[] { game });
    }

    /**
     * Gets the JavaScript object for this role. This can be used directly in a
     * script with no further parsing.
     * 
     * @return the JavaScript object
     */
    public Scriptable getJSObject() {
	return scope;
    }

}
