/**
 * Enemy that patrols vertically or horizontally and shoots perpendicularly
 */
public class PatrollingEnemy extends RegularEnemy {
    
	/* Interval in seconds at which SnipingEnemy fires projectiles 
	 * and velocity that it moves at */
    private static final double FIRING_INTERVAL = 1;
    private static final int INIT_VEL = 4;
    
    private Direction projDirection;
    private int counterFire = -1;
    private int firingFreq; 
    
	/**
    * Constructor
    */
    public PatrollingEnemy(int px, int py, Direction projDirection) {
        super(INIT_VEL, px, py);
        this.projDirection = projDirection;
        this.firingFreq = (int) (Math.round((double) GameCourt.FPS * FIRING_INTERVAL));
        if (projDirection == Direction.UP || projDirection == Direction.DOWN) {
                this.setVx(INIT_VEL);
        } else {
            this.setVy(INIT_VEL);
        }
    }
    
	@Override
    public void advance(Blob blob) {
        if ((counterFire % firingFreq) == 0) {
            this.fire(projDirection, null);
        }
        counterFire++;
        // change velocity if PatrollingEnemy will hit wall in next time step
        Direction d = this.hitWall();
        if (this.hitWall() != null) {
            if (d == Direction.UP || d == Direction.DOWN) {
                this.setVy(-this.getVy());
            } else {
                this.setVx(-this.getVx());
            }
        }
    }
    
}