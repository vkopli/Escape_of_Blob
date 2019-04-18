/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.BadLocationException;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.
        
        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("The Escape of Blob");
		
        // Status panel
        final JPanel statusPanel = new JPanel();
        frame.add(statusPanel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        statusPanel.add(status);
        
        // Pause button
        final JButton pauseButton = new JButton("Pause");
        
        // Main playing area
        final GameCourt court = new GameCourt(status, pauseButton);
        frame.add(court, BorderLayout.CENTER);
                
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.togglePause();  
            }
        });
        
        // Control panel
        final JPanel gameControlPanel = new JPanel();
        frame.add(gameControlPanel, BorderLayout.NORTH);
        
        // Reset button
        final JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        gameControlPanel.add(resetButton);
              
        // Instructions panel
        DefaultStyledDocument document = new DefaultStyledDocument();
        JTextPane instructions = new JTextPane(document);
        StyleContext context = new StyleContext();
        Style style = context.addStyle("test", null);
		instructions.setFont(new Font("sansserif", Font.PLAIN, 20));
        String text = "THE BLOB NEEDS TO GET OUT\n\nBlake the blob monster doesn't know where it" +
            " is. All it knows is that it is in a cold, damp, and scary place with strange" +
            " noises echoing from the walls. Help Blake use its goo generation powers to" +
            " navigate the labyrinthe and find the portal to escape.\n\nINSTRUCTIONS\n\nHold" +
            " down arrow keys to to move\nTap the WASD keys to shoot goo in all 4 directions\n" +
            "Press P to pause\n\nYou have 4 health points displayed on the upper right corner of" +
            " the screen. You lose a half heart when you get shot and a full heart when you" +
            " touch an enemy. When you run out of health, you lose. Enter the portal, and you" +
			" win. You can't leave a room unless you have defeated all of the enemies inside." +
			"\n\nTIPS\n\nThe main character will change direction smoothly when multiple arrows" +
			" keys are held down simultaneously (e.g. if you hold down the left arrow key," + 
			" then additionally hold down the right arrow key, then release the right arrow key," +
			" Blake will go left, then right, then left again). Get a feel for the controls" +
			" before leaving the starting room (Blake's safe place), and try to fire rapidly!";
        try {
            document.insertString(0, text, style);
        } catch (BadLocationException e) {
            System.out.println("Instructions Document Error:" + e.getMessage());;
        }
        
        // Instructions control panel
        final JPanel instructionsControlPanel = new JPanel();
        
        // Instructions open button
        final JButton instructionsButton = new JButton("Instructions");
        instructionsButton.addActionListener(new ActionListener() {            
            public void actionPerformed(ActionEvent e) {
                court.togglePause();
                frame.remove(court);
                frame.revalidate();
                frame.remove(gameControlPanel);
                frame.revalidate();
                frame.add(instructions, BorderLayout.CENTER);
                frame.revalidate();
                frame.add(instructionsControlPanel, BorderLayout.NORTH);
                frame.revalidate();
            }
        });
        gameControlPanel.add(instructionsButton);
        gameControlPanel.add(pauseButton);
        
        // Instructions close button
        final JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {            
            public void actionPerformed(ActionEvent e) {
                frame.remove(instructions);
                frame.revalidate();
                frame.remove(instructionsControlPanel);
                frame.revalidate();
                frame.add(court, BorderLayout.CENTER);
                frame.revalidate();
                frame.add(gameControlPanel, BorderLayout.NORTH);
                frame.revalidate();
                court.togglePause();
            }
        });
        instructionsControlPanel.add(closeButton);
        
        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Start game
        court.reset();
    }
       
    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game
     * and runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
    
}