#include <jni.h>
#include <irrlicht.h>
#include "android-global.h"

#ifdef LOG_TAG
#undef LOG_TAG
#endif

#define LOG_TAG "NativeCamera"

extern "C"
{
	int Java_zte_irrlib_scene_CameraSceneNode_nativeSetLookAt(
		JNIEnv *env, jobject defaultObj, jdouble x, jdouble y, jdouble z, jint id)
	{
		scene::ICameraSceneNode* camera = 
			(scene::ICameraSceneNode*)smgr->getSceneNodeFromId(id);
		
		if(!camera){
			WARN_NODE_NOT_FOUND(id, SetLookAt);	
			return -1;
		}
		camera->setTarget(core::vector3df(x,y,z));
		return 0;
	}

	int Java_zte_irrlib_scene_CameraSceneNode_nativeSetClipPlane(
		JNIEnv *env, jobject defaultObj, jdouble nearClip, jdouble farClip, jint id)
	{
		scene::ICameraSceneNode* camera =
			(scene::ICameraSceneNode*)smgr->getSceneNodeFromId(id);
		if(!camera){
			WARN_NODE_NOT_FOUND(id, SetClipPlane);	
			return -1;
		}
		camera->setNearValue(nearClip);
		camera->setFarValue(farClip);
		return 0;
	}

	int Java_zte_irrlib_scene_CameraSceneNode_nativeSetAspectRatio(
		JNIEnv *env, jobject defaultObj, jdouble ratio, jint id)
	{
		scene::ICameraSceneNode* camera =
			(scene::ICameraSceneNode*)smgr->getSceneNodeFromId(id);
			
		if(!camera){
			WARN_NODE_NOT_FOUND(id, SetAspectRatio);	
			return -1;
		}
		camera->setAspectRatio(ratio);
		return 0;
	}

	int Java_zte_irrlib_scene_CameraSceneNode_nativeSetFovy(
		JNIEnv *env, jobject defaultObj, jdouble fovy, jint id)
	{
		scene::ICameraSceneNode* camera =
			(scene::ICameraSceneNode*)smgr->getSceneNodeFromId(id);
			
		if(!camera){
			WARN_NODE_NOT_FOUND(id, SetFovy);	
			return -1;
		}
		camera->setFOV(fovy);
		return 0;
	}
	
	int Java_zte_irrlib_scene_CameraSceneNode_nativeSetUpVector(
		JNIEnv *env, jobject defaultObj, jobject upVec, jint id)
	{
		scene::ICameraSceneNode* camera =
			(scene::ICameraSceneNode*)smgr->getSceneNodeFromId(id);
			
		if(!camera){
			WARN_NODE_NOT_FOUND(id, SetUpVector);	
			return -1;
		}
		camera->setUpVector(utils->createvector3dfFromVector3d(env, upVec));
		return 0;
	}
}