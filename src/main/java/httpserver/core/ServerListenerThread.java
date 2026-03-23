package httpserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Using threads to perform each of the requests independently of each other
//Server Listener thread is incharge of listening for new connections.
public class ServerListenerThread extends Thread {
    //Webroot & Port in constructor for different webroots and ports for new threads
    private int port;
    private String webroot;
    private ServerSocket serverSocket;
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);

    
    public ServerListenerThread(int port, String webroot) throws IOException {
        this.port = port;
        this.webroot = webroot;
        this.serverSocket = new ServerSocket(this.port);
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        
        try {
            //keeps serversocket open to multiple connections
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                
                //creates socket obj once connection is found 
                Socket socket = serverSocket.accept();

                LOGGER.info(" * Connection accepted: " + socket.getInetAddress());
                HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(socket);
                workerThread.start();
            } 

        } catch (IOException e) {
            LOGGER.error(" * Problem with communication ", e);
        } finally {
            if(serverSocket!=null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    //don't do anything if closes for now
                }
            }
        }
    }

}
