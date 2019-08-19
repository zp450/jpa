package com.zp.Jpa.entity.queryentity.sys;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomChoice{
	   private String lmId;
	   private String name;
}