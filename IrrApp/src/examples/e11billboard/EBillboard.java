package examples.e11billboard;

import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 演示了如何使用公告板组管理公告板
 * @author Roy
 *
 */
public class EBillboard extends Activity {

	private Button up, down, left, right;
	private EBView mDemo;
	private EBRenderer mRenderer;
	private EBReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ebillboard);
		
		mDemo = (EBView)findViewById(R.id.e11view);
		
		mRenderer = new EBRenderer();
		mDemo.setRecommendEGLConfigChooser(0);
		mDemo.setEngineRenderer(mRenderer);
		
		mReceiver = new EBReceiver(mDemo, mRenderer);
		mDemo.setEventReceiver(mReceiver);
	
		up = (Button)findViewById(R.id.up2);
		down = (Button)findViewById(R.id.down2);
		left = (Button)findViewById(R.id.left2);
		right = (Button)findViewById(R.id.right2);
		
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
}