import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Enemy that is represented by red rounded triangular image
 */
public abstract class RegularEnemy extends Enemy {
    
    private static final String IMG_FILE_RIGHT = "files/EnemyRight.png";
    private static final String IMG_FILE_LEFT = "files/EnemyLeft.png";
    private static final String IMG_FILE_RIGHT_HIT = "files/EnemyRightHit.png";
    private static final String IMG_FILE_LEFT_HIT = "files/EnemyLeftHit.png";
	private static final String IMG_FILE_PROJECTILE = "files/EnemyProjectile.png";
	
	private static final int NUM_LIVES = 4;
	
	/* Size of image and radius used for collisions */
    public static final int SIZE = 60;
    private static final int RADIUS = 30;

    private static BufferedImage img;
    private static BufferedImage imgRight;
    private static BufferedImage imgLeft;
    private static BufferedImage imgRightHit;
    private static BufferedImage imgLeftHit;
	private static BufferedImage imgProj;
	
	/* true if instance is set to display the RegularEnemy facing left */
	private boolean directionSetLeft = false;
    
    /**
    * Constructor
    */
    public RegularEnemy(int velocity, int px, int py) {
        super(velocity, px, py, SIZE, RADIUS, NUM_LIVES);
        try {
            if (imgRight == null) {
                imgRight = ImageIO.read(new File(IMG_FILE_RIGHT));
                imgLeft = ImageIO.read(new File(IMG_FILE_LEFT));
                imgRightHit = ImageIO.read(new File(IMG_FILE_RIGHT_HIT));
                imgLeftHit = ImageIO.read(new File(IMG_FILE_LEFT_HIT));
				imgProj = ImageIO.read(new File(IMG_FILE_PROJECTILE));
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
    
	/**
    * Sets direction that enemy is facing to be left
    */
	public void setDirectionLeft() {
        directionSetLeft = true;
    }
	
    @Override
    public void draw(Graphics g) {
        if (this.isHurt()) {
            drawLeftOrRight(imgLeftHit, imgRightHit);
        } else {
            drawLeftOrRight(imgLeft, imgRight);
        }
        g.drawImage(img, this.getPx(), this.getPy(), SIZE, SIZE, null);
    }
	
	/**
     * Helper method for draw
     * 
     * @param leftImage image of RegularEnemy facing left, either squished or not
     *        rightImage image of RegularEnemy facing right, either squished or not
     *        d direction that RegularEnemy is currently moving
     */
    private void drawLeftOrRight(BufferedImage leftImage, BufferedImage rightImage) {
		if (directionSetLeft || this.getVx() < 0) {
			img = leftImage;
        } else {
            img = rightImage;
        }
    }
	
	/**
     * Fires a projectile from RegularEnemy
     * @param roomHandler the instance of the RoomHandler that controls the environment of the game
     *        d1 main direction that the projectile should be fired in
     *        d2 secondary direction that the projectile should be fired in 
     *           (null if no secondary direction)
     */
    public void fire(Direction d1, Direction d2) {
        int x = this.getPxCenter();
        int y = this.getPyCenter();
        Room.addProjectile((Projectile) new RegularEnemyProjectile(x, y, d1, d2));
    }
	
	/**
     * Default advance method that updates the velocity and state of the RegularEnemy
     * 
     * @param blob the main character of the game, used by some RegularEnemies
     */
	public abstract void advance(Blob blob);
	
	/**
	 * Projectile associated with Blob
	 */
	public class RegularEnemyProjectile extends Projectile {

		public static final int SIZE = 16;
		public static final int RADIUS = 8;

		/**
		 * Constructor
		 */
		public RegularEnemyProjectile(int x, int y, Direction d1, Direction d2) {
			super(x - RADIUS, y - RADIUS, SIZE, RADIUS, d1, d2);
		}

		@Override
		public void draw(Graphics g) {
			g.drawImage(imgProj, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
		}

	}
	
}