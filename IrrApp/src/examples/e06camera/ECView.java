package examples.e06camera;

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
	
	public boolean onTouchEvent(MotionEvent event){
		if (mReceiver != null){
			return mReceiver.handleEvent(event);
		}
		return super.onTouchEvent(event);
	}
	
	public void setEventReceiver(ECReceiver receiver){
		mReceiver = receiver;
	}
	
	private ECReceiver mReceiver;
}
