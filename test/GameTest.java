import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

/** Put your OWN test cases in this file, for all classes in the assignment. */
public class GameTest {

	// ROOM TESTS 

	@Test
	public void testRoomAddNoEnemiesOpen() {
		Room room = new Room("Bkgrnd1Left", "Bkgrnd1LeftClosed.png");
		assertTrue(room.isOpen());
	}

	@Test
	public void testRoomSingleEnemyClosed() {
		Room room = new Room("Bkgrnd1Left", "Bkgrnd1LeftClosed.png");
		room.addEnemy(new PatrollingEnemy(0, 0, Direction.RIGHT));
		assertFalse(room.isOpen());
	}

	@Test
	public void testRoomMultipleEnemiesClosed() {
		Room room = new Room("Bkgrnd1Left", "Bkgrnd1LeftClosed.png");
		room.addEnemy(new PatrollingEnemy(0, 0, Direction.RIGHT));
		room.addEnemy(new PatrollingEnemy(1, 1, Direction.LEFT));
		assertFalse(room.isOpen());
	}

	@Test
	public void testRoomAddThenRemoveSingleEnemyOpen() {
		Room room = new Room("Bkgrnd1Left", "Bkgrnd1LeftClosed.png");
		room.addEnemy(new PatrollingEnemy(0, 0, Direction.RIGHT));
		assertFalse(room.isOpen());
		room.removeEnemy();
		assertTrue(room.isOpen());
	}
	
	@Test
	public void testRoomAddThenRemoveEnemiesOpen() {
		Room room = new Room("Bkgrnd1Left", "Bkgrnd1LeftClosed.png");
		room.addEnemy(new PatrollingEnemy(0, 0, Direction.RIGHT));
		room.addEnemy(new PatrollingEnemy(0, 0, Direction.RIGHT));
		assertFalse(room.isOpen());
		room.removeEnemy();
		room.removeEnemy();
		assertTrue(room.isOpen());
	}

	@Test
	public void testRoomAddThenRemoveEnemiesClosed() {
		Room room = new Room("Bkgrnd1Left", "Bkgrnd1LeftClosed.png");
		room.addEnemy(new PatrollingEnemy(0, 0, Direction.RIGHT));
		room.addEnemy(new PatrollingEnemy(0, 0, Direction.RIGHT));
		assertFalse(room.isOpen());
		room.removeEnemy();
		assertFalse(room.isOpen());
	}

	@Test
	public void testRoomAddThenRemoveDifferentEnemiesOpen() {
		Room room = new Room("Bkgrnd1Left", "Bkgrnd1LeftClosed.png");
		room.addEnemy(new PatrollingEnemy(0, 0, Direction.RIGHT));
		room.addEnemy(new Boss(1, 1));
		assertFalse(room.isOpen());
		room.removeEnemy();
		room.removeEnemy();
		assertTrue(room.isOpen());
	}

	@Test
	public void testRoomAddThenRemoveDifferentEnemiesClosed() {
		Room room = new Room("Bkgrnd1Left", "Bkgrnd1LeftClosed.png");
		room.addEnemy(new PatrollingEnemy(0, 0, Direction.RIGHT));
		room.addEnemy(new Boss(1, 1));
		assertFalse(room.isOpen());
		room.removeEnemy();
		assertFalse(room.isOpen());
	}

	// HEALTHBAR TESTS

	@Test
	public void testHealthBarRemoveFullHeartNotDead() {
		HealthBar healthBar = new HealthBar(3);
		assertFalse(healthBar.removeFullHeart());
		assertFalse(healthBar.removeFullHeart());
	}

	@Test
	public void testHealthBarRemoveFullHeartIsDead() {
		HealthBar healthBar = new HealthBar(2);
		healthBar.removeFullHeart();
		assertTrue(healthBar.removeFullHeart());
	}

	@Test
	public void testHealthBarRemoveHalfHeartNotDead() {
		HealthBar healthBar = new HealthBar(2);
		assertFalse(healthBar.removeHalfHeart());
		assertFalse(healthBar.removeHalfHeart());
		assertFalse(healthBar.removeHalfHeart());
	}

	@Test
	public void testHealthBarRemoveHalfHeartIsDead() {
		HealthBar healthBar = new HealthBar(1);
		assertFalse(healthBar.removeHalfHeart());
		assertTrue(healthBar.removeHalfHeart());
	}

	@Test
	public void testHealthBarRemoveHalfHeartAlreadyDead() {
		HealthBar healthBar = new HealthBar(1);
		assertFalse(healthBar.removeHalfHeart());
		assertTrue(healthBar.removeHalfHeart());
		assertTrue(healthBar.removeHalfHeart());
	}

	@Test
	public void testHealthBarRemoveFullHeartAlreadyDead() {
		HealthBar healthBar = new HealthBar(2);
		assertFalse(healthBar.removeFullHeart());
		assertTrue(healthBar.removeFullHeart());
		assertTrue(healthBar.removeFullHeart());
	}
	
}