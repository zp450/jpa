package com.zp.Jpa.entity.sys;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 
 * @Description:   
 * @author         Mashuai 
 * @Date           2018-5-16 下午10:50:07  
 * @Email          1119616605@qq.com
 * 树控件数据格式化
每个节点都具备以下属性：

id：节点ID，对加载远程数据很重要。
text：显示节点文本。
state：节点状态，'open' 或 'closed'，默认：'open'。如果为'closed'的时候，将不自动展开该节点。
checked：表示该节点是否被选中。
attributes: 被添加到节点的自定义属性。
children: 一个节点数组声明了若干节点。
 */
@Data
@JsonInclude(Include.NON_NULL)	//如果该属性为NULL则不参与序列化
public class Node {
	private Integer id;//节点ID，对加载远程数据很重要。
	private String text;//显示节点文本。
	private String state;//节点状态，'open' 或 'closed'，默认：'open'。如果为'closed'的时候，将不自动展开该节点
	private Boolean checked;//表示该节点是否被选中。
	private List<Node> children;//一个节点数组声明了若干节点。
	private Integer isDel;

}
