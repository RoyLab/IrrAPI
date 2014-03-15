package zte.irrlib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;


/**
 * 引擎的辅助工具集，包括一些文件处理相关的静态方法。
 */
public class Utils {

	/**
	 * 日志标签
	 */
	public static final String TAG = "Untils";
	
	/**
	 * 判断某一个文件是否存在。
	 * @param path 文件的绝对路径
	 * @return 若为true，则表示文件存在
	 */
	public static boolean fileIsExists(String path){
	    try{
            File f = new File(path);
            if(!f.exists()){
            	return false;
            }
	    }catch (Exception e){
            return false;
	    }
	    return true;
	}
	
	/**
	 * 判断一个路径是否为绝对路径。实际上是判断该字符串是否以'/'开头。
	 * @param path 路径字符串
	 * @return 若为true，则表示字符串为绝对路径
	 */
	public static boolean isAbsolutePath(String path){
		return (path.charAt(0) == '/');
	}
	
	/**
	 * 将文件从asset中指定目录下的文件复制到另一个指定目录下。该方法不会复制子文件夹。
	 * @param assetManager asset管理器
	 * @param source 需要复制的目录在asset中的相对路径。若设为""，则表示在根目录下
	 * @param desPath 目标目录的绝对路径
	 * @return 若为true，则表示成功复制
	 */
	public static boolean copyAssets(AssetManager assetManager, String source, String desPath) 
			throws IOException{
		
		File checkFile = new File(desPath);
		if (checkFile.exists()){
			if (!checkFile.isDirectory()){
				Log.w(TAG, desPath + " exists but not a directory.");
				return false;
			}
		} else {
			checkFile.mkdirs();
		}
		
		String[] fileList = assetManager.list(source);
		for(String file:fileList){
			InputStream input;
			try{
				 input = assetManager.open(source+"/"+file);
			}catch (IOException e){
				Log.i(TAG, "Skip dir: " + file);
				continue;
			}
			OutputStream output =
					new FileOutputStream(desPath+"/"+file);
			byte[] buffer = new byte[4096];
			int length;
			while ((length = input.read(buffer)) > 0){
				output.write(buffer, 0, length);
			}
			output.flush();
			output.close();
			input.close();
		}
		return true;
	}
	
	public void UtilsInit(AssetManager assetManager){
		try {
            unpackOnSdCard(assetManager);
        } catch (IOException e) {
            Log.i("Irrlicht", "Error in unpack "+e.getMessage());
        }
		try {
            setSDCardPath();
            Log.i("new","I want to see!");
        } catch (IOException e) {
            Log.i("Irrlicht", "Error in setSdCardPath  "+e.getMessage());
        }
	}
	public void unpackOnSdCard(AssetManager assetManager) throws IOException {
        if (Environment.getExternalStorageState().compareTo(Environment.MEDIA_MOUNTED)==0) {
            File sdcard = Environment.getExternalStorageDirectory();
            String irrlichtPath = sdcard.getAbsoluteFile() + "/Irrlicht/";
            File irrlichtDir = new File(irrlichtPath);
            if (irrlichtDir.exists() && !irrlichtDir.isDirectory()) {
                throw new IOException("Irrlicht exists and is not a directory on SD Card");
            } else if (!irrlichtDir.exists()) {
                irrlichtDir.mkdirs();
            }
            // Note: /sdcard/irrlicht dir exists
            String[] filenames = assetManager.list("data");
            for(String filename:filenames) {

            	Log.i("Irrlicht", "filename"+filename);
                InputStream inputStream = assetManager.open("data/" + filename);
                OutputStream outputStream = new FileOutputStream(irrlichtPath + "/" + filename);
                // copy
                byte[] buffer = new byte[4096];
                int length;
                while ( (length = inputStream.read(buffer)) > 0 ) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }  
            	
        } else {
            throw new IOException("SD Card not available");
        }

    }
	
	public boolean setSDCardPath() throws IOException{
		if (Environment.getExternalStorageState().compareTo(Environment.MEDIA_MOUNTED)==0) {
			File sdcard = Environment.getExternalStorageDirectory();
			nativeSetSdCardPath(sdcard.getAbsolutePath());
		}
		else {
			throw new IOException("SD Card not available");
		}
		return true;
	}
	
	private native void nativeSetSdCardPath(String path);
	
	static {
		System.loadLibrary("irrlicht");
	}

}
