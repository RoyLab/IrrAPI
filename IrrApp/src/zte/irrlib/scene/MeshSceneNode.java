package zte.irrlib.scene;

import zte.irrlib.Engine;
import zte.irrlib.core.BoundingBox;
import zte.irrlib.core.Color4i;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * ����νڵ���
 * @author Fxx
 *
 */
public class MeshSceneNode extends SceneNode{
	
	/**
	 * ���³�������{@link #setMaterialFlag(int, boolean)}���趨��
	 * ��Щ�������ڿ��ƽڵ����Ⱦ��ʽ�������Ƿ�����߿�������������
	 * �����˲���ʽ�ȣ���ϸ��μ��������˵��
	 */
	//! Draw as wireframe or filled triangles? Default: false
	public static final int EMF_WIREFRAME = 0x1;

	//! Draw as point cloud or filled triangles? Default: false
	public static final int EMF_POINTCLOUD = 0x2;

	//! Flat or Gouraud shading? Default: true
	public static final int EMF_GOURAUD_SHADING = 0x4;

	//! Will this material be lighted? Default: true
	public static final int EMF_LIGHTING = 0x8;

	//! Is the ZBuffer enabled? Default: true
	public static final int  EMF_ZBUFFER = 0x10;

	//! May be written to the zbuffer or is it readonly. Default: true
	/** This flag is ignored, if the material type is a transparent type. */
	public static final int  EMF_ZWRITE_ENABLE = 0x20;

	//! Is backface culling enabled? Default: true
	public static final int  EMF_BACK_FACE_CULLING = 0x40;

	//! Is frontface culling enabled? Default: false
	/** Overrides 	public static final int  EMF_BACK_FACE_CULLING if both are enabled. */
	public static final int  EMF_FRONT_FACE_CULLING = 0x80;

	//! Is bilinear filtering enabled? Default: true
	public static final int  EMF_BILINEAR_FILTER = 0x100;

	//! Is trilinear filtering enabled? Default: false
	/** If the trilinear filter flag is enabled,
	the bilinear filtering flag is ignored. */
	public static final int  EMF_TRILINEAR_FILTER = 0x200;

	//! Is anisotropic filtering? Default: false
	/** In Irrlicht you can use anisotropic texture filtering in
	conjunction with bilinear or trilinear texture filtering
	to improve rendering results. Primitives will look less
	blurry with this flag switched on. */
	public static final int  EMF_ANISOTROPIC_FILTER = 0x400;

	//! Is fog enabled? Default: false
	public static final int  EMF_FOG_ENABLE = 0x800;

	//! Normalizes normals. Default: false
	/** You can enable this if you need to scale a dynamic lighted
	model. Usually, its normals will get scaled too then and it
	will get darker. If you enable the 	public static final int  EMF_NORMALIZE_NORMALS flag,
	the normals will be normalized again, and the model will look
	as bright as it should. */
	public static final int  EMF_NORMALIZE_NORMALS = 0x1000;

	//! Access to all layers texture wrap settings. Overwrites separate layer settings.
	public static final int  EMF_TEXTURE_WRAP = 0x2000;
	
	//! AntiAliasing mode
	public static final int  EMF_ANTI_ALIASING = 0x4000;

	//! ColorMask bits, for enabling the color planes
	public static final int  EMF_COLOR_MASK = 0x8000;

	//! ColorMaterial enum for vertex color interpretation
	public static final int  EMF_COLOR_MATERIAL = 0x10000;
	
	public static final String TAG = "MeshSceneNode";
	
	/**
	 * ����ģ�ͽڵ��Ƿ���Ӧ���ա�
	 * @param flag ֵΪtrueʱ��Ӧ���գ��������ӹ�Դ
	 */
	public void enableLighting(boolean flag){
		nativeEnableLighting(flag, getId());
	}
	
	/**
	 * ����ģ�ͽڵ��Ƿ���Ӧ��ѡ��ײ��⡣
	 * @param flag ֵΪtrueʱ����Ӧ��ѡ��ײ��⣬������Ӧ
	 */
	public void setTouchable(boolean flag){
		nativeSetTouchable(flag, getId());
	}
	
	/**
	 * ����ָ�������Ƿ�ʹ��ʹ��ƽ����ɫ��������ɫ��Gouraud Shading����
	 * @param flag ֵΪtrueʱ����Gouraud Shading���������á�
	 * @param materialId ָ�����ʵ�IDֵ
	 */
	public void setSmoothShade(boolean flag, int materialId){
		nativeSetSmoothShade(flag, materialId, getId());
	}
	
