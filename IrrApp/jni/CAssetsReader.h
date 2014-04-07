#ifndef __C_ASSETS_READER_H_INCLUDED__
#define __C_ASSETS_READER_H_INCLUDED__

#include "IrrCompileConfig.h"

#ifdef _IRR_COMPILE_WITH_ANDROID_DEVICE_

#include "IFileSystem.h"
#include "CFileList.h"
#include <android/asset_manager_jni.h>
#include <android/asset_manager.h>

namespace irr
{
namespace io
{

	//! Archiveloader capable of loading Assets
	class CArchiveLoaderAssets : public IArchiveLoader
	{
	public:

		//! Constructor
		CArchiveLoaderAssets(io::IFileSystem* fs);

		//! returns true if the file maybe is able to be loaded by this class
		//! based on the file extension (e.g. ".zip")
		virtual bool isALoadableFileFormat(const io::path& filename) const;

		//! Check if the file might be loaded by this class
		/** Check might look into the file.
		\param file File handle to check.
		\return True if file seems to be loadable. */
		virtual bool isALoadableFileFormat(io::IReadFile* file) const;

		//! Check to see if the loader can create archives of this type.
		/** Check based on the archive type.
		\param fileType The archive type to check.
		\return True if the archile loader supports this type, false if not */
		virtual bool isALoadableFileFormat(E_FILE_ARCHIVE_TYPE fileType) const;

		//! Creates an archive from the filename
		/** \param file File handle to check.
		\return Pointer to newly created archive, or 0 upon error. */
		virtual IFileArchive* createArchive(const io::path& filename, bool ignoreCase, bool ignorePaths) const;

		//! creates/loads an archive from the file.
		//! \return Pointer to the created archive. Returns 0 if loading failed.
		virtual IFileArchive* createArchive(io::IReadFile* file, bool ignoreCase, bool ignorePaths) const;

	private:
		io::IFileSystem* FileSystem;
	};

	//! A File Archive which uses a mountpoint
	class CAssetsReader : public virtual IFileArchive, virtual CFileList
	{
	public:

		//! Constructor
		CAssetsReader(IFileSystem *parent, const io::path& basename,
				bool ignoreCase, bool ignorePaths);

		//! opens a file by index
		virtual IReadFile* createAndOpenFile(u32 index);

		//! opens a file by file name
		virtual IReadFile* createAndOpenFile(const io::path& filename);

		//! returns the list of files
		virtual const IFileList* getFileList() const;

		//! get the class Type
		virtual E_FILE_ARCHIVE_TYPE getType() const { return EFAT_FOLDER; }

	private:

		core::array<io::path> RealFileNames;

		IFileSystem *Parent;
		void buildDirectory(const io::path& basename);
	};
	
	extern io::path PATH_ASSETS;
	extern AAssetManager* _assetManager;
} // io
} // irr

#endif // _IRR_COMPILE_WITH_ANDROID_DEVICE_
#endif // __C_ASSETS_READER_H_INCLUDED__
