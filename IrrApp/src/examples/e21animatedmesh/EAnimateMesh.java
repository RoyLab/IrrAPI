package examples.e21animatedmesh;

import zte.irrlib.IrrlichtView;
import zte.test.irrapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EAnimateMesh extends Activity {
	
	private Button play, rewind, pause;
	private IrrlichtView mDemo;
	private AMRenderer mRenderer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eanimate_mesh);
		
		mDemo = (IrrlichtView)findViewById(R.id.view21);
		
		mDemo.setRecommendEGLConfigChooser(0);
		mRenderer = new AMRenderer();
		mDemo.setEngineRenderer(mRenderer);
	
		play = (Button)findViewById(R.id.play21);
		rewind = (Button)findViewById(R.id.rewind21);
		pause = (Button)findViewById(R.id.pause21);
		
		/** 设定事件处理函数。*/
		play.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mDemo.queueEvent(new Runnable(){
					public void run(){
						mRenderer.play();
					}
				});
			}
		});
		
		rewind.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mDemo.queueEvent(new Runnable(){
					public void run(){
						mRenderer.rewind();
					}
				});
			}
		});
		
		pause.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mDemo.queueEvent(new Runnable(){
					public void run(){
						mRenderer.pause();
					}
				});
			}
		});
	}
}
