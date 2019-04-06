package cn.ehi.core.dataprovider.reader;

import cn.ehi.core.config.AppiumConfig;
import cn.ehi.core.config.CapabilityConfig;
import cn.ehi.core.dataprovider.data.*;
import cn.ehi.utils.FileUtil;
import org.apache.commons.digester3.Digester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlReader {
	/**
	 * xml工具类
	 */
    private static final Logger LOG = LoggerFactory.getLogger(XmlReader.class);
	public static final Digester digester = new Digester();
	public static final String DEVICE_CONFIG = "deviceConfig";
	public static final String DEVICE_CONFIG_SEPARATOR = "deviceConfig/";
	private static final String CONFIG_DIR_NAME = "config";
	private static final String CONFIG_FILE_NAME = "deviceConfig.xml";
	private static final String APPIUM_CONFIG_FILE_NAME = "appiumConfig.xml";
	private static final String APP_DIR_NAME = "app";
	private static final String DATA_DIR_NAME = "data";
	private static final String DATA_FILE_NAME = "DataList.xml";
	public static String DEFALUT_CONFIG_PATH = // 获取默认配置路径
	getSystemDir() + File.separator + CONFIG_DIR_NAME + File.separator + CONFIG_FILE_NAME;
	public static String DEFALUT_CONFIG_DIR = // 获取默认配置文件夹路径
	getSystemDir() + File.separator + CONFIG_DIR_NAME;
	public static String DEFALUT_APP_DIR_PATH = // 获取默认APP文件夹路径
	getSystemDir() + File.separator + APP_DIR_NAME;
	public static String DEFALUT_DATA_DIR_PATH = // 获取默认数据文件夹路径
	getSystemDir() + File.separator + DATA_DIR_NAME;
	public static String DEFALUT_DATA_PATH = // 获取默认数据文件路径
	getSystemDir() + File.separator + DATA_DIR_NAME + File.separator + DATA_FILE_NAME;
	private static String SystemConfigPath = DEFALUT_CONFIG_PATH;
	private static String SystemAppPath = DEFALUT_APP_DIR_PATH;
	private static String SystemDataPath = DEFALUT_DATA_PATH;

	/**
	 * v1.1修改为获取data文件下的所有xml
	 * 
	 * @return data
	 */
	public static DataSource getDataList() {
		List<File> list = FileUtil.getFileList(DEFALUT_DATA_DIR_PATH, "xml");
		return getData(list);
	}

	/**
	 * @param fileName
	 *            传入默认文件夹下的数据XML文件的文件名
	 * @return data 获取默认的数据
	 */
	public static DataSource getDataList(String fileName) {
		return getData(DEFALUT_DATA_DIR_PATH.concat(File.separator).concat(fileName).concat(".xml"));
	}

	/**
	 * 获取一组文件的xml数据
	 * 
	 * @param fileList
	 * @return
	 */
	public static DataSource getDataList(List<File> fileList) {
		return getData(fileList);
	}

	/**
	 * 
	 * @param filePath
	 *            获取指定文件路径的的 DataSource
	 * @return DataSource 指定xml的
	 */
	private static DataSource getData(String filePath) {
		DataSource dataList = null;
		String _DATA_LIST = DataSource.DATA_LIST.concat("/");
		String DATA_LIST = DataSource.DATA_LIST;
		String _BASE_DATA = _DATA_LIST.concat("BaseData/");
		String BASE_DATA = _DATA_LIST.concat("BaseData");
		String _DATA_GROUP = _BASE_DATA.concat("DataGroup/");
		String DATA_GROUP = _BASE_DATA.concat("DataGroup");

		Digester d = getDefalutDigester();
		d.addObjectCreate(DATA_LIST, DataSource.class);

		d.addObjectCreate(BASE_DATA, BaseData.class);
		d.addBeanPropertySetter(_BASE_DATA.concat("typeName"), "typeName");
		d.addBeanPropertySetter(_BASE_DATA.concat("condition"), "condition");
		d.addBeanPropertySetter(_BASE_DATA.concat("expectedValue"), "expectedValue");

		d.addObjectCreate(DATA_GROUP, DataGroup.class);
		d.addBeanPropertySetter(_DATA_GROUP.concat("key"), "key");
		d.addBeanPropertySetter(_DATA_GROUP.concat("value"), "value");
		d.addSetNext(DATA_GROUP, "putData");
		d.addSetNext(BASE_DATA, "addData");
		try {
			dataList = d.parse(new File(filePath));

		} catch (IOException e) {
			LOG.error("IOException 异常", e);
		} catch (SAXException e) {
            LOG.error("SAXException 异常", e);
		}finally {
			d = null;
		}
		return dataList;
	}

    public static List<AppiumConfig> getAppiumConfigList() {
        List<AppiumConfig> configs = new ArrayList<>();
        Digester digester = new Digester();
        digester.setValidating(false);
        digester.push(configs);
        digester.addObjectCreate("AppiumConfigs/AppiumConfig", AppiumConfig.class);
        digester.addBeanPropertySetter("AppiumConfigs/AppiumConfig/address", "address");
        digester.addBeanPropertySetter("AppiumConfigs/AppiumConfig/port", "port");
        digester.addBeanPropertySetter("AppiumConfigs/AppiumConfig/udid", "udid");
        digester.addBeanPropertySetter("AppiumConfigs/AppiumConfig/deviceName", "deviceName");
        digester.addBeanPropertySetter("AppiumConfigs/AppiumConfig/dataFile", "dataFile");
        digester.addBeanPropertySetter("AppiumConfigs/AppiumConfig/sqlFile", "sqlFile");
        digester.addSetNext("AppiumConfigs/AppiumConfig", "add");
        try {
            configs = digester.parse(new File(getSystemDir() + File.separator + "config/appium_config.xml"));
        } catch (IOException e) {
            LOG.error("未找到xml文件",e);
        } catch (SAXException e) {
            LOG.error("xml文件解析出错",e);
        }finally {
            digester = null;
        }
        return configs;
    }

    public static List<CapabilityConfig> getCapabilityConfigList() {
        List<CapabilityConfig> capabilities = new ArrayList<>();
        Digester digester = new Digester();
        digester.setValidating(false);
        digester.push(capabilities);
        digester.addObjectCreate("CapabilitiesGroup/Capabilities", CapabilityConfig.class);
        digester.addBeanPropertySetter("CapabilitiesGroup/Capabilities/udid", "udid");
        digester.addBeanPropertySetter("CapabilitiesGroup/Capabilities/remoteAdbHost", "remoteAdbHost");
        digester.addBeanPropertySetter("CapabilitiesGroup/Capabilities/adbPort", "adbPort");
        digester.addBeanPropertySetter("CapabilitiesGroup/Capabilities/systemPort", "systemPort");
        digester.addBeanPropertySetter("CapabilitiesGroup/Capabilities/platformName", "platformName");
        digester.addBeanPropertySetter("CapabilitiesGroup/Capabilities/automationName", "automationName");
        digester.addBeanPropertySetter("CapabilitiesGroup/Capabilities/platformVersion", "platformVersion");
        digester.addBeanPropertySetter("CapabilitiesGroup/Capabilities/app", "app");
        digester.addBeanPropertySetter("CapabilitiesGroup/Capabilities/appPackage", "appPackage");
        digester.addBeanPropertySetter("CapabilitiesGroup/Capabilities/appActivity", "appActivity");
        digester.addBeanPropertySetter("CapabilitiesGroup/Capabilities/fullReset", "fullReset");
		digester.addBeanPropertySetter("CapabilitiesGroup/Capabilities/autoGrantPermissions", "autoGrantPermissions");
        digester.addSetNext("CapabilitiesGroup/Capabilities", "add");
        try {
            capabilities = digester.parse(new File(System.getProperty("user.dir") + File.separator + "config/capability_config.xml"));
        } catch (IOException e) {
            LOG.error("未找到xml文件",e);
        } catch (SAXException e) {
            LOG.error("xml文件解析出错",e);
        }finally {
            digester = null;
        }
        return capabilities;
    }

	public static SqlSource getSqlSource(String filePath) {
        SqlSource sqlSource = null;
        Digester digester = getDefalutDigester();
        digester.addObjectCreate("SqlSource", SqlSource.class);
        digester.addObjectCreate("SqlSource/SqlGroup", SqlGroup.class);
        digester.addBeanPropertySetter("SqlSource/SqlGroup/caseName", "caseName");
        digester.addObjectCreate("SqlSource/SqlGroup/SqlData", SqlData.class);
        digester.addBeanPropertySetter("SqlSource/SqlGroup/SqlData/id", "id");
        digester.addBeanPropertySetter("SqlSource/SqlGroup/SqlData/sql", "sql");
        digester.addBeanPropertySetter("SqlSource/SqlGroup/SqlData/type", "type");
        digester.addSetNext("SqlSource/SqlGroup", "addSqlGroup");
        digester.addSetNext("SqlSource/SqlGroup/SqlData", "addSqlData");
        try {
            sqlSource = digester.parse(new File(getSystemDir() + File.separator + "data" + File.separator + filePath + ".xml"));
        } catch (Exception e) {
            LOG.error("SAX解析异常", e);
        }
        return sqlSource;
    }


	/**
	 * 
	 * @param fileList
	 *            获取一个文件下的所有xml数据
	 * @return
	 */
	private static DataSource getData(List<File> fileList) {
		DataSource dataList = new DataSource();
		DataSource temp;
		for (File file : fileList) {
			temp = getData(file.getAbsolutePath());
			if (temp == null) {
				continue;
			}
			dataList.addAllDataList(temp);
		}
		return dataList;
	}

	private static int digesters = 0;
	/**
	 * 获取xml解析文件类
	 *
            * @return digester
	 */
    public static Digester getDefalutDigester() {
        digester.setValidating(false);
        digesters++;
//		LoggerUtils.debug(Util.class,"digesters : "+digesters);
        return new Digester();
    }

	/**
	 * 暂定使用这种方法，但是未来打包jar时候，可能会有问题
	 * 
	 * @return
	 */
	public static String getSystemDir() {
		return System.getProperty("user.dir");
	}



	public static String getSystemAppiumConfigPath(){
		return getSystemDir() + File.separator + CONFIG_DIR_NAME + File.separator + APPIUM_CONFIG_FILE_NAME;
	}

}
