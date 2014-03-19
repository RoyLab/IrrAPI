package zte.irrlib.scene;



/**
 * 公告板组节点类，管理一组公告板的位置，可见性等，同时可以设置
 * 组内公告版的可见的距离范围，可用于烟雾、植被等的模拟。
 * @author Fxx
 *
 */
public class BillboardGroup extends SceneNode implements Scene.Updatable{
	
	public static final int REMOVE_FROM_SCENE = 0x01;
	public static final int REMOVE_FROM_GROUP = 0x02;
	
	/**
	 * 添加指定公告板节点到公告板组。
	 * @param node 所添加的公告板对象
	 */
	public void add(BillboardSceneNode node){
		node.setParent(this);
		node.setVisible(true);
	}
	
	/**
	 * 从公告板组移除指定公告板节点
	 * @param node 所要移除的节点对象
	 * @param mode 移除的模式：
	 * 		模式为 REMOVE_FROM_SCENE 则从公告板组移除并从场景中删除该节点
	 * 		模式为 REMOVE_FROM_GROUP 则仅从公告板组移除，等同于{@link #removeChild(SceneNode)}
	 * @return 移除成功返回true，否则返回false
	 */
	public void remove(BillboardSceneNode node, int mode){
		if (node == null || node.getParent() != this)
		if (mode == REMOVE_FROM_SCENE){
			node.remove();
		}
		else {
			node.setParent(getParent());
		}
	}
	
	/**
	 * 将公告板中的节点全部移除
	 * @param mode 移除的模式：
	 * 		模式为 REMOVE_FROM_SCENE 则从公告板组移除并从场景中删除该节点
	 * 		模式为 REMOVE_FROM_GRO UP 则仅从公告板组移除，等同于{@link #removeChild(SceneNode)}
	 */
	public void removeAll(int mode){
		if (mode == REMOVE_FROM_SCENE){
			for (int i = 0; i < getChildrenCount(); i++)
				mChild.get(i).remove();
		}
		else {
			for (int i = 0; i < getChildrenCount(); i++)
				removeChild(mChild.get(i));
		}
	}
	
	/**
	 * 设置公告板的可视范围
	 * @param near 公告板可视区域近端的值
	 * @param far 公告板可视区域远端的值
	 */
	public void setVisibleDistance(double near, double far){
		mFar = far; mNear = near;
	}
	
	//Scene.Updatable
	public void updateFromCamera(CameraSceneNode camera){
		for (SceneNode itr:mChild){
			double disSquare = itr.getPosition().distanceSquare(camera.getPosition());
			
			if (disSquare < mNear*mNear || disSquare > mFar*mFar){
				itr.setVisible(false);
			}
			else itr.setVisible(true);
		}
	}
	
	//Scene.Updatable
	public void enableUpdate(Scene sc, boolean flag) {
		if (flag){
			sc.regUpdatableObject(this);
		}
		else{
			sc.unregUpdatableObject(this);
			do2EveryChild(new SceneNode.TraversalCallback() {
				public void operate(SceneNode node) {
					node.setVisible(true);
				}
			});
		}
	}

	/**
	 * 唯一构造函数。
	 */
	BillboardGroup(){
		super();
		mNodeType = TYPE_BILLBOARD_GROUP;
		mNear = 0.01;
		mFar = 1000;
	}
	
	@Override
	public BillboardGroup clone(){
		BillboardGroup res = softClone();
		cloneInNativeAndSetupNodesId(res);
		return res;
	}
	
	@Override
	protected BillboardGroup softClone(){
		BillboardGroup res = new BillboardGroup(this);
		softCopyChildren(res);
		return res;
	}
	
	protected BillboardGroup(BillboardGroup node){
		super(node);
		mNear = node.mNear;
		mFar = node.mFar;
	}

	private double mNear, mFar;
}