	/**
	 * ����ָ�����ʶԻ��������ɫ��Ӧ��
	 * @param color ���ʶԻ��������ɫ��Ӧ����
	 * @param materialId ָ�����ʵ�IDֵ
	 */
	public void setAmbientColor(Color4i color, int materialId) {
		nativeSetAmbientColor(color.r(), color.g(), color.b(), color.a(), materialId, getId());
	}
	
	/**
	 * ����ָ�����ʶ�����������ɫ��Ӧ��
	 * @param color ���ʶ�����������Ӧ������Ĭ��ֵΪȫ�ף�255��255��255��
	 * @param materialId ָ�����ʵ�IDֵ
	 */
	public void setDiffuseColor(Color4i color, int materialId) {
		nativeSetDiffuseColor(color.r(), color.g(), color.b(), color.a(), materialId, getId());
	}
	
	/**
	 * ����ָ�����ʵķ������ɫ��Ĭ�ϲ����⡣
	 * @param color ���ʵķ���������
	 * @param materialId ָ�����ʵ�IDֵ
	 */
	public void setEmissiveColor(Color4i color, int materialId) {
		nativeSetEmissiveColor(color.r(), color.g(), color.b(), color.a(), materialId, getId());
	}
	
	/**
	 * ����ָ�����ʵĶԸ߹����ɫ��Ӧ��
	 * @param color ���ʵĸ߹���Ӧ������Ĭ��ֵΪȫ�ף�255��255��255��
	 * @param materialId ָ�����ʵ�IDֵ
	 */
	public void setSpecularColor(Color4i color, int materialId) {
		nativeSetSpecularColor(color.r(), color.g(), color.b(), color.a(), materialId, getId());
	}
	
	/**
	 * ����ָ�����ʹ�ǿ��������Ӱ����ʵĸ߹⡣
	 * ͨ��ֵΪ20����0ʱ���޸߹⡣����ȡֵ��ΧΪ[0.5,128]��
	 * @param para ���ʹ�ǿ����ֵ
	 * @param materialId ָ�����ʵ�IDֵ
	 */
	public void setShininess(double para, int materialId) {
		nativeSetShininess(para, materialId, getId());
	}
	
	/**
	 * ����ָ�����ʵ���ͼ��
	 * @param path ��ͼ��·��
	 * @param materialId ָ�����ʵ�IDֵ 
	 */
	public void setTexture(String path, int materialId) {
		nativeSetTexture(mScene.getFullPath(path), materialId, getId());
	}
	
	/**
	 * ����ָ�����ʵ���ͼΪλͼ���÷������ױ����ã���ʹ��{@link Scene#uploadBitmap(Bitmap, String)}
	 * Ȼ��ʹ��{@link #setTexture(String, int)}��
	 * @param bitmap ��ʹ�õ�λͼ����
	 * @param name ��λͼ������������Ψһ��
	 * @param materialId ָ�����ʵ�IDֵ
	 */
	@Deprecated public void setTexture(Bitmap bitmap, String bitmapName, int materialId){
		nativeSetBitmapTexture(bitmapName, bitmap, materialId, getId());
	}
	
	@Deprecated
	/**
	 * ����ָ��������ͼΪ��Ƶͼ��
	 * @param materialId ָ�����ʵ�IDֵ
	 */
	public void setMediaTexture(int materialId){
		nativeSetMediaTexture(materialId, getId());
	}
	
	/**
	 * Ϊģ�ͽڵ����в���ָ��ͳһ��ͼ
	 * @param path ��ͼ��·��
	 */
	public void setTexture(String path) {
		nativeAllSetTexture(mScene.getFullPath(path), getId());
	}
	
	/**
	 * Ϊģ�����нڵ�ָ��ͳһλͼ��ͼ���÷������ױ����ã���ʹ��{@link Scene#uploadBitmap(Bitmap, String)}
	 * Ȼ��ʹ��{@link #setTexture(String)}��
	 * @param bitmap ��ʹ�õ�λͼ����
	 * @param texName ��ͼ�����ƣ�����Ψһ�����Ҳ���������ͼƬ��·������
	 */
	@Deprecated public void setTexture(Bitmap bitmap, String bitmapName){
		nativeAllSetBitmapTexture(bitmapName, bitmap, getId());
	}
	
