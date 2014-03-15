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
 * �����࣬��zte.irrlib.scene���ڣ����е���Ĺ��췽�������ɼ�����ʹ��
 * ��Ӧ��add����({@link #addCubeSceneNode(Vector3d, double, SceneNode)}��)
 * ����get����({@link #getMediaPlayer()}��)��������ʵ����<br>
 * @author Fxx
 *
 */
public class Scene {
	
	/**
	 * Log Tag
	 */
	public static final String TAG = "Scene";
	
	/**
	 * �����Ƿ�򿪹��ա�
	 * @param flag ֵΪtrueʱ���մ򿪣�����ر�
	 */
	public void enableLighting(boolean flag){
		mEnableLighting = flag;
	}
	
	/**
	 * ��������·����
	 * @param path ����ͼƬ�ļ�����·��
	 */
	public void setDefaultFontPath(String path){
		nativeSetFontPath(getFullPath(path));
	}
	
	/**
	 * ���ò��������ļ��е�·����
	 * @param path �����ļ���·��
	 */
	public void setResourceDir(String path){
		mResourceDir = path;
	}

	/**
	 * ������Ҫʹ�õ��������
	 * @param camera ��Ҫʹ�õ����������
	 */
	public void setActiveCamera(CameraSceneNode camera){
		mActiveCamera = camera;
		nativeSetActiveCamera(getId(camera));
	}
	
	/**
	 * ���ñ�����ɫ��
	 * @param color ������ɫ����
	 */
	public void setClearColor(Color4i color){
		nativeSetClearColor(color.r(), color.g(), color.b(), color.a());
	}
	
	/**
	 * ���û�������ɫ��
	 * @param color ��������ɫ
	 */
	public void setAmbientLight(Color4i color){
		nativeSetAmbientLight(color.r(), color.g(), color.b(), color.a());
	}
	
	/**
	 * ���ز���ģ�������ļ��е�·����
	 * @return ����ģ�������ļ��е�·����
	 */
	public String getResourceDir(){
		return mResourceDir;
	}
	
	/**
	 * ���ص�ǰʹ�õ����������
	 * @return ��ǰʹ�õ����������
	 */
	public CameraSceneNode getActiveCamera(){
		return mActiveCamera;
	}
	
	/**
	 * ����ָ�����ڵ����ӽڵ��б�ָ�����߻��еĽڵ���󡣸����ߴ������������ָ����������꣨x��y����
	 * @param x ����ָ��ĵ��x������ֵ������ֵ�����豸��ʾ���ķֱ��ʾ���
	 * @param y ����ָ��ĵ��y������ֵ������ֵ�����豸��ʾ���ķֱ��ʾ���
	 * @param root ��ײ�����ʼ�ĸ��ڵ����
	 * @return ָ�����ڵ����ӽڵ��б�ָ�����߻��еĽڵ����
	 */
	public SceneNode getTouchedSceneNode(float x, float y, SceneNode root){
		return queryById(nativeGetTouchedSceneNode(x, y, getId(root)));
	}
	
	/**
	 * ������Ⱦ����ĳߴ硣�ߴ�ֵ�����豸��ʾ���ֱ��ʾ�����
	 * @return ��Ⱦ����ĳߴ硣
	 */
	public Vector2i getRenderSize(){
		return new Vector2i(mWidth, mHeight);
	}
	
	/**
	 * ����ָ��IDֵ��Ӧ�Ľڵ����
	 * @param id ��Ҫ���ҵĽڵ�IDֵ
	 * @return ָ��IDֵ��Ӧ�Ľڵ����
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
	 * ����ָ���ڵ��IDֵ��
	 * @param node ��Ҫ���ҵĽڵ����
	 * @return ָ���ڵ��IDֵ��
	 */
	public int getId(SceneNode node){
		if (node == null){
			return 0;
		}
		else return node.getId();
	}
	
	/**
	 * ����2Dͼ��
	 * @param path ��Ҫ���Ƶ�ͼƬ����·��
	 * @param leftUp ��Ҫ���Ƶ�ͼƬ����λ�õ����ϵ�����ֵ
	 * @param size ���������Сֵ
	 */
	//to be extend, can be transparent one, can be only part of it.
	public void drawImage(String path, Vector2i leftUp, Vector2i size){
		nativeDrawImage(getFullPath(path), leftUp.X, leftUp.Y, size.X, size.Y);
	}
	
	/**
	 * ����2D���Ρ�
	 * @param leftUp ��Ҫ���Ƶľ�������λ�õ����ϵ�����ֵ
	 * @param size ��Ҫ���Ƶľ��γߴ�
	 * @param color ��Ҫ���Ƶľ��ε���ɫ
	 */
	//to be extend, can be multi-color one
	public void drawRectangle(Vector2i leftUp, Vector2i size, Color4i color){
		nativeDrawRectangle(leftUp.X, leftUp.Y, size.X, size.Y, color.r(), color.g(), color.b(), color.a());
	}
	
	/**
	 * �������֡�
	 * @param text ��Ҫ���Ƶ���������
	 * @param leftUp ��Ҫ���Ƶ���������λ�õ����ϵ�����ֵ
	 * @param color ��Ҫ���Ƶ����ֵ���ɫ
	 */
	//It is necessary to supply font choice?
	public void drawText(String text, Vector2i leftUp, Color4i color){
		nativeDrawText(text, leftUp.X, leftUp.Y, color.r(), color.g(), color.b(), color.a());
	}
	
	/**
	 * �������нڵ㣬ͨ���ú���ʵ�ֶ�һ֡���������нڵ������Ⱦ��
	 */
	public void drawAllNodes(){
		nativeSmgrDrawAll();
	}
	
	/**
	 * ��ӿսڵ㡣��������ӵĽڵ����ע�⣺�սڵ�û�д�С��
	 * @param pos ����ӿսڵ�����λ��
	 * @param parent ����ӿսڵ�ĸ��ڵ����
	 * @return ����ӵĽڵ����
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
	 * ���������ڵ㣬��������ӵĽڵ����
	 * @param pos �����������ڵ�����λ��
	 * @param size �����������ڵ�ĳߴ磬����������ά�ȴ�Сһ�¡�
	 * @param parent �����������ڵ�ĸ��ڵ����
	 * @return ����ӵĽڵ����
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
	 * ��Ӿ�̬ģ�ͽڵ㣬��������ӵ�ģ�ͽڵ����
	 * @param path ���þ�̬ģ�͵�·��
	 * @param pos ����ӽڵ��λ��
	 * @param parent ����ӽڵ�ĸ��ڵ����
	 * @return ����ӵĽڵ����
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
	 * ������ֽڵ㣬��������ӵ����ֽڵ����
	 * @param text ����ӵ���������
	 * @param pos ��������ֽڵ��λ��
	 * @param size ��������ֽڵ�Ĵ�С
	 * @param parent ��������ֽڵ�ĸ��ڵ����
	 * @return ����ӵĽڵ����
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
	 * �������ڵ㣬��������ӵ�����ڵ����ꡣ
	 * @param pos ���������ڵ��λ��
	 * @param lookAt ���������ڵ�ĳ�������
	 * @param isActive �Ƿ񼤻������ڵ㣬ֵΪtrueʱ����
	 * @param parent ���������ڵ�ĸ��ڵ����
	 * @return ����ӵĽڵ����
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
	 * ��ӹ����ڵ㣬��������ӹ����ڵ����
	 * @param pos ����ӹ����ڵ�����λ��
	 * @param size ����ӹ����ڵ�Ķ�ά�ߴ�
	 * @param parent ����ӹ����ڵ�ĸ��ڵ����
	 * @return ����ӵĽڵ����
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
	 * ��ӹ�Դ�ڵ㣬��������ӵĹ�Դ�ڵ����
	 * @param pos ����ӹ�Դ�ڵ��λ��
	 * @param radius ����ӹ�Դ�ڵ������뾶
	 * @param color ����ӹ�Դ�ڵ����ɫ
	 * @param parent ����ӹ�Դ�ڵ�ĸ��ڵ����
	 * @return ����ӵĽڵ����
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
	 * ��ӹ������ڵ㣬��������ӵĹ���������
	 * @param pos ����ӽڵ��λ��
	 * @param parent ����ӽڵ�ĸ��ڵ�
	 * @return ����ӵĹ������ڵ�
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
	 * ��Ӷ���ģ�ͽڵ㣬��������ӵĽڵ����
	 * @param path ��ʹ�ö���ģ���ļ�����·��
	 * @param pos �����ģ�ͽڵ��λ��
	 * @param parent �����ģ�ͽڵ�ĸ��ڵ�
	 * @return ����ӵĽڵ����
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
	 * ������ӷ���ڵ㣬��������ӵ����ӷ������
	 * @param pos ����ӽڵ��λ��
	 * @param withDefaultEmitter ָ���Ƿ�ʹ��Ĭ�ϵķ�����
	 * @param parent ����ӽڵ�ĸ��ڵ�
	 * @return ����ӵĽڵ����
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
	 * ��������Ƶ��������
	 * @return ��Ƶ����������
	 */
	public TexMediaPlayer getMediaPlayer(){
		if (mMediaPlayer == null){
			mMediaPlayer = new TexMediaPlayer(this, nativeGetMediaTextureId());
		}
		return mMediaPlayer;
	}
	
	/**
	 * ɾ��ָ���ڵ㡣
	 * @param node ��Ҫɾ���Ľڵ����
	 */
	public void removeNode(SceneNode node){
		unregisterNode(node);
		nativeRemoveNode(node.getId());
	}
	
	/**
	 * ɾ�����нڵ㡣
	 */
	public void clearAllNodes(){
		mNodeList.clear();
		nativeClear();
		_NewId = 0;
	}
	
	/**
	 * ������Ⱦ����ߴ硣
	 * @param width ��Ⱦ����Ŀ��ֵ����λΪ����
	 * @param height ��Ⱦ����߶�ֵ����λΪ����
	 */
	public void onResize(int width, int height){
		mWidth = width;
		mHeight = height;
	}
	
	/**
	 * ֡���ƺ��������¹����������λ�á�
	 */
	public void onDrawFrame(){
		for (BillboardGroup itr:mBBGroup){
			itr.updateVisible(getActiveCamera());
		}
		getActiveCamera().resetPosChangedFlag();
	}
	
	/**
	 * ����洢��Java������нڵ㡢��������Ƶ��������Ϣ
	 * ͬʱ��ʼ�����������
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
	 * ��ָ���ڵ���ӵ��ڵ��б�
	 * @param node ��Ҫ��ӵĽڵ����
	 */
	//this method will *NOT* automatically register node in native engine
	//thus, it should not be used alone
	void registerNode(SceneNode node){
		mNodeList.add(node);
	}
	
	/**
	 * ��ָ���ڵ�ӽڵ��б����Ƴ���
	 * ����Ҫ�Ƴ��Ľڵ��ڽڵ��б��У������Ƴ�����true�����򷵻�false��
	 * @param node ��Ҫ�Ƴ��Ľڵ����
	 * @return ɾ���ɹ�ʱ����true����ɾ���ڵ㲻����ʱ����false
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
	 * ���µ�ǰ���µĽڵ�IDֵ���������µĽڵ�ID��
	 * @return ��ǰ���µĽڵ�IDֵ
	 */
	int getNewId(){
		return ++_NewId;
	}
	
	/**
	 * Ϊ��������ָ�����棬���س�������
	 * @param engine ָ�����������
	 * @return ��������
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
	 * ���������Ӧ�ñ��û�������
	 */
	public static void release(){
		
	}
	
	/**
	 * ���س�������
	 * @return ��������
	 */
	public static Scene getInstance(){
		if (_UniInstance == null || _UniInstance.mEngine == null)
			return null;
		
		return _UniInstance;
	}
	
	/**
	 * ���ݸ������·�����������·��ֵ��
	 * @param path ���������·��
	 * @return �������·�����������·��ֵ
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
