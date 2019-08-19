package com.zp.Jpa.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;



public class MapTool {
	 static final Map<String, Object> map = new HashMap<String, Object>();
    /**保存结果返回  
     * @param object
     * @param msg
     * @return map
     */
	 /**支付,
	 * @param oid 支付成功的订单id
	 * @param msg 支付类型
	 * @return
	 */
	public static  Map<String, Object> zhifu(String oid,String msg){
	    	if(oid!=null) {
	    		map.put("msg", msg+"支付成功");
	    		map.put("data", oid);
	    	}else {
	    		map.put("msg", msg+"支付失败");
	    		map.put("data", oid);
	    	}
	    	return map;
	    }
	/**返回分页查询结果,
	 * @param pages
	 * @return count 多少条  data 查到的内容
	 */
	public static  Map<String, Object> page(Page<?>  pages){
    		map.put("count", pages.getTotalElements());
    		map.put("data", pages.getContent());
    	return map;
    }
	public static  Map<String, Object> findS(List<?> list,String msg){
		
		map.put("msg", msg+"成功");
		map.put("data", list);
	return map;
}
    public static  Map<String, Object> saveObject(Object object,String msg){
    	if(object!=null) {
    		map.put("msg", msg+"成功");
    		map.put("data", object);
    	}else {
    		map.put("msg", msg+"失败");
    		map.put("data", object);
    	}
    	return map;
    }
	public static Map<String, Object> mapTool(int count, String msg) {
		if (count > 0) {
			map.put("success", true);
			map.put("msg", msg + "成功");
		} else {
			map.put("success", false);
			map.put("msg", msg + "失败");

		}
		return map;

	}
	public static Map<String, Object> importData(int count, String msg) {
		if (count > 0) {
			map.put("success", true);
			map.put("msg", msg+":"+count+"条");
		} else {
			map.put("success", false);
			map.put("msg", msg + "失败");

		}
		return map;

	}
	public static Map<String, Object> returnMsg( String msg) {
		
			map.put("msg", msg);
			
		return map;

	}
}