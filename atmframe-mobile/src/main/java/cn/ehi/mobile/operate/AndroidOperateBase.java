package cn.ehi.mobile.operate;

import cn.ehi.core.operate.OperateBase;
import cn.ehi.mobile.operate.constant.AndroidClassName;
import cn.ehi.mobile.operate.constant.SelectorMatchType;
import com.google.common.base.Joiner;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import javax.xml.stream.Location;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AndroidOperateBase extends OperateBase<AndroidDriver> {
    private static final Logger LOG = LoggerFactory.getLogger(AndroidOperateBase.class);

    /**
     * 触摸操作类
     */
    private TouchAction touchAction;
    private MultiTouchAction multiTouchAction;

    /**
     * 操作常量
     */
    public final static int SWIPE_DEFAULT_PERCENT = 20;
    public final static String SWIP_UP = "UP",
            SWIP_DOWN = "DOWN", SWIP_LEFT = "LEFT", SWIP_RIGHT = "RIGHT";

    /**
     * 等待时间
     */
    public final static int COMMON_WAIT_TIME = 20;
    public final static int DEFAULT_WAIT_TIME = 60;
    public final static int MAX_WAIT_TIME = 120;

    /**
     * 定位策略标识
     */
    public final static String XPATH_PREFIX = "/";
    public final static String ID_PREFIX = ":id/";
    public final static String ANDROID_PREFIX = "android.widget.";

    /**
     * 测试 context
     */
    public final static String NATIVE_APP_CONTEXT = "NATIVE_APP";



    public AndroidOperateBase(AndroidDriver driver) {
        super(driver);
    }

    /**
     * 获取当前的测试 driver
     */
    public AndroidDriver getDriver() {
        return driver;
    }

    /**
     * 切换到 webview 上下文
     */
    public void switchToWebContext(String webview) {
        driver.context(webview);
    }

    /**
     * 切换到 native app 上下文
     */
    public void switchToNativeContext() {
        driver.context(NATIVE_APP_CONTEXT);
    }

    /**
     * 获取当前上下文
     */
    public String getCurrentContext() {
        return driver.getContext();
    }

    @Override
    public void clickView(String by) {
        WebElement element = getElement(by);
        if (element == null) {
            throw new NoSuchElementException("通过规则[" + by + "]，未找到元素！！");
        }
        element.click();
    }

    /**
     * 等待控件出现之后再进行点击
     * @param by
     */
    public void waitAndClickView(String by) {
        waitAuto(by);
        clickView(by);
    }

    /**
     * 获取指定控件的中心坐标
     * @param by
     * @return
     */
    public Point getViewCenterPoint(String by) {
        WebElement webElement = getElement(by);
        Point elementPoint = webElement.getLocation();
        Rectangle elementRect =  webElement.getRect();
        elementPoint.x += elementRect.width / 2;
        elementPoint.y += elementRect.height / 2;
        return elementPoint;
    }

    /**
     * 通过坐标点击元素
     * @param x
     * @param y
     */
    public void clickByLocation(int x, int y) {
        this.getTouch().tap(PointOption.point(x, y)).perform();
    }

    /**
     * 点击元素中间位置
     * @param webElement
     */
    public void clickElementCenter(WebElement webElement) {
        Rectangle rectangle = webElement.getRect();
        clickByLocation(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
    }

    @Override
    public void sendKeys(String by, String content) {
        WebElement element = getElement(by);
        if (element == null) {
            throw new NoSuchElementException("通过规则[" + by + "]，未找到元素！！");
        }
        element.clear();
        element.sendKeys(content);
    }

    @Override
    public void clear(String by) {
        WebElement element = getElement(by);
        element.clear();
    }

    @Override
    public void clickItem(String by, int position) {
        List<WebElement> distances = getElements(by);
        if (distances != null && distances.isEmpty()) {
            throw new RuntimeException("列表为空不能点击");
        } else {
            if (distances.size() >= position) {
                distances.get(position).click();
            } else {
                throw new RuntimeException("点击位置超出列表长度");
            }
        }
    }

    @Override
    public String getText(String by) {
        WebElement element = getElement(by);
        if(element == null)
            throw new NoSuchElementException("通过规则["+by+"]，未找到元素！！");
        return element.getText();
    }

    @Override
    public String getItemText(String by, int position) {
        List<WebElement> distances = getElements(by);
        if (distances != null && distances.isEmpty()) {
            throw new RuntimeException("列表为空获取失败");
        } else {
            if (distances.size() >= position) {
                return distances.get(position).getText();
            } else {
                throw new RuntimeException("列表为空获取失败");
            }
        }
    }

    @Override
    public boolean isTextExist(String text) {
        String str = driver.getPageSource();
        return str.contains(text);
    }

    @Override
    public boolean checkCell(String by) {
        WebElement webElement;
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(1000);
                webElement = getElement(by);
                if (webElement != null) {
                    return true;
                }
            } catch (Exception e) {
                if (i >= 3) {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean check(List<WebElement> elements, String content) {
        for (int i = 0; i < elements.size(); i++) {
            if (content.equals(elements.get(i).getText())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkDifference(List<WebElement> elements, String content) {
        for (int i = 0; i < elements.size(); i++) {
            if (!content.equals(elements.get(i).getText())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public WebElement getElement(String by) {
        if (StringUtils.isEmpty(by)) {
            return null;
        }
        if (by.startsWith(XPATH_PREFIX)) {
            return driver.findElementByXPath(by);
        }
        if (by.contains(ID_PREFIX)) {
            return driver.findElementById(by);
        }
        if (by.contains(ANDROID_PREFIX)) {
            return driver.findElementByClassName(by);
        }
        return driver.findElementByAndroidUIAutomator("text(\""+by+"\")");
    }

    @Override
    public List<WebElement> getElements(String by) {
        if (StringUtils.isEmpty(by)) {
            return null;
        }
        if (by.startsWith(XPATH_PREFIX)) {
            return driver.findElementsByXPath(by);
        }
        if (by.contains(ID_PREFIX)) {
            return driver.findElementsById(by);
        }
        if (by.contains(ANDROID_PREFIX)) {
            return driver.findElementsByClassName(by);
        }
        return driver.findElementsByAndroidUIAutomator("text(\""+by+"\")");
    }

    @Override
    public WebElement getElement(WebElement e, String by) {
        if (StringUtils.isEmpty(by)) {
            return null;
        }
        if (by.startsWith(XPATH_PREFIX)) {
            return e.findElement(By.xpath(by));
        }
        if (by.contains(ID_PREFIX)) {
            return e.findElement(By.id(by));
        }
        if (by.contains(ANDROID_PREFIX)) {
            return e.findElement(By.className(by));
        }
        return toAndroidElement(e).findElementByAndroidUIAutomator("text(\""+by+"\")");

    }

    @Override
    public List<? extends WebElement> getElements(WebElement e, String by) {
        if (StringUtils.isEmpty(by)) {
            return null;
        }
        if (by.startsWith(XPATH_PREFIX)) {
            return e.findElements(By.xpath(by));
        }
        if (by.contains(ID_PREFIX)) {
            return e.findElements(By.id(by));
        }
        if (by.contains(ANDROID_PREFIX)) {
            return e.findElements(By.className(by));
        }
        return toAndroidElement(e).findElementsByAndroidUIAutomator("text(\""+by+"\")");
    }

    @Override
    public void setGlobalWaitTime(int time) {
        driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
    }

    @Override
    public void waitTime(int s) {
        try {
            Thread.sleep(s * 1000);
        } catch (InterruptedException e) {
            LOG.error("线程休眠中断！！", e);
        }
    }

    @Override
    public WebElement waitAuto(String by) {
        //默认等待DEFAULT_WAIT_TIME秒
        return waitAuto(by, DEFAULT_WAIT_TIME);
    }

    @Override
    public WebElement waitAuto(String by, int time) {
        ExpectedCondition<WebElement> condition = driver -> {
            WebElement element = getElement(by);
            try {
                if (element != null && element.isEnabled()) {
                    return element;
                }
                return null;
            } catch (StaleElementReferenceException e) {
                return null;
            }
        };
        return waitCondition(condition, time,true);
    }


    @Override
    public Integer waitOneElementShow(List<String> bys, int time, boolean needShot) {
        List<ExpectedCondition<WebElement>> conditions = new ArrayList<>();
        for(String by : bys){
            conditions.add(ExpectedConditions.elementToBeClickable(createBy(by)));
        }
        ExpectedCondition<Integer> condition = new ExpectedCondition<Integer>() {
            @Override
            public Integer apply(WebDriver driver) {
                RuntimeException lastException = null;
                for (int i = 0; i < conditions.size(); i++) {
                    ExpectedCondition<WebElement> condition = conditions.get(i);
                    try {
                        WebElement el = condition.apply(driver);
                        if (el != null) {
							LOG.info(condition.toString());
                            return i;
                        }
                    } catch (RuntimeException e) {
						LOG.error(e.getMessage());
                        lastException = e;
                    }
                }
                if (lastException != null) {
                    throw lastException;
                }
                return -1;
            }

            @Override
            public String toString() {
                StringBuilder message = new StringBuilder("at least one condition to be valid: ");
                Joiner.on(" || ").appendTo(message, conditions);
                return message.toString();
            }
        };
        return waitCondition(condition, time, needShot);
    }

    public Integer waitOnToastShow(List<String> bys, int time) {
        List<ExpectedCondition<WebElement>> conditions = new ArrayList<>();
        for(String by : bys){
            conditions.add(ExpectedConditions.presenceOfElementLocated(By.xpath(by)));
        }
        ExpectedCondition<Integer> condition = new ExpectedCondition<Integer>() {
            @Override
            public Integer apply(WebDriver driver) {
                for (int i = 0; i < conditions.size(); i++) {
                    ExpectedCondition<WebElement> condition1 = conditions.get(i);
                    try {
                        WebElement el = condition1.apply(driver);
                        if (el != null) {
                            return i;
                        }
                    } catch (Exception e) {
                        LOG.error(e.getMessage());
                    }
                }
                return null;
            }
        };
        return waitCondition(condition, time, false);
    }

    private <T> T waitCondition(ExpectedCondition<T> condition ,int time,boolean needShot){
        try {
            WebDriverWait wait = new WebDriverWait(driver, time);
            return wait.until(condition);
        } catch (Throwable e) {
//			if(needShot)
            throw e;
        }
//		return null;
    }

    /**
     * @Description: 系统操作，如：返回键，home键等。。。
     * @param androidKeyCode
     *            void
     */
    public void pressKeyCode(int androidKeyCode) {
        driver.pressKeyCode(androidKeyCode);
    }

    public void goBackBy() {
        driver.pressKeyCode(AndroidKeyCode.BACK);
    }

    /**
     * 获取触摸实例
     *
     * @return
     */
    public TouchAction getTouch() {
        if (driver == null) {
            printlnException("单点触摸时候driver为空");
            return null;
        } else {
            if (touchAction == null) {
                return new TouchAction(driver);
            } else {
                return touchAction;
            }

        }
    }

    /**
     * 获取多点触摸实例
     *
     * @return
     */
    public MultiTouchAction getMultiTouch() {
        if (driver == null) {
            printlnException("多点触摸时候driver为空");
            return null;
        } else {
            if (multiTouchAction == null) {
                return new MultiTouchAction(driver);
            } else {
                return multiTouchAction;
            }

        }
    }

    /**
     * 获取当前界面的所有EditText，并依次输入内容
     *
     * @param str
     *            要输入的数组
     */
    public void inputManyText(String... str) {
        List<WebElement> textFieldsList = null;
        textFieldsList = driver.findElementsByClassName(AndroidClassName.EditText);
        if (textFieldsList == null) {
            printlnException("未获取到指定组件");
        }
        for (int i = 0; i < str.length; i++) {
            textFieldsList.get(i).click();
            textFieldsList.get(i).clear();// 清除内容
            textFieldsList.get(i).sendKeys(str[i]);
        }
    }

    public void sendKeyboardNumber(int number) {
        driver.pressKeyCode(numberToAndroidKeyCode(number));
    }


    private int numberToAndroidKeyCode(int number) {
        switch (number) {
            case 0:
                return AndroidKeyCode.KEYCODE_0;
            case 1:
                return AndroidKeyCode.KEYCODE_1;
            case 2:
                return AndroidKeyCode.KEYCODE_2;
            case 3:
                return AndroidKeyCode.KEYCODE_3;
            case 4:
                return AndroidKeyCode.KEYCODE_4;
            case 5:
                return AndroidKeyCode.KEYCODE_5;
            case 6:
                return AndroidKeyCode.KEYCODE_6;
            case 7:
                return AndroidKeyCode.KEYCODE_7;
            case 8:
                return AndroidKeyCode.KEYCODE_8;
            case 9:
                return AndroidKeyCode.KEYCODE_9;
                default:
                    throw new RuntimeException("请输入数字");

        }
    }

    /**
     * 点击屏幕中间
     */
    public void press() {
        press(getScreenWidth() / 2, getScreenHeight() / 2);
    }

    /**
     * 点击某个控件
     *
     * @param ae
     *            要点击的控件
     */
    public void press(WebElement ae) {
        Point point =  ae.getLocation();
        try {
            getTouch().tap(PointOption.point(point.x, point.y)).perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击某个控件
     *
     * @param by
     *            通过by要点击的控件
     */
    public void press(String by) {
        WebElement ae = getElement(by);
        press(ae);
    }

    /**
     * 点击某个坐标
     *
     * @param x
     * @param y
     */
    public void press(int x, int y) {
        try {
            getTouch().tap(PointOption.point(x, y));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 长按某个控件
     *
     * @param ae
     *            要点击的控件
     */
    public void longPress(WebElement ae) {
        Point point =  ae.getLocation();
        try {
            getTouch().longPress(PointOption.point(point.x, point.y)).release().perform();
        } catch (Exception e) {
//			println("长按点击元素错误" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 长按某个控件
     *
     * @param by
     *            通过一种方式获取控件长按
     *
     *            要点击的控件
     */
    public void longPress(String by) {
        WebElement ae = getElement(by);
        longPress(ae);
    }

    /**
     * 长按某个坐标
     *
     * @param x
     * @param y
     */
    public void longPress(int x, int y) {
        try {
            getTouch().longPress(PointOption.point(x, y)).release().perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*********************** 触控事件 end ***************/

    /********************* ：TODO 滑动事件相关start ***************/
    /**
     * 基于屏幕百分比进行滑动，滑动数次
     *
     * @param 可以输入四个方向
     *            ：Left，Right，Top，Down 关联 DataString
     * @param 滑动次数
     *            ：number
     */
    public void scrollTo(String direction, int number) {
        int x = driver.manage().window().getSize().width;
        int y = driver.manage().window().getSize().height;
        waitTime(2);
        for (int i = 0; i < number; i++) {
            try {
                if (SWIP_LEFT.equals(direction)) {
                    swipe(x / 4 * 3, y / 2, x / 4, y / 2, 500);
                } else if (SWIP_RIGHT.equals(direction)) {
                    swipe(x / 4, y / 2, x / 4 * 3, y / 2, 500);
                } else if (SWIP_UP.equals(direction)) {
                    swipe(x / 2, y / 4 * 3, x / 2, y / 4, 500);
                } else if (SWIP_DOWN.equals(direction)) {
                    swipe(x / 2, y / 4, x / 2, y / 4 * 3, 500);
                }
            } catch (Exception e) {
                printlnException("输入错误，参数只能是Left，Right，Up，Down四种");
            }
            waitTime(2);
        }
    }

    public void swipe(int startX,int startY,int endX,int endY,int direction){
        TouchAction swipe = new TouchAction(driver).press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(direction)))
            .moveTo(PointOption.point(endX, endY))
                .release();
        swipe.perform();
}
    /**
     * 基于屏幕滑动到某一控件,并点击此控件 暂时关闭
     *
     * @param by
     * @param direction
     *            滑动方向
     */
    public void scrollToBy(String by, String direction) {
        for (int i = 0; i < 3; i++) {
            if (checkCell(by)) {
                getElement(by).click();
                break;
            } else {
                scrollTo(direction, 1);
            }
        }
    }

    /**
     * 向上滑动20%
     *
     * @param during
     *            滑动持续多久
     */
    public void swipeToUp(int during) {
        swipeToUp(during, SWIPE_DEFAULT_PERCENT);
    }

    /**
     * 向上滑动，点击开始位置为屏幕中心,左上角原点
     *
     * @param during
     *            持续时间
     * @param percent
     *            百分比
     */
    public void swipeToUp(int during, int percent) {
        int width = getScreenWidth();
        int height = getScreenHeight();
        swipe(width / 2, height * (percent - 1) / percent, width / 2,
                height / percent, during);

//		driver.swipe(900, 1550, 900, 2DEFAULT_WAIT_TIME, during);
        // System.out.print("宽："+width+" 高: "+height+" startx: "+width/2+"
        // starty: "+height*(percent - 1)/percent);
    }

    public void swipeToDown(int during) {
        swipeToDown(during, SWIPE_DEFAULT_PERCENT);
    }

    /**
     * 向下滑动，
     *
     * @param during
     *            滑动时间
     */
    public void swipeToDown(int during, int percent) {
        int width = getScreenWidth();
        int height = getScreenHeight();
        swipe(width / 2, height / percent, width / 2, height * (percent - 1) / percent, during);
    }

    public void swipeToLeft(int during) {
        swipeToLeft(during, SWIPE_DEFAULT_PERCENT);
    }

    /**
     * 向左滑动，
     *
     * @param during
     *            滑动时间
     * @param percent
     *            位置的百分比
     */
    public void swipeToLeft(int during, int percent) {
        int width = getScreenWidth();
        int height = getScreenHeight();
        swipe(width * (percent - 1) / percent, height / 2, width / percent, height / 2, during);
    }

    public void swipeToRight(int during) {
        swipeToRight(during, SWIPE_DEFAULT_PERCENT);
    }

    /**
     * 向右滑动，
     *
     * @param during
     *            滑动时间
     * @param percent
     *            位置的百分比，2-10， 例如3就是 从1/3滑到2/3
     */
    public void swipeToRight(int during, int percent) {
        int width = getScreenWidth();
        int height = getScreenHeight();
        swipe(width / percent, height / 2, width * (percent - 1) / percent, height / 2, during);
    }


    /**
     * 在控件上滑动
     *
     * @param element
     *            要滑动的控件
     * @param direction
     *            方向，事件不设置默认1秒
     */
    public void swipOnElement(WebElement element, String direction) {
        swipOnElement(element, direction, 1000); // 不设置时间就为2秒
    }

    /**
     * 在某一个控件上滑动
     *
     * @param element
     *            在那个元素上滑动
     * @param direction
     *            方向，UP DOWN LEFT RIGHT
     */
    public void swipOnElement(WebElement element, String direction, int duration) {
        // 获取元素的起初xy，在左上角
        int x = element.getLocation().getX();
        int y = element.getLocation().getY();
        // 获取元素的宽高
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();

        switch (direction) {
            case SWIP_UP:
                int startX = x + width / 2;
                // 在4/5的底部的中间向上滑动
                swipe(startX, y + height * 4 / 5, startX, y + height / 5, duration);
                break;
            case SWIP_DOWN:
                startX = x + width / 2;
                // 在4/5的底部的中间向上滑动
                swipe(startX, y + height / 5, startX, y + height * 4 / 5, duration);
                break;

            case SWIP_LEFT:
                int startY = y + width / 2;
                swipe(x + width * 4 / 5, startY, x + width / 5, startY, duration);
                break;
            case SWIP_RIGHT:
                startY = y + width / 2;
                swipe(x + width / 5, startY, x + width * 4 / 5, startY, duration);
                break;
        }
    }

    /**
     * 在某个方向上滑动
     * @param direction
     *            方向，UP DOWN LEFT RIGHT
     * @param duration
     *            持续时间
     */
    public void swip(String direction, int duration) {
        switch (direction) {
            case SWIP_UP:
                swipeToUp(duration);
                break;
            case SWIP_DOWN:
                swipeToDown(duration);
                break;
            case SWIP_LEFT:
                swipeToLeft(duration);
                break;
            case SWIP_RIGHT:
                swipeToRight(duration);
                break;
        }
    }

    /**
     * 在某个方向上滑动
     * @param direction
     * @param duration
     * @param precent
     */
    public void swip(String direction, int duration, int precent) {
        switch (direction) {
            case SWIP_UP:
                swipeToUp(duration, precent);
                break;
            case SWIP_DOWN:
                swipeToDown(duration, precent);
                break;
            case SWIP_LEFT:
                swipeToLeft(duration, precent);
                break;
            case SWIP_RIGHT:
                swipeToRight(duration, precent);
                break;
        }
    }

    /**
     * 在指定次数的条件下，某个方向滑动，直到这个元素出现
     *
     * @param by
     *            控件
     * @param direction
     *            方向，UP DOWN LEFT RIGHT
     * @param duration
     *            滑动一次持续时间
     * @param maxSwipNum
     *            最大滑动次数
     */
    public void swipUtilElementAppear(String by, String direction, int duration, int maxSwipNum) {
        int i = maxSwipNum;
        Boolean flag = true;
        while (flag) {
            try {
                if (i <= 0) {
                    flag = false;
                }
                WebElement webElement = getElement(by);
                if (webElement != null) {
                    flag = false;
                }
            } catch (Exception e) {
                i--;
                swip(direction, duration);
            }
        }
    }

    /**
     * 在某个方向滑动直到这个元素出现
     *
     * @param by
     *            控件
     * @param direction
     *            方向，UP DOWN LEFT RIGHT
     * @param duration
     *            滑动一次持续时间
     */
    public WebElement swipUtilElementAppear(String by, String direction, int duration) {
        Boolean flag = true;
        WebElement webElement = null;
        do {
            try {
                webElement = getElement(by);
                if (webElement != null) {
                    flag = false;
                }
            } catch (Exception e) {
                swip(direction, duration);
            }
        } while (flag);
        return webElement;
    }


    public WebElement swipUtilElementAppear(String by, String direction, int duration, int precent, int maxSwipNum) {
        int i = maxSwipNum;
        Boolean flag = true;
        WebElement webElement = null;
        do {
            try {
                //判断放在上面，防止异常终止
                if (i <= 0) {
                    flag = false;
                }
                webElement = getElement(by);
                if (webElement != null) {
                    flag = false;
                }
            } catch (Exception e) {
                i--;
                swip(direction, duration, precent);
            }
        } while (flag);
        return webElement;
    }

    /**
     * 控件内上下滑动(测试方法)
     *
     * @param step
     *            测试步骤
     * @param by
     *            控件定位方式
     * @param direction
     *            滑动方向 UP DOWN
     */
    public void swipeControl(String by, String direction) {
        // 获取控件开始位置的坐标轴
        Point start = getElement(by).getLocation();
        int startX = start.x;
        int startY = start.y;

        // 获取控件坐标轴差
        Dimension q = getElement(by).getSize();
        int x = q.getWidth();
        int y = q.getHeight();
        // 计算出控件结束坐标
        int endX = x + startX;
        int endY = y + startY;

        // 计算中间点坐标
        int centreX = (endX + startX) / 2;
        int centreY = (endY + startY) / 2;

        switch (direction) {
            // 向上滑动
            case SWIP_UP:
                swipe(centreX, centreY + 30, centreX, centreY - 40, 500);
                break;
            // 向下滑动
            case SWIP_DOWN:
                swipe(centreX, centreY - 30, centreX, centreY + 40, 500);
                break;
            // 向左滑动
            case SWIP_LEFT:
                swipe(centreX + 40, centreY, centreX - 40, centreY, 500);
                break;
            // 向右滑动
            case SWIP_RIGHT:
                swipe(centreX - 40, centreY, centreX + 40, centreY, 500);
                break;
        }

    }

    public void swipeControl(String by, String direction, int offsetOld, int offsetNew) {
        // 获取控件开始位置的坐标轴
        Point start = getElement(by).getLocation();
        int startX = start.x;
        int startY = start.y;

        // 获取控件坐标轴差
        Dimension q = getElement(by).getSize();
        int x = q.getWidth();
        int y = q.getHeight();
        // 计算出控件结束坐标
        int endX = x + startX;
        int endY = y + startY;

        // 计算中间点坐标
        int centreX = (endX + startX) / 2;
        int centreY = (endY + startY) / 2;

        switch (direction) {
            // 向上滑动
            case SWIP_UP:
                swipe(centreX, centreY + offsetOld, centreX, centreY - offsetNew, 500);
                break;
            // 向下滑动
            case SWIP_DOWN:
                swipe(centreX, centreY - offsetOld, centreX, centreY + offsetNew, 500);
                break;
            // 向左滑动
            case SWIP_LEFT:
                swipe(centreX + offsetOld, centreY, centreX - offsetNew, centreY, 500);
                break;
            // 向右滑动
            case SWIP_RIGHT:
                swipe(centreX - offsetOld, centreY, centreX + offsetNew, centreY, 500);
                break;
        }

    }

    /**
     * 控件内上下滑动(测试方法)
     *
     * @param step
     *            测试步骤
     * @param by
     *            控件定位方式
     * @param direction
     *            滑动方向 UP DOWN
     * @param offSet
     *            偏移量
     */
    public void swipeControl(String by, String direction, int offSet) {
        // 获取控件开始位置的坐标轴
        Point start = getElement(by).getLocation();
        int startX = start.x;
        int startY = start.y;

        // 获取控件坐标轴差
        Dimension q = getElement(by).getSize();
        int x = q.getWidth();
        int y = q.getHeight();
        // 计算出控件结束坐标
        int endX = x + startX;
        int endY = y + startY;

        // 计算中间点坐标
        int centreX = (endX + startX) / 2;
        int centreY = (endY + startY) / 2;

        switch (direction) {
            // 向上滑动
            case SWIP_UP:
                swipe(centreX, centreY + 30, centreX + offSet, centreY - 40, 500);
                break;
            // 向下滑动
            case SWIP_DOWN:
                swipe(centreX, centreY - 30, centreX + offSet, centreY + 40, 500);
                break;
            // 向左滑动
            case SWIP_LEFT:
                swipe(centreX + 40, centreY, centreX - 40, centreY + offSet, 500);
                break;
            // 向右滑动
            case SWIP_RIGHT:
                swipe(centreX - 40, centreY, centreX + 40, centreY + offSet, 500);
                break;
        }

    }

    public void longPressSliding() {
        int screenWidth = getScreenWidth();
        int screentHeight = getScreenHeight();
        //中心坐标作为起点
        int centreX = screenWidth / 2;
        int centreY = screentHeight / 2;
        this.getTouch().longPress(PointOption.point(centreX, centreY))
                .moveTo(PointOption.point(centreX, centreY + 50))
                .moveTo(PointOption.point(centreX + 40, centreY + 50))
                .release().perform();
    }

    public WebElement swipeControlUtilElementAppear(String by, String direction, String appearElement) {
        Boolean flag = true;
        WebElement webElement = null;
        do {
            try {
                webElement = driver.findElement(createBy(appearElement));
                if (webElement != null) {
                    flag = false;

                }
            } catch (Exception e) {
                swipeControl(by, direction);
            }
        } while (flag);
        return webElement;
    }

    public WebElement swipeControlUtilElementAppear(String by, String direction, String appearElement, int offsetOld,
                                                    int offsetNew) {
        Boolean flag = true;
        WebElement webElement = null;
        do {
            try {
                webElement = getElement(appearElement);
                if (webElement != null) {
                    flag = false;
                }
            } catch (Exception e) {
                swipeControl(by, direction, offsetOld, offsetNew);
            }
        } while (flag);
        return webElement;
    }
    /**
     * (测试方法)
     *
     * @param by
     * @param direction
     */
    public void swipeControl2(String by, String direction) {
        WebElement swipeElement = getElement(by);
        // 获取屏幕高度和宽度
        int screen_width = driver.manage().window().getSize().width;// screen
        // width
        int screen_height = driver.manage().window().getSize().height; // screen
        // height
        // 获取组件高度和宽度
        int element_width = swipeElement.getSize().getWidth(); // element width
        int element_height = swipeElement.getSize().getHeight(); // element
        // height
        // 获取组件坐标
        int x_location = swipeElement.getLocation().getX(); // left x location
        // of element
        int y_location = swipeElement.getLocation().getY(); // top y location of
        // element

        int x_left = x_location + element_width * 1 / 8;// left x location of
        // element
        int y_top = y_location + element_height * 1 / 8;// top y location of
        // element

        int x_center = x_location + element_width / 2; // center x location of
        // element
        int y_center = y_location + element_height / 2; // center y location of
        // element

        int x_right = x_location + element_width * 7 / 8;// right x location of
        // element
        int y_bottom = y_location + element_height * 7 / 8;// bottom y location
        // of element

        if (direction.equals("左") || direction.equals("left")) {
            swipe(x_right, y_center, screen_width * 1 / 20, y_center, 1000);
        } else if (direction.equals("右") || direction.equals("right")) {
            swipe(x_left, y_center, screen_width * 19 / 20, y_center, 1000);
        } else if (direction.equals("上") || direction.equals("up")) {
            swipe(x_center, y_bottom, x_center, screen_height * 1 / 20, 1000);
        } else if (direction.equals("下") || direction.equals("down")) {
            swipe(x_center, y_top, x_center, screen_height * 19 / 20, 1000);
        }

    }
    /************************** 滑动事件相关end ***************/

    /********************** ：TODO 系统方法 获取屏幕属性start **************/
    /**
     * 获取屏幕的宽高
     *
     * @return 返回宽高的数组
     */
    public int[] getScreen() {
        int width = driver.manage().window().getSize().getWidth();
        int height = driver.manage().window().getSize().getHeight();
        return new int[] { width, height };
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getScreenWidth() {
        return driver.manage().window().getSize().getWidth();
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public int getScreenHeight() {
        return driver.manage().window().getSize().getHeight();
    }

    //下拉屏幕进行刷新
    public void dropDownScreenRefresh() {
        //从屏幕中心开始下拉进行刷新
        int startX = getScreenWidth() / 2;
        int startY = getScreenHeight() / 2;
        //默认进行3次刷新
        for (int i = 0; i < 3; i++) {
            this.getTouch().longPress(PointOption.point(startX, startY)).moveTo(PointOption.point(startX, startY + getScreenHeight() / 3)).release().perform();
            waitTime(2);
        }
    }



    /********************** 系统方法 获取屏幕属性end **************/

    /*********** ：TODO 断言部分start *****************/
    /**
     * 判断相等并且失败后截图
     * 所有截图操作在统一异常处理进行
     * @param actual
     *            需要判断的对象
     * @param expected
     *            期望值
     * @param screenShotName
     *            截图文件名
     */
    public void assertEqualsAndScreenShot(Object actual, Object expected,
                                          String condition, String screenShotName) {
        try {
            Assert.assertEquals(actual, expected, "测试用例条件：" + condition);
        } catch (Error e) {

            //screenshotDefault(screenShotName);
            throw e;
        }
    }

    /**
     * 所有截图操作在统一异常处理进行
     * @Description:TODO
     * @param actual
     * @param expected
     * @param condition
     * void
     * @exception:
     * @author: 程文月
     * @time:2018年3月16日 上午10:33:36
     */
    public void assertEqualsAndScreenShot(Object actual, Object expected,
                                          String condition) {
        try {
            Assert.assertEquals(actual, expected, "测试用例条件：" + condition);
        } catch (Error e) {

//			screenshotDefault(condition+Util.getCurrentDateTime());
//			clickBack();
//			screenshotDefault("返回之后的截图"+Util.getCurrentDateTime());
            throw e;
        }
    }

    /**
     * 判断不为空并且失败后截图
     *
     * 所有截图操作在统一异常处理进行
     * @param actual
     *            需要判断的对象
     * @param expected
     *            期望值
     * @param screenShotName
     *            截图文件名
     */
    public void assertNotNullAndScreenShot(Object actual, String condition,
                                           String screenShotName) {
        try {
            Assert.assertNotNull(actual, condition);
        } catch (Error e) {
//			screenshotDefault(screenShotName+Util.getCurrentDateTime());
            throw e;
        }
    }

    /**
     * 所有截图操作在统一异常处理进行
     * @param actual
     *            实际值
     * @param condition
     *            条件以及截图名字，都为此值
     */
    public void assertNotNullAndScreenShot(Object actual, String condition) {
        try {
            Assert.assertNotNull(actual, condition);
        } catch (Error e) {
//			screenshotDefault(condition+Util.getCurrentDateTime());
            throw e;
        }
    }

    /************** 断言部分end *****************/



    /**
     *
     * 抛出一个运行时异常，打印指定文字
     *
     * @param str
     *            要打印的字符
     */
    public <T> void printlnException(T str) {
//        imageUtils.screenshotDefault(driver,"页面出现超时");
//		imageUtils.screenshotDefault("发生系统异常" + Util.getCurrentDateTime());
        if (!TextUtils.isEmpty(String.valueOf(str))) {
            throw new RuntimeException(String.valueOf(str));
        } else {
            throw new RuntimeException();
        }
    }
    /*********** 原TestCaseBase中的方法  start**************/


    /**
     * 使用 Android UIAutomator，判断元素中是否包含某个字符
     * @param text
     * @return
     */
    public boolean isContainText(String text) {
        WebElement title = getElement(text);
        return title == null ? false : true;
    }

    /**
     * 在规定时间内检查 toast 是否存在
     * @param by
     * @param waitTime
     * @return
     */
    public String checkToast(String by, int waitTime) {

        WebElement webElement = null;
        try {
            webElement = waitCondition(ExpectedConditions.presenceOfElementLocated(By.xpath(by)), waitTime,false);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        if (null != webElement) {
            return webElement.getText();
        }
        return null;
    }

    /**
     * 等待指定元素消失
     * @param by
     * @param waitTime
     */
    public void waitForElementDisappear(String by) {
        waitCondition(ExpectedConditions.invisibilityOfElementLocated(By.xpath(by)), DEFAULT_WAIT_TIME,false);
    }

    /**
     * 定位ListView中的元素
     * @param listViewNumber
     * @param selectorMatchType
     * @param by
     * @return
     */
    public WebElement getListViewElement(int listViewNumber, String selectorMatchType, String by) {
        WebElement webElement = null;
        String selectorRule = generateUiSelectorRule(selectorMatchType, by);
        String rule = "new UiCollection(new UiSelector().className(\"" + AndroidClassName.ListView + "\").instance(" + listViewNumber + ")).getChild(" + selectorRule + ")";
        try {
            webElement = driver.findElementByAndroidUIAutomator(rule);
        } catch (Exception e) {
            throw new NoSuchElementException("通过规则["+by+"]，未找到元素！！");
        }
        return webElement;
    }

    /**
     * 使用UiScrollable对象查找滚动容器中的元素（会滚动查找元素）
     * @param scrollViewNumber
     * @param selectorMatchType
     * @param by
     * @return
     */
    public WebElement getScrollableContainerElement(int scrollViewNumber,String selectorMatchType ,String by) {
        WebElement webElement = null;
        String selectorRule = generateUiSelectorRule(selectorMatchType, by);
        String rule = "new UiScrollable(new UiSelector().scrollable(true).instance(" + scrollViewNumber + ")).scrollIntoView(" + selectorRule + ")";
        try {
            webElement = driver.findElementByAndroidUIAutomator(rule);
        } catch (Exception e) {
            throw new NoSuchElementException("通过规则["+by+"]，未找到元素！！");
        }
        return webElement;
    }

    /**
     * 使用UiScrollable对象查找滚动容器中的元素（会滚动查找元素,相同条件下有多个元素时按顺序选择元素）
     * @param scrollViewNumber
     * @param selectorMatchType
     * @param by
     * @param index
     * @return
     */
    public WebElement getScrollableContainerElementByInstance(int scrollViewNumber,String selectorMatchType ,String by, int index) {
        WebElement webElement = null;
        String selectorRule = generateUiSelectorRule(selectorMatchType, by, index);
        String rule = "new UiScrollable(new UiSelector().scrollable(true).instance(" + scrollViewNumber + ")).scrollIntoView(" + selectorRule + ")";
        try {
            webElement = driver.findElementByAndroidUIAutomator(rule);
        } catch (Exception e) {
            throw new NoSuchElementException("通过规则["+by+"]，未找到元素！！");
        }
        return webElement;
    }

    public WebElement getScrollableContainerElementByInstance(String scrollViewId,String selectorMatchType ,String by, int index) {
        WebElement webElement = null;
        String selectorRule = generateUiSelectorRule(selectorMatchType, by, index);
        String rule = "new UiScrollable(new UiSelector().resourceId(" + scrollViewId +").instance(0)).scrollIntoView(" + selectorRule + ")";
        try {
            webElement = driver.findElementByAndroidUIAutomator(rule);
        } catch (Exception e) {
            throw new NoSuchElementException("通过规则["+by+"]，未找到元素！！");
        }
        return webElement;
    }


    /**
     * 获取UICollection集合中的元素（当前状态集合中可见的元素,通过特征和索引位置获取）
     * @param scrollViewNumber
     * @param selectorMatchType
     * @param by
     * @param index
     * @return
     */
    public WebElement getScrollViewSpecialfyChildByInstance(int scrollViewNumber, String selectorMatchType, String by, int index) {
        WebElement webElement = null;
        String selectorRule = generateUiSelectorRule(selectorMatchType, by);
        String rule = "new UiScrollable(new UiSelector().scrollable(true).instance("+ scrollViewNumber +")).getChildByInstance(" + selectorRule + "," + index + ")";
        try {
            webElement = driver.findElementByAndroidUIAutomator(rule);
        } catch (Exception e) {
            throw new NoSuchElementException("通过规则["+by+"]，未找到元素！！");
        }
        return webElement;
    }

    /**
     * 获取容器中所有符合条件的元素
     * @param scrollViewNumber
     * @param selectorMatchType
     * @param by
     * @return
     */
    public List<WebElement> getScrollableContainerElements(int scrollViewNumber, String selectorMatchType, String by){
        List<WebElement> webElements = null;
        String selectorRule = generateUiSelectorRule(selectorMatchType, by);
        String rule = "new UiScrollable(new UiSelector().scrollable(true).instance(" + scrollViewNumber + ")).scrollIntoView(" + selectorRule + ")";
        try {
            webElements = driver.findElementsByAndroidUIAutomator(rule);
        } catch (Exception e) {
            throw new NoSuchElementException("通过规则["+by+"]，未找到元素！！");
        }
        return webElements;
    }


    /**
     * 获取容器中所有符合条件的元素
     * @param scrollViewNumber
     */
    public void scrollViewFlingToEnd(int scrollViewNumber) {
        int maxSwip = 5;
        WebElement webElement = null;
        String rule = "new UiScrollable(new UiSelector().className("+ AndroidClassName.ScrollView +").instance("+ scrollViewNumber +")).flingToEnd("+ maxSwip +")";
        try {
            webElement = driver.findElementByAndroidUIAutomator(rule);
        } catch (Exception e) {
            LOG.info("无法操作元素");
        }
    }

    /**
     * 滑动到容器底部
     * @param scrollViewNumber
     * @param isScrollView
     */
    public void scrollViewFlingToEnd(int scrollViewNumber, boolean isScrollView) {
        int maxSwip = 5;
        WebElement webElement = null;
        String rule = "";
        if (isScrollView) {
            rule = "new UiScrollable(new UiSelector().className("+ AndroidClassName.ScrollView +").instance("+ scrollViewNumber +")).flingToEnd("+ maxSwip +")";
        } else {
            rule = "new UiScrollable(new UiSelector().scrollable(true).instance("+ scrollViewNumber +")).flingToEnd("+ maxSwip +")";
        }
        try {
            webElement = driver.findElementByAndroidUIAutomator(rule);
        } catch (Exception e) {
            LOG.info("无法操作元素");
        }
    }

    /**
     * 滑动到容器顶部
     * @param scrollViewNumber
     */
    public void scrollViewFlingToTop(int scrollViewNumber) {
        int maxSwip = 5;
        WebElement webElement = null;
        String rule = "new UiScrollable(new UiSelector().className("+ AndroidClassName.ScrollView +").instance("+ scrollViewNumber +")).flingToBeginning("+ maxSwip +")";
        try {
            webElement = driver.findElementByAndroidUIAutomator(rule);
        } catch (Exception e) {
            LOG.info("无法操作元素");
        }
    }

    /**
     * 生成UiSelector规则字符串
     * @param selectorMatchType
     * @param by
     * @return
     */
    public String generateUiSelectorRule(String selectorMatchType, String by) {
        String rule = "new UiSelector().";
        switch (selectorMatchType) {
            case SelectorMatchType.TEXT:
                rule += "text(\"" + by + "\")";
                break;
            case SelectorMatchType.TEXT_CONTAINS:
                rule += "textContains(\"" + by + "\")";
                break;
            case SelectorMatchType.CLASS_NAME:
                rule += "className(\"" + by + "\")";
                break;
            case SelectorMatchType.PACKAGE_NAME:
                rule += "packageName(\"" + by + "\")";
                break;
            case SelectorMatchType.ID:
                rule += "resourceId(\"" + by + "\")";
                break;
            default:
                rule += "text(\"" + by + "\")";
        }
        return rule;
    }

    /**
     * 生成UiSelector规则字符串，指定instance参数（当符合条件的实例不止一个时，指定index的实例）
     * @param selectorMatchType
     * @param by
     * @param index
     * @return
     */
    public String generateUiSelectorRule(String selectorMatchType,String by, int index) {
        String rule = "new UiSelector().";
        switch (selectorMatchType) {
            case SelectorMatchType.TEXT:
                rule += "text(\"" + by + "\")";
                break;
            case SelectorMatchType.TEXT_CONTAINS:
                rule += "textContains(\"" + by + "\")";
                break;
            case SelectorMatchType.CLASS_NAME:
                rule += "className(\"" + by + "\")";
                break;
            case SelectorMatchType.PACKAGE_NAME:
                rule += "packageName(\"" + by + "\")";
                break;
            case SelectorMatchType.ID:
                rule += "resourceId(\"" + by + "\")";
                break;
            default:
                rule += "text(\"" + by + "\")";
        }

        rule += ".instance(" + index + ")";

        return rule;
    }

    /**
     * 使用UiSelector对象查找元素
     * @param selectorMatchType
     * @param by
     * @return
     */
    public WebElement getElementByUIAutomator(String selectorMatchType, String by) {
        return driver.findElementByAndroidUIAutomator(generateUiSelectorRule(selectorMatchType, by));
    }

    /**
     * 判断元素是否被选中
     * @param webElement
     * @return
     */
    public boolean isChecked(WebElement webElement) {
        return webElement.getAttribute("checked").equalsIgnoreCase("true") ? true : false;
    }

    /**
     * 判断元素是否被选中
     * @param by
     * @return
     */
    public boolean isChecked(String by) {
        WebElement webElement = getElement(by);
        return webElement.getAttribute("checked").equalsIgnoreCase("true") ? true : false;
    }

    private AndroidElement toAndroidElement(WebElement e) {
        return (AndroidElement) e;
    }




}
