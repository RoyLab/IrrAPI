package examples.e12ps;

import zte.irrlib.IrrlichtView;
import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;
import examples.e08lighting.ELRenderer;

public class EParticleSystem extends Activity {

	protected IrrlichtView mDemo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.defaultlayout);
		
		mDemo = (IrrlichtView)findViewById(R.id.bview);
		mDemo.setEngineRenderer(new ELRenderer());
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
