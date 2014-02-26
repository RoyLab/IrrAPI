package zte.irrapp;

import zte.irrlib.IrrlichtView;
import android.view.MotionEvent;

public class DemoReceiver {
	
	
	public DemoReceiver(IrrlichtView view, DemoRenderer renderer){
		mView = view; mRenderer = renderer;
	}
	
	public boolean handleEvent(MotionEvent cEvent){
		
		if (pressed){
			dx = cEvent.getX() -lx;
			dy = cEvent.getY() - ly;
			mView.queueEvent(new Runnable(){
				public void run() {
					mRenderer.w.turnUp(-dy/300);
					mRenderer.w.turnLeft(-dx/300);
				}
			});
		}
		
		lx = cEvent.getX();
		ly = cEvent.getY();
		if (MotionEvent.ACTION_DOWN == cEvent.getAction()) pressed = true;
		else if (MotionEvent.ACTION_UP == cEvent.getAction()) pressed = false;
		return true;
	}
	
	private float dx, dy;
	private float lx, ly;
	private boolean pressed;
	private DemoRenderer mRenderer;
	private IrrlichtView mView;
}
