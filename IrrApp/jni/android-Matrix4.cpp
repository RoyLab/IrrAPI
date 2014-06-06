#include <jni.h>
#include <irrlicht.h>
#include "android-global.h"

#ifdef LOG_TAG
#undef LOG_TAG
#endif

#define LOG_TAG "NativeMatrix"

extern "C"
{
	void Java_zte_irrlib_core_Matrix4_nativeBuildCameraLookAtMatrixLH(
		JNIEnv *env, jclass clazz, jobject jmat, jobject jpos, 
		jobject jtar, jobject jupVec)
	{
		matrix4 mat;
		mat.buildCameraLookAtMatrixLH(
			utils->createvector3dfFromVector3d(env, jpos),
			utils->createvector3dfFromVector3d(env, jtar),
			utils->createvector3dfFromVector3d(env, jupVec)
			);
		
		utils->setMatrix4Frommatrix4(env, jmat, mat);
	}
	
	void Java_zte_irrlib_core_Matrix4_nativeBuildCameraLookAtMatrixRH(
		JNIEnv *env, jclass clazz, jobject jmat, jobject jpos, 
		jobject jtar, jobject jupVec)
	{
		matrix4 mat;
		mat.buildCameraLookAtMatrixRH(
			utils->createvector3dfFromVector3d(env, jpos),
			utils->createvector3dfFromVector3d(env, jtar),
			utils->createvector3dfFromVector3d(env, jupVec)
			);
		
		utils->setMatrix4Frommatrix4(env, jmat, mat);
	}

	void Java_zte_irrlib_core_Matrix4_nativeBuildProjectionMatrixOrthoLH(
		JNIEnv *env, jclass clazz, jobject jmat, jfloat width, 
		jfloat height, jfloat near, jfloat far)
	{
		matrix4 mat;
		mat.buildProjectionMatrixOrthoLH(width, height, near, far);
		utils->setMatrix4Frommatrix4(env, jmat, mat);
	}

	void Java_zte_irrlib_core_Matrix4_nativeBuildProjectionMatrixOrthoRH(
		JNIEnv *env, jclass clazz, jobject jmat, jfloat width, 
		jfloat height, jfloat near, jfloat far)
	{
		matrix4 mat;
		mat.buildProjectionMatrixOrthoRH(width, height, near, far);
		utils->setMatrix4Frommatrix4(env, jmat, mat);
	}
	
	void Java_zte_irrlib_core_Matrix4_nativeBuildProjectionMatrixPerspectiveLH(
		JNIEnv *env, jclass clazz, jobject jmat, jfloat width, 
		jfloat height, jfloat near, jfloat far)
	{
		matrix4 mat;
		mat.buildProjectionMatrixPerspectiveLH(width, height, near, far);
		utils->setMatrix4Frommatrix4(env, jmat, mat);
	}

	void Java_zte_irrlib_core_Matrix4_nativeBuildProjectionMatrixPerspectiveRH(
		JNIEnv *env, jclass clazz, jobject jmat, jfloat width, 
		jfloat height, jfloat near, jfloat far)
	{
		matrix4 mat;
		mat.buildProjectionMatrixPerspectiveRH(width, height, near, far);
		utils->setMatrix4Frommatrix4(env, jmat, mat);
	}

	void Java_zte_irrlib_core_Matrix4_nativeBuildProjectionMatrixPerspectiveFovLH(
		JNIEnv *env, jclass clazz, jobject jmat, jfloat angle, 
		jfloat ratio, jfloat near, jfloat far)
	{
		matrix4 mat;
		mat.buildProjectionMatrixPerspectiveFovLH(angle, ratio, near, far);
		utils->setMatrix4Frommatrix4(env, jmat, mat);
	}

	void Java_zte_irrlib_core_Matrix4_nativeBuildProjectionMatrixPerspectiveFovRH(
		JNIEnv *env, jclass clazz, jobject jmat, jfloat angle, 
		jfloat ratio, jfloat near, jfloat far)
	{
		matrix4 mat;
		mat.buildProjectionMatrixPerspectiveFovRH(angle, ratio, near, far);
		utils->setMatrix4Frommatrix4(env, jmat, mat);
	}

	void Java_zte_irrlib_core_Matrix4_nativeBuildRotateFromTo(
		JNIEnv *env, jclass clazz, jobject jmat, jobject jone, 
		jobject janother)
	{
		matrix4 mat;
		mat.buildRotateFromTo(
			utils->createvector3dfFromVector3d(env, jone),
			utils->createvector3dfFromVector3d(env, janother)
			);
		
		utils->setMatrix4Frommatrix4(env, jmat, mat);
	}

	void Java_zte_irrlib_core_Matrix4_nativeGetRotationDegrees(
		JNIEnv *env, jobject thiz, jobject jvec)
	{
		utils->setVector3dFromvector3df(env, jvec,
			utils->creatematrix4FromMatrix4(env, thiz).getRotationDegrees());
	}
	
	void Java_zte_irrlib_core_Matrix4_nativeGetScale(
		JNIEnv *env, jobject thiz, jobject jvec)
	{
		utils->setVector3dFromvector3df(env, jvec,
			utils->creatematrix4FromMatrix4(env, thiz).getScale());
	}
	
	void Java_zte_irrlib_core_Matrix4_nativeGetTranslation(
		JNIEnv *env, jobject thiz, jobject jvec)
	{
		utils->setVector3dFromvector3df(env, jvec,
			utils->creatematrix4FromMatrix4(env, thiz).getTranslation());
	}
	
	void Java_zte_irrlib_core_Matrix4_nativeSetScale(
		JNIEnv *env, jobject thiz, jobject jvec)
	{
		matrix4 mat(utils->creatematrix4FromMatrix4(env, thiz));
		vector3df vec(utils->createvector3dfFromVector3d(env, jvec));
		mat.setScale(vec);
		utils->setMatrix4Frommatrix4(env, thiz, mat);
	}
	
	void Java_zte_irrlib_core_Matrix4_nativeSetRotationDegrees(
		JNIEnv *env, jobject thiz, jobject jvec)
	{
		matrix4 mat(utils->creatematrix4FromMatrix4(env, thiz));
		vector3df vec(utils->createvector3dfFromVector3d(env, jvec));
		mat.setRotationDegrees(vec);
		utils->setMatrix4Frommatrix4(env, thiz, mat);
	}
	
	void Java_zte_irrlib_core_Matrix4_nativeSetTranslation(
		JNIEnv *env, jobject thiz, jobject jvec)
	{
		matrix4 mat(utils->creatematrix4FromMatrix4(env, thiz));
		vector3df vec(utils->createvector3dfFromVector3d(env, jvec));
		mat.setTranslation(vec);
		utils->setMatrix4Frommatrix4(env, thiz, mat);
	}

	void Java_zte_irrlib_core_Matrix4_nativeTransformVect(
		JNIEnv *env, jobject thiz, jobject jvec)
	{
		matrix4 mat(utils->creatematrix4FromMatrix4(env, thiz));
		vector3df vec(utils->createvector3dfFromVector3d(env, jvec));
		mat.transformVect(vec);
		utils->setMatrix4Frommatrix4(env, thiz, mat);
	}
	
	void Java_zte_irrlib_core_Matrix4_nativeMakeInverse(
		JNIEnv *env, jobject thiz)
	{
		matrix4 mat(utils->creatematrix4FromMatrix4(env, thiz));
		mat.makeInverse();
		utils->setMatrix4Frommatrix4(env, thiz, mat);
	}
	
	void Java_zte_irrlib_core_Matrix4_nativeTranspose(
		JNIEnv *env, jobject thiz)
	{
		matrix4 mat(utils->creatematrix4FromMatrix4(env, thiz));
		mat = mat.getTransposed();
		utils->setMatrix4Frommatrix4(env, thiz, mat);
	}
}