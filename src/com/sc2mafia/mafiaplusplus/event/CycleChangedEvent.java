package com.sc2mafia.mafiaplusplus.event;

import java.util.EventObject;

import com.sc2mafia.mafiaplusplus.Game;

public class CycleChangedEvent extends EventObject {

    public enum Cycle {
	DAYSTART, DAYEND, NIGHTSTART, NIGHTEND;
    }

    private static final long serialVersionUID = -840048517793265536L;

    Cycle cycle;

    public CycleChangedEvent(Game source, Cycle cycle) {
	super(source);
	this.cycle = cycle;
    }

    public Cycle getCycle() {
	return cycle;
    }

}