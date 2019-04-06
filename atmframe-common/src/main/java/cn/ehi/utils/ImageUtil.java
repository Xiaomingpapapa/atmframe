package cn.ehi.utils;

import org.apache.commons.io.FileUtils;
import org.apache.http.util.TextUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author 33053
 * @create 2018/11/20
 * 〈截图工具类〉
 */
public class ImageUtil {
	private static final Logger LOG = LoggerFactory.getLogger(ImageUtil.class);

	private static final String defaultImagePath = "screenShot/androidPhone/";
	private static final String webDefaultImagePath = "screenShot/web/";

	/**
	 * 截图并保存在默认的地址   {@value ImageUtil#defaultImagePath}
	 * @Description:TODO
	 * @param driver
	 * @param screenShotName
	 * void
	 * @exception:
	 * @author: 程文月
	 * @time:2018年3月6日 下午3:01:47
	 */
	public static void screenshotDefault(WebDriver driver,String screenShotName) {
		
		if(TextUtils.isEmpty(screenShotName)){
			throw new RuntimeException("截图文件名为空");
		}
		File screenShotFile =  ((RemoteWebDriver) driver).getScreenshotAs(OutputType.FILE);
		StringBuilder builder = new StringBuilder(defaultImagePath);
		builder.append(screenShotName).append(SystemUtil.getCurrentDateTime()).append(".jpg");
		File screenFile = new File(builder.toString());
		
		try {
			FileUtils.copyFile(screenShotFile, screenFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 截图
	 * @Description:TODO
	 * @param driver
	 * @param screenShotName  截图名称
	 * @param path 截图保存的地址
	 * Sting
	 * @exception:
	 * @author: 程文月
	 * @time:2018年3月19日 上午9:39:22
	 */
	public static String screenshot(WebDriver driver,String screenShotName ,String path) {

		if(TextUtils.isEmpty(screenShotName)){
			throw new RuntimeException("截图文件名为空");
		}
		File screenShotFile =  ((RemoteWebDriver) driver).getScreenshotAs(OutputType.FILE);
		StringBuilder builder = new StringBuilder(defaultImagePath);
		builder.append(screenShotName).append(SystemUtil.getCurrentDateTime()).append(".jpg");

		File screenFile = new File(builder.toString());
		try {
			FileUtils.copyFile(screenShotFile, screenFile);
			return screenFile.getPath();
		} catch (IOException e) {
			LOG.error("截图保存出错", e);
			return null;
		}
	}



}
