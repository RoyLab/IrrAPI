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
enum
{
	// I use this ISceneNode ID to indicate a scene node that is
	// not pickable by getSceneNodeAndCollisionPointFromRay()
	ID_IsNotPickable = 0,

	// I use this flag in ISceneNode IDs to indicate that the
	// scene node can be picked by ray selection.
	IDFlag_IsPickable = 1 << 0,

	// I use this flag in ISceneNode IDs to indicate that the
	// scene node can be highlighted.  In this example, the
	// homonids can be highlighted, but the level mesh can't.
	IDFlag_IsHighlightable = 1 << 1
};
#define MIN(x, y) ((x) < (y)) ? (x) : (y);
#define MAX(x, y) ((x) > (y)) ? (x) : (y);
#define NUM_G 3;
#define NUM_TV 4;
#define NUM_HM 2;
#define NUM_MA 5;
#define NUM_S 2;

extern IrrlichtDevice *device;
extern IVideoDriver* driver;
extern stringc gSdCardPath;
bool UseHighLevelShaders = false;
bool UseCgShaders = false;

ISceneManager* smgr = NULL;
IGUIEnvironment* guienv = NULL;
IAnimatedMesh* mesh = NULL;
ISceneNode* node = NULL;
IAnimatedMeshSceneNode *nodeSydney = NULL;
ICameraSceneNode* camera = NULL;
IGUIStaticText* diagnostics = NULL;
SAppContext context;
AndroidEventReceiver *receiver;
scene::ISceneNodeAnimator* animPa;


video::ITexture** images  = NULL;
gui::IGUIFont* font = NULL;

u32 then;
u32 nFrame;
s32 lastFPS;
f32 startFlag;
const f32 MOVEMENT_SPEED = 5.f;
core::vector3df cameraPosition;
core::vector3df cameraRotation;
int *frameNo;
char filename[50];
char invfilename[50];
f32 frameElapsed;
s32 activeNode;
s32 frameCounter = 0;
s32 currentNode = 0;
s32 playframe = 0;
u16 num_me[5] = {3,5,7,5,2};
u16 titleIndex[5] = {2,3,2,4,1};
bool playflag,flag,flag1;
s32 menuNum = 4;
s32 glassOffset = 3;

//��̬ģ�͵�·��
stringc meshPath[5][7] = {
		{"/Irrlicht/3D_3D.3ds","/Irrlicht/3D_pad.3ds","/Irrlicht/3D_title.3ds","/Irrlicht/3D_padm.3ds"},
		{"/Irrlicht/mytv_whitepad.3ds","/Irrlicht/mytv_MY.3ds","/Irrlicht/why.3ds","/Irrlicht/hua_padm.3ds","/Irrlicht/mytv_title.3ds"},
		{"/Irrlicht/hua_pad.3ds","/Irrlicht/hua_padm.3ds","/Irrlicht/homemedia_title.3ds","/Irrlicht/hua_padm.3ds","/Irrlicht/hua_padm.3ds","/Irrlicht/hua_padm.3ds","/Irrlicht/hua_padm.3ds"},
		{"/Irrlicht/myapp_box.3ds","/Irrlicht/myapp_player.3ds","/Irrlicht/myapp_controler.3ds","/Irrlicht/myapp_My.3ds","/Irrlicht/myapp_App.3ds"},
		{"/Irrlicht/setting_lun.3ds","/Irrlicht/setting_title.3ds"}
};
/*
stringc banPath[5] = {
	"/Irrlicht/3D.png",
	"/Irrlicht/mytv.png",
	"/Irrlicht/homemedia.png",
	"/Irrlicht/myapp.png",
	"/Irrlicht/settings.png"
};

stringc invPath[5] = {
	"/Irrlicht/3Dinv.png",
	"/Irrlicht/mytvinv.png",
	"/Irrlicht/homemediainv.png",
	"/Irrlicht/myappinv.png",
	"/Irrlicht/settingsinv.png"
};
*/

//ģ����ͼ��·��
stringc banPath[5] = {
	"/Irrlicht/glass1.jpg",
	"/Irrlicht/glass1.jpg",
	"/Irrlicht/glass1.jpg",
	"/Irrlicht/glass1.jpg",
	"/Irrlicht/glass1.jpg"
};

//ģ����ͼ��Ӱ��·��
stringc invPath[5] = {
	"/Irrlicht/invglass1.jpg",
	"/Irrlicht/invglass1.jpg",
	"/Irrlicht/invglass1.jpg",
	"/Irrlicht/invglass1.jpg",
	"/Irrlicht/invglass1.jpg"
};

//��̬ģ�͵�·��
stringc aniPath[5] = {
			"/Irrlicht/mytv.b3d",
//			"Irrlicht/game.3DS",
			"/Irrlicht/homemedia.b3d",
			"/Irrlicht/try.b3d",
			"/Irrlicht/settings.b3d",
			"/Irrlicht/ninja.b3d"
};

//active node��������ͼ���䵹Ӱ��·��
stringc glassPath[2] = {
		"/Irrlicht/glass1.jpg",
		"/Irrlicht/invglass1.jpg",
};

//�趨���λ�á���С�����Եĸ���
class SceneLayoutBase {
protected:
	s32 amount;                             //��ڸ���
	irr::core::vector3df *position;         //���λ��
	irr::core::vector3df *scale;            //��ڴ�С
	irr::core::vector3df *rotation;         //�����ת�Ƕ�
	irr::core::vector3df cameraPosition;    //������ӵ㣩λ��
	irr::core::vector3df cameraRotation;    //������ӵ㣩��ת�Ƕ�
public:
	SceneLayoutBase(s32 amount) {
		this->amount = amount;
		this->position = new irr::core::vector3df[amount];
		this->scale = new irr::core::vector3df[amount];
		this->rotation = new irr::core::vector3df[amount];
	}
	virtual ~SceneLayoutBase() {
		delete [] position;
		delete [] scale;
		delete [] rotation;
	};

	//������ڸ���
	s32 getAmount() {
		return amount;
	}

	//�������λ��
	irr::core::vector3df *fixedPosition() {
		return position;
	}

	//������ڴ�С
	irr::core::vector3df *fixedScale() {
		return scale;
	}

	//���������ת�Ƕ�
	irr::core::vector3df *fixedRotation() {
		return rotation;
	}

	//����������ӵ㣩λ��
	irr::core::vector3df getCameraPosition() {
		return cameraPosition;
	}

	//����������ӵ㣩��ת�Ƕ�
	irr::core::vector3df getCameraRotation() {
		return cameraRotation;
	}
};


