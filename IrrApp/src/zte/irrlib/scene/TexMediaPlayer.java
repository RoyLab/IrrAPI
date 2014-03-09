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
	 * ������Ƶͼ��
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
	 * ���캯����Ϊ������ֵ��
	 * @param sc ��ʹ�õĳ�������
	 */
	TexMediaPlayer(Scene sc){
		super();
		mScene = sc;
	}

	/**
	 * ���캯����Ϊ������ֵ��ָ������IDֵ
	 * @param sc ��ʹ�õĳ�������
	 * @param texId ���ʵ�IDֵ
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
