package com.zp.Jpa.service.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.zp.Jpa.entity.UserCheck;
import com.zp.Jpa.entity.Users;
import com.zp.Jpa.repository.UserCheckRepository;
import com.zp.Jpa.repository.UsersRepository;
import com.zp.Jpa.service.UserService;
import com.zp.Jpa.tools.MapTool;
import com.zp.Jpa.tools.StringUtils;
import com.zp.Jpa.tools.Date.DateUtil;

@Service
public class UserServiceImpl implements UserService {
	private Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private UserCheckRepository userCheckRepository;
	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public Object userCheck(Integer uid) {
		// TODO Auto-generated method stub
		// 用户签到判断,查看上次签到时间以及状态
		UserCheck userCheck = userCheckRepository.findByUidAndCheckTime(uid);
		// 第一次签到情况
		if (StringUtils.isEmpty(userCheck)) {
			// 签到表中没有记录的话,第一次签到
			// 增加用户表中积分
			Users user = usersRepository.findOne(uid);
			Long vouchers = user.getVouchers();
			user.setVouchers(vouchers + 2);
			usersRepository.save(user);
			// 保存签到表
			UserCheck userCheck2 = new UserCheck();
			userCheck2.setUid(uid);
			userCheck2.setUserName(user.getUserName());
			Date date = new Date();
			userCheck2.setCheckTime(date);
			userCheck2.setContinuousCheckDay(1);
			userCheck2.setRemark("无备注");
			userCheck2.setIsDel(0);

			// 返回
			return MapTool.saveObject(userCheckRepository.save(userCheck2), "签到");
		}
		// 如果查询的签到时间在今天,则提示不用重复签到
		Date data = userCheck.getCheckTime();
		// 计算出今天时间的开始和结束
		Date begin = DateUtil.getStartTime();
		Date enDate = DateUtil.getnowEndTime();
		// 如果签到日期在今天
		if (data.after(begin) && data.before(enDate)) {
			map.put("msg", "今天已经签到过了,今天签到的时间是:" + userCheck.getCheckTime());
			map.put("data", "0");
			return map;
		}
		// 连续签到的天数
		Integer conDay = userCheck.getContinuousCheckDay();
		// 签到的下一天对比今天是否相同
		Date checkTime = userCheck.getCheckTime();
		Calendar c = Calendar.getInstance();
		c.setTime(checkTime);
		c.add(Calendar.DAY_OF_MONTH, 1);
		Date checkTimeNextDay = c.getTime();
		Date date = new Date();
		System.out.println("签到的下一天" + checkTimeNextDay);
		System.out.println("今天" + date);
		// 如果签到的下一天和今天相同
		if (checkTimeNextDay == date) {
			// 连续签到
			conDay += 1;
			// 已一星期7天为一周期,连续签到自动切断,连续七天会有大奖励
			// if(conDay==8) {
			// userCheck.setCheckTime(date);
			// userCheck.setContinuousCheckDay(1);
			// }
			userCheck.setCheckTime(date);
			userCheck.setContinuousCheckDay(conDay);
		} else {
			// 未连续签到
			userCheck.setCheckTime(date);
			userCheck.setContinuousCheckDay(1);
		}
		// 签到过增加用户积分
		Users user = usersRepository.findOne(uid);
		Long vouchers = user.getVouchers();
		user.setVouchers(vouchers + 2);
		usersRepository.save(user);

		return MapTool.saveObject(userCheckRepository.save(userCheck), "签到");
	}

	@Override
	public List<Users> exportStudents(List<Integer> ids) {
		// TODO Auto-generated method stub
		return usersRepository.findAll(ids);
	}

	public List<Users> addUsers(List<Users> users) {
		return usersRepository.save(users);
	}

	/**
	 * 获取用户策略：先从缓存中获取用户，没有则取数据表中 数据，再将数据写入缓存
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public Object findOne(Integer uid) throws ClassNotFoundException, IOException {

		String key = "user_" + uid;

		ValueOperations<String, Users> operations = redisTemplate.opsForValue();
		boolean hasKey = redisTemplate.hasKey(key);
		if (hasKey) {
			long start = System.currentTimeMillis();
			//redis取出来的是序列化过的,object接,然后转成byte[] 再反序列化 转成object 再强转成users类型
			Object object=operations.get(key);
			byte[] by=toByteArray(object);
			Object object2=unserialize(by);
			
			Users user = (Users)object2;  

			
			System.out.println("Users对象反序列化成功！");
			System.out.println("==========从缓存中获得数据=========");
			 System.out.println(user.toString());
			System.out.println("==============================");
			long end = System.currentTimeMillis();
			System.out.println("查询redis花费的时间是:" + (end - start) + "ms");
			return user;
		} else {
			long start = System.currentTimeMillis();
			Users user = usersRepository.findOne(uid);
			System.out.println("==========从数据表中获得数据=========");
			System.out.println(user.getUserName());
			System.out.println("==============================");

			// 写入缓存 自动序列化了
			operations.set(key, user, 5, TimeUnit.HOURS);
			long end = System.currentTimeMillis();
			System.out.println("查询redis花费的时间是:" + (end - start) + "ms");
			return user;
		}

	}
	/**  
     * 对象转数组  
     * @param obj  
     * @return  
     */  
    public byte[] toByteArray (Object obj) {      
        byte[] bytes = null;      
        ByteArrayOutputStream bos = new ByteArrayOutputStream();      
        try {        
            ObjectOutputStream oos = new ObjectOutputStream(bos);         
            oos.writeObject(obj);        
            oos.flush();         
            bytes = bos.toByteArray ();      
            oos.close();         
            bos.close();        
        } catch (IOException ex) {        
            ex.printStackTrace();   
        }      
        return bytes;    
    }   
	//反序列化
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {

            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {

        }
        return null;
    }
	@Override
	public Users save(Users users) {
		ValueOperations<String, Users> operations = redisTemplate.opsForValue();
		Users users2 = usersRepository.save(users);
		if (!StringUtils.isEmpty(users2)) {
			String key = "user_" + users.getUid();
			boolean haskey = redisTemplate.hasKey(key);
			if (haskey) {
				redisTemplate.delete(key);
				System.out.println("删除缓存中的key=========>" + key);
			}
			// 再将更新后的数据加入缓存
			Users userNew = usersRepository.findOne(users.getUid());
			if (userNew != null) {
				operations.set(key, userNew, 3, TimeUnit.HOURS);
			}
		}

		// TODO Auto-generated method stub
		return users2;
	}

	/**
	 * 删除用户策略：删除数据表中数据，然后删除缓存
	 */
	public int deleteUserById(int uid) {
		Users users = usersRepository.findOne(uid);
		users.setIsDel(1);
		Users users2 = usersRepository.save(users);

		String key = "user_" + uid;
		if (!StringUtils.isEmpty(users2)) {
			boolean hasKey = redisTemplate.hasKey(key);
			if (hasKey) {
				redisTemplate.delete(key);
				System.out.println("删除了缓存中的key:" + key);
			}
		}
		return users2.getIsDel();
	}

}
