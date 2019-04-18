import java.awt.Graphics;

/** 
 * An object in the game. 
 *
 * Game objects exist in the game court. They have a position, velocity, size and bounds. Their
 * velocity controls how they move; their position should always be within their bounds.
 */
public abstract class GameObj {
	
    /*
     * Current position of the object (in terms of graphics coordinates)
     *  
     * Coordinates are given by the upper-left hand corner of the object. This position should
     * always be within bounds.
     *  0 <= px <= maxX 
     *  0 <= py <= maxY 
     */
    private int px; 
    private int py;

    /* Size of object, in pixels. */
    private int width;
    private int height;
    
    /* default velocity that object moves */
    private int velocity;
    
    /* Velocity: number of pixels to move every time move() is called. */
    private int vx;
    private int vy;
        
    /* 
     * Radius of object in pixels used to calculate collisions
     */
    private int radius;
	
    /* 
     * Upper bounds of the area in which the object can be positioned. Maximum permissible x, y
     * positions for the upper-left hand corner of the object.
     */
    private int maxX;
    private int maxY;
    
    /* 
     * x and y positions for the upper-left hand corner of the object at which object is in middle of court.
     */
    private int centerX;
    private int centerY;
    
    /**
     * Constructor
     */
    public GameObj(int velocity, int vx, int vy, int px, int py, int width, int height, int radius) {
        
    	this.velocity = velocity;
        this.vx = vx;
        this.vy = vy;
        this.px = px;
        this.py = py;
        this.width  = width;
        this.height = height;
        this.radius = radius;

        // take the width and height into account when setting the bounds for the upper left corner
        // of the object.
        this.maxX = GameCourt.COURT_WIDTH - 2 * radius;
        this.maxY = GameCourt.COURT_HEIGHT - 2 * radius;
        
        // coordinates for object to be in center of court
        this.centerX = (int) Math.round(((double) (GameCourt.COURT_WIDTH - width)) / 2);
        this.centerY = (int) Math.round(((double) (GameCourt.COURT_HEIGHT - height)) / 2);
    }
	 
