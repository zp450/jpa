package com.zp.Jpa.controller;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zp.Jpa.tools.sys.ExceptionHandle;
import com.zp.Jpa.tools.sys.Result;
import com.zp.Jpa.tools.sys.ResultUtil;

@RestController
@RequestMapping("/result")
public class ResultController {

    @Autowired
    private ExceptionHandle exceptionHandle;

    /**
     * 返回体测试
     * @param name
     * @param pwd
     * @return
     */
    @RequestMapping(value = "/getResult",method = RequestMethod.GET)
    public Result getResult(@RequestParam("name") String name, @RequestParam("pwd") String pwd){
        
        return getResult(name);
    }
    public Result<T> getResult(String name){
    	Result result = ResultUtil.success();
//        try {
//            if (name.equals("zzp")){
//            	UserUser user=new UserUser("zzp","123456");
//            	System.out.println(user);
//                result =  ResultUtil.success(user);
//                System.out.println(result);
//            }else if (name.equals("pzz")){
//                result =  ResultUtil.error(ExceptionEnum.USER_NOT_FIND);
//            }else{
//                int i = 1/0;
//            }
//        }catch (Exception e){
//            result =  exceptionHandle.exceptionGet(e);
//        }
        System.out.println(result);
        return result;
    }
    }
    
//    public Result<T> getLoginResult(String name){
//    	Result result = ResultUtil.success();
//        try {
//            if (name.equals("zzp")){
//            	UserUser user=new UserUser("zzp","123456");
//            	
//                result =  ResultUtil.success(user);
//                
//            }else if (name.equals("pzz")){
//                result =  ResultUtil.error(ExceptionEnum.USER_NOT_FIND);
//            }else{
//                int i = 1/0;
//            }
//        }catch (Exception e){
//            result =  exceptionHandle.exceptionGet(e);
//        }
//        System.out.println(result);
//        return result;
//    }
//}
