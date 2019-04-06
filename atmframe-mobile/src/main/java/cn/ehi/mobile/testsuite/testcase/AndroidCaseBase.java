package cn.ehi.mobile.testsuite.testcase;

import cn.ehi.core.config.Configuration;
import cn.ehi.core.testbase.TestCaseBase;
import cn.ehi.mobile.driver.DriverManager;
import cn.ehi.mobile.testsuite.page.PageFactory;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

public class AndroidCaseBase extends TestCaseBase {
    private static final Logger LOG = LoggerFactory.getLogger(AndroidCaseBase.class);

    @Override
    @BeforeSuite
    public void onStart() {
        LOG.info("单个套件测试开始，初始化appium driver");
        Configuration conf = new Configuration();
        DriverManager.initDriver(conf);
        PageFactory.initPageFactory(conf);
    }

    @Override
    public void onBeforeClass() {
    }

    @Override
    public void onAfterClass() {

    }

    @Override
    @AfterSuite(alwaysRun = true)
    public void onClose() {
        LOG.info("单个套件测试完毕，关闭 appium driver对象，关闭 appium server");
        RemoteWebDriver currentWebDriver = DriverManager.getDriver();
        if (null != currentWebDriver) {
            currentWebDriver.quit();
        }
    }
}
