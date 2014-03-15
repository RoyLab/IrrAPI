#include <jni.h>
#include <irrlicht.h>
#include "android-global.h"

#ifdef LOG_TAG
#undef LOG_TAG
#endif

#define LOG_TAG "NativeParticleSystemSceneNode"

using namespace irr;
using namespace core;
using namespace core;
using namespace scene;
using namespace video;
using namespace io;
using namespace gui;

extern IrrlichtDevice *device;
extern IVideoDriver* driver;
extern ISceneManager* smgr;

/*class Param in ParticleSystemSceneNode.java
public class Param{
		double BBox[];
		Vector3d InitialDirection;
		double MinEmitRate, MaxEmitRate;
		Color4i DarkestColor, BrightestColor;
		double MinLifeTime, MaxLifeTime;
		double MaxAngleDegrees;
		Vector2d MinSize, MaxSize;
	}
*/

#define NUM 11

extern "C"
{
	static jfieldID id_field[NUM];
	static bool isInitEmitter = false;

	static void initFieldIdEmitter(JNIEnv *env, jobject emitter_obj)
	{
		if(isInitEmitter) return;
		char **name = new char*[NUM];
		char **type = new char*[NUM];
		for(int i=0;i<NUM;++i){
			name[i] = new char[32];
			type[i] = new char[32];
		}

		strcpy(name[0],"BBox");	strcpy(type[0],"[D");
		strcpy(name[1],"InitialDirection");	strcpy(type[1],"Lzte/irrlib/core/Vector3d;");
		strcpy(name[2],"MinEmitRate");	strcpy(type[2],"I");
		strcpy(name[3],"MaxEmitRate");	strcpy(type[3],"I");
		strcpy(name[4],"DarkestColor");	strcpy(type[4],"Lzte/irrlib/core/Color4i;");
		strcpy(name[5],"BrightestColor");	strcpy(type[5],"Lzte/irrlib/core/Color4i;");
		strcpy(name[6],"MinLifeTime");	strcpy(type[6],"I");
		strcpy(name[7],"MaxLifeTime");	strcpy(type[7],"I");
		strcpy(name[8],"MaxAngleDegrees");	strcpy(type[8],"I");
		strcpy(name[9],"MinSize");	strcpy(type[9],"Lzte/irrlib/core/Vector2d;");
		strcpy(name[10],"MaxSize");	strcpy(type[10],"Lzte/irrlib/core/Vector2d;");
		
		jclass cls_emitter = env->GetObjectClass(emitter_obj);
		for (int i = 0; i < NUM; i++)
			id_field[i] =  env->GetFieldID(cls_emitter, name[i], type[i]);
		
		for (int i = 0; i < NUM; i++)
		{
			delete [] name[i];
			delete [] type[i];
		}
		delete name, type;
		LOGE("NativeSetEmitter: initFieldID end!");
		isInitEmitter = true;
	}

	void Java_zte_irrlib_scene_ParticleSystemSceneNode_nativeSetBoxEmitter(
		JNIEnv *env, jobject defaultObj, jobject emitter_obj, jint id)
	{
		initFieldIdEmitter(env, emitter_obj);
		
		scene::IParticleSystemSceneNode* ps = 
			(scene::IParticleSystemSceneNode*)smgr->getSceneNodeFromId(id);
		
		if (!ps)
		{
			WARN_NODE_NOT_FOUND(id, SetEmitter);
			return;
		}

		jdoubleArray bboxArr = (jdoubleArray)(env->GetObjectField(emitter_obj,id_field[0]));
		jdouble* bboxVal = env->GetDoubleArrayElements(bboxArr,NULL);
		jsize len = env->GetArrayLength(bboxArr);
		if(len<5){
			LOGE("NativeSetEmitter: bbox index error! ");
			return;		//no enough elements for bbox
		}
		
		core::aabbox3d<f32> bbox = core::aabbox3d<f32>(
				bboxVal[0],bboxVal[1],bboxVal[2],bboxVal[3],bboxVal[4],bboxVal[5]);
		env->ReleaseDoubleArrayElements(bboxArr,bboxVal,JNI_ABORT);
		
		vector3df initDir = createvector3dfFromVector3d(env, env->GetObjectField(emitter_obj, id_field[1]));
		jint minRate = (jdouble)env->GetIntField(emitter_obj,id_field[2]);
		jint maxRate = (jdouble)env->GetIntField(emitter_obj,id_field[3]);
		SColor darkColor = createSColorFromColor4i(env,env->GetObjectField(emitter_obj,id_field[4]));
		SColor brightColor = createSColorFromColor4i(env,env->GetObjectField(emitter_obj,id_field[5]));
		jint minTime = (jdouble)env->GetIntField(emitter_obj,id_field[6]);
		jint maxTime = (jdouble)env->GetIntField(emitter_obj,id_field[7]);
		jint maxAngle = (jdouble)env->GetIntField(emitter_obj,id_field[8]);
		dimension2df minSize = createdimension2dfFromVector2d(env,env->GetObjectField(emitter_obj,id_field[9]));
		dimension2df maxSize = createdimension2dfFromVector2d(env,env->GetObjectField(emitter_obj,id_field[10]));
		LOGE("NativeSetEmitter: param inti end!");
		scene::IParticleEmitter* em = ps->createBoxEmitter(
			bbox, 								// emitter size
			initDir,							// initial direction
			minRate,maxRate,                    // emit rate
			darkColor,       					// darkest color
			brightColor,       					// brightest color
			minTime,maxTime,maxAngle,           // min and max age, angle
			minSize, 							// min size
			maxSize								// max size 
		);
		ps->setEmitter(em);
		em->drop();
	}

	int Java_zte_irrlib_scene_ParticleSystemSceneNode_nativeAllSetTexture(
		JNIEnv *env, jobject defaultObj, jstring path, jint id)
	{
		const char *msg = env->GetStringUTFChars(path,0);
		core::stringc file = msg;
		scene::IParticleSystemSceneNode* ps =
					(scene::IParticleSystemSceneNode*)smgr->getSceneNodeFromId(id);
		if (!ps)
		{
			WARN_NODE_NOT_FOUND(id, SetAllTexture);
			return -1;
		}
		ps->setMaterialTexture(0,driver->getTexture(file.c_str()));
		env->ReleaseStringUTFChars(path,msg);
		return 0;
	}

	void Java_zte_irrlib_scene_ParticleSystemSceneNode_nativeAddAttractionParticleAffector(
			JNIEnv *env, jobject defaultObj, jdouble x, jdouble y, jdouble z, jint speed, jboolean attract,
			jboolean affectX, jboolean affectY, jboolean affectZ, jint id){
		scene::IParticleSystemSceneNode* ps = (scene::IParticleSystemSceneNode*)smgr->getSceneNodeFromId(id);
		if (!ps){
			WARN_NODE_NOT_FOUND(id, AddAttractionParticleAffector);
			return;
		}
		IParticleAffector* paf = ps->createAttractionAffector(vector3df(x,y,z), speed, attract, affectX, affectY, affectZ);
		ps->addAffector(paf);
		paf->drop();
	}

	void Java_zte_irrlib_scene_ParticleSystemSceneNode_nativeAddScaleParticleAffector(
			JNIEnv *env, jobject defaultObj, jdouble x, jdouble y, jint id){
		scene::IParticleSystemSceneNode* ps = (scene::IParticleSystemSceneNode*)smgr->getSceneNodeFromId(id);
		if (!ps){
			WARN_NODE_NOT_FOUND(id, AddScaleParticleAffector);
			return;
		}
		IParticleAffector* paf = ps->createScaleParticleAffector( dimension2df(x,y));
		ps->addAffector(paf);
		paf->drop();
	}

	void Java_zte_irrlib_scene_ParticleSystemSceneNode_nativeAddFadeOutAffectorParticleAffector(
			JNIEnv *env, jobject defaultObj, jint  r, jint g, jint b, jint a, jint time, jint id){
		scene::IParticleSystemSceneNode* ps = (scene::IParticleSystemSceneNode*)smgr->getSceneNodeFromId(id);
		if (!ps){
			WARN_NODE_NOT_FOUND(id, AddFadeOutAffectorParticleAffector);
			return;
		}
		IParticleAffector* paf = ps->createFadeOutParticleAffector(SColor(a,r,g,b), time);
		ps->addAffector(paf);
		paf->drop();
	}

	void Java_zte_irrlib_scene_ParticleSystemSceneNode_nativeAddGravityAffector(
			JNIEnv *env, jobject defaultObj, jdouble x, jdouble y, jdouble z, jint time,  jint id){
		scene::IParticleSystemSceneNode* ps = (scene::IParticleSystemSceneNode*)smgr->getSceneNodeFromId(id);
		if (!ps){
			WARN_NODE_NOT_FOUND(id, AddGravityAffector);
			return;
		}
		IParticleAffector* paf = ps->createGravityAffector(vector3df(x,y,z), time);
		ps->addAffector(paf);
		paf->drop();
	}

	void Java_zte_irrlib_scene_ParticleSystemSceneNode_nativeAddRotationAffector(
			JNIEnv *env, jobject defaultObj, jdouble sx, jdouble sy, jdouble sz, jdouble cx, jdouble cy, jdouble cz, jint id){
		scene::IParticleSystemSceneNode* ps = (scene::IParticleSystemSceneNode*)smgr->getSceneNodeFromId(id);
		if (!ps){
			WARN_NODE_NOT_FOUND(id, AddRotationAffector);
			return;
		}
		IParticleAffector* paf = ps->createRotationAffector(vector3df(sx,sy,sz), vector3df(cx, cy, cz));
		ps->addAffector(paf);
		paf->drop();
	}
}


