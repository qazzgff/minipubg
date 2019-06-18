import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerClient extends Frame {
	public static final int GAME_WIDTH = 1000;
	public static final int GAME_HEIGHT = 700;
	boolean music = false;
	boolean multi = true;
	private static Toolkit tk = Toolkit.getDefaultToolkit();

	static Stone s1 = new Stone(95, 240, 85, 50);
	static Stone s2 = new Stone(750, 280, 120, 160);
	static Stone s3 = new Stone(240, 20, 120, 100);
	static Stone s4 = new Stone(535, 560, 46, 70);
	static Stone s5 = new Stone(270, 470, 50, 80);
	static Stone s6 = new Stone(420, 530, 60, 70);
	static Stone s7 = new Stone(620, 410, 110, 50);
	static Stone s8 = new Stone(620, 610, 60, 60);
    public int life =100;
	int ai = 5;
	boolean start = false;
	Random r = new Random();
	Player myPlayer = new Player(r.nextInt(GAME_WIDTH), r.nextInt(GAME_HEIGHT), Dir.STOP, life, false, this);

	HpCount hpc = new HpCount();

	List<Missile> missiles = new ArrayList<Missile>();

	List<Explode> explodes = new ArrayList<Explode>();

	List<Player> players = new ArrayList<Player>();

	Image offScreenImage = null;

	NetClient nc = new NetClient(this);

	ConnDialog dialog = new ConnDialog();
	int count = 0;
	boolean isstart = false;
	
	/**
	 * Draw the graphic in the game
	 */
	@Override
	public void paint(Graphics g) {
		g.drawImage(tk.getImage(Player.class.getClassLoader().getResource("images/ground.jpg")), 0, 0, null);
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitPlayers(players);

			if (m.hitPlayer(myPlayer)) {
				
				if (myPlayer.getLife() <= 0) {

					PlayerDeadMsg msg = new PlayerDeadMsg(myPlayer.id);
					nc.send(msg);
				}
				MissileDeadMsg mdmMsg = new MissileDeadMsg(m.playerId, m.id);
				nc.send(mdmMsg);
			}
			if (m.hitStone(s1) || m.hitStone(s2) || m.hitStone(s3) || m.hitStone(s4) || m.hitStone(s5) || m.hitStone(s6)
					|| m.hitStone(s7) || m.hitStone(s8)) {
				MissileDeadMsg mdmMsg = new MissileDeadMsg(m.playerId, m.id);
				nc.send(mdmMsg);
			}

			m.draw(g);
		}

		for (int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}

		for (int i = 0; i < players.size(); i++) {
			Player t = players.get(i);
			t.draw(g);
		}


		myPlayer.draw(g);

		g.drawImage(tk.getImage(Player.class.getClassLoader().getResource("images/tree.gif")), 100, 100, null);
		g.drawImage(tk.getImage(Player.class.getClassLoader().getResource("images/tree.gif")), 300, 150, null);
		g.drawImage(tk.getImage(Player.class.getClassLoader().getResource("images/tree.gif")), 250, 400, null);
		g.drawImage(tk.getImage(Player.class.getClassLoader().getResource("images/tree.gif")), 700, 500, null);
		g.drawImage(tk.getImage(Player.class.getClassLoader().getResource("images/tree.gif")), 400, 150, null);
		g.drawImage(tk.getImage(Player.class.getClassLoader().getResource("images/tree.gif")), 350, 600, null);
		g.setFont(new Font("Verdana", Font.BOLD, 16));
		g.setColor(new Color(255,255,255));
		g.drawString("Life:" + myPlayer.getLife() + " / 100", 20, 50);
		g.drawString("Players count:" + players.size(), 20, 75);
		g.drawString("Medical Package Left:" + HpCount.count, 20, 100);
		

		if (players.size() >= 1 && isstart == false) {
			isstart = true;
		}

		if (myPlayer.getLife() > 0) {
			myPlayer.drawbloodbar(g);

			if (players.size() == 0 && isstart == true) {
				g.drawImage(tk.getImage(Player.class.getClassLoader().getResource("images/win.jpg")), 0, 0, null);

			}

		} else {
			g.drawImage(tk.getImage(Player.class.getClassLoader().getResource("images/die.jpg")), 0, 0, null);
			
		}


	}

	/**
	 * Update the frame
	 */
	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		if (myPlayer.getLife() > 0) {
			gOffScreen.setColor(new Color(111, 162, 57));
		} else {
			gOffScreen.setColor(Color.LIGHT_GRAY);
		}

		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
/**
 * set the running argument before game launch
 */
	public void launchFrame() {

		this.setLocation(400, 300);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("MireMare");

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				PlayerDeadMsg msg = new PlayerDeadMsg(myPlayer.id);
				nc.send(msg);
				
				System.exit(0);
			}
		});
		this.setResizable(false);
		

		this.addKeyListener(new KeyMonitor());

		dialog.setSize(new Dimension(300, 550));
		dialog.setVisible(true);

		while (!start) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				// e1.printStackTrace();
			}
		}
		Random r = new Random();
		if (!multi) {
			for (int i = 0; i < ai; i++) {
				players.add(new Player(r.nextInt(GAME_WIDTH), r.nextInt(GAME_HEIGHT), Dir.D, life, true, this));
			}
		}

		if (music) {
			Audio2.playBGM();
		}

		new Thread(new PaintThread()).start();

		
	}
