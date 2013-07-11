package com.sc2mafia.mafia;

import java.util.ArrayList;

public abstract class Role {

    public enum Alignment {
	TOWN, MAFIA, INDEPENDENT;
    }

    int priority = 0;
    boolean canVote = true;

    public void lynched(Player player, Game game) {
	player.alive = false;
    }

    public void killed(Player player, Player[] killers, Game game) {
	player.alive = false;
	game.playerKilled(player, killers);
    }
    
    public void roleblocked(Player player, Player roleblocker, Game game) {
    }

    public void dayStart(Player player, Game game) {
	if (player.health < 0) {
	    killed(player, player.getKillers(), game);
	}
    }

    public void dayEnd(Player player, Game game) {
    }

    public void nightStart(Player player, Game game) {
    }

    public void nightEnd(Player player, Game game) {
    }

    public Player[] chatTargets(Player player, Game game) {
	switch (getAlignment()) {
	case INDEPENDENT:
	    return new Player[0];
	case MAFIA:
	    ArrayList<Player> mafia = new ArrayList<Player>();
	    for (Player p : game.players) {
		if (p.role.getAlignment() == Alignment.MAFIA) {
		    mafia.add(p);
		}
	    }
	    return mafia.toArray(new Player[mafia.size()]);
	case TOWN:
	    return new Player[0];
	default:
	    return new Player[0];
	}
    }

    public abstract Alignment getAlignment();

    public void attack(Player player, Player attacker, Game game) {
	player.health--;
	if (player.health < 0) {
	    player.addKiller(attacker);
	}
    }

    public void heal(Player player, Player healer, Game game) {
	player.health++;
	if (player.getKillers().length != 0) {
	    player.removeFirstKiller();
	}
    }
    
    public int getNumberOfTargets() {
	return 0;
    }

}
