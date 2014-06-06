package examples.e11billboard;

import zte.irrlib.IrrlichtView;
import android.view.MotionEvent;

public class EBReceiver {
	
	
	public EBReceiver(IrrlichtView view, EBRenderer renderer){
		mView = view; mRenderer = renderer;
	}
	
	public boolean handleEvent(MotionEvent cEvent){
		
		if (pressed){
			dx = cEvent.getX() -lx;
			dy = cEvent.getY() - ly;
			
			/**
			 * 因为渲染线程是独立与主线程的，因此在非渲染线程对引擎的读写
			 * 都有可能产生错误。一个较为安全的办法是使用queueEvent方法。
			 */
			mView.queueEvent(new Runnable(){
				public void run() {
					mRenderer.FPSWrapper.turnUp(-dy/300);
					mRenderer.FPSWrapper.turnLeft(-dx/300);
				}
			});
		}
		
		lx = cEvent.getX();
		ly = cEvent.getY();
		
		if (MotionEvent.ACTION_DOWN == cEvent.getAction()) 
			pressed = true;
		else if (MotionEvent.ACTION_UP == cEvent.getAction()) 
			pressed = false;
		
		return true;
	}
	
	public void cameraGoUp(){
		mView.queueEvent(new Runnable(){
			public void run() {
				mRenderer.FPSWrapper.goAhead(2); 
			}
		});
	}
	
	public void cameraGoDown(){
		mView.queueEvent(new Runnable(){
			public void run() {
				mRenderer.FPSWrapper.goAhead(-2); 
			}
		});
	}
	
	public void cameraGoLeft(){
		mView.queueEvent(new Runnable(){
			public void run() {
				mRenderer.FPSWrapper.goLeft(2); 
			}
		});
	}
	
	public void cameraGoRight(){
		mView.queueEvent(new Runnable(){
			public void run() {
				mRenderer.FPSWrapper.goLeft(-2); 
			}
		});
	}
	
	private float dx, dy;
	private float lx, ly;
	private boolean pressed;
	private EBRenderer mRenderer;
	private IrrlichtView mView;
}
