import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

/**
 * Missile class in the game
 * 
 */
public class Missile {
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	public static final int WIDTH = 5;
	public static final int HEIGHT = 5;

	private static int ID = 1;

	PlayerClient tc;

	int playerId;

	int id;

	int x, y;

	Dir dir = Dir.R;

	boolean live = true;

	boolean robot;
	
	/**
	 * Constructor
	 * @param playerId id is used to identify who shoot this missile
	 * @param x is the location of x 
	 * @param y is the location of y 
	 * @param Dir in which direction that this missile shoots
	 * 
	 */
	
	public Missile(int playerId, int x, int y, Dir dir) {
		this.playerId = playerId;
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.id = ID++;
	}
	
	/**
	 * Constructor
	 * @param playerId id is used to identify who shoot this missile
	 * @param x is the location of x 
	 * @param y is the location of y 
	 * @param Dir in which direction that this missile shoots
	 * @param tc in which client that shoots
	 */
	public Missile(int playerId, int x, int y, Dir dir,
			PlayerClient tc) {
		this(playerId, x, y, dir);
		this.tc = tc;
	}
	
	/**
	 * Constructor
	 * @param playerId id is used to identify who shoot this missile
	 * @param x is the location of x 
	 * @param y is the location of y 
	 * @param Dir in which direction that this missile shoots
	 * @param robot if the missile is shot by robot
	 * @param tc in which client that shoots
	 */
	
	public Missile(int playerId, int x, int y, Dir dir, boolean robot, PlayerClient tc) {
		this(playerId, x, y, dir, tc);
		this.robot = robot;
	}
	
	/**
	 * draw missile in game. If player, it's yellow, else, blue
	 * @param g where to draw
	 */
	public void draw(Graphics g) {
		if (!live) {
			tc.missiles.remove(this);
			return;
		}

		Color c = g.getColor();
		if(robot) {
			g.setColor(Color.YELLOW);
		} else {
			g.setColor(Color.YELLOW);
		}
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);

		move();
	}
	
	/**
	 * move missile in one direction
	 * 
	 */
	private void move() {
		switch (dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}

		if (x < 0 || y < 0 || x > PlayerClient.GAME_WIDTH
				|| y > PlayerClient.GAME_HEIGHT) {
			live = false;
		}
	}

	/**
	 * Return the rectangle location of current missile 
	 * 
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	/**
	 * to judge if it hit any player. If so, it will decrease HP and create explode effect
	 * @param t the player t that to be judged
	 * @return judge whether hit player t
	 */
	public boolean hitPlayer(Player t) {

		if(this.live && t.isLive() && this.playerId != t.id && this.getRect().intersects(t.getRect())) {
			t.setLife(t.getLife()-10);
			this.live = false;
			if (t.getLife()<=0) {
				t.setLive(false);
			}
			tc.explodes.add(new Explode(x, y, tc));
			return true;
		}
		if(this.live && t.isLive() && this.robot != t.robot && this.getRect().intersects(t.getRect())) {
			t.setLife(t.getLife()-10);
			this.live = false;
			if (t.getLife()<=0) {
				t.setLive(false);
			}
			tc.explodes.add(new Explode(x, y, tc));
			return true;
		}
		return false;
	}
	
	/**
	 * for each player in the list, to judge if it hit any player. If so, it will decrease its HP and create explode effect
	 * @param players the player list  that to be judged
	 * @return judge whether hit any player t
	 */
	public boolean hitPlayers(List<Player> players) {
		for (int i = 0; i < players.size(); i++) {
			if (this.hitPlayer(players.get(i))) {
				return true;
			}
		}
		return false;
	}
	/**
	 * to judge if it hit any obstacle. If so, it will create explode effect
	 * @param s the obstacle s that to be judged
	 * @return judge whether hit or not
	 */
	public boolean hitStone(Stone s){
		if(this.live && this.getRect().intersects(s.getRect())){
			this.live = false;
			return true;
		}
		return false;
	}
}
