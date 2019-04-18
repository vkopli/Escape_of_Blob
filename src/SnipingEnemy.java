/**
 * Enemy that stays still and shoots bursts of projectiles 
 */
public class SnipingEnemy extends RegularEnemy {
    
	/* Interval in seconds at which SnipingEnemy fires projectiles 
	 * and length of time that it pauses between firing projectiles */
    private static final double FIRING_INTERVAL = 0.5;
	private static final double PAUSE_INTERVAL = 2;
    
	/* Primary and secondary directions at which enemy fires projectile at given moment */
    private Direction projDirection1;
	private Direction projDirection2;
	
    private int counterFire;
	private int counterPause = 0;
	private boolean firingMode;
    private int firingFreq; 
	private int pauseFreq; 
    
	/**
    * Constructor
    */
    public SnipingEnemy(int px, int py, Direction projDirection1, Direction projDirection2, 
						double offsetTime, boolean firingMode) {
        super(0, px, py);
        this.projDirection1 = projDirection1;
		this.projDirection2 = projDirection2;
        this.firingFreq = (int) (Math.round((double) GameCourt.FPS * FIRING_INTERVAL));
		this.pauseFreq = (int) (Math.round((double) GameCourt.FPS * PAUSE_INTERVAL));
		this.counterFire = (int) (Math.round((double) GameCourt.FPS * offsetTime));
		this.firingMode = firingMode;
    }
    
	@Override
    public void advance(Blob blob) {
 		if (firingMode) {
 			if (counterFire % firingFreq == 0) {
 				this.fire(projDirection1, null);
				if (projDirection2 != null) {
					this.fire(projDirection2, null);
				}
 			}
			counterFire++;
 		} 
 		if (counterPause % pauseFreq == 0) {
 			firingMode = !firingMode;
 		}
 		counterPause++;
	}
}