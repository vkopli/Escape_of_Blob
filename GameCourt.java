import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Set;
import java.util.TreeSet;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {
    
    // the state of the game logic
    private RoomHandler roomHandler;
    private Blob blob; // the main character

    private boolean moveKeyPressed = false;
    private boolean stopKeyPresses;
    private boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."
    private JButton pauseButton; // Button used to pause game
	    
    private Direction blobDirection;
	private boolean hasWon;
    private boolean hasLost;
	
	// variables for end animation
	private int counterRotate;
	private int freqRotate;
	
    // Game constants
    public static final int BORDER = 88;
    public static final int ROOM_WIDTH = 957;
    public static final int ROOM_HEIGHT = 604;
    public static final int COURT_WIDTH = ROOM_WIDTH - 2 * BORDER;
    public static final int COURT_HEIGHT = ROOM_HEIGHT - 2 * BORDER;
    public static final int BLOB_VELOCITY = 4;
	public static final double INTERVAL_ROTATE = 0.3;

    // Update interval for timer, in milliseconds
    public static final int FPS = 30;
    private static final int INTERVAL = 1000 / FPS;

    public GameCourt(JLabel status, JButton pauseButton) {

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)
        addKeyListener(new KeyAdapter() {
                       
            //set of move keys that are currently pressed
            private final Set<Integer> pressedMove = new TreeSet<Integer>();
            public void keyPressed(KeyEvent e) {
            	
            	if (stopKeyPresses) {
            		return;
            	}
            	
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT) { 
                    blob.setVx(-BLOB_VELOCITY);
                    blobDirection = Direction.LEFT;
                    moveKeyPressed = true;
                    pressedMove.add(keyCode);
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    blob.setVx(BLOB_VELOCITY);
                    blobDirection = Direction.RIGHT;
                    moveKeyPressed = true;
                    pressedMove.add(keyCode);
                } else if (keyCode == KeyEvent.VK_UP) {
                    blob.setVy(-BLOB_VELOCITY);
                    moveKeyPressed = true;
                    pressedMove.add(keyCode);
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    blob.setVy(BLOB_VELOCITY);
                    moveKeyPressed = true;
                    pressedMove.add(keyCode);
                } else if (keyCode == KeyEvent.VK_W) {
                    blob.fire(roomHandler, Direction.UP);
                } else if (keyCode == KeyEvent.VK_A) {
                    blob.fire(roomHandler, Direction.LEFT);
                } else if (keyCode == KeyEvent.VK_S) {
                    blob.fire(roomHandler, Direction.DOWN);
                } else if (keyCode == KeyEvent.VK_D) {
                    blob.fire(roomHandler, Direction.RIGHT);
                }
            }
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();              
                if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
                    pressedMove.remove(keyCode); 
                    if (keyCode == KeyEvent.VK_RIGHT && pressedMove.contains(KeyEvent.VK_LEFT)) {
                        blob.setVx(-BLOB_VELOCITY);
                        blobDirection = Direction.LEFT;
                    } else if (keyCode == KeyEvent.VK_LEFT && 
                               pressedMove.contains(KeyEvent.VK_RIGHT)) {
                        blob.setVx(BLOB_VELOCITY);
                        blobDirection = Direction.RIGHT;
                    } else {
                        blob.setVx(0);
                    }
                } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_UP) {
                    pressedMove.remove(keyCode); 
                    if (keyCode == KeyEvent.VK_DOWN && pressedMove.contains(KeyEvent.VK_UP)) {
                        blob.setVy(-BLOB_VELOCITY);
                    } else if (keyCode == KeyEvent.VK_UP && 
                               pressedMove.contains(KeyEvent.VK_DOWN)) {
                        blob.setVy(BLOB_VELOCITY);
                    } else {
                        blob.setVy(0);
                    }
                } else if (keyCode == KeyEvent.VK_P) {
                    togglePause();
                }
                if (pressedMove.isEmpty()) {
                    moveKeyPressed = false;
                }
            }
            public void keyTyped(KeyEvent e) {}
        });
        this.status = status;
        this.pauseButton = pauseButton;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        
        roomHandler = new RoomHandler();        
        
        // make blob and health bar and move blob to center of court
        blob = new Blob(BLOB_VELOCITY);
        blob.moveToCenter();
		blobDirection = Direction.RIGHT;
        
		hasWon = false;
		hasLost = false;
		stopKeyPresses = false;
        playing = true;
        status.setText("Playing");

		// variables for end animation
		counterRotate = 0;
		freqRotate = (int) (Math.round((double) FPS * INTERVAL_ROTATE));
		
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
            
            // advance blob and enemies and projectiles in room
            hasWon = roomHandler.advance(blob);
			hasLost = blob.isDead();

            // check for the game end conditions
            if (hasLost) {
                status.setText("GAME OVER. Press Reset button to try again.");
                playing = false;
            } else if (hasWon) {
				if (!blob.isAtCenter()) {
					blob.aimAtCenter();
                } else {
					status.setText("CONGRATULATIONS! You made it to the portal!");
					blob.setVx(0);
					blob.setVy(0);
					if (counterRotate > (3 * FPS)) {
						blob.toggleTransparency();
						playing = false;
					} else {
						stopKeyPresses = true;
						if ((counterRotate % freqRotate) == 0) {
							blob.rotate();
						}
						counterRotate++;
					}
				}
			}
            // update the display
            repaint();
        }
    }

	/**
     * Toggle the playing state of the game. Keeps pause button updated with state.
     */
    public void togglePause() {
		if (!hasWon && !hasLost) {
			if (playing) {
				playing = false;
				status.setText("Paused");
				pauseButton.setText("Unpause");
			} else {
				playing = true;
				status.setText("Playing");
				pauseButton.setText("Pause");
			}
			requestFocusInWindow();
		}
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
               
        // draws room, then translates graphics context to upper left corner of game court
        // to draw enemies and boulders
        roomHandler.draw(g);
        
        // draw blob and healthbar
        blob.draw(g, moveKeyPressed, blobDirection, COURT_WIDTH, BORDER);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(ROOM_WIDTH, ROOM_HEIGHT);
    }
}