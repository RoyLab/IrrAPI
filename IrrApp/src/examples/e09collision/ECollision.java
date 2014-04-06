package examples.e09collision;

import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;

public class ECollision extends Activity {

	protected ECView mDemo;

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

