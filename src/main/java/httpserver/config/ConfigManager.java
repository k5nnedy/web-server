package httpserver.config;

public class ConfigManager {

    private static ConfigManager myConfigManager;
    private static Configuration myCurrentConfiguration;

    private ConfigManager () {

    }

    public static ConfigManager getInstance() {
        if (myConfigManager == null) {
            myConfigManager = new ConfigManager();
        }
        return myConfigManager;
    }

    /**
     * Used for configuration file loading file by input filepath
     * */
    public void loadConfigurationFile(String filepath) {

    }

    /**
     * Returns the current loaded configuration
     */
    public void getCurretConfig(){

    }
}
