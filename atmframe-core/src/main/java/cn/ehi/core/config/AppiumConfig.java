package cn.ehi.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 33053
 * @create 2018/12/8
 * 〈设备对应appium server的配置信息〉
 */
public class AppiumConfig {
    private static final Logger LOG = LoggerFactory.getLogger(AppiumConfig.class);

    private String address;
    private String port;
    private String udid;
    private String deviceName;
    private String dataFile;
    private String sqlFile;
    private boolean isOccupied;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public String getSqlFile() {
        return sqlFile;
    }

    public void setSqlFile(String sqlFile) {
        this.sqlFile = sqlFile;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }


    @Override
    public String toString() {
        return "AppiumConfig{" +
                "address='" + address + '\'' +
                ", port='" + port + '\'' +
                ", udid='" + udid + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", dataFile='" + dataFile + '\'' +
                ", sqlFile='" + sqlFile + '\'' +
                ", isOccupied=" + isOccupied +
                '}';
    }
}
