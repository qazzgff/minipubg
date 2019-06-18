import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;



/**
 * This is a class to present explode effect when bullet hit something in the game.
 *
 */
public class Explode {
	int x, y;

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	Image[] imgs = {
			tk.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/10.gif"))	
	};
	private boolean live = true;

	private PlayerClient tc;

	int step = 0;
	
	/**
	 * Constructor
	 * @param x is the location of x 
	 * @param y is the location of y
	 * @param tc in which client that this explode happened
	 * 
	 */
	public Explode(int x, int y, PlayerClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	private static boolean init = false;
	/**
	 * Draw the current explode
	 * @param g where to graw
	 * @see java.awt.Graphics
	 */
	public void draw(Graphics g) {

		if(!init) {
			for (int i = 0; i < imgs.length; i++) {
				g.drawImage(imgs[i], -100, -100, null);
			}			
			init = true;
		}
		
		if(!live) {
			tc.explodes.remove(this);
			return;
		}
		
		if(step == imgs.length) {
			live = false;
			step = 0;
			return;
		}
		
		g.drawImage(imgs[step], x, y, null);
		
		step ++;
	}
}
