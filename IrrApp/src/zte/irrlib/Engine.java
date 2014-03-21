package zte.irrlib;

import zte.irrlib.core.Vector2i;
import zte.irrlib.scene.Scene;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * 3D引擎类，负责管理引擎的创建和销毁，处理与文件系统相关的事务，
 * 并提供接口供视图类调用。
 */
public class Engine{
	
	/** 日志标签*/
	public static final String TAG = "IrrEngine";
	/** assets目录前缀*/
	public static final String ASSETS_PATH = "<assets>/";
	/** 系统内置资源assets目录名*/
	public static final String SYSTEM_MEDIA = "sysmedia";
	/** 系统内置资源assets目录完整路径*/
	public static final String SYSTEM_MEDIA_FULL = "<assets>/sysmedia/";
	
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
	 * 慎重使用，释放引擎所使用的native和Java内存，当且仅当在一个程
	 * 序中不再使用引擎时调用。<br>
	 * 该方法存在的原因是你可能不再需要引擎，且你需要释放引擎占据的内存
	 * 空间留给别的模块。<br>
	 * 一种典型的情况是你用引擎仅仅作为欢迎界面的渲染器，那么当欢迎界
	 * 面结束时，你应当调用该方法释放资源。如果欢迎界面结束后，你仍有
	 * 可能在别的地方用到该引擎，请不要使用本方法：引擎不会因为频繁的
	 * 初始化而造成内存溢出，而在不合适的时机调用该方法则可能造成程序
	 * 崩溃。<br>
	 * 注意：本方法不建议在EGL上下文中（如）时候使用，否则可能会
	 * 出现材质丢失的问题。
	 */
	public static void release(){
		nativeRelease();
		Scene.release();
		mUniInstance = null;
		mJNIIsInit = false;
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
	
	/**
	 * 将assets中的一个文件夹添加入引擎文件系统，这个方法并不会搜索文件夹下的子文件夹，
	 * 添加目录之后，可以在资源路径之前添加{@link #SYSTEM_MEDIA_FULL}以表示这是个
	 * assets目录。
	 * @param dirname 目录名称，根目录用用空字符串代表
	 * @param ignorePath 在将来查找文件时，是否忽略路径
	 * @return 是否成功添加
	 */
	public boolean addAssetsDir(String dirname, boolean ignorePath){
		return nativeAddAssetsDir(dirname, ignorePath);
	}
	
	public synchronized void onSurfaceCreated(IrrlichtView view){
		//nativeInit(mRenderType, new Vector3d(), new Color4i(), new Color3i(), new Rect4i(), new BoundingBox());
		nativeInitAssetManager(view.getContext().getAssets());
		nativeCreateDevice(view.IsGLES2Enabled()?
				EGL10Ext.EGL_OPENGL_ES2_BIT:EGL10Ext.EGL_OPENGL_ES1_BIT);
		initJNIFieldID();
		nativeSetAssetsPath(ASSETS_PATH);
		addAssetsDir(SYSTEM_MEDIA, false);
		javaReset();
		//nativeTest();
		mRenderer.onCreate(this);
		Log.d(TAG, "OnSurfaceCreated");
	}

	public synchronized void onSurfaceChanged(int width, int height){
		nativeResize(width, height);
		mScene.onResize(width, height);
		mRenderer.onResize(this, width, height);
	}
	
	public synchronized void onDrawFrame(){
		nativeBeginScene();
		mScene.onDrawFrame();
		mRenderer.onDrawFrame(this);
		nativeEndScene();
	}
	
	void setRenderer(Renderer renderer){
		mRenderer = renderer;
	}
	
	private void initJNIFieldID(){
		if (mJNIIsInit) return;
		
		String fname[] = new String[4];
		String fsig[] = new String[4];
		
		fname[0] = "MinEdge"; fsig[0] = "Lzte/irrlib/core/Vector3d;";
		fname[1] = "MaxEdge"; fsig[1] = "Lzte/irrlib/core/Vector3d;";
		nativeInitJNI("zte/irrlib/core/BoundingBox", fname, fsig, 2);
		
		fname[0] = "X"; 		fsig[0] = "D";
		fname[1] = "Y";			fsig[1] = "D";
		nativeInitJNI("zte/irrlib/core/Vector2d", fname, fsig, 2);
		
		fname[0] = "X";			fsig[0] = "D";
		fname[1] = "Y";			fsig[1] = "D";
		fname[2] = "Z"; 		fsig[2] = "D";
		nativeInitJNI("zte/irrlib/core/Vector3d", fname, fsig, 3);
		
		fname[0] = "red"; 		fsig[0] = "I";
		fname[1] = "green";		fsig[1] = "I";
		fname[2] = "blue";		fsig[2] = "I";
		nativeInitJNI("zte/irrlib/core/Color3i", fname, fsig, 3);
		
		fname[0] = "red"; 		fsig[0] = "I";
		fname[1] = "green"; 	fsig[1] = "I";
		fname[2] = "blue"; 		fsig[2] = "I";
		fname[3] = "alpha"; 	fsig[3] = "I";
		nativeInitJNI("zte/irrlib/core/Color4i", fname, fsig, 4);
		
		fname[0] = "Left"; 		fsig[0] = "I";
		fname[1] = "Top"; 		fsig[1] = "I";
		fname[2] = "Right";		fsig[2] = "I";
		fname[3] = "Bottom";	fsig[3] = "I";
		nativeInitJNI("zte/irrlib/core/Rect4i", fname, fsig, 4);		
		
		mJNIIsInit = true;
	}
	
	private void javaReset(){
		mScene = Scene.getInstance(this);
		mScene.javaReset();
	}
	
	private Engine(){
	}
	
	private static Engine mUniInstance;
	private static boolean mJNIIsInit;
	
	private Scene mScene;
	private Renderer mRenderer;
	
	private native void nativeResize(int w, int h);
	private native double nativeGetFPS();
	
	//private native int nativeInit(int rendertype, Vector3d vec, Color4i color4, Color3i color3, Rect4i rect, BoundingBox bbox);
	private native int nativeCreateDevice(int type);
	private static native void nativeRelease();
	private native void nativeInitJNI(String name, String[] fname, String[] fsig, int num);
	private native int nativeInitAssetManager(AssetManager asset);
	
	private native void nativeBeginScene();
	private native void nativeEndScene();
	
	private native boolean nativeAddAssetsDir(String name, boolean ignorePath);
	private native void nativeSetAssetsPath(String path);
	private native void nativeTest();
	
	/**
	 * 引擎的渲染器接口，用于场景渲染，需要用户自己实现。 
	 */
	public interface Renderer {
		void onDrawFrame(Engine engine);
		void onCreate(Engine engine);
		void onResize(Engine engine, int width, int height);
	}
}