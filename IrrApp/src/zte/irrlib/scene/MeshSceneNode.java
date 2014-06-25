package zte.irrlib.scene;

import zte.irrlib.Engine;
import zte.irrlib.core.BoundingBox;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Vector3d;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * 多边形节点类
 * @author Fxx
 *
 */
public class MeshSceneNode extends SceneNode{
	
	/**
	 * 以下常量用于{@link #setMaterialFlag(int, boolean)}的设定。
	 * 这些常量用于控制节点的渲染方式，包括是否绘制线框，隐藏面消除，
	 * 材质滤波方式等，详细请参见鬼火引擎说明
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
	 * 设置模型节点是否响应光照。
	 * @param flag 值为true时响应光照，否则无视光源
	 */
	public void enableLighting(boolean flag){
		nativeEnableLighting(flag, getId());
	}
	
	/**
	 * 设置模型节点是否响应点选碰撞检测。
	 * @param flag 值为true时则响应点选碰撞检测，否则不响应
	 */
	public void setTouchable(boolean flag){
		nativeSetTouchable(flag, getId());
	}
	
	/**
	 * 设置指定材质是否使用使用平滑着色（高氏着色：Gouraud Shading）。
	 * @param flag 值为true时启用Gouraud Shading，否则不启用。
	 * @param materialId 指定材质的ID值
	 */
	public void setSmoothShade(boolean flag, int materialId){
		nativeSetSmoothShade(flag, materialId, getId());
	}
	
	/**
	 * 设置指定材质对环境光的颜色响应。
	 * @param color 材质对环境光的颜色响应参数
	 * @param materialId 指定材质的ID值
	 */
	public void setAmbientColor(Color4i color, int materialId) {
		nativeSetAmbientColor(color.r(), color.g(), color.b(), color.a(), materialId, getId());
	}
	
	/**
	 * 设置指定材质对漫反射光的颜色响应。
	 * @param color 材质对漫反射光的响应参数，默认值为全白（255，255，255， 255）
	 * @param materialId 指定材质的ID值
	 */
	public void setDiffuseColor(Color4i color, int materialId) {
		nativeSetDiffuseColor(color.r(), color.g(), color.b(), color.a(), materialId, getId());
	}
	
	/**
	 * 设置指定材质的发射光颜色。默认不发光。
	 * @param color 材质的发射射光参数
	 * @param materialId 指定材质的ID值
	 */
	public void setEmissiveColor(Color4i color, int materialId) {
		nativeSetEmissiveColor(color.r(), color.g(), color.b(), color.a(), materialId, getId());
	}
	
	/**
	 * 设置指定材质的对高光的颜色响应。
	 * @param color 材质的高光响应参数。默认值为全白（255，255，255）
	 * @param materialId 指定材质的ID值
	 */
	public void setSpecularColor(Color4i color, int materialId) {
		nativeSetSpecularColor(color.r(), color.g(), color.b(), color.a(), materialId, getId());
	}
	
	/**
	 * 设置指定材质光强参数，将影响材质的高光。
	 * 通用值为20。置0时则无高光。正常取值范围为[0.5,128]。
	 * @param para 材质光强参数值
	 * @param materialId 指定材质的ID值
	 */
	public void setShininess(double para, int materialId) {
		nativeSetShininess(para, materialId, getId());
	}
	
	/**
	 * 设置指定材质的贴图。
	 * @param path 贴图的路径
	 * @param materialId 指定材质的ID值
	 */
	public void setTexture(String path, int materialId) {
		nativeSetTexture(mScene.getFullPath(path), materialId, getId());
	}
	
	/**
	 * 设置指定材质的贴图为位图（该方法容易被误用，请使用{@link Scene#uploadBitmap(Bitmap, String)}
	 * 然后使用{@link #setTexture(String, int)}）
	 * @param bitmap 所使用的位图对象
	 * @param bitmapName 该位图的命名，必须唯一！
	 * @param materialId 指定材质的ID值
	 */
	@Deprecated public void setTexture(Bitmap bitmap, String bitmapName, int materialId){
		nativeSetBitmapTexture(bitmapName, bitmap, materialId, getId());
	}
	
	@Deprecated
	/**
	 * 设置指定材质贴图为视频图像
	 * @param materialId 指定材质的ID值
	 */
	public void setMediaTexture(int materialId){
		nativeSetMediaTexture(materialId, getId());
	}
	
	/**
	 * 为模型节点所有材质指定统一贴图
	 * @param path 贴图的路径
	 */
	public void setTexture(String path) {
		nativeAllSetTexture(mScene.getFullPath(path), getId());
	}
	
	/**
	 * 为模型所有节点指定统一位图贴图（该方法容易被误用，请使用{@link Scene#uploadBitmap(Bitmap, String)}
	 * 然后使用{@link #setTexture(String)}）
	 * @param bitmap 所使用的位图对象
	 * @param bitmapName 贴图的名称（必须唯一），且不可与其他图片的路径重名
	 */
	@Deprecated public void setTexture(Bitmap bitmap, String bitmapName){
		nativeAllSetBitmapTexture(bitmapName, bitmap, getId());
	}
	
