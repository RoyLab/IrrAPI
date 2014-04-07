package examples.e10mediaplayer;

import zte.irrlib.IrrlichtView;
import zte.test.irrapp.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 演示了如何播放视频
 * @author Roy
 *
 */
public class EMediaPlayer extends Activity {
	
	protected IrrlichtView mDemo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/** 横竖屏的切换总是带来令人头疼的问题，因此这里决定禁用它*/
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_emedia_player);
		
		mDemo = (IrrlichtView)findViewById(R.id.mediaplayerview);
		mDemo.setEngineRenderer(new EMRenderer());
		
		/** 加入按钮响应*/
		Button play = (Button)findViewById(R.id.play);
		play.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				((EMRenderer)mDemo.getRenderer()).player.start();
			}
		});
		
		Button pause = (Button)findViewById(R.id.pause);
		pause.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				((EMRenderer)mDemo.getRenderer()).player.pause();
			}
		});
		
		Button stop = (Button)findViewById(R.id.stop);
		stop.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				((EMRenderer)mDemo.getRenderer()).player.stop();
			}
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
	protected void onDestroy(){
		((EMRenderer)mDemo.getRenderer()).player.release();
		super.onDestroy();
	}
}
