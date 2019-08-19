package com.zp.Jpa.entity.queryentity.sys;

import lombok.Data;

/**批量删除工具类
 * ids  要删除的id集合数组   ["xxx","vvv"]
 * @author zp
 *
 */
@Data
public class DeleteObjects {

	private Object ids;
}
