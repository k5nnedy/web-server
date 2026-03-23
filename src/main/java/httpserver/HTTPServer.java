package httpserver;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import httpserver.config.ConfigManager;
import httpserver.config.Configuration;
import httpserver.core.ServerListenerThread;


/**
 * 
 * Driver Class for HTTP Server
*/
public class HTTPServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(HTTPServer.class);

    public static void main(String[] args) {
        
        LOGGER.info("Server starting...");
        ConfigManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigManager.getInstance().getCurrentConfig();
        
        LOGGER.info("Using port " + conf.getPort());
        LOGGER.info("Using Port: " + conf.getWebroot());

        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            serverListenerThread.start();
        } catch (IOException e) { 
            e.printStackTrace();
            //todo handle
        }
    }

}
