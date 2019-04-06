package cn.ehi.mobile.testsuite.page;

import cn.ehi.core.config.Configuration;
import cn.ehi.core.config.PlatformType;
import cn.ehi.core.operate.OperateBase;
import cn.ehi.mobile.driver.DriverManager;
import cn.ehi.mobile.operate.AndroidOperateBase;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

/**
 * @author 33053
 * @create 2019/3/16 10:40
 * <页面工厂>
 */
public class PageFactory {
    private static final Logger LOG = LoggerFactory.getLogger(PageFactory.class);
    private static OperateBase currentTestOperate;
    public static void initPageFactory(Configuration conf) {
        if (conf.getPlatformType() == PlatformType.Android) {
            AndroidDriver androidDriver = (AndroidDriver<WebElement>) DriverManager.getDriver();
            currentTestOperate = new AndroidOperateBase(androidDriver);
            return;
        }
    }

    public static Object getPage(Class pageClass) {
        //获取有参构造方法
        try {
            Constructor constructor = pageClass.getConstructor(OperateBase.class);
            return constructor.newInstance(currentTestOperate);
        } catch (Exception e) {
            LOG.error("当前类创建失败", e);
        }
        return null;
    }
}