//�趨���λ�á���С�����Ե�����
class OutCyclicLayout : public SceneLayoutBase {
public:
	OutCyclicLayout(s32 amount,f32 w=80,f32 h=80,f32 t=2)
		: SceneLayoutBase(amount) {
			this->cylinderR = 100;
			this->width = w;
			this->height = h;
			this->disY = height;
			this->offsetY = height * .5f;
			this->thickness = t;
			this->deltaOmega = irr::core::PI * 2 / amount ;
			for (s32 i = 0; i < amount; i++) {
				float omega = 0;
				//this->offsetY = 0;
				if (i){
					omega = (amount - i) * deltaOmega;      //�����i����ڵ����ƫ�ƽǶ�
				}
				if(i == 1 or i==3){
					position[i] = irr::core::vector3df(sin(omega) * cylinderR*1.5, 0, cos(omega) * cylinderR);
				}
				else {
					position[i] = irr::core::vector3df(sin(omega) * cylinderR, 0, cos(omega) * cylinderR);
				}
				scale[i] = irr::core::vector3df(width, height, thickness);
				rotation[i] = irr::core::vector3df(0, omega * 180 / irr::core::PI, 0);
			}
			rotation[0] = irr::core::vector3df(0, 0, 0);
			cameraPosition = irr::core::vector3df(0, 0, 180);	//λ���������Ǳ�Ϊ�����ң����£���ǰ��
	}
	virtual ~OutCyclicLayout() {};
private:
	f32 disY;       //Y���ϵ�λ��ƫ����
	f32 offsetY;    //Y���ϵ�λ��ƫ����
	f32 cylinderR;  //����Ų���Բ�İ뾶
	f32 width;      //��ڵĿ��
	f32 height;     //��ڵĸ߶�
	f32 thickness;  //��ڵĺ��
	f32 deltaOmega; //��ڵ����ƫ�ƽǶ�
	f32 offsetOmega;
};

//��¼���node��Ϣ����
class WallNode {
public:
	WallNode(scene::ISceneManager* smgr, SceneLayoutBase *layout, s32 pos = 0, bool active = false, f32 animationLast = 1000.f) {
		//sceme::IMeshManipulator *manipulator = smgr->getMeshManipulator();
		this->smgr = smgr;
		this->layout = layout;
		this->pos = pos;
		this->active = active;
		this->posTimeLast = this->animationLast = animationLast;
		this->posTimeToGo = 0;
		this->scaTimeElapse = this->scaTimeLast = 500.f;
		this->parentNode = smgr->addCubeSceneNode(1, 0 , ID_IsNotPickable);
		//this->parentNode->setVisible(false);
		this->sceneNode = smgr->addCubeSceneNode(1, parentNode, IDFlag_IsPickable | IDFlag_IsHighlightable | (pos << 2));
		this->invNode = smgr->addCubeSceneNode(1,parentNode,ID_IsNotPickable);
		this->selector = smgr->createTriangleSelector(this->sceneNode->getMesh(), this->sceneNode);
		this->sceneNode->setTriangleSelector(this->selector);
		//this->selector->drop();
		//std::cout << this->sceneNode->getMaterialCount();
	}
	~WallNode() {};

	//����wallNodes��һ֡�е�λ�á���С����ת�Ƕ�
	void touch(f32 time) {
		if (posTimeToGo) {              //�����˶���
			if (posTimeToGo <= time) {  //�˶�Ӧ��ֹͣ
				posTimeToGo = 0;        //ʣ���˶�ʱ����0
				this->posTimeLast =  this->animationLast;
				this->parentNode->setPosition(posDes);  //�̶��ýڵ�λ��
			} else {                    //�˶�����
				posTimeToGo -= time;    //ʣ���˶�ʱ���ü�ȥ������ʱ��time
				if (posTimeToGo > posTimeLast / 2) {    //ǰ���˶�Ч��
					f32 t2 =  (posTimeLast - posTimeToGo) * (posTimeLast - posTimeToGo);
					this->parentNode->setPosition(posSrc + posVel * (posTimeLast - posTimeToGo) + posAcc * (t2 / 2));
					//std::cout << this->sceneNode->getPosition().X << ", " <<  this->sceneNode->getPosition().Y << ", " <<  this->sceneNode->getPosition().Z << "\n";
				} else {                //�����˶�Ч��
					f32 t2 =  posTimeToGo * posTimeToGo;
					this->parentNode->setPosition(posDes - posVel * posTimeToGo - posAcc * (t2 / 2));
				}
			}
		}
		if (this->scaTimeElapse < scaTimeLast) {    //�˶���δֹͣ
			this->scaTimeElapse += time;            //���˶�ʱ�����������ʱ��time
			if (this->scaTimeElapse > this->scaTimeLast) {  //�˶�Ӧ��ֹͣ
				this->scaTimeElapse = this->scaTimeLast;
				this->sceneNode->setScale(this->scaDes);    //������ڵ�Ŀ���С
				this->parentNode->setRotation(this->rotDes);//������ڵ�Ŀ����ת�Ƕ�
			} else {        //�˶�����
				this->sceneNode->setScale(this->scaSrc + this->scaVel * this->scaTimeElapse);       //������ڴ˿̴�С
				this->parentNode->setRotation(this->rotSrc + this->rotVel * this->scaTimeElapse);   //����parentNode�˿���ת�Ƕ�
			}
		}
	}

	//���ز�����ڵ�
	scene::ISceneNode *getSceneNode() {
		return this->sceneNode;
	}

	//���ظ��ڵ�
	scene::ISceneNode *getParentNode() {
		return this->parentNode;
	}

	//���ز����鵹Ӱ�ڵ�
	scene::ISceneNode *getInvNode(){
		return this->invNode;
	}

	//���ر���ڵ�
	scene::ISceneNode *getTitleNode(){
		return this->titleNode;
	}

	//���ر��⵹Ӱ�ڵ�
	scene::ISceneNode *getInvtitleNode(){
		return this->invtitleNode;
	}

	//���ض���ģ�ͽڵ�
	scene::IAnimatedMeshSceneNode *getAniNode(){
		return this->aniNode;
	}


	scene::ISceneNode *getBanNode() {
		return this->banNode;
	}

	//��ʼ��֡��Ŀ
	void initAF() {
		aniframe = 0;
	}

	//��ʼ�������ڵ�
	void initAniNode(u16 i_ani){
		this->aniNode = smgr->addAnimatedMeshSceneNode(
				smgr->getMesh((gSdCardPath + aniPath[i_ani]).c_str()),
				this->getParentNode(),
				ID_IsNotPickable);  //����Ϊ·�������ڵ㼰ID,ID����Ϊ����ѡ��
		this->aniNode->setAnimationSpeed(0);            //��ʼ�����ٶ�Ϊ0��Ϊ��ֹ״̬
		this->aniNode->getMaterial(0).Lighting = true;  //ʹ�ù�����Ч���Ӷ����Կ���ģ��
		this->aniNode->setVisible(false);               //ģ������Ϊ���ɼ�
		this->aniNode->setLoopMode(false);              //ģ�Ͷ�������ѭ��
		//this->aniNode->setMaterialType(video::EMT_TRANSPARENT_ALPHA_CHANNEL);
	}

	//��ʼ������ڵ�
	void initTtileNode(u16 ind){
	    //����ڵ���parentNodeΪ���ڵ㣬����ѡ��
		this->titleNode = smgr->addCubeSceneNode(1,this->parentNode,ID_IsNotPickable);
		this->invtitleNode = smgr->addCubeSceneNode(1,this->parentNode,ID_IsNotPickable);
		//this->titleNode->setVisible(false);
		//this->invtitleNode->setVisible(false);
	}

