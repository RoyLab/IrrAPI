package examples.e02config;

import zte.irrlib.IrrlichtView;
import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * 本例程演示了如何进行引擎的基本配置
 * @author Roy
 *
 */
public class EConfig extends Activity {

	private IrrlichtView mDemo;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.defaultlayout);
		
		mDemo = (IrrlichtView)findViewById(R.id.bview);
		/**
		 * 如果要使用GL ES2（通常这不是必要的）渲染，可以调用enableGLES2
		 * 打开渲染开关。默认情况下，使用GL ES1渲染，它更稳定，功能更全，效
		 * 率更高。该函数（如果需要）必须在setRecommendEGLConfigChooser之前调用。
		 */
		mDemo.enableGLES2(true);
		/**
		 * 进行简单的egl上下文环境配置。你可以编写自己的配置方法，并使用setEGLConfigChooser
		 * 来代替这个函数。该函数必须在setEngineRenderer前调用。配置egl环境在某些设备中是必须的。
		 */
		mDemo.setRecommendEGLConfigChooser(8);
		/**
		 * 禁用native层读取assets的功能，可以较为明显的提升性能，但会导致部分功能不可用，并且
		 * 不能通过native引擎直接读取assets中的资源
		 */
		//mDemo.enableNativeAssetsReader(false);
		
		/**
		 * 除了这些外，绝大部分用于GLSurfaceView的设置也可以在这里使用，比如渲染模式（连续渲染和
		 * 触发渲染）等，详见GLSurfaceView的文档
		 */
		
		/**
		 * 最后不要忘记，一定要设定一个渲染器
		 */
		mDemo.setEngineRenderer(new ECRenderer());
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if (mDemo != null){
			mDemo.onResume();
		}
	}
	
	@Override
	protected void onPause(){
		if (mDemo != null){
			mDemo.onPause();
		}
		super.onPause();
	}
}