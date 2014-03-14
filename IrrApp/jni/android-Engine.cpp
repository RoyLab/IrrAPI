#include <jni.h>
#include <irrlicht.h>
#include "android-global.h"
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
			
			device = 0;
			driver = 0;
			smgr = 0;
			_extTex = 0;
		}
	
		video::E_DRIVER_TYPE videoType =  video::EDT_NULL;
		if (type == 0x00000001) videoType = video::EDT_OGLES1;
		else if (type == 0x00000004) videoType = video::EDT_OGLES2;
		
		//importGLInit();
		
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
	}
	
	void Java_zte_irrlib_Engine_nativeInitJNI(
		JNIEnv *env, jobject thiz, jstring name, 
		jobjectArray fname, jobjectArray fsig, jint num)
	{
		utils->initJNIClass(env, name, fname, fsig, num);
	}
	
	void Java_zte_irrlib_Engine_nativeTest(
		JNIEnv *env, jobject thiz, jobject obj)
	{
		LOGD("test?");
		utils->setBoundingBoxFromaabbox3df(env, obj, aabbox3df());
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
		gWindowWidth  = w;
		gWindowHeight = h;
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
}