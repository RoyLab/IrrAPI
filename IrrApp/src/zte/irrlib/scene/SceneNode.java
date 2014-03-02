package zte.irrlib.scene;

import zte.irrlib.core.Vector3d;

public class SceneNode {
	
	//�ڵ�����
	public static final int TYPE_NULL = 0x00000000;
	public static final int TYPE_COMMON = 0x00000001;
	public static final int TYPE_MESH = 0x00000102;
	public static final int TYPE_ANIMATE_MESH = 0x00000103;
	public static final int TYPE_LIGHT = 0x00000004;
	public static final int TYPE_BILLBOARD = 0x00001005;
	public static final int TYPE_BILLBOARD_GROUP = 0x00000006;
	public static final int TYPE_CAMERA = 0x00000007;
	public static final int TYPE_PARTICLE_SYSTEM = 0x00000008;
	
	//�任��ʽ
	public static final int TRANS_ABSOLUTE = 0;
	public static final int TRANS_RELATIVE = 1;
	
	public static final int FLAG_MATERIAL_OWNER = 0x00001000;
	
	/**
	 * ���ó�������
	 * @param scene ��ʹ�õĳ�������
	 */
	public static final void setScene(Scene scene){
		mScene = scene;
	}
	
	/**
	 * ���ؽڵ����͡�
	 * @return �ڵ�����
	 */
	public final int getNodeType(){
		return mNodeType;
	}
	
	/**
	 * �Գ�ʼλ�á���ת����С��Ϣ���б��ݡ�
	 */
	public void mark(){
		mPosition[1].copy(mPosition[0]);
		mRotation[1].copy(mRotation[0]);
		mScale[1].copy(mScale[0]);
	}
	
	/**
	 * ���ýڵ�ĸ��ڵ㡣
	 * @param node ���ڵ����
	 */
	public void setParent(SceneNode node){
		mParent = node;
		nativeSetParent(mScene.getId(mParent), getId());
	}
	
	/**
	 * ���ýڵ��Ƿ�ɼ���
	 * @param isVisible ֵΪtrueʱ�ڵ�ɼ������򲻿ɼ�
	 */
	public void setVisible(boolean isVisible){
		mIsVisible = isVisible;
		nativeSetVisible(isVisible, getId());
	}
	
	/**
	 * ���ýڵ�λ�����ꡣ
	 * @param para	�ڵ�λ������
	 * @param mode ����任ģʽ
	 * 		TRANS_ABSOLUTE��	���ݸ��ڵ����λ�ñ任
	 * 		TRANS_RELATIVE��		����ģ�ͽڵ������ʼλ�ý��б任
	 */
	public void setPosition(Vector3d para, int mode){
		if (mode == TRANS_ABSOLUTE){
			mPosition[0].copy(para);
		}
		else if (mode == TRANS_RELATIVE){
			mPosition[0] = mPosition[1].plus(para);
		}
		else return;
		
		nativeSetPosition(mPosition[0].x, mPosition[0].y, mPosition[0].z, getId());
	}
	
	/**
	 * ���ýڵ���ת�Ƕȡ�
	 * @param para	�������������ת�Ƕ�ֵ
	 * @param mode ��תģʽ
	 * 		TRANS_ABSOLUTE��	������ת����ֱ�ӽ�����ת
	 * 		TRANS_RELATIVE��		����ģ�ͽڵ������ʼ��ת�ǶȽ�����ת
	 */
	public void setRotation(Vector3d para, int mode){
		if (mode == TRANS_ABSOLUTE){
			mRotation[0].copy(para);
		}
		else if (mode == TRANS_RELATIVE){
			mRotation[0] = mRotation[1].plus(para);
		}
		else return;
		
		nativeSetRotation(mRotation[0].x, mRotation[0].y, mRotation[0].z, getId());
	}
	
