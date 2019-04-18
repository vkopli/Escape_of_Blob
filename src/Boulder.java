import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * square of a specified color.
 */
public class Boulder extends GameObj {
    
    private static final String IMG_FILE = "files/Boulder.png";
    
    public static final int SIZE = 90;
    public static final int RADIUS = 40;

    private static BufferedImage img;
    
    /**
    * By default is still until given a velocity
    */
    public Boulder(int x, int y) {
        super(-0, 0, 0, x, y, SIZE, SIZE, RADIUS);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
    
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), SIZE, SIZE, null);
    }
    
}