	/**
	 * 为模型设定外部贴图
	 * @param name 该贴图的唯一名字，注意不要使用外部前缀
	 * @param materialId 指定材质的ID值
	 * @return 为真则设定成功
	 */
	@Deprecated
	public boolean setExternalTexture(String name, int materialId){
		if (Engine.getInstance().getRenderType() != 1){
			Log.e(TAG, "Can not set external texture. Unsupported renderer type.");
			return false;
		}
		if (nativeSetExternalTexture(name, materialId, getId()) == 0) return true;
		return false;
	}
	
	@Deprecated
	/**
	 * 为模型所有节点指定统一视频贴图
	 * @param bitmap 所使用的视频贴图对象
	 */
	public void setMediaTexture(){
		if (Engine.getInstance().getRenderType() != 1){
			Log.e(TAG, "Can not set Mediaplayer texture. Unsupported renderer type.");
			return;
		}
		nativeAllSetMediaTexture(getId());
	}
	
	/**
	 * 为模型材质设定动画贴图<br>
	 * 注意，当你添加（通常情况下请不要这么做）多个Animator时，请谨慎维护
	 * Animator的添加顺序，顺序会显著的影响每一帧的更新效果（比如，先做碰撞检测
	 * {@link #addCollisionResponseAnimator(SceneNode, boolean, boolean)}再添加直线
	 * 飞行动画{@link #addFlyStraightAnimator(zte.irrlib.core.Vector3d, 
	 * zte.irrlib.core.Vector3d, double, boolean, boolean)}，那么碰撞检测的效果
	 * 会被后续执行的直线飞行动画所覆盖，如果调换顺序，则碰撞检测的位置将是执行过飞行动画后的
	 * 位置）。如果您不需要使用多个动画，请确保节点没有被添加过动画或使用{@link SceneNode#removeAllAnimators()}
	 * 清除所有动画，动画一旦被添加，它会一直存在于节点上直到{@link SceneNode#removeAllAnimators()}被调用。
	 * @param path 所用贴图组的路径
	 * @param timePerFrame 贴图动画播放速率，单位frame/second
	 * @param loop 值为true时循环播放贴图动画，否则单次播放
	 * @return 节点动画的Id 
	 */
	public int addTextureAnimator(String[] path, int timePerFrame, boolean loop) {
		String text[] = path.clone();
		for (int i = 0; i < text.length; i++){
			text[i] = mScene.getFullPath(text[i]);
		}
		if (nativeAddTextureAnimator(text, timePerFrame, loop, getId()) == 0)
			return addAnimator();
		return -1;
	}
	
	/**
	 * 设置模型节点的包围盒是否可视
	 * @param flag 值为true时包围盒可视，否则不可见
	 */
	public void setBBoxVisibility(boolean flag){
		nativeSetBBoxVisibility(flag, getId());
	}
	
	/**
	 * 返回模型节点的材质数目
	 * @return 模型节点的材质数目
	 */
	public int getMaterialCount(){
		return nativeGetMaterialCount(getId());
	}
	
	/**
	 * 返回未变换的包围盒，就是说，即使节点是运动的，经过变换的，返回的包围盒依然是最初的那个。
	 * @return 包围盒的实例
	 */
	public BoundingBox getBoundingBox(){
		BoundingBox bbox = new BoundingBox();
		nativeGetBoundingBox(bbox, false, getId());
		return bbox;
	}
	
	/**
	 * 返回绝对的包围盒，就是说，如果节点是运动的，经过变换的，返回的包围盒也会是经过变换的。
	 * @return 包围盒的实例
	 */
	public BoundingBox getAbsoluteBoundingBox(){
		BoundingBox bbox = new BoundingBox();
		nativeGetBoundingBox(bbox, true, getId());
		return bbox;
	}
	
	/**
	 * 设置贴图渲染方法，可用于设定透明贴图
	 * @param type 渲染方法
	 */
	public void setMaterialType(E_MATERIAL_TYPE type){
		nativeSetMaterialType(type.ordinal(), getId());
	}
	
	/**
	 * 设置渲染器参数开关
	 * @param EMF 渲染器参数，是以EMF为开头的常量
	 * @param flag 如为true，则打开开关
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
	 * 材质模式的枚举类，可以用于设置贴图的渲染方式。基本是从native层搬上来的，其中比较常用的有EMT_SOLID，
	 * EMT_TRANSPARENT_ALPHA_CHANNEL，EMT_TRANSPARENT_ADD_COLOR，
	 * EMT_TRANSPARENT_ALPHA_CHANNEL_REF，使用{@link MeshSceneNode#setMaterialType(E_MATERIAL_TYPE)}
	 * 设定材质模式
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
	 * 唯一构造函数
	 */
	MeshSceneNode(Vector3d pos, SceneNode parent){
		super(pos, parent);
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
	private native int nativeSetMaterialType(int type, int Id);
	private native int nativeSetMaterialFlag(int emf, boolean flag, int Id);
}
