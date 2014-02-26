package zte.irrlib;

import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.CameraSceneNode;
import zte.irrlib.scene.SceneNode;

public class CameraFPSWrapper {
	
	public CameraFPSWrapper(CameraSceneNode cam){
		mCamera = cam;
		mCamera.setUpVector(new Vector3d(0,1,0));
		pLeft = new Vector3d();
		pLook = new Vector3d();
		calcFaiXita();
	}
	
	public void goLeft(double x){
		mCamera.mark();
		mCamera.setPosition(pLeft.multi(x), SceneNode.TRANS_RELATIVE);
		mCamera.setLookAt(mCamera.getPosition().plus(pLook));
	}
	
	public void goAhead(double x){
		mCamera.mark();
		mCamera.setPosition(pLook.multi(x), SceneNode.TRANS_RELATIVE);
		mCamera.setLookAt(mCamera.getPosition().plus(pLook));
	}
	
	public void turnLeft(double x){
		mXita -= x;
		recalculate();
		mCamera.setLookAt(mCamera.getPosition().plus(pLook));
	}
	
	public void turnUp(double x){
		mFai -= x;
		recalculate();
		mCamera.setLookAt(mCamera.getPosition().plus(pLook));
	}
	
	private void recalculate(){
		if (mFai > Math.PI - 1e-5){
			mFai = Math.PI - 1e-5;
		}
		if (mFai < 1e-5){
			mFai = 1e-5;
		}
		
		pLook.x = Math.sin(mFai) * Math.sin(mXita);
		pLook.z = Math.sin(mFai) * Math.cos(mXita);
		pLook.y = Math.cos(mFai);
		
		pLeft = pLook.cross(new Vector3d(0,1,0));
		pLeft.normalize();
	}
	
	private void calcFaiXita(){
		Vector3d d = mCamera.getLookAt().minus(mCamera.getPosition());
		if (d.isZero()){
			d.z = 1.0;
		}
		d.normalize();
		
		mFai = Math.acos(d.y);
		
		mXita = Math.atan(d.x/d.z);
		if (d.x < 0){
			mXita += Math.PI;
		}
		
		recalculate();
	}
	
	private CameraSceneNode mCamera;
	private double mFai/*0~PI*/, mXita;
	private Vector3d pLook, pLeft;
}
