package examples.e05draw2D;

import zte.irrlib.IrrlichtView;
import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;

/**
 *  本例程演示了如何进行2D绘图
 * @author Roy
 *
 */
public class EDraw2D extends Activity {

	private IrrlichtView mDemo;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.defaultlayout);
		
		mDemo = (IrrlichtView)findViewById(R.id.bview);
		mDemo.setEngineRenderer(new ED2Renderer());
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
