package zte.irrlib.core;

/**
 * 三维浮点型向量
 * @author Roy
 *
 */
public class Vector3d {
	
	/**
	 * 构造方法，生成（0,0,0）
	 */
	public Vector3d(){
		
	}	
	
	/**
	 * 复制构造方法
	 * @param v 被复制的向量
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
	 * 向量的加法，逐分量做加法
	 * @param v 另一个向量
	 * @return 加法的结果
	 */
	public Vector3d plus(Vector3d v){
		return new Vector3d(X+v.X, Y+v.Y, Z+v.Z);
	}
	
	/**
	 * 向量的减法，逐分量做减法
	 * @param v 另一个向量
	 * @return 减法的结果
	 */
	public Vector3d minus(Vector3d v){
		return new Vector3d(X-v.X, Y-v.Y, Z-v.Z);
	}
	
	/**
	 * 向量的乘法，逐分量做乘法（不是点积，也不是叉积，而是乘法！）
	 * @param v 另一个向量
	 * @return 乘法的结果
	 */
	public Vector3d multi(Vector3d v){
		return new Vector3d(X*v.X, Y*v.Y, Z*v.Z);
	}
	
	/**
	 * 向量的数乘
	 * @param k 系数
	 * @return 数乘的结果
	 */
	public Vector3d multi(double k){
		return new Vector3d(X*k, Y*k, Z*k);
	}
	
	/**
	 * 两点之间距离的平方
	 * @param v 另一个点
	 * @return 距离的平方
	 */
	public double distanceSquare(Vector3d v){
		return (v.X-X)*(v.X-X) + (v.Y-Y)*(v.Y-Y) + (v.Z-Z)*(v.Z-Z);
	}
	
	/**
	 * 向量的赋值
	 * @param v 源向量
	 * @return 赋值后的目标向量(this)
	 */
	public Vector3d copy(Vector3d v){
		X = v.X; Y = v.Y; Z = v.Z;
		return this;
	}
	
	/**
	 * 求向量的反转（逐分量求反）
	 * @return 反转后的向量(this)
	 */
	public Vector3d reverse(){
		X = -X; Y = -Y; Z = -Z;
		return this;
	}
	
	/**
	 * 向量的点积
	 * @param v 另一个向量
	 * @return 点积的结果
	 */
	public double dot(Vector3d v){
		return v.X*X + v.Y*Y + v.Z*Z;
	}
	
	/**
	 * 向量的叉积
	 * @param v 另一个向量
	 * @return 叉积的结果
	 */
	public Vector3d cross(Vector3d v){
		return new Vector3d(
				Y*v.Z-Z*v.Y,
				Z*v.X-X*v.Z,
				X*v.Y-Y*v.X
				);
	}
	
	/**
	 * 对向量作归一化操作
	 * @return 归一化后的向量(this)
	 */
	public Vector3d normalize(){
		double d = Math.sqrt(X*X+Y*Y+Z*Z);
		X/= d; Y/=d; Z/=d;
		return this;
	}
	
	/**
	 * 判断向量是否约等于0向量，以{@link #ZERO}为标准
	 * @return 如为true则表示约等于0向量
	 */
	public boolean isZero(){
		return ((Math.abs(X) < ZERO) && (Math.abs(Y) < ZERO) && (Math.abs(Z) < ZERO));
	}
	
	/**
	 * 判断两个向量大约是否相等，以{@link #ZERO}为标准
	 * @param v 另一个向量
	 * @return 如为true则表示两个向量大约相等
	 */
	public boolean isEqual(Vector3d v){
		return minus(v).isZero();
	}
	
	/**
	 * 取得向量的长度
	 * @return 向量的长度
	 */
	public double length(){
		return Math.sqrt(X*X + Y*Y + Z*Z);
	}
	
	public double X, Y, Z;
	
	public static final double ZERO = 1E-9;
}
