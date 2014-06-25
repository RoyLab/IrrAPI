package zte.irrlib.core;

/**
 * 包含灯光信息的数据结构。<br>
 * 注意，这些属性设置只有在合适的灯光类型中才有用，
 * 如（{@link #OuterCone}的设置仅在灯光类型为{@link #SPOT_LIGHT}
 * 时，才是有效的）。
 * @author Roy
 *
 */
public class SLight {
	
	/** 点光源，向全方向均匀的发射光线。 */
	public static final int POINT_LIGHT = 0x01;
	/** 圆锥光源，只照亮某个方向上的有限圆锥体内的物体*/
	public static final int SPOT_LIGHT = 0x02;
	/** 平行光源，有方向，影响距离无限远。*/
	public static final int DIRECTIONAL_LIGHT = 0x03;
	
	/**
	 * 灯光类型，可选：{@link #POINT_LIGHT}, {@link #SPOT_LIGHT},
	 * {@link #DIRECTIONAL_LIGHT}.
	 */
	public int Type = POINT_LIGHT;
	
	/** 环境光*/
	public Color3i AmbientColor = new Color3i(0,0,0);
	/** 漫反射光*/
	public Color3i DiffuseColor = new Color3i();
	/** 高光*/
	public Color3i SpecularColor = new Color3i();
	
	/** 
	 * 衰减因子：(常量系数a，一阶系数b，二阶系数c)<br>
	 * 计算公式为: L = L0/(a+b*r+c*r*r).<br>
	 * 其中L是距离r处的光强，L0初始光强。<br>
	 * 这个系数会在使用{@link #setRadius(double)}后
	 * 被覆盖。覆盖值为(0, 1/radius, 0)
	 */
	public Vector3d Attenuation = new Vector3d(0, 0.01, 0);
	
	/**外径角度，仅用于{@link #SPOT_LIGHT}*/
	public double OuterCone = 45.0;
	/**内径角度，仅用于{@link #SPOT_LIGHT}*/
	public double InnerCone = 0.0;
	/**由内径到外径的衰减速度，仅用于{@link #SPOT_LIGHT}*/
	public double Falloff = 2.0;
	
	/**
	 * 设定灯光的名义作用半径，该半径处的光强为指定的光强值
	 * @param radius 名义作用半径
	 */
	public void setRadius(double radius){
		Attenuation.X = 0;
		Attenuation.Y = 1/radius;
		Attenuation.Z = 0;
	}
}
