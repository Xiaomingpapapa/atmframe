package cn.ehi.testng.utils;

import cn.ehi.core.annotation.TestClassDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestNGMethod;

/**
 * @author 33053
 * @create 2018/11/20
 * 〈TestNG工具类〉
 */
public class TestNGUtil {
    public static String getTestMethodPath(ITestNGMethod method){
        if(method == null || method.getInstance() == null)
            return "";

        return method.getInstance().getClass().getName()+"#"+method.getMethodName();
    }

    public static String getTestClassDescription(ITestNGMethod method){
        if(method == null || method.getInstance() == null)
            return "";
        TestClassDescription annotation = method.getInstance().getClass().getAnnotation(TestClassDescription.class);
        String description = "未添加描述";
        if(annotation == null)
            return description;
        return annotation.value();
    }

    public static String getTestMethodDescription(ITestNGMethod method){
        if(method == null || method.getInstance() == null)
            return "";
        String description = "未添加描述";
        return method.getDescription() == null ? description : method.getDescription();
    }
}
