package examples.e10mediaplayer;

import zte.irrlib.IrrlichtView;
import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;
import examples.e09collision.ECRenderer;
import examples.e09collision.ECView;

public class EMediaPlayer extends Activity {
	
	protected IrrlichtView mDemo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ecollision);
		
		mDemo = (ECView)findViewById(R.id.mediaplayerview);
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