	//ֹͣģ�͵Ķ���
	void removePlay() {
		this->aniNode->removeAnimators();
	}

	//�������˶���ʼ��
	//����src��ԴparentNode��index
	//����src��parentNode���Ƶ���λ�ö�Ӧ��index
	void initAnimation(s32 src, s32 des) {
		if (this->posTimeToGo) {                                //��ʾ�����鴦���˶���
			this->posSrc = this->parentNode->getPosition();     //��ȡ��ǰparentNode��λ��Ϊ��һ֡�˶��ĳ�ʼλ��
			this->posTimeToGo = this->posTimeLast = MAX(300.f, posTimeToGo);
		}
		else {                                                  //��ʾ�˶���δ��ʼ
			this->posSrc = layout->fixedPosition()[src];        //src��Ӧ�Ľڵ�λ��Ϊ��ʼλ��
			this->posTimeToGo = 1000.f;                         //�˶�ʱ������Ϊ1000ms
		}
		this->posDes = layout->fixedPosition()[des];            //des��Ӧ�Ľڵ�λ��ΪĿ��λ��
		this->posAcc = (this->posDes - this->posSrc) * (4/ this->animationLast / this->animationLast);  //�����˶����ٶ�
		this->posVel = (this->posDes - this->posSrc) * ((1 / this->posTimeLast) - (this->posTimeLast / this->animationLast / this->animationLast));

		this->scaTimeElapse = 0;
		this->scaSrc = this->sceneNode->getScale();             //��ȡ��ʼ�ڵ��С
		this->scaDes = layout->fixedScale()[des];               //��ȡĿ��ڵ��С
		this->scaVel = (this->scaDes - this->scaSrc) / this->scaTimeLast;   //�����С�仯��λ��
		this->rotSrc = this->parentNode->getRotation();         //��ȡ��ʼ�ڵ���ת�Ƕ�
		this->rotDes = layout->fixedRotation()[des];            //��ȡĿ��ڵ���ת�Ƕ�
		irr::core::vector3df rotDis = (this->rotDes - this->rotSrc);        //������ת�ǶȲ�����
		//std::cout << rotDis.X << " " << rotDis.Y << " " << rotDis.Z << "\n";

		//����ת�Ƕȿ�����-180��180�ȵ�������
		while (rotDis.X > 180) rotDis.X -= 360;
		while (rotDis.Y > 180) rotDis.Y -= 360;
		while (rotDis.Z > 180) rotDis.Z -= 360;
		while (rotDis.X < -180) rotDis.X += 360;
		while (rotDis.Y < -180) rotDis.Y += 360;
		while (rotDis.Z < -180) rotDis.Z += 360;

		this->rotVel = rotDis / this->scaTimeLast;              //������ת�Ƕȱ仯��λ��

	}

	//�������˶���ʼ��
	//����src��ԴparentNode��λ��
	//����src��parentNode���Ƶ���λ��
	//�˺�������void initAnimation(s32 src, s32 des)������ʵ����֮����һ��
	void initAnimation(core::vector3df src, core::vector3df des) {
			if (this->posTimeToGo) {
				this->posSrc = this->parentNode->getPosition();
				this->posTimeToGo = this->posTimeLast = MAX(300.f, posTimeToGo);
			} else {
				this->posSrc = src;
				this->posTimeToGo = 1000.f;
			}
			this->posDes = des;
			this->posAcc = (this->posDes - this->posSrc) * (4/ this->animationLast / this->animationLast);
			this->posVel = (this->posDes - this->posSrc) * ((1 / this->posTimeLast) - (this->posTimeLast / this->animationLast / this->animationLast));

			this->scaTimeElapse = 0;
			this->scaSrc = this->sceneNode->getScale();
			this->scaDes = this->scaSrc;
			this->scaVel = (this->scaDes - this->scaSrc) / this->scaTimeLast;
			this->rotSrc = this->parentNode->getRotation();
			this->rotDes = this->rotSrc;
			irr::core::vector3df rotDis = (this->rotDes - this->rotSrc);
			//std::cout << rotDis.X << " " << rotDis.Y << " " << rotDis.Z << "\n";
			while (rotDis.X > 180) rotDis.X -= 360;
			while (rotDis.Y > 180) rotDis.Y -= 360;
			while (rotDis.Z > 180) rotDis.Z -= 360;
			while (rotDis.X < -180) rotDis.X += 360;
			while (rotDis.Y < -180) rotDis.Y += 360;
			while (rotDis.Z < -180) rotDis.Z += 360;
			this->rotVel = rotDis / this->scaTimeLast;

	}

	//���ö���ģ�ͼ�����ڵ������
	void setVis(bool isVis) {
		aniNode->setVisible(isVis);
		titleNode->setVisible(isVis);
		invtitleNode->setVisible(isVis);
	}

	//ת��next����ʱ���½ڵ�index��active node
	void labelNext(s32 amount, s32 times) {
		this->pos += times;             //����ƶ�times��λʱindex����times
		while (this->pos >= amount) {
			this->pos -= amount;        //ѭ�������ֹ���
		}
		if (!this->pos) active = true;  //����active node
		else active = false;
	}

	////ת��previous����ʱ���½ڵ�index��active node
	void labelPrev(s32 amount, s32 times) {
		this->pos -= times;             //��ǰ�ƶ�times��λʱindex����times
		while (this->pos < 0) {
			this->pos += amount;        //ѭ�������ֹ���
		}
		if (!this->pos) active = true;  //����active node
		else active = false;
	}

    //����֡���Լ�һ����λ
	void incAF(){
		this->aniframe++;
	}

	//��ȡ��ǰ������Ӧ֡��
	s32 getAF(){
		return this->aniframe;
	}

	//��ȡ��ǰ�ڵ�λ��
	s32 getPos() {
		return this->pos;
	}

	//���ص�ǰ�ڵ��Ƿ�Ϊactive node
	bool isActive() {
		return this->active;
	}

