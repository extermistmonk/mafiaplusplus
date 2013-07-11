package com.sc2mafia.mafia;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import com.sc2mafia.mafia.roles.MafiaRole;

public class Game {

    Player[] players;
    HashMap<Player, Integer> votes = new HashMap<Player, Integer>();
    HashMap<Player, Integer> mafiaTarget = new HashMap<Player, Integer>();
    HashMap<Player, Integer> mafiaKiller = new HashMap<Player, Integer>();
    HashMap<Player, Player> killerVotes = new HashMap<Player, Player>();
    boolean day;
    boolean started = false;
    int cycles = 0;
    Setup setup;

    private ArrayList<PlayerLynchedListener> lynchListeners = new ArrayList<PlayerLynchedListener>();
    private ArrayList<PlayerKilledListener> killListeners = new ArrayList<PlayerKilledListener>();
    private ArrayList<GameOverListener> gameOverListeners = new ArrayList<GameOverListener>();

    public Game(Setup setup) {
	this.setup = setup;
    }

    public void startGame() {
	Role[] roles = setup.roles.clone();
	Collections.shuffle(Arrays.asList(roles));
	players = new Player[roles.length];
	for (int i = 0; i < roles.length; i++) {
	    players[i] = new Player(roles[i]);
	    mafiaTarget.put(players[i], 0);
	    votes.put(players[i], 0);
	    if (players[i].role instanceof MafiaRole) {
		mafiaKiller.put(players[i], 0);
	    }
	}
	if (setup.dayStart) {
	    startDay();
	} else {
	    startNight();
	}
    }

    public void startNight() {
	day = false;
	cycles++;
	sortPlayersByPriority();
	for (Player p : players) {
	    p.nightStart(this);
	}
    }

    public void endNight() {
	day = false;
	sortPlayersByPriority();
	((MafiaRole) getMafiaKiller().role).setMafiaKiller(getMafiaTarget());
	for (Player p : players) {
	    p.nightEnd(this);
	}
    }

    public void startDay() {
	day = true;
	cycles++;
	sortPlayersByPriority();
	for (Player p : players) {
	    votes.put(p, 0);
	    p.dayStart(this);
	}
	checkWins();
    }

    public void endDay() {
	sortPlayersByPriority();
	for (Player p : players) {
	    p.dayEnd(this);
	}
	checkWins();
    }

    public void mafiaVotedTarget(Player voter, Player target)
	    throws InvalidVoteException {
	if (target.alive && voter.alive) {
	    mafiaTarget.put(voter.vote, mafiaTarget.get(target)
		    - voter.voteWeight);
	    mafiaTarget.put(target, mafiaTarget.get(target) + voter.voteWeight);
	    voter.vote = target;
	} else if (target.alive) {
	    throw new InvalidVoteException("Dead players cannot vote.");
	} else {
	    throw new InvalidVoteException("Dead players cannot be voted for.");
	}
    }

    public void mafiaVotedKiller(Player voter, Player killer)
	    throws InvalidVoteException {
	if (killer.role.getAlignment() != Role.Alignment.MAFIA) {
	    throw new InvalidVoteException("Killer must be mafia");
	} else if (killer.alive && voter.alive) {
	    mafiaKiller.put(killerVotes.get(voter), mafiaKiller.get(killer)
		    - voter.voteWeight);
	    mafiaKiller.put(killer, mafiaKiller.get(killer) + voter.voteWeight);
	    killerVotes.put(voter, killer);
	} else if (killer.alive) {
	    throw new InvalidVoteException("Dead players cannot vote.");
	} else {
	    throw new InvalidVoteException("Dead players cannot be voted for.");
	}
    }

    public void playerVoted(Player voter, Player vote)
	    throws InvalidVoteException {
	if (vote.alive && voter.alive) {
	    votes.put(voter.vote, votes.get(vote) - voter.voteWeight);
	    votes.put(vote, votes.get(vote) + voter.voteWeight);
	    voter.vote = vote;
	    countVotes();
	} else if (vote.alive) {
	    throw new InvalidVoteException("Dead players cannot vote.");
	} else {
	    throw new InvalidVoteException("Dead players cannot be voted for.");
	}
    }

    private void countVotes() {
	int numMajority = countLiving() / 2 + 1;
	for (Player p : votes.keySet()) {
	    if (votes.get(p) >= numMajority) {
		lynch(p);
	    }
	}
    }

