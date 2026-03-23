package httpserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Date;

import httpserver.config.ConfigManager;
import httpserver.config.Configuration;


/**
 * 
 * Driver Class for HTTP Server
*/
public class HTTPServer {
    public static void main(String[] args) {
        System.out.println("Server starting...");

        ConfigManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigManager.getInstance().getCurrentConfig();

        System.out.println("Using port: " + conf.getPort());
        System.out.println("Using webroot: " + conf.getWebroot());

        try {
            //binding server socket to config port
            ServerSocket serverSocket = new ServerSocket(conf.getPort());

            //creates socket obj once connection is found 
            Socket socket = serverSocket.accept();

            // Reading data from the socket's input stream
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            String html = "<html><head><title>Simple HTTP Server</title></head><body><h1>This page was served using my simple Java HTTP Server</h1></body></html>";
            
            //Carriage return + line feed:
            final String CRLF = "\n\r"; //ascii 13 and 10
            String response = 
                "HTTP/1.1 200 OK" + CRLF +//Status Line: HTTP VERSION | RESPONSE_CODE | RESPONSE_MESSAGE
                "Content Length: " + html.getBytes().length + CRLF +  //HEADER
                    CRLF +
                    html +
                    CRLF + CRLF;
            out.write(response.getBytes());

            in.close();
            out.close();
            socket.close();
            serverSocket.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // try {
        //     System.out.println("Waiting for clients...");
        //     //binding server socket to specific port
        //     ServerSocket serverSocket = new ServerSocket(8080);

        //     while (true) {
        //         // create socket obj once connection is found 
        //         final Socket socket = serverSocket.accept();
        //         System.out.println("Connection found!");

        //         // adding a hold to the client to prove connection works. prevents client from closing instantly
        //         // System.in.read();
                
        //         // Reading data from the socket's input stream
        //         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //         // Reads the string and sets it to str 
        //         String str = in.readLine();

        //         //show the http request while connection is established
        //         while(!str.isEmpty() || !(str == null)) {
        //             System.out.println(str);
        //             str = in.readLine();

        //             LocalDate today = LocalDate.now();
        //             //httpresponse 
        //             String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
        //             //output stream
        //             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //             out.println(httpResponse);
        //         }

        //         // // sending the same string back to the client using the socket's output stream
        //         // PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //         // out.println("User says: "+ str);
        //     }

        // } catch(Exception e) {

        //     e.printStackTrace();
        // }
    }

}
