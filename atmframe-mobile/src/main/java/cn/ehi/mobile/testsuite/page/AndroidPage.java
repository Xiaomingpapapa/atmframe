package cn.ehi.mobile.testsuite.page;

import cn.ehi.core.operate.OperateBase;
import cn.ehi.core.testbase.Page;
import cn.ehi.core.testbase.TestCaseBase;
import cn.ehi.mobile.operate.AndroidOperateBase;
import cn.ehi.utils.ImageUtil;
import io.appium.java_client.android.AndroidDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AndroidPage extends Page<AndroidDriver> {
    private static final Logger LOG = LoggerFactory.getLogger(AndroidPage.class);
    protected AndroidOperateBase androidOperateBase;

    public AndroidPage(OperateBase operateBase) {
        this.androidOperateBase = (AndroidOperateBase)operateBase;
    }

    /**
     * 记录异常并且截图
     * @param message
     */
    public void logErrorAndScreenshot(String message) {
        LOG.error(message);
        ImageUtil.screenshotDefault(androidOperateBase.getDriver(), message);
    }
}
