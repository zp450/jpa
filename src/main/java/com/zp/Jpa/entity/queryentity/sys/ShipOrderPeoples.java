package com.zp.Jpa.entity.queryentity.sys;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShipOrderPeoples {
   private String name;
   private String price;
  // private String uid;
   private String number;
   private String lpid;//是shipprice中的主键;
//   private boolean addStatus;
  
   private List<?> rooms;
   
  
}
