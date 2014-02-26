
package zte.irrapp;

import java.io.IOException;

import zte.irrlib.IrrlichtView;
import zte.irrlib.Utils;
import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DemoActivity extends Activity {
	DemoView mDemo;
	DemoRenderer mRenderer;
	Button up, down, right, left, play, pause, stop;
	protected String TAG = "IrrActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_irr);
		
		mDemo = (DemoView)findViewById(R.id.irrview);
		mDemo.setRecommendEGLConfigChooser(0);
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
		Log.d(TAG, "Activity onDestroy");
	}
}
