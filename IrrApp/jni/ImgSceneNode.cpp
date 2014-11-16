#include "ImgSceneNode.h"

CImgSceneNode::CImgSceneNode(scene::ISceneNode* parent, scene::ISceneManager* mgr, s32 id,s32 iWidth/* = 12*/,s32 iHeight/*=12*/)
	: scene::ISceneNode(parent, mgr, id),m_iWidth(iWidth),m_iHeight(iHeight)
{
	Material.Wireframe = false;
	Material.Lighting = false;


	Vertices[0] = video::S3DVertex(0,0,0, 0,0,0,
		video::SColor(255,255,255,255), 0, 1);
	Vertices[1] = video::S3DVertex(0,m_iHeight,0, 0,0,0,
		video::SColor(255,255,255,255), 0, 0);
	Vertices[2] = video::S3DVertex(m_iWidth,m_iHeight,0, 0,0,0,
		video::SColor(255,255,255,255), 1, 0);
	Vertices[3] = video::S3DVertex(m_iWidth,0,0,0,0,0,
		video::SColor(255,255,255,255), 1, 1);
}

CImgSceneNode::~CImgSceneNode(void)
{
}


void CImgSceneNode::OnRegisterSceneNode()
{
	if (IsVisible)
		SceneManager->registerNodeForRendering(this);

	ISceneNode::OnRegisterSceneNode();
}

void CImgSceneNode::render()
{
	video::IVideoDriver* driver = SceneManager->getVideoDriver();
	driver->setMaterial(Material);
	driver->setTransform(video::ETS_WORLD, AbsoluteTransformation);
	u16 indices[] = {0,1,2,0,2,3};
	driver->drawVertexPrimitiveList(&Vertices[0], 4, &indices[0], 2, video::EVT_STANDARD, scene::EPT_TRIANGLES, video::EIT_16BIT);
}

const core::aabbox3d<f32>& CImgSceneNode::getBoundingBox() const
{
	return Box;
}

u32 CImgSceneNode::getMaterialCount() const
{
	return 1;
}

video::SMaterial& CImgSceneNode::getMaterial(u32 i)
{
	return Material;
}