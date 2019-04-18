import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class HealthBar {
        
    private static final String IMG_EMPTY_HEART = "files/HeartEmpty.png";
    private static final String IMG_HALF_HEART = "files/HeartHalf.png";
    private static final String IMG_FULL_HEART = "files/HeartFull.png";
    private static final int HEART_SIZE = 30;
    
    private static BufferedImage imgEmptyHeart;
    private static BufferedImage imgHalfHeart;
    private static BufferedImage imgFullHeart;
    
	/* Health information, with numLives being the number of hearts possible
	 * and the maxHealth being the number of half hearts possible */
    private int numLives;
    private int maxHealth;
    private int health;
    
	/**
     * Constructor
     */
    public HealthBar(int numLives) {
        this.numLives = numLives;
        this.maxHealth = numLives * 2;
        health = maxHealth;
        try {
            if (imgEmptyHeart == null) {
                imgEmptyHeart = ImageIO.read(new File(IMG_EMPTY_HEART));
                imgHalfHeart = ImageIO.read(new File(IMG_HALF_HEART));
                imgFullHeart = ImageIO.read(new File(IMG_FULL_HEART));
            }
        } catch (IOException e) {
            System.out.println("IOException:" + e.getMessage());
        }    
    }
 
	/**
     * Draws HealthBar from graphics context going left
     * 
     * @param g The <code>Graphics</code> context used for drawing the object
     *        border size of border between room and court used to draw things inside the court
     */
    public void draw(Graphics g) {
        int heartNumber = numLives;
        int numFullHearts = health / 2;
        int numHalfHearts = health % 2;
        int numEmptyHearts = heartNumber - numFullHearts - numHalfHearts;
        int x = -HEART_SIZE;
        for (int i = 0; i < numEmptyHearts; i++) {
            g.drawImage(imgEmptyHeart, x, 0, HEART_SIZE, HEART_SIZE, null);   
            x -= HEART_SIZE;
        }
        if (numHalfHearts == 1) {
            g.drawImage(imgHalfHeart, x, 0, HEART_SIZE, HEART_SIZE, null);  
            x -= HEART_SIZE;
        }
        for (int i = 0; i < numFullHearts; i++) {
            g.drawImage(imgFullHeart, x, 0, HEART_SIZE, HEART_SIZE, null);  
            x -= HEART_SIZE;
        }
    }
        
	/**
     * Removes half a heart from HealthBar
     * 
     * @return Whether there is no health left
     */
    public boolean removeHalfHeart() {
        health -= 1;
        return (health <= 0);
    }
    
	/**
     * Removes a full heart from HealthBar
     * 
     * @return Whether there is no health left
     */
    public boolean removeFullHeart() {
        health -= 2;
        return (health <= 0);
    }
    
}
