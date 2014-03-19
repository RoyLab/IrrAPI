package zte.irrlib.scene;

import zte.irrlib.Engine;
import zte.irrlib.core.Vector3d;
import android.graphics.Bitmap;
import android.util.Log;

public class NineCubeLayout extends SceneNode implements Scene.Updatable{
	
	/** �����������Ĳ������*/
	public static final int FRONT_MATERIAL_ID = 3;
	public static final int BACK_MATERIAL_ID = 1;
	public static final int LEFT_MATERIAL_ID = 5;
	public static final int UP_MATERIAL_ID = 4;
	public static final int RIGHT_MATERIAL_ID = 2;
	public static final int BOTTOM_MATERIAL_ID = 0;
	
	public static final String TAG = "NineCubeLayout";

	NineCubeLayout(Vector3d pos, Vector3d size, double dx, double dy, SceneNode parent){
		
		super();
		
		mSize = new Vector3d(size);
		mX = dx;
		mY = dy;
		nativeCreateEmptySceneNode(getId(), mScene.isLightingEnabled());
		
		setPosition(pos, TRANS_ABSOLUTE);
		setParent(parent);
		mark();
		
		double x = (size.X - 2*mX)/3;
		double y = (size.Y - 2*mY)/3;
		double a = x + mX, b = y + mY;
		
		for (int i = 0; i < 9; i++){
			int u = i/3;
			int v = i%3;
					
			SceneNode node = mScene.addMeshSceneNode(
					Engine.SYSTEM_MEDIA_FULL + "ext_cube.obj", 
					new Vector3d(a*(-1+v), b*(-1+u), 0), false, this);
			node.setScale(new Vector3d(x/10, y/10, size.Z/10), TRANS_ABSOLUTE);
			node.mark();
		}
	}
	
	/**
	 * ָ��ĳ���ڵ������Ĳ��ʡ�
	 * @param tex ����·��
	 * @param index �ڵ����
	 */
	public void setFrontTexture(String tex, int index){
		if (mChild == null) return;
		((MeshSceneNode)mChild.get(index)).setTexture(tex, 3);
	}
	
	/**
	 * ָ��ĳ���ڵ������Ĳ��ʡ�
	 * @param bitmap λͼ
	 * @param name ���������ȡ��������Ψһ
	 * @param index �ڵ����
	 */
	public void setFrontTexture(Bitmap bitmap, String name, int index){
		if (mChild == null || bitmap == null || name == "") return;
		((MeshSceneNode)mChild.get(index)).setTexture(bitmap, name, FRONT_MATERIAL_ID);
	}

	/**
	 * Ϊ�����ӽڵ��ĳһ���渽��ͳһ�Ĳ���
	 * @param tex ����·��
	 * @param materialId ���ʺ�
	 */
	public void setUniTexture(final String tex, final int materialId){
		do2EveryChild(new TraversalCallback(){
			public void operate(SceneNode node){
				((MeshSceneNode)node).setTexture(tex, materialId);
			}
		});
	}
	
	/**
	 * Ϊ�����ӽڵ��ĳһ���渽��ͳһ�Ĳ���
	 * @param bit λͼ
	 * @param name ���������ȡ��������Ψһ
	 * @param materialId ���ʺ�
	 */
	public void setUniTexture(final Bitmap bit, final String name, final int materialId){
		do2EveryChild(new TraversalCallback(){
			public void operate(SceneNode node){
				((MeshSceneNode)node).setTexture(bit, name, materialId);
			}
		});
	}
	
	/**
	 * �����Ƿ�������ͼ��alphaͨ����ʵ��͸��Ч��
	 * @param flag
	 */
	public void enableTransparentTex(boolean flag){
		if (flag){
			do2EveryChild(new TraversalCallback(){
				public void operate(SceneNode node) {
					((MeshSceneNode)node).setMaterialType(
							MeshSceneNode.E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
				}
			});
		}
		else {
			do2EveryChild(new TraversalCallback(){
				public void operate(SceneNode node) {
					((MeshSceneNode)node).setMaterialType(
							MeshSceneNode.E_MATERIAL_TYPE.EMT_SOLID);
				}
			});
		}
	}
	
	//Scene.Updatable
	/**
	 * �򿪸��¿��أ�����ʹ�����еĽڵ㳯�������������ò��Ч�����Ǻܺá�
	 * �����е�û�У�����demoҲ�ɡ�
	 */
	public void enableUpdate(Scene sc, boolean flag) {
		if (flag){
			sc.regUpdatableObject(this);
		}
		else{
			sc.unregUpdatableObject(this);
			do2EveryChild(new SceneNode.TraversalCallback() {
				public void operate(SceneNode node) {
					node.setRotation(new Vector3d(0, 0, 0), SceneNode.TRANS_ABSOLUTE);
				}
			});
		}
	}

	//Scene.Updatable
	public void updateFromCamera(CameraSceneNode cam) {
		final Vector3d camPos = cam.getAbsolutePosition();
		do2EveryChild(new SceneNode.TraversalCallback() {
			public void operate(SceneNode node) {
				Vector3d nPos = node.getAbsolutePosition();
				Vector3d p2c = camPos.minus(nPos);
				double d = p2c.length();
				double a = Math.asin(p2c.Y/d)+((p2c.Z>0)?Math.PI:0);
				double b = Math.asin(-p2c.X/d/Math.cos(a))+((p2c.Z>0)?Math.PI:0);
				node.setRotation(new Vector3d(a*360/2/Math.PI, b*360/2/Math.PI, 0), SceneNode.TRANS_ABSOLUTE);
			}
		});
	}
	
	@Override @Deprecated
	public NineCubeLayout clone(){
		Log.e(TAG, "NineCubeLayout are not supported to clone.");
		return null;
	}
	
	protected Vector3d mSize;
	protected double mX, mY;
}
