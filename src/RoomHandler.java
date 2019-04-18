import java.awt.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * Handles all game objects in between ticks, except for drawing the main character.
 * Holds all room and level information
 */
public class RoomHandler {
        
	/* 2D array of rooms representative of spatial location */
    private static Room[][] rooms = new Room[3][];
    
	/* Current room and location of room in rooms array */
    private Room room;
    private int i = 0;
    private int j = 1;

	private boolean hasWon = false;
    
    /**
    * Constructor
    */
    public RoomHandler() {
        
		int courtWidth = GameCourt.COURT_WIDTH;
		int courtHeight = GameCourt.COURT_HEIGHT;
		
        // create rooms
        Room startRoom = new Room("files/Bkgrnd3Lower.png", "files/Bkgrnd3Lower.png");
        Room centerRoom = new Room("files/Bkgrnd2LeftTop.png", "files/Bkgrnd2LeftTopClosed.png");
        Room trickRoom = new Room("files/Bkgrnd1Left.png", "files/Bkgrnd1LeftClosed.png");
        Room centerLeftRoom = new Room("files/Bkgrnd3Right.png", "files/Bkgrnd3RightClosed.png");
		Room upperLeftRoom = new Room("files/Bkgrnd2RightLower.png", 
									  "files/Bkgrnd2RightLowerClosed.png");
        
        // create final room
        Room finalRoom = new FinalRoom("files/Bkgrnd1Top.png", "files/Bkgrnd1TopClosed.png");
        
        rooms[0] = new Room[] {upperLeftRoom, startRoom, trickRoom};
        rooms[1] = new Room[] {centerLeftRoom, centerRoom};
        rooms[2] = new Room[] {finalRoom}; 

        this.room = rooms[i][j];
        
        // Add enemies and boulders to rooms
        
        // Upper Left Room
        upperLeftRoom.addEnemy(new PatrollingEnemy(7, 0, Direction.RIGHT));
		upperLeftRoom.addBoulder(new Boulder (300, 150));
		
		// Upper Right Room
		Enemy rightWallEnemy = new SprinklerEnemy(0, 0, Direction.LEFT);
		rightWallEnemy.moveToEdge(Direction.RIGHT);
		trickRoom.addEnemy(rightWallEnemy);
		Enemy topWallEnemy = new SprinklerEnemy(0, 0, Direction.DOWN);
		topWallEnemy.moveToEdge(Direction.UP);
		trickRoom.addEnemy(topWallEnemy);
		Enemy lowerWallEnemy = new SprinklerEnemy(0, 0, Direction.UP);
		lowerWallEnemy.moveToEdge(Direction.DOWN);
		trickRoom.addEnemy(lowerWallEnemy);
		
		// Center Room
		centerRoom.addEnemy(new SnipingEnemy(7, courtHeight - RegularEnemy.SIZE - 7,
				 Direction.RIGHT, null, 0.5, true));
		centerRoom.addBoulder(new Boulder(10, 270));
		centerRoom.addEnemy(new SnipingEnemy(courtWidth - 67, 7, Direction.DOWN, null, 0.7, 
				false));
		centerRoom.addBoulder(new Boulder(courtWidth - 165, 7));
		centerRoom.addEnemy(new SnipingEnemy(450, courtHeight - RegularEnemy.SIZE - 7, 
				Direction.UP, null, 0.4, true));
		RegularEnemy opposingEnemy = new SnipingEnemy(195, 7, Direction.LEFT, Direction.DOWN, 
				0.2, false);
		opposingEnemy.setDirectionLeft();
		centerRoom.addEnemy(opposingEnemy);
		centerRoom.addBoulder(new Boulder(262, 0));
		
		// Center Left Room
		Enemy followingEnemy1 = new FollowingEnemy(0, 0);
		Enemy followingEnemy2 = new FollowingEnemy(0, 0);
		Enemy followingEnemy3 = new FollowingEnemy(240, 260);
		followingEnemy1.moveToEdge(Direction.LEFT);
		followingEnemy2.moveToEdge(Direction.DOWN);
		centerLeftRoom.addEnemy(followingEnemy1);
		centerLeftRoom.addEnemy(followingEnemy2);
		centerLeftRoom.addEnemy(followingEnemy3);
    }

	/**
     * Draw method for RoomHandler
     * Draws room and all contents, then drows all projectiles held in RoomHandler
     * 
     * @param g The <code>Graphics</code> context used for drawing the object
     */
    public void draw(Graphics g) {
        room.draw(g);
    }
    
	/**
     * Advances everything in game
     * Changes rooms if blob moves through door 
     * 
     * @param blob the main character of the game
     */
    public boolean advance(Blob blob) {
        
        // return whether game has been won (in final room and hit the portal and defeated boss)
        if (room.getClass().equals(FinalRoom.class)) {
            FinalRoom finalRoom = (FinalRoom) room;
			boolean enemiesDefeated = finalRoom.enemiesDefeated();
			boolean hitPortal = finalRoom.intersectsPortal(blob);
			if (enemiesDefeated) {
				if (finalRoom.isPortalActivated() && hitPortal) {
					hasWon = true;
				} else if (!finalRoom.isPortalActivated()){
					blob.moveToEdge(Direction.RIGHT);
					finalRoom.activatePortal();
					Room.clearProjectiles();
					
				}
			} else {
				if (finalRoom.isPortalActivated() && hitPortal) {
					finalRoom.activateBoss();
					finalRoom.deactivatePortal();
				}
			}
		}
    	// change room and clear projectiles if blob moves into door
        changeRoom(blob);
		
		// advance everything in the room with reference to the projectiles and each other
		room.advance(blob);
        
		// move blob
        blob.move();    
		return hasWon;
    }
	
	/**
     * Adds projectile to game
     * 
     * @param projectile to be added to game
     */
    public void addProjectile(Projectile p) {
        Room.addProjectile(p);
    }
    
	/**
     * Change room if blob hits a door
     * 
     * @param blob the main character of the game
     */
    private void changeRoom(Blob blob) {
        
        Direction d = blob.hitDoor();
        if (d == null || !room.isOpen() || hasWon) {
            return;
        }
        Room.clearProjectiles();
        switch (d) {
        case UP:
            if ((i - 1) >= 0) {
                i--;
                blob.moveToEdge(Direction.DOWN);
            }
            break;  
        case DOWN:
            if ((i + 1) < rooms.length) {
                i++;
                blob.moveToEdge(Direction.UP);
            }
            break;
        case LEFT:
            if ((j - 1) >= 0) {
                j--;
                blob.moveToEdge(Direction.RIGHT);
            }
            break;
        case RIGHT:
            if ((j + 1) < rooms[i].length) {
                j++;
                blob.moveToEdge(Direction.LEFT);
            }
            break;
        }
        room = rooms[i][j];
    }
    
}
