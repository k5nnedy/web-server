import java.net.*;
import java.io.*;

public class TestClient {
    public static void main(String[] args) {
        try{

            System.out.println("Client started");
            //creating socket on localhost because client and server are using the same machine
            Socket socket = new Socket("localhost", 9806);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
        
}
