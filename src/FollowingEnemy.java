/**
 * Enemy that follows and tries to shoot at the main character, blob
 */
public class FollowingEnemy extends RegularEnemy {
    
	/* Interval in seconds at which FollowingEnemy fires projectiles */
    private static final double FIRING_INTERVAL = 1.2;
	
	/* Threshold distance from ideal coordinates for FollowingEnemy to fire projectile at Blob */
	private static final int THRESHOLD = 40;
	
	/* Velocity at which FollowingEnemy should pursue Blob */
    private static final int VELOCITY = 2;
    
    private int counterFire = -1;
    private int firingFreq;
	
	/**
    * Constructor
    */
	public FollowingEnemy(int px, int py) {
        super(VELOCITY, px, py);
        this.firingFreq = (int) (Math.round((double) GameCourt.FPS * FIRING_INTERVAL));
    }
    
	@Override
    public void advance(Blob blob) {
		int tgtX = blob.getPx();
		int tgtY = blob.getPy();
		this.aimAtPoint(tgtX, tgtY);
		if (counterFire % firingFreq == 0) {
			int srcX = this.getPx();
			int srcY = this.getPy();
			if (tgtX > srcX) {
				fireRightOrLeft(Direction.RIGHT, srcX, srcY, tgtX, tgtY);
			} else if (tgtX < srcX) {
				fireRightOrLeft(Direction.LEFT, srcX, srcY, tgtX, tgtY);
			} else if (tgtY > srcY) {
				this.fire(Direction.DOWN, null);
			} else if (tgtY < srcY) {
				this.fire(Direction.UP, null);
			}
		}
		counterFire++;
	}
	
	/**
     * Helper method
     * @param roomHandler the instance of the RoomHandler that controls the environment of the game
     *        d direction that the projectile should be fired in
     */
	public void fireRightOrLeft(Direction d, int srcX, int srcY, int tgtX, int tgtY) {
		if (Math.abs(tgtY - srcY) < THRESHOLD) {
			this.fire(d, null);
		} else if (Math.abs(Math.abs(tgtX - srcX) - Math.abs(tgtY - srcY)) < THRESHOLD) {
			if (tgtY > srcY) {
				this.fire(d, Direction.DOWN);
			} else {
				this.fire(d, Direction.UP);
			}
		}
	}
		
}