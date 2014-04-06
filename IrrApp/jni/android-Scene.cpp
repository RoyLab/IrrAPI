#include <jni.h>
#include <irrlicht.h>
#include "android-global.h"

#ifdef LOG_TAG
#undef LOG_TAG
#endif

#define LOG_TAG "NativeScene"
#include "COGLESDriver.h"
extern "C"
{
	//set background color
	void Java_zte_irrlib_scene_Scene_nativeSetClearColor(
		JNIEnv *env, jobject defaultObj, jint r, jint g, jint b, jint a)
	{
		backColor = SColor(a,r,g,b);
	}
	 
	int Java_zte_irrlib_scene_Scene_nativeDrawImage(
		JNIEnv *env, jobject defaultObj, jstring path,
		jobject des, jdouble left, jdouble top, jdouble right, 
		jdouble bottom, jboolean alpha)
	{
		const char *msg = env->GetStringUTFChars(path,0);
		ITexture* tex = driver->getTexture(msg);
		if (!tex) 
		{
			LOGE("Cannot find Image: %s", msg);
			env->ReleaseStringUTFChars(path, msg);
			return -1;
		}
		env->ReleaseStringUTFChars(path, msg);
		dimension2d<u32> size = tex->getOriginalSize();
		driver->draw2DImage(tex, utils->createrectiFromRect4i(env, des), 
			recti(left*size.Width, top*size.Height, right*size.Width, bottom*size.Height),
			0, 0, alpha);
		return 0;
	}
	
	int Java_zte_irrlib_scene_Scene_nativeDrawBitmap(
		JNIEnv *env, jobject defaultObj, jstring jname, jobject jbitmap,
		jobject des, jdouble left, jdouble top, jdouble right, 
		jint bottom, jboolean alpha)
	{
		const char *name = env->GetStringUTFChars(jname,0);
		ITexture *tex = driver->getTexture(name);
		if (!tex)
		{
			IImage *image = utils->createImageFromBitmap(env, jbitmap);
			if (!image)
			{
				env->ReleaseStringUTFChars(jname,name);
				return -1;
			}
			tex = driver->addTexture(name, image, 0);
			image->drop();
		}
		env->ReleaseStringUTFChars(jname,name);
		dimension2d<u32> size = tex->getOriginalSize();
		driver->draw2DImage(tex, utils->createrectiFromRect4i(env, des), 
			recti(left*size.Width, top*size.Height, right*size.Width, bottom*size.Height),
			0, 0, alpha);
		return 0;
	}
	
	void Java_zte_irrlib_scene_Scene_nativeDrawRectangle(
		JNIEnv *env, jobject defaultObj, 
		jint left, jint up, jint right, jint bottom, 
		jint r, jint g, jint b, jint transparent)
	{
		driver->draw2DRectangle(SColor(transparent,r,g,b),
				rect<s32>(left, up, right, bottom));
	}
	
	void Java_zte_irrlib_scene_Scene_nativeDrawRectangleChrome(
		JNIEnv *env, jobject defaultObj,
		jobject jrec, jobject c1, jobject c2, jobject c3, jobject c4)
	{
		driver->draw2DRectangle(
			utils->createrectiFromRect4i(env, jrec), 
			utils->createSColorFromColor4i(env, c1),
			utils->createSColorFromColor4i(env, c2), 
			utils->createSColorFromColor4i(env, c3), 
			utils->createSColorFromColor4i(env, c4)
			);
	}
	
	void Java_zte_irrlib_scene_Scene_nativeDrawText(
		JNIEnv *env, jobject defaultObj,jstring text, jint left, jint up, jint r, jint g, jint b, jint a)
	{
		IGUIFont* font = device->getGUIEnvironment()->getFont(_builtInFontPath);
		if (!font){
			LOGE("Font not found.");
			return;
		}
		const char *msg = env->GetStringUTFChars(text,0);
		font->draw(msg, rect<s32>(left,up,left+100,up+100), SColor(a,r,g,b));
		env->ReleaseStringUTFChars(text, msg);
	}
	
	void Java_zte_irrlib_scene_Scene_nativeSmgrDrawAll(
		JNIEnv *env, jobject defaultObj)
	{
		//((COGLES1Driver*)device)->testGLError();
		smgr->drawAll();
	}
	
	void Java_zte_irrlib_scene_Scene_nativeSetAmbientLight(
		JNIEnv *env, jobject defaultObj, jint r, jint g, jint b, jint a)
	{
		smgr->setAmbientLight(video::SColor(a,r,g,b));
	}
	
	int Java_zte_irrlib_scene_Scene_nativeSetActiveCamera(
		JNIEnv *env, jobject defaultObj, jint id)
	{
		scene::ISceneNode* camera = smgr->getSceneNodeFromId(id);
		if (!camera)
		{
			WARN_NODE_NOT_FOUND(id, SetActiveCamera);
			return -1;
		}
		smgr->setActiveCamera((ICameraSceneNode *)camera);
		return 0;
	}
	
	int Java_zte_irrlib_scene_Scene_nativeGetTouchedSceneNode(
		JNIEnv *env, jobject defaultObj, jfloat x, jfloat y, jint root)
	{

		ISceneNode* rootNode;
		if (root == 0) rootNode = smgr->getRootSceneNode();
		else rootNode = smgr->getSceneNodeFromId(root);

		if (!rootNode)
		{
			WARN_NODE_NOT_FOUND(root, GetTouchedSceneNode);
			return -1;
		}
		ICameraSceneNode* camera = smgr->getActiveCamera();
		
		ISceneCollisionManager *collMgr = smgr->getSceneCollisionManager();
		core::line3d<f32> ray = collMgr->getRayFromScreenCoordinates(
            irr::core::position2di(x, y), camera); 
		core::vector3df intersection; 
		core::triangle3df hitTriangle;  
		scene::ISceneNode * selectedSceneNode =
		collMgr->getSceneNodeAndCollisionPointFromRay(
			ray,
			intersection,	//intersection position
			hitTriangle,	//hit triangle
			0,				//idBitMask: 0 to test all nodes
			rootNode		//root node to search from
		);
		if(selectedSceneNode) return selectedSceneNode->getID();
		else return 0;
	}

	void Java_zte_irrlib_scene_Scene_nativeClear(JNIEnv *env, jobject defaultObj){
		smgr->clear();
		LOGI("All node cleared!");
	}
	
	int Java_zte_irrlib_scene_Scene_nativeAddEmptySceneNode(
		JNIEnv*  env, jobject defaultObj, 
		jdouble x, jdouble y, jdouble z, jint id, jint parent, jboolean isLight)
	{
		ISceneNode* node = NULL;
		ISceneNode* parentNode = NULL;
		if(parent != 0){
			parentNode = smgr->getSceneNodeFromId(parent);
			if (!parentNode) 
			{
				WARN_PARENT_NOT_FOUND(parent, AddEmptySceneNode);
				return -2;
			}
		}
		node = smgr->addEmptySceneNode(parentNode,id);

		if (node)
		{
			node->setPosition(vector3df(x, y, z));
			if (!isLight) node->setMaterialFlag(video::EMF_LIGHTING, false);
			return 0;
		}
		else 
		{
			ERROR_ADD_FAILD(id, AddEmptySceneNode);
			return -1;
		}
	}

	int Java_zte_irrlib_scene_Scene_nativeAddCubeSceneNode(
		JNIEnv*  env, jobject defaultObj, jdouble x, jdouble y, jdouble z,
		jdouble size, jint id, jint parent, jboolean isLight)
	{
		core::vector3df pos = core::vector3df(x,y,z);

		ISceneNode* node = NULL;
		ISceneNode* parentNode = NULL;
		if(parent != 0){
			parentNode = smgr->getSceneNodeFromId(parent);
			if (!parentNode) 
			{
				WARN_PARENT_NOT_FOUND(parent, AddCubeSceneNode);
				return -2;
			}
		}
		node = smgr->addCubeSceneNode(size,parentNode,id,pos);

		if (node)
		{
			if (!isLight) node->setMaterialFlag(video::EMF_LIGHTING, false);
			return 0;
		}
		else 
		{
			ERROR_ADD_FAILD(id, AddCubeSceneNode);
			return -3;
		}
	}

	int Java_zte_irrlib_scene_Scene_nativeAddSphereSceneNode(
		JNIEnv*  env, jobject defaultObj, jdouble x, jdouble y, jdouble z,
		jdouble radius, jint polyCount,  jint id, jint parent, jboolean isLight)
	{
		core::vector3df pos = core::vector3df(x,y,z);

		ISceneNode* node = NULL;
		ISceneNode* parentNode = NULL;
		if(parent != 0){
			parentNode = smgr->getSceneNodeFromId(parent);
			if (!parentNode)
			{
				WARN_PARENT_NOT_FOUND(parent, AddCubeSceneNode);
				return -2;
			}
		}
		node = smgr->addSphereSceneNode(radius, polyCount, parentNode,id,pos);

		if (node)
		{
			if (!isLight) node->setMaterialFlag(video::EMF_LIGHTING, false);
			return 0;
		}
		else
		{
			ERROR_ADD_FAILD(id, AddCubeSceneNode);
			return -3;
		}
	}

	int Java_zte_irrlib_scene_Scene_nativeAddMeshSceneNode(
		JNIEnv*  env, jobject defaultObj, jstring path, 
		jdouble x, jdouble y, jdouble z, jint id, 
		jint parent, jboolean isLight, jboolean optimizedByOctree)
	{
		core::vector3df pos = core::vector3df(x,y,z);

		const char *msg = env->GetStringUTFChars(path,0);
		IMesh* mesh = smgr->getMesh(io::path(msg));
		env->ReleaseStringUTFChars(path, msg);
		
		if (!mesh)
		{
			LOGW("Mesh not found!");
			return -4;
		}

		ISceneNode* node = NULL;
		ISceneNode* parentNode = NULL;
		if(parent != 0){
			parentNode = smgr->getSceneNodeFromId(parent);
			if (!parentNode) 
			{
				WARN_PARENT_NOT_FOUND(parent, AddMeshSceneNode);
				return -1;
			}
		}
		if (optimizedByOctree) node = smgr->addOctreeSceneNode(mesh,parentNode,id);
		else node = smgr->addMeshSceneNode(mesh,parentNode,id);

		if (node)
		{
			node->setPosition(pos);
			if (!isLight) node->setMaterialFlag(video::EMF_LIGHTING, false);
			return 0;
		}
		else 
		{
			ERROR_ADD_FAILD(id, AddMeshSceneNode);
			return -3;
		}
	}

	int Java_zte_irrlib_scene_Scene_nativeAddTextNode(
		JNIEnv*  env, jobject defaultObj, jstring text,
		jdouble x, jdouble y, jdouble z, jdouble size, 
		jint id, jint parent, jboolean isLight)
	{
		/*Original Func:
		ITextSceneNode* addTextSceneNode(gui::IGUIFont* font, const wchar_t* text,
			video::SColor color=video::SColor(100,255,255,255),
			ISceneNode* parent = 0, const core::vector3df& position = core::vector3df(0,0,0),
			s32 id=-1)
		*/

		ITextSceneNode* node = 0;
		ISceneNode *parentNode = 0;
		IGUIFont* font = device->getGUIEnvironment()->getFont(_builtInFontPath);
		
		if (!font){
			LOGE("Font not found.");
			return -3;
		}
		
		if(parent != 0){
			scene::ISceneNode* parentNode = smgr->getSceneNodeFromId(parent);
			if (!parentNode)
			{
				WARN_PARENT_NOT_FOUND(parent, AddTextNode);
				return -2;
			}
		}
		
		const char *msg = env->GetStringUTFChars(text,0);
		stringw content(msg);
		node = smgr->addTextSceneNode(font, content.c_str(), SColor(255,255,255,255),parentNode, vector3df(x, y, z), id);
		env->ReleaseStringUTFChars(text, msg);
		
		if (node)
		{
			if (!isLight) node->setMaterialFlag(video::EMF_LIGHTING, false);
			return 0;
		}
		else
		{
			ERROR_ADD_FAILD(id, AddTextNode);
			return -3;
		}
	}

	int Java_zte_irrlib_scene_Scene_nativeAddCameraSceneNode(
		JNIEnv*  env, jobject defaultObj, jdouble px, jdouble py, jdouble pz,
		jdouble lx, jdouble ly, jdouble lz, jboolean isActive, jint id, jint parent, jboolean isLight)
	{
		core::vector3df pos = core::vector3df(px,py,pz);
		core::vector3df lookat = core::vector3df(lx,ly,lz);

		ISceneNode* node = NULL;
		ISceneNode* parentNode = NULL;
		if(parent != 0){
			parentNode = smgr->getSceneNodeFromId(parent);
			if (!parentNode) 
			{
				WARN_PARENT_NOT_FOUND(parent, AddCameraSceneNode);
				return -1;
			}
		}
		node = smgr->addCameraSceneNode(parentNode,pos,lookat,id);

		if (node)
		{
			if (!isLight) node->setMaterialFlag(video::EMF_LIGHTING, false);
			return 0;
		}
		else 
		{
			ERROR_ADD_FAILD(id, AddCameraSceneNode);
			return -3;
		}
	}

	int Java_zte_irrlib_scene_Scene_nativeAddBillboardSceneNode(
		JNIEnv *env, jobject defaultObj, jdouble px, jdouble py, jdouble pz,
		jdouble sx, jdouble sy, jint id, jint parent, jboolean isLight)
	{
		core::vector3df pos = core::vector3df(px,py,pz);
		core::dimension2d<f32> size = core::dimension2d<f32>(sx,sy);
		
		ISceneNode* node = NULL;
		ISceneNode* parentNode = NULL;
		if(parent != 0){
			parentNode = smgr->getSceneNodeFromId(parent);
			if (!parentNode) 
			{
				WARN_PARENT_NOT_FOUND(parent, AddBillboardSceneNode);
				return -1;
			}
		}
		node = smgr->addBillboardSceneNode(parentNode,size,pos,id);

		if (node)
		{
			if (!isLight) node->setMaterialFlag(video::EMF_LIGHTING, false);
			return 0;
		}
		else 
		{
			ERROR_ADD_FAILD(id, AddBillboardSceneNode);
			return -3;
		}
	}

	int Java_zte_irrlib_scene_Scene_nativeAddLightSceneNode(
		JNIEnv *env, jobject defaultObj, jdouble px, jdouble py, jdouble pz,
		jdouble radius, jint r, jint g, jint b, jint id, jint parent, jboolean isLight)
	{	
		ISceneNode* node = NULL;
		ISceneNode* parentNode = NULL;
		if(parent != 0){
			parentNode = smgr->getSceneNodeFromId(parent);
			if (!parentNode) 
			{
				WARN_PARENT_NOT_FOUND(parent, AddLightSceneNode);
				return -2;
			}
		}
		node = smgr->addLightSceneNode(parentNode,vector3df(px,py,pz),SColor(0xff,r,g,b),radius,id);
		
		if (node)
		{
			if (!isLight) node->setMaterialFlag(video::EMF_LIGHTING, false);
			return 0;
		}
		else 
		{
			ERROR_ADD_FAILD(id, AddLightSceneNode);
			return -3;
		}
	}

	int Java_zte_irrlib_scene_Scene_nativeAddAnimateMeshSceneNode(
		JNIEnv*  env, jobject defaultObj, jstring path,
		jdouble x, jdouble y, jdouble z, 
		jint id, jint parent, jboolean isLight)
	{
		const char *msg = env->GetStringUTFChars(path,0);
		scene::IAnimatedMesh* mesh = smgr->getMesh(msg);
		env->ReleaseStringUTFChars(path, msg);
		
		if (!mesh)
		{
			LOGW("Mesh not found!");
			return -4;
		}

		
		ISceneNode* node = NULL;
		ISceneNode* parentNode = NULL;
		if(parent != 0){
			parentNode = smgr->getSceneNodeFromId(parent);
			if (!parentNode) 
			{
				WARN_PARENT_NOT_FOUND(parent, AddLightSceneNode);
				return -2;
			}
		}		
		node = smgr->addAnimatedMeshSceneNode(mesh,0,id,vector3df(x, y, z));
		if (node)
		{
			if (!isLight) node->setMaterialFlag(video::EMF_LIGHTING, false);
			return 0;
		}
		else 
		{
			ERROR_ADD_FAILD(id, AddLightSceneNode);
			return -3;
		}
	}

	int Java_zte_irrlib_scene_Scene_nativeAddParticleSystemSceneNode(
		JNIEnv *env, jobject defaultObj, jdouble x, jdouble y, jdouble z,
		jboolean withDefaultEmitter, jint id, jint parent, jboolean isLight)
	{
		//1.25: must use default emitter now
		if(!withDefaultEmitter) return -1;

		core::vector3df pos = core::vector3df(x,y,z);
		scene::IParticleSystemSceneNode* ps = NULL;
		if(parent != 0){
			scene::ISceneNode* parentNode = smgr->getSceneNodeFromId(parent);
			ps = smgr->addParticleSystemSceneNode(withDefaultEmitter, parentNode, id, pos);
		}
		else ps = smgr->addParticleSystemSceneNode(withDefaultEmitter, 0, id, pos);

		//1.25: use default affector currently
		scene::IParticleAffector* paf = ps->createFadeOutParticleAffector();
		ps->addAffector(paf);
		paf->drop();

		if(ps) return 0;
		else return -1;
	}

	int Java_zte_irrlib_scene_Scene_nativeAddCometTailSceneNode(
			JNIEnv *env, jobject defaultObj, jdouble x, jdouble y, jdouble z,
			jdouble radius, jdouble length, jdouble dx, jdouble dy, jdouble dz,
			jint id, jint parent, jboolean isLight)
	{
		core::vector3df pos = core::vector3df(x,y,z);
		scene::IParticleSystemSceneNode* ps = NULL;
		if(parent != 0){
			scene::ISceneNode* parentNode = smgr->getSceneNodeFromId(parent);
			ps = smgr->addParticleSystemSceneNode(true, parentNode, id, pos);
		}
		else ps = smgr->addParticleSystemSceneNode(true, 0, id, pos);
		scene::IParticleEmitter* emCT = ps->createCylinderEmitter(
				/*center*/			vector3df(0,0,0),
				/*radius*/			radius,
				/*normal*/		vector3df(0, 0, 0),
				/*length*/			length,
				/*outlineOnly*/	false,
				/*direction*/		vector3df(dx,dy,dz),
				/*minNum*/		1000,
				/*maxNum*/	2000,
				/*minColor*/	SColor(255,0,0,0),
				/*maxColor*/	SColor(255,255,255,255),
				/*minLife*/		500,
				/*maxLife*/		1000,
				/*maxAngle*/	20,
				/*minSize*/		dimension2df(1.0,1.0),
				/*maxSize*/		dimension2df(2.0,2.0)
		);
		ps->setEmitter(emCT);
		emCT->drop();
		ps->setMaterialType(video::EMT_TRANSPARENT_ADD_COLOR);
		ps->setMaterialFlag(video::EMF_LIGHTING,false);
		ps->setMaterialFlag(video::EMF_ZWRITE_ENABLE,false);
		scene::IParticleAffector* fadeAff = ps->createFadeOutParticleAffector(SColor(0,0,0,0),500);
		ps->addAffector(fadeAff);
		fadeAff->drop();

		if(ps) return 0;
		else return -1;
	}

	int Java_zte_irrlib_scene_Scene_nativeAddStarsParticleSceneNode(
				JNIEnv *env, jobject defaultObj, jdouble x, jdouble y, jdouble z,
				jdouble radius, jint id, jint parent, jboolean isLight)
		{
			core::vector3df pos = core::vector3df(x,y,z);
			scene::IParticleSystemSceneNode* ps = NULL;
			if(parent != 0){
				scene::ISceneNode* parentNode = smgr->getSceneNodeFromId(parent);
				ps = smgr->addParticleSystemSceneNode(true, parentNode, id, pos);
			}
			else ps = smgr->addParticleSystemSceneNode(true, 0, id, pos);
			scene::IParticleEmitter* emStars = ps->createSphereEmitter(
				/*center*/			vector3df(0,0,0),
				/*radius*/			radius,
				/*direction*/		vector3df(0.0,0.0,0.0),
				/*minNum*/		100,
				/*maxNum*/	200,
				/*minColor*/	SColor(0,0,0,0),
				/*maxColor*/	SColor(255,255,255,255),
				/*minTime*/		5000,
				/*maxTime*/		10000,
				/*maxAngle*/	0,
				/*minSize*/		dimension2df(1.0,1.0),
				/*maxSize*/		dimension2df(2.0,2.0)
			);

			ps->setEmitter(emStars);
			emStars->drop();
			ps->setMaterialType(video::EMT_TRANSPARENT_ADD_COLOR);
			ps->setMaterialFlag(video::EMF_LIGHTING,false);
			ps->setMaterialFlag(video::EMF_ZWRITE_ENABLE,false);
			scene::IParticleAffector* fadeAff = ps->createFadeOutParticleAffector(SColor(0,0,0,0),500);
			ps->addAffector(fadeAff);
			fadeAff->drop();

			if(ps) return 0;
			else return -1;
		}

	int Java_zte_irrlib_scene_Scene_nativeAddExplosionParticleSceneNode(
			JNIEnv *env, jobject defaultObj, jdouble x, jdouble y, jdouble z,
			jdouble radius, jdouble speed, jint id, jint parent, jboolean isLight)
	{
		core::vector3df pos = core::vector3df(x,y,z);
		scene::IParticleSystemSceneNode* ps = NULL;
		if(parent != 0){
			scene::ISceneNode* parentNode = smgr->getSceneNodeFromId(parent);
			ps = smgr->addParticleSystemSceneNode(true, parentNode, id, pos);
		}
		else ps = smgr->addParticleSystemSceneNode(true, 0, id, pos);
		scene::IParticleEmitter* emExp = ps->createSphereEmitter(
			/*center*/			vector3df(0,0,0),
			/*radius*/			radius,
			/*direction*/		vector3df(0.0,0.0,0.0),
			/*minNum*/		500,
			/*maxNum*/	1000,
			/*minColor*/	SColor(0,0,0,0),
			/*maxColor*/	SColor(255,255,255,255),
			/*minTime*/		500,
			/*maxTime*/		1000,
			/*maxAngle*/	0,
			/*minSize*/		dimension2df(1.0,1.0),
			/*maxSize*/		dimension2df(2.0,2.0)
		);

		ps->setEmitter(emExp);
		emExp->drop();
		ps->setMaterialType(video::EMT_TRANSPARENT_ADD_COLOR);
		ps->setMaterialFlag(video::EMF_LIGHTING,false);
		ps->setMaterialFlag(video::EMF_ZWRITE_ENABLE,false);
		ps->addAnimator(smgr->createDeleteAnimator(5000));
		scene::IParticleAffector* fadeAff = ps->createFadeOutParticleAffector(SColor(0,0,0,0),500);
		scene::IParticleAffector* attrAff = ps->createAttractionAffector(pos,speed,false);
		ps->addAffector(fadeAff);
		ps->addAffector(attrAff);
		fadeAff->drop();
		attrAff->drop();


		if(ps) return 0;
		else return -1;
	}
	void Java_zte_irrlib_scene_Scene_nativeRemoveNode(
		JNIEnv *env, jobject defaultObj, jint id)
	{
		ISceneNode* node = smgr->getSceneNodeFromId(id);
		if (!node) return;
			
		ISceneNode* parentNode = node->getParent();
		if (!parentNode) return;
			
		parentNode->removeChild(node);
	}
	
	int Java_zte_irrlib_scene_Scene_nativeGetMediaTextureId(
		JNIEnv *env, jobject defaultObj)
	{	
		if (_extTex)
		{
			return getOpenGLESTextureID(_extTex);
		}
		else
		{
			_extTex = driver->addTexture("<external>", 0);
			return getOpenGLESTextureID(_extTex);
		}
	}
		
	void Java_zte_irrlib_scene_Scene_nativeSetFontPath(
		JNIEnv *env, jobject defaultObj, jstring path)
	{
		const char* ch = env->GetStringUTFChars(path, 0);
		strcpy(_builtInFontPath, ch);
		env->ReleaseStringUTFChars(path, ch);
	}
	
	void Java_zte_irrlib_scene_Scene_nativeRemoveTexture(
		JNIEnv *env, jobject defaultObj, jstring name)
	{
		const char* ch = env->GetStringUTFChars(name, 0);
		driver->removeTexture(driver->findTexture(ch));
		env->ReleaseStringUTFChars(name, ch);
	}
	
	void Java_zte_irrlib_scene_Scene_nativeRemoveUnusedMesh(
		JNIEnv *env, jobject defaultObj)
	{
		smgr->getMeshCache()->clearUnusedMeshes();
	}
	
	void Java_zte_irrlib_scene_Scene_nativeRemoveMesh(
		JNIEnv *env, jobject defaultObj, jstring path)
	{
		const char* ch = env->GetStringUTFChars(path, 0);
		smgr->getMeshCache()->removeMesh(smgr->getMesh(ch));
		env->ReleaseStringUTFChars(path, ch);
	}
	
	int Java_zte_irrlib_scene_Scene_nativeApplyNewExternalTex(
		JNIEnv *env, jobject defaultObj, jstring name)
	{
		const char* ch = env->GetStringUTFChars(name, 0);
		ITexture* tex = driver->addTexture(ch, 0);
		if (!tex)
		{
			LOGE("Cannot apply new tex: %s", ch);
			return -1;
		}
		env->ReleaseStringUTFChars(name, ch);
		return utils->getGLTexId(tex);
	}

	jboolean Java_zte_irrlib_scene_Scene_nativeUploadBitmap(
		JNIEnv *env, jobject defaultObj, jstring jname, jobject jbitmap)
	{
		const char* ch = env->GetStringUTFChars(jname, 0);
		ITexture* tex = driver->findTexture(ch);
		if (tex)
		{
			LOGW("Texture(%s) is already exists", ch);
			return false;
		}
		
		IImage *image = utils->createImageFromBitmap(env, jbitmap);
		if (!image)
		{
			env->ReleaseStringUTFChars(jname, ch);
			return false;
		}
		tex = driver->addTexture(ch, image, 0);
		image->drop();
		
		env->ReleaseStringUTFChars(jname, ch);
		return true;
	}
}
	
	