	//��ȡʣ���˶�ʱ��
	f32 getPosTimeToGo(){
		return posTimeToGo;
	}
private:
	s32 pos;            //���λ��
	bool active;        //����Ƿ�Ϊactive node
	f32 animationLast;
	f32 posTimeLast;    //�˶�ʱ��
	f32 posTimeToGo;    //ʣ���˶�ʱ��
	f32 scaTimeLast;    //�˶�ʱ��
	f32 scaTimeElapse;  //���˶�ʱ��
	//s32 *pnframe;
	s32 aniframe;                   //��ǰ������֡��
	irr::core::vector3df posSrc;    //��ʼλ��
	irr::core::vector3df posDes;    //Ŀ��λ��
	irr::core::vector3df posAcc;    //�˶��ٶȴ�С
	irr::core::vector3df posVel;
	irr::core::vector3df scaSrc;    //��ʼ��С
	irr::core::vector3df scaDes;    //Ŀ���С
	irr::core::vector3df scaVel;
	irr::core::vector3df rotSrc;    //��ʼ��ת�Ƕ�
	irr::core::vector3df rotDes;    //Ŀ����ת�Ƕ�
	irr::core::vector3df rotVel;
	scene::ISceneManager *smgr;
	SceneLayoutBase *layout;            //��¼λ�ô�С��Ϣ
	scene::ISceneNode *parentNode;      //�����ڵ�ĸ��ڵ㣬Ϊ�����ڵ��ṩλ���������
	scene::IMeshSceneNode *invNode;     //��ڲ����鵹Ӱ�ڵ�
	scene::IMeshSceneNode *sceneNode;   //��ڲ�����ڵ�
	scene::IMeshSceneNode *banNode;     //����ģ�ͽڵ�
	scene::IMeshSceneNode *titleNode;   //����ڵ�
	scene::IMeshSceneNode *invtitleNode;//���⵹Ӱ�ڵ�
	//scene::IAnimatedMeshSceneNode **playNode;
	scene::IAnimatedMeshSceneNode *aniNode;//����ģ�ͽڵ�
	scene::ITriangleSelector *selector; //����ѡ����
};

//ʹwallNodes��next����ת��times��λ
void turnNext(SceneLayoutBase *sceneLayout, WallNode **wallNodes, s32 times) {
	for (s32 i = 0; i < sceneLayout->getAmount(); i++) {
		s32 pos = wallNodes[i]->getPos();   //Դ�ڵ�index
		s32 pos1 = pos + times;             //Ŀ��ڵ�index
		while (pos1 >= sceneLayout->getAmount()) {
			pos1 -= sceneLayout->getAmount();       //ѭ�������
		}
		wallNodes[i]->initAnimation(pos, pos1);     //��ʼ���ڵ�pos�˶����ڵ�pos1�������Ϣ
		wallNodes[i]->labelNext(sceneLayout->getAmount(), times);   //���½ڵ�index
	}
}

//ʹwallNodes��previous����ת��times��λ
void turnPrev(SceneLayoutBase *sceneLayout, WallNode **wallNodes, s32 times) {
	for (s32 i = 0; i < sceneLayout->getAmount(); i++) {
		s32 pos = wallNodes[i]->getPos();   //Դ�ڵ�index
		s32 pos1 = pos - times;             //Ŀ��ڵ�index
		while (pos1 < 0) {
			pos1 += sceneLayout->getAmount();       //ѭ�������
		}
		wallNodes[i]->initAnimation(pos, pos1);     //��ʼ���ڵ�pos�˶����ڵ�pos1�������Ϣ
		wallNodes[i]->labelPrev(sceneLayout->getAmount(), times);   //���½ڵ�index
	}
}

WallNode **wallNodes =NULL;                     //��ڽڵ���Ϣ
SceneLayoutBase *sceneLayout = NULL;            //���λ�ü���С��Ϣ
scene::ISceneCollisionManager *collMan = NULL;  //��ײ������
video::SMaterial material;                      //����
scene::IMeshSceneNode** frontLight;             //active node�����ƹ�
scene::IAnimatedMeshSceneNode** mytvNode = NULL;    //mytv���е�node������mytv��ڲ��þ�̬ģ�ͣ��ɶ��ģ�ͽڵ㹲ͬ��ɶ�����
int mytvframe = 0;                              //mytv������֡��

//��ʼ��mytv��ڵĽڵ�
void initMytvNode()
{
	mytvNode = new IAnimatedMeshSceneNode* [4]; //��4���ڵ�
	for(u16 i=0;i<4;++i){
		mytvNode[i] = smgr->addAnimatedMeshSceneNode(smgr->getMesh((gSdCardPath + meshPath[1][i]).c_str()),wallNodes[0]->getParentNode(),ID_IsNotPickable);
		mytvNode[i]->setVisible(false);

	}

	//����ÿ���ڵ�Ĵ�С��λ�ú���ת�Ƕ�
	mytvNode[0]->setScale(core::vector3df(2,3,3));
	mytvNode[1]->setScale(core::vector3df(2,3,3));
	mytvNode[2]->setScale(core::vector3df(2,3,3));
	mytvNode[0]->setPosition(core::vector3df(0,7,3));
	mytvNode[0]->setRotation(core::vector3df(0,-90,0));
	mytvNode[1]->setPosition(core::vector3df(0,17,8));
	mytvNode[1]->setRotation(core::vector3df(0,-90,0));
	mytvNode[2]->setPosition(core::vector3df(0,-3,6));
	mytvNode[2]->setRotation(core::vector3df(0,-90,0));
	mytvNode[3]->setParent(mytvNode[0]);
	mytvNode[3]->setScale(core::vector3df(1.38,1,1));
	mytvNode[3]->setPosition(core::vector3df(0,-0.5,0.42));
	mytvNode[3]->setRotation(core::vector3df(0,90,0));

}

//mytv�����˶���ʼ��
void startPlayMytv()
{
	for(u16 i=0;i<3;++i) mytvNode[i]->setVisible(true);
	mytvframe = 0;
	nFrame = 0;
}

//mytv�����˶���ʼ
void doPlayMytv()
{
	scene::ISceneNodeAnimator* anim;    //��������
	core::vector3df pos_D;              //�ڵ��ʼλ��
	core::vector3df pos_S;              //�ڵ�Ŀ��λ��
	u32 now;
	f32 frameDeltaTime;
	stringc frameFilename;

	if(mytvframe == 0){
		mytvframe++;
		pos_S = mytvNode[1]->getPosition();
		pos_D = core::vector3df(pos_S.X,pos_S.Y+50,pos_S.Z);
		anim = smgr->createFlyStraightAnimator(pos_S,pos_D, 1000);      //������������pos_S�˶���pos_D����ʱ1000ms
		if (anim)
		{
			mytvNode[1]->addAnimator(anim);     //�ڵ����붯��
			anim->drop();
		}
		anim = smgr->createRotationAnimator(core::vector3df(0,3,0));    //�����������أ�0,3,0��������ת
		if (anim)
		{
			mytvNode[0]->addAnimator(anim);     //�ڵ����붯��
			anim->drop();
		}
		anim = smgr->createRotationAnimator(core::vector3df(0,3,0));    //�����������أ�0,3,0��������ת
		if (anim)
		{
			mytvNode[1]->addAnimator(anim);      //�ڵ����붯��
			anim->drop();
		}
		pos_S = mytvNode[2]->getPosition();
		pos_D = core::vector3df(pos_S.X,pos_S.Y-30,pos_S.Z+60);
		anim = smgr->createFlyStraightAnimator(pos_S,pos_D, 1000);
		if (anim)
		{
			mytvNode[2]->addAnimator(anim);     //�ڵ����붯��
			anim->drop();
		}
		anim = smgr->createRotationAnimator(core::vector3df(0,-3,0));
		if (anim)
		{
			mytvNode[2]->addAnimator(anim);     //�ڵ����붯��
			anim->drop();
		}

	}

	if(mytvNode[0]->getRotation().Y>=90) {  //��ת����90�ȣ�����ֹͣ��ת
		mytvNode[0]->removeAnimators();     //ժ����ת����
		mytvNode[0]->setRotation(core::vector3df(0,90,0));  //������ת�Ƕȣ�������ת�ѳ���90��ʱУ��Ϊ90��
		mytvNode[3]->setVisible(true);      //�ڵ�3���ӻ�����ʼ���Ŷ���
		now = device->getTimer()->getTime();                //��ȡ��ǰʱ��
		frameDeltaTime = (f32)(now - then) / 1000.f;        //��ȡʱ����
		then = now;
		frameElapsed += frameDeltaTime;

		if (frameElapsed > 1.f / 24) {      //ÿ��24֡
			frameElapsed -= 1.f / 24;
			nFrame++;
		    sprintf(filename, "/Irrlicht/frame0/v0%d.jpg",nFrame);
			frameFilename = filename;
			mytvNode[3]->setMaterialTexture(0, driver->getTexture((gSdCardPath+frameFilename).c_str()));    //����һ֡��ͼ
			if(nFrame==95) nFrame = 0;                      //��95֡��ѭ������
		}

	}
}

