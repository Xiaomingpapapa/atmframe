package cn.ehi.core.dataprovider.data;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 28476
 * 基本数据存储
 */
public class
BaseData {
	private Map<String, String> dataPool = new HashMap<String, String>();//以键值对key 存入的位置，value 输入的值
	private String condition;
	private String typeName;
	private String expectedValue;
	public String getData(String key) {
		return this.getDataPool().get(key);
	}
	/**
	 * 添加数据到map
	 * @param dataGroup
	 */
	public void putData(DataGroup dataGroup) {
		dataPool.put(dataGroup.getKey(), dataGroup.getValue());
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public Map<String, String> getDataPool() {
		return dataPool;
	}
	public void setDataPool(Map<String, String> dataPool) {
		this.dataPool = dataPool;
	}
	public void putData(String key, String value) {
		dataPool.put(key, value);
	}
	public void removeData(String key) {
		dataPool.remove(key);
	}
	public void clearData() {
		dataPool.clear();
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getExpectedValue() {
		return expectedValue;
	}
	public void setExpectedValue(String expectedValue) {
		this.expectedValue = expectedValue;
	}
	
}
