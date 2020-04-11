package eggbonk.core;

import java.io.Serializable;

/**
 * This class represents a participant in the Egg Bonk challenge. 
 * Each Player starts with two {@linkplain Egg}s, and can at any point report
 * its current active egg and its current backup egg.
 * @author ianbulovic
 */
public class Player implements Serializable{
    
    //TODO equals

	private static final long serialVersionUID = -8258986605640084236L;
	
	String name;
	private Egg first, second;
	
	/**
	 * Creates an egg bonk participant.
	 * @param name this Player's name.
	 * @param first this Player's primary egg.
	 * @param second this Player's secondary egg.
	 */
	public Player(String name, Egg first, Egg second) {
		this.name = name;
		this.first = first;
		this.second = second;
	}
	
	/**
	 * Gets the current egg of this Player. The current egg is the first egg in their
	 * possession that is not completely broken. 
	 * @return the current egg, or null if all of this Player's eggs are broken.
	 */
	public Egg currentEgg() {
		if(!first.isBroken()) return first;
		if(!second.isBroken()) return second;
		return null;
	}
	
	/**
	 * Gets the backup egg of this Player. The backup egg is the second egg in their
	 * possession that is not completely broken.
	 * @return the backup egg, or null if this Player has one or fewer unbroken eggs.
	 */
	public Egg backupEgg() {
		if(!second.isBroken()) return second;
		return null;
	}
	
	/**
	 * Reports whether this Player is out of the game, i.e. all of their eggs are broken.
	 * @return whether this Player is out.
	 */
	public boolean isOut() {
		return currentEgg() == null;
	}
	
	/**
	 * Returns this players name.
	 */
	@Override
	public String toString() {
		return name;
	}
	
}
