
import java.net.*;
import java.util.*;
public class PINGServer
{
	//Percent loss to simulate real network traffic loss
	private static final double LOSS = 0.3;

	public static void main(String[] args) throws Exception
	{
		//Random number generator used for loss simulation
		Random random = new Random();

		/******************************
		 process command-line arguments
		 ******************************/
		//too little arguments
		if (args.length < 1) {
			System.out.println ("ERR - missing java UDPServer port\n");
			System.exit (-1);
		}
		//too many arguments
		if (args.length > 1) {
			System.out.println ("ERR - too many arguments\n");
			System.exit (-1);
		}

		//check port range for valid port number
		int port = Integer.parseInt(args[0]);
		if(port < 0 || port >= 65536){
			System.out.println ("ERR - invalid port value, must be positive and in the range of 0 - 65536\n");
			System.exit (-1);
		}

		/***********************************************************
		 * Set up datagram socket ready to receive a packet from PINGClient
		 **********************************************************/
		try {
			//create a datagram socket for receiving and sending UDP packets
			//through the port specified on the command line
			DatagramSocket socket = new DatagramSocket(port);

			//print IP and port info
			System.out.println("PINGServer’s socket is created using port number " + port + " with IP address " + InetAddress.getLocalHost().getHostAddress() + "...");

			//stalemate loop
			while(true) {
				//new datagram packet to hold incoming UDP packet
				DatagramPacket request = new DatagramPacket(new byte[1024], 1024);

				//wait until packet is received
				socket.receive(request);

				/****************************************************
				 * Parse and modify received packet data for print formatting
				 ****************************************************/
				String temp = new String(request.getData());
				temp = temp.substring(0, request.getLength());
				String[] splitUp = temp.split("\n");

				//new titles
				splitUp[0] = "----------Received Ping Packet Header----------";
				splitUp[5] = "----------Received Ping Packet Payload---------";

				//rebuild the payload
				String recPayload = splitUp[5] + "\n"
						+ splitUp[6] + "\n"
						+ splitUp[7] + "\n"
						+ splitUp[8] + "\n"
						+ splitUp[9] + "\n";

				//update payload size since the string values changed
				splitUp[4] = "Payload Size: " + recPayload.getBytes().length + " bytes\n";

				//rebuild the header
				String recHeader = splitUp[0] + "\n"
						+ splitUp[1] + "\n"
						+ splitUp[2] + "\n"
						+ splitUp[3] + "\n"
						+ splitUp[4];

				//rebuild the string entirely
				String receivedString = recHeader + recPayload;

				/************************************************************
				 * Build a reply packet with updated info with an uppercase payload
				 ************************************************************/
				//Build our reply header and payload strings similar to above
				splitUp[0] = "-----------Ping Reply Header ----------";
				splitUp[5] = "---------- Ping Reply Payload -------------";
				String replyHeader = splitUp[0] + "\n"
						+ splitUp[1] + "\n"
						+ splitUp[2] + "\n"
						+ splitUp[3] + "\n"
						+ splitUp[4];
				String replyPayload = splitUp[5] + "\n"
						+ splitUp[6] + "\n"
						+ splitUp[7] + "\n"
						+ splitUp[8] + "\n"
						+ splitUp[9] + "\n";
				replyPayload = replyPayload.toUpperCase();
				String response = replyHeader + replyPayload;

				//parse for sequence number
				int seqNum = Integer.parseInt(splitUp[2].replaceAll("[\\D]", ""));

				/*************************************************
				 * Simulate packet loss based on our percent value
				 *************************************************/
				if (random.nextDouble() <= LOSS) {
					//print dropped packet info and the reply it would've sent
					System.out.println(request.getAddress().getHostAddress() + ":" + request.getPort() + ":" + seqNum + ":DROPPED");
					System.out.println(receivedString);
					continue;
				}

				/*********************************************
				 * Updated and send back the datagram packet
				 ********************************************/
				request.setData(response.getBytes(), 0, response.getBytes().length);
				DatagramPacket reply = new DatagramPacket(request.getData(), request.getData().length, request.getAddress(), request.getPort());
				socket.send(reply);
				//print the relevant info and both the packet we received and then the packet we sent back
				System.out.println(request.getAddress().getHostAddress() + ":" + request.getPort() + ":" + seqNum + ":RECEIVED");
				System.out.println(receivedString);
				System.out.println(response);
			}
		}
		catch(Exception ex){
			//catches an exception when a port is already in use
			System.out.println ("ERR - cannot create PINGServer socket using port number " + port);
			System.exit (-1);
		}
	}
}

