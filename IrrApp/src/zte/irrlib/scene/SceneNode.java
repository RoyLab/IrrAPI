package zte.irrlib.scene;

import java.util.ArrayList;

import zte.irrlib.core.Vector3d;

/**
 * 场景节点的基本类。
 * @author Fxx
 *
 */
public class SceneNode {
	
	//节点类型
	public static final int TYPE_NULL = 0x00000000;
	public static final int TYPE_COMMON = 0x00000001;
	public static final int TYPE_MESH = 0x00000102;
	public static final int TYPE_ANIMATE_MESH = 0x00000103;
	public static final int TYPE_LIGHT = 0x00000004;
	public static final int TYPE_BILLBOARD = 0x00001005;
	public static final int TYPE_BILLBOARD_GROUP = 0x00000006;
	public static final int TYPE_CAMERA = 0x00000007;
	public static final int TYPE_PARTICLE_SYSTEM = 0x00000008;
	
	//变换方式
	public static final int TRANS_ABSOLUTE = 0;
	public static final int TRANS_RELATIVE = 1;
	
	public static final int FLAG_MATERIAL_OWNER = 0x00001000;
	
	/**
	 * 设置场景对象。
	 * @param scene 所使用的场景对象
	 */
	public static final void setScene(Scene scene){
		mScene = scene;
	}
	
	/**
	 * 返回节点类型。
	 * @return 节点类型
	 */
	public final int getNodeType(){
		return mNodeType;
	}
	
	/**
	 * 对初始位置、旋转及大小信息进行备份。
	 */
	public void mark(){
		mPosition[1].copy(mPosition[0]);
		mRotation[1].copy(mRotation[0]);
		mScale[1].copy(mScale[0]);
	}
	
	/**
	 * 设置节点的父节点。
	 * @param node 父节点对象
	 */
	public void setParent(SceneNode node){
		if (mParent != null) mParent.removeChild2(this);
		mParent = node;
		if (mParent != null) mParent.addChild(this);
		nativeSetParent(mScene.getId(mParent), getId());
	}
	
	/**
	 * 设置节点是否可见。
	 * @param isVisible 值为true时节点可见，否则不可见
	 */
	public void setVisible(boolean isVisible){
		mIsVisible = isVisible;
		nativeSetVisible(isVisible, getId());
	}
	
	/**
	 * 设置节点位置坐标。
	 * @param para	节点位置坐标
	 * @param mode 坐标变换模式
	 * 		TRANS_ABSOLUTE：	依据父节点进行位置变换
	 * 		TRANS_RELATIVE：		依据模型节点自身初始位置进行变换
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
	 * 设置节点旋转角度。
	 * @param para	沿三个轴向的旋转角度值
	 * @param mode 旋转模式
	 * 		TRANS_ABSOLUTE：	依据旋转参数直接进行旋转
	 * 		TRANS_RELATIVE：		依据模型节点自身初始旋转角度进行旋转
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
	 * 取得父节点的指针
	 * @return 父节点的指针
	 */
	public SceneNode getParent(){
		return mParent;
	}
	
	/**
	 * 设置节点的大小。
	 * @param para	沿三个轴向大小变换值
	 * @param mode 变换模式
	 * 		TRANS_ABSOLUTE：	依据节点的父节点进行大小变换
	 * 		TRANS_RELATIVE：		依据模型节点自身初始大小进行变换
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
	 * 返回节点相对于父节点的位置坐标。
	 * @return 相对于父节点的位置坐标
	 */
	public Vector3d getPosition(){
		return new Vector3d(mPosition[0]);
	}
	
	/**
	 * 返回节点的绝对位置坐标。
	 * @return 绝对位置坐标
	 */
	public Vector3d getAbsolutePosition(){
		if (mParent == null) return new Vector3d(mPosition[0]);
		return new Vector3d(mPosition[0]).plus(mParent.getAbsolutePosition());
	}
	
	/**
	 * 返回节点的旋转角度。
	 * @return 节点的旋转角度
	 */
	public Vector3d getRotation(){
		return new Vector3d(mRotation[0]); 
	}
	
	/**
	 * 返回节点的绝对旋转角度。
	 * @return 绝对旋转角度
	 */
	public Vector3d getAbsoluteRotation(){
		if (mParent == null) return new Vector3d(mRotation[0]);
		return new Vector3d(mRotation[0]).plus(mParent.getAbsoluteRotation());
	}
	
	/**
	 * 返回节点相对于父节点的缩放。
	 * @return 节点相对于父节点的缩放
	 */
	public Vector3d getScale(){
		return new Vector3d(mScale[0]); 
	}
	
	/**
	 * 返回节点的绝对缩放。
	 * @return 绝对缩放
	 */
	public Vector3d getAbsoluteScale(){
		if (mParent == null) return new Vector3d(mScale[0]);
		return new Vector3d(mScale[0]).plus(mParent.getAbsoluteScale());
	}
	