	/**
	 * Ϊģ���趨�ⲿ��ͼ
	 * @param name ����ͼ��Ψһ����
	 * @return Ϊ�����趨�ɹ�
	 */
	public boolean setExternalTexture(String name){
		if (Engine.getInstance().getRenderType() != 1){
			Log.e(TAG, "Can not set external texture. Unsupported renderer type.");
			return false;
		}
		if (nativeSetExternalTexture(name, -1, getId()) == 0) return true;
		return false;
	}
	
	@Deprecated
	/**
	 * Ϊģ�����нڵ�ָ��ͳһ��Ƶ��ͼ
	 * @param bitmap ��ʹ�õ���Ƶ��ͼ����
	 */
	public void setMediaTexture(){
		if (Engine.getInstance().getRenderType() != 1){
			Log.e(TAG, "Can not set Mediaplayer texture. Unsupported renderer type.");
			return;
		}
		nativeAllSetMediaTexture(getId());
	}
	
	/**
	 * Ϊģ�Ͳ����趨������ͼ<br>
	 * ע�⣬������ӣ�ͨ��������벻Ҫ��ô�������Animatorʱ�������ά��
	 * Animator�����˳��˳���������Ӱ��ÿһ֡�ĸ���Ч�������磬������ײ���
	 * {@link #addCollisionResponseAnimator(SceneNode, boolean, boolean)}�����ֱ��
	 * ���ж���{@link #addFlyStraightAnimator(zte.irrlib.core.Vector3d, 
	 * zte.irrlib.core.Vector3d, double, boolean, boolean)}����ô��ײ����Ч��
	 * �ᱻ����ִ�е�ֱ�߷��ж��������ǣ��������˳������ײ����λ�ý���ִ�й����ж������
	 * λ�ã������������Ҫʹ�ö����������ȷ���ڵ�û�б���ӹ�������ʹ��{@link #removeAllAnimator()}
	 * ������ж���������һ������ӣ�����һֱ�����ڽڵ���ֱ��{@link #removeAllAnimator()}�����á�
	 * @param path ������ͼ���·��
	 * @param timePerFrame ��ͼ�����������ʣ���λframe/second
	 * @param loop ֵΪtrueʱѭ��������ͼ���������򵥴β���
	 */
	public void addTextureAnimator(String[] path, int timePerFrame, boolean loop) {
		String text[] = path.clone();
		for (int i = 0; i < text.length; i++){
			text[i] = mScene.getFullPath(text[i]);
		}
		if (nativeAddTextureAnimator(text, timePerFrame, loop, getId()) == 0)
			addAnimator();
	}
	
	/**
	 * ��Ӷ�ָ���ڵ����ײ�����Ӧ��<br>
	 * ע�⣬������ӣ�ͨ��������벻Ҫ��ô�������Animatorʱ�������ά��
	 * Animator�����˳��˳���������Ӱ��ÿһ֡�ĸ���Ч�������磬������ײ���
	 * {@link #addCollisionResponseAnimator(SceneNode, boolean, boolean)}�����ֱ��
	 * ���ж���{@link #addFlyStraightAnimator(zte.irrlib.core.Vector3d, 
	 * zte.irrlib.core.Vector3d, double, boolean, boolean)}����ô��ײ����Ч��
	 * �ᱻ����ִ�е�ֱ�߷��ж��������ǣ��������˳������ײ����λ�ý���ִ�й����ж������
	 * λ�ã������������Ҫʹ�ö����������ȷ���ڵ�û�б���ӹ�������ʹ��{@link #removeAllAnimator()}
	 * ������ж���������һ������ӣ�����һֱ�����ڽڵ���ֱ��{@link #removeAllAnimator()}�����á�
	 * @param selNode ָ�����������ͨ���Ǿ�ֹ�ĳ�����
	 * @param fromBoundingBox �Ƿ�Ӵ��Ե���ģ�͵İ�Χ������ײ���
	 * @param optimizedByOctree �Ƿ��ð˲����Ż�����㷨��fromBoundingBoxΪ��ʱǿ�Ʋ��Ż���
	 */
	public void addCollisionResponseAnimator(SceneNode selNode, 
			boolean fromBoundingBox, boolean optimizedByOctree){
		if (nativeAddCollisionResponseAnimator(
				mScene.getId(selNode), fromBoundingBox,
				optimizedByOctree, getId()) == 0)
			addAnimator();
	}
	
	/**
	 * ����ģ�ͽڵ�İ�Χ���Ƿ����
	 * @param flag ֵΪtrueʱ��Χ�п��ӣ����򲻿ɼ�
	 */
	public void setBBoxVisibility(boolean flag){
		nativeSetBBoxVisibility(flag, getId());
	}
	
