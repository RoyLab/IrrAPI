
#ifndef IMGSCENENODE_H
#define IMGSCENENODE_H

#include "irrlicht.h"
using namespace irr;


class CImgSceneNode : public scene::ISceneNode
{
public:
	CImgSceneNode(scene::ISceneNode* parent, scene::ISceneManager* mgr, s32 id,s32 iWidth = 12,s32 iHeight=12);
	~CImgSceneNode(void);

	virtual void OnRegisterSceneNode();
	
	virtual void render();

	virtual const core::aabbox3d<f32>& getBoundingBox() const;

	virtual u32 getMaterialCount() const;
	virtual video::SMaterial& getMaterial(u32 i);

private:
	core::aabbox3d<f32> Box;
	video::S3DVertex Vertices[4];
	video::SMaterial Material;
	s32 m_iWidth;
	s32 m_iHeight;
};

#endif