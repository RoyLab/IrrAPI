// Copyright (C) 2002-2009 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CShadowVolumeSceneNode.h"
#include "ISceneManager.h"
#include "IMesh.h"
#include "IVideoDriver.h"
#include "SLight.h"

#define hashROOT 5987

namespace irr
{
namespace scene
{


//! constructor
CShadowVolumeSceneNode::CShadowVolumeSceneNode(const IMesh* shadowMesh, ISceneNode* parent,
		ISceneManager* mgr, s32 id, bool zfailmethod, f32 infinity)
: IShadowVolumeSceneNode(parent, mgr, id),
	ShadowMesh(0), IndexCount(0), VertexCount(0), ShadowVolumesUsed(0),
	Infinity(infinity), UseZFailMethod(zfailmethod)
{
	#ifdef _DEBUG
	setDebugName("CShadowVolumeSceneNode");
	#endif
	setShadowMesh(shadowMesh);
	setAutomaticCulling(scene::EAC_OFF);
}


//! destructor
CShadowVolumeSceneNode::~CShadowVolumeSceneNode()
{
	if (ShadowMesh)
		ShadowMesh->drop();

	for (u32 i=0; i<ShadowVolumes.size(); ++i)
		delete [] ShadowVolumes[i].vertices;
}


void CShadowVolumeSceneNode::createShadowVolume(const core::vector3df& light)
{
	//SShadowVolume ��һ���ṹ�壬����һ��vector3df���飬һ��count��һ��size
	SShadowVolume* svp = 0;

	// builds the shadow volume and adds it to the shadow volume list.
	
	if (ShadowVolumes.size() > ShadowVolumesUsed)
	{
		// get the next unused buffer
		//�ҵ���һ��û��ʹ�õ�buffer
		//ÿ�ζ��ҵ���һ��û��ʹ�õ�buffer��Ҳ����ShadowVolumes����ĩβԪ�ص���һ��

		//__android_log_print(ANDROID_LOG_INFO,"Enron","Index:%d", IndexCount);
		svp = &ShadowVolumes[ShadowVolumesUsed];
		if (svp->size >= IndexCount*5)
		{
			svp->count = 0;
			svp->realcount = 0;
		}
		else
		{
			svp->size = IndexCount*8;
			svp->count = 0;
			svp->realcount = 0;
			delete [] svp->vertices;
			svp->vertices = new core::vector3df[svp->size];
			//culling
			svp->cullingFlag = new u16[svp->size];

			//for hash
			svp->hashValueY = new u16[hashROOT];
			svp->hashValueX = new u16[hashROOT];
			svp->isEmpty = new bool[hashROOT];

		}

		//ʹ�õ�һ��buffer
		++ShadowVolumesUsed;
	}
	else
	{
		// add a buffer
		SShadowVolume tmp;
		// lets make a rather large shadowbuffer
		tmp.size = IndexCount*8;
		tmp.count = 0;
		tmp.realcount = 0;
		tmp.vertices = new core::vector3df[tmp.size];
		//culling
		tmp.cullingFlag = new u16[tmp.size];

		//for hash
		tmp.hashValueY = new u16[hashROOT];
		tmp.hashValueX = new u16[hashROOT];
		tmp.isEmpty = new bool[hashROOT];

		ShadowVolumes.push_back(tmp);
		svp = &ShadowVolumes[ShadowVolumes.size()-1];
		++ShadowVolumesUsed;
	}

	const u32 faceCount = IndexCount / 3;

	if (faceCount * 6 > Edges.size())
	{
		Edges.set_used(faceCount*6);
		//svp->hashValueX.set_used(faceCount*6);
		//svp->hashValueY.set_used(faceCount*6);
		//svp->isEmpty.set_used(faceCount*6);
	}

	//svp->hashValueX.set_used(hashROOT);
	//svp->hashValueY.set_used(hashROOT);
	//svp->isEmpty.set_used(hashROOT);

	u32 numEdges = 0;

	//__android_log_print(ANDROID_LOG_INFO,"Enron","Infinity:%d", Infinity);
	const core::vector3df ls = light * 999999999999999999999999999999999.0f; // light scaled

	/*if (UseZFailMethod)
		createZFailVolume(faceCount, numEdges, light, svp);
	else
		createZPassVolume(faceCount, numEdges, light, svp, false);*/

	// the createZFailVolume does currently not work 100% correctly,
	// so we create createZPassVolume with caps if the zfail method
	// is used
	createZPassVolume(faceCount, numEdges, light, svp, UseZFailMethod);
	

	//����ÿһ��������ı� modi
	int edgecount = 0;
	/*for (u32 i=0; i<numEdges; ++i)*/
	for(u32 i = 0; i<hashROOT; i++)
	{
		
		//if(svp->cullingFlag[2*i] == 1) continue;

		//modi
		if(svp->isEmpty[i]) continue;

		edgecount ++;
		//v1��v2һ��������ıߵ������� modi
		/*core::vector3df &v1 = Vertices[Edges[2*i+0]];
		core::vector3df &v2 = Vertices[Edges[2*i+1]];*/


		core::vector3df &v1 = Vertices[svp->hashValueX[i]];
		core::vector3df &v2 = Vertices[svp->hashValueY[i]];

		//ls��ʾlight*infinity
		//v3��v4��ʾ��Դͨ��v1��v2Ͷ��ȥ��������
		//core::vector3df v3(v1 - ls);
		//core::vector3df v4(v2 - ls);

		core::vector3df v3;
		v3 = (v1 - ls);		
		core::vector3df v4;
		v4 = (v2 - ls);



		// Add a quad (two triangles) to the vertex list
		if (svp->vertices && svp->count < svp->size-5)
		{
			
			svp->vertices[svp->count++] = v1;
			svp->vertices[svp->count++] = v2;
			svp->vertices[svp->count++] = v3;

			//to print coordination 
			/*__android_log_print(ANDROID_LOG_INFO,"Enron","x+y+z=%d",svp->vertices[svp->count].X);

			__android_log_print(ANDROID_LOG_INFO,"Enron","count=%d",svp->count);*/

			//modi
			//svp->vertices[svp->count++] = v2;
			//svp->vertices[svp->count++] = v4;
			//svp->vertices[svp->count++] = v3;
			
		}
	}

}

void CShadowVolumeSceneNode::addHash(SShadowVolume* svp,u16 hashKey, u16 hashValue0, u16 hashValue1)
{
	//add edge
	if(svp->isEmpty[hashKey])
	{
		svp->isEmpty[hashKey] = false;

		(svp->hashCount)++;
		
		svp->hashValueX[hashKey] = hashValue0;
		svp->hashValueY[hashKey] = hashValue1;

		//__android_log_print(ANDROID_LOG_INFO,"Enron","Add one hash:%d,%d",hashValue0, hashValue1);

	}
	else
	{
		//remove	
		if((svp->hashValueX[hashKey] == hashValue0 && svp->hashValueY[hashKey] == hashValue1)
			||(svp->hashValueX[hashKey] == hashValue1 && svp->hashValueY[hashKey] == hashValue0))
		{
			svp->isEmpty[hashKey] = true;
			svp->hashValueX[hashKey] = 0;
			svp->hashValueY[hashKey] = 0;
			(svp->hashCount)--;
		}
		else
		{
			addHash(svp, (hashKey+241)%hashROOT, hashValue0, hashValue1);
			(svp->collisionCount)++;
		}
	}
}


void CShadowVolumeSceneNode::createZFailVolume(s32 faceCount, u32& numEdges,
						const core::vector3df& light,
						SShadowVolume* svp)
{
	s32 i;
	const core::vector3df ls = light * Infinity;

	// Check every face if it is front or back facing the light.
	for (i=0; i<faceCount; ++i)
	{
		const u16 wFace0 = Indices[3*i+0];
		const u16 wFace1 = Indices[3*i+1];
		const u16 wFace2 = Indices[3*i+2];

		const core::vector3df v0 = Vertices[wFace0];
		const core::vector3df v1 = Vertices[wFace1];
		const core::vector3df v2 = Vertices[wFace2];

		if (core::triangle3df(v0,v1,v2).isFrontFacing(light))
		{
			FaceData[i] = false; // it's a back facing face

			if (svp->vertices && svp->count < svp->size-5)
			{
				// add front cap
				svp->vertices[svp->count++] = v0;
				svp->vertices[svp->count++] = v2;
				svp->vertices[svp->count++] = v1;

				// add back cap
				svp->vertices[svp->count++] = v0 - ls;
				svp->vertices[svp->count++] = v1 - ls;
				svp->vertices[svp->count++] = v2 - ls;
			}
		}
		else
			FaceData[i] = true; // it's a front facing face
	}

	for(i=0; i<faceCount; ++i)
	{
		if (FaceData[i] == true)
		{
			const u16 wFace0 = Indices[3*i+0];
			const u16 wFace1 = Indices[3*i+1];
			const u16 wFace2 = Indices[3*i+2];

			const u16 adj0 = Adjacency[3*i+0];
			const u16 adj1 = Adjacency[3*i+1];
			const u16 adj2 = Adjacency[3*i+2];

			if (adj0 != (u16)-1 && FaceData[adj0] == false)
			{
				// add edge v0-v1
				Edges[2*numEdges+0] = wFace0;
				Edges[2*numEdges+1] = wFace1;
				++numEdges;
			}

			if (adj1 != (u16)-1 && FaceData[adj1] == false)
			{
				// add edge v1-v2
				Edges[2*numEdges+0] = wFace1;
				Edges[2*numEdges+1] = wFace2;
				++numEdges;
			}

			if (adj2 != (u16)-1 && FaceData[adj2] == false)
			{
				// add edge v2-v0
				Edges[2*numEdges+0] = wFace2;
				Edges[2*numEdges+1] = wFace0;
				++numEdges;
			}
		}
	}
}



void CShadowVolumeSceneNode::createZPassVolume(s32 faceCount,
						u32& numEdges,
						core::vector3df light,
						SShadowVolume* svp, bool caps)
{

	//__android_log_print(ANDROID_LOG_INFO,"Enron","CShadowVolumeSceneNode:ZPass()");

	//Light��ֵ*Infinity
	light *= Infinity;
	//���Light��ԭ��Ļ�������Ϊ�ǳ�����ԭ���һ����
	if (light == core::vector3df(0,0,0))
		light = core::vector3df(0.0001f,0.0001f,0.0001f);

	//for hash
	svp->hashCount = 0;
	svp->collisionCount = 0;
	//svp->isEmpty.set_used(hashROOT);
	for(u16 i = 0; i < hashROOT; i++)
	{
		svp->isEmpty[i] = true;
	}
	//
	for (s32 i=0; i<faceCount; ++i)
	{
		//ÿ����������Indexֵ����Vertex�����еĶ������һ��������
		//ÿ���ҳ���������indexֵ
		const u16 wFace0 = Indices[3*i+0];
		const u16 wFace1 = Indices[3*i+1];
		const u16 wFace2 = Indices[3*i+2];

		//����ÿһ�������Σ�������ڹ�Դ������
		if (core::triangle3df(Vertices[wFace0],Vertices[wFace1],Vertices[wFace2]).isFrontFacing(light))
		{
			//һ�δ���0,1��1��2��2,0����ʾ��������
			//numEdge��ʾ�����˼�����
			//Edges�д����Index��ֵ��Ҳ����Vertex�����е�����ֵ
			//linear search
			/*bool exist1 = false, exist2 = false, exist3 = false;

			for(int i  = 0; i < numEdges; i++)
			{
				if((Edges[2*i+0] == wFace0 && Edges[2*i+1] == wFace1) || (Edges[2*i+0] == wFace1 && Edges[2*i+1] == wFace0))
				{
					exist1 = true;
					svp->cullingFlag[2*i+0] = 1;
					svp->cullingFlag[2*i+1] = 1;
				}

				if((Edges[2*i+0] == wFace1 && Edges[2*i+1] == wFace2) || (Edges[2*i+0] == wFace2 && Edges[2*i+1] == wFace1))
				{
					exist2 = true;
					svp->cullingFlag[2*i+0] = 1;
					svp->cullingFlag[2*i+1] = 1;
				}

				if((Edges[2*i+0] == wFace2 && Edges[2*i+1] == wFace0) || (Edges[2*i+0] == wFace0 && Edges[2*i+1] == wFace2))
				{
					exist3 = true;
					svp->cullingFlag[2*i+0] = 1;
					svp->cullingFlag[2*i+1] = 1;
				}
			}
		
			if(!exist1)
			{
				Edges[2*numEdges+0] = wFace0;
				Edges[2*numEdges+1] = wFace1;
				svp->cullingFlag[2*numEdges+0] = 0;
				svp->cullingFlag[2*numEdges+1] = 0;
				++numEdges;
			}

			if(!exist2)
			{
				Edges[2*numEdges+0] = wFace1;
				Edges[2*numEdges+1] = wFace2;
				svp->cullingFlag[2*numEdges+0] = 0;
				svp->cullingFlag[2*numEdges+1] = 0;
				++numEdges;
			}
		
			if(!exist3)
			{
				Edges[2*numEdges+0] = wFace2;
				Edges[2*numEdges+1] = wFace0;
				svp->cullingFlag[2*numEdges+0] = 0;
				svp->cullingFlag[2*numEdges+1] = 0;
				++numEdges;
			}*/

			//ori
			/*Edges[2*numEdges+0] = wFace0;
			Edges[2*numEdges+1] = wFace1;
			++numEdges;
			Edges[2*numEdges+0] = wFace1;
			Edges[2*numEdges+1] = wFace2;
			++numEdges;
			Edges[2*numEdges+0] = wFace2;
			Edges[2*numEdges+1] = wFace0;
			++numEdges;*/

			//--for hash--
			//u16 hash0 = (wFace0 + wFace1)%hashROOT;
			//u16 hash1 = (wFace1 + wFace2)%hashROOT;
			//u16 hash2 = (wFace2 + wFace0)%hashROOT;

			//modi
			u16 hash0 = (wFace0*wFace0 + wFace1*wFace1)%hashROOT;
			u16 hash1 = (wFace1*wFace1 + wFace2*wFace2)%hashROOT;
			u16 hash2 = (wFace2*wFace2 + wFace0*wFace0)%hashROOT;

			addHash(svp, hash0, wFace0, wFace1);
			addHash(svp, hash1, wFace1, wFace2);
			addHash(svp, hash2, wFace2, wFace0);
			 

			if (caps && svp->vertices && svp->count < svp->size-5)
			{
				//Сдv��ͷ��vertices��svp�ĳ�Ա����дV��ͷ����ShadowVolumeNode�ĳ�Ա
				//wFace0,1,2 ������������indexֵ�������ǿ����ҵ���Vertices�����д���Ķ��㣬ע��0,1,2���һ��������
				//��¼mesh�ϵ�������
				svp->vertices[svp->count++] = Vertices[wFace0];
				svp->vertices[svp->count++] = Vertices[wFace2];
				svp->vertices[svp->count++] = Vertices[wFace1];

				//��¼meshͶӰ��ȥ��������
				//ע�ⶥ��ļ�¼���򽫾�������
				//svp->vertices[svp->count++] = Vertices[wFace0] - light;
				//svp->vertices[svp->count++] = Vertices[wFace1] - light;
				//svp->vertices[svp->count++] = Vertices[wFace2] - light;

			}
		}
	}

	//__android_log_print(ANDROID_LOG_INFO,"Enron","EdgeNum:%d",numEdges);
	//__android_log_print(ANDROID_LOG_INFO,"Enron","EdgeNum hash:%d",svp->hashCount);
	//__android_log_print(ANDROID_LOG_INFO,"Enron","collision count:%d",svp->collisionCount);
	
}


void CShadowVolumeSceneNode::setShadowMesh(const IMesh* mesh)
{
    if ( ShadowMesh == mesh )
        return;
	if (ShadowMesh)
		ShadowMesh->drop();
	ShadowMesh = mesh;
	if (ShadowMesh)
		ShadowMesh->grab();
}

//Update shadow volumes
void CShadowVolumeSceneNode::updateShadowVolumes()
{	
	//������һ����Ⱦ�����������Ͷ�����
	const u32 oldIndexCount = IndexCount;
	const u32 oldVertexCount = VertexCount;

	//���µ��������Ͷ��������㣬ShadowVolume��Ҳ����
	VertexCount = 0;
	IndexCount = 0;
	ShadowVolumesUsed = 0;

	//ָ��ҪͶӰ��mesh
	const IMesh* const mesh = ShadowMesh;
	if (!mesh)
		return;

	// calculate total amount of vertices and indices

	u32 i;
	u32 totalVertices = 0;
	u32 totalIndices = 0;
	const u32 bufcnt = mesh->getMeshBufferCount();

	
	//ʵ��ͶӰһ���Ļ���mesh ��bufferҲֻ��һ������������ֻ��ѭ��һ��
	for (i=0; i<bufcnt; ++i)
	{
		//�õ�ͶӰ������Index��Vertex����
		const IMeshBuffer* buf = mesh->getMeshBuffer(i);
		totalIndices += buf->getIndexCount();
		totalVertices += buf->getVertexCount();
	}

	//__android_log_print(ANDROID_LOG_INFO,"Enron","updateShadowVolumes: ver:%d, indi:%d",totalIndices,totalVertices);
	
	// allocate memory if necessary
	if (totalVertices > Vertices.size())
		Vertices.set_used(totalVertices);

	if (totalIndices > Indices.size())
	{
		Indices.set_used(totalIndices);

		if (UseZFailMethod)
			FaceData.set_used(totalIndices / 3);
	}

	// copy mesh
	//ֻ��һ��Ӱ�ӣ�Ҳֻ��ѭ��һ��
	for (i=0; i<bufcnt; ++i)
	{
		//�õ�MeshBuffer
		const IMeshBuffer* buf = mesh->getMeshBuffer(i);
		
		//��Buffer�õ�ָ��index�����ָ��
		const u16* idxp = buf->getIndices();
		//getIndexCount()�Ǵ�Buffer�õ�index���������
		//������idxpend����ָ���index�����ĩβ�������ж�index�����Ƿ������һ��Ԫ��
		const u16* idxpend = idxp + buf->getIndexCount();

		//��Mesh�е�index����ȫ���洢��Indice������
		for (; idxp!=idxpend; ++idxp)
			Indices[IndexCount++] = *idxp + VertexCount;

		//��Mesh�еĶ�������ȫ���浽Vertices������
		const u32 vtxcnt = buf->getVertexCount();
		for (u32 j=0; j<vtxcnt; ++j)
			Vertices[VertexCount++] = buf->getPosition(j);
	}



	// recalculate adjacency if necessary
	//if (oldVertexCount != VertexCount && oldIndexCount != IndexCount && UseZFailMethod)
	//{
	//	__android_log_print(ANDROID_LOG_INFO,"Enron","cal adj");
	//	calculateAdjacency();
	//}

	// create as much shadow volumes as there are lights but
	// do not ignore the max light settings.

	//��SceneManager�еõ�����
	const u32 lights = SceneManager->getVideoDriver()->getDynamicLightCount();
	core::matrix4 mat = Parent->getAbsoluteTransformation();
	mat.makeInverse();
	const core::vector3df parentpos = Parent->getAbsolutePosition();
	core::vector3df lpos;

	// TODO: Only correct for point lights.
	//ֻ��һ�����ߵĻ���ֻ��ѭ��һ��
	for (i=0; i<lights; ++i)
	{
		//�õ�����
		const video::SLight& dl = SceneManager->getVideoDriver()->getDynamicLight(i);
		//�õ����ߵ�λ��
		lpos = dl.Position;
		
		//__android_log_print(ANDROID_LOG_INFO,"Enron","Light:  X=%d,Y=%d,Z=%d",lpos.X, lpos.Y, lpos.Z);

		//�������ΪҪͶӰ
		if (dl.CastShadows &&
			fabs((lpos - parentpos).getLengthSQ()) <= (dl.Radius*dl.Radius*4.0f))
		{

			mat.transformVect(lpos);
			//__android_log_print(ANDROID_LOG_INFO,"Enron","afterLight:  X=%d,Y=%d,Z=%d",lpos.X, lpos.Y, lpos.Z);

			createShadowVolume(lpos);
		}
	}
}


//! pre render method
void CShadowVolumeSceneNode::OnRegisterSceneNode()
{
	if (IsVisible)
	{
		//__android_log_print(ANDROID_LOG_INFO,"Enron","CShadowVolumeSceneNode:OnRegisterSceneNode()");
		SceneManager->registerNodeForRendering(this, scene::ESNRP_SHADOW);
		ISceneNode::OnRegisterSceneNode();
	}
}


//! renders the node.
void CShadowVolumeSceneNode::render()
{
	//__android_log_print(ANDROID_LOG_INFO,"Enron","CShadowVolumeSceneNode:render()");
	video::IVideoDriver* driver = SceneManager->getVideoDriver();

	if (!ShadowVolumesUsed || !driver)
		return;

	driver->setTransform(video::ETS_WORLD, Parent->getAbsoluteTransformation());

	//Ϊÿһ��ShadowVolume����stencilBuffer
	for (u32 i=0; i<ShadowVolumesUsed; ++i)
	{
		driver->drawStencilShadowVolume(ShadowVolumes[i].vertices,
			ShadowVolumes[i].count, UseZFailMethod);

	}

	if ( DebugDataVisible & scene::EDS_MESH_WIRE_OVERLAY )
	{
		video::SMaterial mat;
		mat.Lighting = false;
		mat.Wireframe = true;
		mat.ZBuffer = true;
		driver->setMaterial(mat);
		driver->setTransform(video::ETS_WORLD, core::IdentityMatrix);

		for (u32 i=0; i<ShadowVolumesUsed; ++i)
		{
			driver->drawVertexPrimitiveList(ShadowVolumes[i].vertices,
			ShadowVolumes[i].count,0,0,video::EVT_STANDARD,scene::EPT_LINES);

			
		}
	}
}


//! returns the axis aligned bounding box of this node
const core::aabbox3d<f32>& CShadowVolumeSceneNode::getBoundingBox() const
{
	return Box;
}


//! Generates adjacency information based on mesh indices.
void CShadowVolumeSceneNode::calculateAdjacency(f32 epsilon)
{
	Adjacency.set_used(IndexCount);

	epsilon *= epsilon;

	// go through all faces and fetch their three neighbours
	for (u32 f=0; f<IndexCount; f+=3)
	{
		for (u32 edge = 0; edge<3; ++edge)
		{
			core::vector3df v1 = Vertices[Indices[f+edge]];
			core::vector3df v2 = Vertices[Indices[f+((edge+1)%3)]];

			// now we search an_O_ther _F_ace with these two
			// vertices, which is not the current face.

			u32 of;

			for (of=0; of<IndexCount; of+=3)
			{
				if (of != f)
				{
					s32 cnt1 = 0;
					s32 cnt2 = 0;

					for (s32 e=0; e<3; ++e)
					{
						const f32 t1 = v1.getDistanceFromSQ(Vertices[Indices[of+e]]);
						if (core::iszero(t1))
							++cnt1;

						const f32 t2 = v2.getDistanceFromSQ(Vertices[Indices[of+e]]);
						if (core::iszero(t2))
							++cnt2;
					}

					if (cnt1 == 1 && cnt2 == 1)
						break;
				}
			}

			if (of == IndexCount)
				Adjacency[f + edge] = f;
			else
				Adjacency[f + edge] = of / 3;
		}
	}
}


} // end namespace scene
} // end namespace irr

