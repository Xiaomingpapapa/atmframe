package cn.ehi.core.config;
/**
 * 定义device类型：
 * 	App：native App
 * 	Chrome：chrome浏览器包括pc和mobile
 * 	Safari：Safari浏览器
 * 	Firefox：火狐浏览器
 * 	IE：IE浏览器
 * @author wangpeng
 *
 */
public enum DeviceType {
	/**
	 * APP on Android platform or  APP on IOS platform
	 */
	App,
	/**
	 * chrome browser on Android platform
	 */
	Chrome,
	Safari,
	Firefox,
	IE,
	Default
	
}
