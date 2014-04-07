// Copyright (C) 2002-2012 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CAssetsReader.h"

#ifdef _IRR_COMPILE_WITH_ANDROID_DEVICE_

#include "CReadFile.h"
#include "os.h"

namespace irr
{
namespace io
{

io::path PATH_ASSETS = "<assets>/";
AAssetManager* _assetManager = 0;

//! Constructor
CArchiveLoaderAssets::CArchiveLoaderAssets( io::IFileSystem* fs)
: FileSystem(fs)
{
	#ifdef _DEBUG
	setDebugName("CArchiveLoaderAssets");
	#endif
}

//! returns true if the file maybe is able to be loaded by this class
bool CArchiveLoaderAssets::isALoadableFileFormat(const io::path& filename) const
{
	return (strcmp(filename.subString(0, PATH_ASSETS.size()).c_str(), PATH_ASSETS.c_str()) == 0);
}

//! Check to see if the loader can create archives of this type.
bool CArchiveLoaderAssets::isALoadableFileFormat(E_FILE_ARCHIVE_TYPE fileType) const
{
	return false;
}

//! Check if the file might be loaded by this class
bool CArchiveLoaderAssets::isALoadableFileFormat(io::IReadFile* file) const
{
	return false;
}

//! Creates an archive from the filename
IFileArchive* CArchiveLoaderAssets::createArchive(const io::path& filename, bool ignoreCase, bool ignorePaths) const
{
	IFileArchive *archive = 0;
	archive = new CAssetsReader(FileSystem, filename.subString(PATH_ASSETS.size(), filename.size()-PATH_ASSETS.size()), ignoreCase, ignorePaths);
	return archive;
}

//! creates/loads an archive from the file.
//! \return Pointer to the created archive. Returns 0 if loading failed.
IFileArchive* CArchiveLoaderAssets::createArchive(io::IReadFile* file, bool ignoreCase, bool ignorePaths) const
{
	return 0;
}

///////////////////////////////////////////////////////////////////////////////////
//! compatible Folder Architecture
CAssetsReader::CAssetsReader(IFileSystem * parent, const io::path& basename, bool ignoreCase, bool ignorePaths)
	: CFileList(basename, ignoreCase, ignorePaths), Parent(parent)
{
	buildDirectory(basename);
	sort();
	os::Printer::log(basename.c_str());
}


//! returns the list of files
const IFileList* CAssetsReader::getFileList() const
{
	return this;
}

void CAssetsReader::buildDirectory(const io::path& basename)
{
	AAssetDir *cDir = AAssetManager_openDir(_assetManager, basename.c_str());
	if (!cDir) return;
		
	//AAssetDir_rewind(cDir);
	while(true)
	{
		const char* fName = AAssetDir_getNextFileName(cDir);
		if (fName == 0) break;
		
		//files traversal, should be moved away if take too much time.
		char full[128];
		strcpy(full, basename.c_str());	strcat(full, "/");	strcat(full, fName);
		
		/*AAsset* file = AAssetManager_open(_assetManager, full, AASSET_MODE_STREAMING);
		if (!file) continue;
		addItem(full, AAsset_getLength(file), false, RealFileNames.size());
		LOGD("File archive added(assets), %s", full);
		AAsset_close(file);*/
		
		addItem(full, 0, false, RealFileNames.size());
		RealFileNames.push_back(full);
	}
	AAssetDir_close(cDir);
}

//! opens a file by index
IReadFile* CAssetsReader::createAndOpenFile(u32 index)
{
	if (index >= Files.size())
		return 0;
	return createAssetFile(RealFileNames[Files[index].ID]);
}

//! opens a file by file name
IReadFile* CAssetsReader::createAndOpenFile(const io::path& filename)
{
	s32 index = findFile(filename, false);
	if (index != -1)
		return createAndOpenFile(index);
	else
		return 0;
}


} // io
} // irr

#endif // _IRR_COMPILE_WITH_ANDROID_DEVICE_
