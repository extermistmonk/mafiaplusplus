package com.sc2mafia.mafiaplusplus.event;

import java.util.EventObject;

import com.sc2mafia.mafiaplusplus.Game;
import com.sc2mafia.mafiaplusplus.Player;

public class GameOverEvent extends EventObject {

    private static final long serialVersionUID = -3828864525074040651L;
    
    Player[] winners;

    public GameOverEvent(Game source, Player[] winners) {
	super(source);
	this.winners = winners;
    }

    public Player[] getWinners() {
	return winners;
    }

}
