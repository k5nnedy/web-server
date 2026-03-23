import java.net.*;
import java.io.*;

public class TestClient {
    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost", 9806)){

            System.out.println("Client started");
            // creating socket on localhost because client and server are using the same machine
            
            // Creating a buffered reader an entire string.
            // use input streamreader to read the byte stream (system.in) to give us a character stream
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Output stream, true is used to auto-flush the data being sent
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Enter a string");
            // using buffered reader object to read a string from the keyboard
            // readline waits for user to enter a string, that string is set to str
            String str = userInput.readLine();

            // Sending the data
            out.println(str);

            // Second buffered reader to read data the server sends
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Printing the data from the server
            System.out.println(in.readLine());

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
        
}
