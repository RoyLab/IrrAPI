package examples.e18psextension;

import zte.irrlib.IrrlichtView;
import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;


/**
 * ��ʾ����θ������ӷ������Ĳ�������������������Ч��
 * ͬʱ��ʾ����������Դ���������β���ǹ⡢��ը����Ч���
 * @author Fxx
 *
 */
public class EPSExtension extends Activity {

	protected IrrlichtView mDemo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.defaultlayout);
		
		mDemo = (IrrlichtView)findViewById(R.id.bview);
		mDemo.setEngineRenderer(new EPSERenderer());
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
