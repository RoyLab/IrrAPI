package zte.irrlib.scene;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 公告板组节点类，管理一组公告板的位置，可见性等，同时可以设置
 * 组内公告版的可见的距离范围，可用于烟雾、植被等的模拟。
 * @author Fxx
 *
 */
public class BillboardGroup extends SceneNode{
	
	public static final int REMOVE_FROM_SCENE = 0x01;
	public static final int REMOVE_FROM_GROUP = 0x02;
	
	/**
	 * 添加指定公告板节点到公告板组。
	 * @param node 所添加的公告板对象
	 */
	public void add(BillboardSceneNode node){
		node.setParent(this);
		node.setVisible(true);
		mGroup.add(node);
	}
	
	/**
	 * 从公告板组移除指定公告板节点
	 * @param node 所要移除的节点对象
	 * @param mode 移除的模式：
	 * 		模式为 REMOVE_FROM_SCENE 则从公告板组移除并从场景中删除该节点
	 * 		模式为 REMOVE_FROM_GROUP 则仅从公告板组移除
	 * @return 移除成功返回true，否则返回false
	 */
	public boolean remove(BillboardSceneNode node, int mode){
		if (mode == REMOVE_FROM_SCENE){
			node.remove();
		}
		node.setParent(null);
		return mGroup.remove(node);
	}
	
	/**
	 * 将公告板中的节点全部移除
	 * @param mode 移除的模式：
	 * 		模式为 REMOVE_FROM_SCENE 则从公告板组移除并从场景中删除该节点
	 * 		模式为 REMOVE_FROM_GROUP 则仅从公告板组移除
	 */
	public void removeAll(int mode){
		Iterator<BillboardSceneNode> itr = mGroup.iterator();
		BillboardSceneNode tmp;
		while (itr.hasNext()){
			tmp = itr.next();
			tmp.setParent(null);
			if (mode == REMOVE_FROM_SCENE){
				tmp.remove();
			}
		}
		mGroup.removeAll(mGroup);
	}
	
	/**
	 * 设置公告板的可视范围
	 * @param near 公告板可视区域近端的值
	 * @param far 公告板可视区域远端的值
	 */
	public void setVisibleDistance(double near, double far){
		mFar = far; mNear = near;
	}
	
	/**
	 * 依照给定相机对象对公告板组内节点是否可视进行更新
	 * @param camera 更新依据的相机对象
	 */
	public void updateVisible(CameraSceneNode camera){
		if ((mLastCamera == camera) && (!camera.isPositionChanged())) return;
		
		for (BillboardSceneNode itr:mGroup){
			double disSquare = itr.getPosition().distanceSquare(camera.getPosition());
			
			if (disSquare < mNear*mNear || disSquare > mFar*mFar){
				itr.setVisible(false);
			}
			else itr.setVisible(true);
		}
		mLastCamera = camera;
	}
	
	/**
	 * 唯一构造函数。
	 */
	BillboardGroup(){
		super();
		mNodeType = TYPE_BILLBOARD_GROUP;
		mGroup = new ArrayList<BillboardSceneNode>();
		mNear = 0.01;
		mFar = 1000;
	}

	private ArrayList<BillboardSceneNode> mGroup;
	private double mNear, mFar;
	private CameraSceneNode mLastCamera;
}
