#include <jni.h>
#include <irrlicht.h>
#include "android-global.h"

#ifdef LOG_TAG
#undef LOG_TAG
#endif

#define LOG_TAG "NativeNode"

extern "C"
{
	int Java_zte_irrlib_scene_SceneNode_nativeSetParent(
		JNIEnv *env, jobject defaultObj, jint parent, jint id)
	{
		//LOGD("PARENT %d", id);
		ISceneNode *node = smgr->getSceneNodeFromId(id);
		ISceneNode *parentNode = NULL;
		
		if (!node)
		{
			WARN_NODE_NOT_FOUND(id, SetParent);
			return -1;
		}
		
		if (parent == 0)
		{
			node->setParent(smgr->getRootSceneNode());
		}
		else 
		{
			parentNode = smgr->getSceneNodeFromId(parent);
			if (!parentNode)
			{
				WARN_PARENT_NOT_FOUND(id, SetParent);
				return -2;
			}
			node->setParent(parentNode);
		}
		return 0;
	}
	
	int Java_zte_irrlib_scene_SceneNode_nativeSetVisible(
		JNIEnv*  env, jobject defaultObj, jboolean isVisible, jint id)
	{
		ISceneNode* node = smgr->getSceneNodeFromId(id);
		//LOGD("VISIBLE %d", id);
		if (!node) 
		{
			WARN_NODE_NOT_FOUND(id, SetVisible);
			return -1;
		}
		node->setVisible(isVisible);
		return 0;
	}
	
	int Java_zte_irrlib_scene_SceneNode_nativeSetRotation(
		JNIEnv*  env, jobject defaultObj, jdouble x, jdouble y, jdouble z, jint id)
	{
		ISceneNode* node = smgr->getSceneNodeFromId(id);
		//LOGD("ROTAION %d", id);
		if (!node) 
		{
			WARN_NODE_NOT_FOUND(id, SetRotation);
			return -1;
		}
		node->setRotation(core::vector3df(x,y,z));
		return 0;
	}
	
	int Java_zte_irrlib_scene_SceneNode_nativeSetScale(
		JNIEnv*  env, jobject defaultObj, jdouble x, jdouble y, jdouble z, jint id)
	{
		//LOGD("SCALE %d", id);
		ISceneNode* node = smgr->getSceneNodeFromId(id);
		if (!node) 
		{
			WARN_NODE_NOT_FOUND(id, SetScale);
			return -1;
		}
		node->setScale(core::vector3df(x,y,z));
		return 0;
	}
	
	int Java_zte_irrlib_scene_SceneNode_nativeSetPosition(
		JNIEnv*  env, jobject defaultObj, jdouble x, jdouble y, jdouble z, jint id)
	{
		ISceneNode* node = smgr->getSceneNodeFromId(id);
		//LOGD("POSITION %d", id);
		if (!node) 
		{
			WARN_NODE_NOT_FOUND(id, SetPosition);
			return -1;
		}
		node->setPosition(core::vector3df(x,y,z));
		return 0;
	}

	int Java_zte_irrlib_scene_SceneNode_nativeAddRotationAnimator(
		JNIEnv*  env, jobject defaultObj, jdouble x, jdouble y, jdouble z,
		jint id)
	{
		ISceneNode* node = smgr->getSceneNodeFromId(id);
		if (!node) 
		{
			WARN_NODE_NOT_FOUND(id, AddRotationAnimator);
			return -1;
		}
		ISceneNodeAnimator* anim = smgr->createRotationAnimator(vector3df(x,y,z));
		node->addAnimator(anim);
		anim->drop();
		return 0;
	}
	
	int Java_zte_irrlib_scene_SceneNode_nativeAddFlyCircleAnimator(
		JNIEnv*  env, jobject defaultObj, jdouble cx, jdouble cy, jdouble cz,
		jdouble radius, jdouble speed, jdouble ax, jdouble ay, jdouble az, 
		jdouble startPosition, jdouble radiusEllipsoid, jint id)
	{
		ISceneNode* node = smgr->getSceneNodeFromId(id);
		if (!node) 
		{
			WARN_NODE_NOT_FOUND(id, AddFlyCircleAnimator);
			return -1;
		}		
		
		ISceneNodeAnimator* anim = 
			smgr->createFlyCircleAnimator(vector3df(cx,cy,cz),radius,speed,vector3df(ax, ay, az), startPosition, radiusEllipsoid);
		node->addAnimator(anim);
		anim->drop();
		return 0;
	}
	
	int Java_zte_irrlib_scene_SceneNode_nativeAddFlyStraightAnimator(
		JNIEnv*  env, jobject defaultObj, jdouble sx, jdouble sy, jdouble sz,
		jdouble dx, jdouble dy, jdouble dz, jdouble time, 
		jboolean loop, jboolean pingpong, jint id)
	{
		ISceneNode* node = smgr->getSceneNodeFromId(id);
		if (!node) 
		{
			WARN_NODE_NOT_FOUND(id, AddFlyStraightAnimator);
			return -1;
		}			
		vector3df start(sx,sy,sz);
		vector3df end(dx, dy, dz);
		
		ISceneNodeAnimator* anim = smgr->createFlyStraightAnimator (start, end, time, loop, pingpong);
		node->addAnimator(anim);
		anim->drop();
		return 0;
	}
	
	int Java_zte_irrlib_scene_SceneNode_nativeAddDeleteAnimator(
		JNIEnv *env, jobject defaultObj, jint ms, jint id)
	{
		ISceneNode* node = smgr->getSceneNodeFromId(id);
		if (!node) 
		{
			WARN_NODE_NOT_FOUND(id, AddDeleteAnimator);
			return -1;
		}				
		ISceneNodeAnimator* anim = smgr->createDeleteAnimator(ms);
		node->addAnimator(anim);
		anim->drop();
		return 0;
	}
	
	int Java_zte_irrlib_scene_SceneNode_nativeAddCollisionResponseAnimator(
		JNIEnv *env, jobject defaultObj, jint selId, jobject jradius, jboolean bbox, jboolean octree, jint id)
	{
		ISceneNode* selNode = smgr->getSceneNodeFromId(selId);
		if (!selNode)
		{
			WARN_NODE_NOT_FOUND(selId, AddCollisionResponseAnimator);
			return -1;
		}
		
		ISceneNode* node = smgr->getSceneNodeFromId(id);
		if (!node)
		{
			WARN_NODE_NOT_FOUND(id, AddCollisionResponseAnimator);
			return -1;
		}
		
		ITriangleSelector* selector = 0;
		if (bbox) selector = smgr->createTriangleSelectorFromBoundingBox(selNode);
		else if (!octree) selector = smgr->createTriangleSelector(((IMeshSceneNode*)selNode)->getMesh(), selNode);
		else selector = smgr->createTriangleSelector(((IMeshSceneNode*)selNode)->getMesh(), selNode);
		
		node->updateAbsolutePosition();
		const aabbox3d<f32>& box = node->getTransformedBoundingBox();
		
		vector3df radius;
		if (jradius == 0)
			radius = box.MaxEdge-box.getCenter();
		else radius = utils->createvector3dfFromVector3d(env, jradius);
		
		vector3df translation = -(box.getCenter() - node->getAbsolutePosition());
		
		//LOGD("max, %f, %f, %f", box.MaxEdge.X, box.MaxEdge.Y, box.MaxEdge.Z);
		//LOGD("min, %f, %f, %f", box.MinEdge.X, box.MinEdge.Y, box.MinEdge.Z);
		//LOGD("trans, %f, %f, %f", translation.X, translation.Y, translation.Z);
		ISceneNodeAnimator* anim = smgr->createCollisionResponseAnimator(selector, node,
			radius,
			vector3df(),/*gravity*/
			translation,
			0.0005f
		);
		selector->drop();
		node->addAnimator(anim);
		anim->drop();
	}

	int Java_zte_irrlib_scene_SceneNode_nativeRemoveAllAnimator(
		JNIEnv *env, jobject defaultObj, jint id)
	{
		ISceneNode* node = smgr->getSceneNodeFromId(id);
		if (!node) 
		{
			WARN_NODE_NOT_FOUND(id, RemoveAllAnimator);
			return -1;
		}				
		node->removeAnimators();
		return 0;
	}

	int Java_zte_irrlib_scene_SceneNode_nativeRemoveLastAnimator(
		JNIEnv *env, jobject defaultObj, jint id)
	{
		ISceneNode* node = smgr->getSceneNodeFromId(id);
		if (!node) 
		{
			WARN_NODE_NOT_FOUND(id, RemoveAllAnimator);
			return -1;
		}
		
		const core::list<ISceneNodeAnimator*> anims = node->getAnimators();
		if (anims.empty()) return -2;
		node->removeAnimator(*(anims.getLast()));
		return 0;
	}
	
	int Java_zte_irrlib_scene_SceneNode_nativeCreateEmptySceneNode(
		JNIEnv*  env, jobject defaultObj, jint id, jboolean isLight)
	{
		ISceneNode* node = smgr->addEmptySceneNode(0, id);

		if (node)
		{
			if (!isLight) node->setMaterialFlag(video::EMF_LIGHTING, false);
			return 0;
		}
		else 
		{
			ERROR_ADD_FAILD(id, CreateEmptySceneNode);
			return -1;
		}
	}
	
	int Java_zte_irrlib_scene_SceneNode_nativeCloneNode(
		JNIEnv*  env, jobject defaultObj, jint res, jint des)
	{
		ISceneNode* node = smgr->getSceneNodeFromId(res);
		if (!node) 
		{
			WARN_NODE_NOT_FOUND(res, nativeCloneNode);
			return -1;
		}
		
		ISceneNode *copy = node->clone();
		copy->setID(des);
		return 0;
	}
	
		
	int Java_zte_irrlib_scene_SceneNode_nativeChangeId(
		JNIEnv*  env, jobject defaultObj, jint res, jint des, jint parent)
	{
		ISceneNode* parentNode = smgr->getSceneNodeFromId(parent);
		ISceneNode* node = smgr->getSceneNodeFromId(res, parentNode);
		if (!node) 
		{
			WARN_NODE_NOT_FOUND(res, nativeChangeId);
			return -1;
		}
		node->setID(des);
		return 0;
	}

	int Java_zte_irrlib_scene_SceneNode_nativeUpdatePosition(
		JNIEnv*  env, jobject defaultObj, jobject jvec, jboolean isAbsolute, jint Id)
	{
		ISceneNode* node = smgr->getSceneNodeFromId(Id);
		if (!node) 
		{
			WARN_NODE_NOT_FOUND(Id, UpdatePosition);
			return -1;
		}
		if (isAbsolute)
		{
			node->updateAbsolutePosition();
			utils->setVector3dFromvector3df(env, jvec, node->getAbsolutePosition());
		}
		else utils->setVector3dFromvector3df(env, jvec, node->getPosition());
		return 0;
	}
	
	
}
