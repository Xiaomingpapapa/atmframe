package cn.ehi.core.operate;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface IWaitForElement {

	/******************** ：TODO 等待事件 start ***************/
	/**
	 * 线程休眠秒数，单位秒 等待xxx秒
	 * 
	 * @param s
	 *            要休眠的秒数
	 * 
	 */
	public void waitTime(int s);
	

	/**
	 * 隐式等待，如果在指定时间内还是找不到下个元素则会报错停止脚本 全局设定的，find控件找不到就会按照这个事件来等待
	 * 
	 * 隐式等待和显示等待待互不干扰，设置了隐式等待时长30s，显示等待时长30s，此时等待时长还是显示等待时长30s
	 * @param time
	 *            要等待的时间
	 */
	void setGlobalWaitTime(int time);
	
	
	
	/**
	 * 显示等待，等待Id对应的控件出现time秒，一出现马上返回，time秒不出现也返回 默认等待20秒
	 */
	WebElement waitAuto(String by, int time);
	WebElement waitAuto(String by);
	/**
	 * 判断多个元素，等待其中某一个元素出现
	 * @Description:TODO
	 * @param bys
	 * @param time
	 * @param needShot
	 * @return
	 * Integer 具体的某一个元素索引，-1 没有任何元素出现
	 * @exception:
	 * @author: 程文月
	 * @time:2018年2月5日 上午10:17:10
	 */
	 Integer waitOneElementShow(List<String> bys, int time, boolean needShot);
}
