package eggbonk.core;

import java.io.Serializable;

/**
 * This class represents an egg in an Egg Bonk game. 
 * An egg starts out with damage value {@linkplain Egg#UNDAMAGED}, and
 * will deteriorate to {@linkplain Egg#CRACKED} and finally {@linkplain Egg#BROKEN} with
 * successive calls to the {@linkplain #crack()} method. An Egg's damage level can be checked
 * with the {@linkplain #getDamage()} method.
 * @author ianbulovic
 */
public class Egg implements Serializable {

	private static final long serialVersionUID = -8374595842387842732L;
	
	private int damage;
	public static final int UNDAMAGED = 0, CRACKED = 1, BROKEN = 2;
	
	/**
	 * Creates an undamaged egg.
	 */
	public Egg() {
		damage = UNDAMAGED;
	}
	
	/**
	 * Gets the damage of this egg. The value returned will be equal to {@linkplain Egg#UNDAMAGED},
	 * {@linkplain Egg#CRACKED}, or {@linkplain Egg#BROKEN}.
	 * @return this Egg's damage level.
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Checks if this Egg is broken. <br>
	 * Calling this method is equivalent to: 
	 * {@link #getDamage()}{@code  == }{@link Egg#BROKEN}
	 * @return {@code true} if this Egg is broken, {@code false} otherwise.
	 */
	public boolean isBroken() {
		return damage == BROKEN;
	}
	
	/**
	 * Damages this egg. Changes the value returned by {@linkplain #getDamage()} in the following
	 * progression: <br>
	 * {@linkplain Egg#UNDAMAGED} ⟶ {@linkplain Egg#CRACKED} ⟶ {@linkplain Egg#BROKEN}
	 */
	public void crack() {
		if(damage != BROKEN) damage++;
	}
}
