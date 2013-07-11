package com.sc2mafia.mafia;

import java.util.ArrayList;

public class Player {

    Role role;
    ArrayList<Player> killers = new ArrayList<Player>();
    ArrayList<Player> visitors = new ArrayList<Player>();
    Player[] targets;
    Player vote;
    int health = 0;
    int voteWeight = 1;
    boolean alive = true;
    boolean roleblocked = false;

    public Player(Role role) {
	this.role = role;
	targets = new Player[role.getNumberOfTargets()];
    }

    public void attack(Player attacker, Game game) {
	role.attack(this, attacker, game);
    }
    
    public void heal(Player healer, Game game) {
	role.heal(this, healer, game);
    }
    
    void lynched(Game game) {
	role.lynched(this, game);
    }

    void dayStart(Game game) {
	this.vote = null;
	role.dayStart(this, game);
    }

    void dayEnd(Game game) {
	role.dayEnd(this, game);
    }

    void nightStart(Game game) {
	this.vote = null;
	this.targets = new Player[role.getNumberOfTargets()];
	role.nightStart(this, game);
    }

    void nightEnd(Game game) {
	role.nightEnd(this, game);
    }

    void roleblocked(Player roleblocker, Game game) {
    }
    
    public void setTargets(Player[] targets) {
	if (targets.length > role.getNumberOfTargets()) {
	    throw new IndexOutOfBoundsException();
	}
	this.targets = targets;
    }
    
    public Player[] getTargets() {
	return targets;
    }

    public Player[] chatTargets(Game game) {
	if (alive) {
	    if (game.day) {
		return game.players;
	    }
	    return role.chatTargets(this, game);
	}
	return game.getDeadPlayers();
    }

    public boolean isAlive() {
	return alive;
    }

    public Player[] getKillers() {
	return killers.toArray(new Player[killers.size()]);
    }
    
    public void addKiller(Player killer) {
	killers.add(killer);
    }
    
    public void removeKiller(Player killer) {
	killers.remove(killer);
    }
    
    public void removeFirstKiller() {
	killers.remove(0);
    }

    public Player[] getVisitors() {
	return visitors.toArray(new Player[visitors.size()]);
    }
    
    public Role getRole() {
	return role;
    }

}
