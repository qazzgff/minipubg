import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class PlayerMoveMsg implements Msg {
	int msgType = Msg.TANK_MOVE_MSG;

	int x, y;

	int id;

	Dir ptDir;

	Dir dir;
	
	boolean robot;

	PlayerClient tc;
	
	int life;

	
	public PlayerMoveMsg(int id, int x, int y, Dir dir, Dir ptDir,int life, boolean robot) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.ptDir = ptDir;
		this.robot = robot;
		this.life =life;
		
	}

	public PlayerMoveMsg(PlayerClient tc) {
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
			int x = dis.readInt();
			int y = dis.readInt();
			int life = dis.readInt();
			Dir dir = Dir.values()[dis.readInt()];
			Dir ptDir = Dir.values()[dis.readInt()];
			boolean robot = dis.readBoolean();
			
			boolean exist = false;
			for (int i = 0; i < tc.players.size(); i++) {
				Player t = tc.players.get(i);
				if (t.id == id) {
					t.x = x;
					t.y = y;
					t.dir = dir;
					t.ptDir = ptDir;
					t.robot = robot;
					t.life = life;
					exist = true;
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
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(life);
			dos.writeInt(dir.ordinal());
			dos.writeInt(ptDir.ordinal());
			dos.writeBoolean(robot);
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
