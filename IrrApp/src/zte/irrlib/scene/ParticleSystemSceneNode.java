package zte.irrlib.scene;

import zte.irrlib.core.Color4i;
import zte.irrlib.core.Vector2d;
import zte.irrlib.core.Vector3d;
import zte.irrlib.core.BoxEmitter;

public class ParticleSystemSceneNode extends SceneNode {
	
	public static final int EMITTER_BOX = 0x01;
	
	/**
	 * Ψһ���캯��
	 */
	public ParticleSystemSceneNode(){
		super();
		mNodeType = TYPE_PARTICLE_SYSTEM;
	}
	
	/**
	 * Ϊ����ϵͳ�ڵ����ú�״������
	 * @param para ��ʹ�õķ���������
	 */
	public void setBoxEmitter( BoxEmitter para){
		nativeSetBoxEmitter(para, getId());
	}
	
	/**
	 * Ϊ����ϵͳ���в���ָ��ͳһ��ͼ
	 * @param path ��ͼ��·��
	 */
	public void setTexture(String path) {
		nativeAllSetTexture(mScene.getFullPath(path), getId());
	}
	
	/**
	 * Ϊ����ϵͳ�������Ӱ�����ӣ���Ӱ�����ӽ�����������ָ����
	 * @param point ���ӱ���������Ŀ�ĵ�
	 * @param speed �����˶����ٶȣ���λ�� units per second
	 * @param attract ֵΪtrueʱ�˶�����Ϊ����������ΪԶ��
	 * @param affectX ָ���˶��Ƿ�Ӱ��X��
	 * @param affectY ָ���˶��Ƿ�Ӱ��Y��
	 * @param affectZ ָ���˶��Ƿ�Ӱ��Z��
	 */
	public void addAttractionParticleAffector(Vector3d point, int speed, boolean attract, boolean affectX, boolean affectY, boolean affectZ){
		nativeAddAttractionParticleAffector(point.x, point.y, point.z, speed, attract, affectX, affectY, affectZ, getId());
	}
	
	/**
	 * Ϊ����ϵͳ��ӱ���Ӱ�����ӣ���Ӱ�����Ӹı����ӵĴ�С
	 * @param scaleTo ���Ӵ�С��������ı���
	 */
	 public void addScaleParticleAffector(Vector2d scaleTo){
		 nativeAddScaleParticleAffector(scaleTo.x, scaleTo.y, getId());
	 }
	 
	 /**
	  * Ϊ����ϵͳ��ӽ���Ӱ�����ӣ���Ӱ������ʹ������Ŀ����ɫ����
	  * @param target ���ӽ����Ŀ����ɫ
	  * @param time ���ӽ��������ʱ�䣬��λ�����루ms��
	  */
	 public void addFadeOutAffectorParticleAffector(Color4i target, int time){
		 nativeAddFadeOutAffectorParticleAffector(target.r(), target.g(), target.b(), target.a(), time, getId());
	 }
	 
	 /**
	  * Ϊ����ϵͳ�������Ӱ�����ӣ���Ӱ������ʹ���������������˶�
	  * @param gravity ��������ά��С
	  * @param time ��������ʱ�䣬��λ�����루ms��
	  */
	 public void addGravityAffector(Vector3d gravity, int time){
		 nativeAddGravityAffector(gravity.x, gravity.y, gravity.z, time, getId());
	 }
	
	/**
	 * Ϊ����ϵͳ�����ת�˶�Ӱ�����ӣ���Ӱ������ʹ���������ĵ���һ���ٶ��˶� 
	 * @param speed �����˶����ٶȣ���λ����/��
	 * @param center �����˶�Χ�Ƶ����ĵ�
	 */
	 public void addRotationAffector(Vector3d speed, Vector3d center){
		 nativeAddRotationAffector(speed.x, speed.y, speed.z, center.x, center.y, center.z, getId());
	 }
	 
	 private native void nativeSetBoxEmitter(BoxEmitter para, int id);
	 private native int nativeAllSetTexture(String path, int Id);
	 private native void nativeAddAttractionParticleAffector(double x, double y, double z, int speed, 
			 boolean attract, boolean affectX, boolean affectY, boolean affectZ, int Id);
	 private native void nativeAddScaleParticleAffector(double x, double y, int Id);
	 private native void nativeAddFadeOutAffectorParticleAffector(int r, int g, int b, int a, int time, int Id);
	 private native void nativeAddGravityAffector(double x, double y, double z, int time, int Id);
	 private native void nativeAddRotationAffector(double sx, double sy, double sz, double cx, double cy, double cz, int Id);
}
