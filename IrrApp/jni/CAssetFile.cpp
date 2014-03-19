// Copyright (C) 2002-2009 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CAssetFile.h"
#include "CAssetsReader.h"

#ifdef _IRR_COMPILE_WITH_ANDROID_DEVICE_

namespace irr
{
namespace io
{

CAssetFile::CAssetFile(const io::path& fileName)
: File(0), FileSize(0), Filename(fileName)
{
	#ifdef _DEBUG
	setDebugName("CAssetFile");
	#endif

	openFile();
}


CAssetFile::~CAssetFile()
{
	if (File)
		AAsset_close(File);
}


//! returns how much was read
s32 CAssetFile::read(void* buffer, u32 sizeToRead)
{
	if (!isOpen())
		return 0;

	return (s32)AAsset_read(File, buffer, sizeToRead);
}


//! changes position in file, returns true if successful
//! if relativeMovement==true, the pos is changed relative to current pos,
//! otherwise from begin of file
bool CAssetFile::seek(long finalPos, bool relativeMovement)
{
	if (!isOpen())
		return false;

	SeekPos = AAsset_seek(File, finalPos, relativeMovement ? SEEK_CUR : SEEK_SET);
	return (SeekPos!= -1);
}


//! returns size of file
long CAssetFile::getSize() const
{
	return FileSize;
}


//! returns where in the file we are.
long CAssetFile::getPos() const
{
	return SeekPos;
}


//! opens the file
void CAssetFile::openFile()
{
	if (Filename.size() == 0) // bugfix posted by rt
	{
		File = 0;
		return;
	}

	File = AAssetManager_open(_assetManager, Filename.c_str(), AASSET_MODE_BUFFER);
	
	if (File)
	{
		// get FileSize
		FileSize = AAsset_getLength(File);
		SeekPos = AAsset_seek(File, 0, SEEK_SET);
	}
}


//! returns name of file
const io::path& CAssetFile::getFileName() const
{
	return Filename;
}



IReadFile* createAssetFile(const io::path& fileName)
{
	CAssetFile* file = new CAssetFile(fileName);
	if (file->isOpen())
		return file;

	file->drop();
	return 0;
}


} // end namespace io
} // end namespace irr
#endif
