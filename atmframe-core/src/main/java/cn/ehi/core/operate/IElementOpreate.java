package cn.ehi.core.operate;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface IElementOpreate {
	void clickView(String by);
	void sendKeys(String by, String content);
	void clear(String by);
	void clickItem(String by, int position);
	String getText(String by);
	String getItemText(String by, int position);
	
	
	/*********************** 判断，检查元素start ***************/
	
	/**
	 * 判断当前界面有没有这个字符串存在
	 * 
	 * @param text
	 *            要判断的字符串
	 * @return 存在返回真
	 */
	boolean isTextExist(String text);
	
	/** 判断控件是否存在,存在返回true，不存在返回false */
	boolean checkCell(String by);
	/**
	 * @Description: 判断集合中是否包含某一元素
	 * @param driver
	 * @param elements
	 *            列表数据
	 * @param content
	 *            item所含元素内容
	 * @return boolean
	 */
	boolean check(List<WebElement> elements, String content);
	
	/**
	 * @Description:判断集合中是否有与某元素不同的内容
	 * @param driver
	 * @param elements
	 *            列表数据
	 * @param content
	 *            item 字符串内容
	 * @return
	 * @return boolean
	 */
	boolean checkDifference(List<WebElement> elements, String content);
	
}
