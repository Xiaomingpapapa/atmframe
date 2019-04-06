package cn.ehi.manage;

import cn.ehi.core.testbase.TestCaseBase;
import cn.ehi.testng.entity.TestMethodResult;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.util.List;

/**
 * @author 33053
 * @create 2018/11/20
 * 〈测试结果管理平台接口〉
 */
public interface ITestProcess {

    void beforeInvocation(TestCaseBase caseBase, TestMethodResult result);

    void afterInvocation(TestCaseBase caseBase, TestMethodResult result);

    void onStart(ISuite suite);

    void onFinish(ISuite suite);

    void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory);

}
