package cn.ehi.mobile.operate;

import cn.ehi.core.operate.OperateBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

public class BrowserOperateBase extends OperateBase<RemoteWebDriver> {

    public BrowserOperateBase(RemoteWebDriver driver) {
        super(driver);
    }

    public void clickView(String by) {

    }

    public void sendKeys(String by, String content) {

    }

    public void clear(String by) {

    }

    public void clickItem(String by, int position) {

    }

    public String getText(String by) {
        return null;
    }

    public String getItemText(String by, int position) {
        return null;
    }

    public boolean isTextExist(String text) {
        return false;
    }

    public boolean checkCell(String by) {
        return false;
    }

    public boolean check(List<WebElement> elements, String content) {
        return false;
    }

    public boolean checkDifference(List<WebElement> elements, String content) {
        return false;
    }

    public WebElement getElement(String by) {
        return null;
    }

    public List<WebElement> getElements(String by) {
        return null;
    }

    public WebElement getElement(WebElement e, String by) {
        return null;
    }

    public List<? extends WebElement> getElements(WebElement e, String by) {
        return null;
    }

    public void waitTime(int s) {

    }

    public void setGlobalWaitTime(int time) {

    }

    public WebElement waitAuto(String by, int time) {
        return null;
    }

    public WebElement waitAuto(String by) {
        return null;
    }

    public Integer waitOneElementShow(List<String> bys, int time, boolean needShot) {
        return null;
    }
}
