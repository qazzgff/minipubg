import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class PlayerDeadMsg implements Msg {
	int msgType = Msg.TANK_DEAD_MSG;

	PlayerClient tc;

	int id;

	public PlayerDeadMsg(int id) {
		this.id = id;
	}

	public PlayerDeadMsg(PlayerClient tc) {
		this.tc = tc;
	}

	
	/**
	 * Parse this msg from server
	 * @param dis the msg that to be parsed
	 */
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (tc.myPlayer.id == id) {
				return;
			}

			for (int i = 0; i < tc.players.size(); i++) {
				Player t = tc.players.get(i);
				if (t.id == id) {
					t.setLive(false);
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
