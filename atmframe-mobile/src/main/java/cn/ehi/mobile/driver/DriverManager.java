package cn.ehi.mobile.driver;

import cn.ehi.core.config.*;
import cn.ehi.mobile.device.AndroidDevice;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverManager {
    private static final Logger LOG = LoggerFactory.getLogger(DriverManager.class);
    private static ThreadLocal<RemoteWebDriver> remoteWebDriverThreadLocal = new ThreadLocal<>();

    public static void initDriver(Configuration conf) {
        PlatformType currentPlatformType = conf.getPlatformType();
        //如果平台不等于 PC,就证明是移动端的测试，初始化 AppiumDriver
        if (currentPlatformType != PlatformType.PC) {
            if (currentPlatformType == PlatformType.Android) {
                AppiumConfig appiumConfig = AppiumConfigUtil.getAppiumConfig();
                if(appiumConfig == null){
                    appiumConfig = AppiumConfigUtil.getAvailableServerConfig(conf);
                    if(appiumConfig == null) {
                        return;
                    }
                }
                //通过 uid 获取对应 desired capability
                CapabilityConfig capabilityConfig = AppiumConfigUtil.getCapabilityConfig(appiumConfig.getUdid());
                RemoteWebDriver driver = AndroidDevice.getDriver(appiumConfig, capabilityConfig);
                remoteWebDriverThreadLocal.set(driver);
            } else if (currentPlatformType == PlatformType.IOS) {
            }
        }
    }
    public static RemoteWebDriver getDriver() {
        return remoteWebDriverThreadLocal.get();
    }



}
