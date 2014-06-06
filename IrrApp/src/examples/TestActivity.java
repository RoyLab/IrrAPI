package examples;

import zte.irrlib.IrrlichtView;
import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * 这是个测试用的模块，可以删除
 * @author Roy
 *
 */
public class TestActivity extends Activity {
	
	private IrrlichtView mDemo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.defaultlayout);
		mDemo = (IrrlichtView)findViewById(R.id.bview);
		
		mDemo.setRecommendEGLConfigChooser(0);
		mDemo.setEngineRenderer(new TestRenderer());
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
