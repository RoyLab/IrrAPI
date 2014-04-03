package examples.gettingstart;

import zte.irrlib.IrrlichtView;
import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * ��������ʾ��һ����С�ĳ�����Ҫ�ı��롣
 * @author Roy
 *
 */
public class EGettingStart extends Activity {

	private IrrlichtView mDemo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.defaultlayout);
		
		mDemo = (IrrlichtView)findViewById(R.id.bview);
		
		/**��������ṩ��һ����װ�õ���ͼ���Ե��ù�����棬����ͼ����
		 * GLSurfaceView������࣬��˼̳��˴���ĺܶ�GLSurfaceView
		 * �ķ�����������Ϊ��Ҫ����setEngineRenderer��ֻ���趨�˺���
		 * ����Ⱦ��������ʹ����ͼ������������
		 */
		mDemo.setEngineRenderer(new EGSRenderer());
	}
	
	/**
	 * ��Ϊ��Ⱦ�߳��Ƕ��������̵߳ģ������Ⱦ�̲߳�
	 * ���������߳���ͣ��ʱ����ͣ
	 * ��ͼ��������ı���������ͣ��ʱ��֪ͨ��ͼ�ࡣ
	 */
	@Override
	protected void onResume(){
		super.onResume();
		if (mDemo != null){
			mDemo.onResume();
		}
	}
	
	/**
	 * ��Ϊ��Ⱦ�߳��Ƕ��������̵߳ģ������Ⱦ�̲߳�
	 * ���������ָ̻߳���ʱ��ָ�
	 * ��ͼ��������ı��������ָ���ʱ��֪ͨ��ͼ�ࡣ
	 */
	@Override
	protected void onPause(){
		if (mDemo != null){
			mDemo.onPause();
		}
		super.onPause();
	}
}
