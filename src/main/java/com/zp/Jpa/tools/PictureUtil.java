package com.zp.Jpa.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class PictureUtil {
	/**
	 * 图片上传
	 * 
	 * @param file 图片 
	 * @param pathInfo 应该放在项目的那个路径下,\\Pictures\\TrustSoft\\xxx的路径
	 * @return
	 */
	public static String addPicture(MultipartFile file,String pathInfo) {
		
	
	String contentType = file.getContentType(); // 图片文件类型
	long date=new Date().getTime();
	String fileName = file.getOriginalFilename(); // 图片名字
	String[] aStrings =fileName.split("\\.");
	String name=date+"."+aStrings[1];
//	Teacher teacher = repository.findOne(idString);
	//object.setPicUrl("images/"+name);
	///D:\\DB\\WEB\\Pictures\\TrustSoft\\
	// 文件存放路径C:\Users\Administrator\HBuilderProjects\教师页面   pathInfo
	//String filePath = "C:\\Users\\Administrator\\HBuilderProjects\\教师页面\\images\\";
	String filePath = "D:\\DB\\WEB\\Pictures\\TrustSoft\\"+pathInfo;
	// 调用文件处理类FileUtil，处理文件，将文件写入指定位置
	
	//要存到数据库里的地址,在外网可以访问到的地址
	String webPicture="http://www.iuju58.com/pictures/TrustSoft/"+pathInfo+name;
	try {
		uploadFile(file.getBytes(), filePath, name);
	} catch (Exception e) {
		// TODO: handle exception
	}
	// 返回图片的存放路径
	//return filePath;
	//返回图片的外网访问路径
	return webPicture;
	}
	
		public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {

			File targetFile = new File(filePath);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			FileOutputStream out = new FileOutputStream(filePath + fileName);
			out.write(file);
			out.flush();
			out.close();
		}
	
}
