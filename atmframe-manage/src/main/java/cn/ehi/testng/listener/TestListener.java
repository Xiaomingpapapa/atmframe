package cn.ehi.testng.listener;

import cn.ehi.core.testbase.TestCaseBase;
import cn.ehi.manage.ITestProcess;
import cn.ehi.mobile.driver.DriverManager;
import cn.ehi.testng.entity.TestMethodResult;
import cn.ehi.testng.utils.TestNGUtil;
import cn.ehi.utils.ImageUtil;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.util.List;

/**
 * @author 33053
 * @create 2018/11/20
 * 〈TestNG用例运行生命周期监听器〉
 */
public class TestListener implements IInvokedMethodListener, ISuiteListener, IReporter {
    private static final Logger LOG = LoggerFactory.getLogger(TestListener.class);
    //外部测试用例管理对象
    private ITestProcess testProcess;


    public TestListener() {}

    public TestListener(ITestProcess testProcess) {
        this.testProcess = testProcess;
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        //获取调用test方法的类实例对象
        Object object = testResult.getInstance();
        if (object instanceof TestCaseBase) {
            if (method.isTestMethod()) {
                LOG.info("用例--" + TestNGUtil.getTestClassDescription(method.getTestMethod()) +
                        "--测试方法--" + TestNGUtil.getTestMethodDescription(method.getTestMethod()) + "开始测试");
            }
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        Object object = testResult.getInstance();
        if (object instanceof TestCaseBase) {
            TestCaseBase caseBase = (TestCaseBase) object;
            TestMethodResult methodResult = new TestMethodResult();
            methodResult.setTestResult(testResult);
            //检查方法是否抛出异常
            Throwable throwable = testResult.getThrowable();
            if(null != throwable){
                WebDriver webDriver = DriverManager.getDriver();
                String imagePath = null;

                //截图
                if(throwable instanceof NoSuchElementException){
                    imagePath = ImageUtil.screenshot(webDriver, "未找到元素", "");
                }else if(throwable instanceof TimeoutException){
                    imagePath = ImageUtil.screenshot(webDriver, "页面等待超时", "");
                }else if(throwable instanceof AssertionError){
                    imagePath = ImageUtil.screenshot(webDriver, "断言失败", "");
                }

                methodResult.setImagePath(imagePath);
            }
            if (method.isTestMethod()) {
                LOG.info("用例--" + TestNGUtil.getTestClassDescription(method.getTestMethod()) +
                        "--测试方法--" + TestNGUtil.getTestMethodDescription(method.getTestMethod()) + "测试完毕");
                testProcess.afterInvocation(caseBase, methodResult);
            }
        }


    }

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        testProcess.generateReport(xmlSuites, suites, outputDirectory);
    }

    @Override
    public void onStart(ISuite suite) {
        testProcess.onStart(suite);
    }

    @Override
    public void onFinish(ISuite suite) {
        testProcess.onFinish(suite);
    }
}
