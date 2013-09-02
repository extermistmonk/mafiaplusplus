package com.sc2mafia.mafiaplusplus;

import java.util.EventObject;

public class GameOver extends EventObject {

    private static final long serialVersionUID = -3828864525074040651L;
    
    Player[] winners;

    public GameOver(Game source, Player[] winners) {
	super(source);
	this.winners = winners;
    }

    public Player[] getWinners() {
	return winners;
    }

}
