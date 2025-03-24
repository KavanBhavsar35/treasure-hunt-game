package tile;

import java.awt.image.BufferedImage;

// CONSTRUCTOR
public class Tile {

	public BufferedImage image = null;
	public boolean collision  = false;
	
	public Tile(BufferedImage image, boolean collision) {
		
		// INITIATION
		this.collision = collision;
		this.image = image;
	}
}
