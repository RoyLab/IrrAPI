package zte.irrlib;

import java.io.IOException;

import zte.irrlib.scene.Scene;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;

public class TexMediaPlayer extends MediaPlayer
	implements SurfaceTexture.OnFrameAvailableListener{
	
	/**
	 * Log Tag
	 */
	public static final String TAG = "TexMediaPlayer";
	
	/**
	 * 更新视频图像
	 */
	public synchronized void update(){
		if ((mSurfaceTex!=null) && mNewFrame){
			mSurfaceTex.updateTexImage();
			mNewFrame = false;
		}
	}
	
	@Override
	public void setDataSource(String path)
			throws IllegalArgumentException, SecurityException,
			IllegalStateException, IOException{
		
		super.setDataSource(mScene.getFullPath(path));
	}
	
	/**
	 * 构造函数，为场景赋值。
	 * @param sc 所使用的场景对象
	 */
	public TexMediaPlayer(Scene sc){
		super();
		mScene = sc;
	}

	/**
	 * 构造函数，为场景赋值并指定材质ID值
	 * @param sc 所使用的场景对象
	 * @param texId 材质的ID值
	 */
	public TexMediaPlayer(Scene sc, int texId){
		this(sc);
		setTexId(texId);
	}

	/**
	 * 设置材质相机的openGL ES纹理ID，相机将在ID被指定后连接到openGL ES。
	 * 用户可以使用{@link TexMediaPlayer#isConnected2OGLES()}查询连接状态
	 * @param id openGL ES纹理ID
	 */
	public void setTexId(int id){
		if (id < 0) return;
		
		mSurfaceTex = new SurfaceTexture(id);
		mSurfaceTex.setOnFrameAvailableListener(this);
		setSurface(new Surface(mSurfaceTex));
		mIsConnected = true;
	}
	
	/**
	 * 查询材质相机是否已经连接到openGL ES。
	 * @return 为true则表示已经连接
	 */
	public boolean isConnected2OGLES(){
		return mIsConnected;
	}

	private SurfaceTexture mSurfaceTex;
	private boolean mNewFrame;
	private Scene mScene;
	private boolean mIsConnected;
	
	//! SurfaceTexture.OnFrameAvailableListener
	public synchronized void onFrameAvailable(SurfaceTexture sf) {
		mNewFrame = true;
	}
}
