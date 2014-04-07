package examples.e06camera;

import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * ��ʾ���ʹ������ڵ㣬�Լ���ν����¼���������ʵ���˵�һ�˳����������ͨ����ť
 * �ͻ������������
 * @author Roy
 *
 */
public class ECamera extends Activity {
	
	private Button up, down, left, right;
	private ECView mDemo;
	private ECRenderer mRenderer;
	private ECReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fpslayout);
		
		mDemo = (ECView)findViewById(R.id.irrview);
		
		mRenderer = new ECRenderer();
		mDemo.setEngineRenderer(mRenderer);
		
		mReceiver = new ECReceiver(mDemo, mRenderer);
		mDemo.setEventReceiver(mReceiver);
	
		up = (Button)findViewById(R.id.up);
		down = (Button)findViewById(R.id.down);
		left = (Button)findViewById(R.id.left);
		right = (Button)findViewById(R.id.right);
		
		/** �趨�¼���������*/
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
