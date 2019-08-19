package com.zp.Jpa.tools;

public class StringUtils {
	public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
}
	/**String 转 uniqueidentifier 格式
	 * @param oid
	 * @return
	 */
	public static String toUniqueidentifier(String oid) {
		
		StringBuffer bufferA = new StringBuffer(oid);

		bufferA.insert(8, "-");
		bufferA.insert(13, "-");
		bufferA.insert(18, "-");
		bufferA.insert(23, "-");

		String out_trade_no = String.valueOf(bufferA).toUpperCase();
		return out_trade_no;
	}
}
