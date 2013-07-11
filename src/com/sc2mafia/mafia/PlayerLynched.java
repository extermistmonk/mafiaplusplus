package com.sc2mafia.mafia;

import java.util.EventObject;

public class PlayerLynched extends EventObject {

    private static final long serialVersionUID = -4434254527074259097L;

    Player player;
    
    public PlayerLynched(Game source, Player player) {
	super(source);
	this.player = player;
    }

    public Player getPlayer() {
	return player;
    }
    
}
