package com.sc2mafia.mafiaplusplus.core;

public class InvalidVoteException extends Exception {

    private static final long serialVersionUID = 3745964923257331587L;

    public InvalidVoteException (String message) {
	super(message);
    }
}
