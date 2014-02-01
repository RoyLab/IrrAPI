#include <stdio.h>
#include <cstdlib>
#include <jni.h>
#include <sys/time.h>
#include <time.h>
#include <android/log.h>
#include <stdint.h>
#include <android-receiver.h>
#include <android-logger.h>
#include <importgl.h>
#include <pthread.h>

#include <irrlicht.h>
#include <os.h>

using namespace irr;

using namespace os;
using namespace core;
using namespace scene;
using namespace video;
using namespace io;
using namespace gui;

extern IrrlichtDevice *device;
extern IVideoDriver* driver;
extern video::SColor backColor;

void initMovie()
{
	ICameraSceneNode* camera = NULL;
	ISceneManager* smgr = NULL;
	smgr = device->getSceneManager();
	smgr->addCameraSceneNodeFPS();      //�����һ�˳��ӽ�
	camera = smgr->getActiveCamera();

	camera->setPosition(vector3df(0,0,180));                //�����ӵ�λ��
	camera->setRotation(vector3df(0,0,0));                //�����ӵ���ת�Ƕ�

	//���뻷���⣬�����׹�ʹ�������������ɼ�
	smgr->setAmbientLight(video::SColor(0,250,250,250));
	scene::IMeshSceneNode *node;
	/*
	node = smgr->addCubeSceneNode(1,0);
	node->setPosition(core::vector3df(0,0,10));
	node->setScale(core::vector3df(100,100,100));
	node->setRotation(core::vector3df(0,0,0));
*/
	node = smgr->addCubeSceneNode(1,0);
	node->setPosition(core::vector3df(0,50,10));
	node->setScale(core::vector3df(100,100,100));
	node->setRotation(core::vector3df(0,0,0));
/*
	node = smgr->addCubeSceneNode(1,0);
	node->setPosition(core::vector3df(200,0,10));
	node->setScale(core::vector3df(50,50,50));
	node->setRotation(core::vector3df(0,0,0));
*/	//driver->getMaterial2D().TextureLayer[0].BilinearFilter=true;
	//driver->getMaterial2D().AntiAliasing=video::EAAM_FULL_BASIC;

    //��������������
	SAppContext context;
	AndroidEventReceiver *receiver;
    receiver = new AndroidEventReceiver(context);
    //�豸���������
    device->setEventReceiver(receiver);
}
static int myCount = 0;
void nativeDrawIteration()
{
	//nativeDrawIteration2DMovie();
	if(myCount == 0) initMovie();
	myCount ++;

	driver->beginScene(true,true,backColor);
	device->getSceneManager()->drawAll();
	driver->endScene();

	if(myCount == 100000) myCount = 1;
}

