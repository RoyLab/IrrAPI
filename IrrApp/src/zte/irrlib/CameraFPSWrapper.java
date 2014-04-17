package zte.irrlib;

import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.CameraSceneNode;
import zte.irrlib.scene.SceneNode;


/**
 * <p>��һ��{@link CameraSceneNode}��װΪ��һ�˳�������������Ķ�ʸ��(Up Vector)
 * ʼ��ָ��0,1,0���� 
 * <p>�ڸ��๤���ڼ䣬��Ҫά������ڵ���ӽ�������ð�װ���ӽ����ݵ�һ���ԡ�
 * {@link CameraSceneNode#setPosition(Vector3d, int)}��
 * {@link CameraSceneNode#setLookAt(Vector3d)}�����ƻ�����һ���ԡ�
 * ��ˣ��뾡����������������������������ã���ʹ��{@link #uploadSettings()}��
 * {@link #downloadSettings()}��ά�����ݵ�һ���ԡ�
 * <p>�벻Ҫʹ��{@link CameraSceneNode#setUpVector(Vector3d)}����
 * ��֤�ڸ��๤��ʱ�����ʼ�ձ��ֶ������ϵ�״̬��
 */
public class CameraFPSWrapper {
	
	/**
	 * <p>Ψһ�Ĺ��캯������{@link CameraSceneNode}��װΪ��һ�˳������
	 * @param cam ����װ�����
	 */
	public CameraFPSWrapper(CameraSceneNode cam){
		mCamera = cam;
		mCamera.setUpVector(new Vector3d(0,1,0));
		pLeft = new Vector3d();
		pLook = new Vector3d();
		syncSettings();
	}
	
	/**
	 * ��������ĳ��������ƶ�x���롣
	 * @param x �ƶ��ľ��롣��x<0���������ƶ�
	 */
	public void goLeft(double x){
		mCamera.mark();
		mCamera.setPosition(pLeft.multi(x), SceneNode.TRANS_RELATIVE);
		uploadSettings();
	}
	
	/**
	 * ��������ĳ�����ǰ�ƶ�x���롣
	 * @param x �ƶ��ľ��룬��x<0��������ƶ�
	 */
	public void goAhead(double x){
		mCamera.mark();
		mCamera.setPosition(pLook.multi(x), SceneNode.TRANS_RELATIVE);
		uploadSettings();
	}
	
	/**
	 * ���������λ�ã�����ת��x���ȡ�
	 * @param x ת���ĽǶȣ������ƣ�����x<0��������ת��
	 */
	public void turnLeft(double x){
		mXita -= x;
		recalculate();
		uploadSettings();
	}
	
	/**
	 * ���������λ�ã�����ת��x���ȡ�
	 * @param x ת���ĽǶȣ������ƣ�����x<0��������ת��
	 */
	public void turnUp(double x){
		mFai -= x;
		recalculate();
		uploadSettings();
	}
	
	/**
	 * ����ͬ��wrapper������ڵ�����ݣ�������ڵ������ӽ����ݵ�wrapper��
	 * �κβ�ͨ��wrapper���ı����λ�úͳ���Ĳ�����Ҫ���ô˷������任��Ϣ
	 * ͬ����wrapper�У�����{@link SceneNode#setPosition(Vector3d, int)}
	 * �ͽڵ㶯���ȡ�
	 */
	public void syncSettings(){
		Vector3d d = mCamera.getLookAt().minus(mCamera.getPosition());
		if (d.isZero()){
			d.Z = 1.0;
		}
		d.normalize();
		mFai = Math.acos(d.Y);
		double tmp = d.X/d.Z;
		if (Double.isNaN(tmp) || Double.isInfinite(tmp)){
			mXita = 0.0;
		}
		else{
			mXita = Math.atan(tmp);
		}
		
		if (d.Z < 0){
			mXita += Math.PI;
		}
		//Log.d("wang", "" + mXita + ", " + mFai);
		
		recalculate();
	}
	
	private void uploadSettings(){
		mCamera.setLookAt(mCamera.getPosition().plus(pLook));
		//Log.d("wang", "" + mXita + ", " + mFai);
	}

	private void recalculate(){
		if (mFai > Math.PI - 0.1){
			mFai = Math.PI - 0.1;
		}
		if (mFai < 0.1){
			mFai = 0.1;
		}
		
		pLook.X = Math.sin(mFai) * Math.sin(mXita);
		pLook.Z = Math.sin(mFai) * Math.cos(mXita);
		pLook.Y = Math.cos(mFai);
		
		pLeft = pLook.cross(new Vector3d(0,1,0));
		pLeft.normalize();
	}

	private CameraSceneNode mCamera;
	private double mFai/*0~PI*/, mXita;
	private Vector3d pLook, pLeft;
}
