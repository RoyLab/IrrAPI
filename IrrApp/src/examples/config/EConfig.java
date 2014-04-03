package examples.config;

import zte.irrlib.IrrlichtView;
import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * ��������ʾ����ν�������Ļ�������
 * @author Roy
 *
 */
public class EConfig extends Activity {

	private IrrlichtView mDemo;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.defaultlayout);
		
		mDemo = (IrrlichtView)findViewById(R.id.bview);
		/**
		 * ���Ҫʹ��GL ES2��ͨ���ⲻ�Ǳ�Ҫ�ģ���Ⱦ�����Ե���enableGLES2
		 * ����Ⱦ���ء�Ĭ������£�ʹ��GL ES1��Ⱦ�������ȶ������ܸ�ȫ��Ч
		 * �ʸ��ߡ��ú����������Ҫ��������setRecommendEGLConfigChooser֮ǰ���á�
		 */
		mDemo.enableGLES2(true);
		/**
		 * ���м򵥵�egl�����Ļ������á�����Ա�д�Լ������÷�������ʹ��setEGLConfigChooser
		 * ����������������ú���������setEngineRendererǰ���á�
		 */
		mDemo.setRecommendEGLConfigChooser(8);
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