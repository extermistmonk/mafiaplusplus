package com.sc2mafia.mafiaplusplus.extra;

import java.util.Timer;
import java.util.TimerTask;

import org.mozilla.javascript.UniqueTag;

import com.sc2mafia.mafiaplusplus.Game;

/**
 * This class provides a lightweight wrapper around the Game class that
 * automatically changes the game cycles at specific time intervals. It uses the
 * dayLength and nightLength variables in the global script to do this, where
 * the values are length in seconds. If dayLength and nightLength are not set,
 * the script defaults to 90 and 30 seconds respectively.
 */
public class TimedGame extends Game {

    /**
     * Instantiates a new timed game. This function is the same as the
     * constructor for a regular Game object.
     * 
     * @param players
     *            the players to initiate the game with
     * @param globalScript
     *            the global JavaScript file, containing variables and methods
     *            shared with all players, as well as game settings
     */
    public TimedGame(String[] roleScripts, String[] roleNames, String globalScript) {
	super(roleScripts, roleNames, globalScript);
    }

    /**
     * Starts the game and the timer.
     */
    public void startGame() {
	setTimer();
	super.startGame();
    }

    private void setTimer() {
	if (isDay()) {
	    long dayLength = 90;
	    Object dayLengthVar = getGlobalScriptVar("dayLength");
	    if (!dayLengthVar.equals(UniqueTag.NOT_FOUND)) {
		dayLength = ((Double) dayLengthVar).longValue();
	    }
	    Timer day = new Timer();
	    day.schedule(new TimerTask() {
		public void run() {
		    createContext();
		    changeCycles();
		}
	    }, dayLength * 1000);
	} else {
	    long nightLength = 30;
	    Object nightLengthVar = getGlobalScriptVar("nightLength");
	    if (!nightLengthVar.equals(UniqueTag.NOT_FOUND)) {
		nightLength = ((Double) nightLengthVar).longValue();
	    }
	    Timer day = new Timer();
	    day.schedule(new TimerTask() {
		public void run() {
		    createContext();
		    changeCycles();
		}
	    }, nightLength * 1000);
	}
    }

    private void changeCycles() {
	if (isDay()) {
	    endDay();
	    startNight();
	} else {
	    endNight();
	    startDay();
	}
	setTimer();
    }

}
