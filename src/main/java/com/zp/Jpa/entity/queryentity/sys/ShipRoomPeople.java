package com.zp.Jpa.entity.queryentity.sys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

//航线订单房间信息
@Data
@Entity
@Table(name="ShipRoomPeople")
public class ShipRoomPeople {
	@Id
	@GeneratedValue
	private Integer srpId;
   private Integer lpId;
   private String sOid;
   private Integer roomName;
   private Integer peopleNum;
   private long roomPrice;
   private long totalRoomPrice;
//   @JsonIgnore
//   @ManyToOne(targetEntity=ShipOrder.class)
//   @JoinColumn(name="shipRoomPeople_shipOrder_id")
//   private ShipOrder shipOrder;
}
