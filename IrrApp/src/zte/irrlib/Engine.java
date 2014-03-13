package zte.irrlib;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import zte.irrlib.core.BoundingBox;
import zte.irrlib.core.Color3i;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Rect4i;
import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.Scene;
import android.opengl.GLSurfaceView;
import android.util.Log;

/**
 * 3D引擎类，负责管理引擎的创建和销毁，处理与文件系统相关的事务，
 * 并提供接口供视图类调用。
 */
public class Engine implements GLSurfaceView.Renderer{
	
	/**
	 * 日志标签
	 */
	public static final String TAG = "IrrEngine";
	
	/**
	 * 构造器不可以被直接调用，本方法替代构造方法，用于取得
	 * 该类的实例。在程序运行时，任何时刻调用本方法取得的实
	 * 例将是同一个实例（单例模式）。
	 * @return 返回该类的实例
	 */
	public static Engine getInstance(){
		if (mUniInstance == null){
			mUniInstance = new Engine();
		}
		return mUniInstance;
	}
	
	/**
	 * 设定资源文件（材质，模型等）的绝对路径。
	 * @param path 路径名。请以'/'为开头，并以'/'为结尾
	 */
	public void setResourceDir(String path){
		mScene.setResourceDir(path);
	}
	
	/**
	 * 取得引擎内部的场景类。
	 * @return 场景类的指针
	 */
	public Scene getScene(){
		return mScene;
	}
	
	/**
	 * 取得资源文件目录的绝对路径。可以通过{@link #setResourceDir(String)}设置路径。
	 * @return 目录的绝对路径
	 */
	public String getResourceDir(){
		return mScene.getResourceDir();
	}
	
	/**
	 * 取得画布大小。
	 * @return 画布大小
	 */
	public Vector2i getRenderSize(){
		return mScene.getRenderSize();
	}
	
	/**
	 * 取得当前渲染的帧率。
	 * @return 帧率（fps）
	 */
	public double getFPS(){
		return nativeGetFPS();
	}
	
	public synchronized void onSurfaceDestroyed(){
		mScene.clearAllNodes();
		if (mIsInit) javaClear();
		if (nativeIsInit()) nativeClear();
		Log.d(TAG, "OnDestroy");
	}
	
	@Override
	public synchronized void onSurfaceCreated(GL10 unused, EGLConfig config){
		if (!checkState()){
			mScene = Scene.getInstance(this);
			
			//do some clean
			if (mIsInit) javaClear();
			if (nativeIsInit()) nativeClear();
			
			//re-initialize
			nativeInit(mRenderType, new Vector3d(), new Color4i(), new Color3i(), new Rect4i(), new BoundingBox());
			JavaInit();
			mRenderer.onCreate(this);
			Log.d(TAG, "Renderer created");
		}
		Log.d(TAG, "OnSurfaceCreated");
	}
	
	@Override
	public synchronized void onSurfaceChanged(GL10 unused, int width, int height){
		nativeResize(width, height);
		mScene.onResize(width, height);
		mRenderer.onResize(this, width, height);
	}
	
	@Override
	public synchronized void onDrawFrame(GL10 unused){
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
	
	//当且仅当native引擎和Java接口都已经被初始化时返回true
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
	
	private native int nativeInit(int rendertype, Vector3d vec, Color4i color4, Color3i color3, Rect4i rect, BoundingBox bbox);
	private native void nativeClear();
	private native boolean nativeIsInit();
	private native void nativeResize(int w, int h);
	private native double nativeGetFPS();
	
	native void nativeBeginScene();
	native void nativeEndScene();
	
	/**
	 * 引擎的渲染器接口，用于场景渲染，需要用户自己实现。 
	 */
	public interface Renderer {
		void onDrawFrame(Engine engine);
		void onCreate(Engine engine);
		void onResize(Engine engine, int width, int height);
	}
}