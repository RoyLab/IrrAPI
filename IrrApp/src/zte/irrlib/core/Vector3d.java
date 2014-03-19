package zte.irrlib.core;

/**
 * ��ά����������
 * @author Roy
 *
 */
public class Vector3d {
	
	/**
	 * ���췽�������ɣ�0,0,0��
	 */
	public Vector3d(){
		
	}	
	
	/**
	 * ���ƹ��췽��
	 * @param v �����Ƶ�����
	 */
	public Vector3d(Vector3d v){
		this.X = v.X;
		this.Y = v.Y;
		this.Z = v.Z;
	}
	
	public Vector3d(double x, double y, double z){
		this.X = x; this.Y = y; this.Z = z;
	}
	
	/**
	 * �����ļӷ�����������ӷ�
	 * @param v ��һ������
	 * @return �ӷ��Ľ��
	 */
	public Vector3d plus(Vector3d v){
		return new Vector3d(X+v.X, Y+v.Y, Z+v.Z);
	}
	
	/**
	 * �����ļ����������������
	 * @param v ��һ������
	 * @return �����Ľ��
	 */
	public Vector3d minus(Vector3d v){
		return new Vector3d(X-v.X, Y-v.Y, Z-v.Z);
	}
	
	/**
	 * �����ĳ˷�����������˷������ǵ����Ҳ���ǲ�������ǳ˷�����
	 * @param v ��һ������
	 * @return �˷��Ľ��
	 */
	public Vector3d multi(Vector3d v){
		return new Vector3d(X*v.X, Y*v.Y, Z*v.Z);
	}
	
	/**
	 * ����������
	 * @param k ϵ��
	 * @return ���˵Ľ��
	 */
	public Vector3d multi(double k){
		return new Vector3d(X*k, Y*k, Z*k);
	}
	
	/**
	 * ����֮������ƽ��
	 * @param v ��һ����
	 * @return �����ƽ��
	 */
	public double distanceSquare(Vector3d v){
		return (v.X-X)*(v.X-X) + (v.Y-Y)*(v.Y-Y) + (v.Z-Z)*(v.Z-Z);
	}
	
	/**
	 * �����ĸ�ֵ
	 * @param v Դ����
	 * @return ��ֵ���Ŀ������(this)
	 */
	public Vector3d copy(Vector3d v){
		X = v.X; Y = v.Y; Z = v.Z;
		return this;
	}
	
	/**
	 * �������ķ�ת��������󷴣�
	 * @return ��ת�������(this)
	 */
	public Vector3d reverse(){
		X = -X; Y = -Y; Z = -Z;
		return this;
	}
	
	/**
	 * �����ĵ��
	 * @param v ��һ������
	 * @return ����Ľ��
	 */
	public double dot(Vector3d v){
		return v.X*X + v.Y*Y + v.Z*Z;
	}
	
	/**
	 * �����Ĳ��
	 * @param v ��һ������
	 * @return ����Ľ��
	 */
	public Vector3d cross(Vector3d v){
		return new Vector3d(
				Y*v.Z-Z*v.Y,
				Z*v.X-X*v.Z,
				X*v.Y-Y*v.X
				);
	}
	
	/**
	 * ����������һ������
	 * @return ��һ���������(this)
	 */
	public Vector3d normalize(){
		double d = Math.sqrt(X*X+Y*Y+Z*Z);
		X/= d; Y/=d; Z/=d;
		return this;
	}
	
	/**
	 * �ж������Ƿ�Լ����0��������{@link #ZERO}Ϊ��׼
	 * @return ��Ϊtrue���ʾԼ����0����
	 */
	public boolean isZero(){
		return ((Math.abs(X) < ZERO) && (Math.abs(Y) < ZERO) && (Math.abs(Z) < ZERO));
	}
	
	/**
	 * �ж�����������Լ�Ƿ���ȣ���{@link #ZERO}Ϊ��׼
	 * @param v ��һ������
	 * @return ��Ϊtrue���ʾ����������Լ���
	 */
	public boolean isEqual(Vector3d v){
		return minus(v).isZero();
	}
	
	/**
	 * ȡ�������ĳ���
	 * @return �����ĳ���
	 */
	public double length(){
		return Math.sqrt(X*X + Y*Y + Z*Z);
	}
	
	public double X, Y, Z;
	
	public static final double ZERO = 1E-9;
}
