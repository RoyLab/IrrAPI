package zte.irrlib;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * <p>��������ݵ���ͼ���һ��ʵ�֡�
 * <p>����ͼ����{@link GLSurfaceView}�����࣬�̳�������{@link GLSurfaceView}�ľ��󲿷ַ�����
 * ͬʱ����һЩ�·�����<b>���</b>����ķ�����ע�⣬Ϊ�˱�֤������ȶ��ԣ��뾡��ʹ��
 * �µķ���������ʹ�ñ�����ķ����� 
 */
public class IrrlichtView extends GLSurfaceView implements GLSurfaceView.Renderer{
	
	/**
	 * ��־��ǩ
	 */
	public final String TAG = "IrrlichtView";
	
	public IrrlichtView(Context context) {		
		super(context);
		mEngine = Engine.getInstance();
		enableGLES2(false);
	}
	
	public IrrlichtView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mEngine = Engine.getInstance();
		enableGLES2(false);
	}
	
	/**
	 * ���Ժ�{@link #setEGLConfigChooser(EGLConfigChooser)}���������
	 * �����������趨�����Ļ����Ĺ������������ڳ�ʼ��һ��RGB565������
	 * �򿪶��ز��������Ļ�����
	 * @param sampleLevel ���ز�������
	 */
	public void setRecommendEGLConfigChooser(int sampleLevel){
		setEGLConfigChooser(new RecommedEGLConfigChooser(mRenderType, sampleLevel));
	}
	
	/**
	 * openGL ES2.0�Ŀ��أ�Ĭ�Ϲرա����{@link #setEGLContextClientVersion(int)}��
	 * ��Ǳ�Ҫ����ʹ��openGL ES1.x������Ⱦ����Ϊ�����죬�������������֧�ָ�Ϊ���ơ�
	 * @param flag Ϊtrueʱ�򿪿���
	 */
	public void enableGLES2(boolean flag){
		if (flag){
			mRenderType = EGL10Ext.EGL_OPENGL_ES2_BIT;
			super.setEGLContextClientVersion(2);
		}
		else{
			mRenderType = EGL10Ext.EGL_OPENGL_ES1_BIT;
			super.setEGLContextClientVersion(1);
		}
	}
	
	/**
	 * �鿴openGL ES2.0�����Ƿ��
	 * @return Ϊtrue����ʹ��ES2.0��Ⱦ
	 */
	public boolean IsGLES2Enabled(){
		return mRenderType == EGL10Ext.EGL_OPENGL_ES2_BIT;
	}
	
	/**
	 * native���ȡassets�Ŀ��ء�����native���ȡassets���ٶȷǳ�������������ṩ�˽���
	 * ���ֶΡ��رմ˿���֮�󣬽������������������й����е��ӳ٣�������ԱҲ����������assets
	 * ��ȡ�������ı������رմ˿���֮���������ִ���NineCubeLayout�࣬�Լ���assets���
	 * �Ĳ����������ã����Ҳ���������־������Ա����ͨ����������ָ����Դ·���ͷ�����Դ
	 * @param flag ��Ϊtrue�����assets��ȡ���ܡ�Ĭ������Ϊtrue
	 */
	public void enableNativeAssetsReader(boolean flag){
		mEnableAssets = flag;
	}
	
	/**
	 * ��ѯnative���ȡassets�Ŀ����Ƿ��
	 * @return ��Ϊtrue�����ʾ�Ѿ���
	 */
	public boolean isNativeAssetsReaderEnabled(){
		return mEnableAssets;
	}
	
	/**
	 * �����{@link #setRenderer(Renderer)}������ָ����ͼ�����Ⱦ�ص�
	 * �������÷�����������ͼ������������б������һ����ֻ�ܵ���һ�Ρ�<br>
	 * ���·��������ڸ÷���֮ǰ�����ã�<br>
	 * {@link #enableGLES2(boolean)}<br>
	 * {@link #setRecommendEGLConfigChooser(int)}<br>
	 * {@link #setEGLConfigChooser(EGLConfigChooser)}<br>
	 * {@link #setEGLConfigChooser(boolean)}<br>
	 * {@link #setEGLConfigChooser(int, int, int, int, int, int)}<br>
	 * {@link #setEGLConfigChooser(boolean)}<br>
	 * ���·��������ڸ÷���֮�����<br>
	 * {@link #setRecommendEGLConfigChooser(int)}<br>
	 * {@link #requestRender()}<br>
	 * {@link #onPause()}<br>
	 * {@link #onResume()}<br>
	 * {@link #getRenderMode()}<br>
	 * {@link #queueEvent(Runnable)}<br>
	 * @param renderer ��Ⱦ������Ҫ�û�ʵ��{@link Engine.Renderer}�ӿ�
	 */
	public void setEngineRenderer(Engine.Renderer renderer){
		mRenderer = renderer;
		super.setRenderer(this);
		//super.setPreserveEGLContextOnPause(true);
	}
	
	/**
	 * ȡ�ø���ͼ�����Ⱦ��
	 * @return ��Ⱦ��ָ��
	 */
	public Engine.Renderer getRenderer(){
		return mRenderer;
	}
	
	@Override@Deprecated
	public final void setEGLContextClientVersion(int version){}
	@Override@Deprecated
	public final void setRenderer(GLSurfaceView.Renderer renderer){}
	@Override@Deprecated
	public final void setPreserveEGLContextOnPause(boolean flag){}
	
	protected Engine mEngine;
	protected boolean mEnableAssets = true;
	protected Engine.Renderer mRenderer;
	protected int mRenderType = EGL10Ext.EGL_OPENGL_ES1_BIT;
	
	public void onDrawFrame(GL10 gl) {
		mEngine.onDrawFrame();
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
			mEngine.onSurfaceChanged(width, height);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			mEngine.onSurfaceCreated(this);
	}
}

