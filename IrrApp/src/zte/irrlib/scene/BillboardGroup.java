package zte.irrlib.scene;



/**
 * �������ڵ��࣬����һ�鹫����λ�ã��ɼ��Եȣ�ͬʱ��������
 * ���ڹ����Ŀɼ��ľ��뷶Χ������������ֲ���ȵ�ģ�⡣
 * @author Fxx
 *
 */
public class BillboardGroup extends SceneNode implements Scene.Updatable{
	
	public static final int REMOVE_FROM_SCENE = 0x01;
	public static final int REMOVE_FROM_GROUP = 0x02;
	
	/**
	 * ���ָ�������ڵ㵽������顣
	 * @param node ����ӵĹ�������
	 */
	public void add(BillboardSceneNode node){
		node.setParent(this);
		node.setVisible(true);
	}
	
	/**
	 * �ӹ�������Ƴ�ָ�������ڵ�
	 * @param node ��Ҫ�Ƴ��Ľڵ����
	 * @param mode �Ƴ���ģʽ��
	 * 		ģʽΪ REMOVE_FROM_SCENE ��ӹ�������Ƴ����ӳ�����ɾ���ýڵ�
	 * 		ģʽΪ REMOVE_FROM_GROUP ����ӹ�������Ƴ�����ͬ��{@link #removeChild(SceneNode)}
	 * @return �Ƴ��ɹ�����true�����򷵻�false
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
	 * ��������еĽڵ�ȫ���Ƴ�
	 * @param mode �Ƴ���ģʽ��
	 * 		ģʽΪ REMOVE_FROM_SCENE ��ӹ�������Ƴ����ӳ�����ɾ���ýڵ�
	 * 		ģʽΪ REMOVE_FROM_GRO UP ����ӹ�������Ƴ�����ͬ��{@link #removeChild(SceneNode)}
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
	 * ���ù����Ŀ��ӷ�Χ
	 * @param near ��������������˵�ֵ
	 * @param far ������������Զ�˵�ֵ
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
	 * Ψһ���캯����
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
