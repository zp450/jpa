package com.zp.Jpa.entity.queryentity.sys;

import java.util.List;

public interface SysPermisssionTree {
 Integer id();
 String label();
 List<SysPermisssionTree> children();
 
}
