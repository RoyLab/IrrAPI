package zte.irrlib;

import zte.irrlib.core.Vector2i;
import zte.irrlib.scene.Scene;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * 3D�����࣬�����������Ĵ��������٣��������ļ�ϵͳ��ص�����
 * ���ṩ�ӿڹ���ͼ����á�
 */
public class Engine{
	
	/** ��־��ǩ*/
	public static final String TAG = "IrrEngine";
	/** assetsĿ¼ǰ׺*/
	public static final String ASSETS_PATH = "<assets>/";
	/** ϵͳ������ԴassetsĿ¼��*/
	public static final String SYSTEM_MEDIA = "sysmedia";
	/** ϵͳ������ԴassetsĿ¼����·��*/
	public static final String SYSTEM_MEDIA_FULL = "<assets>/sysmedia/";
	
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
	 * ����ʹ�ã��ͷ�������ʹ�õ�native��Java�ڴ棬���ҽ�����һ����
	 * ���в���ʹ������ʱ���á�<br>
	 * �÷������ڵ�ԭ��������ܲ�����Ҫ���棬������Ҫ�ͷ�����ռ�ݵ��ڴ�
	 * �ռ��������ģ�顣<br>
	 * һ�ֵ��͵�������������������Ϊ��ӭ�������Ⱦ������ô����ӭ��
	 * �����ʱ����Ӧ�����ø÷����ͷ���Դ�������ӭ���������������
	 * �����ڱ�ĵط��õ������棬�벻Ҫʹ�ñ����������治����ΪƵ����
	 * ��ʼ��������ڴ���������ڲ����ʵ�ʱ�����ø÷����������ɳ���
	 * ������<br>
	 * ע�⣺��������������EGL�������У��磩ʱ��ʹ�ã�������ܻ�
	 * ���ֲ��ʶ�ʧ�����⡣
	 */
	public static void release(){
		nativeRelease();
		Scene.release();
		mUniInstance = null;
		mJNIIsInit = false;
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
	
	/**
	 * ��assets�е�һ���ļ�������������ļ�ϵͳ��������������������ļ����µ����ļ��У�
	 * ���Ŀ¼֮�󣬿�������Դ·��֮ǰ���{@link #SYSTEM_MEDIA_FULL}�Ա�ʾ���Ǹ�
	 * assetsĿ¼��
	 * @param dirname Ŀ¼���ƣ���Ŀ¼���ÿ��ַ�������
	 * @param ignorePath �ڽ��������ļ�ʱ���Ƿ����·��
	 * @return �Ƿ�ɹ����
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
	 * �������Ⱦ���ӿڣ����ڳ�����Ⱦ����Ҫ�û��Լ�ʵ�֡� 
	 */
	public interface Renderer {
		void onDrawFrame(Engine engine);
		void onCreate(Engine engine);
		void onResize(Engine engine, int width, int height);
	}
}