class RecommedEGLConfigChooser implements GLSurfaceView.EGLConfigChooser{
	
	public static final String TAG = "IrrConfigChooser";
	
	public RecommedEGLConfigChooser(int renderType, int sampleLevel){
		mRenderType = renderType;
		mSampleLevel = sampleLevel;
	}
	
	public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
        int numConfigs[] = new int[1];
        int[] configSpec = null;
        
        if (mSampleLevel != 0){
	        // Try to find a normal multisample configuration first.
	        configSpec = new int[]{
	                EGL10.EGL_RED_SIZE, 5,
	                EGL10.EGL_GREEN_SIZE, 6,
	                EGL10.EGL_BLUE_SIZE, 5,
	                EGL10.EGL_DEPTH_SIZE, 16,
	                EGL10.EGL_RENDERABLE_TYPE, mRenderType,
	                EGL10.EGL_SAMPLE_BUFFERS, 1 /* true */,
	                EGL10.EGL_SAMPLES, mSampleLevel,
	                EGL10.EGL_NONE
	        };
	        if (!egl.eglChooseConfig(display, configSpec, null, 0, numConfigs)) {
	            throw new IllegalArgumentException("eglChooseConfig failed: code 1");
	        }
	        mState = 1;
	        
	        if (numConfigs[0] <= 0) {
	            // No normal multisampling configuration was found. Try to create a
	            // multisampling configuration, for the nVidia Tegra2.
	            // See the EGL_NV_coverage_sample documentation.
	
	            configSpec = new int[]{
	                    EGL10.EGL_RED_SIZE, 5,
	                    EGL10.EGL_GREEN_SIZE, 6,
	                    EGL10.EGL_BLUE_SIZE, 5,
	                    EGL10.EGL_DEPTH_SIZE, 16,
	                    EGL10.EGL_RENDERABLE_TYPE, mRenderType,
	                    EGL10Ext.EGL_COVERAGE_BUFFERS_NV, 1 /* true */,
	                    EGL10Ext.EGL_COVERAGE_SAMPLES_NV, 2,  // always 5 in practice on tegra 2
	                    EGL10.EGL_NONE
	            };
	
	            if (!egl.eglChooseConfig(display, configSpec, null, 0, numConfigs)) {
	                throw new IllegalArgumentException("eglChooseConfig failed: code 2");
	            }
	            mState = 2;
	        }
        }
        
        if (numConfigs[0] <= 0 || mSampleLevel == 0) {
	        //do without multisampling.
	        configSpec = new int[]{
	                EGL10.EGL_RED_SIZE, 5,
	                EGL10.EGL_GREEN_SIZE, 6,
	                EGL10.EGL_BLUE_SIZE, 5,
	                EGL10.EGL_DEPTH_SIZE, 16,
	                EGL10.EGL_RENDERABLE_TYPE, mRenderType,
	                EGL10.EGL_NONE
	        };

	        if (!egl.eglChooseConfig(display, configSpec, null, 0, numConfigs)) {
	            throw new IllegalArgumentException("eglChooseConfig failed: code 3");
	        }
	        mState = 3;
	        if (numConfigs[0] <= 0) {
	          throw new IllegalArgumentException("No configs match configSpec!");
	        }
        }
        
        //logger
        switch (mState){
        case 1:
        	Log.d(TAG, "Normal: level " + mSampleLevel);
        	break;
        case 2:
        	Log.d(TAG, "Coverage: level " + mSampleLevel);
        	break;
        case 3:
        	Log.d(TAG, "None: level " + mSampleLevel);
        	break;
        default:
        	Log.w(TAG, "Unexpected condition.");
        }

        // Get all matching configurations.
        int numValue = numConfigs[0];
        EGLConfig[] configs = new EGLConfig[numValue];
        if (!egl.eglChooseConfig(display, configSpec, configs, numValue, numConfigs)) {
            throw new IllegalArgumentException("get eglChooseConfig data failed!");
        }

        // CAUTION! eglChooseConfigs returns configs with higher bit depth
        // first: Even though we asked for rgb565 configurations, rgb888
        // configurations are considered to be "better" and returned first.
        // You need to explicitly filter the data returned by eglChooseConfig!
        EGLConfig config = null;
        for (EGLConfig configIter: configs) {
            if (findConfigAttrib(egl, display, configIter, EGL10.EGL_RED_SIZE, -1) == 5) {
                config = configIter;
                break;
            }
        }
        if (config == null) {
            Log.w(toString(), "Did not find sane config, using first");
            config = configs[0];
        }
        return config;
	}
        
    private int findConfigAttrib(EGL10 egl, EGLDisplay display,
            EGLConfig config, int attribute, int defaultValue) {
        if (egl.eglGetConfigAttrib(display, config, attribute, tmp)) {
            return tmp[0];
        }
        return defaultValue;
    }
	
	protected int mRenderType = EGL10Ext.EGL_OPENGL_ES1_BIT;
	protected int mSampleLevel = 0;
	protected int mState = 0;
	
	//create to avoid frequent new operation in findConfigAttrib
	private int tmp[] = new int[1];
}
//define some hard code in EGL10
class EGL10Ext{
	public static final int EGL_OPENGL_ES1_BIT = 0x00000001;
	public static final int EGL_OPENGL_ES2_BIT = 0x00000004;
	public static final int EGL_COVERAGE_BUFFERS_NV = 0x30E0;
	public static final int EGL_COVERAGE_SAMPLES_NV = 0x30E1;	
}
