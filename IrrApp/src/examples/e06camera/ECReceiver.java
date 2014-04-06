package examples.e06camera;

import zte.irrlib.IrrlichtView;
import android.view.MotionEvent;

public class ECReceiver {
	
	
	public ECReceiver(IrrlichtView view, ECRenderer renderer){
		mView = view; mRenderer = renderer;
	}
	
	public boolean handleEvent(MotionEvent cEvent){
		
		if (pressed){
			dx = cEvent.getX() -lx;
			dy = cEvent.getY() - ly;
			
			/**
			 * ��Ϊ��Ⱦ�߳��Ƕ��������̵߳ģ�����ڷ���Ⱦ�̶߳�����Ķ�д
			 * ���п��ܲ�������һ����Ϊ��ȫ�İ취��ʹ��queueEvent������
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
	private ECRenderer mRenderer;
	private IrrlichtView mView;
}
