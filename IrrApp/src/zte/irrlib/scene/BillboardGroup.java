package zte.irrlib.scene;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * �������ڵ��࣬����һ�鹫����λ�ã��ɼ��Եȣ�ͬʱ��������
 * ���ڹ����Ŀɼ��ľ��뷶Χ������������ֲ���ȵ�ģ�⡣
 * @author Fxx
 *
 */
public class BillboardGroup extends SceneNode{
	
	public static final int REMOVE_FROM_SCENE = 0x01;
	public static final int REMOVE_FROM_GROUP = 0x02;
	
	/**
	 * ���ָ�������ڵ㵽������顣
	 * @param node ����ӵĹ�������
	 */
	public void add(BillboardSceneNode node){
		node.setParent(this);
		node.setVisible(true);
		mGroup.add(node);
	}
	
	/**
	 * �ӹ�������Ƴ�ָ�������ڵ�
	 * @param node ��Ҫ�Ƴ��Ľڵ����
	 * @param mode �Ƴ���ģʽ��
	 * 		ģʽΪ REMOVE_FROM_SCENE ��ӹ�������Ƴ����ӳ�����ɾ���ýڵ�
	 * 		ģʽΪ REMOVE_FROM_GROUP ����ӹ�������Ƴ�
	 * @return �Ƴ��ɹ�����true�����򷵻�false
	 */
	public boolean remove(BillboardSceneNode node, int mode){
		if (mode == REMOVE_FROM_SCENE){
			node.remove();
		}
		node.setParent(null);
		return mGroup.remove(node);
	}
	
	/**
	 * ��������еĽڵ�ȫ���Ƴ�
	 * @param mode �Ƴ���ģʽ��
	 * 		ģʽΪ REMOVE_FROM_SCENE ��ӹ�������Ƴ����ӳ�����ɾ���ýڵ�
	 * 		ģʽΪ REMOVE_FROM_GROUP ����ӹ�������Ƴ�
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
	 * ���ù����Ŀ��ӷ�Χ
	 * @param near ��������������˵�ֵ
	 * @param far ������������Զ�˵�ֵ
	 */
	public void setVisibleDistance(double near, double far){
		mFar = far; mNear = near;
	}
	
	/**
	 * ���ո����������Թ�������ڽڵ��Ƿ���ӽ��и���
	 * @param camera �������ݵ��������
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
	 * Ψһ���캯����
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
