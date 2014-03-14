package zte.irrlib;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * <p>与引擎兼容的视图类的一种实现。
 * <p>该视图类是{@link GLSurfaceView}的子类，继承了来自{@link GLSurfaceView}的绝大部分方法。
 * 同时建立一些新方法来<b>替代</b>父类的方法。注意，为了保证软件的稳定性，请尽量使用
 * 新的方法，避免使用被替代的方法。 
 */
public class IrrlichtView extends GLSurfaceView {
	
	/**
	 * 日志标签
	 */
	public final String TAG = "IrrlichtView";
	
	public IrrlichtView(Context context) {		
		super(context);
		mEngine = Engine.getInstance();
	}
	
	public IrrlichtView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mEngine = Engine.getInstance();
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
	 * @param flag 为true时打开开关
	 */
	public void enableGLES2(boolean flag){
		if (flag){
			mRenderType = EGL10Ext.EGL_OPENGL_ES2_BIT;
			super.setEGLContextClientVersion(2);
			mEngine.setRenderType(EGL10Ext.EGL_OPENGL_ES2_BIT);
		}
		else{
			mRenderType = EGL10Ext.EGL_OPENGL_ES1_BIT;
			super.setEGLContextClientVersion(1);
			mEngine.setRenderType(EGL10Ext.EGL_OPENGL_ES1_BIT);
		}
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
		mEngine.setRenderer(renderer);
		super.setRenderer(mEngine);
		//super.setPreserveEGLContextOnPause(true);
	}
	
	@Override@Deprecated
	public final void setEGLContextClientVersion(int version){}
	@Override@Deprecated
	public final void setRenderer(GLSurfaceView.Renderer renderer){}
	@Override@Deprecated
	public final void setPreserveEGLContextOnPause(boolean flag){}
	
	protected Activity getActivity(){
		return (Activity)getContext();
	}
	
	protected Engine mEngine;
	protected int mRenderType = EGL10Ext.EGL_OPENGL_ES1_BIT;
	
	static {
		System.loadLibrary("irrlicht");
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
