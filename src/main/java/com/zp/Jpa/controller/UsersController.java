package com.zp.Jpa.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.zp.Jpa.SMS.SmsDemo;
import com.zp.Jpa.entity.Users;
import com.zp.Jpa.entity.equity.EquityUser;
import com.zp.Jpa.repository.UserCheckRepository;
import com.zp.Jpa.repository.UsersRepository;
import com.zp.Jpa.service.Impl.EquityUserServiceImpl;
import com.zp.Jpa.service.Impl.SysUserServiceImpl;
import com.zp.Jpa.service.Impl.UserServiceImpl;
import com.zp.Jpa.service.Impl.WeixinLoginImpl;
import com.zp.Jpa.tools.MapTool;
import com.zp.Jpa.tools.PictureUtil;
import com.zp.Jpa.tools.StringUtils;
import com.zp.Jpa.tools.MD5.PasswordMD5;
import com.zp.Jpa.tools.excel.PoiUtils;
import com.zp.tools.wexin.CommonUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
@CrossOrigin
@RestController
@RequestMapping(value="/user" ,name="微信用户模块")
public class UsersController {
	public Map<String, Object> map = new HashMap<String, Object>();
	public static Logger log = Logger.getLogger(CommonUtil.class);
	@Autowired
	public WeixinLoginImpl weixinLoginImpl;
	@Autowired
	public UserServiceImpl uServer;
	@Autowired
	public UserCheckRepository UserCheckRepository;
	@Autowired
	public SysUserServiceImpl sysUserServiceImpl;
	@Autowired
	public EquityUserServiceImpl equityUserServiceImpl;
	@Autowired
	public UsersRepository usersRepository;
	public PoiUtils poiUtils = new PoiUtils();// 初始化工具类
//	@ApiOperation(value = "保存微信用户信息 ", notes = "用于添加用户的支付密码    uid 必传   passWord 密码必传")
//	@ApiImplicitParam(name = "users", value = "用户详细实体users", required = true, dataType = "Users")
//	@RequestMapping(value = "/saveUserPassWord", name = "保存微信用户信息", method = RequestMethod.POST)
//    public Object saveUserPassWord(Users users) {
//    	Users user2 = usersRepository.findOne(users.getUid());
//    	if(!StringUtils.isEmpty(users.getPassWord())) {
//		    //用户名为盐,md5加密
//    		String newpassWord=PasswordMD5.getNewPassWord(user2.getUserName(), users.getPassWord());
//			user2.setPassWord(newpassWord);
//		}
//    	Users user3=usersRepository.save(user2);
//    	return MapTool.saveObject(user3, "用户密码保存成功");
//    }
	/**
	 * @param uid
	 * @param oldPayPassWord
	 * @param newPayPassWord
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@ApiOperation(value = "用户修改支付密码", notes = "修改")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "uid", value = "用户id ", required = true, dataType = "int", paramType = "query", defaultValue = "6032"),
			@ApiImplicitParam(name = "oldPayPassWord", value = "用户老密码", required = false, dataType = "string", paramType = "query", defaultValue = "000000"),
			@ApiImplicitParam(name = "newPayPassWord", value = "用户新密码", required = true, dataType = "string", paramType = "query", defaultValue = "111111"),
			 })
	@RequestMapping(value = "changeUserPayPassWord", name = "用户修改支付密码", method = RequestMethod.POST)
	public Object changeUserPayPassWord(Integer uid,String oldPayPassWord ,String newPayPassWord) throws ClassNotFoundException, IOException {
		Users users= usersRepository.findOne(uid);
		String payPassWord=users.getPayPassWord();//拿到数据库中加密过的 原来的密码
		//如果用户没有设置过支付密码
		if(StringUtils.isEmpty(payPassWord)) {
			//用加密后的新密码
			String newPayPassWordMD5=PasswordMD5.getNewPassWord(users.getUserName(), newPayPassWord);
			//进行对用户信息更新
			users.setPayPassWord(newPayPassWordMD5);
			//保存用户信息
			Users users2=uServer.save(users);
			if(!StringUtils.isEmpty(users2)) {
				map.put("status", 1);
				map.put("msg", "设置新密码成功");
			}else {
				map.put("status", 0);
				map.put("msg", "密码设置失败,用户信息保存出错,请稍后再试");
			}
			return map;
		}
		
		//如果老密码和新密码相同返回失败
		if(newPayPassWord.equals(oldPayPassWord)) {
			map.put("status", 0);
			map.put("msg", "老密码和新密码相同,支付密码修改失败");
		}else {
			//验证用户输入的老密码是否正确
			String pay=PasswordMD5.getNewPassWord(users.getUserName(), oldPayPassWord);
			
			if(pay.equals(payPassWord)) {
				//用加密后的新密码
				String newPayPassWordMD5=PasswordMD5.getNewPassWord(users.getUserName(), newPayPassWord);
				//进行对用户信息更新
				users.setPayPassWord(newPayPassWordMD5);
				//保存用户信息
				Users users2=uServer.save(users);
				if(!StringUtils.isEmpty(users2)) {
					map.put("status", 1);
					map.put("msg", "密码修改成功");
				}else {
					map.put("status", 0);
					map.put("msg", "密码修改失败,用户信息保存出错,请稍后再试");
				}
			}else {
				map.put("status", 0);
				map.put("msg", "密码修改失败,用户原有密码验证失败");
			}
		}
		return map;
	}
	/**
     * http://localhost:8081/user/exportUsers?objs[]=72&objs[]=73
     * @param s
     * @param response
     * @return
     */
    @RequestMapping(value = "/exportUsers",name="用户数据导出excel", method = RequestMethod.GET)
	public Object exportUsers(@RequestParam(value="objs[]",required=true) List<Object> objs, HttpServletResponse response) {
		String downloadName = "SpringBoot下载的中文名字Excel文件.xls";
		List<Integer> ids=new ArrayList<Integer>();
		for(Object obj:objs) {
			ids.add(Integer.parseInt(obj.toString()));
    	}  
		//拿到要导出的数据
		List<Users> list = uServer.exportStudents(ids);
		System.out.println("///////list=>"+list);
		try {
			response.setCharacterEncoding("UTF-8");// 设置响应的字符编码格式
			response.setContentType("application/vnd.ms-excel");// 指明响应文件为excel类型
			response.setHeader("Content-disposition", "attachment;filename="
					+ new String(downloadName.getBytes("GB2312"), "ISO8859-1"));// 文件名编码处理，防止浏览器下载文件名乱码
			ServletOutputStream outputStream = response.getOutputStream();// 获取响应的字节输出流
			String[] headers = { "用户编号", "用户类型","用户账号","邮箱","移动电话","密码(加密)",
					"真实姓名","介绍人(上线)","性别","生日","电话","图片","余额","活动金额",
					"赠送金额","冻结金额","积分总额","登录次数","注册时间","登录时间","手机验证状态",
					"邮箱验证状态","注册ip","上次登录ip","QQ","状态","运营商","分销商","是否已经被删除",
					"支付密码","微信openid","微信号","备注","reid"};
			poiUtils.createExcel(list, Users.class, outputStream, headers);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/importUsers", method = RequestMethod.POST, name = "导入用户信息")
	public Object importUsers(
			@RequestParam(value = "myfile", required = false) CommonsMultipartFile myfile) {
		String originalFilename = myfile.getOriginalFilename();// 得到上传文件的名称
		List<Users> list = null;
		try {
			//getInputStream()拿到具体文件路径
			list = (List<Users>) poiUtils.parseExcel(Users.class,myfile.getInputStream(), originalFilename);
			System.out.println(list.toString());
			//一起添加不行,咱就一条一条添加
//			int k=0;
//            for(StudentTb stu:list){
//            	System.out.println("遍历执行=》"+stu);
//		        this.addStudent(stu);
//		       count=k+1;
//		    }
			int count = uServer.addUsers(list).size();
			return MapTool.importData(count, "成功导入");
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * http://localhost:8081/user/getAmount?uid=6032 获取用户信息
	 * 
	 * @param uid
	 * @return users
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@ApiOperation(value = "获取用户信息", notes = "根据url的id来获取用户详细信息")
	@ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "int", paramType = "query")
	@RequestMapping(value = "/getAmount", name = "获取用户信息", method = RequestMethod.GET)
	public Object getAmount( Integer uid) throws ClassNotFoundException, IOException {
		 
		Users users=(Users)uServer.findOne(uid);
		users.setUserCheckList(UserCheckRepository.findByUid(uid));
		return users;
	}

	/**
	 * http://localhost:8081/user/saveUserinfo?users.uid=xx&users.username=xx
	 * http://localhost:8081/user/saveUserinfo?uid=830&password=000000
	 * 保存微信用户信息
	 * 
	 * @param users
	 * @return 保存成功或失败
	 */
	@ApiOperation(value = "保存微信用户信息", notes = "获取前台传输用户详细信息")
	@ApiImplicitParam(name = "users", value = "用户详细实体users", required = false, dataType = "Users")
	@RequestMapping(value = "/saveUserinfo", name = "保存微信用户信息", method = RequestMethod.POST)
	public Object saveUserinfo( Users users) {
		
		//为了防止不为空
		Users usera = usersRepository.findOne(users.getUid());
		//昵称
		if(!StringUtils.isEmpty(users.getUserName())) {
			usera.setUserName(users.getUserName());
		}
		//真实姓名
		if(!StringUtils.isEmpty(users.getTrueName())) {
			usera.setTrueName(users.getTrueName());
		}
		//手机号
		if(!StringUtils.isEmpty(users.getMobile())) {
			usera.setMobile(users.getMobile());
		}
		//邮箱
		if(!StringUtils.isEmpty(users.getEmail())) {
			usera.setEmail(users.getEmail());
		}
		//qq
		if(!StringUtils.isEmpty(users.getQQID())) {
			usera.setQQID(users.getQQID());
		}
		
//		if(!StringUtils.isEmpty(users.getPayPassWord())) {
//		    //用户名为盐,md5加密
//    		String newpassWord=PasswordMD5.getNewPassWord(usera.getUserName(), users.getPayPassWord());
//    		users.setPayPassWord(newpassWord);
//    		
//		}
		Users users2 = usersRepository.save(usera);
//		Users users1 = usersRepository.save(users);
		if (users2 != null) {
			return "保存成功";
		}
		return "保存失败";
	}

	/**
	 * http://localhost:8081/user/findChildrenForRecommend?Recommend=123
	 * 查看通过该id下所查询到的全体下线
	 * 
	 * @param Recommend
	 * @return List<Users>
	 */
	@ApiOperation(value = "通过uid查询下线", notes = "根据url的id来获取用户详细信息")
	@ApiImplicitParam(name = "Recommend", value = "用户ID", required = true, dataType = "String",defaultValue="1", paramType = "query")
	@RequestMapping(value = "/findChildrenForRecommend", name = "通过uid查询下线", method = RequestMethod.GET)
	public List<Users> findChildrenForRecommend(String Recommend) {
		List<Users> userslist = new ArrayList<>();
		if (!StringUtils.isEmpty(Recommend)) {
			userslist = usersRepository.findByRecommend(Recommend);
		}
		return userslist;
	}

	/**
	 * 扫码分享,增加下线
	 * 
	 * @author zhangpu
	 *         http://localhost:8081/user/getUserInfoForScope?code=011zkHw52ZcUjR0BS2y526HTw52zkHwc
	 *         特殊场景下的静默授权 用户授权带上的code和推荐人id 扫谁的码就是谁的id 可存在可不存在
	 * @return 获取用户基本信息,如果数据库中没有存在,就进行添加微信用户操作
	 */
	@ApiOperation(value = "静默授权通过code获得微信用户信息", notes = "扫码分享,增加下线")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "code", value = "用户授权带上的code", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "Recommend", value = "用户id   Recommend", required = false, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/getUserInfoForScope", name = "静默授权通过code获得微信用户信息",method = RequestMethod.GET)
	public Object getUserInfoForScope( String code,Integer Recommend) { 
		Object object = "code为空了";
		if (!StringUtils.isEmpty(code)) {
			Map<String, Object>	map = weixinLoginImpl.getUserInfoForScope(code, Recommend);
			
			Users user=(Users) map.get("user");
			if(!StringUtils.isEmpty(user)) {
				//获取token
				Map<String, Object> tokenList=sysUserServiceImpl.getTokenAndListUsers(user.getUid());
				String token=(String) tokenList.get("token");
				map.put("token", token);
			}
			//如果是股东,返回股东的登记
			BigDecimal level;
			if(!StringUtils.isEmpty(user)) {
				EquityUser equityUser=equityUserServiceImpl.findByUId(user.getUid());
				
				if(StringUtils.isEmpty(equityUser)) {//如果他不是股东就返回0
					level=new BigDecimal("0");
					
				}else {//否则返回已存在的股份登记
					level=equityUser.getLevel();
					System.out.println("股东==>"+equityUser);
					map.put("equityUser", equityUser);
				}
				
				map.put("level", level);
			}
			
						
			return map;
		}
		return object;
		
		
	}
	
	@ApiOperation(value = "通过微信的主键id查询对应的管理员", notes = "通过微信的主键id查询对应的管理员")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "uid", value = "微信用户的uid", required = true, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/getSysUserByUid", name = "微信用户获取对应的管理员账号",method = RequestMethod.GET)
	public Object getSysUserByUid( Integer uid) { 
		
		return weixinLoginImpl.getSysUserByWeiXinUid(uid);
		
		
	}
	// http://localhost:8081/user/getTokenTest?uid=6035
	@RequestMapping(value="getTokenTest", method = RequestMethod.GET)
	public Object getTokenTest(Integer uid) {
		Map<String, Object> tokenList=sysUserServiceImpl.getTokenAndListUsers(uid);
		String token=(String) tokenList.get("token");
		System.out.println(token);
		return token;
	}
	/**
	 * http://localhost:8081/user/upload
	 * 
	 * 图片上传
	 * 方法未用
	 * @param file
	 * @param uid
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@ApiOperation(value = "上传图片", notes = "上传要传输对象的id")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "file", value = "图片文件", required = true, dataType = "MultipartFile", paramType = "query"),
	@ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Integer", paramType = "query")
	})
	@RequestMapping(value="upload", name = "上传图片", method = RequestMethod.POST)
	public Object upload(MultipartFile file, Integer uid) throws ClassNotFoundException, IOException {
		//拿到这个用户
		Users users = (Users)uServer.findOne(uid);
		//拿到类名
		String pathInfo = users.getClass().toString();
		String webPicture = "地址错误";
		try {
			//添加图片到服务器或本地
			webPicture = PictureUtil.addPicture(file, pathInfo);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			log.error("图片上传错误");
		}
		return webPicture;
	}
	

	//忘记密码
	@ApiOperation(value = "忘记密码,通过验证码修改密码", notes = "修改密码")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "uid", value = "用户id", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "mobile", value = "用户手机号   mobile", required = true, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/changePasswordByCode", name = "静默授权通过code获得微信用户信息",method = RequestMethod.GET)
	public String changePasswordByCode(Integer uid ,@RequestParam(required=true)String mobile) throws ClientException {
		Users users = usersRepository.findOne(uid);
		if (mobile.equals(users.getMobile())) {
			System.out.println("提供的手机号和已经存在的手机号想通,可以发送验证码...");
			SendSmsResponse response = SmsDemo.sendSms(mobile);
			if(response.getMessage().equals("OK")) {
				System.out.println("短信接口返回的数据----------------");
		        System.out.println("Code=" + response.getCode());
			}
	        return response.getCode();
		}
		return "请提供您绑定的手机号";
	}
	@ApiOperation(value = "用户签到", notes = "用户签到")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "uid", value = "用户id", required = true, dataType = "int", paramType = "query"),
			 })
	@RequestMapping(value = "/userCheck", name = "用户签到",method = RequestMethod.GET)
	public Object userCheck(Integer uid) {
		return uServer.userCheck(uid);
	}
	
	
	
}
