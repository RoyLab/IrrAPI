#include <sys/time.h>
#include <string.h>
#include "android-global.h"
#include <android/bitmap.h>
#include "COGLESTexture.h"

using namespace irr;
using namespace core;
using namespace scene;
using namespace video;
using namespace io;
using namespace gui;

IrrlichtDevice *device = 0;
IVideoDriver* driver = 0;
ISceneManager* smgr = 0;
JNIUtils *utils = 0;

int gWindowWidth = 640;
int gWindowHeight = 480;
stringc gSdCardPath;
SColor backColor = SColor(255,150,150,150);

bool _isInit = false;
const char _extPrefix[] = "<external>";
char _builtInFontPath[128] = "";
ITexture *_extTex = 0;


JavaClassInfo::JavaClassInfo(const JavaClassInfo& other):
	count(other.count), Sig(new char[128]),
	FieldID(new jfieldID[count])
{
	memcpy(Sig, other.Sig, 128);
	memcpy(FieldID, other.FieldID, other.count * sizeof(jfieldID));
	//LOGD("%s, %d", Sig, other.count * sizeof(jfieldID));
	//LOGD("%d, %d, %d", sizeof(FieldID), &FieldID[0], &FieldID[1]);
}

JNIUtils::JNIUtils():clsArray(1)
{

}

void JNIUtils::initJNIClass(JNIEnv *env, jstring clsName, 
	jobjectArray fname, jobjectArray fsig, int num)
{
	JavaClassInfo info;
	const char *name = env->GetStringUTFChars(clsName, 0);
	strcpy(info.Sig, name);
	//LOGD("%s", info.Sig);
	env->ReleaseStringUTFChars(clsName, name);
	
	jclass cls = env->FindClass(info.Sig);
	info.count = num;
	info.FieldID = new jfieldID[info.count];
	
	//will there be any memory leak?
	for (int i = 0; i < info.count; i++)
	{	
		jstring tfname = (jstring)env->GetObjectArrayElement(fname, i);
		jstring tfsig = (jstring)env->GetObjectArrayElement(fsig, i);
		const char *tmp1 = env->GetStringUTFChars(tfname, 0);
		const char *tmp2 = env->GetStringUTFChars(tfsig, 0);
		//LOGD("%s4 %d, %s, %s", info.Sig, i, tmp1, tmp2);
		info.FieldID[i] = env->GetFieldID(cls, tmp1, tmp2);
		env->ReleaseStringUTFChars(tfname, tmp1);
		env->ReleaseStringUTFChars(tfsig, tmp2);
	}
	//LOGD("push start.%s, %d", info.Sig, &clsArray);
	clsArray.push_back(&info);
	//LOGD("push end");
}

const JavaClassInfo* JNIUtils::getClassInfo(const char* name)
{
	for (int i = 0; i < clsArray.size(); i++)
	{
		if (strstr(clsArray[i]->Sig, name) != 0)
			return clsArray[i];
	}
	return 0;
}

vector3df JNIUtils::createvector3dfFromVector3d(JNIEnv *env, jobject vec)
{
	const JavaClassInfo* info = getClassInfo("Vector3d");
	return vector3df(
		env->GetDoubleField(vec, info->FieldID[0]), 
		env->GetDoubleField(vec, info->FieldID[1]), 
		env->GetDoubleField(vec, info->FieldID[2])
		);
}

SColorf JNIUtils::createSColorfFromColor3i(JNIEnv *env, jobject color)
{
	const JavaClassInfo* info = getClassInfo("Color3i");
	return SColorf(
		env->GetIntField(color, info->FieldID[0])/255.0,
		env->GetIntField(color, info->FieldID[1])/255.0,
		env->GetIntField(color, info->FieldID[2])/255.0,
		1.0);
}

SColor JNIUtils::createSColorFromColor3i(JNIEnv *env, jobject color)
{
	const JavaClassInfo* info = getClassInfo("Color3i");
	return SColor(0xff,
		env->GetIntField(color, info->FieldID[0]),
		env->GetIntField(color, info->FieldID[1]),
		env->GetIntField(color, info->FieldID[2])
		);
}

SColor JNIUtils::createSColorFromColor4i(JNIEnv *env, jobject color)
{
	const JavaClassInfo* info = getClassInfo("Color4i");
	return SColor(
		env->GetIntField(color, info->FieldID[3]),
		env->GetIntField(color, info->FieldID[0]),
		env->GetIntField(color, info->FieldID[1]),
		env->GetIntField(color, info->FieldID[2])
		);
}