    /*** GETTERS **********************************************************************************/
    
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }
    
    public int getVx() {
        return this.vx;
    }
    
    public int getVy() {
        return this.vy;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }

    public int getPxCenter() {
        return this.px + radius;
    }
    
    public int getPyCenter() {
        return this.py + radius;
    }
    
    /*** SETTERS **********************************************************************************/
    public void setPx(int px) {
        this.px = px;
        clip();
    }

    public void setPy(int py) {
        this.py = py;
        clip();
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    /*** UPDATES AND OTHER METHODS ****************************************************************/

    /**
     * Prevents the object from going outside of the bounds of the area designated for the object.
     * (i.e. Object cannot go outside of the active area the user defines for it).
     */ 
    private void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }
    
    /**
     * Moves the object by its velocity.  Ensures that the object does not go outside its bounds by
     * clipping.
     */
    public void move() {
        this.px += this.vx;
        this.py += this.vy;
        clip();
    }
	       
    /**
     * Checks whether object is in middle of court
     * 
     * @return Whether object in middle of court
     */
    public boolean isAtCenter() {
    	int threshold = 1 + (int) ((double) this.velocity / 2);
        return (this.px > (this.centerX - threshold) && this.px < (this.centerX + threshold)) && 
        	   (this.py > (this.centerY - threshold) && this.py < (this.centerY + threshold));
    }
    
    /**
     * Moves the object to the center of the court
     */
    public void moveToCenter() {
        this.px = this.centerX;
        this.py = this.centerY;
    }
    
    /**
     * Changes object's velocity so object will move towards center of the court
     * 
     * @param velocity number of pixels object should move through towards center
     */
    public void aimAtCenter() {
        aimAtPoint(this.centerX, this.centerY);
    }
    
    /**
     * Changes object's velocity so that it will move towards an inputted point
     * 
     * @param x x coordinate of point to move towards
     *        y y coordinate of point to move towards
     *        velocity number of pixels object should move through towards point
     */
    public void aimAtPoint(int x, int y) {
        if (x > this.px) {
            this.vx = this.velocity;
        } else if (x < this.px) {
            this.vx = -this.velocity;
        } else {
            this.vx = 0;
        }
        if (y > this.py) {
            this.vy = this.velocity;
        } else if (y < this.py) {
            this.vy = -this.velocity;
        } else {
            this.vy = 0;
        }
    }
    
    /**
     * Moves the object to the center of the given edge of the court
     * 
     * @param d direction indicating side of the room to move to
     */
    public void moveToEdge(Direction d) {
        switch (d) {
        case UP:
            this.px = centerX;
            this.py = 0;
            break;  
        case DOWN:
            this.px = centerX;
            this.py = maxY;
            break;
        case LEFT:
            this.px = 0;
            this.py = centerY;
            break;
        case RIGHT:
            this.px = maxX;
            this.py = centerY;
            break;
        }
    }

    /**
     * Determine whether this game object is currently intersecting another object.
     * 
     * Intersection is determined by comparing bounding boxes. If the bounding boxes overlap, then
     * an intersection is considered to occur.
     * 
     * @param that The other object
     * @return Whether this object intersects the other object.
     */
    public boolean intersects(GameObj that) {
        double dx = (this.px + this.radius) - (that.px + that.radius);
        double dy = (this.py + this.radius) - (that.py + that.radius);
        double distance = Math.pow(dx * dx + dy * dy, 0.5);
        return (distance <= Math.abs(this.radius + that.radius));
    }
	
	/**
     * Determine whether this game object will intersect another in the next time step, assuming
     * that both objects continue with their current velocity.
     * 
     * Intersection is determined by comparing bounding boxes. If the  bounding boxes (for the next
     * time step) overlap, then an intersection is considered to occur.
     * 
     * @param that The other object
     * @return Whether an intersection will occur.
     */
    public boolean willIntersect(GameObj that) {
        int thisNextX = this.px + this.vx;
        int thisNextY = this.py + this.vy;
        int thatNextX = that.px + that.vx;
        int thatNextY = that.py + that.vy;
		double dx = (thisNextX + this.radius) - (thatNextX + that.radius);
        double dy = (thisNextY + this.radius) - (thatNextY + that.radius);
        double distance = Math.pow(dx * dx + dy * dy, 0.5);
        return (distance <= Math.abs(this.radius + that.radius));
    }

    /**
     * Determine whether the game object will hit a wall in the next time step. If so, return the
     * direction of the wall in relation to this game object.
     *  
     * @return Direction of impending wall, null if all clear.
     */
    public Direction hitWall() {
        if (this.px + this.vx < 0) {
            return Direction.LEFT;
        } else if (this.px + this.vx > this.maxX) {
           return Direction.RIGHT;
        }

        if (this.py + this.vy < 0) {
            return Direction.UP;
        } else if (this.py + this.vy > this.maxY) {
            return Direction.DOWN;
        } else {
            return null;
        }
    }
    
    /**
     * Determine whether the game object will hit a door in the next time step. If so, return the
     * direction of the wall in relation to this game object.
     *  
     * @return Direction of door, null if all clear.
     */
    public Direction hitDoor() {
        if (!((this.px < (centerX + 30)) && (this.px > (centerX - 30))) &&
            !((this.py < (centerY + 30)) && (this.py > (centerY - 30))))  {
            return null;
        }
        return hitWall();
    }

    /**
     * Default draw method that provides how the object should be drawn in the GUI. This method does
     * not draw anything. Subclass should override this method based on how their object should
     * appear.
     * 
     * @param g The <code>Graphics</code> context used for drawing the object. Remember graphics
     * contexts that we used in OCaml, it gives the context in which the object should be drawn (a
     * canvas, a frame, etc.)
     */
    public abstract void draw(Graphics g);
}