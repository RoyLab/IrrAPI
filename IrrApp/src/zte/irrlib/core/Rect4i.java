package zte.irrlib.core;

/**
 * �����࣬�ߵĳ���ֻ��Ϊ������ͨ������ָ�����������е�����
 * @author Roy
 *
 */
public class Rect4i {
	
	/**
	 * ���췽��������һ�����Ͻ�λ��(0, 0)���߳�Ϊ10��������
	 */
	public Rect4i(){
		Left = Top = 0; Right = Bottom = 10;
	}
	
	/**
	 * ���췽����ָ�����ϽǺ����½ǵ�λ��
	 * @param left ��
	 * @param up ��
	 * @param right ��
	 * @param bottom ��
	 */
	public Rect4i(int left, int up, int right, int bottom){
		this.Left = left;
		this.Right = right;
		this.Top = up;
		this.Bottom = bottom;
	}
	
	public int Left, Right, Top, Bottom;
}
