package com.zp.Jpa.entity.search;

import lombok.Data;

/**首页导航查询,微信
 * @author Administrator
 *
 */
@Data
public class WeiXinWebMenuSearch {
	private Integer page;
	private Integer size;
   private String title;
   private String url;
   private Integer sort;
   private Integer status;
   private Integer isDel;
}
