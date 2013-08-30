package com.sc2mafia.mafia;

import org.mozilla.javascript.*;

public class Player {

    String script;
    Context cx;
    Scriptable scope;

    public Player(String script) {
	this.script = script;
    }

    void initRole(Context cx) {
	this.cx = cx;
	this.scope = cx.initStandardObjects();
	this.cx.evaluateString(scope, script, "RoleScript", 1, null);
	Object fObj = scope.get("init", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function)fObj;
	f.call(cx, scope, scope, new Object[]{this});
    }
    
    public String getRoleName() {
	return (String) scope.get("roleName", scope);
    }

    public String getAlignment() {
	return (String) scope.get("alignment", scope);
    }

    public boolean isAlive() {
	Function f = (Function) scope.get("isAlive", scope);
	return (Boolean)f.call(cx, scope, scope, new Object[]{});
    }

    public int getPriority() {
	Function f = (Function) scope.get("getPriority", scope);
	return ((Double)f.call(cx, scope, scope, new Object[]{})).intValue();
    }

    void dayStart(Game game) {
	Object fObj = scope.get("dayStart", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function)fObj;
	f.call(cx, scope, scope, new Object[]{game});
    }

    void dayEnd(Game game) {
	Object fObj = scope.get("dayEnd", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function)fObj;
	f.call(cx, scope, scope, new Object[]{game});
    }

    void nightStart(Game game) {
	Object fObj = scope.get("nightStart", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function)fObj;
	f.call(cx, scope, scope, new Object[]{game});
    }

    void nightEnd(Game game) {
	Object fObj = scope.get("nightEnd", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function)fObj;
	f.call(cx, scope, scope, new Object[]{game});
    }

    public void attack(Player attacker, Game game) {
	Object fObj = scope.get("attack", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function)fObj;
	f.call(cx, scope, scope, new Object[]{attacker, game});
    }

    public void heal(Player healer, Game game) {
	Object fObj = scope.get("heal", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function)fObj;
	f.call(cx, scope, scope, new Object[]{healer, game});
    }

    void lynched(Game game) {
	Object fObj = scope.get("lynched", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function)fObj;
	f.call(cx, scope, scope, new Object[]{game});
    }

    void roleblocked(Player roleblocker, Game game) {
	Object fObj = scope.get("roleblocked", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function)fObj;
	f.call(cx, scope, scope, new Object[]{roleblocker, game});
    }

    public Player[] getTargets() {
	Function f = (Function) scope.get("getTargets", scope);
	NativeArray result = (NativeArray)f.call(cx, scope, scope, new Object[]{});
	Player[] returnArray = new Player[(int) result.getLength()];
	for (Object o : result.getIds()) {
	    int index = (Integer) o;
	    returnArray[index] = (Player) result.get(index);
	}
	return returnArray;
    }
    
    public void setTargets(Player[] targets) {
	Object fObj = scope.get("setTargets", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function)fObj;
	f.call(cx, scope, scope, new Object[]{});
    }

    public void handleMessage(Message message, Game game) {
	Object fObj = scope.get("handleMessage", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function)fObj;
	f.call(cx, scope, scope, new Object[]{message, game});
    }
    
    public Player[] getKillers() {
	Function f = (Function) scope.get("getKillers", scope);
	NativeArray result = (NativeArray)f.call(cx, scope, scope, new Object[]{});
	Player[] returnArray = new Player[(int) result.getLength()];
	for (Object o : result.getIds()) {
	    int index = (Integer) o;
	    returnArray[index] = (Player) result.get(index);
	}
	return returnArray;
    }

    public void setLynchVote(Player vote) {
	Object fObj = scope.get("setLynchVote", scope);
	if (!(fObj instanceof Function)) {
	    return;
	}
	Function f = (Function)fObj;
	f.call(cx, scope, scope, new Object[]{vote});
    }

    public Player getLynchVote() {
	Function f = (Function) scope.get("getLynchVote", scope);
	return (Player)f.call(cx, scope, scope, new Object[]{});
    }

    public int voteWeight() {
	Function f = (Function) scope.get("voteWeight", scope);
	return ((Double)f.call(cx, scope, scope, new Object[]{})).intValue();
    }

    public boolean canGameEnd(Game game) {
	Function f = (Function) scope.get("canGameEnd", scope);
	return (boolean)f.call(cx, scope, scope, new Object[]{});
    }

    public boolean isWinner(Game game) {
	Function f = (Function) scope.get("isWinner", scope);
	return (boolean)f.call(cx, scope, scope, new Object[]{});
    }

}
