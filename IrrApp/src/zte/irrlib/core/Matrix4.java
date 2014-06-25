package zte.irrlib.core;

/**
 * 矩阵类
 * @author Roy
 *
 */
public class Matrix4 {
	
	public static final int NUM = 16;
	
	/** 浮点数的精度*/
	public static float PRECISE = 1.0e-9f;
	
	/** 矩阵数据*/
	public float[] M;
	
	/**
	 * 索引数据
	 * @param index 索引位置
	 * @return 该位置的数据
	 */
	public float at(int index){
		return M[index];
	}
	
	/**
	 * 索引数据（二维版）
	 * @param row 行索引
	 * @param colum 列索引
	 * @return 该位置的数据
	 */
	public float at(int row, int colum){
		return M[row*4+colum];
	}
	
	/**
	 * 构造方法，生成0矩阵
	 */
	public Matrix4(){
		M = new float[NUM];
	}
	
	/**
	 * 复制构造方法
	 * @param other
	 */
	public Matrix4(final Matrix4 other){
		this();
		for (int i = 0; i < NUM; i++){
			M[i] = other.M[i];
		}
	}
	
	@Override
	public boolean equals(Object other){
		if (other.getClass() != Matrix4.class)
			return false;
		Matrix4 a = (Matrix4)other;
		for (int i = 0; i < 4; i++){
			if (Math.abs(M[i]-a.M[i]) > PRECISE)
				return false;
		}
		return true;
	}
	