//mytv����ֹͣ
void stopPlayMytv()
{
    //�ڵ�λ�÷��س�ʼλ��
	mytvNode[0]->setPosition(core::vector3df(0,7,3));
	mytvNode[0]->setRotation(core::vector3df(0,-90,0));
	mytvNode[1]->setPosition(core::vector3df(0,17,8));
	mytvNode[1]->setRotation(core::vector3df(0,-90,0));
	mytvNode[2]->setPosition(core::vector3df(0,-3,6));
	mytvNode[2]->setRotation(core::vector3df(0,-90,0));

	//ȥ������������Ϊ���ɼ�
	for(u16 i=0;i<4;++i){
		mytvNode[i]->removeAnimators();
		mytvNode[i]->setVisible(false);
	}
	mytvframe = 0;
}

//��ȡ��ǰʱ��
static long _getTime(void)
{
    struct timeval  now;

    gettimeofday(&now, NULL);
    return (long)(now.tv_sec*1000 + now.tv_usec/1000);
}


//��ʼ������ģ�ͽڵ�
//����ind����Ҫ��ʼ���Ľڵ�index
void initPN(u16 ind)
{
	u16 i;
	stringc texfile;
	wallNodes[ind]->initAniNode(ind);
	switch(ind){
		case 0:
			(wallNodes[ind]->getAniNode())->setScale(core::vector3df(0.1,0.1,0.1));
			(wallNodes[ind]->getAniNode())->setRotation(core::vector3df(-90,0,0));
			break;
		case 1:
		case 2:
		case 3:
		case 4:
			(wallNodes[ind]->getAniNode())->setScale(core::vector3df(5,5,5));
			(wallNodes[ind]->getAniNode())->setRotation(core::vector3df(-90,0,0));
			break;
		default: break;
	}

	sprintf(filename, "/Irrlicht/black.jpg");
	texfile = filename;
	(wallNodes[ind]->getParentNode())->setMaterialTexture(0, driver->getTexture((gSdCardPath + texfile).c_str()));  //parentNode����Ϊ����ɫ
	(wallNodes[ind]->getParentNode())->setMaterialFlag(video::EMF_LIGHTING, false);             //���ù���ʹnode�ɱ�������
	(wallNodes[ind]->getParentNode())->setMaterialType(video::EMT_TRANSPARENT_ADD_COLOR);       //ʹ�ü�ɫ͸��ģʽ��ʹ��ɫ��parentNode���ɼ�

	wallNodes[ind]->initTtileNode(ind);                 //��ʼ������ڵ�ind

    //���ñ���ڵ�Ĳ��ʡ���С��λ�úͻ�ɫģʽ
	sprintf(filename, "/Irrlicht/title0%d.jpg",ind+1);
	texfile = filename;
	(wallNodes[ind]->getTitleNode())->setMaterialTexture(0, driver->getTexture((gSdCardPath + texfile).c_str()));
	(wallNodes[ind]->getTitleNode())->setScale(core::vector3df(50,25,0));
	(wallNodes[ind]->getTitleNode())->setPosition(core::vector3df(0,-30,0));
	(wallNodes[ind]->getTitleNode())->setMaterialFlag(video::EMF_LIGHTING, false);
	//(wallNodes[ind]->getTitleNode())->setMaterialType(video::EMT_TRANSPARENT_ALPHA_CHANNEL);
	(wallNodes[ind]->getTitleNode())->setMaterialType(video::EMT_TRANSPARENT_ADD_COLOR);        //ʹ�ü�ɫ͸��ģʽ���Ӷ�ͼƬ�ĺ�ɫ�������ɼ�

	sprintf(filename, "/Irrlicht/invtitle0%d.jpg",ind+1);
	texfile = filename;
	(wallNodes[ind]->getInvtitleNode())->setMaterialTexture(0, driver->getTexture((gSdCardPath + texfile).c_str()));
	(wallNodes[ind]->getInvtitleNode())->setScale(core::vector3df(50,25,0));
	(wallNodes[ind]->getInvtitleNode())->setPosition(core::vector3df(0,-50+glassOffset,0));
	(wallNodes[ind]->getInvtitleNode())->setMaterialFlag(video::EMF_LIGHTING, false);
	(wallNodes[ind]->getInvtitleNode())->setMaterialType(video::EMT_TRANSPARENT_ADD_COLOR);     //ʹ�ü�ɫ͸��ģʽ���Ӷ�ͼƬ�ĺ�ɫ�������ɼ�

}

//��ʼ����ڽڵ�
void initWallNodes()
{
	sceneLayout = new OutCyclicLayout(menuNum);
	wallNodes = new WallNode*[menuNum];
	for (s32 i = 0; i < sceneLayout->getAmount(); i++) {
		if (!i) wallNodes[i] = new WallNode(smgr, sceneLayout, i, true);	//��0��Ϊactive
		else wallNodes[i] = new WallNode(smgr, sceneLayout, i);

        //������ڽڵ㲣�����λ�á���С����ת�Ƕȡ����ʺͻ�ɫģʽ
		if (wallNodes[i]->getSceneNode()) {
			wallNodes[i]->getParentNode()->setPosition(sceneLayout->fixedPosition()[i]);
			wallNodes[i]->getParentNode()->setRotation(sceneLayout->fixedRotation()[i]);
			//wallNodes[i]->getParentNode()->setScale(irr::core::vector3df(0.1,0.1,0.1));
			//wallNodes[i]->getParentNode()->setVisible(false);

			wallNodes[i]->getSceneNode()->setScale(sceneLayout->fixedScale()[i]);
			wallNodes[i]->getSceneNode()->setMaterialTexture(0, driver->getTexture((gSdCardPath + banPath[i]).c_str()));
			wallNodes[i]->getSceneNode()->setMaterialFlag(video::EMF_LIGHTING, false);
			wallNodes[i]->getSceneNode()->setMaterialType(video::EMT_TRANSPARENT_ADD_COLOR);    //���ü�ɫ͸��ģʽ�ﵽ͸��Ч��
			//wallNodes[i]->getSceneNode()->setMaterialType(video::EMT_TRANSPARENT_ALPHA_CHANNEL);

			initPN(i);
		}
		//������ڽڵ㲣����ĵ�Ӱ
		if(wallNodes[i]->getInvNode()){
			wallNodes[i]->getInvNode()->setPosition(irr::core::vector3df(0,-80+glassOffset,0));
			wallNodes[i]->getInvNode()->setScale(sceneLayout->fixedScale()[i]);
			wallNodes[i]->getInvNode()->setMaterialTexture(0, driver->getTexture((gSdCardPath + invPath[i]).c_str()));
			wallNodes[i]->getInvNode()->setMaterialFlag(video::EMF_LIGHTING, false);
			wallNodes[i]->getInvNode()->setMaterialType(video::EMT_TRANSPARENT_ADD_COLOR);
		}
	}
	initMytvNode(); //��ʼ��mytv��nodes
}


