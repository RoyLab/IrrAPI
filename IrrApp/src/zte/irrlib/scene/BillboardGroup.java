package zte.irrlib.scene;

import java.util.ArrayList;
import java.util.Iterator;


public class BillboardGroup extends SceneNode{
	
	public static final int REMOVE_FROM_SCENE = 0x01;
	public static final int REMOVE_FROM_GROUP = 0x02;
	
	BillboardGroup(){
		super();
		mNodeType = TYPE_BILLBOARD_GROUP;
		mGroup = new ArrayList<BillboardSceneNode>();
		mNear = 0.01;
		mFar = 1000;
	}
	
	public void add(BillboardSceneNode node){
		node.setParent(this);
		node.setVisible(true);
		mGroup.add(node);
	}
	
	public boolean remove(BillboardSceneNode node, int mode){
		if (mode == REMOVE_FROM_SCENE){
			node.remove();
		}
		node.setParent(null);
		return mGroup.remove(node);
	}
	
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
	
	public void setVisibleDistance(double near, double far){
		mFar = far; mNear = near;
	}
	
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
	
	private ArrayList<BillboardSceneNode> mGroup;
	private double mNear, mFar;
	private CameraSceneNode mLastCamera;
}
