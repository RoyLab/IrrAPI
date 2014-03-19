package zte.irrlib.core;

/**
 * �����࣬�ߵĳ���ֻ��Ϊ������ͨ������ָ�����������е�����
 * @author Roy
 *
 */
public class Rect4d {
	
	/**
	 * ���췽��������һ�����Ͻ�λ��(0, 0)���߳�Ϊ1.0��������
	 */
	public Rect4d(){
		Left = Top = 0; Right = Bottom = 1.0;
	}
	
	/**
	 * ���췽����ָ�����ϽǺ����½ǵ�λ��
	 * @param left ��
	 * @param up ��
	 * @param right ��
	 * @param bottom ��
	 */
	public Rect4d(double left, double up, double right, double bottom){
		this.Left = left;
		this.Right = right;
		this.Top = up;
		this.Bottom = bottom;
	}
	
	public double Left, Right, Top, Bottom;
}