void setMaterial(u16 ind)
{
	switch(ind){
		case 0:
			break;
		case 1:
			//wallNodes[ind]->getAniNode()->getMaterial(0).MaterialType = (video::EMT_TRANSPARENT_ALPHA_CHANNEL);
			//wallNodes[ind]->getAniNode()->getMaterial(10).MaterialType = (video::EMT_TRANSPARENT_ALPHA_CHANNEL);
			wallNodes[ind]->getAniNode()->setMaterialType(video::EMT_TRANSPARENT_ALPHA_CHANNEL);
			break;
		case 2:
			//wallNodes[ind]->getAniNode()->setMaterialType(video::EMT_TRANSPARENT_ALPHA_CHANNEL);
			wallNodes[ind]->getAniNode()->getMaterial(0).MaterialType = (video::EMT_TRANSPARENT_ALPHA_CHANNEL);
			break;
		case 3:
			wallNodes[ind]->getAniNode()->getMaterial(0).MaterialType = (video::EMT_TRANSPARENT_ALPHA_CHANNEL);
			//wallNodes[ind]->getAniNode()->setMaterialType(video::EMT_TRANSPARENT_ALPHA_CHANNEL);
			break;
		default: break;
	}
}
//ģ�Ͷ����˶���ʼ��
//����ind������ʼ���ڵ��index
void startPlay(u16 ind)
{
	u16 i;
	//���ýڵ�Ĳ��ʡ���С
	wallNodes[ind]->getSceneNode()->setMaterialTexture(0, driver->getTexture((gSdCardPath + glassPath[0]).c_str()));
	wallNodes[ind]->getSceneNode()->setScale(irr::core::vector3df(80,80,0));
	wallNodes[ind]->getInvNode()->setMaterialTexture(0, driver->getTexture((gSdCardPath + glassPath[1]).c_str()));
	wallNodes[ind]->getInvNode()->setScale(irr::core::vector3df(80,80,0));
	wallNodes[ind]->setVis(true);   //ʹ�ڵ�ɼ�
	wallNodes[ind]->initAF();       //��ʼ������֡��

	setMaterial(ind);

	scene::ISceneNodeAnimator* anim;    //ƽ�ƶ�������
	core::vector3df pos_D,pos_S;        //ƽ�Ƴ�ʼλ�á�Ŀ��λ��

	if(ind == 0) {
		startPlayMytv();            //��ʼ��mytv node���˶�
		//wallNodes[ind]->getAniNode()->setVisible(false);
	}

	/********* animation **********/
	pos_S = wallNodes[ind]->getAniNode()->getPosition();
	pos_D =  core::vector3df(pos_S.X,pos_S.Y,pos_S.Z+0.00001);
	startFlag = pos_D.Z;
	anim = smgr->createFlyStraightAnimator(pos_S,pos_D, 500);   //��pos_S��pos_Dƽ�ƣ���ʱ500ms
	if (anim)
	{
		(wallNodes[ind]->getAniNode())->addAnimator(anim);      //���붯��
		anim->drop();
	}

	pos_S = wallNodes[ind]->getTitleNode()->getPosition();
	pos_D =  core::vector3df(pos_S.X,pos_S.Y,pos_S.Z+0.00001);
	anim = smgr->createFlyStraightAnimator(pos_S,pos_D, 500);
	if (anim)
	{
		(wallNodes[ind]->getTitleNode())->addAnimator(anim);    //���붯��
		anim->drop();
	}

	pos_S = wallNodes[ind]->getInvtitleNode()->getPosition();
	pos_D =  core::vector3df(pos_S.X,pos_S.Y,pos_S.Z+0.00001);
	anim = smgr->createFlyStraightAnimator(pos_S,pos_D, 500);
	if (anim)
	{
		(wallNodes[ind]->getInvtitleNode())->addAnimator(anim); //���붯��
		anim->drop();
	}

}

//ģ���˶���ʼ
//����ind���˶��ڵ��index
void doingPlay(u16 ind)
{
	u16 i;
	u32 now;
	f32 frameDeltaTime;
	stringc frameFilename;
	scene::ISceneNodeAnimator* anim;
	core::vector3df pos_D,pos_S;
	if(ind == 0 && (wallNodes[ind]->getAniNode())->getPosition().Z == startFlag) doPlayMytv();  //indΪ�㣬����mytv�Ķ���

	if(wallNodes[ind]->getAF() == 0 && (wallNodes[ind]->getAniNode())->getPosition().Z == startFlag){   //startPlay������ʼ���
		wallNodes[ind]->incAF();    //֡���Լӣ���ʾ�����ѿ�ʼ
		//if(ind == 0) (wallNodes[ind]->getAniNode())->setAnimationSpeed(300.0f);
		if(ind == 0) {
			wallNodes[ind]->getAniNode()->setVisible(false);        //mytv���þ�̬ģ�ͣ����̬ģ�Ͳ��ɼ�
		}
		else {
			pos_S = (wallNodes[ind]->getAniNode())->getPosition();
			pos_D = core::vector3df(pos_S.X,pos_S.Y-10,pos_S.Z+10);
			anim = smgr->createFlyStraightAnimator(pos_S,pos_D, 400);   //��pos_S��pos_Dƽ�ƣ���ʱ400ms
			if (anim)
			{
				(wallNodes[ind]->getAniNode())->addAnimator(anim);      //���붯��
				anim->drop();
			}
			if(ind ==1)(wallNodes[ind]->getAniNode())->setAnimationSpeed(10.0f);    //1����ڣ���home media���������ٶȽ���
			else (wallNodes[ind]->getAniNode())->setAnimationSpeed(30.0f);          //���ö����ٶ�
		}

		pos_S = wallNodes[ind]->getTitleNode()->getPosition();
		pos_D =  core::vector3df(0,-18,30);
		anim = smgr->createFlyStraightAnimator(pos_S,pos_D, 400);
		if (anim)
		{
			(wallNodes[ind]->getTitleNode())->addAnimator(anim);    //���붯��
			anim->drop();
		}

		pos_S = wallNodes[ind]->getInvtitleNode()->getPosition();
		pos_D =  core::vector3df(0,-33+glassOffset,30);
		anim = smgr->createFlyStraightAnimator(pos_S,pos_D, 400);
		if (anim)
		{
			(wallNodes[ind]->getInvtitleNode())->addAnimator(anim); //���붯��
			anim->drop();
		}
	}
}

