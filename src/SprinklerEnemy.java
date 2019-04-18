/**
 * Enemy that rotates in range of -180 to 180 degrees and shoots projectiles
 */
public class SprinklerEnemy extends RegularEnemy {
    
	/* Interval in seconds at which FollowingEnemy fires projectiles 
	 * and length of time between changing the firing angle like a sprinkler */
    private static final double FIRING_INTERVAL = 0.5;
	private static final double ANGLE_INTERVAL = 2;
	
    private int counter = 0;
    private int firingFreq;
	private int angleFreq;
	
	// whether angle of next projectile will be straight or at a diagonal
	private boolean straightMode = true;
	
	// whether projectile shots are moving towards or away from original direction of enemy
	private boolean movingToCenter = false;
	
	// whether projectile is set to project left of enemy or right of enemy
	private boolean leftMode = true;
	
	// reference direction for SprinklerEnemy
	private Direction projDirection;
	
	// primary and secondary directions for projectiles
	private Direction projDirection1;
	private Direction projDirection2;
	
	// directions that projectiles range across
	private Direction primaryDirection1;
	private Direction primaryDirection2;
	
	/**
    * Constructor
    */
	public SprinklerEnemy(int px, int py, Direction projDirection) {
        super(0, px, py);
        this.firingFreq = (int) (Math.round((double) GameCourt.FPS * FIRING_INTERVAL));
		this.angleFreq = (int) (Math.round((double) GameCourt.FPS * ANGLE_INTERVAL));
		this.projDirection = projDirection;
		switch (projDirection) {
		case UP:
			primaryDirection1 = Direction.LEFT;
			primaryDirection2 = Direction.RIGHT;
			break;  
		case RIGHT:
			primaryDirection1 = Direction.UP;
			primaryDirection2 = Direction.DOWN;
			break;
		case LEFT:
			this.setDirectionLeft();
			primaryDirection1 = Direction.DOWN;
			primaryDirection2 = Direction.UP;
			break;
		case DOWN:
			primaryDirection1 = Direction.RIGHT;
			primaryDirection2 = Direction.LEFT;
			break;
		}
    }
    
	@Override
    public void advance(Blob blob) {
		if (counter % angleFreq == 0) {
			if (straightMode) {
				if (movingToCenter) {
					projDirection1 = projDirection;
					projDirection2 = null;
					leftMode = !leftMode;
				} else {
					choosePrimaryDirection();
					projDirection2 = null;
				}
				movingToCenter = !movingToCenter;
			} else {
				choosePrimaryDirection();
				projDirection2 = projDirection;
			}
			straightMode = !straightMode;
		}
		if (counter % firingFreq == 0) {
			this.fire(projDirection1, projDirection2);
		}
		counter++;
	}
	
	/**
     * Helper method for advance
     * Chooses primary direction based of whether SprinklerEnemy is currently firing left or right
     */
	private void choosePrimaryDirection() {
		if (leftMode) {
			projDirection1 = primaryDirection1;
		} else {
			projDirection1 = primaryDirection2;
		}
	}
		
}