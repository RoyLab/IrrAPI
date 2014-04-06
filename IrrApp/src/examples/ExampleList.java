package examples;

import zte.irrlib.Engine;
import android.app.ListActivity;
import android.content.Intent;
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

public class ExampleList extends ListActivity {

	public final String[] exampleName = new String[] {
			"01.GettingStart",
			"02.Configuration", 
			"03.Transfromation",
			"04.Animator",
			"05.2D Drawing",
			"06.Camera & Response",
			"07.Bitmap",
			"08.Lighting",
			"09.Collision",
			"10.MediaPlayer",
			"11.BillboardGroup",
			"12.ParticleSystem",
			"13.CubeLayout",
			"14.Material & Texture",
			"15.Memory Control",
			"16.Hierarchy Structure",
			"17.Debug & BoundingBox",
			"18.Native Extension"
	};
	
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_expandable_list_item_1, exampleName);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id){
		Class<?> cls = null;
		switch (position + 1){
		case 1: cls = EGettingStart.class; 	break;
		case 2: cls = EConfig.class; 		break;
		case 3: cls = ETransAndHier.class;	break;
		case 4: cls = EAnimator.class; 		break;
		case 5: cls = EDraw2D.class; 		break;
		case 6: cls = ECamera.class; 		break;
		case 7: cls = EBitmap.class;		break;
		case 8: cls = ELighting.class;		break;
		case 9: cls = ECollision.class;		break;
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
		 */
		Engine.release();
		super.onDestroy();
	}
}