	/**
	 * ���ýڵ�Ĵ�С��
	 * @param para	�����������С�任ֵ
	 * @param mode �任ģʽ
	 * 		TRANS_ABSOLUTE��	���ݽڵ�ĸ��ڵ���д�С�任
	 * 		TRANS_RELATIVE��		����ģ�ͽڵ������ʼ��С���б任
	 */
	public void setScale(Vector3d para, int mode){
		if (mode == TRANS_ABSOLUTE){
			mScale[0].copy(para);
		}
		else if (mode == TRANS_RELATIVE){
			mScale[0] = mScale[1].multi(para);
		}
		else return;
		
		nativeSetScale(mScale[0].x, mScale[0].y, mScale[0].z, getId());
	}
	
	/**
	 * ���ؽڵ�����ڸ��ڵ��λ�����ꡣ
	 * @return ����ڸ��ڵ��λ������
	 */
	public Vector3d getPosition(){
		return new Vector3d(mPosition[0]);
	}
	
	/**
	 * ���ؽڵ����ת�Ƕȡ�
	 * @return �ڵ����ת�Ƕ�
	 */
	public Vector3d getRotation(){
		return new Vector3d(mRotation[0]); 
	}
	
	/**
	 * ���ؽڵ�����ڸ��ڵ�Ĵ�С��
	 * @return �ڵ�����ڸ��ڵ�Ĵ�С
	 */
	public Vector3d getScale(){
		return new Vector3d(mScale[0]); 
	}
	
	/**
	 * ���ؽڵ��λ�á���ת�ʹ�С�ı任ֵ��
	 * @return �ڵ��λ�á���ת�ʹ�С�ı任ֵ
	 */
	public TransformationInfo getTransformationInfo(){
		TransformationInfo info = new TransformationInfo();
		info.Position = new Vector3d(mPosition[0]);
		info.Rotation = new Vector3d(mRotation[0]);
		info.Scale = new Vector3d(mScale[0]);
		
		return info;
	}
	
	/**
	 * Ϊ�ڵ�����λ�á���ת�ʹ�С�ı任ֵ��
	 * @param info �ڵ�任��Ϣֵ
	 */
	public void setTransformationInfo(TransformationInfo info){
		mPosition[0].copy(info.Position);
		mRotation[0].copy(info.Rotation);
		mScale[0].copy(info.Scale);
	}
	
	/**
	 * ���ƽ�ƶ�����
	 * @param start ƽ�ƶ�����ʼ������
	 * @param end ƽ�ƶ���Ŀ�������
	 * @param time ƽ�ƶ�������ʱ�䣬��λ�����루ms��
	 * @param loop ֵΪtrueʱ����ѭ�����У�ֵΪfalseʱ��������һ��
	 * @param pingpong ֵΪtrueʱ�ڵ㵽��Ŀ���ʱ�᷵�س�ʼ�㣬���򲻷���
	 */
	public void addFlyStraightAnimator(Vector3d start, Vector3d end, 
			double time, boolean loop, boolean pingpong){
		nativeAddFlyStraightAnimator(start.x, start.y, start.z, 
				end.x, end.y, end.z, time, loop, pingpong, getId());
	}
	
	/**
	 * ��ӻ����˶�������
	 * @param center �˶�����Բ����Բ������
	 * @param radius �˶�����Բ���İ뾶
	 * @param speed �˶����ʣ���λ������/����
	 * @param axis �˶����Ƶ��ᣬ���˶�ƽ���up vector
	 * @param startPoint �˶������ʼ�㡣��0.5ʱ�Ӱ�Բ����ʼ�˶�
	 * @param radiusEllipsoid �˶��켣����Բ�̶ȡ�Ĭ��ֵΪ0���˶��켣ΪԲ��
	 */
	public void addFlyCircleAnimator(Vector3d center, double radius,
			double speed, Vector3d axis, double startPoint, double radiusEllipsoid){
		nativeAddFlyCircleAnimator(center.x, center.y, center.z, radius, speed, 
				axis.x, axis.y, axis.z, startPoint, startPoint, getId());
	}
	
