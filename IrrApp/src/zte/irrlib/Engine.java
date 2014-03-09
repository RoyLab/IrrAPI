package zte.irrlib;

import zte.irrlib.core.Color3i;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Rect4i;
import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.Scene;
import android.util.Log;

/**
 * 3D�����࣬�����������Ĵ��������٣��������ļ�ϵͳ��ص�����
 * ���ṩ�ӿڹ���ͼ����á�
 */
public class Engine{
	
	/**
	 * ��־��ǩ
	 */
	public static final String TAG = "IrrEngine";
	
	/**
	 * �����������Ա�ֱ�ӵ��ã�������������췽��������ȡ��
	 * �����ʵ�����ڳ�������ʱ���κ�ʱ�̵��ñ�����ȡ�õ�ʵ
	 * ������ͬһ��ʵ��������ģʽ����
	 * @return ���ظ����ʵ��
	 */
	public static Engine getInstance(){
		if (mUniInstance == null){
			mUniInstance = new Engine();
		}
		return mUniInstance;
	}
	
	/**
	 * �趨��Դ�ļ������ʣ�ģ�͵ȣ��ľ���·����
	 * @param path ·����������'/'Ϊ��ͷ������'/'Ϊ��β
	 */
	public void setResourceDir(String path){
		mScene.setResourceDir(path);
	}
	
	/**
	 * ȡ�������ڲ��ĳ����ࡣ
	 * @return �������ָ��
	 */
	public Scene getScene(){
		return mScene;
	}
	
	/**
	 * ȡ����Դ�ļ�Ŀ¼�ľ���·��������ͨ��{@link #setResourceDir(String)}����·����
	 * @return Ŀ¼�ľ���·��
	 */
	public String getResourceDir(){
		return mScene.getResourceDir();
	}
	
	/**
	 * ȡ�û�����С��
	 * @return ������С
	 */
	public Vector2i getRenderSize(){
		return mScene.getRenderSize();
	}
	
	/**
	 * ȡ�õ�ǰ��Ⱦ��֡�ʡ�
	 * @return ֡�ʣ�fps��
	 */
	public double getFPS(){
		return nativeGetFPS();
	}
	
	synchronized void onDestroy(){
		if (mIsInit) javaClear();
		if (nativeIsInit()) nativeClear();
		Log.d(TAG, "OnDestroy");
	}
	
	synchronized void onSurfaceCreated(){
		if (!checkState()){
			mScene = Scene.getInstance(this);
			
			//do some clean
			if (mIsInit) javaClear();
			if (nativeIsInit()) nativeClear();
			
			//re-initialize
			nativeInit(mRenderType, new Vector3d(), new Color4i(), new Color3i(), new Rect4i());
			JavaInit();
			mRenderer.onCreate(this);
			Log.d(TAG, "Renderer created");
		}
		Log.d(TAG, "OnSurfaceCreated");
	}
	
	void onSurfaceChanged(int width, int height){
		nativeResize(width, height);
		mScene.onResize(width, height);
		mRenderer.onResize(this, width, height);
	}
	
	void onDrawFrame(){
		nativeBeginScene();
		mScene.onDrawFrame();
		mRenderer.onDrawFrame(this);
		nativeEndScene();
	}
	
	void setRenderer(Renderer renderer){
		mRenderer = renderer;
	}

	void setRenderType(int type){
		mRenderType = type;
	}

	private int JavaInit(){
		mScene.init();
		mIsInit = true; 
		return 0;
	}
	
	private void javaClear(){
		mScene.javaClear();
		mIsInit = false; 
	}
	
	//���ҽ���native�����Java�ӿڶ��Ѿ�����ʼ��ʱ����true
	private boolean checkState(){
		return (mIsInit && nativeIsInit());
	}
	
	private Engine(){
	}
	
	private static Engine mUniInstance;
	
	private Scene mScene;
	private Renderer mRenderer;
	private int mRenderType = EGL10Ext.EGL_OPENGL_ES1_BIT;
	
	private boolean mIsInit;
	
	private native int nativeInit(int rendertype, Vector3d vec, Color4i color4, Color3i color3, Rect4i rect);
	private native void nativeClear();
	private native boolean nativeIsInit();
	private native void nativeResize(int w, int h);
	private native double nativeGetFPS();
	
	native void nativeBeginScene();
	native void nativeEndScene();
	
	/**
	 * �������Ⱦ���ӿڣ����ڳ�����Ⱦ����Ҫ�û��Լ�ʵ�֡� 
	 */
	public interface Renderer {
		void onDrawFrame(Engine engine);
		void onCreate(Engine engine);
		void onResize(Engine engine, int width, int height);
	}
}