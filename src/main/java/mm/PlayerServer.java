import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class PlayerServer extends Frame{
	private static int ID = 100;
	
	public static final int TCP_PORT = 8888;
	public static final int UDP_PORT = 6666;

	List<Client> clients = new ArrayList<Client>();
	/**
	 * set configuration and start server
	 */
	public void start() {

		new Thread(new UDPThread()).start();

		this.setLocation(0, 0);
		this.setSize(300, 100);
		this.setVisible(true);
		this.setTitle("MireMare Server");
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(TCP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			Socket s = null;
			try {
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				String IP = s.getInetAddress().getHostAddress();
				int udpPort = dis.readInt();
				Client c = new Client(IP, udpPort);
				clients.add(c);
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeInt(ID++);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (s != null) {
					try {
						s.close();
						s = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		new PlayerServer().start();
	}
	/**
	 * 
	 * An inner class for some configurations
	 *
	 */
	private class Client {
		String IP;

		int udpPort;

		public Client(String IP, int udpPort) {
			this.IP = IP;
			this.udpPort = udpPort;
		}
	}

	/**
	 * A UDP Thread class to receive and send messages from/to client 
	 *
	 */
	private class UDPThread implements Runnable {

		byte[] buf = new byte[1024];

		public void run() {
			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket(UDP_PORT);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			System.out.println("UDP thread started at port :" + UDP_PORT);
			while (ds != null) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					
					for (int i = 0; i < clients.size(); i++) {
						Client c = clients.get(i);
						dp.setSocketAddress(new InetSocketAddress(c.IP,
								c.udpPort));
						ds.send(dp);
					}
					//System.out.println("a packet received!");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
