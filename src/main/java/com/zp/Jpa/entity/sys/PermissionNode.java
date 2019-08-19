package com.zp.Jpa.entity.sys;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PermissionNode {
  private Integer id;
  private String label;
  private String childrens;
}
