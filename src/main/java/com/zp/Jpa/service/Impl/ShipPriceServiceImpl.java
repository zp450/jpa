package com.zp.Jpa.service.Impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zp.Jpa.entity.ShipPrice;
import com.zp.Jpa.repository.ShipPriceRepository;
import com.zp.Jpa.service.ShipPriceService;
@Service
public class ShipPriceServiceImpl implements ShipPriceService{
	@Autowired
    private ShipPriceRepository shipPriceRepository;
	@Override
	public List<ShipPrice> selectBySidAndOutTime(Integer sid, Date outTime) {
		// TODO Auto-generated method stub
		//为了配合老系统的数据库,,对出发时间进行拼接,
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String time=format.format(outTime);
		String times=time+" 00:00:00.000";
		System.out.println("times===>"+times);
		return   shipPriceRepository.selectBySidAndOutTime(sid,times);
		
		
	}

	@Override
	public List<ShipPrice> findAll() {
		// TODO Auto-generated method stub
		return shipPriceRepository.findAll();
	}

	@Override
	public ShipPrice save(ShipPrice shipPrice) {
		// TODO Auto-generated method stub
		return shipPriceRepository.save(shipPrice);
	}

}
