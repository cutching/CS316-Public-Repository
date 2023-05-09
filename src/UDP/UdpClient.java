import java.io.*;
import java.net.*;

public class UdpClient {
    public static void main(String[] args) throws Exception {
    
        //Create socket 
        DatagramSocket socket = new DatagramSocket();
        
        while (true) {
            //Create message 
            byte[] sendData = "hello".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("192.168.100.4"), 1234);
            
            socket.send(sendPacket);
            
            //Receive response 
            byte[] receiveData = new byte[1024];
            
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
         
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Response from Server: " + response);
            
            Thread.sleep(1000);
        }

        // socket.close();
    }
}