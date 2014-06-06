package examples.e07bitmap;

import zte.irrlib.IrrlichtView;
import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * 作为Android平台的2D绘图的重要元素Bitmap类，可以直接传入本引擎进行贴图或
 * 2D绘制等。Bitmap传入引擎后，通常会先转换成引擎的图片类，并将其上传至显存，
 * 与Bitmap类一同传入的标识符（可以叫做名字，也可以叫做ID）被存储在引擎中，
 * 以供下次查询。
 * Bitmap类跟引擎的对接使得Android平台的2D引擎可以与3D引擎合作完成一些比较
 * 复杂的绘图过程，如复杂的文字显示，贴图的拼合与修改。但需要注意的是，引擎只能
 * 解码RGB565和RGBA8888格式的位图。
 * 由于存在额外的格式转化过程，当Bitmap较大时，处理它可能需要较多的资源；并且
 * 由于要给不同的Bitmap指定不同的标识符，这可能使得整个场景难于管理。
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
		mDemo.setRecommendEGLConfigChooser(0);
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
