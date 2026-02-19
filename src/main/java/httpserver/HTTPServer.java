package httpserver;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Date;


/**
 * 
 * Driver Class for HTTP Server
*/
public class HTTPServer {
    public static void main(String[] args) {
        try {
            System.out.println("Waiting for clients...");
            //binding server socket to specific port
            ServerSocket serverSocket = new ServerSocket(8080);

            while (true) {
                // create socket obj once connection is found 
                final Socket socket = serverSocket.accept();
                System.out.println("Connection found!");

                // adding a hold to the client to prove connection works. prevents client from closing instantly
                // System.in.read();
                
                // Reading data from the socket's input stream
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // Reads the string and sets it to str 
                String str = in.readLine();

                //show the http request while connection is established
                while(!str.isEmpty() || !(str == null)) {
                    System.out.println(str);
                    str = in.readLine();

                    LocalDate today = LocalDate.now();
                    //httpresponse 
                    String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
                    //output stream
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(httpResponse);
                }

                // // sending the same string back to the client using the socket's output stream
                // PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                // out.println("User says: "+ str);
            }

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

}
