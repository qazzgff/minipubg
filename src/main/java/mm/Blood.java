/**
 * This class is the basic set of blood.
 * 
 *
 */


public class Blood {
	
PlayerClient tc;
	
 public Blood (PlayerClient tc) {
		this.tc =tc;
	}
	
	/**
	 *  check the player status whether is alive.
	 */
 public boolean live = true;
	
	
	public boolean isLive() {
		return live;
	}
	
	
	public void setLive(boolean live) {
		this.live = live;
		
	}
	/**
	 * set Hp equal to 100
	 * @return life
	 * and set new current new life
	 */  
	public int life = 100;
	
	public int getLife() {
		return life;
		
	}
	public void setLife(int life) {
		this.life = life;
	}	
	
	
	
}