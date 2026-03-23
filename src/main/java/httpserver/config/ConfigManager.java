package httpserver.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import httpserver.util.Json;

//Singleton class
public class ConfigManager {

    private static ConfigManager myConfigManager;
    private static Configuration myCurrentConfiguration;

    private ConfigManager () {

    }

    /**
    Creates config manager if we do not have one and returns it.
    */
    public static ConfigManager getInstance() {
        if (myConfigManager == null) {
            myConfigManager = new ConfigManager();
        }
        return myConfigManager;
    }

    /**
     * Used for configuration file loading file by input filepath
     * @throws IOException 
     * */
    public void loadConfigurationFile(String filepath) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filepath);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new HttpConfiguartionException(e);
        }
        StringBuffer sb = new StringBuffer();
        //Reading everying on the file:
        int i;
        try {
            while ((i = fileReader.read()) != -1) {
                sb.append((char)i);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new HttpConfiguartionException(e);
        }
        JsonNode conf = null;
        try {
            conf = Json.parse(sb.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new HttpConfiguartionException("Error with parsing the config file", e);
        }
        try {
            myCurrentConfiguration = Json.fromJson(conf, Configuration.class);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            throw new HttpConfiguartionException("Error parsing the config file, INTERNAL", e);
        } 
        //fileReader.close();
    }

    /**
     * Returns the current loaded configuration
     */
    public Configuration getCurrentConfig(){
        if (myCurrentConfiguration == null) {
            throw new HttpConfiguartionException("No current Configuration was set.");
        }
        return myCurrentConfiguration;

    }
}
