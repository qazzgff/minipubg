import static org.junit.Assert.*;

import java.awt.event.KeyEvent;

import javax.swing.plaf.metal.MetalBorders.PaletteBorder;

import org.junit.Test;

public class PlayerTest {

	@Test
	public void testGetLife() {
		//PlayerClient playerClient=new PlayerClient();
		Player player=new Player(100, 100, Dir.U, 100, null);
		int life = player.getLife();
		assertEquals(life, 100);
		
		boolean live = player.isLive();
		assertTrue("should live", live);
		
		
		
	}

	@Test
	public void testSetLife() {
		Player player=new Player(100, 100, Dir.U, 100, null);
		int life = player.getLife();
		assertEquals(life, 100);
		player.setLife(50);
		life = player.getLife();
		assertEquals(life, 50);
		player.setLife(0);
		boolean live = player.isLive();
		assertFalse("should died", live);
		
	}

	
	@Test
	public void testIsLive() {
		Player player=new Player(100, 100, Dir.U, 100, null);
		boolean live = player.isLive();
		assertTrue("should live", live);
		
	}

	@Test
	public void testSetLive() {
		Player player=new Player(100, 100, Dir.U, 100, null);
		player.setLife(0);
		assertFalse("should died", player.isLive());
		player.setLive(true);
		assertTrue("should live", player.isLive());
	}

	/*
	@Test
	public void testEat() {
		Blood blood = new Blood();
		Player player = new Player(blood.x,blood.y);
		assertFalse("should not eat or return True",player.eat(blood));
		
		
	}
	*/
	
}
