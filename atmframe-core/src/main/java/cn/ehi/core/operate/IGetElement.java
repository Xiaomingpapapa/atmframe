package cn.ehi.core.operate;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface IGetElement {
	
	/**
	 * Appium通过 id、name、xpath规则查找元素，
	 * Selenium目前只支持xpath规则，其他方式会返回null，推荐使用{@link IGetElement#getElement(By)}
	 * 
	 * @Description:请全部是是用string的类型取代by类型
	 * @param by
	 * @return
	 * WebElement
	 * @exception:
	 * @author: 程文月
	 * @time:2018年3月15日 上午11:49:48
	 */
	WebElement getElement(String by);
	List<WebElement> getElements(String by);
	
	/**
	 * 在给定WebElement上查找元素<br>
	 * @Description: 请全部是是用string的类型取代by类型
	 * @param e
	 * @param by
	 * @return
	 * WebElement
	 * @exception:
	 * @author: 程文月
	 * @time:2018年3月23日 上午10:59:17
	 */
	WebElement getElement(WebElement e, String by);
	List<? extends WebElement> getElements(WebElement e, String by);
}
