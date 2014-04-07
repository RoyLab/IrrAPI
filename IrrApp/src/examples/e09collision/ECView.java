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
		
		/** ȡ����ͼ������Ļ�е�����*/
		int viewPos[] = new int[2];
		getLocationOnScreen(viewPos);
		
		final int X = viewPos[0];
		final int Y = viewPos[1];
		final MotionEvent event = e;
		
		/** ���㴥���¼�����ͼ�е����꣬��������Ⱦ��*/
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
