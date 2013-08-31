package com.sc2mafia.mafiaplusplus;

import java.util.EventObject;

public class PlayerKilled extends EventObject {

    private static final long serialVersionUID = -7234287211786587126L;
    
    Player player;
    Player[] killers;

    public PlayerKilled(Game source, Player player, Player[] killers) {
	super(source);
	this.player = player;
	this.killers = killers;
    }

    public Player getPlayer() {
	return player;
    }
    
    public Player[] getKillers() {
	return killers;
    }

}
