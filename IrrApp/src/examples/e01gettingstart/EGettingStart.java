package examples.e01gettingstart;

import zte.irrlib.IrrlichtView;
import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * 本例程演示了一个最小的程序需要的编码。
 * @author Roy
 *
 */
public class EGettingStart extends Activity {

	private IrrlichtView mDemo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.defaultlayout);
		
		mDemo = (IrrlichtView)findViewById(R.id.bview);
		
		/**
		 * 进行简单的egl上下文环境配置。你可以编写自己的配置方法，并使用setEGLConfigChooser
		 * 来代替这个函数。该函数必须在setEngineRenderer前调用。配置egl环境在某些设备中是必须的。
		 */
		mDemo.setRecommendEGLConfigChooser(0);
		
		/**本软件包提供了一个封装好的视图类以调用鬼火引擎，该视图类是
		 * GLSurfaceView类的子类，因此继承了此类的很多GLSurfaceView
		 * 的方法。其中最为重要的是setEngineRenderer，只有设定了合适
		 * 的渲染器，才能使得视图类正常工作。
		 */
		mDemo.setEngineRenderer(new EGSRenderer());
	}
	
	/**
	 * 因为渲染线程是独立于主线程的，因此渲染线程并
	 * 不会在主线程暂停的时候暂停
	 * 视图类的上下文必须在它暂停的时候通知视图类。
	 */
	@Override
	protected void onResume(){
		super.onResume();
		if (mDemo != null){
			mDemo.onResume();
		}
	}
	
	/**
	 * 因为渲染线程是独立于主线程的，因此渲染线程并
	 * 不会在主线程恢复的时候恢复
	 * 视图类的上下文必须在它恢复的时候通知视图类。
	 */
	@Override
	protected void onPause(){
		if (mDemo != null){
			mDemo.onPause();
		}
		super.onPause();
	}
}
