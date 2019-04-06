package cn.ehi.core.operate;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;


public abstract class OperateBase<T extends RemoteWebDriver> implements IOperate{

	protected T driver;
	public OperateBase(T driver) {
		this.driver = driver;
	}

	public By createBy(String by) {
		if (by.startsWith("/")) {
			return By.xpath(by);
		}
		if (by.contains(":id/")) {
            return By.id(by);
        }

        return filterByName(by);
	}

    /**
     * 将 ByName 的查找转换为 ByXpath
     * @param by
     * @return
     */
    protected By filterByName(String by){
	    return By.xpath(".//*[@text = '"+ by + "']");
    }

    public WebElement getClassElement(String clazz) {
        return driver.findElementByClassName(clazz);
    }

    public List<WebElement> getClassElements(String clazz) {
        return driver.findElementsByClassName(clazz);
    }

    /**
     * @Description: 获取控件数组
     * @param id
     *            列表id
     * @param clazz
     *            itemClassName
     * @return List<WebElement>
     */
    public List<WebElement> getClassElements(String id, String clazz) {
        return driver.findElementById(id).findElements(By.className(clazz));
    }

}
