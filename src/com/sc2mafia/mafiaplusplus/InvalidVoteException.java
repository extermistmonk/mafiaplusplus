package com.sc2mafia.mafiaplusplus;

public class InvalidVoteException extends Exception {

    private static final long serialVersionUID = 3745964923257331587L;

    InvalidVoteException (String message) {
	super(message);
    }
}
