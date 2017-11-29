package com.clj.reptilehouse.common.util;

import java.io.File;
import java.io.IOException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

public class UploadUtil {
	/**
	 * 文件上传
	 * @param file
	 * @param realPath
	 * @param savePath
	 * @param fileName
	 * @return
	 */
	public static String upload(MultipartFile file,String realPath,String savePath,String fileName){
		String returnUrl=null;
		if (!file.isEmpty()) {
			realPath=realPath+File.separator;
			String tmpPath = realPath+savePath;
			//创建临时目录保存源文件
			File saveFile = new File(tmpPath);
			if (!saveFile.exists())
				saveFile.mkdirs();
			try {
				file.transferTo(new File(tmpPath+ File.separator+fileName));
				returnUrl="upload/"+savePath + "/"+ fileName;
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnUrl;
	}
	
	/**
	 * 图片上传，压缩上传
	 * @param file
	 * @param realPath
	 * @param savePath
	 * @return
	 */
	public static String upload(MultipartFile file,String realPath,String savePath){
		String returnUrl=null;
		if (!file.isEmpty()) {
			String fileName=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
			fileName =System.currentTimeMillis()+"."+fileName;
			realPath=realPath+File.separator+"upload";
			String tmpPath = realPath+File.separator+savePath+ File.separator+"orig";
			//创建临时目录保存源文件
			File saveFile = new File(tmpPath);
			if (!saveFile.exists())
				saveFile.mkdirs();
			try {
				file.transferTo(new File(tmpPath+ File.separator+fileName));
				//创建保存目录
				File compressSaveFile = new File(realPath+File.separator+savePath);
				if (!compressSaveFile.exists())
					compressSaveFile.mkdirs();
				String compress_type = ConfigUtil.getConfProp("img.compress.type");
				if ("1".equals(compress_type)) {// 按指定尺寸缩放
					String compress_width = ConfigUtil.getConfProp("img.compress.width");
					String compress_hight = ConfigUtil.getConfProp("img.compress.hight");
					Thumbnails.of(tmpPath+File.separator +fileName).size(Integer.parseInt(compress_width), Integer.parseInt(compress_hight)).keepAspectRatio(false).toFile(realPath+File.separator+savePath+File.separator+fileName);
				}else if("2".equals(compress_type)) {// 按比例进行缩放
					String compress_percent = ConfigUtil.getConfProp("img.compress.percent");
					Thumbnails.of(tmpPath+File.separator +fileName).scale(Double.parseDouble(compress_percent)).toFile(realPath+File.separator+savePath+File.separator+fileName);
				}
				returnUrl="upload/"+savePath + "/"+ fileName;// 获取缩略图路径
				File tmpFile = new File(tmpPath+File.separator +fileName);
				if(tmpFile.exists()){
					tmpFile.delete();
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnUrl;
	}
}
