package zte.irrapp;

import zte.test.irrapp.R;
import zte.test.irrapp.R.layout;
import zte.test.irrapp.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SubActivity extends Activity {
	
	DemoView mDemo;
	DemoRenderer mRenderer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub);
		
		mDemo = (DemoView)findViewById(R.id.irrview);
		mDemo.setRecommendEGLConfigChooser(0);
		//mDemo.enableGLES2(true);
		mRenderer = new DemoRenderer();
		mDemo.setEngineRenderer(mRenderer);
		WLog.d("sub onCreate");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sub, menu);
		return true;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		mDemo.onResume();
		WLog.d("sub onResume");
	}

	@Override
	public void onPause(){
		mDemo.onPause();
		super.onPause();
		WLog.d("sub onPause");
	}
	
	@Override
	public void onStop(){
		super.onStop();
		WLog.d("sub onStop");
	}
	
	@Override
	public void onRestart(){
		super.onRestart();
		WLog.d("sub onRestart");
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		WLog.d("sub onDestroy");
	}
}
