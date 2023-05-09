import java.io.*;
import java.net.*;

public class UdpServer {
    public static void main(String[] args) throws Exception {
        //Create socket 
        DatagramSocket socket = new DatagramSocket(1234);
        
        System.out.println("Waiting for client to connect");
        
        while (true) {
            //Receive message 
            byte[] receiveData = new byte[1024];
            
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            
            String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received: " + message);
            
            //Send a response 
            byte[] sendData = "Message received".getBytes();
            
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
            
            socket.send(sendPacket);
        }
    }
}