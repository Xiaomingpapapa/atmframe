package cn.ehi.core.testbase;

import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 33053
 * @create 2019/3/16 10:40
 * <页面基类>
 */
public class Page<T extends RemoteWebDriver> {
    protected static Map<String, Object> data=null;

    public void putData(String key, Object value) {
        if (null == data) {
            data = new HashMap<>(10);
        }
        data.put(key, value);
    }

    public Object getData(String key) {
        if (null == data) {
            return null;
        }
        return data.get(key);
    }

}
