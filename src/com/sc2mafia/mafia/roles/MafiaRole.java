package com.sc2mafia.mafia.roles;

import com.sc2mafia.mafia.Game;
import com.sc2mafia.mafia.Player;
import com.sc2mafia.mafia.Role;

public class MafiaRole extends Role {
    
    Boolean mafiaKiller = false;
    Player mafiaKillTarget = null;
    
    @Override
    public Alignment getAlignment() {
	return Role.Alignment.MAFIA;
    }
    
    public void setMafiaKiller(Player target) {
	this.mafiaKiller = true;
	this.mafiaKillTarget = target;
    }
    
    @Override
    public void nightEnd(Player player, Game game) {
	if (mafiaKiller) {
	    mafiaKillTarget.attack(player, game);
	}
    }

    @Override
    public void dayStart(Player player, Game game) {
	
    }
    
}
