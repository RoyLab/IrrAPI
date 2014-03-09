package zte.irrlib.core;

/**
 * 二维浮点向量，一般用于指定面积大小
 * @author Roy
 *
 */
public class Vector2d {

	/**
	 * 构造方法，生成(0, 0)
	 */
	public Vector2d(){
		
	}
	
	public Vector2d(double x, double y){
		this.X = x;
		this.Y = y;
	}
	
	public double X, Y;
}
