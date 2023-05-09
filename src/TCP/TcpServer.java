import java.io.*;
import java.net.*;

public class TcpServer {
    public static void main(String[] args) throws Exception {
        //Create server socket 
        ServerSocket serverSocket = new ServerSocket(1234);
        
        System.out.println("Waiting for client to connect...");
        
        //Client connection
        Socket clientSocket = serverSocket.accept();
        
        System.out.println("Client connected");
        
        //Input and output streams
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        
        while (true) {
            //Incoming message
            String message = in.readLine();
            System.out.println("Received: " + message);
            
            //Responsd back 
            out.println("Message received");
        }
    }
}