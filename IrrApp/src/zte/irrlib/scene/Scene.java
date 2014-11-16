package zte.irrlib.scene;

import java.util.ArrayList;

import zte.irrlib.Engine;
import zte.irrlib.TexMediaPlayer;
import zte.irrlib.Utils;
import zte.irrlib.core.Color3i;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Rect4d;
import zte.irrlib.core.Rect4i;
import zte.irrlib.core.Vector2d;
import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * 场景类，在zte.irrlib.scene包内，所有的类的构造方法均不可见，请使用
 * 对应的add方法({@link #addCubeSceneNode(Vector3d, double, SceneNode)}等)
 * 或者get方法({@link #getMediaPlayer()}等)来获得类的实例。<br>
 * @author Fxx
 *
 */
public class Scene {
	
	/**
	 * Log Tag
	 */
	public static final String TAG = "Scene";
	
	/**
	 * 如果某个类需要在每次绘制循环中对相机的移动做出反应，那么需要
	 * 完成改接口，并使用{@link Scene#regUpdatableObject(Updatable)}
	 * 注册，使用{@link Scene#unregUpdatableObject(Updatable)}取消注册
	 * @author Roy
	 *
	 */
	public interface Updatable{
		void enableUpdate(Scene sc, boolean flag);
		void updateFromCamera(CameraSceneNode cam);
	}
	
	/**
	 * 在场景中移除可根据当前相机更新信息的类
	 * @param obj 注册的类
	 * @return 是否成功移除
	 */
	public boolean unregUpdatableObject(Updatable obj){
		return mUpdateList.remove(obj);
	}
	
	/**
	 * 在场景中注册可根据当前相机更新信息的类
	 * @param obj 注册的类
	 */
	public void regUpdatableObject(Updatable obj){
		mUpdateList.add(obj);
	}
	
	/**
	 * 设置是否打开光照（默认关闭）
	 * @param flag 值为true时光照打开，否则关闭
	 */
	public void enableLighting(boolean flag){
		mEnableLighting = flag;
	}
	
	/**
	 * 光照开关是否打开
	 * @return 若为真，则光照已经被打开QFGJH 
	 */
	public boolean isLightingEnabled(){
		return mEnableLighting;
	}
	
	/**
	 * 设置字体文件
	 * assets读取被禁用时不可用
	 * @param font 字体图片文件名
	 */
	public void setFont(String font){
		if (mEngine.isNativeAssetsReaderEnabled() == false){
			Log.e(TAG, "assets is not allowed to be opened!");
			return;
		}
		nativeSetFontPath(getFullPath(font));
	}
	
	/**
	 * 设置所要使用的摄像机。
	 * @param camera 所要使用的摄像机对象
	 */
	public void setActiveCamera(CameraSceneNode camera){
		mActiveCamera = camera;
		nativeSetActiveCamera(getId(camera));
	}
	
	/**
	 * 设置背景颜色。
	 * @param color 背景颜色参数
	 */
	public void setClearColor(Color4i color){
		nativeSetClearColor(color.r(), color.g(), color.b(), color.a());
	}
	
	/**
	 * 设置环境光颜色。
	 * @param color 环境光颜色
	 */
	public void setAmbientLight(Color4i color){
		nativeSetAmbientLight(color.r(), color.g(), color.b(), color.a());
	}
	
	/**
	 * 返回当前使用的摄像机对象。
	 * @return 当前使用的摄像机对象
	 */
	public CameraSceneNode getActiveCamera(){
		return mActiveCamera;
	}
	
	/**
	 * 返回指定根节点下子节点中被指定射线击中的节点对象。该射线从摄像机出发，指向给定的坐标（x，y）。
	 * @param x 射线指向的点的x轴坐标值，坐标值依据设备显示器的分辨率决定
	 * @param y 射线指向的点的y轴坐标值，坐标值依据设备显示器的分辨率决定
	 * @param root 碰撞检测起始的根节点对象
	 * @return 指定根节点下子节点中被指定射线击中的节点对象
	 */
	public SceneNode getTouchedSceneNode(float x, float y, SceneNode root){
		return queryById(nativeGetTouchedSceneNode(x, y, getId(root)));
	}
	
	/**
	 * 返回渲染区域的尺寸。尺寸值依据设备显示器分辨率决定。
	 * @return 渲染区域的尺寸。
	 */
	public Vector2i getRenderSize(){
		return new Vector2i(mWidth, mHeight);
	}
	
	/**
	 * 返回指定ID值对应的节点对象。
	 * @param id 所要查找的节点ID值
	 * @return 指定ID值对应的节点对象。
	 */
	public SceneNode queryById(int id){
		if (id <= 0) return null;
		for (SceneNode node:mNodeList){
			if (getId(node) == id){
				return node;
			}
		}
		return null;
	}
	
	/**
	 * 返回指定节点的ID值。
	 * @param node 所要查找的节点对象
	 * @return 指定节点的ID值。
	 */
	public int getId(SceneNode node){
		if (node == null){
			return 0;
		}
		else return node.getId();
	}
	
	/**
	 * 绘制2D图像。
	 * @param path 所要绘制的图片所在路径
	 * @param des 绘制区域的位置
	 * @param src 用户所希望绘制的图片区域在整个图片中的坐标，坐标是归一化的，如为null，则绘制整张图片
	 * @param useAlphaAsTransparentValue 是否使用alpha通道（如果有）作为材质的透明度
	 */
	public void drawImage(String path, Rect4i des, Rect4d src, boolean useAlphaAsTransparentValue){
		if (src == null)
			nativeDrawImage(getFullPath(path), des, 
					0, 0, 1, 1, useAlphaAsTransparentValue);
		else
			nativeDrawImage(getFullPath(path), des, 
				src.Left, src.Top, src.Right, src.Bottom, useAlphaAsTransparentValue);
	}
	
	/**
	 * 绘制位图
	 * @param bit 所需要绘制的位图
	 * @param des 绘制区域的位置
	 * @param src 用户所希望绘制的图片区域在整个图片中的坐标，坐标是归一化的，如为null，则绘制整张图片
	 * @param useAlphaAsTransparentValue 是否使用alpha通道（如果有）作为材质的透明度
	 */
	public void drawImage(Bitmap bit, String name, Rect4i des, Rect4d src, boolean useAlphaAsTransparentValue){
		if (src == null)
			nativeDrawBitmap(name, bit, des, 0, 0, 0, 0, useAlphaAsTransparentValue);
		else
			nativeDrawBitmap(name, bit, des, src.Left, src.Top, src.Right,
					src.Bottom, useAlphaAsTransparentValue);
	}
	
	/**
	 * 绘制2D矩形。
	 * @param rect 所要绘制矩形的区域
	 * @param color 所要绘制的矩形的颜色
	 */
	public void drawRectangle(Rect4i rect, Color4i color){
		nativeDrawRectangle(rect.Left, rect.Top, rect.Right, rect.Bottom, color.r(), color.g(), color.b(), color.a());
	}
	
	/**
	 * 绘制具有多种颜色的2D矩形
	 * @param rect 绘制区域
	 * @param LT 左上角颜色
	 * @param LB 左下角颜色
	 * @param RB 右下角颜色
	 * @param RT 右上角颜色
	 */
	public void drawRectangle(Rect4i rect, Color4i LT, Color4i LB, Color4i RB, Color4i RT){
		nativeDrawRectangleChrome(rect, LT, LB, RB, RT);
	}
	
	/**
	 * 绘制文字，assets开关关闭时被禁用
	 * @param text 所要绘制的文字内容
	 * @param leftUp 所要绘制的文字所在位置的左上点坐标值
	 * @param color 所要绘制的文字的颜色
	 */
	public void drawText(String text, Vector2i leftUp, Color4i color){
		if (mEngine.isNativeAssetsReaderEnabled() == false){
			Log.e(TAG, "assets is not allowed to be opened!");
			return;
		}
		nativeDrawText(text, leftUp.X, leftUp.Y, color.r(), color.g(), color.b(), color.a());
	}
	
	/**
	 * 绘制所有节点，通过该函数实现对一帧场景里所有节点进行渲染。
	 */
	public void drawAllNodes(){
		nativeSmgrDrawAll();
	}
	
	/**
	 * 添加空节点。返回所添加的节点对象。注意：空节点没有大小。
	 * @param pos 所添加空节点所在位置
	 * @param parent 所添加空节点的父节点对象
	 * @return 所添加的节点对象
	 */
	public SceneNode addEmptySceneNode(Vector3d pos, SceneNode parent){
		SceneNode node = new SceneNode(pos, parent);
		if (nativeAddEmptySceneNode(pos.X, pos.Y, pos.Z, 
				getId(node), getId(parent), mEnableLighting) != 0)
			return null;
		
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加立方体节点，返回所添加的节点对象。
	 * @param pos 所添加立方体节点所在位置
	 * @param size 所添加立方体节点的尺寸，立方体三个维度大小一致。
	 * @param parent 所添加立方体节点的父节点对象
	 * @return 所添加的节点对象
	 */
	public MeshSceneNode addCubeSceneNode(Vector3d pos, double size, SceneNode parent){
		MeshSceneNode node = new MeshSceneNode(pos, parent);
		if (nativeAddCubeSceneNode(pos.X, pos.Y, pos.Z, 
				size, getId(node), getId(parent), mEnableLighting) != 0)
			return null;
		
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加矩形节点，返回所添加的节点对象。
	 * @param pos 所添加矩形节点所在位置
	 * @param size 所添加矩形节点的尺寸，指定长宽。
	 * @param parent 所添加立方体节点的父节点对象
	 * @return 所添加的节点对象
	 */
	public MeshSceneNode addRectSceneNode(String tex, Vector3d pos, Vector2d size, SceneNode parent){
		MeshSceneNode node = new MeshSceneNode(pos, parent);
		if (nativeAddRectSceneNode(getFullPath(tex), pos.X, pos.Y, pos.Z, size.X, size.Y,
				getId(node), getId(parent), mEnableLighting) != 0)
			return null;
		
		registerNode(node);
		return node;
	}	
	
	/**
	 * 添加球体节点，返回所添加的节点对象
	 * @param pos 所添加球体节点所在的位置
	 * @param radius 所添加球体节点的半径
	 * @param polyCount 所添加球体节点垂直方向、水平方向的顶点数目。球体总共有polyCount*polyCount个片面。polyCount必须小于256。
	 * @param parent 所添加球体节点的父节点对象
	 * @return 所添加的节点对象
	 */
	public MeshSceneNode addSphereSceneNode(Vector3d pos, double radius, int polyCount, SceneNode parent){
		MeshSceneNode node = new MeshSceneNode(pos, parent);
		if(nativeAddSphereSceneNode(pos.X, pos.Y, pos.Z, 
				radius, polyCount, getId(node), getId(parent), mEnableLighting) !=0)
			return null;
		
		registerNode(node);
		return node;
	}
	/**
	 * 添加静态模型节点，返回所添加的模型节点对象。
	 * @param path 所用静态模型的路径
	 * @param pos 所添加节点的位置
	 * @param optimizedByOctree 是否使用八叉树优化显示
	 * @param parent 所添加节点的父节点对象
	 * @return 所添加的节点对象
	 */
	public MeshSceneNode addMeshSceneNode(String path, Vector3d pos, boolean optimizedByOctree,SceneNode parent){
		MeshSceneNode node = new MeshSceneNode(pos, parent);
		if (nativeAddMeshSceneNode(getFullPath(path), pos.X, pos.Y, pos.Z,
				getId(node), getId(parent), mEnableLighting, optimizedByOctree) != 0)
			return null;
		
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加文字节点，返回所添加的文字节点对象。
	 * @param text 所添加的文字内容
	 * @param pos 所添加文字节点的位置
	 * @param size 所添加文字节点的大小
	 * @param parent 所添加文字节点的父节点对象
	 * @return 所添加的节点对象
	 */
	public SceneNode addTextNode(String text, Vector3d pos, double size, SceneNode parent){
		SceneNode node = new SceneNode(pos, parent);
		if (nativeAddTextNode(text, pos.X, pos.Y, pos.Z, size, 
				getId(node), getId(parent), mEnableLighting) != 0)
			return null;
		
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加相机节点，返回所添加的相机节点坐标。
	 * @param pos 所添加相机节点的位置
	 * @param lookAt 所添加相机节点的朝向坐标
	 * @param isActive 是否激活该相机节点，值为true时激活
	 * @param parent 所添加相机节点的父节点对象
	 * @return 所添加的节点对象
	 */
	public CameraSceneNode addCameraSceneNode(Vector3d pos, Vector3d lookAt, boolean isActive, SceneNode parent){
		CameraSceneNode node = new CameraSceneNode(pos, lookAt, parent);
		if (nativeAddCameraSceneNode(pos.X, pos.Y, pos.Z, 
				lookAt.X, lookAt.Y, lookAt.Z, isActive, 
				getId(node), getId(parent), mEnableLighting) != 0)
			return null;
		
		Vector2i size = getRenderSize();
		node.setAspectRatio((float)size.X/size.Y);
		registerNode(node);
		
		if (isActive){
			setActiveCamera(node);
		}
		return node;
	}
	
	/**
	 * 添加公告板节点，返回所添加公告板节点对象。
	 * @param pos 所添加公告板节点所在位置
	 * @param size 所添加公告板节点的二维尺寸
	 * @param parent 所添加公告板节点的父节点对象
	 * @return 所添加的节点对象
	 */
	public BillboardSceneNode addBillboardSceneNode(Vector3d pos, Vector2d size, SceneNode parent){
		BillboardSceneNode node = new BillboardSceneNode(pos, parent);
		if (nativeAddBillboardSceneNode(pos.X, pos.Y, pos.Z, 
				size.X, size.Y, getId(node), getId(parent), mEnableLighting) != 0){
			return null;
		}
		
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加光源节点，返回所添加的光源节点对象，参数的意义可以参见{@link zte.irrlib.core.SLight}
	 * @param pos 所添加光源节点的位置
	 * @param radius 所添加光源节点的照射半径
	 * @param color 所添加光源节点的颜色
	 * @param parent 所添加光源节点的父节点对象
	 * @return 所添加的节点对象
	 */
	public LightSceneNode addLightSceneNode(Vector3d pos, double radius, Color3i color, SceneNode parent){
		LightSceneNode node = new LightSceneNode(pos, parent);
		if (nativeAddLightSceneNode(pos.X, pos.Y, pos.Z, radius,
				color.r(), color.g(), color.b(), 
				getId(node), getId(parent), mEnableLighting) != 0){
			return null;
		}
		node.downloadLightData();
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加公告板组节点，返回所添加的公告板组对象。
	 * @param pos 所添加节点的位置
	 * @param parent 所添加节点的父节点
	 * @return 所添加的公告板组节点
	 */
	public BillboardGroup addBillboardGroup(Vector3d pos, SceneNode parent){
		BillboardGroup node = new BillboardGroup(pos, parent);
		if (nativeAddEmptySceneNode(pos.X, pos.Y, pos.Z, 
				getId(node), getId(parent), mEnableLighting) != 0){
			return null;
		}
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加动画模型节点，返回所添加的节点对象。
	 * @param path 所使用动画模型文件所在路径
	 * @param pos 所添加模型节点的位置
	 * @param parent 所添加模型节点的父节点
	 * @return 所添加的节点对象
	 */
	public AnimateMeshSceneNode addAnimateMeshSceneNode(String path, Vector3d pos, SceneNode parent){
		AnimateMeshSceneNode node = new AnimateMeshSceneNode(pos, parent);
		if (nativeAddAnimateMeshSceneNode(getFullPath(path), pos.X, pos.Y, pos.Z,
				getId(node), getId(parent), mEnableLighting) != 0){
			return null;
		}
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加粒子发射节点，返回所添加的例子发射对象。
	 * @param pos 所添加节点的位置
	 * @param withDefaultEmitter 指定是否使用默认的发射器
	 * @param parent 所添加节点的父节点
	 * @return 所添加的节点对象
	 */
	public ParticleSystemSceneNode addParticleSystemSceneNode(Vector3d pos, boolean withDefaultEmitter, SceneNode parent){
		ParticleSystemSceneNode node = new ParticleSystemSceneNode(pos, parent);
		if (nativeAddParticleSystemSceneNode(pos.X, pos.Y, pos.Z, 
				withDefaultEmitter, getId(node), getId(parent), mEnableLighting) != 0){
			return null;
		}
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加彗星拖尾效果粒子发射节点
	 * @param pos 粒子发射节点的位置
	 * @param parent 粒子发射节点的父节点
	 * @return 所添加的粒子发射节点对象
	 */
	public ParticleSystemSceneNode addCometTailSceneNode(Vector3d pos, double radius, double length, 
			Vector3d dir, SceneNode parent){
		ParticleSystemSceneNode node = new ParticleSystemSceneNode(pos, parent);
		if(nativeAddCometTailSceneNode(pos.X, pos.Y, pos.Z, radius, length, dir.X, dir.Y, dir.Z, getId(node), getId(parent), mEnableLighting) != 0){
			return null;
		}
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加星光粒子系统节点，即在指定的星空区域内随机产生闪烁的星星粒子。
	 * @param pos 所添加星光粒子系统节点的中心位置
	 * @param radius 所添加星光粒子系统节点涵盖的区域半径
	 * @param parent 所添加星光粒子系统节点的父节点对象
	 * @return 返回所添加的星光粒子系统节点
	 */
	public ParticleSystemSceneNode addStarsParticleSceneNode(Vector3d pos, double radius, SceneNode parent){
		ParticleSystemSceneNode node = new ParticleSystemSceneNode(pos, parent);
		if(nativeAddStarsParticleSceneNode(pos.X, pos.Y, pos.Z, radius, getId(node), getId(parent), mEnableLighting)!=0){
			return null;
		}
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加爆炸粒子系统节点
	 * @param pos	所添加的爆炸粒子系统节点所在位置坐标
	 * @param radius 爆炸效果波及范围的半径
	 * @param speed 爆炸粒子飞离中心点的速度，单位：unit/秒
	 * @param parent 爆炸利息系统节点的父节点对象
	 * @return 返回所添加的爆炸粒子系统节点对象
	 */
	public ParticleSystemSceneNode addExplosionParticleSceneNode(Vector3d pos, double radius, double speed,
			SceneNode parent){
		ParticleSystemSceneNode node = new ParticleSystemSceneNode(pos, parent);
		if(nativeAddExplosionParticleSceneNode(pos.X, pos.Y, pos.Z,
				radius, speed, getId(node), getId(parent), mEnableLighting)!=0){
			return null;
		}
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加雪粒子系统节点
	 * @param pos 位置坐标
	 * @param dir 方向
	 * @param size 大小
	 * @param speed 速度
	 * @param rate 粒子产生速率
	 * @param parent 父节点
	 * @return 创建的对象
	 */
	public ParticleSystemSceneNode addSnowParticleSceneNode(Vector3d pos, Vector3d dir, double size, double speed, int rate,
			SceneNode parent){
		ParticleSystemSceneNode node = new ParticleSystemSceneNode(pos, parent);
		if(nativeAddSnowParticleSceneNode(pos.X, pos.Y, pos.Z,
				size, rate, dir.X, dir.Y, dir.Z, getId(node), getId(parent),mEnableLighting)!=0){
			return null;
		}
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加烟雾粒子系统节点
	 * @param pos 位置
	 * @param dir 方向
	 * @param size 大小
	 * @param speed 速度
	 * @param rate 粒子产生速率
	 * @param parent 父节点
	 * @return 创建的对象
	 */
	public ParticleSystemSceneNode addSmokeParticleSceneNode(Vector3d pos, Vector3d dir, double size, double speed, int rate,
			SceneNode parent){
		ParticleSystemSceneNode node = new ParticleSystemSceneNode(pos, parent);
		if(nativeAddSmokeParticleSceneNode(pos.X, pos.Y, pos.Z,
				size, rate, dir.X, dir.Y, dir.Z, getId(node), getId(parent),mEnableLighting)!=0){
			return null;
		}
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加9个立方体组成的布局类，assets本地读取被禁用时不可用
	 * @param pos 位置
	 * @param size 大小
	 * @param dx x方向的缝隙宽度
	 * @param dy y方向的缝隙宽度
	 * @param parent 父节点
	 * @return 返回所添加的9个立方体组成的布局类
	 */
	public NineCubeLayout addNineCubeLayoutSceneNode(Vector3d pos,
			Vector3d size, double dx, double dy, SceneNode parent){
		if (mEngine.isNativeAssetsReaderEnabled() == false){
			Log.e(TAG, "assets is not allowed to be opened!");
			return null;
		}
		NineCubeLayout res = new NineCubeLayout(pos, size, dx, dy, parent);
		registerNode(res);
		return res;
	}
	
	/**
	 * 添加天空盒节点
	 * @param top 顶部材质
	 * @param bottom 底部材质
	 * @param left 左方材质
	 * @param right 右方材质
	 * @param front 前方材质
	 * @param back 后方材质
	 * @param parent 父节点
	 * @return 返回新添加的节点
	 */
	public MeshSceneNode addSkyBoxSceneNode(String top, String bottom, String left, String right,
			String front, String back, SceneNode parent){
		MeshSceneNode node = new MeshSceneNode(new Vector3d(), parent);
		if(nativeAddSkyBoxSceneNode(getFullPath(top), getFullPath(bottom), getFullPath(left), 
				getFullPath(right), getFullPath(front), getFullPath(back),
				getId(node.getParent()), node.getId()) != 0){
			return null;
		}
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加穹顶节点
	 * @param tex 穹顶材质
	 * @param hr 水平方向的球的分段数
	 * @param vr 垂直方向的球的分段数
	 * @param tp 在垂直方向，材质需要显示多少高度，取值0~1
	 * @param sp 穹顶球的绘制比例，取值0~2；为1时，正好绘制半球
	 * @param radius 球的半径
	 * @param parent 父节点
	 * @return 返回新添加的节点
	 */
	public MeshSceneNode addSkyDomeSceneNode(String tex, int hr, int vr,
			double tp, double sp, double radius, SceneNode parent){
		MeshSceneNode node = new MeshSceneNode(new Vector3d(), parent);
		if(nativeAddSkyDomeSceneNode(getFullPath(tex), hr, vr, tp, sp, radius,
				getId(node.getParent()), node.getId()) != 0){
			return null;
		}
		registerNode(node);
		return node;
	}
	
	/**
	 * 添加地形节点
	 * @param heightmap 高度图
	 * @param color 节点颜色（如果应用了贴图，则颜色设置失效）
	 * @param smoothFactor 平滑次数
	 * @param parent 父节点
	 * @return 添加的节点
	 */
	public MeshSceneNode addTerrainSceneNode(String heightmap, Color4i color, int smoothFactor, SceneNode parent){
		MeshSceneNode node = new MeshSceneNode(new Vector3d(), parent);
		if(nativeAddTerrainSceneNode(getFullPath(heightmap), color, smoothFactor,
				getId(node.getParent()), node.getId(), mEnableLighting) != 0){
			return null;
		}
		registerNode(node);
		return node;
	}
	
	/**
	 * 仅支持OpenGL ES 1.0，向引擎申请一个外部纹理的存储空间，并为之取名，名字必须是唯
	 * 一的。该方法会返回一个opengGL ES Id号，用户可以保存这个Id号将openGL ES与视频
	 * 解码器，相机等流媒体连接起来。<br>
	 * 注意：这个材质是GL_TEXTURE_EXTERNAL_OES类，而非GL_TEXTURE_2D。
	 * @param texname 名字，使用者必须将这个名字存储起来，因为它是指向这个材质的唯一标识
	 * @return 纹理的opengGL ES Id号，若为正整数，则表示申请成功。
	 */
	public int applyNewExternalTexture(String texname){
		if (mEngine.getRenderType() != 1){
			Log.e(TAG, "Can not apply new external texture. Unsupported renderer type.");
			return -1;
		}
		return nativeApplyNewExternalTex(texname);
	}
	
	/**
	 * 将bitmap上传至引擎
	 * @param bitmap 位图
	 * @param texname 标识符
	 * @return 如果上传失败（比如存在重名，位图不可读取等）返回false
	 */
	public boolean uploadBitmap(Bitmap bitmap, String texname){
		return nativeUploadBitmap(texname, bitmap);
	}
	
	@Deprecated
	/**
	 * 返回添视频播放器。
	 * @return 视频播放器对象
	 */
	public TexMediaPlayer getMediaPlayer(){
		if (mEngine.getRenderType() != 1){
			Log.e(TAG, "Can not getMediaPlayer. Unsupported renderer type.");
			return null;
		}
		if (mMediaPlayer == null){
			mMediaPlayer = new TexMediaPlayer(this, nativeGetMediaTextureId());
		}
		return mMediaPlayer;
	}
	
	/**
	 * 删除指定节点。
	 * @param node 所要删除的节点对象
	 * @return 是否成功删除
	 */
	public boolean removeNode(SceneNode node){
		if (node == null) return false;
		SceneNode pa = node.getParent();
		if (pa != null) pa.removeChild2(node);
		unregisterNode(node);
		nativeRemoveNode(node.getId());
		return true;
	}
	
	/**
	 * 删除所有节点，<b>包括</b>相机节点
	 */
	public void clearAllNodes(){
		mNodeList.clear();
		mUpdateList.clear();
		mCameraList.clear();
		mActiveCamera = null;
		nativeClear();
		_NewId = 0;
	}
	
	/**
	 * 这是个不安全的方法，程序员<b>必须</b>确保移除的贴图并不被当前场景
	 * 所使用，否则程序将在渲染时崩溃。然而，因为贴图会占据大量的图像
	 * 缓存和内存（如果在内存中留有备份的话，目前的设置下是没有备份的），
	 * 所以及时的清理贴图是减少系统资源消耗的有效方法。然而，反复加载
	 * 贴图是非常耗时的，因此引擎不会自动删除那些不再被使用的贴图。
	 * 建议删除那些不会再用到的贴图。
	 * @param path 贴图的路径（必须跟创建时所用的路径一致）
	 */
	public void removeTexture(String path){
		nativeRemoveTexture(getFullPath(path));
	}
	
	/**
	 * 这是个不安全的方法，程序员<b>必须</b>确保移除的贴图并不被当前场景
	 * 所使用，否则程序将在渲染时崩溃。然而，因为贴图会占据大量的图像
	 * 缓存和内存（如果在内存中留有备份的话，目前的设置下是没有备份的），
	 * 所以及时的清理贴图是减少系统资源消耗的有效方法。然而，反复加载
	 * 贴图是非常耗时的，因此引擎不会自动删除那些不再被使用的贴图。
	 * 建议删除那些不会再用到的贴图。
	 * @param bitmapName 贴图的名称（必须跟创建时所用的名称一致，无需前缀）
	 */
	public void removeBitmap(String bitmapName){
		nativeRemoveTexture(bitmapName);
	}
	
	/**
	 * 去除目前不被使用的多边形。出于性能考虑，引擎会缓存所有已经被加载
	 * 的多边形模型。这些模型会一直驻留在内存中，即使使用它们的节点已经
	 * 不存在。这个方法用于清除内存中不被使用的多边形模型。
	 */
	public void removeUnusedMesh(){
		nativeRemoveUnusedMesh();
	}
	
	/**
	 * 在缓存中去除指定的多边形模型。这个函数仅仅在缓存中去除节点，并不影响已
	 * 经在节点中被引用的多边形模型的渲染（就是说不会导致空指针的错误）。然而，
	 * 由于在这种情况下，引擎仅仅是取消了该多边形模型在缓存中的指针，而非释放
	 * 内存，由此会导致两个后果：如果再次使用到该模型，引擎会认为该模型未被加
	 * 载而创建该模型的另一个备份，由此会导致内存的浪费；另一个较为有利的后果
	 * 是，如果引用该模型的节点全部被删除，那么引擎将自动释放模型所占据的空间
	 * （而不是如默认的情况下让模型驻留在缓存中供下次查询和调用）。<br>
	 * 反之，如果该方法被调用时，没有节点引用了该模型，则模型所占用的内存将会
	 * 被释放。<br>
	 * 综上，我们并不建议提前从缓存中清除模型，即使这种行为通常不会导致错误。<br>
	 * 多边形模型管理与贴图管理的区别主要是由引擎native实现的不同导致的。
	 * @param path 模型的路径
	 */
	public void removeMesh(String path){
		nativeRemoveMesh(getFullPath(path));
	}
	
	/**
	 * 更改渲染区域尺寸。这个方法不应该被用户所调用
	 * @param width 渲染区域的宽度值，单位为像素
	 * @param height 渲染区域高度值，单位为像素
	 */
	public void onResize(int width, int height){
		mWidth = width;
		mHeight = height;
		for (CameraSceneNode itr:mCameraList){
			itr.setAspectRatio((double)mWidth/mHeight);
		}
			
	}
	
	/**
	 * 帧绘制函数，更新公告板组和相机位置。这个方法不应该被用户所调用
	 */
	public void onDrawFrame(){
		for (SceneNode itr: mNodeList){
			itr.onAnimate();
		}
		for (Updatable itr:mUpdateList){
			itr.updateFromCamera(getActiveCamera());
		}
		getActiveCamera().resetPosChangedFlag();
	}
	
	/**
	 * 这个方法不应该被用户所调用
	 */
	public void reset(){
		clearAllNodes();
		
		if (mMediaPlayer != null){
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		_NewId = 0;
		
		SceneNode.setScene(this);
		addCameraSceneNode(
				new Vector3d(0, 0, 0), 
				new Vector3d(0, 0, 100), true, null);
		
		if (mEngine.isNativeAssetsReaderEnabled()){
			setFont(Engine.SYSTEM_MEDIA+"buildinfont.png");
		}
		mEnableLighting = false;
	}
	
	/**
	 * 设置材质所在文件夹的路径，这个方法不应该被用户所调用
	 * @param path 材质文件夹路径
	 */
	public void setResourceDir(String path){
		mResourceDir = path;
	}

	/**
	 * 返回材质模型所在文件夹的路径，这个方法不应该被用户所调用
	 * @return 材质模型所在文件夹的路径。
	 */
	public String getResourceDir(){
		return mResourceDir;
	}

	/**
	 * 这个方法不应该被用户所调用
	 */
	public static Scene getInstance(Engine engine){
		if (_UniInstance == null){
			_UniInstance = new Scene(engine);
		}else {
			_UniInstance.mEngine = engine;
		}
		return _UniInstance;	
	}

	/**
	 * 这个方法不应该被用户所调用
	 */
	public static void release(){
		
	}

	/**
	 * 这个方法不应该被用户所调用
	 */
	public String getFullPath(String path){
		
		if (path == null) return null;
		if (path.equals("")) return "";
		
		if (path.length() > Engine.ASSETS_MARK.length() &&
				path.substring(0, Engine.ASSETS_MARK.length()).equals(
				Engine.ASSETS_MARK)){
			if (mEngine.isNativeAssetsReaderEnabled() == false){
				Log.e(TAG, "assets is not allowed to be opened!");
				return null;
			}
			return path.substring(Engine.ASSETS_MARK.length());
		}
		
		if (path.length() > Engine.BITMAP_MARK.length() &&
				path.substring(0, Engine.BITMAP_MARK.length()).equals(
				Engine.BITMAP_MARK)){
			return path.substring(Engine.ASSETS_MARK.length());
		}
		
		if (path.length() > Engine.EXTERNAL_TEX_MARK.length() &&
				path.substring(0, Engine.EXTERNAL_TEX_MARK.length()).equals(
				Engine.EXTERNAL_TEX_MARK)){
			return path.substring(Engine.EXTERNAL_TEX_MARK.length());
		}
		
		if (Utils.isAbsolutePath(path)){
			return path; 
		} else {
			return getResourceDir() + path;
		}
	}

	/**
	 * 将指定节点添加到节点列表。
	 * @param node 所要添加的节点对象
	 */
	//this method will *NOT* automatically register node in native engine
	//thus, it should not be used alone
	void registerNode(SceneNode node){
		mNodeList.add(node);
		if (SceneNode.TYPE_CAMERA == node.getNodeType()){
			mCameraList.add((CameraSceneNode)node);
		}
	}
	
	/**
	 * 将指定节点从节点列表中移除。
	 * 若所要移除的节点在节点列表中，则将其移除返回true，否则返回false。
	 * @param node 所要移除的节点对象。
	 * @return 删除成功时返回true，所删除节点不存在时返回false
	 */
	//this method will *NOT* automatically unregister node in native engine
	//thus, it should not be used alone
	boolean unregisterNode(SceneNode node){
		for (SceneNode itr:mNodeList){
			if (getId(itr) == getId(node)){
				mNodeList.remove(itr);
				if (SceneNode.TYPE_CAMERA == node.getNodeType()){
					mCameraList.remove(node);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 更新当前最新的节点ID值，返回最新的节点ID。
	 * @return 当前最新的节点ID值
	 */
	int getNewId(){
		return ++_NewId;
	}
	
	/**
	 * 这个方法不应该被用户所调用
	 */
	static Scene getInstance(){
		if (_UniInstance == null || _UniInstance.mEngine == null)
			return null;
		
		return _UniInstance;
	}
	
	private static Scene _UniInstance;
	private static int _NewId;
	
	private Engine mEngine;
	private CameraSceneNode mActiveCamera;
	private ArrayList<SceneNode> mNodeList;
	private int mWidth, mHeight;
	private boolean mEnableLighting;
	private String mResourceDir;
	private TexMediaPlayer mMediaPlayer;
	private ArrayList<Updatable> mUpdateList;
	private ArrayList<CameraSceneNode> mCameraList;
	
	private Scene(Engine engine){
		mEngine = engine;
		mNodeList = new ArrayList<SceneNode>();
		mUpdateList = new ArrayList<Updatable>();
		mCameraList = new ArrayList<CameraSceneNode>();
	}
	
	private native void nativeSetClearColor(int r, int g, int b, int a);
    private native void nativeSetAmbientLight(int r, int g, int b, int a);
    private native void nativeSetActiveCamera(int id);
    private native int nativeGetTouchedSceneNode(float x, float y, int root);
    
    //native draw API
	private native int nativeDrawImage(
			String path, Rect4i des, double left, double up, 
			double width, double height, boolean alpha);
	
	private native int nativeDrawBitmap(
			String name, Bitmap bit, Rect4i des, double left, double up, 
			double width, double height, boolean alpha);
	
	private native void nativeDrawRectangle(
			int left, int up, int width, int height, 
			int r, int g, int b,int a);
	
	private native void nativeDrawRectangleChrome(Rect4i rect,
			Color4i LT, Color4i LB, Color4i RB, Color4i RT);
	
	private native void nativeDrawText(
			String text, int left, int up,
			int r, int g, int b, int a);
	
    private native void nativeSmgrDrawAll();

    
    //native add node API
	private native int nativeAddEmptySceneNode(
			double x, double y, double z, int id, int parent, boolean isLight);
	
	private native int nativeAddCubeSceneNode(
			double x, double y, double z, 
			double size, int id, int parent, boolean isLight);
	private native int nativeAddSphereSceneNode(
			double x, double y, double z, double radius,
			int polyCount, int id, int parent, boolean isLight);
	
	private native int nativeAddMeshSceneNode(
			String path, double x, double y, double z, 
			int id, int parent, boolean isLight, boolean optimizedByOctree);
	
	private native int nativeAddTextNode(
			String text, double x, double y, double z, 
			double size, int id, int parent, boolean isLight);

	private native int nativeAddCameraSceneNode(
			double px, double py, double pz, 
			double lx, double ly, double lz, 
			boolean isActive, int id, int parent, boolean isLight);
	
	private native int nativeAddBillboardSceneNode(
			double px, double py, double pz, 
			double sx, double sy, int id, int parent, boolean isLight);
	
	private native int nativeAddLightSceneNode(
			double px, double py, double pz, double radius,
			int r, int g, int b, int id, int parent, boolean isLight);
	
	private native int nativeAddAnimateMeshSceneNode(
			String path, double x, double y, double z,
			int id, int parent, boolean isLight);
	
	private native int nativeAddParticleSystemSceneNode(
			double x, double y, double z, boolean withDefaultEmitter, 
			int id, int parent, boolean isLight);
	
	private native int nativeAddCometTailSceneNode(
			double x, double y, double z, double radius, double length, 
			double dirx, double diry, double dirz,
			int id, int parent, boolean isLight);
	
	private native int nativeAddStarsParticleSceneNode(
			double x, double y, double z, double radius,
			int id, int parent, boolean isLight);
	private native int nativeAddExplosionParticleSceneNode(
			double x, double y, double z, double radius,
			double speed, int id, int parent, boolean isLight);
	private native int nativeAddSnowParticleSceneNode(
			double x, double y, double z, double maxSize, int snowrate, 
			double dx, double dy, double dz, int id, int parent, boolean isLight);
	private native int nativeAddSmokeParticleSceneNode(
			double x, double y, double z, double maxSize, int smokerate, 
			double dx, double dy, double dz, int id, int parent, boolean isLight);
	
	
	private native int nativeAddSkyBoxSceneNode(String top, String bottom, String left, String right,
			String front, String back, int parent, int id);
	private native int nativeAddSkyDomeSceneNode(String tex, int hr, int vr,
			double tp, double sp, double radius, int parent, int id);
	private native int nativeAddTerrainSceneNode(String heightmap, Color4i color, int smooth, int parent, int id, boolean light);	
	
	private native int nativeAddRectSceneNode(String tex, double x, double y, double z, double w, double h,
			int id, int parent, boolean isLight);
	
	//native remove node
	private native void nativeRemoveNode(int id);
	private native void nativeClear();
	private native void nativeSetFontPath(String path);
	private native int nativeGetMediaTextureId();
	private native int nativeApplyNewExternalTex(String name);
	private native boolean nativeUploadBitmap(String name, Bitmap bitmap);
	private native void nativeRemoveTexture(String name);
	private native void nativeRemoveUnusedMesh();
	private native void nativeRemoveMesh(String path);
}