//ģ���˶�ֹͣ
//����ind��ֹͣ�ڵ��index
void stopPlay(u16 ind)
{
	/*
	int i;
	for(i=0;i<5;++i){
		if(i!=ind){
			wallNodes[i]->getAniNode()->setVisible(false);
			wallNodes[i]->getSceneNode()->setVisible(false);
		}

		wallNodes[i]->getInvNode()->setVisible(false);
		wallNodes[i]->getTitleNode()->setVisible(false);
		wallNodes[i]->getInvtitleNode()->setVisible(false);
	}
	*/
	if(ind == 0) stopPlayMytv();    //��Ϊmytv���������Լ��Ľ�����������

	//��������ʴ�С�趨
	wallNodes[ind]->getSceneNode()->setMaterialTexture(0, driver->getTexture((gSdCardPath + banPath[ind]).c_str()));
	wallNodes[ind]->getSceneNode()->setScale(irr::core::vector3df(80,80,2));
	wallNodes[ind]->getInvNode()->setMaterialTexture(0, driver->getTexture((gSdCardPath + invPath[ind]).c_str()));
	wallNodes[ind]->getInvNode()->setScale(irr::core::vector3df(80,80,2));
	//wallNodes[ind]->getParentNode()->setVisible(false);
	//wallNodes[ind]->setVis(false);
	wallNodes[ind]->removePlay();   //ȥ��ģ�Ͷ���
	wallNodes[ind]->getAniNode()->setMesh(smgr->getMesh((gSdCardPath + aniPath[ind]).c_str())); //��������meshʹ��ģ��ͣ���ڵ�һ֡
	wallNodes[ind]->getAniNode()->setPosition(core::vector3df(0,0,0));  //ģ�ͽڵ㷵�س�ʼλ��
	wallNodes[ind]->getAniNode()->setAnimationSpeed(0);                 //ģ��ֹͣ�˶�
	wallNodes[ind]->getAniNode()->setVisible(false);                    //ģ�Ͳ��ɼ�

	scene::ISceneNodeAnimator* anim;
	core::vector3df pos_D,pos_S;

    //Ϊ����ڵ����ôӵ�ǰλ�÷��ص���ʼλ�õĶ���
	pos_S = wallNodes[ind]->getTitleNode()->getPosition();
	pos_D =  core::vector3df(0,-30,0);
	anim = smgr->createFlyStraightAnimator(pos_S,pos_D, 500);   //������ʱ500ms
	if (anim)
	{
		(wallNodes[ind]->getTitleNode())->addAnimator(anim);    //���붯��
		anim->drop();
	}
	pos_S = wallNodes[ind]->getInvtitleNode()->getPosition();
	pos_D =  core::vector3df(0,-50+glassOffset,0);
	anim = smgr->createFlyStraightAnimator(pos_S,pos_D, 500);
	if (anim)
	{
		(wallNodes[ind]->getInvtitleNode())->addAnimator(anim);
		anim->drop();
	}
}

//��������ʼ��
void init2DMovie()
{
	smgr = device->getSceneManager();   //��ȡscene manager

	initWallNodes();                    //��ʼ����ڽڵ�

	smgr->addCameraSceneNodeFPS();      //�����һ�˳��ӽ�
	camera = smgr->getActiveCamera();
	//device->getCursorControl()->setVisible(false);

	lastFPS = -1;

	then = device->getTimer()->getTime();   //��ȡ��ǰʱ��

	//core::vector3df cameraPosition = camera->getPosition();
	cameraPosition = sceneLayout->getCameraPosition();  //��ȡ�ӵ�λ��
	cameraRotation = sceneLayout->getCameraRotation();  //��ȡ�ӵ���ת�Ƕ�
	camera->setPosition(cameraPosition);                //�����ӵ�λ��
	camera->setRotation(cameraRotation);                //�����ӵ���ת�Ƕ�

	char fname[50];
	stringc movieFilename;

	//������ʼ����ĵ������
	scene::IMeshSceneNode *floor = smgr->addCubeSceneNode(1,0,ID_IsNotPickable);
	floor->setPosition(core::vector3df(0,-32,0));       //����λ��
	floor->setScale(core::vector3df(500,500,0));        //�����С���ɾ�����Щ
	floor->setRotation(core::vector3df(92,0,0));        //������ת�Ƕȣ�Լ90��ʹ֮����ȥΪˮƽ��
	sprintf(fname, "/Irrlicht/verfloor2.jpg");
	movieFilename = fname;
	floor->setMaterialTexture(0,driver->getTexture((gSdCardPath + movieFilename).c_str())); //����������
	floor->setMaterialFlag(video::EMF_LIGHTING,false);
	floor->setMaterialType(video::EMT_TRANSPARENT_ADD_COLOR);   //���ü�ɫ͸��ģʽ����ȥͼƬ��Χ�ĺ�ɫ����
	//floor->setVisible(false);

	/************** title ***************/
	/*
	sprintf(fname, "/Irrlicht/fonthaettenschweiler.bmp");
	movieFilename = fname;
	font = device->getGUIEnvironment()->getFont((gSdCardPath + movieFilename).c_str());
	scene::ISceneNode* title = smgr->addTextSceneNode(font, L"3D User Guide Beta",
	video::SColor(255,255,255,255), 0,core::vector3df(20,12,160));
    */

	//���뻷���⣬�����׹�ʹ�������������ɼ�
	smgr->setAmbientLight(video::SColor(0,250,250,250));

    //����active node�ĸ�����ڵ�
	frontLight = new scene::IMeshSceneNode*[5];
	for(int i =0;i<menuNum;++i){
		frontLight[i] = smgr->addCubeSceneNode(1, 0,ID_IsNotPickable);  //��ڵ㲻��ѡ��
		frontLight[i]->setPosition(core::vector3df(0,0,170));           //���ù�ڵ�λ�ã����ӵ�ǰ��
		frontLight[i]->setScale(core::vector3df(10,10,0));              //���ù�ڵ��С
		//sprintf(fname, "/Irrlicht/light0%d.jpg",i);
		sprintf(fname, "/Irrlicht/light0%d.jpg",i);
		movieFilename = fname;
		frontLight[i]->setMaterialTexture(0,driver->getTexture((gSdCardPath + movieFilename).c_str())); //���ù�ڵ����
		frontLight[i]->setMaterialFlag(video::EMF_LIGHTING,false);      //ʹ��ڵ�Ϊ����˵��֪
		//frontLight[i]->setMaterialFlag(video::EMF_ZWRITE_ENABLE, false);
		frontLight[i]->setMaterialType(video::EMT_TRANSPARENT_ADD_COLOR);//���ü�ɫ͸��ģʽ����ȥͼƬ��Χ�ĺ�ɫ����
		frontLight[i]->setVisible(false);       //��ʱ�رչ�ڵ�
	}

	collMan = smgr->getSceneCollisionManager(); //������ײ������

    //��ʼ������ȫ�ֱ���
	frameNo = new int[sceneLayout->getAmount()];
	for (int k=0; k<sceneLayout->getAmount(); ++k){
		frameNo[k]=1;
	}
	frameElapsed  = 0;
	activeNode = -1;
	frameCounter = 0;
	currentNode = 0;

	driver->getMaterial2D().TextureLayer[0].BilinearFilter=true;
	driver->getMaterial2D().AntiAliasing=video::EAAM_FULL_BASIC;

    //��������������
    receiver = new AndroidEventReceiver(context);

    //�豸���������
    device->setEventReceiver(receiver);
}
static int counter = 0;
static bool disLight = true;
static int delay = 0;
static bool doDelay = true;

