import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
/**
 * A class for handling network issues 
 *
 */
public class NetClient {
	PlayerClient tc;

	private int udpPort;

	String IP; 
	DatagramSocket ds = null;
	
	/**
	 * constructor 
	 * @param tc the PlayerClient that want to communicate with server
	 */
	public NetClient(PlayerClient tc) {
		this.tc = tc;

	}
	/**
	 * connect to server
	 * @param IP server IP
	 * @param port server TCP port
	 */
	public void connect(String IP, int port) {

		this.IP = IP;

		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		Socket s = null;
		try {
			s = new Socket(IP, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int id = dis.readInt();
			tc.myPlayer.id = id;

			if (id % 2 == 0)
				tc.myPlayer.good = false;
			else
				tc.myPlayer.good = true;

			System.out.println("Connected to server! and server give me a ID:"
					+ id);
		} catch (UnknownHostException e) {
			e.printStackTrace();
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

		PlayerNewMsg msg = new PlayerNewMsg(tc.myPlayer);
		send(msg);
		for(int i =0; i<tc.players.size();i++) {
			msg = new PlayerNewMsg(tc.players.get(i));
			send(msg);
		}

		new Thread(new UDPRecvThread()).start();
	}
	
	/**
	 * send message to server
	 * @param msg the message to send via UDP port
	 */
	public void send(Msg msg) {
		msg.send(ds, IP, PlayerServer.UDP_PORT);
	}

	private class UDPRecvThread implements Runnable {

		byte[] buf = new byte[1024];

		public void run() {

			while (ds != null) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					parse(dp);
					System.out.println("a packet received from server!");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void parse(DatagramPacket dp) {
			ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp
					.getLength());
			DataInputStream dis = new DataInputStream(bais);
			int msgType = 0;
			try {
				msgType = dis.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Msg msg = null;
			switch (msgType) {
			case Msg.TANK_NEW_MSG:
				msg = new PlayerNewMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.TANK_MOVE_MSG:
				msg = new PlayerMoveMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.MISSILE_NEW_MSG:
				msg = new MissileNewMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.TANK_DEAD_MSG:
				msg = new PlayerDeadMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.MISSILE_DEAD_MSG:
				msg = new MissileDeadMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			}

		}

	}
	/**
	 * To get UDP Port number
	 * @return UDP port number
	 */
	public int getUdpPort() {
		return udpPort;
	}
	/**
	 * To set UDP Port Number
	 * @param udpPort to set UDP Port Number
	 */
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
}
