import java.net.*;
import java.io.*;

public class TestServer {
    public static void main(String[] args) {
        try {
            System.out.println("Waiting for clients...");
            //binding server socket to specific port
            ServerSocket serverSocket = new ServerSocket(9806);

            //create socket obj once connection is found 
            Socket socket = serverSocket.accept();
            System.out.println("Connection found!");
            //adding a hold to the client to prove connection works. prevents client from closing instantly
            System.in.read();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
