import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
/**
 * If one missile is shot(created), send msg for server to synchronize
 * 
 */
public class MissileNewMsg implements Msg {
	int msgType = Msg.MISSILE_NEW_MSG;

	PlayerClient tc;

	Missile m;

	/**
	 * Constructor if one missile is created
	 * @param tc id is used to identify who send this message
	 */
	public MissileNewMsg(Missile m) {
		this.m = m;
	}
	
	/**
	 * Constructor if one missile is created
	 * @param tc is used to identify who send this message
	 */
	public MissileNewMsg(PlayerClient tc) {
		this.tc = tc;
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
			dos.writeInt(m.playerId);
			dos.writeInt(m.id);
			dos.writeInt(m.x);
			dos.writeInt(m.y);
			dos.writeInt(m.dir.ordinal());
			dos.writeBoolean(m.robot);
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
	
	/**
	 * Parse this msg from server
	 * @param dis the msg that to be parsed
	 */
	public void parse(DataInputStream dis) {
		try {
			int playerId = dis.readInt();
			if (playerId == tc.myPlayer.id) {
				return;
			}
			int id = dis.readInt();
			int x = dis.readInt();
			int y = dis.readInt();
			Dir dir = Dir.values()[dis.readInt()];
			boolean robot = dis.readBoolean();
			
			Missile m = new Missile(playerId, x, y, dir, robot, tc);
			m.id = id;
			if(!robot) {
				tc.missiles.add(m);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