	/**
	 * �����ת������
	 * @param speed �Ƹ����������ת���ʣ���λ����/10����
	 */
	public void addRotationAnimator(Vector3d speed){
		nativeAddRotationAnimator(speed.x, speed.y, speed.z, getId());
	}
	
	/**
	 * �����ʧ�������ڵ㽫��ָ��ʱ������ʧ��
	 * @param ms ��ʧ������ʱ�䣬��λ����
	 */
	public void addDeleteAnimator(int ms){
		nativeAddDeleteAnimator(ms, getId());
	}
	
	/**
	 * ��Ӷ�ָ���ڵ����ײ�����Ӧ��
	 * @param selNode ָ���Ľڵ����
	 */
	public void addCollisionResponseAnimator(SceneNode selNode){
		nativeAddCollisionResponseAnimator(mScene.getId(selNode),getId());
	}
	
	/**
	 * ɾ���ڵ��ϵ����ж���
	 */
	//����ȥ�������ĺ���ʵ����������Զ࣬�ݲ���
	public void removeAllAnimator(){
		nativeRemoveAllAnimator(getId());
	}
	
	/**
	 * ���ڵ�ӳ������Ƴ�
	 */
	public void remove(){
		mScene.removeNode(this);
	}	
	
	/**
	 * ��Java�㱣�漰��ʼ���ڵ���Ϣ
	 * @param pos �ڵ�ĳ�ʼλ��
	 * @param parent �ڵ�ĸ��ڵ�
	 */
	void javaLoadDataAndInit(Vector3d pos, SceneNode parent){
		mPosition[0] = pos;
		mParent = parent;
		mark();
		mScene.registerNode(this);
	}
	
	/**
	 * Ψһ���캯��
	 */
	SceneNode() {
		this.Id = mScene.getNewId();
		
		//��һ���ǵ�ǰֵ���ڶ����Ǳ��ֵ
		mPosition = new Vector3d[2];
		mPosition[0] = new Vector3d(0, 0, 0);
		mPosition[1] = new Vector3d();
		
		mRotation = new Vector3d[2];
		mRotation[0] = new Vector3d(0, 0, 0);
		mRotation[1] = new Vector3d();
		
		mScale = new Vector3d[2];
		mScale[0] = new Vector3d(1, 1, 1);
		mScale[1] = new Vector3d();
		
		mNodeType = TYPE_COMMON;
	}
	
	/**
	 * ���ؽڵ��IDֵ
	 * @return �ڵ��IDֵ
	 */
	int getId() {return Id;}
	
	protected static Scene mScene;
	
	protected final int Id;
	
	protected SceneNode mParent = null;
	protected boolean mIsVisible = true;
	protected Vector3d []mPosition;
	protected Vector3d []mRotation;
	protected Vector3d []mScale;
	
	protected int mNodeType;
	
	//! transform native method.
	private native int nativeSetParent(int parent, int Id);
	private native int nativeSetVisible(boolean isVisible, int Id);
	private native int nativeSetRotation(double x, double y, double z, int Id);
	private native int nativeSetScale(double x, double y, double z, int Id);
	private native int nativeSetPosition(double x, double y, double z, int Id);
	
	//! animator native method.
	private native int nativeAddRotationAnimator(
			double x, double y, double z, int Id);
			
	private native int nativeAddFlyCircleAnimator(
			double cx, double cy, double cz, double radius, double speed, 
			double ax, double ay, double az, double startPosition, 
			double radiusEllipsoid, int Id);
			
	private native int nativeAddFlyStraightAnimator(double sx, double sy, double sz,
			double dx, double dy, double dz, double time, 
			boolean loop, boolean pingpong, int Id);
			
	private native int nativeAddDeleteAnimator(int ms, int Id);
	private native int nativeRemoveAllAnimator(int Id);
	private native int nativeAddCollisionResponseAnimator(int selId, int Id);
	
	public class TransformationInfo{
		public Vector3d Position;
		public Vector3d Rotation;
		public Vector3d Scale;
	}
}
