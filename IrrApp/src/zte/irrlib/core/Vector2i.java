package zte.irrlib.core;

/**
 * 二维整型向量
 * @author Roy
 *
 */
public class Vector2i {
	
	/**
	 * 构造方法，生成(0, 0)
	 */
	public Vector2i(){
		
	}
	
	public Vector2i(int x, int y){
		this.X = x; this.Y = y;
	}
	
	public int X, Y;
}
