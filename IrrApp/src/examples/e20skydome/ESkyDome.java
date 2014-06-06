package examples.e20skydome;

import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class ESkyDome extends Activity {
	
	private Button up, down, left, right;
	private SDView mDemo;
	private SDRenderer mRenderer;
	private SDReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eskydome);
		mDemo = (SDView)findViewById(R.id.sdview);
		
		mDemo.setRecommendEGLConfigChooser(0);
		mRenderer = new SDRenderer();
		mDemo.setEngineRenderer(mRenderer);

		mReceiver = new SDReceiver(mDemo, mRenderer);
		mDemo.setEventReceiver(mReceiver);
	
		up = (Button)findViewById(R.id.up3);
		down = (Button)findViewById(R.id.down3);
		left = (Button)findViewById(R.id.left3);
		right = (Button)findViewById(R.id.right3);
		
		/** 设定事件处理函数。*/
		up.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mReceiver.cameraGoUp();
			}
		});
		
		down.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mReceiver.cameraGoDown();
			}
		});
		
		left.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mReceiver.cameraGoLeft();
			}
		});
		
		right.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mReceiver.cameraGoRight();			}
		});
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

	@Override
	/**
	 * 添加事件响应，当点击屏幕时，切换天空上的装饰。
	 */
	public boolean onTouchEvent(MotionEvent event){
		if (MotionEvent.ACTION_DOWN == event.getAction()){
			final SDRenderer renderer = mRenderer; 
			mDemo.queueEvent(new Runnable(){
				public void run() {
					renderer.changeCurtain();  
				}
			});
			return true;
		}
		return super.onTouchEvent(event);
	}
}
