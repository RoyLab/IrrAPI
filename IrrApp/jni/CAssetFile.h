// Copyright (C) 2002-2009 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_ASSET_FILE_H_INCLUDED__
#define __C_ASSET_FILE_H_INCLUDED__

#include "IrrCompileConfig.h"

#ifdef _IRR_COMPILE_WITH_ANDROID_DEVICE_

#include <stdio.h>
#include "IReadFile.h"
#include "irrString.h"
#include <android/asset_manager_jni.h>
#include <android/asset_manager.h>

namespace irr
{

namespace io
{
	/*!
		class for reading asset in an android device.
	*/
	class CAssetFile : public IReadFile
	{
	public:

		CAssetFile(const io::path& fileName);

		virtual ~CAssetFile();

		//! returns how much was read
		virtual s32 read(void* buffer, u32 sizeToRead);

		//! changes position in file, returns true if successful
		virtual bool seek(long finalPos, bool relativeMovement = false);

		//! returns size of file
		virtual long getSize() const;

		//! returns if file is open
		virtual bool isOpen() const
		{
			return File != 0;
		}

		//! returns where in the file we are.
		virtual long getPos() const;

		//! returns name of file
		virtual const io::path& getFileName() const;

	private:

		//! opens the file
		void openFile();

		AAsset* File;
		long FileSize;
		io::path Filename;
		
		long SeekPos;
	};

} // end namespace io
} // end namespace irr

#endif
#endif

