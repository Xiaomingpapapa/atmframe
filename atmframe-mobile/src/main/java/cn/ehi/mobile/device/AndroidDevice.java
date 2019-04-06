package cn.ehi.mobile.device;

import cn.ehi.core.config.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AndroidDevice {
    public final static Logger LOG = LoggerFactory.getLogger(AndroidDevice.class);

    //capability属性
    public final static String REMOTE_ADB_HOST = "remoteAdbHost";
    public final static String ADB_PORT = "adbPort";
    public final static String UNICODE_KEYBOARD = "unicodeKeyboard";
    public final static String RESET_KEYBOARD = "resetKeyboard";
    public final static String FULL_RESET = "fullReset";
    public final static String SYSTEM_PORT = "systemPort";
    public final static String APP_PACKAGE = "appPackage";
    public final static String APP_ACTIVITY = "appActivity";
    public final static String AUTO_GRANT_PERMISSIONS = "autoGrantPermissions";
    public final static String HTTP_IDENTIFICATION = "http";

    private AndroidDevice() {}
    public static AndroidDriver getDriver(AppiumConfig appiumConfig, CapabilityConfig capabilityConfig) {
        return createAndroidDriver(appiumConfig, capabilityConfig);
    }

    private static AndroidDriver createAndroidDriver(AppiumConfig appiumConfig, CapabilityConfig conf) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(REMOTE_ADB_HOST, conf.getRemoteAdbHost());
        capabilities.setCapability(ADB_PORT, conf.getAdbPort());
        capabilities.setCapability(UNICODE_KEYBOARD, true);
        capabilities.setCapability(RESET_KEYBOARD, true);
        capabilities.setCapability(FULL_RESET, conf.getFullReset());
        capabilities.setCapability(MobileCapabilityType.APPIUM_VERSION, "1.8.1");
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,conf.getAutomationName());
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PlatformType.Android);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, conf.getUdid());
        capabilities.setCapability(MobileCapabilityType.UDID, conf.getUdid());
        capabilities.setCapability(SYSTEM_PORT, conf.getSystemPort());
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, conf.getPlatformVersion());
        // appium 支持 url 指定 app 位置，以及本地磁盘路径
        if (conf.getApp().contains(HTTP_IDENTIFICATION)) {
            capabilities.setCapability(MobileCapabilityType.APP, conf.getApp());
        } else {
            File app = new File(conf.getApp());
            capabilities.setCapability(MobileCapabilityType.APP, app.getAbsoluteFile());
        }
        capabilities.setCapability(APP_PACKAGE, conf.getAppPackage());
        capabilities.setCapability(APP_ACTIVITY, conf.getAppActivity());
        capabilities.setCapability(AUTO_GRANT_PERMISSIONS, conf.getAutoGrantPermissions());
        //设置 appium server 接收新命令超时时间，这边先写死 80
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "80");
        AndroidDriver driver = null;
        try {
            LOG.info("初始化 appium driver");
            URL serverUrl = new URL("http://" + appiumConfig.getAddress() + ":" + appiumConfig.getPort() + "/wd/hub");
            driver = new AndroidDriver<RemoteWebElement>(serverUrl, capabilities);
            LOG.info("初始化 appium driver 成功");
//            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.info("初始化 appium driver 失败");
            throw new RuntimeException(e);
        }
        return driver;
    }

    public static String getAppName(String path) {
        File file = new File(path);
        String [] appName = file.list();
        return appName[0];
    }

}

