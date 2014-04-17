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
 * <p>与引擎兼容的视图类的一种实现。
 * <p>该视图类是{@link GLSurfaceView}的子类，继承了来自{@link GLSurfaceView}的绝大部分方法。
 * 同时建立一些新方法来<b>替代</b>父类的方法。注意，为了保证软件的稳定性，请尽量使用
 * 新的方法，避免使用被替代的方法。 
 */
public class IrrlichtView extends GLSurfaceView implements GLSurfaceView.Renderer{
	
	/**
	 * 日志标签
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
	 * 可以和{@link #setEGLConfigChooser(EGLConfigChooser)}互相替代。
	 * 本方法简化了设定上下文环境的工作，可以用于初始化一个RGB565，并且
	 * 打开多重采样上下文环境。
	 * @param sampleLevel 多重采样重数
	 */
	public void setRecommendEGLConfigChooser(int sampleLevel){
		setEGLConfigChooser(new RecommedEGLConfigChooser(mRenderType, sampleLevel));
	}
	
	/**
	 * openGL ES2.0的开关，默认关闭。替代{@link #setEGLContextClientVersion(int)}。
	 * 如非必要，请使用openGL ES1.x进行渲染，因为它更快，并且引擎对它的支持更为完善。
	 * @param flag 为true时打开开关
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
	 * 查看openGL ES2.0开关是否打开
	 * @return 为true，则使用ES2.0渲染
	 */
	public boolean IsGLES2Enabled(){
		return mRenderType == EGL10Ext.EGL_OPENGL_ES2_BIT;
	}
	
	/**
	 * native层读取assets的开关。由于native层读取assets的速度非常慢，因此我们提供了禁用
	 * 的手段。关闭此开关之后，将显著降低启动和运行过程中的延迟，但程序员也不能再享受assets
	 * 读取所带来的便利。关闭此开关之后，内置文字处理，NineCubeLayout类，以及与assets相关
	 * 的操作将不可用，并且产生报警日志。程序员必须通过其他方法指定资源路径和访问资源
	 * @param flag 如为true，则打开assets读取功能。默认设置为true
	 */
	public void enableNativeAssetsReader(boolean flag){
		mEnableAssets = flag;
	}
	
	/**
	 * 查询native层读取assets的开关是否打开
	 * @return 如为true，则表示已经打开
	 */
	public boolean isNativeAssetsReaderEnabled(){
		return mEnableAssets;
	}
	
	/**
	 * 替代了{@link #setRenderer(Renderer)}，用于指定视图类的渲染回调
	 * 方法。该方法在整个视图类的生命周期中必须调用一次且只能调用一次。<br>
	 * 以下方法必须在该方法之前被调用：<br>
	 * {@link #enableGLES2(boolean)}<br>
	 * {@link #setRecommendEGLConfigChooser(int)}<br>
	 * {@link #setEGLConfigChooser(EGLConfigChooser)}<br>
	 * {@link #setEGLConfigChooser(boolean)}<br>
	 * {@link #setEGLConfigChooser(int, int, int, int, int, int)}<br>
	 * {@link #setEGLConfigChooser(boolean)}<br>
	 * 以下方法必须在该方法之后调用<br>
	 * {@link #setRecommendEGLConfigChooser(int)}<br>
	 * {@link #requestRender()}<br>
	 * {@link #onPause()}<br>
	 * {@link #onResume()}<br>
	 * {@link #getRenderMode()}<br>
	 * {@link #queueEvent(Runnable)}<br>
	 * @param renderer 渲染器，需要用户实现{@link Engine.Renderer}接口
	 */
	public void setEngineRenderer(Engine.Renderer renderer){
		mRenderer = renderer;
		super.setRenderer(this);
		//super.setPreserveEGLContextOnPause(true);
	}
	
	/**
	 * 取得该视图类的渲染器
	 * @return 渲染器指针
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
