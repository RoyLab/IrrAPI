package zte.irrlib.scene;

import zte.irrlib.core.Vector3d;

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
		mParent = node;
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
		
		nativeSetPosition(mPosition[0].x, mPosition[0].y, mPosition[0].z, getId());
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
		
		nativeSetRotation(mRotation[0].x, mRotation[0].y, mRotation[0].z, getId());
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
		
		nativeSetScale(mScale[0].x, mScale[0].y, mScale[0].z, getId());
	}
	
	/**
	 * 返回节点相对于父节点的位置坐标。
	 * @return 相对于父节点的位置坐标
	 */
	public Vector3d getPosition(){
		return new Vector3d(mPosition[0]);
	}
	
	/**
	 * 返回节点的旋转角度。
	 * @return 节点的旋转角度
	 */
	public Vector3d getRotation(){
		return new Vector3d(mRotation[0]); 
	}
	
	/**
	 * 返回节点相对于父节点的大小。
	 * @return 节点相对于父节点的大小
	 */
	public Vector3d getScale(){
		return new Vector3d(mScale[0]); 
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
	 * 为节点设置位置、旋转和大小的变换值。
	 * @param info 节点变换信息值
	 */
	public void setTransformationInfo(TransformationInfo info){
		mPosition[0].copy(info.Position);
		mRotation[0].copy(info.Rotation);
		mScale[0].copy(info.Scale);
	}
	
	/**
	 * 添加平移动画。
	 * @param start 平移动画起始点坐标
	 * @param end 平移动画目标点坐标
	 * @param time 平移动画所用时间，单位：毫秒（ms）
	 * @param loop 值为true时动画循环进行，值为false时动画进行一次
	 * @param pingpong 值为true时节点到达目标点时会返回初始点，否则不返回
	 */
	public void addFlyStraightAnimator(Vector3d start, Vector3d end, 
			double time, boolean loop, boolean pingpong){
		nativeAddFlyStraightAnimator(start.x, start.y, start.z, 
				end.x, end.y, end.z, time, loop, pingpong, getId());
	}
	
	/**
	 * 添加环形运动动画。
	 * @param center 运动所绕圆环的圆心坐标
	 * @param radius 运动所绕圆环的半径
	 * @param speed 运动速率，单位：弧度/毫秒
	 * @param axis 运动所绕的轴，即运动平面的up vector
	 * @param startPoint 运动相对起始点。即0.5时从半圆处开始运动
	 * @param radiusEllipsoid 运动轨迹的椭圆程度。默认值为0，运动轨迹为圆。
	 */
	public void addFlyCircleAnimator(Vector3d center, double radius,
			double speed, Vector3d axis, double startPoint, double radiusEllipsoid){
		nativeAddFlyCircleAnimator(center.x, center.y, center.z, radius, speed, 
				axis.x, axis.y, axis.z, startPoint, startPoint, getId());
	}
	
	/**
	 * 添加旋转动画。
	 * @param speed 绕各个轴向的旋转速率，单位：度/10毫秒
	 */
	public void addRotationAnimator(Vector3d speed){
		nativeAddRotationAnimator(speed.x, speed.y, speed.z, getId());
	}
	
	/**
	 * 添加消失动画，节点将于指定时间内消失。
	 * @param ms 消失动画的时间，单位毫秒
	 */
	public void addDeleteAnimator(int ms){
		nativeAddDeleteAnimator(ms, getId());
	}
	
	/**
	 * 添加对指定节点的碰撞检测响应。
	 * @param selNode 指定的节点对象
	 */
	public void addCollisionResponseAnimator(SceneNode selNode){
		nativeAddCollisionResponseAnimator(mScene.getId(selNode),getId());
	}
	
	/**
	 * 删除节点上的所有动画
	 */
	//单个去除动画的函数实现所需代码略多，暂不管
	public void removeAllAnimator(){
		nativeRemoveAllAnimator(getId());
	}
	
	/**
	 * 将节点从场景中移除
	 */
	public void remove(){
		mScene.removeNode(this);
	}	
	
	/**
	 * 在Java层保存及初始化节点信息
	 * @param pos 节点的初始位置
	 * @param parent 节点的父节点
	 */
	void javaLoadDataAndInit(Vector3d pos, SceneNode parent){
		mPosition[0] = pos;
		mParent = parent;
		mark();
		mScene.registerNode(this);
	}
	
	/**
	 * 唯一构造函数
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
