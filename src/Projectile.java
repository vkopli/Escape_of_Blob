/**
 * Generic projectile in game used to hurt the opponent (either Blob or Enemy)
 */
public abstract class Projectile extends GameObj {
    
    public static final int PROJECTILE_VELOCITY = 8;

    /**
    * Constructor
    * d1 must be some direction, but d2 can be null
    * d2 will override d1 if in the same axis (x or y)
    */
    public Projectile(int x, int y, int size, int radius, Direction d1, Direction d2) {
        super(PROJECTILE_VELOCITY, 0, 0, x, y, size, size, radius);
        switch (d1) {
        case UP:
            this.setVy(-PROJECTILE_VELOCITY);
            break;  
        case DOWN:
            this.setVy(PROJECTILE_VELOCITY);
            break;
        case LEFT:
            this.setVx(-PROJECTILE_VELOCITY);
            break;
        case RIGHT:
            this.setVx(PROJECTILE_VELOCITY);
            break;
        }
        if (d2 != null) {
            switch (d2) {
            case UP:
                this.setVy(-PROJECTILE_VELOCITY);
                break;  
            case DOWN:
                this.setVy(PROJECTILE_VELOCITY);
                break;
            case LEFT:
                this.setVx(-PROJECTILE_VELOCITY);
                break;
            case RIGHT:
                this.setVx(PROJECTILE_VELOCITY);
                break;
            }
        }
    }
}