import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Main character of game represented by a green blob on the screen
 */
public class Blob extends GameObj {
    
    private static final String IMG_FILE_RIGHT = "files/BlobRight.png";
    private static final String IMG_FILE_LEFT = "files/BlobLeft.png";
    private static final String IMG_FILE_RIGHT_SQUISHED = "files/BlobRightSquished.png";
    private static final String IMG_FILE_LEFT_SQUISHED = "files/BlobLeftSquished.png";
    private static final String IMG_FILE_RIGHT_HIT = "files/BlobRightHit.png";
    private static final String IMG_FILE_LEFT_HIT = "files/BlobLeftHit.png";
    private static final String IMG_FILE_RIGHT_SQUISHED_HIT = "files/BlobRightSquishedHit.png";
    private static final String IMG_FILE_LEFT_SQUISHED_HIT = "files/BlobLeftSquishedHit.png";
    private static final String IMG_FILE_DEAD = "files/BloodSplatGreen.png";
    private static final String IMG_FILE_90 = "files/Blob90.png";
	private static final String IMG_FILE_180 = "files/Blob180.png";
	private static final String IMG_FILE_270 = "files/Blob270.png";
	private static final String IMG_FILE_PROJECTILE = "files/BlobProjectile.png";
	
	/* Constants specific to Blob */
    private static final int NUM_LIVES = 6;
    private static final int INIT_POS_X = 0;
    private static final int INIT_POS_Y = 0;
    private static final int INIT_VEL_X = 0;
    private static final int INIT_VEL_Y = 0;
	
	/* Interval in seconds at which Blob is animated squishing (while moving) 
	 * and changing color (after being hit) 
	 */
    private static final double SQUISH_INTERVAL = 0.3;
    private static final double HURT_INTERVAL = 1.0;
    
	/* Size of image and radius used for collisions */
    public static final int SIZE = 60;
    private static final int RADIUS = 29;
    
	/* Current image associated with Blob Object */
    private BufferedImage img;
	
	/* Images associated with Blob Object */
    private static BufferedImage imgRight;
    private static BufferedImage imgLeft;
    private static BufferedImage imgRightSquished;
    private static BufferedImage imgLeftSquished;
    private static BufferedImage imgRightHit;
    private static BufferedImage imgLeftHit;
    private static BufferedImage imgRightSquishedHit;
    private static BufferedImage imgLeftSquishedHit;
    private static BufferedImage imgDead;
	private static BufferedImage img90;
	private static BufferedImage img180;
	private static BufferedImage img270;
	private static BufferedImage imgProj;
    
	/* Health Bar for Blob */
    private HealthBar healthBar;
	
	/* State Variables */
    private boolean isDead = false;
    private int counterSquish = -1;
    private int squishFreq;
    private boolean squishedState = false;
    private int numHurtFrames;
    private int counterHurt;
	private boolean isTransparent = false;
	private boolean rotateMode = false;
	private int rotation = -1;

