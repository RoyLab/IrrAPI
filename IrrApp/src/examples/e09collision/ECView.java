package examples.e09collision;

import zte.irrlib.IrrlichtView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ECView extends IrrlichtView {

	public ECView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ECView(Context context) {
		super(context);
	}
	
	public boolean onTouchEvent(MotionEvent e){
		
		/** 取得视图类在屏幕中的坐标*/
		int viewPos[] = new int[2];
		getLocationOnScreen(viewPos);
		
		final int X = viewPos[0];
		final int Y = viewPos[1];
		final MotionEvent event = e;
		
		/** 计算触摸事件在视图中的坐标，并传入渲染器*/
		if (MotionEvent.ACTION_DOWN == event.getAction()){
			queueEvent(new Runnable(){
				public void run() {
					((ECRenderer)getRenderer()).X = event.getRawX() - X;
					((ECRenderer)getRenderer()).Y = event.getRawY() - Y;
				}
			});
		}
		return super.onTouchEvent(event);
	}
}