/**
 * main function to run client
 * @param args
 */
	public static void main(String[] args) {
		PlayerClient tc = new PlayerClient();

		tc.launchFrame();
	}
	
	

	class PaintThread implements Runnable {
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class KeyMonitor extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			myPlayer.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			
			myPlayer.keyPressed(e);

		}
	}
/**
 * Here is the GUI Part
 * 
 */
	class ConnDialog extends Dialog {
		Button mulButton = new Button("To Single Player Mode");
		Button b = new Button("Start Game");
		Button helpButton = new Button("Help");
		Button exitButton = new Button("Exit");
		TextField tfIP = new TextField("127.0.0.1", 12);

		TextField tfPort = new TextField("" + PlayerServer.TCP_PORT, 4);
		TextField ais = new TextField("5");
		Random r = new Random();
		Integer iip = 1024 + r.nextInt(1000);
		TextField tfMyUDPPort = new TextField(iip.toString(), 4);

		public ConnDialog() {
			super(PlayerClient.this, "MireMare Client");
			
			this.setLayout(null);
			
			Label l1 = new Label("Welcome to MireMare");
			l1.setForeground(new Color(120, 255, 120));
			l1.setAlignment(Label.CENTER);
			l1.setBounds(10, 50, 300, 20);
			l1.setFont(new Font("Lucida", Font.PLAIN, 24));
			this.add(l1);

			mulButton.setBounds(50, 100, 200, 20);
			this.add(mulButton);

			Label IP = new Label("IP:");
			IP.setBounds(50, 150, 200, 20);
			IP.setForeground(new Color(120, 255, 120));
			this.add(IP);
			
			tfIP.setBounds(50, 170, 200, 30);
			this.add(tfIP);
			Label Port = new Label("Port:");
			Port.setBounds(50, 200, 200, 20);
			Port.setForeground(new Color(120, 255, 120));
			this.add(Port);

			tfPort.setBounds(50, 220, 200, 30);
			this.add(tfPort);

			Label udp = new Label("UDP Port:");
			udp.setBounds(50, 250, 200, 20);
			udp.setForeground(new Color(120, 255, 120));
			this.add(udp);

			tfMyUDPPort.setBounds(50, 270, 200, 30);
			this.add(tfMyUDPPort);

			Label setaiLabel = new Label("Set AI Numbers");
			setaiLabel.setBounds(50, 170, 200, 20);
			setaiLabel.setForeground(new Color(120, 255, 120));
			setaiLabel.setVisible(false);
			this.add(setaiLabel);

			ais.setBounds(50, 200, 200, 30);
			ais.setVisible(false);
			this.add(ais);

			

			b.setBounds(50, 370, 200, 30);
			this.add(b);

			Checkbox checkbox1 = new Checkbox("Play Music");
			checkbox1.setBounds(80, 400, 150, 30);
			Color checkboxColor = new Color(255, 255, 255);
			checkbox1.setForeground(checkboxColor);
			
			this.add(checkbox1);
			

			helpButton.setBounds(50, 450, 200, 20);
			this.add(helpButton);
			
			exitButton.setBounds(50, 490, 200, 20);
			this.add(exitButton);

			checkbox1.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					music = !music;
				}
			});


			this.setVisible(true);
			this.setLocation(300, 300);
			this.setResizable(false);
			this.setBackground(new Color(110, 110, 0));

			this.pack();
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					setVisible(false);
					System.exit(0);
				}
			});
			b.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					String IP = tfIP.getText().trim();
					int port = Integer.parseInt(tfPort.getText().trim());
					int myUDPPort = Integer.parseInt(tfMyUDPPort.getText().trim());
					ai = Integer.parseInt(ais.getText().trim());
					start = true;
					nc.setUdpPort(myUDPPort);
					nc.connect(IP, port);
					setVisible(false);
					PlayerClient.this.setVisible(true);
					
				}
			});
			mulButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (multi == false) {
						multi = true;
						ais.setVisible(false);
						setaiLabel.setVisible(false);
						IP.setVisible(true);
						tfIP.setVisible(true);
						Port.setVisible(true);
						tfPort.setVisible(true);
						udp.setVisible(true);
						tfMyUDPPort.setVisible(true);
						mulButton.setLabel("To Single Player Mode");
					} else {
						multi = false;
						ais.setVisible(true);
						setaiLabel.setVisible(true);
						IP.setVisible(false);
						tfIP.setVisible(false);
						Port.setVisible(false);
						tfPort.setVisible(false);
						udp.setVisible(false);
						tfMyUDPPort.setVisible(false);
						mulButton.setLabel("To Multiplayer Mode");
					}

				}
			});

			helpButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					Frame f = new Frame();
					Label helpmsg = new Label(
							"<html>Here is the help:<br> Press space key to Fire<br> Press arrow keys to Move<br> Kill all men to survive<br> Press R to use Medkit to Heal<br> GOOD LUCK MAN!</html>");
					helpmsg.setForeground(new Color(120, 255, 120));
					helpmsg.setFont(new Font("Lucida", Font.PLAIN, 18));
					helpmsg.setBounds(20, 50, 400, 400);
					f.setSize(600, 300);
					f.setLocation(300, 300);
					f.setBackground(new Color(19, 19, 19));
					f.addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent e) {
							f.dispose();
						}
					});
					f.add(helpmsg);
					f.setVisible(true);

				}
			});
			
			exitButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
					
				}
			});
		}
	}

	public void connectGame(String ip, int port, int udp) {
		System.out.println("ip" + ip + "port" + port + "udp" + udp);
		nc.setUdpPort(udp);
		nc.connect(ip, port);
	}
}
