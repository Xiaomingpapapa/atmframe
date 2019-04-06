package cn.ehi.core.config;

/**
 * @author 33053
 * @create 2018/12/8
 * 〈开启 appium session 参数〉
 */
public class CapabilityConfig {
    private String udid;
    private String remoteAdbHost;
    private String adbPort;
    private String systemPort;
    private String platformName;
    private String automationName;
    private String platformVersion;
    private String app;
    private String appPackage;
    private String appActivity;
    private String fullReset;
    private String autoGrantPermissions;

    public String getUdid() {
        return udid;
    }

    public String getRemoteAdbHost() {
        return remoteAdbHost;
    }

    public void setRemoteAdbHost(String remoteAdbHost) {
        this.remoteAdbHost = remoteAdbHost;
    }

    public String getAdbPort() {
        return adbPort;
    }

    public void setAdbPort(String adbPort) {
        this.adbPort = adbPort;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getSystemPort() {
        return systemPort;
    }

    public void setSystemPort(String systemPort) {
        this.systemPort = systemPort;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getAutomationName() {
        return automationName;
    }

    public void setAutomationName(String automationName) {
        this.automationName = automationName;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppActivity() {
        return appActivity;
    }

    public void setAppActivity(String appActivity) {
        this.appActivity = appActivity;
    }

    public String getFullReset() {
        return fullReset;
    }

    public void setFullReset(String fullReset) {
        this.fullReset = fullReset;
    }

    public String getAutoGrantPermissions() {
        return autoGrantPermissions;
    }

    public void setAutoGrantPermissions(String autoGrantPermissions) {
        this.autoGrantPermissions = autoGrantPermissions;
    }
}
