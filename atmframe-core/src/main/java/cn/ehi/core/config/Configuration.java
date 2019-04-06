package cn.ehi.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

public class Configuration {
	static final Logger logger = LoggerFactory.getLogger(Configuration.class);
	private Properties properties=new Properties();
	private String confPath="\\sys_config.properties";
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	/**
	 * 默认初始化读取src/test/resources/config/conf.properties
	 */
	public Configuration(){
		//
		readConf(confPath);
	}
	/**
	 * 初始化读取confPath的配置内容
	 * @param confPath 配置文件路径
	 */
	public Configuration(String confPath){
		//
		this.confPath=confPath;
		readConf(confPath);
		
	}
	private void readConf(String path){
		logger.info("配置文件路径："+path);
		try (InputStreamReader propFile=new InputStreamReader(new FileInputStream(System.getProperty("user.dir") + confPath),"UTF-8")){
			properties.load(propFile);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("读取配置文件错误："+confPath, e);
		}
	}
	public String getConfPath() {
		return confPath;
	}
	public boolean contains(String key){
		return properties.containsKey(key);
	}
	/**
	 *  获取配置信息，返回字符串，自动去除首尾空格
	 * @param key
	 * @return
	 */
	public String get(String key){
		if (contains(key)){
			return properties.getProperty(key).trim();
		}
		else {
			logger.error("配置中不存在\""+key+"\"配置项");
			return "";
		}
	}

	public String getAdbHost() {
	    return properties.getProperty("adbHost").trim();
    }

	/**
	 * 获得配置platformType的值
	 * @return
	 */
	public PlatformType getPlatformType(){
		String platform=properties.getProperty("platformType").trim();
		if (platform.equalsIgnoreCase("ANDROID")){
			return PlatformType.Android;
		}else if(platform.equalsIgnoreCase("IOS")){
			return PlatformType.IOS;
		}else if(platform.equalsIgnoreCase("PC")) {
			return PlatformType.PC;
		}else if(platform.equalsIgnoreCase("IOSSim")) {
			return PlatformType.IOSSim;
		}else {
			return PlatformType.Default;
		}
	}
	public DeviceType getDeviceType(){
		String platform=properties.getProperty("deviceType").trim();
		if (platform.equalsIgnoreCase("APP")){
			return DeviceType.App;
		}else if(platform.equalsIgnoreCase("CHROME")){
			return DeviceType.Chrome;
		}else if(platform.equalsIgnoreCase("FIREFOX")) {
			return DeviceType.Firefox;
		}else if(platform.equalsIgnoreCase("IE")) {
			return DeviceType.IE;
		}else if(platform.equalsIgnoreCase("SAFARI")) {
			return DeviceType.Safari;
		}else {
			return DeviceType.Default;
		}
	}
}
