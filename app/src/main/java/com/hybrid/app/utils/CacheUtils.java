package com.hybrid.app.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;

/**
 * 文件处理类
 */
public class CacheUtils {

	public static final  String  DIRNAME = File.separator + "webveiw"+ File.separator +"cache";

	public  String  APP_CACHE_DIRNAME = null;


	public static String  getCachePath(Context context){
		return setMkdir(Environment.getExternalStorageDirectory().getPath().toString()+ File.separator + context.getPackageName() + CacheUtils.DIRNAME).getPath();
	}

	
	/**
	 * 创建目录
	 * @param path
	 */
	public static File setMkdir(String path)
	{
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
			Log.e("file", "目录不存在  创建目录    ");
			return file;
		}else{
			Log.e("file", "目录存在");
			return file;
		}
	}
	
	/**
	 * 获取目录名称
	 * @param url
	 * @return FileName
	 */
	public static String getFileName(String url)
	{
		int lastIndexStart = url.lastIndexOf("/");
		if(lastIndexStart!=-1)
		{
			return url.substring(lastIndexStart+1, url.length());
		}else{
			return null;
		}
	}
	
	/**
	 * 删除该目录下的文件
	 * 
	 * @param path
	 */
	public static void delFile(String path) {
		if (!TextUtils.isEmpty(path)) {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
		}
	}
}
