package cn.ehi.mobile.server;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.ServerArgument;

import java.io.File;
import java.net.URL;

public class AppiumServer {
    public static AppiumDriverLocalService service;
    public static AppiumServer appiumServer;


    public static AppiumServer getInstance() {
        if (appiumServer == null) {
            synchronized (AppiumServer.class) {
                if (appiumServer == null) {
                    appiumServer = new AppiumServer();
                }
            }
        }
        return appiumServer;
    }

    public URL startAppiumServerByDefault() {
        service = AppiumDriverLocalService.buildDefaultService();
        service.start();
        if (service == null || !service.isRunning()) {
            throw new RuntimeException("An appium server node is not started!");
        }
        return service.getUrl();
    }

    public void stopServer() {
        if (service != null) {
            service.stop();
        }
    }

    public URL startServer(String ipAddress, int port) {
        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.withIPAddress(ipAddress);
        builder.usingPort(port);
        service = AppiumDriverLocalService.buildService(builder);
        service.start();
        if (service == null || !service.isRunning()) {
            throw new RuntimeException("An appium server node is not started!");
        }
        return service.getUrl();

    }

    public URL startServer(String ipAddress, int port, File logFile, ServerArgument... arguments) {
        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.withIPAddress(ipAddress);
        builder.usingPort(port);
        builder.withLogFile(logFile);
        for (ServerArgument argument : arguments) {
            builder.withArgument(argument);
        }
        service = AppiumDriverLocalService.buildService(builder);
        service.start();
        if (service == null || !service.isRunning()) {
            throw new RuntimeException("An appium server node is not started!");
        }
        return service.getUrl();
    }
}
