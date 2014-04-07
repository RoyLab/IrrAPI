package examples.e07bitmap;

import zte.irrlib.IrrlichtView;
import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * ��ΪAndroidƽ̨��2D��ͼ����ҪԪ��Bitmap�࣬����ֱ�Ӵ��뱾���������ͼ��
 * 2D���Ƶȡ�Bitmap���������ͨ������ת���������ͼƬ�࣬�������ϴ����Դ棬
 * ��Bitmap��һͬ����ı�ʶ�������Խ������֣�Ҳ���Խ���ID�����洢�������У�
 * �Թ��´β�ѯ��
 * Bitmap�������ĶԽ�ʹ��Androidƽ̨��2D���������3D����������һЩ�Ƚ�
 * ���ӵĻ�ͼ���̣��縴�ӵ�������ʾ����ͼ��ƴ�����޸ġ�����Ҫע����ǣ�����ֻ��
 * ����RGB565��RGBA8888��ʽ��λͼ��
 * ���ڴ��ڶ���ĸ�ʽת�����̣���Bitmap�ϴ�ʱ��������������Ҫ�϶����Դ������
 * ����Ҫ����ͬ��Bitmapָ����ͬ�ı�ʶ���������ʹ�������������ڹ���
 * @author Roy
 *
 */
public class EBitmap extends Activity {
	
	protected IrrlichtView mDemo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.defaultlayout);
		
		mDemo = (IrrlichtView)findViewById(R.id.bview);
		mDemo.setEngineRenderer(new EBRenderer());
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
