#include <jni.h>
#include <irrlicht.h>
#include "android-global.h"

#ifdef LOG_TAG
#undef LOG_TAG
#endif

#define LOG_TAG "NativeMeshNode"

extern "C"
{
	int Java_zte_irrlib_scene_AnimateMeshSceneNode_nativeGetStartFrame(
		JNIEnv *env, jobject defaultObj, jint id)
	{
		scene::IAnimatedMeshSceneNode* node = 
			(IAnimatedMeshSceneNode*)smgr->getSceneNodeFromId(id);
		return node->getStartFrame();
	}
	
	int Java_zte_irrlib_scene_AnimateMeshSceneNode_nativeGetEndFrame(
		JNIEnv *env, jobject defaultObj, jint id)
	{
		scene::IAnimatedMeshSceneNode* node = 
			(IAnimatedMeshSceneNode*)smgr->getSceneNodeFromId(id);
		return node->getEndFrame();
	}
	
	int Java_zte_irrlib_scene_AnimateMeshSceneNode_nativeGetFrameNumber(
		JNIEnv *env, jobject defaultObj, jint id)
	{
		scene::IAnimatedMeshSceneNode* node = 
			(IAnimatedMeshSceneNode*)smgr->getSceneNodeFromId(id);
		return node->getFrameNr();
	}
	
	int Java_zte_irrlib_scene_AnimateMeshSceneNode_nativeSetCurrentFrame(
		JNIEnv *env, jobject defaultObj, jint frame, jint id)
	{
		scene::IAnimatedMeshSceneNode* node = 
			(IAnimatedMeshSceneNode*)smgr->getSceneNodeFromId(id);
		if (!node)
		{
			WARN_NODE_NOT_FOUND(id, SetCurrentFrame);
			return -1;
		}		
		if(frame<0 || frame >= node->getMesh()->getFrameCount())
			return -1;
		node->setCurrentFrame((float)frame);
		return 0;
	}
	
	int Java_zte_irrlib_scene_AnimateMeshSceneNode_nativeSetAnimationSpeed(
		JNIEnv *env, jobject defaultObj, jdouble fps, jint id)
	{
		scene::IAnimatedMeshSceneNode* node = 
			(IAnimatedMeshSceneNode*)smgr->getSceneNodeFromId(id);
		if (!node)
		{
			WARN_NODE_NOT_FOUND(id, SetAnimationSpeed);
			return -1;
		}
		node->setAnimationSpeed(fps);
		return 0;
	}
	
	int Java_zte_irrlib_scene_AnimateMeshSceneNode_nativeSetLoopMode(
		JNIEnv *env, jobject defaultObj, jboolean loop, jint id)
	{
		scene::IAnimatedMeshSceneNode* node = 
			(IAnimatedMeshSceneNode*)smgr->getSceneNodeFromId(id);
		if (!node)
		{
			WARN_NODE_NOT_FOUND(id, SetLoopMode);
			return -1;
		}
		node->setLoopMode(loop);
		return 0;
	}
	
	int Java_zte_irrlib_scene_AnimateMeshSceneNode_nativeSetFromLoop(
		JNIEnv *env, jobject defaultObj, jint begin, jint end, jint id)
	{
		scene::IAnimatedMeshSceneNode* node = 
			(IAnimatedMeshSceneNode*)smgr->getSceneNodeFromId(id);
		if (!node)
		{
			WARN_NODE_NOT_FOUND(id, SetFromLoop);
			return -1;
		}
		node->setFrameLoop(begin, end);
		return 0;
	}
}
