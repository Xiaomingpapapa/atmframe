package cn.ehi.core.config;

import cn.ehi.core.dataprovider.reader.XmlReader;
import cn.ehi.utils.CommandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * @author 33053
 * @create 2018/12/11
 * 〈appium 配置文件工具类〉
 */
public class AppiumConfigUtil {
    private static final Logger LOG = LoggerFactory.getLogger(AppiumConfigUtil.class);
    private static List<AppiumConfig> appiumConfigs = null;
    private static List<CapabilityConfig> capabilityConfigs = null;
    private static ThreadLocal<AppiumConfig> appiumConfigLocal = new ThreadLocal<AppiumConfig>();

    /****/
    public static AppiumConfig getAvailableServerConfig(Configuration conf){
        if(appiumConfigs == null) {
            appiumConfigs = XmlReader.getAppiumConfigList();
        }

        //获取当前连接的设备
        List<String> udidList = CommandUtil.getUdidList(conf.getAdbHost());
        //通过设备uid获取对应的appium config
        for (Iterator<AppiumConfig> iterator = appiumConfigs.iterator(); iterator.hasNext();) {
            AppiumConfig config = iterator.next();
            synchronized (config) {
                if (!config.isOccupied()) {
                    if (udidList != null && udidList.contains(config.getUdid())) {
                        config.setOccupied(true);
                        LOG.debug("使用设备：ip " + config.getAddress() + " 设备id " + config.getUdid());
                        appiumConfigLocal.set(config);
                        return config;
                    }
                }
            }
        }
        return null;
    }

    public static CapabilityConfig getCapabilityConfig(String uid) {
        if(capabilityConfigs == null) {
            capabilityConfigs = XmlReader.getCapabilityConfigList();
        }
        return capabilityConfigs.stream().filter(a -> uid.equals(a.getUdid())).findFirst().get();
    }

    public static AppiumConfig getAppiumConfig() {
        return appiumConfigLocal.get();
    }
}
