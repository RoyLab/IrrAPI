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
 * ��ʾ����β�����Ƶ
 * @author Roy
 *
 */
public class EMediaPlayer extends Activity {
	
	protected IrrlichtView mDemo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/** ���������л����Ǵ�������ͷ�۵����⣬����������������*/
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_emedia_player);
		
		mDemo = (IrrlichtView)findViewById(R.id.mediaplayerview);
		mDemo.setEngineRenderer(new EMRenderer());
		
		/** ���밴ť��Ӧ*/
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
