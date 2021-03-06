
#ifndef __ANDOIRD_GLOBAL_H_INCLUDED__
#define __ANDOIRD_GLOBAL_H_INCLUDED__

#include <irrlicht.h>
#include <android/log.h>
#include <android/bitmap.h>
#include <jni.h>
#include <android/asset_manager_jni.h>

#define LOG_TAG "irrlicht engine"

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

#define WARN_PARENT_NOT_FOUND(id, funcName) \
	LOGW("Parent Node(id: %d) not found! In Function: %s", id, #funcName)

#define WARN_NODE_NOT_FOUND(id, funcName) \
	LOGW("Node(id: %d) not found! In Function: %s", id, #funcName)
	
#define ERROR_ADD_FAILD(id, funcName) \
	LOGE("Add Node(id: %d) failed! In Function: %s", id, #funcName)

#define INFO_ADD_SUCCEED(id, funcName) \
	LOGI("Add Node(id: %d) success! In Function: %s", id, #funcName)	

using namespace irr;
using namespace core;
using namespace scene;
using namespace video;
using namespace io;
using namespace gui;

long _getTime();
int getOpenGLESTextureID(const ITexture* tex);
void resetGlobalValue();
void setgSdCardPath(JNIEnv* env, jstring newpath);


/*void initJNIInfo(JNIEnv *env, jobject vector, jobject color4, jobject color3, jobject rect);
void initBoundingBoxId(JNIEnv *env, jobject thiz);

IImage* createImageFromBitmap(JNIEnv* env, jobject jbitmap);

recti createrectiFromRect4i(JNIEnv *env, jobject rec);
SColor createSColorFromColor4i(JNIEnv *env, jobject color);
SColor createSColorFromColor3i(JNIEnv *env, jobject color);
SColorf createSColorfFromColor3i(JNIEnv *env, jobject color);
vector3df createvector3dfFromVector3d(JNIEnv *env, jobject vec);
dimension2df createdimension2dfFromVector2d(JNIEnv *env, jobject vec);

void setVector3dFromvector3df(JNIEnv *env, jobject obj, const vector3df& vecorig);
void setColor3iFromSColorf(JNIEnv *env, jobject obj, const SColorf& colororig);
void setBoundingBoxFromaabbox3df(JNIEnv *env, jobject bbox, const aabbox3df& bboxorig);*/

extern IrrlichtDevice *device;
extern IVideoDriver* driver;
extern ISceneManager* smgr;

extern stringc gSdCardPath;
extern video::SColor backColor;

//extern const char _extPrefix[];

extern char _builtInFontPath[];
extern ITexture* _extTex;

struct JavaClassInfo
{
	char *Sig;
	jfieldID *FieldID;
	unsigned short count;
	
	JavaClassInfo();
	//JavaClassInfo(const JavaClassInfo& other);
	~JavaClassInfo();
};

class JNIUtils
{
public:
	JNIUtils();
	~JNIUtils();
	
	void initJNIClass(JNIEnv *env, jstring clsName, 
		jobjectArray fname, jobjectArray fsig, int num);
	
	vector3df createvector3dfFromVector3d(JNIEnv *env, jobject vec);
	
	SColorf createSColorfFromColor3i(JNIEnv *env, jobject color);
	
	SColor createSColorFromColor3i(JNIEnv *env, jobject color);
	
	SColor createSColorFromColor4i(JNIEnv *env, jobject color);
	
	recti createrectiFromRect4i(JNIEnv *env, jobject rec);
	
	matrix4 creatematrix4FromMatrix4(JNIEnv *env, jobject mat);
	
	dimension2df createdimension2dfFromVector2d(JNIEnv *env, jobject vec);
	
	void setMatrix4Frommatrix4(JNIEnv* env, jobject obj, const matrix4& m);
	
	void setVector3dFromvector3df(JNIEnv *env, jobject obj, const vector3df& vec);
	
	void setColor3iFromSColorf(JNIEnv *env, jobject obj, const SColorf& color);
	
	void setBoundingBoxFromaabbox3df(JNIEnv *env, jobject bbox, const aabbox3df& bboxorig);
	
	IImage* createImageFromBitmap(JNIEnv* env, jobject jbitmap);
	
	int getGLTexId(ITexture* tex);
	
	bool isExtTex(ITexture* tex);
	
private:
	
	const JavaClassInfo* getClassInfo(const char* name);
	
	enum {CAPACITY = 10};
	
	JavaClassInfo clsArray[CAPACITY];
	int count;
};

extern JNIUtils *utils;
#endif // __ANDOIRD_GLOBAL_H_INCLUDED__
