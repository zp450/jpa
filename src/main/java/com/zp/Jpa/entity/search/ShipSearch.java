package com.zp.Jpa.entity.search;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ShipSearch {
	private String shipName;//轮船
	private Integer SPid;//出发港口  取消
    private List<Integer> SNids;//轮船名称
    private Integer sRid;//邮轮航线
    private Integer id;
    private Integer goMonth;//出发月份
    private Integer beginMoney;//价格最低
    private Integer endMoney;//价格最高
	private Integer page;
	private Integer size;
	private Date begin;
	private Date end;
    private String name;//航线名称
    private Integer isHot;
    private Integer recommend;
    private Date nowTime;
    private Integer isDel;
    
}