    private Player getMafiaTarget() {
	List<Player> targetList = new ArrayList<Player>();
	int largestvote = 0;
	for (Entry<Player, Integer> i : mafiaTarget.entrySet()) {
	    if (largestvote < i.getValue()) {
		largestvote = i.getValue();
		targetList.clear();
		targetList.add(i.getKey());
	    } else if (largestvote == i.getValue()) {
		targetList.add(i.getKey());
	    }
	}
	Collections.shuffle(targetList);
	return targetList.get(0);
    }

    private Player getMafiaKiller() {
	List<Player> targetList = new ArrayList<Player>();
	int largestvote = 0;
	for (Entry<Player, Integer> i : mafiaKiller.entrySet()) {
	    if (largestvote < i.getValue()) {
		largestvote = i.getValue();
		targetList.clear();
		targetList.add(i.getKey());
	    } else if (largestvote == i.getValue()) {
		targetList.add(i.getKey());
	    }
	}
	Collections.shuffle(targetList);
	return targetList.get(0);
    }

    void playerKilled(Player player, Player[] killers) {
	PlayerKilled event = new PlayerKilled(this, player, killers);
	for (PlayerKilledListener l : killListeners) {
	    l.handlePlayerKilledEvent(event);
	}
    }

    private void lynch(Player player) {
	player.lynched(this);
	PlayerLynched event = new PlayerLynched(this, player);
	for (PlayerLynchedListener l : lynchListeners) {
	    l.handlePlayerLynchedEvent(event);
	}
    }

    public Player[] livingPlayers() {
	ArrayList<Player> living = new ArrayList<Player>();
	for (Player p : players) {
	    if (p.alive) {
		living.add(p);
	    }
	}
	return living.toArray(new Player[living.size()]);
    }

    private int countLiving() {
	return livingPlayers().length;
    }

    public Player[] getDeadPlayers() {
	ArrayList<Player> dead = new ArrayList<Player>();
	for (Player p : players) {
	    if (!p.alive) {
		dead.add(p);
	    }
	}
	return dead.toArray(new Player[dead.size()]);
    }

    public Player[] getMafiaPlayers() {
	ArrayList<Player> mafia = new ArrayList<Player>();
	for (Player p : players) {
	    if (p.role.getAlignment() == Role.Alignment.MAFIA) {
		mafia.add(p);
	    }
	}
	return mafia.toArray(new Player[mafia.size()]);
    }

    public Player[] getTownPlayers() {
	ArrayList<Player> town = new ArrayList<Player>();
	for (Player p : players) {
	    if (p.role.getAlignment() == Role.Alignment.TOWN) {
		town.add(p);
	    }
	}
	return town.toArray(new Player[town.size()]);
    }

    private void sortPlayersByPriority() {
	Collections.sort(Arrays.asList(players), new Comparator<Player>() {
	    @Override
	    public int compare(Player p1, Player p2) {
		return p1.role.priority - p2.role.priority;
	    }
	});
    }

    private void checkWins() {
	ArrayList<Player> livingMafia = new ArrayList<Player>();
	for (Player p : getMafiaPlayers()) {
	    if (p.alive) {
		livingMafia.add(p);
	    }
	}
	ArrayList<Player> livingTown = new ArrayList<Player>();
	for (Player p : getTownPlayers()) {
	    if (p.alive) {
		livingTown.add(p);
	    }
	}
	if (livingMafia.size() == 0) {
	    GameOver event = new GameOver(this, getTownPlayers());
	    for (GameOverListener l : gameOverListeners) {
		l.handleGameOverEvent(event);
	    }
	} else if (livingMafia.size() >= livingTown.size()) {
	    GameOver event = new GameOver(this, getMafiaPlayers());
	    for (GameOverListener l : gameOverListeners) {
		l.handleGameOverEvent(event);
	    }
	} 
    }

    public synchronized void addEventListener(GameOverListener listener) {
	gameOverListeners.add(listener);
    }

    public synchronized void removeEventListener(GameOverListener listener) {
	gameOverListeners.remove(listener);
    }

    public synchronized void addEventListener(PlayerLynchedListener listener) {
	lynchListeners.add(listener);
    }

    public synchronized void removeEventListener(PlayerLynchedListener listener) {
	lynchListeners.remove(listener);
    }

    public synchronized void addEventListener(PlayerKilledListener listener) {
	killListeners.add(listener);
    }

    public synchronized void removeEventListener(PlayerKilledListener listener) {
	killListeners.remove(listener);
    }

    public Player[] getPlayers() {
	return players;
    }

    public boolean isDay() {
	return day;
    }

    public boolean isStarted() {
	return started;
    }

    public int getCycles() {
	return cycles;
    }

}
