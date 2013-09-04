package com.sc2mafia.mafiaplusplus;

import java.util.ArrayList;

/**
 * This class contains a player-sent message as well as the authorised
 * recipients. Each message is passed through the handleMessage method in each
 * role, which then decides how to deal with the message. Once this is done, the
 * wrapper handles sending the message to the proper recipients.
 */
public class Message {

    private String message;
    private Player sender;
    private ArrayList<Player> receiverList;
    private ArrayList<Player> anonymousReceiverList;
    private ArrayList<Player> overrideList;
    private boolean anonymous = false;
    private boolean killed = false;

    /**
     * Creates a new message. This should be done right when the wrapper
     * receives a message from a player, then immediately passed to the
     * handleMessage method in the relevant Game instance.
     * 
     * @param message
     *            the message that was sent
     * @param sender
     *            the player sending the message
     * @param anonymous
     *            if the message should be anonymous or not (i.e. if the player
     *            who sent the message should be broadcast as well
     */
    public Message(String message, Player sender, boolean anonymous) {
	this.message = message;
	this.sender = sender;
	this.anonymous = anonymous;
	receiverList = new ArrayList<Player>();
	anonymousReceiverList = new ArrayList<Player>();
	overrideList = new ArrayList<Player>();
    }

    /**
     * Adds a player as a receiver. This should be done by roles when they
     * handle the message. Receivers of a message can see the message itself and
     * the sender's name.
     * 
     * @param player
     *            the receiver to be added
     */
    public void addReceiver(Player player) {
	receiverList.add(player);
    }

    /**
     * Adds a player as an anonymous receiver. This should be done by roles when
     * they handle the message. Anonymous receivers of a message can see the
     * message itself but not the sender's name.
     * 
     * @param player
     *            the receiver to be added
     */
    public void addAnonymousReceiver(Player player) {
	anonymousReceiverList.add(player);
    }

    /**
     * Adds a player as an override receiver. This should be done by roles when
     * they handle the message. Override receivers of a message can see the
     * message itself and the sender's name. In addition, if there is at least
     * one override receiver, then the message should not be sent to any other
     * receivers, only those on the override list.
     * 
     * @param player
     *            the receiver to be added
     */
    public void addOverrideReceiver(Player player) {
	overrideList.add(player);
    }

    /**
     * Gets the message.
     * 
     * @return the message
     */
    public String getMessage() {
	return message;
    }

    /**
     * Gets the sender.
     * 
     * @return the sender
     */
    public Player getSender() {
	return sender;
    }

    /**
     * Gets the receiver list.
     * 
     * @return the receiver list
     */
    public Player[] getReceiverList() {
	return receiverList.toArray(new Player[receiverList.size()]).clone();
    }

    /**
     * Gets the anonymous receiver list.
     * 
     * @return the anonymous receiver list
     */
    public Player[] getAnonymousReceiverList() {
	return anonymousReceiverList.toArray(
		new Player[anonymousReceiverList.size()]).clone();
    }

    /**
     * Gets the override list.
     * 
     * @return the override list
     */
    public Player[] getOverrideList() {
	return overrideList.toArray(new Player[overrideList.size()]).clone();
    }

    /**
     * Checks if the message is anonymous.
     * 
     * @return true, if is anonymous
     */
    public boolean isAnonymous() {
	return anonymous;
    }

    /**
     * Checks if the message is killed.
     * 
     * @return true, if is killed
     */
    public boolean isKilled() {
	return killed;
    }

    /**
     * Kills the message. This prevents the message from being sent to anyone.
     */
    public void kill() {
	killed = true;
    }

}
