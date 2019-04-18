import java.util.Collections;
import java.util.Arrays;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *  Represents the final enemy in the game. It pops up by surprise in the game.
 */
public class Boss extends Enemy {

	private static final String IMG_FILE = "files/Boss.png";
	private static final String IMG_FILE_HIT = "files/BossHit.png";
	public static final String IMG_FILE_PROJECTILE = "files/BossProjectile.png";

	private static final int NUM_LIVES = 20;
	private static final double ATTACK_INTERVAL = 8;
	private static final double FIRING_INTERVAL = 1.8;
	private static final int[] ATTACK_ORDER = {0, 1, 2, 3};

	public static final int SIZE = 120;
	public static final int RADIUS = 52;

	private static BufferedImage img;
	private static BufferedImage imgNotHit;
	private static BufferedImage imgHit;
	private static BufferedImage imgProj;

	private boolean activated = false;
	private int counterAttackMode = 0;
	private int attackModeFreq;
	private AttackMode attack = AttackMode.CENTER;
	private int attackIdx = 0;
	
	private boolean firingMode = true;
	private int counterFire = 1;
	private int firingFreq; 
	private int fourthSecond;
    
    /**
    * By default is still until given a velocity
    */
    public Boss(int px, int py) {
		super(0, px, py, SIZE, RADIUS, NUM_LIVES);
		this.attackModeFreq =  (int) (Math.round((double) GameCourt.FPS * ATTACK_INTERVAL));
		this.firingFreq = (int) (Math.round((double) GameCourt.FPS * FIRING_INTERVAL));
		this.fourthSecond = (int) (Math.round((double) GameCourt.FPS / 4));
		try {
			if (img == null) {
				imgNotHit = ImageIO.read(new File(IMG_FILE));
				imgHit = ImageIO.read(new File(IMG_FILE_HIT));
				imgProj = ImageIO.read(new File(IMG_FILE_PROJECTILE));
			}
			img = imgHit;
			} catch (IOException e) {
				System.out.println("IOException: " + e.getMessage());
		}
    }
    
    @Override
    public void draw(Graphics g) {
		if (!activated) {
			return;
		}
		if (this.isHurt()) {
            img = imgHit;
        } else {
            img = imgNotHit;
        }
        g.drawImage(img, this.getPx(), this.getPy(), SIZE, SIZE, null);
    }
    
	public void activate() {
		activated = true;
	}

	@Override
	public void advance(Blob blob) {
		if (!activated) {
			return;
		}
		int modFire = counterFire % firingFreq;
		if (modFire == 0 && (firingFreq - modFire) >= fourthSecond &&
			counterFire > (firingFreq * 1.5)) {
			fire();
		}
		counterFire++;
		counterAttackMode++;
		if ((counterAttackMode % attackModeFreq) == 0) {
			if (attackModeFreq > 1) {
				attackModeFreq -= fourthSecond;
			}
			if (attackIdx == ATTACK_ORDER.length) {
				attack = AttackMode.CENTER;
				attackIdx = 0;
				Collections.shuffle(Arrays.asList(ATTACK_ORDER));
			} else {
				attack = AttackMode.values()[attackIdx];
				attackIdx++;
			}
			switch (attack) {
				case CENTER:
					this.moveToCenter();
					break;  
				case UP:
					this.moveToEdge(Direction.UP);
					break;
				case LEFT:
					this.moveToEdge(Direction.LEFT);
					break;
				case RIGHT:
					this.moveToEdge(Direction.RIGHT);
					break;
				case DOWN:
					this.moveToEdge(Direction.DOWN);
					break;
			}
		}
	}
	
	/**
     * Fires projectiles from Boss
     * 
     * @param roomHandler the instance of the RoomHandler that controls the environment of the game
     */
	private void fire() {
		int x = this.getPxCenter();
		int y = this.getPyCenter();
		if (firingMode) {
			Room.addProjectile((Projectile) new BossProjectile(x, y, Direction.UP, null));
			Room.addProjectile((Projectile) new BossProjectile(x, y, Direction.DOWN, null));
			Room.addProjectile((Projectile) new BossProjectile(x, y, Direction.RIGHT, null));
			Room.addProjectile((Projectile) new BossProjectile(x, y, Direction.LEFT, null));
		} else {
			Room.addProjectile((Projectile) new BossProjectile(x, y, Direction.UP, Direction.RIGHT));
			Room.addProjectile((Projectile) new BossProjectile(x, y, Direction.UP, Direction.LEFT));
			Room.addProjectile((Projectile) new BossProjectile(x, y, Direction.DOWN, Direction.RIGHT));
			Room.addProjectile((Projectile) new BossProjectile(x, y, Direction.DOWN, Direction.LEFT));
		}
		firingMode = !firingMode;
	}

	/**
		 * Enumerator for all possible positions that Boss can attack from
		 */
	public enum AttackMode {
		UP, DOWN, LEFT, RIGHT, CENTER;
	}

	/**
	 * Projectile associated with Boss
	 */
	public class BossProjectile extends Projectile {

		public static final int SIZE = 20;
		public static final int RADIUS = 10;

		/**
		 * Constructor
		 */
		public BossProjectile(int x, int y, Direction d1, Direction d2) {
			super(x - RADIUS, y - RADIUS, SIZE, RADIUS, d1, d2);
		}

		@Override
		public void draw(Graphics g) {
			g.drawImage(imgProj, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(),
					null);
		}

	}

}