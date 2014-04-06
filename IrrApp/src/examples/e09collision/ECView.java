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
		
		final MotionEvent event = e;
		
		if (MotionEvent.ACTION_DOWN == event.getAction()){
			queueEvent(new Runnable(){
				public void run() {
					((ECRenderer)getRenderer()).X = event.getX();
					((ECRenderer)getRenderer()).Y = event.getY();
				}
			});
		}
		return super.onTouchEvent(event);
	}
}