	/**
	 * ����ģ�ͽڵ�Ĳ�����Ŀ
	 * @return ģ�ͽڵ�Ĳ�����Ŀ
	 */
	public int getMaterialCount(){
		return nativeGetMaterialCount(getId());
	}
	
	/**
	 * ����δ�任�İ�Χ�У�����˵����ʹ�ڵ����˶��ģ������任�ģ����صİ�Χ����Ȼ��������Ǹ���
	 * @return ��Χ�е�ʵ��
	 */
	public BoundingBox getBoundingBox(){
		BoundingBox bbox = new BoundingBox();
		nativeGetBoundingBox(bbox, false, getId());
		return bbox;
	}
	
	/**
	 * ���ؾ��Եİ�Χ�У�����˵������ڵ����˶��ģ������任�ģ����صİ�Χ��Ҳ���Ǿ����任�ġ�
	 * @return ��Χ�е�ʵ��
	 */
	public BoundingBox getAbsoluteBoundingBox(){
		BoundingBox bbox = new BoundingBox();
		nativeGetBoundingBox(bbox, true, getId());
		return bbox;
	}
	
	/**
	 * ������ͼ��Ⱦ�������������趨͸����ͼ
	 * @param type ��Ⱦ����
	 */
	public void setMaterialType(E_MATERIAL_TYPE type){
		nativeSetMaterialType(type.ordinal(), getId());
	}
	
	/**
	 * ������Ⱦ����������
	 * @param EMF ��Ⱦ������������EMFΪ��ͷ�ĳ���
	 * @param flag ��Ϊtrue����򿪿���
	 */
	public void setMaterialFlag(int EMF, boolean flag){
		nativeSetMaterialFlag(EMF, flag, getId());
	}
	
	@Override
	public MeshSceneNode clone(){
		MeshSceneNode res = softClone();
		cloneInNativeAndSetupNodesId(res);
		return res;
	}
	
	@Override
	protected MeshSceneNode softClone(){
		MeshSceneNode res = new MeshSceneNode(this);
		softCopyChildren(res);
		return res;
	}
	
	protected MeshSceneNode(MeshSceneNode node){
		super(node);
		
	}
	
	/**
	 * ����ģʽ��ö���࣬��������������ͼ����Ⱦ��ʽ�������Ǵ�native��������ģ����бȽϳ��õ���EMT_SOLID��
	 * EMT_TRANSPARENT_ALPHA_CHANNEL��EMT_TRANSPARENT_ADD_COLOR��
	 * EMT_TRANSPARENT_ALPHA_CHANNEL_REF��ʹ��{@link MeshSceneNode#setMaterialType(E_MATERIAL_TYPE)}
	 * �趨����ģʽ
	 * @author Fxx
	 *
	 */
	public enum E_MATERIAL_TYPE
	{
		//! Standard solid material.
		/** Only first texture is used, which is supposed to be the
		diffuse material. */
		EMT_SOLID,
	
		//! Solid material with 2 texture layers.
		/** The second is blended onto the first using the alpha value
		of the vertex colors. This material is currently not implemented in OpenGL.
		*/
		EMT_SOLID_2_LAYER,
	
		//! Material type with standard lightmap technique
		/** There should be 2 textures: The first texture layer is a
		diffuse map, the second is a light map. Dynamic light is
		ignored. */
		EMT_LIGHTMAP,
	
		//! Material type with lightmap technique like EMT_LIGHTMAP.
		/** But lightmap and diffuse texture are added instead of modulated. */
		EMT_LIGHTMAP_ADD,
	
		//! Material type with standard lightmap technique
		/** There should be 2 textures: The first texture layer is a
		diffuse map, the second is a light map. Dynamic light is
		ignored. The texture colors are effectively multiplied by 2
		for brightening. Like known in DirectX as D3DTOP_MODULATE2X. */
		EMT_LIGHTMAP_M2,
	
		//! Material type with standard lightmap technique
		/** There should be 2 textures: The first texture layer is a
		diffuse map, the second is a light map. Dynamic light is
		ignored. The texture colors are effectively multiplyied by 4
		for brightening. Like known in DirectX as D3DTOP_MODULATE4X. */
		EMT_LIGHTMAP_M4,
	
		//! Like EMT_LIGHTMAP, but also supports dynamic lighting.
		EMT_LIGHTMAP_LIGHTING,
	
