package zte.irrlib.scene;

import java.util.ArrayList;

import zte.irrlib.core.Vector3d;

/**
 * �����ڵ�Ļ����ࡣ
 * @author Fxx
 *
 */
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
		if (mParent != null) mParent.removeChild2(this);
		mParent = node;
		if (mParent != null) mParent.addChild(this);
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
		
		nativeSetPosition(mPosition[0].X, mPosition[0].Y, mPosition[0].Z, getId());
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
		
		nativeSetRotation(mRotation[0].X, mRotation[0].Y, mRotation[0].Z, getId());
	}
	
	/**
	 * ȡ�ø��ڵ��ָ��
	 * @return ���ڵ��ָ��
	 */
	public SceneNode getParent(){
		return mParent;
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
		
		nativeSetScale(mScale[0].X, mScale[0].Y, mScale[0].Z, getId());
	}
	
	/**
	 * ���ؽڵ�����ڸ��ڵ��λ�����ꡣ
	 * @return ����ڸ��ڵ��λ������
	 */
	public Vector3d getPosition(){
		return new Vector3d(mPosition[0]);
	}
	
	/**
	 * ���ؽڵ�ľ���λ�����ꡣ
	 * @return ����λ������
	 */
	public Vector3d getAbsolutePosition(){
		if (mParent == null) return new Vector3d(mPosition[0]);
		return new Vector3d(mPosition[0]).plus(mParent.getAbsolutePosition());
	}
	
	/**
	 * ���ؽڵ����ת�Ƕȡ�
	 * @return �ڵ����ת�Ƕ�
	 */
	public Vector3d getRotation(){
		return new Vector3d(mRotation[0]); 
	}
	
	/**
	 * ���ؽڵ�ľ�����ת�Ƕȡ�
	 * @return ������ת�Ƕ�
	 */
	public Vector3d getAbsoluteRotation(){
		if (mParent == null) return new Vector3d(mRotation[0]);
		return new Vector3d(mRotation[0]).plus(mParent.getAbsoluteRotation());
	}
	
	/**
	 * ���ؽڵ�����ڸ��ڵ�����š�
	 * @return �ڵ�����ڸ��ڵ������
	 */
	public Vector3d getScale(){
		return new Vector3d(mScale[0]); 
	}
	
	/**
	 * ���ؽڵ�ľ������š�
	 * @return ��������
	 */
	public Vector3d getAbsoluteScale(){
		if (mParent == null) return new Vector3d(mScale[0]);
		return new Vector3d(mScale[0]).plus(mParent.getAbsoluteScale());
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
	 * ���ؽڵ��ֱ���ӽڵ�ĸ���
	 * @return ֱ���ӽڵ�ĸ���
	 */
	public int getChildrenCount(){
		if (mChild == null) return 0;
		return mChild.size();
	}
	
	/**
	 * ͨ�����ȡ���ӽڵ�
	 * @param index ���
	 * @return ��Ӧ���ӽڵ�
	 */
	public SceneNode getChild(int index){
		if (mChild == null) return null;
		return mChild.get(index);
	}
	
	/**
	 * �������е�һ���ӽڵ㣨����������˳�����ͬ���Ĳ���
	 * @param f �����ص��࣬�����operate���������ÿ��һ���ӽڵ㱻����һ��
	 */
	public void do2EveryChild(TraversalCallback f){
		if (mChild == null) return;
		for (SceneNode child: mChild){
			f.operate(child);
		}
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
	 * ���ƽ�ƶ���<br>
	 * ע�⣬������ӣ�ͨ��������벻Ҫ��ô�������Animatorʱ�������ά��
	 * Animator�����˳��˳���������Ӱ��ÿһ֡�ĸ���Ч�������磬������ײ���
	 * {@link MeshSceneNode#addCollisionResponseAnimator(SceneNode, boolean, boolean)}�����ֱ��
	 * ���ж���{@link #addFlyStraightAnimator(zte.irrlib.core.Vector3d, 
	 * zte.irrlib.core.Vector3d, double, boolean, boolean)}����ô��ײ����Ч��
	 * �ᱻ����ִ�е�ֱ�߷��ж��������ǣ��������˳������ײ����λ�ý���ִ�й����ж������
	 * λ�ã������������Ҫʹ�ö����������ȷ���ڵ�û�б���ӹ�������ʹ��{@link #removeAllAnimators()}
	 * ������ж���������һ������ӣ�����һֱ�����ڽڵ���ֱ��{@link #removeAllAnimators()}�����á�
	 * @param start ƽ�ƶ�����ʼ������
	 * @param end ƽ�ƶ���Ŀ�������
	 * @param time ƽ�ƶ�������ʱ�䣬��λ�����루ms��
	 * @param loop ֵΪtrueʱ����ѭ�����У�ֵΪfalseʱ��������һ��
	 * @param pingpong ֵΪtrueʱ�ڵ㵽��Ŀ���ʱ�᷵�س�ʼ�㣬���򲻷���
	 */
	public void addFlyStraightAnimator(Vector3d start, Vector3d end, 
			double time, boolean loop, boolean pingpong){
		if (nativeAddFlyStraightAnimator(start.X, start.Y, start.Z, 
				end.X, end.Y, end.Z, time, loop, pingpong, getId()) == 0)
			addAnimator();
	}
	
	/**
	 * ��ӻ����˶�����<br>
	 * ע�⣬������ӣ�ͨ��������벻Ҫ��ô�������Animatorʱ�������ά��
	 * Animator�����˳��˳���������Ӱ��ÿһ֡�ĸ���Ч�������磬������ײ���
	 * {@link MeshSceneNode#addCollisionResponseAnimator(SceneNode, boolean, boolean)}�����ֱ��
	 * ���ж���{@link #addFlyStraightAnimator(zte.irrlib.core.Vector3d, 
	 * zte.irrlib.core.Vector3d, double, boolean, boolean)}����ô��ײ����Ч��
	 * �ᱻ����ִ�е�ֱ�߷��ж��������ǣ��������˳������ײ����λ�ý���ִ�й����ж������
	 * λ�ã������������Ҫʹ�ö����������ȷ���ڵ�û�б���ӹ�������ʹ��{@link #removeAllAnimators()}
	 * ������ж���������һ������ӣ�����һֱ�����ڽڵ���ֱ��{@link #removeAllAnimators()}�����á�
	 * @param center �˶�����Բ����Բ������
	 * @param radius �˶�����Բ���İ뾶
	 * @param speed �˶����ʣ���λ������/����
	 * @param axis �˶����Ƶ��ᣬ���˶�ƽ���up vector
	 * @param startPoint �˶������ʼ�㡣��0.5ʱ�Ӱ�Բ����ʼ�˶�
	 * @param radiusEllipsoid �˶��켣����Բ�̶ȡ�Ĭ��ֵΪ0���˶��켣ΪԲ��
	 */
	public void addFlyCircleAnimator(Vector3d center, double radius,
			double speed, Vector3d axis, double startPoint, double radiusEllipsoid){
		if (nativeAddFlyCircleAnimator(center.X, center.Y, center.Z, radius, speed, 
				axis.X, axis.Y, axis.Z, startPoint, startPoint, getId()) == 0)
				addAnimator();
	}
	
	/**
	 * �����ת����<br>
	 * ע�⣬������ӣ�ͨ��������벻Ҫ��ô�������Animatorʱ�������ά��
	 * Animator�����˳��˳���������Ӱ��ÿһ֡�ĸ���Ч�������磬������ײ���
	 * {@link MeshSceneNode#addCollisionResponseAnimator(SceneNode, boolean, boolean)}�����ֱ��
	 * ���ж���{@link #addFlyStraightAnimator(zte.irrlib.core.Vector3d, 
	 * zte.irrlib.core.Vector3d, double, boolean, boolean)}����ô��ײ����Ч��
	 * �ᱻ����ִ�е�ֱ�߷��ж��������ǣ��������˳������ײ����λ�ý���ִ�й����ж������
	 * λ�ã������������Ҫʹ�ö����������ȷ���ڵ�û�б���ӹ�������ʹ��{@link #removeAllAnimators()}
	 * ������ж���������һ������ӣ�����һֱ�����ڽڵ���ֱ��{@link #removeAllAnimators()}�����á�
	 * @param speed �Ƹ����������ת���ʣ���λ����/10����
	 */
	public void addRotationAnimator(Vector3d speed){
		if (nativeAddRotationAnimator(speed.X, speed.Y, speed.Z, getId()) == 0)
			addAnimator();
	}
	
	/**
	 * �����ʧ�������ڵ㽫��ָ��ʱ������ʧ<br>
	 * ע�⣬������ӣ�ͨ��������벻Ҫ��ô�������Animatorʱ�������ά��
	 * Animator�����˳��˳���������Ӱ��ÿһ֡�ĸ���Ч�������磬������ײ���
	 * {@link MeshSceneNode#addCollisionResponseAnimator(SceneNode, boolean, boolean)}�����ֱ��
	 * ���ж���{@link #addFlyStraightAnimator(zte.irrlib.core.Vector3d, 
	 * zte.irrlib.core.Vector3d, double, boolean, boolean)}����ô��ײ����Ч��
	 * �ᱻ����ִ�е�ֱ�߷��ж��������ǣ��������˳������ײ����λ�ý���ִ�й����ж������
	 * λ�ã������������Ҫʹ�ö����������ȷ���ڵ�û�б���ӹ�������ʹ��{@link #removeAllAnimators()}
	 * ������ж���������һ������ӣ�����һֱ�����ڽڵ���ֱ��{@link #removeAllAnimators()}�����á�
	 * @param ms ��ʧ������ʱ�䣬��λ����
	 */
	public void addDeleteAnimator(int ms){
		if (nativeAddDeleteAnimator(ms, getId()) == 0)
			addAnimator();
	}
	
	/**
	 * ɾ���ڵ��ϵ����ж���
	 */
	public void removeAllAnimators(){
		nativeRemoveAllAnimator(getId());
		mHasAnimator = 0;
	}
	
	/**
	 * ȥ���ϴ���ӵĶ���
	 * @return ���ڵ����Ѿ�û�ж������򷵻�false
	 */
	public boolean removeLastAnimator(){
		if (nativeRemoveLastAnimator(getId()) == 0){
			mHasAnimator -= 1;
			return true;
		}
		else return false;
	}
	
	/**
	 * ���ڵ�ӳ������Ƴ�
	 */
	public boolean remove(){
		return mScene.removeNode(this);
	}
	
	/**
	 * ��ָ����һ���ӽڵ��Ϊ��ǰ�ڵ���ֵܽڵ�
	 * @param child �ӽڵ��ָ��
	 * @return Ϊ�����ʾ�Ƴ��ɹ�
	 */
	public boolean removeChild(SceneNode child){
		if (child == null || mChild == null || 
				!mChild.contains(child)) return false;
		child.setParent(mParent);
		return true;
	}

	@Override/** ��¡�ڵ�*/
	public SceneNode clone(){
		SceneNode res = softClone();
		cloneInNativeAndSetupNodesId(res);
		return res;
	}
	
	/**
	 * ���ؽڵ����Ƿ����Animator
	 * @return Ϊ�����ʾ����
	 */
	public boolean hasAnimator(){
		return (mHasAnimator > 0);
	}
	
	void addAnimator(){
		mHasAnimator += 1;
	}
	
	void onAnimate(){
		if (hasAnimator()){
			nativeUpdatePosition(mPosition[0], false, getId());
		}
	}

	/**
	 * ��Java�㱣�漰��ʼ���ڵ���Ϣ
	 * param pos �ڵ�ĳ�ʼλ��
	 * @param parent �ڵ�ĸ��ڵ�
	 */
	void javaLoadDataAndInit(Vector3d pos, SceneNode parent){
		mPosition[0] = pos;
		setParent(parent);
		mark();
		mScene.registerNode(this);
	}
	
	/**
	 * ���캯��
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
	
	void addChild(SceneNode child){
		if (mChild == null) mChild = new ArrayList<SceneNode>();
		mChild.add(child);
	}
	
	void removeChild2(SceneNode child){
		mChild.remove(child);
	}
	
	/**
	 * ��java�������Լ���ע�ᣬ���ҵ����ӽڵ�ĵ������趨�ø��ӹ�ϵ
	 * @return ��¡�ĸ��ڵ�
	 */
	protected SceneNode softClone(){
		SceneNode res = new SceneNode(this);
		softCopyChildren(res);
		return res;
	}
	
	protected final void softCopyChildren(SceneNode root){
		if (mChild != null){
			root.mChild = new ArrayList<SceneNode>();
			for (SceneNode node:mChild){
				SceneNode son = node.softClone();
				son.mParent = root;
				root.mChild.add(son);
			}
		}
	}
	
	protected final void setupNodesId(SceneNode res, SceneNode des){
		if (mChild == null) return;
		for (int i = 0; i < des.getChildrenCount(); i++){
			nativeChangeId(res.getChild(i).getId(), des.getChild(i).getId(), des.getId());
			setupNodesId(res.getChild(i), des.getChild(i));
		}
	}
	
	protected final void cloneInNativeAndSetupNodesId(SceneNode res){
		nativeCloneNode(getId(), res.getId());
		setupNodesId(this, res);
	}
	
	protected SceneNode(SceneNode node) {
		Id = mScene.getNewId();
		mPosition = new Vector3d[2];
		mPosition[0] = new Vector3d(node.mPosition[0]);
		mPosition[1] = new Vector3d(node.mPosition[1]);
		
		mRotation = new Vector3d[2];
		mRotation[0] = new Vector3d(node.mRotation[0]);
		mRotation[1] = new Vector3d(node.mRotation[1]);
		
		mScale = new Vector3d[2];
		mScale[0] = new Vector3d(node.mScale[0]);
		mScale[1] = new Vector3d(node.mScale[1]);
		
		mNodeType = node.mNodeType;
		mParent = node.mParent;
		mIsVisible = node.mIsVisible;
		mScene.registerNode(this);
	}
	
	protected static Scene mScene;
	
	protected final int Id;
	
	protected SceneNode mParent = null;
	protected boolean mIsVisible = true;
	protected Vector3d []mPosition;
	protected Vector3d []mRotation;
	protected Vector3d []mScale;
	protected int mHasAnimator;
	
	protected int mNodeType;
	protected ArrayList<SceneNode> mChild;
	
	//! transform native method.
	private native int nativeSetParent(int parent, int Id);
	private native int nativeSetVisible(boolean isVisible, int Id);
	private native int nativeSetRotation(double x, double y, double z, int Id);
	private native int nativeSetScale(double x, double y, double z, int Id);
	private native int nativeSetPosition(double x, double y, double z, int Id);
	private native int nativeUpdatePosition(Vector3d pos, boolean isAbsolute, int Id);
	
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
	protected native int nativeRemoveLastAnimator(int Id);
	//private native int nativeAddCollisionResponseAnimator(int selId, int Id);
	
	protected native int nativeCreateEmptySceneNode(int Id, boolean isLight);
	protected native int nativeCloneNode(int res, int des);
	protected native int nativeChangeId(int res, int des, int parent);
	
	/**
	 * �ڵ�ı任��Ϣ��
	 * @author Roy
	 *
	 */
	public class TransformationInfo{
		public Vector3d Position;
		public Vector3d Rotation;
		public Vector3d Scale;
	}
	
	/**
	 * �����ص��࣬�μ�{@link SceneNode#do2EveryChild(TraversalCallback)}
	 * @author Roy
	 *
	 */
	public interface TraversalCallback{
		void operate(SceneNode node);
	}
}
