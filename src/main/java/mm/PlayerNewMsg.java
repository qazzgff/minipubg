import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class PlayerNewMsg implements Msg {
	int msgType = Msg.TANK_NEW_MSG;

	Player player;

	PlayerClient tc;
	
	public PlayerNewMsg(Player player) {
		this.player = player;
	}
	
	public PlayerNewMsg(PlayerClient tc) {
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
			dos.writeInt(player.id);
			dos.writeInt(player.x);
			dos.writeInt(player.y);
			dos.writeInt(player.getLife());
			dos.writeInt(player.dir.ordinal());
			dos.writeBoolean(player.robot);
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
			int id = dis.readInt();
			if (tc.myPlayer.id == id) {
				return;
			}

			int x = dis.readInt();
			int y = dis.readInt();
			int life = dis.readInt();
			Dir dir = Dir.values()[dis.readInt()];
			
			boolean robot = dis.readBoolean();
			
			boolean exist = false;
			for (int i = 0; i < tc.players.size(); i++) {
				Player t = tc.players.get(i);
				if (t.id == id) {
					t.robot = robot;
					exist = true;
					break;
				}
			}

			if (!exist) {
				PlayerNewMsg tnMsg = new PlayerNewMsg(tc.myPlayer);
				tc.nc.send(tnMsg);
				for(int i = 0; i<tc.players.size();i++) {
					tnMsg = new PlayerNewMsg(tc.players.get(i));
					tc.nc.send(tnMsg);
				}

				Player t = new Player(x, y, dir, life,robot, tc);
				t.id = id;
				tc.players.add(t);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