	@Override
	public String toString(){
		String res = "";
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				res += M[i*4+j];
				res += " ";
			}
			res += "\n";
		}
		return res;
	}
	
	/**
	 * 静态方法，生成左手系的相机变换矩阵
	 * @param pos 相机位置
	 * @param tar 相机目标位置
	 * @param upVec 相机头向量的位置
	 * @return 生成的矩阵
	 */
	public static Matrix4 buildCameraLookAtMatrixLH(Vector3d pos, Vector3d tar, Vector3d upVec){
		Matrix4 res = new Matrix4();
		nativeBuildCameraLookAtMatrixLH(res, pos, tar, upVec);
		return res;
	}
	
	/**
	 * 静态方法，生成右手系的相机变换矩阵
	 * @param pos 相机位置
	 * @param tar 相机目标位置
	 * @param upVec 相机头向量的位置
	 * @return 生成的矩阵
	 */
	public static Matrix4 buildCameraLookAtMatrixRH(Vector3d pos, Vector3d tar, Vector3d upVec){
		Matrix4 res = new Matrix4();
		nativeBuildCameraLookAtMatrixRH(res, pos, tar, upVec);
		return res;
	}
	
	/**
	 * 静态方法，生成左手系正交投影矩阵
	 * @param width 视景体宽度
	 * @param height 视景体高度
	 * @param near 前裁剪平面
	 * @param far 后裁剪平面
	 * @return 生成的矩阵
	 */
	public static Matrix4 buildProjectionMatrixOrthoLH(float width, float height, float near, float far){
		Matrix4 res = new Matrix4();
		nativeBuildProjectionMatrixOrthoLH(res, width, height, near, far);
		return res;
	}
	
	/**
	 * 静态方法，生成右手系正交投影矩阵
	 * @param width 视景体宽度
	 * @param height 视景体高度
	 * @param near 前裁剪平面
	 * @param far 后裁剪平面
	 * @return 生成的矩阵
	 */
	public static Matrix4 buildProjectionMatrixOrthoRH(float width, float height, float near, float far){
		Matrix4 res = new Matrix4();
		nativeBuildProjectionMatrixOrthoRH(res, width, height, near, far);
		return res;
	}
	
	/**
	 * 静态方法，生成左手系透视投影矩阵
	 * @param width 视景体宽度
	 * @param height 视景体高度
	 * @param near 前裁剪平面
	 * @param far 后裁剪平面
	 * @return 生成的矩阵
	 */
	public static Matrix4 buildProjectionMatrixPerspectiveLH(float width, float height, float near, float far){
		Matrix4 res = new Matrix4();
		nativeBuildProjectionMatrixPerspectiveLH(res, width, height, near, far);
		return res;
	}
	
	/**
	 * 静态方法，生成右手系透视投影矩阵
	 * @param width 视景体宽度
	 * @param height 视景体高度
	 * @param near 前裁剪平面
	 * @param far 后裁剪平面
	 * @return 生成的矩阵
	 */
	public static Matrix4 buildProjectionMatrixPerspectiveRH(float width, float height, float near, float far){
		Matrix4 res = new Matrix4();
		nativeBuildProjectionMatrixPerspectiveRH(res, width, height, near, far);
		return res;
	}
	
	/**
	 * 静态方法，生成左手系透视投影矩阵
	 * @param width 视景体宽度
	 * @param height 视景体高度
	 * @param near 前裁剪平面
	 * @param far 后裁剪平面
	 * @return 生成的矩阵
	 */
	public static Matrix4 buildProjectionMatrixPerspectiveFovLH(float radians, float ratio, float near, float far){
		Matrix4 res = new Matrix4();
		nativeBuildProjectionMatrixPerspectiveFovLH(res, radians, ratio, near, far);
		return res;
	}
	
	/**
	 * 静态方法，生成右手系透视投影矩阵
	 * @param width 视景体宽度
	 * @param height 视景体高度
	 * @param near 前裁剪平面
	 * @param far 后裁剪平面
	 * @return 生成的矩阵
	 */
	public static Matrix4 buildProjectionMatrixPerspectiveFovRH(float radians, float ratio, float near, float far){
		Matrix4 res = new Matrix4();
		nativeBuildProjectionMatrixPerspectiveFovRH(res, radians, ratio, near, far);
		return res;
	}
	
	/**
	 * 静态方法，通过两个向量生成旋转矩阵
	 * @param one 原向量
	 * @param another 变换后的向量
	 * @return 生成的向量
	 */
	public static Matrix4 buildRotateFromTo(Vector3d one, Vector3d another){
		Matrix4 res = new Matrix4();
		nativeBuildRotateFromTo(res, one, another);
		return res;
	}
	
	/**
	 * 取得该矩阵所包含的旋转信息
	 * @return 旋转角度
	 */
	public Vector3d getRotationDegrees(){
		Vector3d res = new Vector3d();
		nativeGetRotationDegrees(res);
		return res;
	}
	
	/**
	 * 取得该矩阵所包含的缩放信息
	 * @return 缩放比例
	 */
	public Vector3d getScale(){
		Vector3d res = new Vector3d();
		nativeGetScale(res);
		return res;
	}
	
	/**
	 * 取得该矩阵所包含的平移信息
	 * @return 平移向量
	 */
	public Vector3d getTranslation(){
		Vector3d res = new Vector3d();
		nativeGetTranslation(res);
		return res;
	}
	
	/**
	 * 设定当前矩阵的缩放比例
	 * @param vec 设定的缩放比例
	 */
	public void setScale(Vector3d vec){
		nativeSetScale(vec);
	}
	
	/**
	 * 设定当前矩阵的旋转角度
	 * @param vec 设定的旋转角度
	 */
	public void setRotationDegrees(Vector3d rotation){
		nativeSetRotationDegrees(rotation);
	}
	
	/**
	 * 通过具有OpenGL风格的参数设置设定旋转矩阵
	 * @param angle 旋转角度
	 * @param axis 旋转轴
	 */
	public void setRotationAxisRadians(float angle, Vector3d axis){
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		double t = 1.0 - c;

		double tx  = t * axis.X;
		double ty  = t * axis.Y;     
		double tz  = t * axis.Z;
		
		double sx  = s * axis.X;
		double sy  = s * axis.Y;
		double sz  = s * axis.Z;
		
		M[0] = (float)(tx * axis.X + c);
		M[1] = (float)(tx * axis.Y + sz);
		M[2] = (float)(tx * axis.Z - sy);
		
		M[4] = (float)(ty * axis.X - sz);
		M[5] = (float)(ty * axis.Y + c);
		M[6] = (float)(ty * axis.Z + sx);
		
		M[8]  = (float)(tz * axis.X + sy);
		M[9]  = (float)(tz * axis.Y - sx);
		M[10] = (float)(tz * axis.Z + c);
	}
	
	/**
	 * 设定当前矩阵的平移量
	 * @param translation 设定的平移向量
	 */
	public void setTranslation(Vector3d translation){
		nativeSetTranslation(translation);		
	}
	
	/**
	 * 对向量应用矩阵变换
	 * @param coord 被变换的向量
	 */
	public void transformVect(Vector3d coord){
		nativeTransformVect(coord);
	}
	
	/**
	 * 求逆矩阵
	 * @return 若矩阵可逆，返回true
	 */
	public boolean makeInverse(){
		return nativeMakeInverse();
	}
	
	/**
	 * 转置矩阵
	 */
	public void transpose(){
		nativeTranspose();
	}
	
	/**
	 * 设定为单位矩阵
	 */
	public void makeIdentity(){
		M = new float[NUM];
		M[0] = M[5] = M[10] = M[15] = 1.0f;
	}
	
	/**
	 * 所有位置的矩阵数据求反
	 */
	public void makeNegative(){
		for (int i = 0; i < NUM; i++){
			M[i] = -M[i];
		}
	}
	
	/**
	 * 复制矩阵
	 * @param other 被复制到矩阵
	 */
	public void copy(final Matrix4 other){
		for (int i = 0; i < NUM; i++){
			M[i] = other.M[i];
		}
	}
	
	/**
	 * 矩阵加法
	 * @param other 另一个矩阵
	 * @return 矩阵之和
	 */
	public Matrix4 plus(final Matrix4 other){
		Matrix4 res = new Matrix4();
		for (int i = 0; i < NUM; i++){
			res.M[i] = M[i] + other.M[i];
		}
		return res;
	}
	
	/**
	 * 矩阵减法
	 * @param other 另一个矩阵
	 * @return 矩阵之差
	 */
	public Matrix4 minus(final Matrix4 other){
		Matrix4 res = new Matrix4();
		for (int i = 0; i < NUM; i++){
			res.M[i] = M[i] - other.M[i];
		}
		return res;
	}
	
	/**
	 * 矩阵乘法
	 * @param other 另一个矩阵
	 * @return 矩阵的积
	 */
	public Matrix4 multiply(final Matrix4 other){
		Matrix4 res = new Matrix4();
		res.M[0] = M[0]*other.M[0] + M[4]*other.M[1] + M[8]*other.M[2] + M[12]*other.M[3];
		res.M[1] = M[1]*other.M[0] + M[5]*other.M[1] + M[9]*other.M[2] + M[13]*other.M[3];
		res.M[2] = M[2]*other.M[0] + M[6]*other.M[1] + M[10]*other.M[2] + M[14]*other.M[3];
		res.M[3] = M[3]*other.M[0] + M[7]*other.M[1] + M[11]*other.M[2] + M[15]*other.M[3];

		res.M[4] = M[0]*other.M[4] + M[4]*other.M[5] + M[8]*other.M[6] + M[12]*other.M[7];
		res.M[5] = M[1]*other.M[4] + M[5]*other.M[5] + M[9]*other.M[6] + M[13]*other.M[7];
		res.M[6] = M[2]*other.M[4] + M[6]*other.M[5] + M[10]*other.M[6] + M[14]*other.M[7];
		res.M[7] = M[3]*other.M[4] + M[7]*other.M[5] + M[11]*other.M[6] + M[15]*other.M[7];

		res.M[8] = M[0]*other.M[8] + M[4]*other.M[9] + M[8]*other.M[10] + M[12]*other.M[11];
		res.M[9] = M[1]*other.M[8] + M[5]*other.M[9] + M[9]*other.M[10] + M[13]*other.M[11];
		res.M[10] = M[2]*other.M[8] + M[6]*other.M[9] + M[10]*other.M[10] + M[14]*other.M[11];
		res.M[11] = M[3]*other.M[8] + M[7]*other.M[9] + M[11]*other.M[10] + M[15]*other.M[11];

		res.M[12] = M[0]*other.M[12] + M[4]*other.M[13] + M[8]*other.M[14] + M[12]*other.M[15];
		res.M[13] = M[1]*other.M[12] + M[5]*other.M[13] + M[9]*other.M[14] + M[13]*other.M[15];
		res.M[14] = M[2]*other.M[12] + M[6]*other.M[13] + M[10]*other.M[14] + M[14]*other.M[15];
		res.M[15] = M[3]*other.M[12] + M[7]*other.M[13] + M[11]*other.M[14] + M[15]*other.M[15];
		return res;
	}
	
	/**
	 * 矩阵的数乘
	 * @param scale 被乘系数
	 * @return 数乘后的矩阵
	 */
	public Matrix4 multiply(float scale){
		Matrix4 res = new Matrix4();
		for (int i = 0; i < NUM; i++){
			res.M[i] = M[i]*scale;
		}
		return res;		
	}

	private static native void nativeBuildCameraLookAtMatrixLH(
			Matrix4 mat, Vector3d pos, Vector3d tar, Vector3d up);
	
	private static native void nativeBuildCameraLookAtMatrixRH(
			Matrix4 mat, Vector3d pos, Vector3d tar, Vector3d up);
	
	private static native void nativeBuildProjectionMatrixOrthoLH(
			Matrix4 mat, float width, float height, float near, float far);
	
	private static native void nativeBuildProjectionMatrixOrthoRH(
			Matrix4 mat, float width, float height, float near, float far);
	
	private static native void nativeBuildProjectionMatrixPerspectiveLH(
			Matrix4 mat, float width, float height, float near, float far);
	
	private static native void nativeBuildProjectionMatrixPerspectiveRH(
			Matrix4 mat, float width, float height, float near, float far);
	
	private static native void nativeBuildProjectionMatrixPerspectiveFovLH(
			Matrix4 mat, float angle, float ratio, float near, float far);
	
	private static native void nativeBuildProjectionMatrixPerspectiveFovRH(
			Matrix4 mat, float angle, float ratio, float near, float far);

	private static native void nativeBuildRotateFromTo(
			Matrix4 mat, Vector3d one, Vector3d another);
	
	private native void nativeGetRotationDegrees(Vector3d res);
	
	private native void nativeGetScale(Vector3d res);
	
	private native void nativeGetTranslation(Vector3d res);
	
	private native void nativeSetScale(Vector3d res);
	
	private native void nativeSetRotationDegrees(Vector3d rot);
	
	private native void nativeSetTranslation(Vector3d vec);
	
	private native void nativeTransformVect(Vector3d res);
	
	private native boolean nativeMakeInverse();
	
	private native void nativeTranspose();
}
