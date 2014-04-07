
package zte.irrapp;

import java.io.IOException;

import zte.irrlib.Engine;
import zte.irrlib.IrrlichtView;
import zte.irrlib.Utils;
import zte.test.irrapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DemoActivity extends Activity {
	DemoView mDemo;
	DemoRenderer mRenderer;
	Button up, down, right, left;
	protected String TAG = "IrrActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fpslayout);
		  
		/*Utils util = new Utils();
		try {
			util.setSDCardPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		util.UtilsInit(getAssets());*/
		
		mDemo = (DemoView)findViewById(R.id.irrview);
		mDemo.setRecommendEGLConfigChooser(0);
		//mDemo.enableGLES2(true);
		mRenderer = new DemoRenderer();
		mDemo.setEngineRenderer(mRenderer);
		 
		mDemo.setEventReceiver(new DemoReceiver(mDemo, mRenderer));
		
		up = (Button)findViewById(R.id.up);
		down = (Button)findViewById(R.id.down);
		left = (Button)findViewById(R.id.left);
		right = (Button)findViewById(R.id.right);
		
		up.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mDemo.queueEvent(new Runnable(){
					public void run() {
						mRenderer.w.goAhead(5);
					}
				});
			}
		});
		
		down.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mDemo.queueEvent(new Runnable(){
					public void run() {
						mRenderer.w.goAhead(-5);
					}
				});
			}
		});
		
		left.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mDemo.queueEvent(new Runnable(){
					public void run() {
						mRenderer.w.goLeft(5);
					}
				});
			}
		});
		
		right.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mDemo.queueEvent(new Runnable(){
					public void run() {
						mRenderer.w.goLeft(-5);
					}
				});
			}
		});
		
		Log.d(TAG, "Activity onCreate.");
	}
	
	public void newActivity(View view){
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if (mDemo != null){
			mDemo.onResume();
		}
		Log.d(TAG, "Activity onResume.");
	}
	
	@Override
	protected void onPause(){
		if (mDemo != null){
			mDemo.onPause();
		}
		super.onPause();
		Log.d(TAG, "Activity onPause.");
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		Engine.release();
		Log.d(TAG, "Activity onDestroy");
	}
	
	@Override
	public void onStop(){
		super.onStop();
		Log.d(TAG, "Activity onStop");
	}
	
	@Override
	public void onRestart(){
		super.onRestart();
		Log.d(TAG, "Activity onRestart");
	}
}
