/*Notice:
 irrLoadAnimatedMesh
 LoadTexture
 LoadTextureToMemory
 ������������õĶ�ȡ���ʵĺ��������ṩ��Ϊ����Ľӿ�
 ���������õĶ�ȡ���ʺ�ģ�͵ĺ�������ֻ�ṩ�򵥽ӿڣ�����ʹ�÷��������irrlicht������
  */

#include "IrrObjLoader.h"
using namespace irr;

using namespace os;
using namespace core;
using namespace scene;
using namespace video;
using namespace io;
using namespace gui;

/*initiate Loader
@param
	mdevice   - the device pointer  created by IrrMain*/
void IrrObjLoader::initObjLoader(IrrlichtDevice *mdevice)
{
		device = mdevice;
		driver = device->getVideoDriver();
	    smgr = device->getSceneManager();
		guienv = device->getGUIEnvironment();
 }

/*Load Animated Mesh
@param
	meshFileName	-the name of the mesh file
	textureFileName	   - the name of the texture file 
	position	- the position of loaded mesh
	scale	-the scale of the  loaded mesh*/
IAnimatedMeshSceneNode *IrrObjLoader::irrLoadAnimatedMesh(stringc meshFileName,
																		stringc textureFileName,
																		vector3df position, 
																		vector3df scale)
{
	IAnimatedMeshSceneNode *nodeSydney = NULL;
	IAnimatedMesh* mesh = smgr->getMesh( (meshFileName).c_str() );
	        if (!mesh)
	        {
	                device->drop();
					__android_log_print(ANDROID_LOG_INFO, "Irrlicht", "cannot get Mesh");
					return NULL;
	        }
	nodeSydney = smgr->addAnimatedMeshSceneNode( mesh );
	nodeSydney->setPosition(position);
	nodeSydney->setScale(scale);

	if (nodeSydney)
			        {
			                nodeSydney->setMaterialFlag(EMF_LIGHTING, true);
			                //--// no animation
			                //--nodeSydney->setMD2Animation(scene::EMAT_STAND);
			                // with animation
		                    //nodeSydney->setMD2Animation(scene::EMAT_RUN);
			                nodeSydney->setMaterialTexture( 0, driver->getTexture((textureFileName).c_str()) );
			        }else{
			        	__android_log_print(ANDROID_LOG_INFO, "Irrlicht", "cannot add MeshNode");
			        	return NULL;
			        }


	return nodeSydney;

}



IAnimatedMeshSceneNode *IrrObjLoader::irrLoadAnimatedMesh(stringc meshFileName,
																		vector3df position,
																		vector3df scale)
{
	IAnimatedMeshSceneNode *nodeSydney = NULL;
	IAnimatedMesh* mesh = smgr->getMesh( (meshFileName).c_str() );
	        if (!mesh)
	        {
	                device->drop();
					__android_log_print(ANDROID_LOG_INFO, "Irrlicht", "cannot get Mesh");
					return NULL;
	        }
	nodeSydney = smgr->addAnimatedMeshSceneNode( mesh );
	nodeSydney->setPosition(position);
	nodeSydney->setScale(scale);
	return nodeSydney;


}

void IrrObjLoader::LoadTexture(stringc textureFileName,IAnimatedMeshSceneNode *nodeSydney)
{

	if (nodeSydney)
		        {
		                nodeSydney->setMaterialFlag(EMF_LIGHTING, true);
		                //--// no animation
		                //--nodeSydney->setMD2Animation(scene::EMAT_STAND);
		                // with animation
	                    //nodeSydney->setMD2Animation(scene::EMAT_RUN);
		                nodeSydney->setMaterialTexture( 0, driver->getTexture((textureFileName).c_str()) );
		        }else{
		        	__android_log_print(ANDROID_LOG_INFO, "Irrlicht", "cannot add MeshNode");
		        	return;
		        }

}
void IrrObjLoader::LoadTextureToMemory(stringc billTexture)
{
	 driver->getTexture(billTexture);
	    return;

}
