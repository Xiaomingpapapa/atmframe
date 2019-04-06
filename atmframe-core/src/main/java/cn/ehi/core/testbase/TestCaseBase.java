package cn.ehi.core.testbase;


import cn.ehi.core.operate.IOperate;
import org.openqa.selenium.remote.RemoteWebDriver;

public abstract class TestCaseBase {
    public abstract void onStart();

    public abstract void onBeforeClass();

    public abstract void onAfterClass();

    public abstract void onClose();
}
