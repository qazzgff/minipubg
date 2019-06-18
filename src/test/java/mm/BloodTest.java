import static org.junit.Assert.*;

import java.awt.Rectangle;

import org.junit.Test;

public class BloodTest
{
	
	Blood Blood = new Blood(null);
	@Test
	public void testIsLive()
	{
		Blood.live = true;
		assertEquals(true, Blood.isLive());
	}

	@Test
	public void testSetLive()
	{
		Blood.live = true;
		Blood.setLive(false);
		assertEquals(false, Blood.live);
	}

/*
	@Test
	public void testBlood()
	{
		assertEquals(700, Blood.x);
		assertEquals(100, Blood.y);
		assertEquals(50, Blood.w);
		assertEquals(50, Blood.h);
		
	}

	@Test
	public void testGetRect()
	{
		assertEquals(new Rectangle(700, 100, 50, 50), Blood.getRect());
	}
	*/
}
