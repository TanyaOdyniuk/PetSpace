package com.netcracker.ui.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerPropertiesGetter {
    private InputStream inputStream;

    public ServerPropertiesGetter() {
        inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
    }

    public String getURL() {
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String serverAddress = properties.getProperty("server.address");
        String serverPort = properties.getProperty("server.port");
        return "http://" + serverAddress + ":" + serverPort;
    }
}