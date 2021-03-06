package com.sc2mafia.mafiaplusplus.event;

import java.util.EventObject;

import com.sc2mafia.mafiaplusplus.Game;
import com.sc2mafia.mafiaplusplus.Player;

public class PlayerKilledEvent extends EventObject {

    private static final long serialVersionUID = -7234287211786587126L;
    
    Player player;
    Player[] killers;

    public PlayerKilledEvent(Game source, Player player, Player[] killers) {
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
