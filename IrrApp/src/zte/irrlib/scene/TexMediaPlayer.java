package zte.irrlib.scene;

import java.io.IOException;

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
	TexMediaPlayer(Scene sc){
		super();
		mScene = sc;
	}

	/**
	 * 构造函数，为场景赋值并指定材质ID值
	 * @param sc 所使用的场景对象
	 * @param texId 材质的ID值
	 */
	TexMediaPlayer(Scene sc, int texId){
		this(sc);
		setTexId(texId);
	}

	void setTexId(int id){
		mSurfaceTex = new SurfaceTexture(id);
		mSurfaceTex.setOnFrameAvailableListener(this);
		setSurface(new Surface(mSurfaceTex));
	}

	private SurfaceTexture mSurfaceTex;
	private boolean mNewFrame;
	private Scene mScene;
	
	//! SurfaceTexture.OnFrameAvailableListener
	public synchronized void onFrameAvailable(SurfaceTexture sf) {
		mNewFrame = true;
	}
}