		//! Like EMT_LIGHTMAP_M2, but also supports dynamic lighting.
		EMT_LIGHTMAP_LIGHTING_M2,
	
		//! Like EMT_LIGHTMAP_4, but also supports dynamic lighting.
		EMT_LIGHTMAP_LIGHTING_M4,
	
		//! Detail mapped material.
		/** The first texture is diffuse color map, the second is added
		to this and usually displayed with a bigger scale value so that
		it adds more detail. The detail map is added to the diffuse map
		using ADD_SIGNED, so that it is possible to add and substract
		color from the diffuse map. For example a value of
		(127,127,127) will not change the appearance of the diffuse map
		at all. Often used for terrain rendering. */
		EMT_DETAIL_MAP,
	
		//! Look like a reflection of the environment around it.
		/** To make this possible, a texture called 'sphere map' is
		used, which must be set as the first texture. */
		EMT_SPHERE_MAP,
	
		//! A reflecting material with an optional non reflecting texture layer.
		/** The reflection map should be set as first texture. */
		EMT_REFLECTION_2_LAYER,
	
		//! A transparent material.
		/** Only the first texture is used. The new color is calculated
		by simply adding the source color and the dest color. This
		means if for example a billboard using a texture with black
		background and a red circle on it is drawn with this material,
		the result is that only the red circle will be drawn a little
		bit transparent, and everything which was black is 100%
		transparent and not visible. This material type is useful for
		particle effects. */
		EMT_TRANSPARENT_ADD_COLOR,
	
		//! Makes the material transparent based on the texture alpha channel.
		/** The final color is blended together from the destination
		color and the texture color, using the alpha channel value as
		blend factor. Only first texture is used. If you are using
		this material with small textures, it is a good idea to load
		the texture in 32 bit mode
		(video::IVideoDriver::setTextureCreationFlag()). Also, an alpha
		ref is used, which can be manipulated using
		SMaterial::MaterialTypeParam. This value controls how sharp the
		edges become when going from a transparent to a solid spot on
		the texture. */
		EMT_TRANSPARENT_ALPHA_CHANNEL,
	
		//! Makes the material transparent based on the texture alpha channel.
		/** If the alpha channel value is greater than 127, a
		pixel is written to the target, otherwise not. This
		material does not use alpha blending and is a lot faster
		than EMT_TRANSPARENT_ALPHA_CHANNEL. It is ideal for drawing
		stuff like leafes of plants, because the borders are not
		blurry but sharp. Only first texture is used. If you are
		using this material with small textures and 3d object, it
		is a good idea to load the texture in 32 bit mode
		(video::IVideoDriver::setTextureCreationFlag()). */
		EMT_TRANSPARENT_ALPHA_CHANNEL_REF,
	
		//! Makes the material transparent based on the vertex alpha value.
		EMT_TRANSPARENT_VERTEX_ALPHA,
	
		//! A transparent reflecting material with an optional additional non reflecting texture layer.
		/** The reflection map should be set as first texture. The
		transparency depends on the alpha value in the vertex colors. A
		texture which will not reflect can be set as second texture.
		Please note that this material type is currently not 100%
		implemented in OpenGL. */
		EMT_TRANSPARENT_REFLECTION_2_LAYER,
	
		//! A solid normal map renderer.
		/** First texture is the color map, the second should be the
		normal map. Note that you should use this material only when
		drawing geometry consisting of vertices of type
		S3DVertexTangents (EVT_TANGENTS). You can convert any mesh into
		this format using IMeshManipulator::createMeshWithTangents()
		(See SpecialFX2 Tutorial). This shader runs on vertex shader
		1.1 and pixel shader 1.1 capable hardware and falls back to a
		fixed function lighted material if this hardware is not
		available. Only two lights are supported by this shader, if
		there are more, the nearest two are chosen. */
		EMT_NORMAL_MAP_SOLID,
	
		//! A transparent normal map renderer.
		/** First texture is the color map, the second should be the
		normal map. Note that you should use this material only when
		drawing geometry consisting of vertices of type
		S3DVertexTangents (EVT_TANGENTS). You can convert any mesh into
		this format using IMeshManipulator::createMeshWithTangents()
		(See SpecialFX2 Tutorial). This shader runs on vertex shader
		1.1 and pixel shader 1.1 capable hardware and falls back to a
		fixed function lighted material if this hardware is not
		available. Only two lights are supported by this shader, if
		there are more, the nearest two are chosen. */
		EMT_NORMAL_MAP_TRANSPARENT_ADD_COLOR,
	