recti JNIUtils::createrectiFromRect4i(JNIEnv *env, jobject rec)
{
	const JavaClassInfo* info = getClassInfo("Rect4i");
	return recti(
		env->GetIntField(rec, info->FieldID[0]), 
		env->GetIntField(rec, info->FieldID[1]),
		env->GetIntField(rec, info->FieldID[2]), 
		env->GetIntField(rec, info->FieldID[3])
		);
}

dimension2df JNIUtils::createdimension2dfFromVector2d(JNIEnv *env, jobject vec)
{
	const JavaClassInfo* info = getClassInfo("Vector2d");
	return dimension2df(
		env->GetDoubleField(vec, info->FieldID[0]), 
		env->GetDoubleField(vec, info->FieldID[1])
		);
}

void JNIUtils::setVector3dFromvector3df(JNIEnv *env, jobject obj, const vector3df& vec)
{
	const JavaClassInfo* info = getClassInfo("Vector3d");
	env->SetDoubleField(obj, info->FieldID[0], vec.X);
	env->SetDoubleField(obj, info->FieldID[1], vec.Y);
	env->SetDoubleField(obj, info->FieldID[2], vec.Z);
}

void JNIUtils::setColor3iFromSColorf(JNIEnv *env, jobject obj, const SColorf& color)
{
	const JavaClassInfo* info = getClassInfo("Color3i");
	env->SetIntField(obj, info->FieldID[0], color.r*255);
	env->SetIntField(obj, info->FieldID[1], color.g*255);
	env->SetIntField(obj, info->FieldID[2], color.b*255);
}

void JNIUtils::setBoundingBoxFromaabbox3df(JNIEnv *env, jobject bbox, const aabbox3df& bboxorig)
{
	LOGD("11");
	const JavaClassInfo* info = getClassInfo("BoundingBox");
	LOGD("11");
	setVector3dFromvector3df(env, 
		env->GetObjectField(bbox, info->FieldID[0]), bboxorig.MinEdge);
	LOGD("11");
	setVector3dFromvector3df(env, 
		env->GetObjectField(bbox, info->FieldID[1]), bboxorig.MaxEdge);
}


IImage* JNIUtils::createImageFromBitmap(JNIEnv* env, jobject jbitmap)
{
	if (device && !driver)
		driver = device->getVideoDriver();
		
	if (!driver)
	{
		LOGE("Driver not set!");
		return 0;
	}

	AndroidBitmapInfo bitmapInfo;
	int result = 0;
	if ((result = AndroidBitmap_getInfo(env, jbitmap, &bitmapInfo)) < 0)
	{
		LOGE("AndroidBitmap_getInfo() failed! error = %d", result);
		return 0;
	}
	
	ECOLOR_FORMAT format = ECF_UNKNOWN;
	switch (bitmapInfo.format)
	{
	case ANDROID_BITMAP_FORMAT_RGB_565:
		//LOGD("565");
		format = ECF_R5G6B5;
		break;
	case ANDROID_BITMAP_FORMAT_RGBA_8888:
		format = ECF_A8R8G8B8;
		//LOGD("A");
		break;
	default: break;
	}
	
	if (format == ECF_UNKNOWN)
	{
		LOGE("Unsupported color format!");
		return 0;
	}
	
	void *pixels = 0;
	
	if ((result = AndroidBitmap_lockPixels(env, jbitmap, &pixels)) < 0 || pixels == 0)
	{
		LOGE("AndroidBitmap_lockPixels() failed! error = %d", result);
		return 0;
	}
	
	IImage* image = driver->createImageFromData(format, 
		dimension2d<u32>(bitmapInfo.width, bitmapInfo.height), pixels);
		
	return image;
}


static jclass cls_color4i;
static jfieldID id_red, id_green, id_blue, id_alpha;

static jclass cls_color3i;
static jfieldID id_red3, id_green3, id_blue3;

static jclass cls_rect4i;
static jfieldID id_left, id_top, id_right, id_bottom;

static jclass cls_vector3d;
static jfieldID id_vx, id_vy, id_vz;

static jclass cls_bbox;
static jfieldID id_min, id_max;

void setVector3dFromvector3df(JNIEnv *env, jobject obj, const vector3df& vec)
{
	env->SetDoubleField(obj, id_vx, vec.X);
	env->SetDoubleField(obj, id_vy, vec.Y);
	env->SetDoubleField(obj, id_vz, vec.Z);
}

