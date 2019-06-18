import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/**
 * A class represent a player(or AI) in the game
 * 
 *
 */
public class Player {
	int id;
	

	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;

	boolean good;
	boolean robot;

	int x, y;
	int oldx,oldy;
	private static Random r = new Random();
	private boolean live = true;
	private int step = r.nextInt(12) + 3;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] playerImages = null;
	private static Map<String, Image> imgs = new HashMap<String, Image>();
	static {
		playerImages = new Image[] {
				tk.getImage(Player.class.getClassLoader().getResource("images/playerL.gif")),
				tk.getImage(Player.class.getClassLoader().getResource("images/playerLU.gif")),
				tk.getImage(Player.class.getClassLoader().getResource("images/playerU.gif")),
				tk.getImage(Player.class.getClassLoader().getResource("images/playerRU.gif")),
				tk.getImage(Player.class.getClassLoader().getResource("images/playerR.gif")),
				tk.getImage(Player.class.getClassLoader().getResource("images/playerRD.gif")),
				tk.getImage(Player.class.getClassLoader().getResource("images/playerD.gif")),
				tk.getImage(Player.class.getClassLoader().getResource("images/playerLD.gif"))
		};
		
		imgs.put("L", playerImages[0]);
		imgs.put("LU", playerImages[1]);
		imgs.put("U", playerImages[2]);
		imgs.put("RU", playerImages[3]);
		imgs.put("R", playerImages[4]);
		imgs.put("RD", playerImages[5]);
		imgs.put("D", playerImages[6]);
		imgs.put("LD", playerImages[7]);
		
	}


	PlayerClient tc;

	boolean bL, bU, bR, bD;

	Dir dir = Dir.STOP;
	Dir ptDir = Dir.D;
	
	/**
	 * Constructor for a new player character in the game
	 * @param x location of x
	 * @param y location of y
	 */

	public Player(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 * @param x location of x
	 * @param y location of y
	 * @param dir direction that the player initially towards
	 * @param tc client
	 */
	public Player(int x, int y, Dir dir, int life, PlayerClient tc) {
		this(x, y);
		this.dir = dir;
		this.tc = tc;
		this.life = life;
	}
	/**
	 * 
	 * @param x location of x
	 * @param y location of y
	 * @param dir direction that the player initially towards
	 * @param tc client
	 * @param robot whether is a robot
	 */
	public Player(int x, int y, Dir dir,int life,boolean robot, PlayerClient tc) {
		this(x, y, dir, life, tc);
		this.robot = robot;
		if(robot) {
			this.setLife(30);
		}
	}
	
	/**
	 * draw the player in the game, depends on location and direction
	 * @param g where to draw
	 */
	public void draw(Graphics g) {
		
		if (!live) {
			tc.players.remove(this);
		
			if (!good) {
				tc.players.remove(this);
			}
			return;
		}


		switch (ptDir) {
		
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);
			break;
		}

		move();
	}
	/**
	 * the player move for some direction. If its robot, it will chase player or go randomly
	 */
	private void move() {
		if (hitStone()) {
			x=oldx;
		     y=oldy;
			return;}
		oldx = x;
		oldy = y;
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

		if (dir != Dir.STOP) {
			ptDir = dir;
		}

		if (x < 0)
			x = 0;
		if (y < 30)
			y = 30;
		if (x + WIDTH > PlayerClient.GAME_WIDTH)
			x = PlayerClient.GAME_WIDTH - WIDTH;
		if (y + HEIGHT > PlayerClient.GAME_HEIGHT)
			y = PlayerClient.GAME_HEIGHT - HEIGHT;
		
		if(robot) {
			Dir[] dirs = Dir.values();
			Random ra = new Random();
			int i = ra.nextInt(100);
			int j = ra.nextInt(100);
			int change1 = 20;
			int change2 = 10;
			if(i > change1) {
				if(step == 0) {
					step = r.nextInt(12)+3;
					int rn = r.nextInt(dirs.length);
					this.dir = dirs[rn];
				}
				step--;
				if(r.nextInt(40)>35) {
					this.fire();
				}
			} else {
				if(this.x == tc.myPlayer.x) {
					if(this.y < tc.myPlayer.y) {
						this.dir = dirs[6];
						if(j < change2) {
							this.fire();
						}
					} else {
						this.dir = dirs[2];
						if(j < change2) {
							this.fire();
						}
					}
				} else if(this.y == tc.myPlayer.y) {
					if(this.x < tc.myPlayer.x) {
						this.dir = dirs[4];
						if(j < change2) {
							this.fire();
						}
					} else {
						this.dir = dirs[0];
						if(j < change2) {
							this.fire();
						}
					}
				} else {
					if(this.x < tc.myPlayer.x && this.y < tc.myPlayer.y) {
						float tan = (float) (tc.myPlayer.y-this.y)/(tc.myPlayer.x-this.x);
						if(tan <= 0.4) {
							this.dir = dirs[4];
							if(j < change2) {
								this.fire();
							}
						} else if(tan > 2.4) {
							this.dir = dirs[6];
							if(j < change2) {
								this.fire();
							}
						} else {
							this.dir = dirs[5];
							if(j < change2) {
								this.fire();
							}
						}
					} else if(this.x > tc.myPlayer.x && this.y < tc.myPlayer.y) {
						float tan = (float) (tc.myPlayer.y-this.y)/(this.x-tc.myPlayer.x);
						if(tan <= 0.4) {
							this.dir = dirs[0];
							if(j < change2) {
								this.fire();
							}
						} else if(tan > 2.4) {
							this.dir = dirs[6];
							if(j < change2) {
								this.fire();
							}
						} else {
							this.dir = dirs[7];
							if(j < change2) {
								this.fire();
							}
						}
					} else if(this.x > tc.myPlayer.x && this.y > tc.myPlayer.y) {
						float tan = (float) (this.y-tc.myPlayer.y)/(this.x-tc.myPlayer.x);
						if(tan <= 0.4) {
							this.dir = dirs[0];
							if(j < change2) {
								this.fire();
							}
						} else if(tan > 2.4) {
							this.dir = dirs[2];
							if(j < change2) {
								this.fire();
							}
						} else {
							this.dir = dirs[1];
							if(j < change2) {
								this.fire();
							}
						}
					} else {
						float tan = (float) (this.y-tc.myPlayer.y)/(tc.myPlayer.x-this.x);
						if(tan <= 0.4) {
							this.dir = dirs[4];
							if(j < change2) {
								this.fire();
							}
						} else if(tan > 2.4) {
							this.dir = dirs[2];
							if(j < change2) {
								this.fire();
							}
						} else {
							this.dir = dirs[3];
							if(j < change2) {
								this.fire();
							}
						}
					}
				}
			}
			
		}
	}
	
	/**
	 * draw HP bar 
	 * @param g where to draw
	 */
	public void drawbloodbar(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.drawRect(x, y-10, WIDTH, 5);
		int w = WIDTH * life/100 ;
		g.fillRect(x, y-10, w, 5);
		g.setColor(c);
	}

	/**
	 * Parse key pressing actions
	 * @param e key event
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
//		case KeyEvent.VK_SPACE:
//			fire();
//			Audio2.playShot();
//			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
//		case KeyEvent.VK_R:
//			hp ();
//			Audio2.playEat();
//			break;
		}
		locateDirection();
	}

	/**
	 * Parse key actions(press or release) and change direction
	 */
	private void locateDirection() {
		Dir oldDir = this.dir;

		if (bL && !bU && !bR && !bD)
			dir = Dir.L;
		else if (bL && bU && !bR && !bD)
			dir = Dir.LU;
		else if (!bL && bU && !bR && !bD)
			dir = Dir.U;
		else if (!bL && bU && bR && !bD)
			dir = Dir.RU;
		else if (!bL && !bU && bR && !bD)
			dir = Dir.R;
		else if (!bL && !bU && bR && bD)
			dir = Dir.RD;
		else if (!bL && !bU && !bR && bD)
			dir = Dir.D;
		else if (bL && !bU && !bR && bD)
			dir = Dir.LD;
		else if (!bL && !bU && !bR && !bD)
			dir = Dir.STOP;

		if (dir != oldDir) {
			PlayerMoveMsg msg = new PlayerMoveMsg(id, x, y, dir, ptDir, life, robot);
			tc.nc.send(msg);
		}
	}
	
	/**
	 * Parse key releasing actions
	 * @param e key event
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_SPACE:
			fire();
			Audio2.playShot();
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:

			bU = false;
			break;
		case KeyEvent.VK_RIGHT:

			bR = false;
			break;
		case KeyEvent.VK_DOWN:

			bD = false;
			break;
		case KeyEvent.VK_R:
			hp ();
			Audio2.playEat();
			break;
		}
		locateDirection();
	}

	/**
	 * a help function when user try to fire
	 * @return missile m
	 */
	private Missile fire() {
		if (!live) {
			return null;
		}
		int x = this.x + WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(id, x, y, this.ptDir, this.robot, this.tc);
		tc.missiles.add(m);

		MissileNewMsg msg = new MissileNewMsg(m);
		tc.nc.send(msg);

		return m;
	}
	
	/**
	 * get current area of player
	 * @return current area(x,y,w,h)
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	
   public boolean isLive() {
		return live;
	}
	
	
	public void setLive(boolean live) {
		this.live = live;
		
	}
	public int getLife() {
		return life;
		
	}
	public void setLife(int life) {
		this.life = life;
	}	
	
	public int life =100;
	
	/**
	 * add hp here, every player have one chance to add hp back to 100; 
	 * count the number of blood use, if used minus 1.
	 * 
	 */
	public String hp(){
	    	 
	    	if(!live||this.life==100||HpCount.count==0) {
	    		return null;
	    		
	    	}
	    	
	         this.life = 100;
	    	
	    	 HpCount.count--;
	    	 
	    	 return null;
	    }

	    	
	    
	    
	     
     
      
	/**
	 * check if any obstacle out of the way 
	 * @return is or not
	 */
	public boolean hitStone(){
		if(this.live && this.getRect().intersects(PlayerClient.s1.getRect())){
			return true;
		}
		else if(this.live && this.getRect().intersects(PlayerClient.s2.getRect())){
			return true;
		}
		else if(this.live && this.getRect().intersects(PlayerClient.s3.getRect())){
			return true;
		}
		else if(this.live && this.getRect().intersects(PlayerClient.s4.getRect())){
			return true;
		}
		else if(this.live && this.getRect().intersects(PlayerClient.s5.getRect())){
			return true;
		}
		else if(this.live && this.getRect().intersects(PlayerClient.s6.getRect())){
			return true;
		}
		else if(this.live && this.getRect().intersects(PlayerClient.s7.getRect())){
			return true;
		}
		else if(this.live && this.getRect().intersects(PlayerClient.s8.getRect())){
			return true;
		}
		else {
		return false;
		}
	}
}
