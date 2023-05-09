import java.io.*;
import java.net.*;

public class TcpClient {
    public static void main(String[] args) throws Exception {
        //Create socket
        Socket socket = new Socket("192.168.100.4", 1234);
        
        while (true) {
            //Send message to Server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("hello");
            
            //Receive response from Server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            System.out.println("Response from server: " + response);
            
            //Wait for 1 second
            Thread.sleep(1000);
        }

        // socket.close();
    }
}