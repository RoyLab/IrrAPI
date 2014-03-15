package zte.irrlib.scene;

import java.util.ArrayList;

import zte.irrlib.Engine;
import zte.irrlib.Utils;
import zte.irrlib.core.Color3i;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Rect4i;
import zte.irrlib.core.Vector2d;
import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;

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
	 * 设置是否打开光照。
	 * @param flag 值为true时光照打开，否则关闭
	 */
	public void enableLighting(boolean flag){
		mEnableLighting = flag;
	}
	
	/**
	 * 设置字体路径。
	 * @param path 字体图片文件所在路径
	 */
	public void setDefaultFontPath(String path){
		nativeSetFontPath(getFullPath(path));
	}
	
	/**
	 * 设置材质所在文件夹的路径。
	 * @param path 材质文件夹路径
	 */
	public void setResourceDir(String path){
		mResourceDir = path;
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
	 * 返回材质模型所在文件夹的路径。
	 * @return 材质模型所在文件夹的路径。
	 */
	public String getResourceDir(){
		return mResourceDir;
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
	 * @param leftUp 所要绘制的图片所在位置的左上点坐标值
	 * @param size 绘制区域大小值
	 */
	//to be extend, can be transparent one, can be only part of it.
	public void drawImage(String path, Vector2i leftUp, Vector2i size){
		nativeDrawImage(getFullPath(path), leftUp.X, leftUp.Y, size.X, size.Y);
	}
	
	/**
	 * 绘制2D矩形。
	 * @param leftUp 所要绘制的矩形所在位置的左上点坐标值
	 * @param size 所要绘制的矩形尺寸
	 * @param color 所要绘制的矩形的颜色
	 */
	//to be extend, can be multi-color one
	public void drawRectangle(Vector2i leftUp, Vector2i size, Color4i color){
		nativeDrawRectangle(leftUp.X, leftUp.Y, size.X, size.Y, color.r(), color.g(), color.b(), color.a());
	}
	
	/**
	 * 绘制文字。
	 * @param text 所要绘制的文字内容
	 * @param leftUp 所要绘制的文字所在位置的左上点坐标值
	 * @param color 所要绘制的文字的颜色
	 */
	//It is necessary to supply font choice?
	public void drawText(String text, Vector2i leftUp, Color4i color){
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
		SceneNode node = new SceneNode();
		if (nativeAddEmptySceneNode(pos.X, pos.Y, pos.Z, 
				getId(node), getId(parent), mEnableLighting) != 0)
			return null;
		
		node.javaLoadDataAndInit(pos, parent);
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
		MeshSceneNode node = new MeshSceneNode();
		if (nativeAddCubeSceneNode(pos.X, pos.Y, pos.Z, 
				size, getId(node), getId(parent), mEnableLighting) != 0)
			return null;
		
		node.javaLoadDataAndInit(pos, parent);
		return node;
	}
	
	/**
	 * 添加静态模型节点，返回所添加的模型节点对象。
	 * @param path 所用静态模型的路径
	 * @param pos 所添加节点的位置
	 * @param parent 所添加节点的父节点对象
	 * @return 所添加的节点对象
	 */
	public MeshSceneNode addMeshSceneNode(String path, Vector3d pos, SceneNode parent){
		MeshSceneNode node = new MeshSceneNode();
		if (nativeAddMeshSceneNode(getFullPath(path), pos.X, pos.Y, pos.Z,
				getId(node), getId(parent), mEnableLighting) != 0)
			return null;
		
		node.javaLoadDataAndInit(pos, parent);
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
		SceneNode node = new SceneNode();
		if (nativeAddTextNode(text, pos.X, pos.Y, pos.Z, size, 
				getId(node), getId(parent), mEnableLighting) != 0)
			return null;
		
		node.javaLoadDataAndInit(pos, parent);
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
		CameraSceneNode node = new CameraSceneNode();
		if (nativeAddCameraSceneNode(pos.X, pos.Y, pos.Z, 
				lookAt.X, lookAt.Y, lookAt.Z, isActive, 
				getId(node), getId(parent), mEnableLighting) != 0)
			return null;
		
		setActiveCamera(node);
		node.javaLoadDataAndInit(pos, lookAt, parent);
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
		BillboardSceneNode node = new BillboardSceneNode();
		if (nativeAddBillboardSceneNode(pos.X, pos.Y, pos.Z, 
				size.X, size.Y, getId(node), getId(parent), mEnableLighting) != 0){
			return null;
		}
		
		node.javaLoadDataAndInit(pos, parent);
		return node;
	}
	
	/**
	 * 添加光源节点，返回所添加的光源节点对象
	 * @param pos 所添加光源节点的位置
	 * @param radius 所添加光源节点的照射半径
	 * @param color 所添加光源节点的颜色
	 * @param parent 所添加光源节点的父节点对象
	 * @return 所添加的节点对象
	 */
	public LightSceneNode addLightSceneNode(Vector3d pos, double radius, Color3i color, SceneNode parent){
		LightSceneNode node = new LightSceneNode();
		if (nativeAddLightSceneNode(pos.X, pos.Y, pos.Z, radius,
				color.r(), color.g(), color.b(), 
				getId(node), getId(parent), mEnableLighting) != 0){
			return null;
		}
		
		node.javaLoadDataAndInit(pos, parent, radius);
		return node;
	}
	
	/**
	 * 添加公告板组节点，返回所添加的公告板组对象。
	 * @param pos 所添加节点的位置
	 * @param parent 所添加节点的父节点
	 * @return 所添加的公告板组节点
	 */
	public BillboardGroup addBillboardGroup(Vector3d pos, SceneNode parent){
		BillboardGroup node = new BillboardGroup();
		if (nativeAddEmptySceneNode(pos.X, pos.Y, pos.Z, 
				getId(node), getId(parent), mEnableLighting) != 0){
			return null;
		}
		node.javaLoadDataAndInit(pos, parent);
		mBBGroup.add(node);
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
		AnimateMeshSceneNode node = new AnimateMeshSceneNode();
		if (nativeAddAnimateMeshSceneNode(getFullPath(path), pos.X, pos.Y, pos.Z,
				getId(node), getId(parent), mEnableLighting) != 0){
			return null;
		}
		node.javaLoadDataAndInit(pos, parent);
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
		ParticleSystemSceneNode node = new ParticleSystemSceneNode();
		if (nativeAddParticleSystemSceneNode(pos.X, pos.Y, pos.Z, 
				withDefaultEmitter, getId(node), getId(parent), mEnableLighting) != 0){
			return null;
		}
		node.javaLoadDataAndInit(pos, parent);
		return node;
	}
	
	/**
	 * 返回添视频播放器。
	 * @return 视频播放器对象
	 */
	public TexMediaPlayer getMediaPlayer(){
		if (mMediaPlayer == null){
			mMediaPlayer = new TexMediaPlayer(this, nativeGetMediaTextureId());
		}
		return mMediaPlayer;
	}
	
	/**
	 * 删除指定节点。
	 * @param node 所要删除的节点对象
	 */
	public void removeNode(SceneNode node){
		unregisterNode(node);
		nativeRemoveNode(node.getId());
	}
	
	/**
	 * 删除所有节点。
	 */
	public void clearAllNodes(){
		mNodeList.clear();
		nativeClear();
		_NewId = 0;
	}
	
	/**
	 * 更改渲染区域尺寸。
	 * @param width 渲染区域的宽度值，单位为像素
	 * @param height 渲染区域高度值，单位为像素
	 */
	public void onResize(int width, int height){
		mWidth = width;
		mHeight = height;
	}
	
	/**
	 * 帧绘制函数，更新公告板组和相机位置。
	 */
	public void onDrawFrame(){
		for (BillboardGroup itr:mBBGroup){
			itr.updateVisible(getActiveCamera());
		}
		getActiveCamera().resetPosChangedFlag();
	}
	
	/**
	 * 清除存储在Java层的所有节点、公告板和视频播放器信息
	 * 同时初始化场景摄像机
	 */
	public void javaReset(){
		mNodeList.clear();
		mBBGroup.clear();
		
		if (mMediaPlayer != null){
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		_NewId = 0;
		
		SceneNode.setScene(this);
		addCameraSceneNode(
				new Vector3d(0, 0, 0), 
				new Vector3d(0, 0, 100), true, null);
	}
	
	/**
	 * 将指定节点添加到节点列表。
	 * @param node 所要添加的节点对象
	 */
	//this method will *NOT* automatically register node in native engine
	//thus, it should not be used alone
	void registerNode(SceneNode node){
		mNodeList.add(node);
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
	 * 为场景对象指定引擎，返回场景对象。
	 * @param engine 指定的引擎对象
	 * @return 场景对象
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
	 * 返回场景对象。
	 * @return 场景对象
	 */
	public static Scene getInstance(){
		if (_UniInstance == null || _UniInstance.mEngine == null)
			return null;
		
		return _UniInstance;
	}
	
	/**
	 * 根据给定相对路径返回其绝对路径值。
	 * @param path 给定的相对路径
	 * @return 给定相对路径返回其绝对路径值
	 */
	public String getFullPath(String path){
		if (Utils.isAbsolutePath(path)){
			return path; 
		} else {
			return getResourceDir() + path;
		}
	}
	
	private static Scene _UniInstance;
	private static int _NewId;
	
	private Engine mEngine;
	private CameraSceneNode mActiveCamera;
	private ArrayList<SceneNode> mNodeList;
	private int mWidth, mHeight;
	private boolean mEnableLighting = true;
	private String mResourceDir;
	private TexMediaPlayer mMediaPlayer;
	private ArrayList<BillboardGroup> mBBGroup;
	
	private Scene(Engine engine){
		mEngine = engine;
		mNodeList = new ArrayList<SceneNode>();
		mBBGroup = new ArrayList<BillboardGroup>();
	}
	
	private native void nativeSetClearColor(int r, int g, int b, int a);
    private native void nativeSetAmbientLight(int r, int g, int b, int a);
    private native void nativeSetActiveCamera(int id);
    private native int nativeGetTouchedSceneNode(float x, float y, int root);
    
    //native draw API
	private native void nativeDrawImage(
			String path, int left, int up, 
			int width, int height);
	
	private native void nativeDrawRectangle(
			int left, int up, int width, int height, 
			int r, int g, int b,int a);
	
	private native void nativeDrawRectangleChrome(Rect4i rect, Color4i[]color) ;
	
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
	
	private native int nativeAddMeshSceneNode(
			String path, double x, double y, double z, 
			int id, int parent, boolean isLight);
	
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
	
	//native remove node
	private native void nativeRemoveNode(int id);
	private native void nativeClear();
	private native void nativeSetFontPath(String path);
	private native int nativeGetMediaTextureId();
}