	/**
	 * 返回节点的位置、旋转和大小的变换值。
	 * @return 节点的位置、旋转和大小的变换值
	 */
	public TransformationInfo getTransformationInfo(){
		TransformationInfo info = new TransformationInfo();
		info.Position = new Vector3d(mPosition[0]);
		info.Rotation = new Vector3d(mRotation[0]);
		info.Scale = new Vector3d(mScale[0]);
		
		return info;
	}
	
	/**
	 * 返回节点的直接子节点的个数
	 * @return 直接子节点的个数
	 */
	public int getChildrenCount(){
		if (mChild == null) return 0;
		return mChild.size();
	}
	
	/**
	 * 通过序号取得子节点
	 * @param index 序号
	 * @return 对应的子节点
	 */
	public SceneNode getChild(int index){
		if (mChild == null) return null;
		return mChild.get(index);
	}
	
	/**
	 * 遍历所有的一级子节点（不迭代），顺序进行同样的操作
	 * @param f 遍历回调类，该类的operate方法会针对每个一级子节点被调用一次
	 */
	public void do2EveryChild(TraversalCallback f){
		if (mChild == null) return;
		for (SceneNode child: mChild){
			f.operate(child);
		}
	}
	
	/**
	 * 为节点设置位置、旋转和大小的变换值。
	 * @param info 节点变换信息值
	 */
	public void setTransformationInfo(TransformationInfo info){
		mPosition[0].copy(info.Position);
		mRotation[0].copy(info.Rotation);
		mScale[0].copy(info.Scale);
	}
	
	/**
	 * 添加平移动画<br>
	 * 注意，当你添加（通常情况下请不要这么做）多个Animator时，请谨慎维护
	 * Animator的添加顺序，顺序会显著的影响每一帧的更新效果（比如，先做碰撞检测
	 * {@link MeshSceneNode#addCollisionResponseAnimator(SceneNode, boolean, boolean)}再添加直线
	 * 飞行动画{@link #addFlyStraightAnimator(zte.irrlib.core.Vector3d, 
	 * zte.irrlib.core.Vector3d, double, boolean, boolean)}，那么碰撞检测的效果
	 * 会被后续执行的直线飞行动画所覆盖，如果调换顺序，则碰撞检测的位置将是执行过飞行动画后的
	 * 位置）。如果您不需要使用多个动画，请确保节点没有被添加过动画或使用{@link #removeAllAnimators()}
	 * 清除所有动画，动画一旦被添加，它会一直存在于节点上直到{@link #removeAllAnimators()}被调用。
	 * @param start 平移动画起始点坐标
	 * @param end 平移动画目标点坐标
	 * @param time 平移动画所用时间，单位：毫秒（ms）
	 * @param loop 值为true时动画循环进行，值为false时动画进行一次
	 * @param pingpong 值为true时节点到达目标点时会返回初始点，否则不返回
	 */
	public void addFlyStraightAnimator(Vector3d start, Vector3d end, 
			double time, boolean loop, boolean pingpong){
		if (nativeAddFlyStraightAnimator(start.X, start.Y, start.Z, 
				end.X, end.Y, end.Z, time, loop, pingpong, getId()) == 0)
			addAnimator();
	}
	
	/**
	 * 添加环形运动动画<br>
	 * 注意，当你添加（通常情况下请不要这么做）多个Animator时，请谨慎维护
	 * Animator的添加顺序，顺序会显著的影响每一帧的更新效果（比如，先做碰撞检测
	 * {@link MeshSceneNode#addCollisionResponseAnimator(SceneNode, boolean, boolean)}再添加直线
	 * 飞行动画{@link #addFlyStraightAnimator(zte.irrlib.core.Vector3d, 
	 * zte.irrlib.core.Vector3d, double, boolean, boolean)}，那么碰撞检测的效果
	 * 会被后续执行的直线飞行动画所覆盖，如果调换顺序，则碰撞检测的位置将是执行过飞行动画后的
	 * 位置）。如果您不需要使用多个动画，请确保节点没有被添加过动画或使用{@link #removeAllAnimators()}
	 * 清除所有动画，动画一旦被添加，它会一直存在于节点上直到{@link #removeAllAnimators()}被调用。
	 * @param center 运动所绕圆环的圆心坐标
	 * @param radius 运动所绕圆环的半径
	 * @param speed 运动速率，单位：弧度/毫秒
	 * @param axis 运动所绕的轴，即运动平面的up vector
	 * @param startPoint 运动相对起始点。即0.5时从半圆处开始运动
	 * @param radiusEllipsoid 运动轨迹的椭圆程度。默认值为0，运动轨迹为圆。
	 */
	public void addFlyCircleAnimator(Vector3d center, double radius,
			double speed, Vector3d axis, double startPoint, double radiusEllipsoid){
		if (nativeAddFlyCircleAnimator(center.X, center.Y, center.Z, radius, speed, 
				axis.X, axis.Y, axis.Z, startPoint, startPoint, getId()) == 0)
				addAnimator();
	}
	
