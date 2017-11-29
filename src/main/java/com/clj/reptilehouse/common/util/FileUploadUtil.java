/**************************************************************
 * Copyright © 2015-2020 www.eidlink.com All rights reserved. 系统名称：eidlink-boss
 **************************************************************/
package com.clj.reptilehouse.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import sun.misc.BASE64Decoder;

/**
 * TODO: DOCUMENT ME!
 * 
 * @author ys
 * @date 2015年4月10日
 */
public class FileUploadUtil {

  public static String getFileNameByFilePath(String filePath) {
    return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
  }

  public static String readBase64StringByInputStream(InputStream sis) throws Exception {
    byte[] writeBytes = readBytesByInputStream(sis);
    if (writeBytes != null) {
      return Base64.encodeBase64String(writeBytes);
    }
    return "";
  }

  public static String readStringByInputStream(InputStream sis) throws Exception {
    byte[] writeBytes = readBytesByInputStream(sis);
    if (writeBytes != null) {
      return new String(writeBytes, "UTF-8");
    } else {
      return "";
    }
  }

  public static byte[] readBytesByInputStream(InputStream sis) throws Exception {
    byte[] writeBytes = null;

    byte[] bytes = new byte[255];
    int len = -1;
    ByteArrayOutputStream osOutputStream = new ByteArrayOutputStream();
    while ((len = sis.read(bytes)) != -1) {
      osOutputStream.write(bytes, 0, len);
    }
    writeBytes = osOutputStream.toByteArray();
    osOutputStream.flush();
    osOutputStream.close();
    sis.close();
    return writeBytes;
  }

  public static JSONObject readJSONByFileItemStream(HttpServletRequest request) throws Exception {
    JSONObject object = null;
    ServletFileUpload upload = new ServletFileUpload();// 声明解析request的对象
    upload.setHeaderEncoding("UTF-8"); // 处理文件名中文
    FileItemIterator iter = upload.getItemIterator(request);
    while (iter.hasNext()) {
      if (null == object)
        object = new JSONObject();
      FileItemStream item = iter.next();
      InputStream stream = item.openStream();
      if (item.isFormField()) {
        object.put(item.getFieldName(), readStringByInputStream(stream));
      } else {
        // object.put("appIconFileName",
        // getFileNameByFilePath(item.getName()));
        object.put(item.getFieldName(), readBase64StringByInputStream(stream));
      }
    }

    return object;
  }

  public static JSONObject readJSONByFlashStream(HttpServletRequest request) throws Exception {
    JSONObject object = new JSONObject();
    InputStream is = request.getInputStream();
    String fileName  = request.getParameter("name");
    object.put("ext", fileName.substring(fileName.lastIndexOf(".")));
    object.put("Filename", request.getParameter("name").substring(0, fileName.lastIndexOf(".")));
    object.put("filenameExt", request.getParameter("name"));
    object.put("file", readBase64StringByInputStream(is));
    return object;
  }

  public static boolean generateImage(String imgStr, String imgFile) throws Exception {
    // 对字节数组字符串进行Base64解码并生成图片
    if (imgStr == null) // 图像数据为空
      return false;
    BASE64Decoder decoder = new BASE64Decoder();
    try {
      // Base64解码
      byte[] b = decoder.decodeBuffer(imgStr);
      for (int i = 0; i < b.length; ++i) {
        if (b[i] < 0) {// 调整异常数据
          b[i] += 256;
        }
      }
      // 生成jpeg图片
      String imgFilePath = imgFile;// 新生成的图片
      OutputStream out = new FileOutputStream(imgFilePath);
      out.write(b);
      out.flush();
      out.close();
      return true;
    } catch (Exception e) {
      throw e;
    }
  }

  public static void saveFile(InputStream is, String realPath, String fileName) throws Exception {
    // String path = request.getServletContext().getRealPath("/version");//
    // 获取文件要保存的目录
    File path = new File(realPath);
    File file = new File(realPath + File.separator + fileName);
    if (!path.exists()) {
      path.mkdirs();
    }
    if (!file.exists()) {
      file.createNewFile();
    }
    byte[] readBytes = new byte[255];
    int len = -1;
    FileOutputStream fos = new FileOutputStream(file);
    while ((len = is.read(readBytes)) != -1) {
      fos.write(readBytes, 0, len);
    }
    fos.flush();
    fos.close();
    is.close();
  }
}
