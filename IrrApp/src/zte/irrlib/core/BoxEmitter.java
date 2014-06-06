package zte.irrlib.core;

/**
 * 盒状粒子发射器类
 * @author Fxx
 */
public class BoxEmitter {
	/**
	 * 粒子发射器的包围盒，指定粒子运动的范围
	 * 六个参数分别为{minX, minY, minZ, maxX, maxY, maxZ}
	 */
	public double BBox[]  = new double[]{-7,-7,-7,7,7,7};
	
	/**
	 * 粒子初始发射方向
	 */
	public Vector3d InitialDirection = new Vector3d(0.0,0.03,0.0);
	
	/**
	 * 粒子最小发射率和最大发射率，单位：个/秒
	 */
	public int MinEmitRate=5, MaxEmitRate=10;
	
	/**
	 * 发射的粒子最暗的颜色的颜色
	 */
	public Color4i DarkestColor = new Color4i(0,0,0,255);
	
	/**
	 * 发射的粒子最亮的最亮的颜色
	 */
	public Color4i BrightestColor = new Color4i(255,255,255,255);
	
	/**
	 * 每个粒子存活的最短寿命，单位：毫秒
	 */
	public int MinLifeTime=2000;
	
	/**
	 * 每个粒子存活的最长寿命，单位：毫秒
	 */
	public int MaxLifeTime=4000;
	
	/**
	 * 粒子偏离初始方向最大角度值，单位：度
	 */
	public int MaxAngleDegrees = 0;
	
	/**
	 * 每个粒子的最小尺寸
	 */
	public Vector2d MinSize = new Vector2d(5.0,5.0);
	
	/**
	 * 每个粒子的最大尺寸
	 */
	public Vector2d  MaxSize = new Vector2d(5.0,5.0);
	
}
