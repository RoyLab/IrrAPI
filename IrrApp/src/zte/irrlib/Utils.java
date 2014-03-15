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
 * ����ĸ������߼�������һЩ�ļ�������صľ�̬������
 */
public class Utils {

	/**
	 * ��־��ǩ
	 */
	public static final String TAG = "Untils";
	
	/**
	 * �ж�ĳһ���ļ��Ƿ���ڡ�
	 * @param path �ļ��ľ���·��
	 * @return ��Ϊtrue�����ʾ�ļ�����
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
	 * �ж�һ��·���Ƿ�Ϊ����·����ʵ�������жϸ��ַ����Ƿ���'/'��ͷ��
	 * @param path ·���ַ���
	 * @return ��Ϊtrue�����ʾ�ַ���Ϊ����·��
	 */
	public static boolean isAbsolutePath(String path){
		return (path.charAt(0) == '/');
	}
	
	/**
	 * ���ļ���asset��ָ��Ŀ¼�µ��ļ����Ƶ���һ��ָ��Ŀ¼�¡��÷������Ḵ�����ļ��С�
	 * @param assetManager asset������
	 * @param source ��Ҫ���Ƶ�Ŀ¼��asset�е����·��������Ϊ""�����ʾ�ڸ�Ŀ¼��
	 * @param desPath Ŀ��Ŀ¼�ľ���·��
	 * @return ��Ϊtrue�����ʾ�ɹ�����
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