//����ѭ�����ź���
void nativeDrawIteration2DMovie()
{
	device->run();
	const u32 now = device->getTimer()->getTime();  //��ȡ��ǰʱ��

	if (counter == 0) { //δ��ʼ�����棬����ó�ʼ������
		init2DMovie();
	}

	u16 dir = receiver->getDI();    //��ȡ����ʶ�𵽵ķ���
	core::line3d<f32> ray;          //��ײ�������õ��ĴӴ���������Զ������Ĺ���

	ray = collMan->getRayFromScreenCoordinates(
            irr::core::position2di(receiver->getX(), receiver->getY()),
            camera);        //ray��ʼ����Ϊ�ӵ�ʹ������ڵ�ֱ��

	core::vector3df intersection;   //ray��node��ײ��λ��
	core::triangle3df hitTriangle;  //��ײ������������
	scene::ISceneNode * selectedSceneNode =
		collMan->getSceneNodeAndCollisionPointFromRay(
		ray,
		intersection,
		hitTriangle,
		IDFlag_IsPickable, //ʹ�ÿɱ�ѡ�еĽڵ���ܱ���ײʶ��
		0);                //��ȡѡ�еĽڵ�

	const f32 frameDeltaTime = (f32)(now - then) / 1000.f; // Time in seconds
	then = now;
	frameElapsed += frameDeltaTime;
	if (activeNode == -1 || !wallNodes[activeNode]->isActive()) {
		for (s32 i = 0; i < sceneLayout->getAmount(); i++) {
			if(wallNodes[i]->isActive()) {
				activeNode = i;
			}
		}
	}	//get the index of the active node

	if (dir>0) {    //�л���������
		//s32 newNodeId = selectedSceneNode->getID() >> 2;
		s32 newNodeId;
		if(dir==1) newNodeId = (currentNode+1)%menuNum; //��next����ת��
		else newNodeId = (currentNode+3)%menuNum;       //��previous����ת��
		__android_log_print(ANDROID_LOG_INFO, "selectedNode", "ID:%d",newNodeId);
		s32 absOffset = abs(newNodeId + sceneLayout->getAmount() - currentNode) % sceneLayout->getAmount(); //����ת��ƫ����
		if (absOffset > (sceneLayout->getAmount() >> 1)) {  //��previous������ת
			turnPrev(sceneLayout, wallNodes, absOffset);
		} else {    //��next������ת
			turnNext(sceneLayout, wallNodes, sceneLayout->getAmount() - absOffset);
		}

		if(currentNode != newNodeId && !disLight){  //��ǰ�ڵ㲻Ϊ�½ڵ��Ҹ�����δϨ��
			for(int i=0;i<menuNum;++i) {
				frontLight[i]->setVisible(false);   //Ϩ�������
			}
			disLight = true;                        //��������Ϩ��
			stopPlay(currentNode);                  //��ǰ�ڵ㶯��ֹͣ
		}
		__android_log_print(ANDROID_LOG_INFO, "selectedNode", "currentNode:%d",currentNode);
		currentNode = newNodeId;                    //��ǰ�ڵ㣨���µ�active node��IDΪnewNodeId
		receiver->setDI(0);                         //������ʶ�����ʼ��Ϊ0
		doDelay = true;                             //�ӳٸ���������
	}

	else if (receiver->IsKeyPressed() && selectedSceneNode && !disLight) {  //�д�����ѡ�нڵ�ͬʱ������δϨ��
		s32 newNodeId = selectedSceneNode->getID() >> 2;    //��ȡ��active node��ID,����ײ����⵽�Ľڵ�
		s32 absOffset = abs(newNodeId + sceneLayout->getAmount() - currentNode) % sceneLayout->getAmount(); //����ת��ƫ����
		if(absOffset == 2) {
			newNodeId = currentNode;
		}											//�޸���ѡ����ڵ�bug
		else if(absOffset !=0) if (absOffset > (sceneLayout->getAmount() >> 1)) {
			turnPrev(sceneLayout, wallNodes, absOffset);    //��previous������ת
		} else {
			turnNext(sceneLayout, wallNodes, sceneLayout->getAmount() - absOffset); //��next������ת
		}
		if(currentNode != newNodeId && !disLight){  //��ǰ�ڵ㲻Ϊ�½ڵ��Ҹ�����δϨ��
			for(int i=0;i<menuNum;++i) {
				frontLight[i]->setVisible(false);   //Ϩ�������
			}
			disLight = true;                        //��������Ϩ��
			doDelay = true;                         //�ӳٸ���������
			stopPlay(currentNode);                  //��ǰ�ڵ㶯��ֹͣ
		}
		currentNode = newNodeId;                    //��ǰ�ڵ㣨���µ�active node��IDΪnewNodeId
	}

	for (s32 i = 0; i < sceneLayout->getAmount(); i++) {
		wallNodes[i]->touch(frameDeltaTime * 1000.f);   //���ýڵ��˶����λ�á���С��ѡ��Ƕȵ���Ϣ������ʱ��Ϣ���䣩
	}
	if(doDelay){
		delay ++;
	}
	/*if(wallNodes[activeNode]->getPosTimeToGo()<= frameDeltaTime * 1000.f && disLight){
		doDelay = true;
	}*/
	if(doDelay&&((int)(wallNodes[activeNode]->getSceneNode()->getRotation().Y)%360 == 0)
                &&(wallNodes[activeNode]->getParentNode()->getPosition().X==0)){    //��active node��ת������ǰ��
		frontLight[activeNode]->setVisible(true);   //�򿪸�����
		startPlay(activeNode);                      //active node��ģ�Ͷ�����ʼ��
		disLight = false;                           //��ʶ�������ѵ���
		delay = 0;                                  //delay��������
		doDelay = false;                            //�ӳٸ�����ر�
	}
	doingPlay(activeNode);                          //active node��ģ�Ͷ��������˶�
	driver->beginScene(true, true, video::SColor(0,0,0,0)); //��������Ϊ��ɫ
	smgr->drawAll();                                //������������
	driver->endScene();

	//count����ѭ���Լ�
	counter ++ ;
	if (counter > 200000) {
		counter = 1;
	}
	//guienv->drawAll();
}


void nativeDrawIteration()
{
	nativeDrawIteration2DMovie();
}

