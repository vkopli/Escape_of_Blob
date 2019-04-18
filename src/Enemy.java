/**
 * Generic enemy in game that blob must defeat to move forward by decreasing its health 
 * by hitting with Blob's projectiles. Holds a counter that can be used to keep track
 * of how long ago Enemy has been h
 */
public abstract class Enemy extends GameObj {
    
	/* Interval in seconds at which Enemy appears hurt after being hit by Blob's projectiles */
    private static final double HURT_INTERVAL = 1.0;
    
	/* Number of frames associated with time Enemy Appears hurt and counter to keep track */
    private int numHurtFrames;
    private int counterHurt;
	
	/* Current health of the enemy */
	private int health;
    
    /**
    * Constructor
    */
    public Enemy(int velocity, int px, int py, int size, int radius, int numLives) {
        super(velocity, 0, 0, px, py, size, size, radius);
        this.health = numLives;
        this.numHurtFrames = (int) (Math.round((double) GameCourt.FPS * HURT_INTERVAL));
        this.counterHurt = numHurtFrames;
    }
	
	/**
     * Removes a unit of health from Enemy. Doesn't do anything if Enemy has been recently hit.
     * 
     * @return Whether Enemy is dead
     */
    public boolean removeHealth() {
        if (counterHurt > numHurtFrames) { 
			counterHurt = 0;
			health -= 1;
		}
        return health <= 0;
    }
	
	/**
     * Checks whether Enemy has been recently hit and increments the counter for the number of 
     * frames since Enemy has been hit.
     * 
     * Must call this method once per a frame (perhaps within draw method) for it to work 
     * accurately.
     * 
     * @return Whether enemy has recently been hit
     */
    public boolean isHurt() {
		counterHurt++;
        return counterHurt < numHurtFrames;
    }
    
	/**
     * Default method to advance enemy. Changes the enemy's velocity and has the enemy shoot 
     * projectiles depending on the the timer of the game. 
     * 
     * @param blob the main character of the game
     */
    public abstract void advance(Blob blob);
    
}