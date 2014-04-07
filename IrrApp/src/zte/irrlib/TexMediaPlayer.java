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
	public TexMediaPlayer(Scene sc){
		super();
		mScene = sc;
	}

	/**
	 * ���캯����Ϊ������ֵ��ָ������IDֵ
	 * @param sc ��ʹ�õĳ�������
	 * @param texId ���ʵ�IDֵ
	 */
	public TexMediaPlayer(Scene sc, int texId){
		this(sc);
		setTexId(texId);
	}

	/**
	 * ���ò��������openGL ES����ID���������ID��ָ�������ӵ�openGL ES��
	 * �û�����ʹ��{@link TexMediaPlayer#isConnected2OGLES()}��ѯ����״̬
	 * @param id openGL ES����ID
	 */
	public void setTexId(int id){
		if (id < 0) return;
		
		mSurfaceTex = new SurfaceTexture(id);
		mSurfaceTex.setOnFrameAvailableListener(this);
		setSurface(new Surface(mSurfaceTex));
		mIsConnected = true;
	}
	
	/**
	 * ��ѯ��������Ƿ��Ѿ����ӵ�openGL ES��
	 * @return Ϊtrue���ʾ�Ѿ�����
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
