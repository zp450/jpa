package com.zp.Jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data
@Table(name="ShipOrderPeople")
public class ShipOrderPeople {
	@Id
	@GeneratedValue
   private String sopId;
   private String orderCn;
   private Integer parentId;//负责人id   user主键
   private Integer lPid;//订房详细名称主键   shipPrice主键
   private String shipRoomName;//房间名称
   private Integer number;//房间人数
   private long Totalprice;//总人数
   private Integer lmId;//linkName主键
   private String shipOrderPeopleName;//名字
   
   //private Integer roomNuber;//预留字段
   
   private Integer lDPid;
//   @JsonIgnore
//   @OneToOne(fetch = FetchType.EAGER) // 默认值optional = true表示是否可以为空
//   @JoinColumn(name="shipOrderPeople_shipOrder_id",unique = true)
   @Transient
   private LinkMan linkMan;
//   @JsonIgnore
//   //@ManyToOne(targetEntity = ShipOrder.class)
//   @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)//可选属性optional=false,表示author不能为空。删除文章，不影响用户
//  @JoinColumn(name="orderCn",insertable = false, updatable = false)//设置在article表中的关联字段(外键)
//   private ShipOrder shipOrder;//所属航线
//   @ManyToMany
//   @JoinTable(name = "ShipOrder_ShipOrderPeople",joinColumns = @JoinColumn(name = "sopId"),
//   inverseJoinColumns = @JoinColumn(name = "soId"))
//   private List<ShipOrder> ShipOrderList;//所属航线
   
}
