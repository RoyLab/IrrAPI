#include <jni.h>
#include <irrlicht.h>
#include "android-global.h"
#include "CAssetsReader.h"
#include <string.h>

#ifdef LOG_TAG
#undef LOG_TAG
#endif

#define LOG_TAG "NativeEngine"

extern "C"
{
	int Java_zte_irrlib_Engine_nativeCreateDevice(
		JNIEnv *env, jobject thiz, int type)
	{
		if (device) 
		{
			device->drop();
			resetGlobalValue();
		}
	
		video::E_DRIVER_TYPE videoType =  video::EDT_NULL;
		if (type == 0x00000001) videoType = video::EDT_OGLES1;
		else if (type == 0x00000004) videoType = video::EDT_OGLES2;
		
		//importGLInit();
		
		device = createDevice( videoType, 
			dimension2d<u32>(640, 480), 16, false, false, false, 0);

		if (!device)
		{
			LOGE("No device!");
			return -1;
		}
		
		driver = device->getVideoDriver();
		if (!driver){
			LOGE("No driver!"); 
			return -2;
		}
		
		smgr = device->getSceneManager();
		if (!smgr){
			LOGE("No scene manager!");
			return -3;
		}
		
		LOGI("Engine is ready");
		smgr->setAmbientLight(video::SColor(0xff,0x3f,0x3f,0x3f));
		if (!utils)	utils = new JNIUtils();
		
		return 0;
	}
	
	int Java_zte_irrlib_Engine_nativeInitAssetManager(
		JNIEnv *env, jobject thiz, jobject amgr)
	{
		_assetManager = 0;
		_assetManager = AAssetManager_fromJava(env, amgr);
		if (!_assetManager) return -1;
		
		return 0;
	}
	
	jboolean Java_zte_irrlib_Engine_nativeAddAssetsDir(
		JNIEnv *env, jobject thiz, jstring dirname, jboolean ignorePath)
	{
		const char *ch = env->GetStringUTFChars(dirname, 0);
		io::path p(io::PATH_ASSETS); p.append(ch);
		env->ReleaseStringUTFChars(dirname, ch);
		
		return device->getFileSystem()->addFileArchive(p, true, ignorePath);
	}
	
	void Java_zte_irrlib_Engine_nativeRelease(
		JNIEnv *env, jclass clazz)
	{
		if (device) device->drop();
		resetGlobalValue();
		if (utils)
		{
			delete utils;
			utils = 0;
		}
	}
	
	void Java_zte_irrlib_Engine_nativeInitJNI(
		JNIEnv *env, jobject thiz, jstring name, 
		jobjectArray fname, jobjectArray fsig, jint num)
	{
		utils->initJNIClass(env, name, fname, fsig, num);
	}
	
	//for test only
	void Java_zte_irrlib_Engine_nativeTest(
		JNIEnv *env, jobject thiz)
	{
		LOGD("test begin");
		ISceneNode *node = smgr->addCubeSceneNode();
		io::path p(io::PATH_ASSETS); p.append("ext");
		device->getFileSystem()->addFileArchive(p, true, false);
		node->setMaterialTexture(0, driver->getTexture("ext/ext_1.png"));
		node->setMaterialFlag(EMF_LIGHTING, false);
		LOGD("test end");
	}

	void Java_zte_irrlib_Engine_nativeSetFontPath(
		JNIEnv *env, jobject defaultObj, jstring path)
	{
		const char* text = env->GetStringUTFChars(path, 0);
		strcpy(_builtInFontPath, text);
		env->ReleaseStringUTFChars(path, text);
	}
	
	void Java_zte_irrlib_Engine_nativeResize(
		JNIEnv *env, jobject defaultObj, jint w, jint h)
	{
		dimension2d<u32> size(w, h);
		if (driver)
		{
			driver->OnResize(size);
			smgr->getActiveCamera()->setAspectRatio((double)w/h);
			LOGI("Render size changed, width: %d height: %d", w, h);
		}
		else LOGE("Driver is not ready.");
	}
	
	void Java_zte_irrlib_Engine_nativeBeginScene(
		JNIEnv *env, jobject defaultObj)
	{
		if (device)	device->run();
		if (driver) driver->beginScene(true,true,backColor);
	}
	
	void Java_zte_irrlib_Engine_nativeEndScene(
		JNIEnv *env, jobject defaultObj)
	{
		if (driver) driver->endScene();
	}
	
	double Java_zte_irrlib_Engine_nativeGetFPS(
		JNIEnv *env, jobject defaultObj)
	{
		if (!driver) return 0.0;
		return (double)(driver->getFPS());
	}
	
	void Java_zte_irrlib_Engine_nativeSetAssetsPath(
		JNIEnv *env, jobject defaultObj, jstring jname)
	{
		const char *ch = env->GetStringUTFChars(jname,0);
		PATH_ASSETS = ch;
		env->ReleaseStringUTFChars(jname,ch);
	}
	
	/*int Java_zte_irrlib_Engine_nativeInit(
		JNIEnv *env, jobject defaultObj, int type,
		jobject vector, jobject color4, jobject color3, jobject rect, jobject bbox)
	{
		initJNIInfo(env, vector, color4, color3, rect);
		initBoundingBoxId(env, bbox);
		
		video::E_DRIVER_TYPE videoType =  video::EDT_NULL;
		if (type == 0x00000001) videoType = video::EDT_OGLES1;
		else if (type == 0x00000004) videoType = video::EDT_OGLES2;
		
		//importGLInit();
		
		if (device) 
		{
			device->drop();
			
			device = 0;
			driver = 0;
			smgr = 0;
			_extTex = 0;
		}
		
		device = createDevice( videoType, 
			dimension2d<u32>(gWindowWidth, gWindowHeight), 16, false, false, false, 0);

		if (!device)
		{
			LOGE("No device!");
			return -1;
		}
		
		driver = device->getVideoDriver();
		if (!driver){
			LOGE("No driver!"); 
			return -2;
		}
		
		smgr = device->getSceneManager();
		if (!smgr){
			LOGE("No scene manager!");
			return -3;
		}
		
		LOGI("Engine is ready. width: %d, height: %d", gWindowWidth, gWindowHeight);
		smgr->setAmbientLight(video::SColor(0xff,0x3f,0x3f,0x3f));
		LOGD("%d1", &utils);
		if (!utils)	utils = new JNIUtils();
		LOGD("%d2", &utils);
		return 0;
	}*/
}