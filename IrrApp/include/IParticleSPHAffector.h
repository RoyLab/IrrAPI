// Copyright (C) 2002-2009 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __I_PARTICLE_SPH_AFFECTOR_H_INCLUDED__
#define __I_PARTICLE_SPH_AFFECTOR_H_INCLUDED__

#include "IParticleAffector.h"

namespace irr
{
namespace scene
{

//! A particle affector which applies gravity to particles.
class IParticleSPHAffector : public IParticleAffector
{
public:

	//! Get emitter type
	virtual E_PARTICLE_AFFECTOR_TYPE getType() const { return EPAT_SPH; }
};

} // end namespace scene
} // end namespace irr


#endif // __I_PARTICLE_GRAVITY_AFFECTOR_H_INCLUDED__

