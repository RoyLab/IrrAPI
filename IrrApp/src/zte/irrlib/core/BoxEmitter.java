package zte.irrlib.core;

/**
 * ��״���ӷ�������
 * @author Fxx
 */
public class BoxEmitter {
	/**
	 * ���ӷ������İ�Χ�У�ָ�������˶��ķ�Χ
	 * ���������ֱ�Ϊ{minX, minY, minZ, maxX, maxY, maxZ}
	 */
	public double BBox[]  = new double[]{-7,-7,-7,7,7,7};
	
	/**
	 * ���ӳ�ʼ���䷽��
	 */
	public Vector3d InitialDirection = new Vector3d(0.0,0.03,0.0);
	
	/**
	 * ������С�����ʺ�������ʣ���λ����/��
	 */
	public int MinEmitRate=5, MaxEmitRate=10;
	
	/**
	 * ��������������ɫ����ɫ
	 */
	public Color4i DarkestColor = new Color4i(0,0,0,255);
	
	/**
	 * �����������������������ɫ
	 */
	public Color4i BrightestColor = new Color4i(255,255,255,255);
	
	/**
	 * ÿ�����Ӵ��������������λ������
	 */
	public int MinLifeTime=2000;
	
	/**
	 * ÿ�����Ӵ������������λ������
	 */
	public int MaxLifeTime=4000;
	
	/**
	 * ����ƫ���ʼ�������Ƕ�ֵ����λ����
	 */
	public int MaxAngleDegrees = 0;
	
	/**
	 * ÿ�����ӵ���С�ߴ�
	 */
	public Vector2d MinSize = new Vector2d(5.0,5.0);
	
	/**
	 * ÿ�����ӵ����ߴ�
	 */
	public Vector2d  MaxSize = new Vector2d(5.0,5.0);
	
}