		//! A transparent (based on the vertex alpha value) normal map renderer.
		/** First texture is the color map, the second should be the
		normal map. Note that you should use this material only when
		drawing geometry consisting of vertices of type
		S3DVertexTangents (EVT_TANGENTS). You can convert any mesh into
		this format using IMeshManipulator::createMeshWithTangents()
		(See SpecialFX2 Tutorial). This shader runs on vertex shader
		1.1 and pixel shader 1.1 capable hardware and falls back to a
		fixed function lighted material if this hardware is not
		available.  Only two lights are supported by this shader, if
		there are more, the nearest two are chosen. */
		EMT_NORMAL_MAP_TRANSPARENT_VERTEX_ALPHA,
	
		//! Just like EMT_NORMAL_MAP_SOLID, but uses parallax mapping.
		/** Looks a lot more realistic. This only works when the
		hardware supports at least vertex shader 1.1 and pixel shader
		1.4. First texture is the color map, the second should be the
		normal map. The normal map texture should contain the height
		value in the alpha component. The
		IVideoDriver::makeNormalMapTexture() method writes this value
		automatically when creating normal maps from a heightmap when
		using a 32 bit texture. The height scale of the material
		(affecting the bumpiness) is being controlled by the
		SMaterial::MaterialTypeParam member. If set to zero, the
		default value (0.02f) will be applied. Otherwise the value set
		in SMaterial::MaterialTypeParam is taken. This value depends on
		with which scale the texture is mapped on the material. Too
		high or low values of MaterialTypeParam can result in strange
		artifacts. */
		EMT_PARALLAX_MAP_SOLID,
	
		//! A material like EMT_PARALLAX_MAP_SOLID, but transparent.
		/** Using EMT_TRANSPARENT_ADD_COLOR as base material. */
		EMT_PARALLAX_MAP_TRANSPARENT_ADD_COLOR,
	
		//! A material like EMT_PARALLAX_MAP_SOLID, but transparent.
		/** Using EMT_TRANSPARENT_VERTEX_ALPHA as base material. */
		EMT_PARALLAX_MAP_TRANSPARENT_VERTEX_ALPHA,
	
		//! BlendFunc = source * sourceFactor + dest * destFactor ( E_BLEND_FUNC )
		/** Using only first texture. Generic blending method. */
		EMT_ONETEXTURE_BLEND,
	
		//! This value is not used. It only forces this enumeration to compile to 32 bit.
		EMT_FORCE_32BIT ;
	}
	
	/**
	 * Ψһ���캯��
	 */
	MeshSceneNode(){
		super();
		mNodeType = TYPE_MESH;
	}

	protected native int nativeSetTouchable(boolean flag, int Id);
	protected native int nativeSetBBoxVisibility(boolean flag, int Id);
	
	private native int nativeEnableLighting(boolean flag, int Id);

	private native int nativeSetSmoothShade(boolean flag, int materialId, int Id);
	private native int nativeSetAmbientColor(int r, int g, int b, int a, int materialId, int Id);
	private native int nativeSetDiffuseColor(int r, int g, int b, int a, int materialId, int Id);
	private native int nativeSetEmissiveColor(int r, int g, int b, int a, int materialId, int Id);
	private native int nativeSetSpecularColor(int r, int g, int b, int a, int materialId, int Id);
	private native int nativeSetShininess(double shininess, int materialId, int Id);
	
	private native int nativeSetTexture(String path, int materialId, int Id);
	private native int nativeSetBitmapTexture(String name, Bitmap bitmap, int materialId, int Id);
	private native int nativeSetMediaTexture(int materialId, int Id);
	private native int nativeSetExternalTexture(String name, int mId, int Id);
	
	private native int nativeAllSetTexture(String path, int Id);
	private native int nativeAllSetBitmapTexture(String name, Bitmap bitmap, int Id);
	private native int nativeAllSetMediaTexture(int Id);

	private native int nativeAddTextureAnimator(String[] path, int timePerFrame, boolean loop, int Id);
	private native int nativeGetMaterialCount(int Id);
	
	private native int nativeGetBoundingBox(BoundingBox box, boolean isAbsolute, int id);
	private native int nativeAddCollisionResponseAnimator(int selId, boolean bbox, boolean octree, int Id);
	
	private native int nativeSetMaterialType(int type, int Id);
	private native int nativeSetMaterialFlag(int emf, boolean flag, int Id);
}
