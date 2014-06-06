package examples.e11billboard;

import zte.irrlib.IrrlichtView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class EBView extends IrrlichtView {

	public EBView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public EBView(Context context) {
		super(context);
	}
	
	public boolean onTouchEvent(MotionEvent event){
		if (mReceiver != null){
			return mReceiver.handleEvent(event);
		}
		return super.onTouchEvent(event);
	}
	
	public void setEventReceiver(EBReceiver receiver){
		mReceiver = receiver;
	}
	
	private EBReceiver mReceiver;
}
