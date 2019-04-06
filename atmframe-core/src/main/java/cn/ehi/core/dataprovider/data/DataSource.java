package cn.ehi.core.dataprovider.data;

import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author 28476
 * 承装数据类
 */

public class DataSource {
	public static final String DATA_LIST = "DataList";
	//private List<BaseData> dataList = new ArrayList<>() ;
	private Map<String,List<BaseData>> map = new HashMap<String, List<BaseData>>();
//	public List<BaseData> getDataList() {
//		return dataList;
//	}
//
//	public void setDataList(List<BaseData> dataList) {
//		this.dataList = dataList;
//	}
	
	public void addData(BaseData bd){
		if(TextUtils.isEmpty(bd.getTypeName())){
			throw new RuntimeException("TypeName为空");
		}
		if(map!=null){
			if(map.get(bd.getTypeName())!= null){
				map.get(bd.getTypeName()).add(bd);
				return;
			}
			List<BaseData> dataList = new ArrayList<BaseData>() ;
			dataList.add(bd);
			map.put(bd.getTypeName(), dataList);
		}else{
			throw new RuntimeException("map为空");
		}
		
		
	}
	/**
	 * 添加temp的数据迁移到指定的DataSource 条目中
	 * @param temp 要迁移数据的DataSource
	 */
	public void addAllDataList(DataSource temp){
		List<BaseData> tempList;
		for(String s:temp.getMap().keySet()){
			tempList = temp.getMap().get(s);
			if(getMap().containsKey(s)){
				getMap().get(s).addAll(tempList);
			}else{
				getMap().put(s,tempList);
			}
		}
	}
	/**
	 * 通过typeName获取对应的基础数据组
	 * @param typeName
	 * @return
	 */
	public List<BaseData>  getBaseDataList(String typeName) {
		if(map == null){
			throw new RuntimeException("map为空");
		}
		return map.get(typeName);
	}
	
	public Map<String, List<BaseData>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<BaseData>> map) {
		this.map = map;
	}
	
	/**
	 * @param baseDataList 对应类型基础list
	 * @return obj 返回dataProvider 需要的内容 
	 */
	public static Object[][] getBaseData(List<BaseData> baseDataList) {
		if (baseDataList == null && baseDataList.size() == 0) {
			throw new RuntimeException("DataList数据未获取到");
		}
		Object[][] obj = new Object[baseDataList.size()][1];
		for (int i = 0; i < baseDataList.size(); i++) {
			obj[i][0] = baseDataList.get(i);
		}
		return obj;
	}
}
