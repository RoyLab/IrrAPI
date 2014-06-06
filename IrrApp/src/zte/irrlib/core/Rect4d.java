package zte.irrlib.core;

/**
 * 矩形类，边的长度为归一化的浮点数。用于指定贴图的某一个区域
 * 其中（0, 0, 1, 1）代表整个贴图
 * @author Roy
 *
 */
public class Rect4d {
	
	/**
	 * 构造方法，生成一个左上角位于(0, 0)，边长为1.0的正方形
	 */
	public Rect4d(){
		Left = Top = 0; Right = Bottom = 1.0;
	}
	
	/**
	 * 构造方法，指定左上角和右下角的位置
	 * @param left 左
	 * @param up 上
	 * @param right 右
	 * @param bottom 下
	 */
	public Rect4d(double left, double up, double right, double bottom){
		this.Left = left;
		this.Right = right;
		this.Top = up;
		this.Bottom = bottom;
	}
	
	public double Left, Right, Top, Bottom;
}
