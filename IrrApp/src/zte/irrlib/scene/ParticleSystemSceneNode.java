package zte.irrlib.scene;

import zte.irrlib.core.Color4i;
import zte.irrlib.core.Vector2d;
import zte.irrlib.core.Vector3d;
import zte.irrlib.core.BoxEmitter;

public class ParticleSystemSceneNode extends SceneNode {
	
	public static final int EMITTER_BOX = 0x01;
	
	/**
	 * 唯一构造函数
	 */
	public ParticleSystemSceneNode(){
		super();
		mNodeType = TYPE_PARTICLE_SYSTEM;
	}
	
	/**
	 * 为粒子系统节点设置盒状发射器
	 * @param para 所使用的发射器对象
	 */
	public void setBoxEmitter( BoxEmitter para){
		nativeSetBoxEmitter(para, getId());
	}
	
	/**
	 * 为粒子系统所有材质指定统一贴图
	 * @param path 贴图的路径
	 */
	public void setTexture(String path) {
		nativeAllSetTexture(mScene.getFullPath(path), getId());
	}
	
	/**
	 * 为粒子系统添加吸引影响因子，该影响因子将粒子吸引往指定点
	 * @param point 粒子被吸引到的目的点
	 * @param speed 粒子运动的速度，单位： units per second
	 * @param attract 值为true时运动趋势为吸引，否则为远离
	 * @param affectX 指定运动是否影响X轴
	 * @param affectY 指定运动是否影响Y轴
	 * @param affectZ 指定运动是否影响Z轴
	 */
	public void addAttractionParticleAffector(Vector3d point, int speed, boolean attract, boolean affectX, boolean affectY, boolean affectZ){
		nativeAddAttractionParticleAffector(point.x, point.y, point.z, speed, attract, affectX, affectY, affectZ, getId());
	}
	
	/**
	 * 为粒子系统添加变形影响因子，该影响因子改变粒子的大小
	 * @param scaleTo 粒子大小将被扩大的倍数
	 */
	 public void addScaleParticleAffector(Vector2d scaleTo){
		 nativeAddScaleParticleAffector(scaleTo.x, scaleTo.y, getId());
	 }
	 
	 /**
	  * 为粒子系统添加渐变影响因子，该影响因子使粒子往目标颜色渐变
	  * @param target 粒子渐变的目标颜色
	  * @param time 粒子渐变所需的时间，单位：毫秒（ms）
	  */
	 public void addFadeOutAffectorParticleAffector(Color4i target, int time){
		 nativeAddFadeOutAffectorParticleAffector(target.r(), target.g(), target.b(), target.a(), time, getId());
	 }
	 
	 /**
	  * 为粒子系统添加重力影响因子，该影响因子使粒子往重力方向运动
	  * @param gravity 重力的三维大小
	  * @param time 粒子受力时间，单位：毫秒（ms）
	  */
	 public void addGravityAffector(Vector3d gravity, int time){
		 nativeAddGravityAffector(gravity.x, gravity.y, gravity.z, time, getId());
	 }
	
	/**
	 * 为粒子系统添加旋转运动影响因子，改影响因子使粒子沿中心点以一定速度运动 
	 * @param speed 粒子运动的速度，单位：度/秒
	 * @param center 粒子运动围绕的中心点
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
