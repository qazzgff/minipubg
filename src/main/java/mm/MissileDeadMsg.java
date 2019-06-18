import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
/**
 * If one missile disappeared, send msg for server to synchronize
 * 
 */
public class MissileDeadMsg implements Msg {
	int msgType = Msg.MISSILE_DEAD_MSG;

	PlayerClient tc;

	int playerId;

	int id;
	
	int harm;
	
	/**
	 * Constructor if one missile disappeared
	 * @param playerId id is used to identify who send this message
	 * @param id is the missile id to identify each missile
	 */
	public MissileDeadMsg(int playerId, int id) {
		this.playerId = playerId;
		this.id = id;
	}
	
	/**
	 * Another alternative Constructor
	 * @param the client who send this msg
	 */
	public MissileDeadMsg(PlayerClient tc) {
		this.tc = tc;
	}
	
	/**
	 * Parse this msg from server
	 * @param dis the msg that to be parsed
	 */
	public void parse(DataInputStream dis) {
		try {
			int playerId = dis.readInt();
			int id = dis.readInt();

			for (int i = 0; i < tc.missiles.size(); i++) {
				Missile m = tc.missiles.get(i);
				if (m.playerId == playerId && m.id == id) {
					m.live = false;
					
					tc.explodes.add(new Explode(m.x, m.y, tc));
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Send this msg to server
	 * @param ds DatagramSocket that to sent
	 * @param IP IP that to sent
	 * @param udpPort UDP Port that to sent
	 */
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(playerId);
			dos.writeInt(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length,
					new InetSocketAddress(IP, udpPort));
			ds.send(dp);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
