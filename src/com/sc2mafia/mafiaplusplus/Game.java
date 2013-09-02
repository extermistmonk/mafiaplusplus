package com.sc2mafia.mafiaplusplus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.mozilla.javascript.*;

public class Game {

    private Player[] players;
    private HashMap<Player, Integer> votes = new HashMap<Player, Integer>();
    private boolean day;
    private boolean started = false;
    private int cycles = 0;
    private String globalScript;
    private Context cx;
    private Scriptable globalScope;

    private ArrayList<PlayerLynchedListener> lynchListeners = new ArrayList<PlayerLynchedListener>();
    private ArrayList<PlayerKilledListener> killListeners = new ArrayList<PlayerKilledListener>();
    private ArrayList<GameOverListener> gameOverListeners = new ArrayList<GameOverListener>();
    private ArrayList<SystemMessageListener> systemMessageListeners = new ArrayList<SystemMessageListener>();

    public Game(Player[] players, String globalScript) {
	this.globalScript = globalScript;
	this.players = players.clone();
	for (int i = 0; i < players.length; i++) {
	    votes.put(players[i], 0);
	}
    }
    
    public void startGame() {
	ContextFactory.initGlobal(new SandboxContextFactory());
	cx = Context.enter();
	cx.setClassShutter(new ClassShutter() {
	    public boolean visibleToScripts(String className) {
		if (className.startsWith("com.sc2mafia.mafia.") || className.startsWith("java.lang.")) {
		    return true;
		}
		return true;
	    }
	});
	globalScope = new ImporterTopLevel(cx); 
	this.cx.evaluateString(globalScope, globalScript, "GlobalScript", 1, null);
	for (Player p : players) {
	    p.initRole(cx, globalScope);
	}
	started = true;
	if (getGlobalScriptVar("nightStart") instanceof Boolean && (Boolean)getGlobalScriptVar("nightStart")) {
	    startNight();
	} else {
	    startDay();
	}	
    }

    public void startNight() {
	day = false;
	cycles++;
	sortPlayersByPriority();
	for (Player p : players) {
	    p.nightStart(this);
	}
    }

    public void endNight() {
	day = false;
	sortPlayersByPriority();
	for (Player p : players) {
	    p.nightEnd(this);
	}
    }

    public void startDay() {
	day = true;
	cycles++;
	sortPlayersByPriority();
	for (Player p : players) {
	    votes.put(p, 0);
	    p.dayStart(this);
	}
	checkWins();
    }

    public void endDay() {
	day = true;
	sortPlayersByPriority();
	for (Player p : players) {
	    p.dayEnd(this);
	}
	checkWins();
    }

    public void playerVoted(Player voter, Player vote)
	    throws InvalidVoteException {
	if (vote.isAlive() && voter.isAlive()) {
	    votes.put(voter.getLynchVote() , votes.get(vote) - voter.voteWeight());
	    votes.put(vote, votes.get(vote) + voter.voteWeight());
	    countVotes();
	} else if (vote.isAlive()) {
	    throw new InvalidVoteException("Dead players cannot vote.");
	} else {
	    throw new InvalidVoteException("Dead players cannot be voted for.");
	}
    }

    private void countVotes() {
	int numMajority = countLiving() / 2 + 1;
	for (Player p : votes.keySet()) {
	    if (votes.get(p) >= numMajority) {
		lynch(p);
	    }
	}
    }

    void playerKilled(Player player, Player[] killers) {
	PlayerKilled event = new PlayerKilled(this, player, killers);
	for (PlayerKilledListener l : killListeners) {
	    l.handlePlayerKilledEvent(event);
	}
    }

    private void lynch(Player player) {
	player.lynched(this);
	PlayerLynched event = new PlayerLynched(this, player);
	for (PlayerLynchedListener l : lynchListeners) {
	    l.handlePlayerLynchedEvent(event);
	}
    }
    
    public Player[] getLivingPlayers() {
	ArrayList<Player> living = new ArrayList<Player>();
	for (Player p : players) {
	    if (p.isAlive()) {
		living.add(p);
	    }
	}
	return living.toArray(new Player[living.size()]);
    }

    private int countLiving() {
	return getLivingPlayers().length;
    }

    public Player[] getDeadPlayers() {
	ArrayList<Player> dead = new ArrayList<Player>();
	for (Player p : players) {
	    if (!p.isAlive()) {
		dead.add(p);
	    }
	}
	return dead.toArray(new Player[dead.size()]);
    }

    public Player[] getPlayers() {
	return players;
    }

    private void sortPlayersByPriority() {
	Collections.sort(Arrays.asList(players), new Comparator<Player>() {
	    @Override
	    public int compare(Player p1, Player p2) {
		return p1.getPriority() - p2.getPriority();
	    }
	});
    }

    private void checkWins() {
	ArrayList<Player> winners = new ArrayList<Player>();
	for (Player p : getPlayers()) {
	    if (p.canGameEnd(this) == false) {
		return;
	    } else if (p.isWinner(this)) {
		winners.add(p);
	    }
	}
	GameOver event = new GameOver(this, winners.toArray((new Player[winners.size()])));;
    	for (GameOverListener l : gameOverListeners) {
    	    l.handleGameOverEvent(event);
    	}
    }
    
    //not implemented
    public void sendSystemMessage(String message, Player[] players) {
	System.out.println(message);
    }
    
    public void processMessage(Message message) {
	for (Player p : getPlayers()) {
	    p.handleMessage(message, this);
	}
    }
    
    Object getGlobalScriptVar(String varName) {
	return globalScope.get(varName, globalScope);
    }

    public synchronized void addEventListener(SystemMessageListener listener) {
	systemMessageListeners.add(listener);
    }

    public synchronized void removeEventListener(SystemMessageListener listener) {
	systemMessageListeners.remove(listener);
    }
    public synchronized void addEventListener(GameOverListener listener) {
	gameOverListeners.add(listener);
    }

    public synchronized void removeEventListener(GameOverListener listener) {
	gameOverListeners.remove(listener);
    }

    public synchronized void addEventListener(PlayerLynchedListener listener) {
	lynchListeners.add(listener);
    }

    public synchronized void removeEventListener(PlayerLynchedListener listener) {
	lynchListeners.remove(listener);
    }

    public synchronized void addEventListener(PlayerKilledListener listener) {
	killListeners.add(listener);
    }

    public synchronized void removeEventListener(PlayerKilledListener listener) {
	killListeners.remove(listener);
    }

    public boolean isDay() {
	return day;
    }

    public boolean isStarted() {
	return started;
    }

    public int getCycles() {
	return cycles;
    }

}
