package examples.e20skydome;

import zte.irrlib.IrrlichtView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SDView extends IrrlichtView {

	public SDView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public SDView(Context context) {
		super(context);
	}
	
	public boolean onTouchEvent(MotionEvent event){
		if (mReceiver != null){
			return mReceiver.handleEvent(event);
		}
		return super.onTouchEvent(event);
	}
	
	public void setEventReceiver(SDReceiver receiver){
		mReceiver = receiver;
	}
	
	private SDReceiver mReceiver;
}
