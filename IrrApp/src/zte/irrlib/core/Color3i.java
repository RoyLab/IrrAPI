package zte.irrlib.core;

/**
 * RGB颜色类，每个颜色的取值区间在0~255的整数
 * @author Roy
 *
 */
public class Color3i {
	
	/**
	 * 构造方法，纯白色
	 */
	public Color3i(){
		red = green = blue = 0xff;
	}
	
	/**
	 * 构造方法
	 * @param v1 红色分量
	 * @param v2 绿色分量
	 * @param v3 蓝色分量
	 */
	public Color3i(int v1, int v2, int v3){
		red = v1; green = v2; blue = v3;
	}
	
	/**
	 * 返回红色分量
	 * @return 红色分量（0~255）
	 */
	public int r() {
		if (red < 0) return 0;
		else if (red > 255) return 255;
		else return red;
	}
	
	/**
	 * 返回绿色分量
	 * @return 绿色分量（0~255）
	 */
	public int g() {
		if (green < 0) return 0;
		else if (green > 255) return 255;
		else return green;
	}
	
	/**
	 * 返回蓝色分量
	 * @return 蓝色分量（0~255）
	 */
	public int b() {
		if (blue < 0) return 0;
		else if (blue > 255) return 255;
		else return blue;
	}
	
	private int red, green, blue;
}