	/**
     * Constructor
     */
    public Blob(int velocity) {
        super(velocity, INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, RADIUS);
        this.squishFreq = (int) (Math.round((double) GameCourt.FPS * SQUISH_INTERVAL));
        this.healthBar = new HealthBar(NUM_LIVES);
        this.numHurtFrames = (int) (Math.round((double) GameCourt.FPS * HURT_INTERVAL));
        this.counterHurt = numHurtFrames;
        try {
            if (imgRight == null) {
                imgRight = ImageIO.read(new File(IMG_FILE_RIGHT));
                imgLeft = ImageIO.read(new File(IMG_FILE_LEFT));
                imgRightSquished = ImageIO.read(new File(IMG_FILE_RIGHT_SQUISHED));
                imgLeftSquished = ImageIO.read(new File(IMG_FILE_LEFT_SQUISHED));
                imgRightHit = ImageIO.read(new File(IMG_FILE_RIGHT_HIT));
                imgLeftHit = ImageIO.read(new File(IMG_FILE_LEFT_HIT));
                imgRightSquishedHit = ImageIO.read(new File(IMG_FILE_RIGHT_SQUISHED_HIT));
                imgLeftSquishedHit = ImageIO.read(new File(IMG_FILE_LEFT_SQUISHED_HIT));
                imgDead = ImageIO.read(new File(IMG_FILE_DEAD));
				img90 = ImageIO.read(new File(IMG_FILE_90));
				img180 = ImageIO.read(new File(IMG_FILE_180));
				img270 = ImageIO.read(new File(IMG_FILE_270));
				imgProj = ImageIO.read(new File(IMG_FILE_PROJECTILE));
            }
            this.img = imgRight;
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
    
    public void toggleTransparency() {
		this.isTransparent = !this.isTransparent;
    }
	
	public void rotate() {
		rotateMode = true;
		rotation = ((rotation + 1) % 4);
    }
	
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), SIZE, SIZE, null);
    }
    
	/**
     * Special draw method for Blob object that allows for animation of blob 
     * and to its own draw healthBar associated with blob
     * 
     * @param g The <code>Graphics</code> context used for drawing the object
     *        moveKeyPressed whether the blob is currently being controlled to move
     *                       used to animate blob
     *        d direction that blob is currently moving used to animate blob
     *        courtWidth width of court used to draw health bar
     *        border size of border between room and court used to draw health bar
     */
    public void draw(Graphics g, boolean moveKeyPressed, Direction d, int courtWidth, int border) {

		if (isDead) {
            img = imgDead;
		} else if (rotateMode) {
			switch (rotation) {
			case 0:
				img = imgRight;
				break;  
			case 1:
				img = img90;
				break;
			case 2:
				img = img180;
				break;
			case 3:
				img = img270;
				break;
			}
		} else if (counterHurt < numHurtFrames) {
            drawHitOrNot(imgLeftSquishedHit, imgRightSquishedHit, imgLeftHit, imgRightHit,
                         moveKeyPressed, d);
        } else {
            drawHitOrNot(imgLeftSquished, imgRightSquished, imgLeft, imgRight,
                         moveKeyPressed, d);
        }
		
		if (!isTransparent) {
			g.drawImage(img, this.getPx(), this.getPy(), SIZE, SIZE, null);
		}
			
        counterHurt++;
        if (!moveKeyPressed) {
            squishedState = false;
            counterSquish = 0;
        } else {
            counterSquish++;
            if ((counterSquish % squishFreq) == 0) {
               squishedState = !squishedState;
            }  
        }
        // draw health bar
        g.translate(courtWidth, -(int) Math.round((double) border / 2));
        healthBar.draw(g);
    }
   
	/**
     * Helper method for draw method with input arguments
     * 
     * @param leftSquishedImage image of blob facing left and being squished
     *        rightSquishedImage image of blob facing right and being squished
     *        leftImage image of Blob facing left and not being squished
     *        rightImage image of Blob facing right and not being squished
     *        moveKeyPressed whether the Blob is currently being controlled to move
     *        d direction that Blob is currently moving
     */
    private void drawHitOrNot(BufferedImage leftSquishedImage, BufferedImage rightSquishedImage,
                              BufferedImage leftImage, BufferedImage rightImage,
                              boolean moveKeyPressed, Direction d) {
        if (moveKeyPressed && squishedState) {
            drawLeftOrRight(leftSquishedImage, rightSquishedImage, d);
        } else {
            drawLeftOrRight(leftImage, rightImage, d);
        }
    }
    
	/**
     * Helper method for drawHitOrNot
     * 
     * @param leftImage image of Blob facing left, either squished or not
     *        rightImage image of Blob facing right, either squished or not
     *        d direction that Blob is currently moving
     */
    private void drawLeftOrRight(BufferedImage leftImage, BufferedImage rightImage, Direction d) {
        if (d == Direction.LEFT) {
            img = leftImage;
        } else if (d == Direction.RIGHT) {
            img = rightImage;
        }
    }
    
	/**
     * Checks whether Blob is dead
     * @Return Whether Blob has lost all health
     */
    public boolean isDead() {
        return this.isDead;        
    }
    
	/**
     * Removes a half unit of health from Blob
     */
    public void removeHalfHealth() {
		counterHurt = 0;
		this.isDead = healthBar.removeHalfHeart();
    }
    
	/**
     * Removes a full unit of health from Blob
     */
    public void removeHealth() {
		if (counterHurt > numHurtFrames) { 
			counterHurt = 0;
			this.isDead = healthBar.removeFullHeart();
		}
    }
    
	/**
     * Fires a projectile from Blob
     * @param roomHandler the instance of the RoomHandler that controls the environment of the game
     *        d direction that the projectile should be fired in
     */
    public void fire(RoomHandler roomHandler, Direction d) {
        int x = this.getPxCenter();
        int y = this.getPyCenter();
        roomHandler.addProjectile(new BlobProjectile(x, y, d));
    }
	
	/**
	 * Projectile associated with Blob
	 */
	public class BlobProjectile extends Projectile {

		/* Size of image and radius used for collisions */
		public static final int SIZE = 16;
		public static final int RADIUS = 8;

		/**
		 * Constructor
		 */
		public BlobProjectile(int x, int y, Direction d) {
			super(x - RADIUS, y - RADIUS, SIZE, RADIUS, d, null);
		}

		@Override
		public void draw(Graphics g) {
			g.drawImage(imgProj, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
		}

	}
	
}