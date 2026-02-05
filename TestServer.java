import java.net.*;
import java.io.*;
// Simple implementation of an Echo Server sending what the user says back to the user.
public class TestServer {
    public static void main(String[] args) {
        try {
            System.out.println("Waiting for clients...");
            //binding server socket to specific port
            ServerSocket serverSocket = new ServerSocket(9806);

            // create socket obj once connection is found 
            Socket socket = serverSocket.accept();
            System.out.println("Connection found!");
            // adding a hold to the client to prove connection works. prevents client from closing instantly
            // System.in.read();
            
            // Reading data from the socket's input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Reads the string and sets it to str 
            String str = in.readLine();

            // sending the same string back to the client using the socket's output stream
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("User says: "+ str);

        } catch(Exception e) {

            e.printStackTrace();

        }
    }

}
