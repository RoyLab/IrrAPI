package zte.irrlib.core;

/**
 * 矩形类，边的长度只能为整数，通常用于指定在像素阵列的区域。
 * @author Roy
 *
 */
public class Rect4i {
	
	/**
	 * 构造方法，生成一个左上角位于(0, 0)，边长为10的正方形
	 */
	public Rect4i(){
		Left = Top = 0; Right = Bottom = 10;
	}
	
	/**
	 * 构造方法，指定左上角和右下角的位置
	 * @param left 左
	 * @param up 上
	 * @param right 右
	 * @param bottom 下
	 */
	public Rect4i(int left, int up, int right, int bottom){
		this.Left = left;
		this.Right = right;
		this.Top = up;
		this.Bottom = bottom;
	}
	
	public int Left, Right, Top, Bottom;
}
