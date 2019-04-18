import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FinalRoom extends Room {
    
	private static final String IMG_PORTAL = "files/Portal.png";
	
	private static BufferedImage imgPortal;
	
    private Boss boss;
	private Portal portal;
	private boolean portalActivated = true;
    
    // draws itself but cannot advance anything or do anything with enemies
    public FinalRoom(String imgFileOpen, String imgFileClosed) {
        super(imgFileOpen, imgFileClosed);		
		portal = new Portal();
		this.boss = new Boss(0, 0);
		boss.moveToCenter();
		this.addEnemy(boss);
    }   

	 public void activatePortal() {
		portalActivated = true;
    }
		
	 public void deactivatePortal() {
		portalActivated = false;
    }
	
	 public void activateBoss() {
		boss.activate();
    }
	
	public boolean isPortalActivated() {
		return portalActivated;
    }
	
    public boolean intersectsPortal(GameObj o) {
        return o.intersects(portal);
    }
	
	@Override
    public void draw(Graphics g) {
        super.draw(g);
        portal.draw(g);
    }
	
	@Override
	public boolean isOpen() {
		return portalActivated;
	}
	
	/**
	 * Portal at the center of the final room
	 * The portal will disappear after colliding with it and reappear when the Boss is defeated
	 * After the Boss is defeated, if the main character touches the portal, the game is won
	 */
	public class Portal extends GameObj {

		public static final int SIZE = 200;
		public static final int RADIUS = 100;

		/**
		* Constructor
		*/
		public Portal() {
			super(0, 0, 0, 0, 0, SIZE, SIZE, RADIUS);
			try {
				if (imgPortal == null) {
					imgPortal = ImageIO.read(new File(IMG_PORTAL));
				}
			} catch (IOException e) {
				System.out.println("IOException: " + e.getMessage());
			}
			this.moveToCenter();
		}

		@Override
		public void draw(Graphics g) {
			if (portalActivated) {
				g.drawImage(imgPortal, this.getPx(), this.getPy(), SIZE, SIZE, null);
			}
		}
	}
}
