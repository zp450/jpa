package com.zp.Jpa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Data
@Table(name="ShipShoppingCart")
public class ShipShoppingCart {
	@Id
	@GeneratedValue
   private Integer cartId;
	//用户id
   private Integer userId;
   //商品id
   private Integer goodsId;
   //门店id
   private Integer storeId;
   //订单所属类型
   private Integer omId;
   //数量
   private Integer number;
   //规格
   private String speces;
   //状态,已过期,为过期,降价,优惠,
   private String status;
   private Date createTime;
   private Date updateTime;
   private String remark;
   private Integer isDel;
//   @JsonIgnore
//	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.PERSIST)
//   @JoinTable(name="shipshoppingCart_goods_tb",
//		joinColumns={@JoinColumn(name="cartid",referencedColumnName="cartId")},
//		inverseJoinColumns={@JoinColumn(name="gid",referencedColumnName="gid")})
//	@NotFound(action = NotFoundAction.IGNORE)
//	private Set<Goods> Goods = new HashSet<>();
}
