package udpgroupchat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.TimerTask;

public class ResendTask extends TimerTask {

	private final InetAddress addr;
	private final int port;
	private final String message;
	private final DatagramSocket socket;
	private int times = 0;
	
//	private final WorkerThread wt;
//	private final String payload;
	
	public ResendTask(DatagramSocket s, String message, InetAddress addr, int port)
	{
		this.message = message;
		this.addr = addr;
		this.port = port;
		this.socket = s;
	}
	
//	public ResendTask(WorkerThread wt, String payload)
//	{
//		this.wt = wt;
//		this.payload = payload;
//	}
	
	@Override
	public void run() {
		if(times < 10)
		{
			DatagramPacket txPacket = new DatagramPacket(message.getBytes(),
				message.length(), addr, port);
			try {
				socket.send(txPacket);
			} catch (IOException e) {
			// 	TODO Auto-generated catch block
				e.printStackTrace();
			}
			times++;
		}
		else
		{
			DatagramPacket txPacket = new DatagramPacket("NO ACK RECEIVED--POLL CANCELLED\n".getBytes(),
					"NO ACK RECEIVED--POLL CANCELLED\n".length(), addr, port);
				try {
					socket.send(txPacket);
				} catch (IOException e) {
				// 	TODO Auto-generated catch block
					e.printStackTrace();
				}	
			this.cancel();
		}
	}

}
