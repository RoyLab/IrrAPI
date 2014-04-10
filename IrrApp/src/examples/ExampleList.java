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
			"02.Engine�������", 
			"03.�ڵ�任",
			"04.�ڵ㶯��",
			"05.2D��ͼ",
			"06.�¼������������",
			"07.Bitmap��",
			"08.����",
			"09.��ײ���",
			"10.ý�岥������������ͼ",
			"11.�������",
			"12.����ϵͳ",
			"13.һ���������ʵ��",
			"14.������ʺ���ͼ",
			"15.�ڴ����",
			"16.����ڵ����",
			"17.��Χ��",
			"18.������Ч"
	};
	
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/** ���������л�����ʹ��Engine.release()����������������������������*/
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
		 * ������Ҳ����Ҫʹ������ʱ�����������ͷ�����ռ�õ���Դ��
		 * ǿ�ҽ��飺��������Ӧ�ó�����Ҳ�����õ�����ʱ���ø÷�����
		 * ���ڴ�ԭ�����ע�⵽�����еķ���activity�ж�û���õ��÷�����
		 * 
		 * ע�⣺ʹ�ñ�������Ҫ�����ϸ�Ĳ��ԣ���һЩ�����Ļ����е��ñ�������
		 * �������ʾ���⣬������ͨ����������onDestroy������ʵ���Զ��ͷ���
		 * ��ʱ�������п����ڲ�ǡ����ʱ���ͷ��ڴ���������⡣��Ҳ������
		 * �����ͷ��ڴ漯�ɽ��������Զ����õ���Ҫԭ�����������Բ�����Release
		 * ��������Ϊ������ڴ潫��������Ӧ�õ����ٶ����ͷš�
		 * 
		 * һ����Ϊ����İ취��ʹ��SurfaceView��д�Լ�����ͼ�ࡣȻ���������
		 * ��Ҫ�����ı���͵��Թ�����������Ҫ������������ɡ�
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
