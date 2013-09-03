package com.sc2mafia.mafiaplusplus.event;

import java.util.EventObject;

import com.sc2mafia.mafiaplusplus.Game;
import com.sc2mafia.mafiaplusplus.Player;

public class PlayerLynchedEvent extends EventObject {

    private static final long serialVersionUID = -4434254527074259097L;

    Player player;
    
    public PlayerLynchedEvent(Game source, Player player) {
	super(source);
	this.player = player;
    }

    public Player getPlayer() {
	return player;
    }
    
}
