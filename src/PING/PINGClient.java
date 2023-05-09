
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class PINGClient
{
	//local variables
	public static final byte version = 1;
	public static int totalSent = 0;
	public static int totalRec = 0;
	public static float minTime = 0;
	public static float maxTime = 0;
	public static float totalTime = 0;
	public static float timeStamp = 0;

	public static void main( String[] args) throws Exception
	{
		//obtain command line argument and check for validity
		if(args.length < 2){
			System.out.println("ERR - Not enough arguments, format is: [IPAddress, host port]");
			System.exit(-1);
		} else if (args.length > 2) {
			System.out.println("ERR - Too many arguments, format is: [IPAddress, host port]");
			System.exit(-1);
		}

		//get host IP could throw UnknownHostException
		InetAddress host = InetAddress.getByName(args[0]);
		//checks if port is between 0 and 65536
		int port = Integer.parseInt(args[1]);
		if(port < 0 || port >= 65536){
			System.out.println("ERR - Invalid port number");
			System.exit(-1);
		}

		//create a datagram socket for receiving and sending UDP packets
		//through the port specified on the command line
		DatagramSocket socket = new DatagramSocket();

		/***************************************
		 * Main ping loop, runs exactly 10 times
		 ***************************************/
		for( int i = 0; i < 10; i++ )
		{
			//grab initial time
			timeStamp = System.nanoTime();
			//simple date formatter
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
			Date date = new Date(System.currentTimeMillis());

			/**********************************
			 * Build initial payload to be sent
			 **********************************/
			String payload = ("--------- Ping Packet Payload ------------\n"
							+ "Host: " + host.getCanonicalHostName() + "\n"
							+ "---------------------------------------");
			//header comes second so the actual byte size of payload can be used
			String header = ("---------- Ping Packet Header ----------\n"
							+ "Version: " + version + "\n"
							+ "Sequence No.: " + (i + 1) + "\n"
							+ "Time: " + formatter.format(date) + "\n"
							+ "Payload Size: " + payload.getBytes().length + " bytes\n");
			//combine parts of the payload
			header += payload;

			//create new datagram packet to be sent
			DatagramPacket request = new DatagramPacket(header.getBytes(), header.getBytes().length, host, port);
			//print what was sent
			System.out.println(new String(request.getData()));
			//send
			socket.send(request);
			totalSent++;
			//set socket timeout to 1 second
			socket.setSoTimeout(1000);

			/*****************************************************
			 * Receives the packet and prints relevant information
			 ****************************************************/
			try
			{
				//receive the packet
				socket.receive(request);
				totalRec++;

				//find time it was received and calculate trip time
				float timeReceived = System.nanoTime();
				float timeForTrip = timeReceived - timeStamp;

				/****************************************************
				 * Parse and modify received packet data for print formatting
				 ****************************************************/
				String response = new String(request.getData());
				response = response.substring(0, request.getLength());
				String[] temp = response.split("\n");

				temp[0] = "\n\n----------- Received Ping Reply Header ----------";
				temp[5] = "---------- Received Ping Reply Payload -------------";
				//important to note payload size is not changed here
				for (String eachString: temp) {
					response += eachString + "\n";
				}
				//print the data
				System.out.println("\n" + response + "\n");

				/********************************************************************
				 * Calculate the max, min, avg trip time and keep track of total time
				 *******************************************************************/
				if(maxTime == 0)
				{	//baseline times
					maxTime = timeForTrip;
					minTime = timeForTrip;
				}
				//new maxTime
				if(timeForTrip > maxTime){
					maxTime = timeForTrip;
				}
				//new minTime
				if(timeForTrip < minTime){
					minTime = timeForTrip;
				}
				//add to total time
				totalTime += timeForTrip;
			}
			catch(SocketTimeoutException ex)
			{
				//if a packet is not received after one second then we print a timeout message
				System.out.println("\n--------------- Ping Reply Timed out ------------------\n");
			}
		}

		//defaults to 100% loss
		double lost = 100;
		//calculates percent lost of packets
		if(totalRec != 0){
			lost = ((double)(totalSent-totalRec)/totalSent) * 100;
		}

		//formats all relevant info in the format [min RTT :: max RTT :: avg RTT :: lost]
		System.out.printf("Summary: %.2f :: %.2f :: %.2f :: " + (int)lost + "%%\n", (double)(minTime / 1000000000), (double)(maxTime / 1000000000), (double)((totalTime / totalRec) / 1000000000));
		System.exit(-1);
	}
}