package examples;

import zte.irrlib.Engine;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import examples.e01gettingstart.EGettingStart;
import examples.e02config.EConfig;
import examples.e03transform.ETransAndHier;
import examples.e04animator.EAnimator;
import examples.e05draw2D.EDraw2D;
import examples.e06camera.ECamera;
import examples.e07bitmap.EBitmap;
import examples.e08lighting.ELighting;
import examples.e09collision.ECollision;
import examples.e10mediaplayer.EMediaPlayer;
import examples.e11billboard.EBillboard;
import examples.e12ps.EParticleSystem;
import examples.e13cubelayout.ECubelayout;
import examples.e14material.EMaterial;
import examples.e15memory.EMemory;
import examples.e16nodemgr.ENodeMgr;
import examples.e17bbox.EBoundingBox;
import examples.e18psextension.EPSExtension;

public class ExampleList extends ListActivity {

	public final String[] exampleName = new String[] {
			"01.GettingStart",
			"02.Engine类的设置", 
			"03.节点变换",
			"04.节点动画",
			"05.2D绘图",
			"06.事件处理与照相机",
			"07.Bitmap类",
			"08.光照",
			"09.碰撞检测",
			"10.媒体播放器与外置贴图",
			"11.公告板组",
			"12.粒子系统",
			"13.一个布局类的实现",
			"14.深入材质和贴图",
			"15.内存控制",
			"16.深入节点管理",
			"17.包围盒",
			"18.粒子特效"
	};
	
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/** 横竖屏的切换总是使得Engine.release()工作不正常，因此这里决定禁用它*/
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		//WLog.d("onCreate");
		adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_expandable_list_item_1, exampleName);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id){
		Class<?> cls = null;
		switch (position + 1){
		case 1: 	cls = EGettingStart.class; 	break;
		case 2: 	cls = EConfig.class; 		break;
		case 3:		cls = ETransAndHier.class;	break;
		case 4: 	cls = EAnimator.class; 		break;
		case 5:		cls = EDraw2D.class; 		break;
		case 6: 	cls = ECamera.class; 		break;
		case 7: 	cls = EBitmap.class;		break;
		case 8: 	cls = ELighting.class;		break;
		case 9: 	cls = ECollision.class;		break;
		case 10:	cls = EMediaPlayer.class;	break;
		case 11:	cls = EBillboard.class;		break;
		case 12:	cls = EParticleSystem.class;break;
		case 13:	cls = ECubelayout.class;	break;
		case 14:	cls = EMaterial.class;		break;
		case 15:	cls = EMemory.class;		break;
		case 16:	cls = ENodeMgr.class;		break;
		case 17:	cls = EBoundingBox.class;	break;
		case 18:cls = EPSExtension.class; 	break;
		default: break;
		}
		
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}
		
	protected void onDestroy(){
		/**
		 * 当你再也不需要使用引擎时，可以主动释放引擎占用的资源。
		 * 强烈建议：仅当整个应用程序再也不会用到引擎时调用该方法。
		 * 基于此原则，你会注意到在所有的范例activity中都没有用到该方法。
		 * 
		 * 注意：使用本方法需要进行严格的测试，在一些上下文环境中调用本方法，
		 * 会造成显示问题，尤其是通过重载诸如onDestroy方法来实现自动释放内
		 * 存时，程序极有可能在不恰当的时刻释放内存而导致问题。这也是我们
		 * 不将释放内存集成进引擎中自动调用的主要原因。你甚至可以不调用Release
		 * 方法，因为申请的内存将随着整个应用的销毁而被释放。
		 * 
		 * 一个更为合理的办法是使用SurfaceView编写自己的视图类。然而这个方法
		 * 需要大量的编码和调试工作，如有需要，可以自行完成。
		 */
		//WLog.d("onDestroy");
		Engine.release(); 
		super.onDestroy(); 
	}
	
	/*protected void onPause(){
		WLog.d("onPause");
		super.onPause();
	}
	
	protected void onRestart(){
		WLog.d("onRestart");
		super.onRestart();
	}
	
	protected void onResume(){
		WLog.d("onResume");
		super.onResume();
	}
	
	protected void onStop(){
		WLog.d("onStop");
		super.onStop();
	}*/
}