	/**
	 * 添加旋转动画<br>
	 * 注意，当你添加（通常情况下请不要这么做）多个Animator时，请谨慎维护
	 * Animator的添加顺序，顺序会显著的影响每一帧的更新效果（比如，先做碰撞检测
	 * {@link MeshSceneNode#addCollisionResponseAnimator(SceneNode, boolean, boolean)}再添加直线
	 * 飞行动画{@link #addFlyStraightAnimator(zte.irrlib.core.Vector3d, 
	 * zte.irrlib.core.Vector3d, double, boolean, boolean)}，那么碰撞检测的效果
	 * 会被后续执行的直线飞行动画所覆盖，如果调换顺序，则碰撞检测的位置将是执行过飞行动画后的
	 * 位置）。如果您不需要使用多个动画，请确保节点没有被添加过动画或使用{@link #removeAllAnimators()}
	 * 清除所有动画，动画一旦被添加，它会一直存在于节点上直到{@link #removeAllAnimators()}被调用。
	 * @param speed 绕各个轴向的旋转速率，单位：度/10毫秒
	 */
	public void addRotationAnimator(Vector3d speed){
		if (nativeAddRotationAnimator(speed.X, speed.Y, speed.Z, getId()) == 0)
			addAnimator();
	}
	
	/**
	 * 添加消失动画，节点将于指定时间内消失<br>
	 * 注意，当你添加（通常情况下请不要这么做）多个Animator时，请谨慎维护
	 * Animator的添加顺序，顺序会显著的影响每一帧的更新效果（比如，先做碰撞检测
	 * {@link MeshSceneNode#addCollisionResponseAnimator(SceneNode, boolean, boolean)}再添加直线
	 * 飞行动画{@link #addFlyStraightAnimator(zte.irrlib.core.Vector3d, 
	 * zte.irrlib.core.Vector3d, double, boolean, boolean)}，那么碰撞检测的效果
	 * 会被后续执行的直线飞行动画所覆盖，如果调换顺序，则碰撞检测的位置将是执行过飞行动画后的
	 * 位置）。如果您不需要使用多个动画，请确保节点没有被添加过动画或使用{@link #removeAllAnimators()}
	 * 清除所有动画，动画一旦被添加，它会一直存在于节点上直到{@link #removeAllAnimators()}被调用。
	 * @param ms 消失动画的时间，单位毫秒
	 */
	public void addDeleteAnimator(int ms){
		if (nativeAddDeleteAnimator(ms, getId()) == 0)
			addAnimator();
	}
	
	/**
	 * 删除节点上的所有动画
	 */
	public void removeAllAnimators(){
		nativeRemoveAllAnimator(getId());
		mHasAnimator = 0;
	}
	
	/**
	 * 去除上次添加的动画
	 * @return 若节点上已经没有动画，则返回false
	 */
	public boolean removeLastAnimator(){
		if (nativeRemoveLastAnimator(getId()) == 0){
			mHasAnimator -= 1;
			return true;
		}
		else return false;
	}
	
	/**
	 * 将节点从场景中移除
	 */
	public boolean remove(){
		return mScene.removeNode(this);
	}
	
	/**
	 * 将指定的一级子节点变为当前节点的兄弟节点
	 * @param child 子节点的指针
	 * @return 为真则表示移除成功
	 */
	public boolean removeChild(SceneNode child){
		if (child == null || mChild == null || 
				!mChild.contains(child)) return false;
		child.setParent(mParent);
		return true;
	}

	@Override/** 克隆节点*/
	public SceneNode clone(){
		SceneNode res = softClone();
		cloneInNativeAndSetupNodesId(res);
		return res;
	}
	
	/**
	 * 返回节点上是否存在Animator
	 * @return 为真则表示存在
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
	 * 在Java层保存及初始化节点信息
	 * param pos 节点的初始位置
	 * @param parent 节点的父节点
	 */
	void javaLoadDataAndInit(Vector3d pos, SceneNode parent){
		mPosition[0] = pos;
		setParent(parent);
		mark();
		mScene.registerNode(this);
	}
	
	/**
	 * 构造函数
	 */
	SceneNode() {
		this.Id = mScene.getNewId();
		
		//第一个是当前值，第二个是标记值
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
	 * 返回节点的ID值
	 * @return 节点的ID值
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
	 * 在java层做好自己的注册，并且调用子节点的迭代，设定好父子关系
	 * @return 克隆的父节点
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
	 * 节点的变换信息类
	 * @author Roy
	 *
	 */
	public class TransformationInfo{
		public Vector3d Position;
		public Vector3d Rotation;
		public Vector3d Scale;
	}
	
	/**
	 * 遍历回调类，参见{@link SceneNode#do2EveryChild(TraversalCallback)}
	 * @author Roy
	 *
	 */
	public interface TraversalCallback{
		void operate(SceneNode node);
	}
}
