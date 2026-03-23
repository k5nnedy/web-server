package httpserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Using threads to perform each of the requests independently of each other 
//after accepted by server listener class

public class HttpConnectionWorkerThread extends Thread{

    private Socket socket;
    private InputStream in = null;
    private OutputStream out = null;
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);
    

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        

            try {
                //Reading data from the socket's input stream
                in = socket.getInputStream();
                out = socket.getOutputStream();

                //How I got the request and copied and pasted it to Request.txt :
                // int _byte;
                // while ((_byte = in.read()) >= 0) {
                //     System.out.print((char)_byte);
                // }

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

                LOGGER.info(" * Connection processing has finished.");

            } catch (Exception e) {
                LOGGER.error(" * Problem with communication ", e);
            } finally {
                closeAll(in, out, socket);
            }
    }
    private void closeAll(InputStream in, OutputStream out, Socket socket) {
        if (in!=null) {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        if (out !=null) {
            try {
                out.close();
            } catch (IOException e) {   
            }
        }
        if (socket !=null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }

    }

}
