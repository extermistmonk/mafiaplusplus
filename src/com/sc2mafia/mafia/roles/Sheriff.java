package com.sc2mafia.mafia.roles;

import java.util.ArrayList;
import java.util.EventObject;

import com.sc2mafia.mafia.Game;
import com.sc2mafia.mafia.Player;
import com.sc2mafia.mafia.Role;

public class Sheriff extends TownRole {

    int targets = 1;
    ArrayList<SheriffResultListener> resultListeners = new ArrayList<SheriffResultListener>();

    public interface SheriffResultListener {

	public void handleSheriffResultEvent(SheriffResult e);

    }

    public class SheriffResult extends EventObject {

	private static final long serialVersionUID = -8955868621728805482L;

	Player target;
	Player player;
	Role.Alignment result;

	public SheriffResult(Game source, Player player, Player target,
		Role.Alignment result) {
	    super(source);
	    this.player = player;
	    this.target = target;
	    this.result = result;
	}

	public Player getTarget() {
	    return target;
	}

	public Player getPlayer() {
	    return player;
	}

	public Role.Alignment getResult() {
	    return result;
	}

    }

    public void nightEnd(Player player, Game game) {
	SheriffResult event = new SheriffResult(game, player,
		player.getTargets()[0], player.getTargets()[0].getRole()
			.getAlignment());
	for (SheriffResultListener l : resultListeners) {
	    l.handleSheriffResultEvent(event);
	}
    }

    public synchronized void addEventListener(SheriffResultListener listener) {
	resultListeners.add(listener);
    }

    public synchronized void removeEventListener(SheriffResultListener listener) {
	resultListeners.remove(listener);
    }
    
    public int getNumberOfTargets() {
	return 1;
    }

}
