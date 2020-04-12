package eggbonk.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

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
	
	private transient BufferedImage image;
	
	/**
	 * Creates an undamaged egg with default image.
	 */
	public Egg() {
		damage = UNDAMAGED;
		try {
			image = ImageIO.read(new File("default-egg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 /**
     * Creates an undamaged egg with specified image.
     */
    public Egg(Image im) {
        image = (BufferedImage) im;
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
	
	/**
	 * Sets the image of this egg.
	 * @param img the new image.
	 */
	public void setImage(BufferedImage img) {
		image = img;
	}
	
	/**
	 * Gets the image of this egg.
	 * @return this egg's image.
	 */
	public BufferedImage getImage() {
		return image;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Egg)) return false;
		Egg o = (Egg) other;
		return damage == o.damage && equals(image, o.image);
	}
	
	public boolean equals(BufferedImage a, BufferedImage b) {
		if (a.getWidth() != b.getWidth() || a.getHeight() != b.getHeight())
		    return false;

		  int width  = a.getWidth();
		  int height = a.getHeight();

		  for (int y = 0; y < height; y++) 
		    for (int x = 0; x < width; x++) 
		      if (a.getRGB(x, y) != b.getRGB(x, y)) 
		        return false;
		      
		  return true;
	}
	
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		ImageIO.write(image, "png", out);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		image = ImageIO.read(in);
	}
}