void setColor3iFromSColorf(JNIEnv *env, jobject obj, const SColorf& color)
{
	env->SetIntField(obj, id_red3, color.r*255);
	env->SetIntField(obj, id_green3, color.g*255);
	env->SetIntField(obj, id_blue3, color.b*255);
}

void setBoundingBoxFromaabbox3df(JNIEnv *env, jobject bbox, const aabbox3df& bboxorig)
{
	jobject obj1 = env->GetObjectField(bbox, id_min);
	setVector3dFromvector3df(env, obj1, bboxorig.MinEdge);
	jobject obj2 = env->GetObjectField(bbox, id_max);
	setVector3dFromvector3df(env, obj2, bboxorig.MaxEdge);
}

SColorf createSColorfFromColor3i(JNIEnv *env, jobject color)
{
	return SColorf(env->GetIntField(color, id_red3)/255.0, env->GetIntField(color, id_green3)/255.0,
			 env->GetIntField(color, id_blue3)/255.0, 1.0);
}

SColor createSColorFromColor3i(JNIEnv *env, jobject color)
{
	return SColor(0xff, env->GetIntField(color, id_red), 
			env->GetIntField(color, id_green), env->GetIntField(color, id_blue));
}

SColor createSColorFromColor4i(JNIEnv *env, jobject color)
{
	return SColor(env->GetIntField(color, id_alpha), env->GetIntField(color, id_red), 
			env->GetIntField(color, id_green), env->GetIntField(color, id_blue));
}

recti createrectiFromRect4i(JNIEnv *env, jobject rec)
{
	return recti(env->GetIntField(rec, id_left), env->GetIntField(rec, id_top),
			env->GetIntField(rec, id_right), env->GetIntField(rec, id_bottom));
}

vector3df createvector3dfFromVector3d(JNIEnv *env, jobject vec)
{
	return vector3df(env->GetDoubleField(vec, id_vx), env->GetDoubleField(vec, id_vy),
			env->GetDoubleField(vec, id_vz));
}

dimension2df createdimension2dfFromVector2d(JNIEnv *env, jobject vec)
{
	return dimension2df(env->GetDoubleField(vec, id_vx), env->GetDoubleField(vec, id_vy));
}

void initJNIInfo(JNIEnv *env, jobject vector, jobject color4, jobject color3, jobject rect)
{
	cls_vector3d = env->GetObjectClass(vector);
	cls_color4i = env->GetObjectClass(color4);
	cls_color3i = env->GetObjectClass(color3);
	cls_rect4i = env->GetObjectClass(rect);
	
	id_vx = env->GetFieldID(cls_vector3d, "X", "D");
	id_vy = env->GetFieldID(cls_vector3d, "Y", "D");
	id_vz = env->GetFieldID(cls_vector3d, "Z", "D");
	
	id_red = env->GetFieldID(cls_color4i, "red", "I");
	id_green = env->GetFieldID(cls_color4i, "green", "I");
	id_blue = env->GetFieldID(cls_color4i, "blue", "I");
	id_alpha = env->GetFieldID(cls_color4i, "alpha", "I");
	
	id_red3 = env->GetFieldID(cls_color3i, "red", "I");
	id_green3 = env->GetFieldID(cls_color3i, "green", "I");
	id_blue3 = env->GetFieldID(cls_color3i, "blue", "I");
	
	id_left = env->GetFieldID(cls_rect4i, "Left", "I");
	id_top = env->GetFieldID(cls_rect4i, "Top", "I");
	id_right = env->GetFieldID(cls_rect4i, "Right", "I");
	id_bottom = env->GetFieldID(cls_rect4i, "Bottom", "I");
}

void initBoundingBoxId(JNIEnv *env, jobject thiz)
{
	cls_bbox = env->GetObjectClass(thiz);
	id_min = env->GetFieldID(cls_bbox, "MinEdge", "Lzte/irrlib/core/Vector3d;");
	id_max = env->GetFieldID(cls_bbox, "MaxEdge", "Lzte/irrlib/core/Vector3d;");
}

long _getTime()
{
    struct timeval  now;

    gettimeofday(&now, NULL);
    return (long)(now.tv_sec*1000 + now.tv_usec/1000);
}

void setgSdCardPath( JNIEnv* env, jstring newpath )
{
	const char* text = env->GetStringUTFChars(newpath, 0);
	gSdCardPath = text;
	env->ReleaseStringUTFChars( newpath, text );
}

//not safe, will cause fatal error when it is not a opengles texture.
int getOpenGLESTextureID(const ITexture* tex)
{
	return ((COGLES1Texture*)tex)->getOGLES1TextureName();
}
