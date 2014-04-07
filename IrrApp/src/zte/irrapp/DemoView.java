package zte.irrapp;

import zte.irrlib.IrrlichtView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DemoView extends IrrlichtView {

	public DemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public DemoView(Context context) {
		super(context);
	}
	
	public boolean onTouchEvent(MotionEvent event){
		if (mReceiver != null){
			return mReceiver.handleEvent(event);
		}
		return super.onTouchEvent(event);
	}
	
	public void setEventReceiver(DemoReceiver receiver){
		mReceiver = receiver;
	}
	
	private DemoReceiver mReceiver;
}
