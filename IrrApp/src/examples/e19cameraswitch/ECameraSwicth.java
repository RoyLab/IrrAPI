package examples.e19cameraswitch;

import zte.irrlib.IrrlichtView;
import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class ECameraSwicth extends Activity {
	
	private IrrlichtView mDemo;
	private CSRenderer mRenderer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.defaultlayout);
		mDemo = (IrrlichtView)findViewById(R.id.bview);
		
		mDemo.setRecommendEGLConfigChooser(0);
		mRenderer = new CSRenderer();
		mDemo.setEngineRenderer(mRenderer);
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
	 * 添加事件响应，当点击屏幕时，切换相机。
	 */
	public boolean onTouchEvent(MotionEvent event){
		if (MotionEvent.ACTION_DOWN == event.getAction()){
			final CSRenderer renderer = mRenderer; 
			mDemo.queueEvent(new Runnable(){
				public void run() {
					renderer.toNextCamera();  
				}
			});
			return true;
		}
		return super.onTouchEvent(event);
	}
}
