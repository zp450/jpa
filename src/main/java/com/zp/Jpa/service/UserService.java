package com.zp.Jpa.service;

import java.io.IOException;
import java.util.List;

import com.zp.Jpa.entity.Users;

public interface UserService {
   Object userCheck(Integer uid);
   List<Users> exportStudents(List<Integer> ids);
   List<Users> addUsers(List<Users> users);
   Object findOne(Integer uid) throws ClassNotFoundException, IOException;
   Users save(Users users);
}
