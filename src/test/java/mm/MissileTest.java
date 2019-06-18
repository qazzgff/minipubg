import static org.junit.Assert.*;

import org.junit.Test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
public class MissileTest {

	
	@Test
	public void testXspeed()
	{
		Missile missile = new Missile(100, 50, 50, Dir.D);
		int xpeed = missile.XSPEED;
		assertEquals(10, xpeed);

	}
	
	@Test
	public void testYspeed()
	{
		Missile missile = new Missile(100, 50, 50, Dir.D);
		int ypeed = missile.YSPEED;
		assertEquals(10, ypeed);

	}

	@Test
	public void testGetRect() {
		Missile missile = new Missile(100, 50, 50, Dir.D);
		Rectangle rectangle = missile.getRect();
		assertEquals(rectangle, new Rectangle(50,50,5,5));
	}

	@Test
	public void testHitPlayer() {

		Player player = new Player(5, 5);
		player.id=0;
		player.setLive(true);
		Missile missile = new Missile(100, 6, 6, Dir.D);
		assertTrue(missile.live && player.isLive() && missile.playerId != player.id && missile.getRect().intersects(player.getRect()));
		
	}

	

	@Test
	public void testHitStone() {
		Missile missile = new Missile(100, 101, 101, Dir.D);
		Stone stone1=new Stone(100, 0, 50, 50);
		Stone stone2=new Stone(100, 100, 50, 50);
		assertFalse(missile.hitStone(stone1));
		assertTrue(missile.hitStone(stone2));
	}

}
