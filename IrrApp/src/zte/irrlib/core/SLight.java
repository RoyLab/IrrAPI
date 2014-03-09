package zte.irrlib.core;

/**
 * �����ƹ���Ϣ�����ݽṹ��<br>
 * ע�⣬��Щ��������ֻ���ں��ʵĵƹ������в����ã�
 * �磨{@link #OuterCone}�����ý��ڵƹ�����Ϊ{@link #SPOT_LIGHT}
 * ʱ��������Ч�ģ���
 * @author Roy
 *
 */
public class SLight {
	
	/** ���Դ����ȫ������ȵķ�����ߡ� */
	public static final int POINT_LIGHT = 0x01;
	/** Բ׶��Դ��ֻ����ĳ�������ϵ�����Բ׶���ڵ�����*/
	public static final int SPOT_LIGHT = 0x02;
	/** ƽ�й�Դ���з���Ӱ���������Զ��*/
	public static final int DIRECTIONAL_LIGHT = 0x03;
	
	/**
	 * �ƹ����ͣ���ѡ��{@link #POINT_LIGHT}, {@link #SPOT_LIGHT},
	 * {@link #DIRECTIONAL_LIGHT}.
	 */
	public int Type = POINT_LIGHT;
	
	/** ������*/
	public Color3i AmbientColor = new Color3i(0,0,0);
	/** �������*/
	public Color3i DiffuseColor = new Color3i();
	/** �߹�*/
	public Color3i SpecularColor = new Color3i();
	
	/** 
	 * ˥�����ӣ�(����ϵ��a��һ��ϵ��b������ϵ��c)<br>
	 * ���㹫ʽΪ: L = L0/(a+b*r+c*r*r).<br>
	 * ����L�Ǿ���r���Ĺ�ǿ��L0��ʼ��ǿ��<br>
	 * ���ϵ������ʹ��{@link #setRadius(double)}��
	 * �����ǡ�����ֵΪ(0, 1/radius, 0)
	 */
	public Vector3d Attenuation = new Vector3d(0, 0.01, 0);
	
	/**�⾶�Ƕȣ�������{@link #SPOT_LIGHT}*/
	public double OuterCone = 45.0;
	/**�ھ��Ƕȣ�������{@link #SPOT_LIGHT}*/
	public double InnerCone = 0.0;
	/**���ھ����⾶��˥���ٶȣ�������{@link #SPOT_LIGHT}*/
	public double Falloff = 2.0;
	
	/**
	 * �趨�ƹ���������ð뾶���ð뾶���Ĺ�ǿΪָ���Ĺ�ǿֵ
	 * @param radius �������ð뾶
	 */
	public void setRadius(double radius){
		mRadius = radius;
		Attenuation.X = 0;
		Attenuation.Y = 1/mRadius;
		Attenuation.Z = 0;
	}
	
	/**
	 * ȡ���������ð뾶��ֵ
	 * @return �������ð뾶
	 */
	public double getRadius(){
		return mRadius;
	}
	
	private double mRadius;
}
