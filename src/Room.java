import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

// draws itself but cannot advance anything or do anything with enemies
public class Room {
	
	/* Image that gets displayed when enemy dies */
	private static BufferedImage imgSplat;
	
	/* Images for room: either doors are open or closed */
    private BufferedImage imgOpen;
	private BufferedImage imgClosed;
	
	/* Linked list of all projectiles that have been fired by Blob or Enemies */
    private static List<Projectile> projectiles = new LinkedList<>();
	
	/* LinkedList of enemies, boulders, and blood splats in room */
    private List<Enemy> enemies;
    private List<Boulder> boulders;
	private List<EnemyBloodSplat> enemyBloodSplats;

	/**
     * Constructor
     */
    public Room(String imgFileOpen, String imgFileClosed) {
        projectiles.clear();
		try {
            this.imgOpen = ImageIO.read(new File(imgFileOpen));
			this.imgClosed = ImageIO.read(new File(imgFileClosed));
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
        enemies = new LinkedList<>();
        boulders = new LinkedList<>();
		enemyBloodSplats = new LinkedList<>();
    }
	
	/**
     * Adds projectile to game
     * 
     * @param projectile projectile to be added to game
     */
	public static void addProjectile(Projectile proj) {
        projectiles.add(proj);
	}
	
	/**
     * Advances everything in the room with reference to projectiles and each other
     * 
     * @param blob     the main character of game controlled by user
     *        iterProj iterator for all projectiles to advance
     */
	public static void clearProjectiles() {
        projectiles.clear();
	}
	
	/**
     * Checks whether room doors are open
     * 
     * @return Whether room doors are open
     */	
	public boolean isOpen() {
        return enemiesDefeated();
    }
		
	/**
     * Checks whether all enemies in room have been defeated
     * 
     * @return Whether all enemies in room have been defeated
     */	
	public boolean enemiesDefeated() {
        return enemies.isEmpty();
    }
	
	/**
     * Adds enemy to room
     * 
     * @param e enemy to be added to room
     */
    public void addEnemy(Enemy e) {
        enemies.add(e);
    }
	
	/**
     * Used only for testing
     * Removes first enemy in list of enemies in room
     * If no enemies in room, does nothing
     */
    public void removeEnemy() {
        Iterator<Enemy> iter = enemies.iterator();
		if (iter.hasNext()) {
			iter.next();
			iter.remove();
		}
    }
	
	/**
     * Adds boulder to room
     * 
     * @param b boulder to be added to room
     */
    public void addBoulder(Boulder b) {
        boulders.add(b);
    }
	
	/**
     * Advances everything in the room with reference to projectiles and each other
     * 
     * @param blob     the main character of game controlled by user
     *        iterProj iterator for all projectiles to advance
     */
	public void advance(Blob blob) {
        advanceProjectiles(blob);
		advanceBoulders(blob);
        advanceEnemies(blob);
	}
	
	/**
     * Advances a collection of projectiles with reference to this Room
     * Checks for collisions between a projectile and various objects in Room
     * 
     * @param blob     the main character of game controlled by user
     *        iterProj iterator for all projectiles to advance
     */
	private void advanceProjectiles(Blob blob) {
		Iterator<Projectile> iterProj = projectiles.iterator();
		projIteration:
		while (iterProj.hasNext()) {
			Projectile proj = iterProj.next();
			if (projCollidesWall(proj) || projCollidesBoulder(proj) || projCollidesEnemyOrBlob(proj, blob)) {
				iterProj.remove();
				continue projIteration;
			} else {
				proj.move(); 
			}
        }
    }
	
	/**
     * Checks projectile for collision with wall
     * 
     * @param proj projectile to check for collision with wall
     * @return Whether projectile collides with any boulder
     */
	private boolean projCollidesWall(Projectile proj) {
		Direction d = proj.hitWall();
		return d != null;
	}
	
	/**
     * Checks projectile for collision with boulder
     * 
     * @param proj projectile to check for collision with boulder
     * @return Whether projectile collides with any boulder
     */
	private boolean projCollidesBoulder(Projectile proj) {
		Iterator<Boulder> iterBould = boulders.iterator();
		while (iterBould.hasNext()) {
			Boulder bould = iterBould.next();
			if (proj.intersects(bould)) {
				return true;
			 }
		}
		return false;
	}
	    	
	/**
     * Checks projectile for collision with blob or enemy depending on type of projectile
     * Blob projectiles hurt enemies and Enemy projectiles hurt blob
     * 
     * @param proj projectile to check for collision with enemy or blob
     *        blob the main character of the game that the user controls
     * @return Whether projectile collides with any boulder
     */
	private boolean projCollidesEnemyOrBlob(Projectile proj, Blob blob) {
		if (proj.getClass().equals(Blob.BlobProjectile.class)) {
			Iterator<Enemy> iterEn = enemies.iterator();
			while (iterEn.hasNext()) {
				Enemy enemy = iterEn.next();
				if (proj.intersects(enemy)) {
					if (enemy.removeHealth()) {
						addEnemyBloodSplat(enemy);
						iterEn.remove();
					}
					return true;
				 }
			 }
		} else if (proj.getClass().equals(RegularEnemy.RegularEnemyProjectile.class) ||
				   proj.getClass().equals(Boss.BossProjectile.class)) {
			 if (proj.intersects(blob)) {
				 blob.removeHalfHealth();
				 return true;
			 }
		}
		return false;
	}
	
	
	/**
     * "Advance" all the boulders in the room by clipping main character's position
     * if it will intersect with a boulder
     * 
     * @param blob the main character of game controlled by user
     */
	private void advanceBoulders(Blob blob) {
		Iterator<Boulder> iterBould = boulders.iterator();	
		while (iterBould.hasNext()) {
			Boulder bould = iterBould.next();
			if (blob.willIntersect(bould)) {
				blob.setPx(blob.getPx() - blob.getVx());
				blob.setPy(blob.getPy() - blob.getVy());
			 }
		}
    }
	
	/**
     * Advance all the enemies in the room
     * 
     * @param blob the main character of game controlled by user
     */
	private void advanceEnemies(Blob blob) {
		Iterator<Enemy> iterEn = enemies.iterator();
        while (iterEn.hasNext()) {
            Enemy enemy = iterEn.next();
            enemy.advance(blob);
			if (blob.intersects(enemy)) {
				blob.removeHealth();
			}
            enemy.move();
        }
    }
	
	/**
     * Adds enemy blood splat to room
     * 
     * @param e the enemy that should be replaced with a blood splat
     */
	private void addEnemyBloodSplat(Enemy e) {
		int width = e.getWidth();
		int height = e.getHeight();
		int ebsSize = EnemyBloodSplat.SIZE;
		int x = e.getPx() + (int) ((double) (width - ebsSize) / 2);
		int y = e.getPy() + (int) ((double) (height - ebsSize) / 2);
        enemyBloodSplats.add(new EnemyBloodSplat(x, y));
    }
    
	/**
     * Draw method for Room
     * 
     * @param g The <code>Graphics</code> context used for drawing the object
     */
    public void draw(Graphics g) {
		if (this.isOpen()) {
			g.drawImage(imgOpen, 0, 0, GameCourt.ROOM_WIDTH, GameCourt.ROOM_HEIGHT, null);
		} else {
			g.drawImage(imgClosed, 0, 0, GameCourt.ROOM_WIDTH, GameCourt.ROOM_HEIGHT, null);
		}
        g.translate(GameCourt.BORDER, GameCourt.BORDER);
        Iterator<EnemyBloodSplat> iterSplat = enemyBloodSplats.iterator();
        while (iterSplat.hasNext()) {
            EnemyBloodSplat enemyBloodSplat = iterSplat.next();
            enemyBloodSplat.draw(g);
        }
		Iterator<Boulder> iterBould = boulders.iterator();
        while (iterBould.hasNext()) {
            Boulder bould = iterBould.next();
            bould.draw(g);
        }
        Iterator<Enemy> iterEn = enemies.iterator();
        while (iterEn.hasNext()) {
            Enemy e = iterEn.next();
            e.draw(g);
        }
		Iterator<Projectile> iterProj = projectiles.iterator();
		while (iterProj.hasNext()) {
            Projectile proj = iterProj.next();
            proj.draw(g);
        }
    }
	
	/**
	 * Object representing the blood splats left behind when an enemy dieds
	 */
	public class EnemyBloodSplat extends GameObj {

		private static final String IMG_FILE = "files/BloodSplatRed.png";
		public static final int SIZE = 120;

		/**
		* Constructor
		*/
		public EnemyBloodSplat(int x, int y) {
			super(0, 0, 0, x, y, SIZE, SIZE, 0);
			try {
				if (imgSplat == null) {
					imgSplat = ImageIO.read(new File(IMG_FILE));
				}
			} catch (IOException e) {
				System.out.println("IOException: " + e.getMessage());
			}
		}

		@Override
		public void draw(Graphics g) {
			g.drawImage(imgSplat, this.getPx(), this.getPy(), SIZE, SIZE, null);
		}

	}
}