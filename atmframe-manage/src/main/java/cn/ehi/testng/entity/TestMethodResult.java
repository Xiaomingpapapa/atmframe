package cn.ehi.testng.entity;

import org.testng.ITestResult;

/**
 * @author 33053
 * @create 2018/11/20
 * 〈测试方法结果实体类〉
 */
public class TestMethodResult {
    private ITestResult testResult;
    private String imagePath;

    public ITestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(ITestResult testResult) {
        this.testResult = testResult;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
