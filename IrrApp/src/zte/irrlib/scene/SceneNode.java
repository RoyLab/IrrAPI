package zte.irrlib.scene;

import zte.irrlib.core.Vector3d;

/**
 * �����ڵ�Ļ����ࡣ
 * @author Fxx
 *
 */
public class SceneNode {
	
	//�ڵ�����
	public static final int TYPE_NULL = 0x00000000;
	public static final int TYPE_COMMON = 0x00000001;
	public static final int TYPE_MESH = 0x00000102;
	public static final int TYPE_ANIMATE_MESH = 0x00000103;
	public static final int TYPE_LIGHT = 0x00000004;
	public static final int TYPE_BILLBOARD = 0x00001005;
	public static final int TYPE_BILLBOARD_GROUP = 0x00000006;
	public static final int TYPE_CAMERA = 0x00000007;
	public static final int TYPE_PARTICLE_SYSTEM = 0x00000008;
	
	//�任��ʽ
	public static final int TRANS_ABSOLUTE = 0;
	public static final int TRANS_RELATIVE = 1;
	
	public static final int FLAG_MATERIAL_OWNER = 0x00001000;
	
	//��������
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
		
		/*
		private int nCode;
		
		private E_MATERIAL_TYPE(int val){
			this.nCode = val;
		}
		
		@Override
		public String toString(){
			return String.valueOf(this.nCode);
		}
		*/
	};
	
	/**
	 * ���ó�������
	 * @param scene ��ʹ�õĳ�������
	 */
	public static final void setScene(Scene scene){
		mScene = scene;
	}
	
	/**
	 * ���ؽڵ����͡�
	 * @return �ڵ�����
	 */
	public final int getNodeType(){
		return mNodeType;
	}
	
	/**
	 * �Գ�ʼλ�á���ת����С��Ϣ���б��ݡ�
	 */
	public void mark(){
		mPosition[1].copy(mPosition[0]);
		mRotation[1].copy(mRotation[0]);
		mScale[1].copy(mScale[0]);
	}
	
	/**
	 * ���ýڵ�ĸ��ڵ㡣
	 * @param node ���ڵ����
	 */
	public void setParent(SceneNode node){
		mParent = node;
		nativeSetParent(mScene.getId(mParent), getId());
	}
	
	/**
	 * ���ýڵ��Ƿ�ɼ���
	 * @param isVisible ֵΪtrueʱ�ڵ�ɼ������򲻿ɼ�
	 */
	public void setVisible(boolean isVisible){
		mIsVisible = isVisible;
		nativeSetVisible(isVisible, getId());
	}
	
	/**
	 * ���ýڵ�λ�����ꡣ
	 * @param para	�ڵ�λ������
	 * @param mode ����任ģʽ
	 * 		TRANS_ABSOLUTE��	���ݸ��ڵ����λ�ñ任
	 * 		TRANS_RELATIVE��		����ģ�ͽڵ������ʼλ�ý��б任
	 */
	public void setPosition(Vector3d para, int mode){
		if (mode == TRANS_ABSOLUTE){
			mPosition[0].copy(para);
		}
		else if (mode == TRANS_RELATIVE){
			mPosition[0] = mPosition[1].plus(para);
		}
		else return;
		
		nativeSetPosition(mPosition[0].X, mPosition[0].Y, mPosition[0].Z, getId());
	}
	
	/**
	 * ���ýڵ���ת�Ƕȡ�
	 * @param para	�������������ת�Ƕ�ֵ
	 * @param mode ��תģʽ
	 * 		TRANS_ABSOLUTE��	������ת����ֱ�ӽ�����ת
	 * 		TRANS_RELATIVE��		����ģ�ͽڵ������ʼ��ת�ǶȽ�����ת
	 */
	public void setRotation(Vector3d para, int mode){
		if (mode == TRANS_ABSOLUTE){
			mRotation[0].copy(para);
		}
		else if (mode == TRANS_RELATIVE){
			mRotation[0] = mRotation[1].plus(para);
		}
		else return;
		
		nativeSetRotation(mRotation[0].X, mRotation[0].Y, mRotation[0].Z, getId());
	}
	
	/**
	 * ���ýڵ�Ĵ�С��
	 * @param para	�����������С�任ֵ
	 * @param mode �任ģʽ
	 * 		TRANS_ABSOLUTE��	���ݽڵ�ĸ��ڵ���д�С�任
	 * 		TRANS_RELATIVE��		����ģ�ͽڵ������ʼ��С���б任
	 */
	public void setScale(Vector3d para, int mode){
		if (mode == TRANS_ABSOLUTE){
			mScale[0].copy(para);
		}
		else if (mode == TRANS_RELATIVE){
			mScale[0] = mScale[1].multi(para);
		}
		else return;
		
		nativeSetScale(mScale[0].X, mScale[0].Y, mScale[0].Z, getId());
	}
	
	/**
	 * ���ؽڵ�����ڸ��ڵ��λ�����ꡣ
	 * @return ����ڸ��ڵ��λ������
	 */
	public Vector3d getPosition(){
		return new Vector3d(mPosition[0]);
	}
	
	/**
	 * ���ؽڵ����ת�Ƕȡ�
	 * @return �ڵ����ת�Ƕ�
	 */
	public Vector3d getRotation(){
		return new Vector3d(mRotation[0]); 
	}
	
	/**
	 * ���ؽڵ�����ڸ��ڵ�Ĵ�С��
	 * @return �ڵ�����ڸ��ڵ�Ĵ�С
	 */
	public Vector3d getScale(){
		return new Vector3d(mScale[0]); 
	}
	
	/**
	 * ���ؽڵ��λ�á���ת�ʹ�С�ı任ֵ��
	 * @return �ڵ��λ�á���ת�ʹ�С�ı任ֵ
	 */
	public TransformationInfo getTransformationInfo(){
		TransformationInfo info = new TransformationInfo();
		info.Position = new Vector3d(mPosition[0]);
		info.Rotation = new Vector3d(mRotation[0]);
		info.Scale = new Vector3d(mScale[0]);
		
		return info;
	}
	
	/**
	 * Ϊ�ڵ�����λ�á���ת�ʹ�С�ı任ֵ��
	 * @param info �ڵ�任��Ϣֵ
	 */
	public void setTransformationInfo(TransformationInfo info){
		mPosition[0].copy(info.Position);
		mRotation[0].copy(info.Rotation);
		mScale[0].copy(info.Scale);
	}
	
	/**
	 * ���ƽ�ƶ�����
	 * @param start ƽ�ƶ�����ʼ������
	 * @param end ƽ�ƶ���Ŀ�������
	 * @param time ƽ�ƶ�������ʱ�䣬��λ�����루ms��
	 * @param loop ֵΪtrueʱ����ѭ�����У�ֵΪfalseʱ��������һ��
	 * @param pingpong ֵΪtrueʱ�ڵ㵽��Ŀ���ʱ�᷵�س�ʼ�㣬���򲻷���
	 */
	public void addFlyStraightAnimator(Vector3d start, Vector3d end, 
			double time, boolean loop, boolean pingpong){
		nativeAddFlyStraightAnimator(start.X, start.Y, start.Z, 
				end.X, end.Y, end.Z, time, loop, pingpong, getId());
	}
	
	/**
	 * ��ӻ����˶�������
	 * @param center �˶�����Բ����Բ������
	 * @param radius �˶�����Բ���İ뾶
	 * @param speed �˶����ʣ���λ������/����
	 * @param axis �˶����Ƶ��ᣬ���˶�ƽ���up vector
	 * @param startPoint �˶������ʼ�㡣��0.5ʱ�Ӱ�Բ����ʼ�˶�
	 * @param radiusEllipsoid �˶��켣����Բ�̶ȡ�Ĭ��ֵΪ0���˶��켣ΪԲ��
	 */
	public void addFlyCircleAnimator(Vector3d center, double radius,
			double speed, Vector3d axis, double startPoint, double radiusEllipsoid){
		nativeAddFlyCircleAnimator(center.X, center.Y, center.Z, radius, speed, 
				axis.X, axis.Y, axis.Z, startPoint, startPoint, getId());
	}
	
	/**
	 * �����ת������
	 * @param speed �Ƹ����������ת���ʣ���λ����/10����
	 */
	public void addRotationAnimator(Vector3d speed){
		nativeAddRotationAnimator(speed.X, speed.Y, speed.Z, getId());
	}
	
	/**
	 * �����ʧ�������ڵ㽫��ָ��ʱ������ʧ��
	 * @param ms ��ʧ������ʱ�䣬��λ����
	 */
	public void addDeleteAnimator(int ms){
		nativeAddDeleteAnimator(ms, getId());
	}
	
	/**
	 * ��Ӷ�ָ���ڵ����ײ�����Ӧ��
	 * @param selNode ָ���Ľڵ����
	 */
	public void addCollisionResponseAnimator(SceneNode selNode){
		nativeAddCollisionResponseAnimator(mScene.getId(selNode),getId());
	}
	
	/**
	 * ɾ���ڵ��ϵ����ж���
	 */
	//����ȥ�������ĺ���ʵ����������Զ࣬�ݲ���
	public void removeAllAnimator(){
		nativeRemoveAllAnimator(getId());
	}
	
	/**
	 * ���ڵ�ӳ������Ƴ�
	 */
	public void remove(){
		mScene.removeNode(this);
	}	
	
	public int setMaterialType(E_MATERIAL_TYPE type){
		return nativeSetMaterialType(type.ordinal(), getId());
	}

	/**
	 * ��Java�㱣�漰��ʼ���ڵ���Ϣ
	 * @param pos �ڵ�ĳ�ʼλ��
	 * @param parent �ڵ�ĸ��ڵ�
	 */
	void javaLoadDataAndInit(Vector3d pos, SceneNode parent){
		mPosition[0] = pos;
		mParent = parent;
		mark();
		mScene.registerNode(this);
	}
	
	/**
	 * Ψһ���캯��
	 */
	SceneNode() {
		this.Id = mScene.getNewId();
		
		//��һ���ǵ�ǰֵ���ڶ����Ǳ��ֵ
		mPosition = new Vector3d[2];
		mPosition[0] = new Vector3d(0, 0, 0);
		mPosition[1] = new Vector3d();
		
		mRotation = new Vector3d[2];
		mRotation[0] = new Vector3d(0, 0, 0);
		mRotation[1] = new Vector3d();
		
		mScale = new Vector3d[2];
		mScale[0] = new Vector3d(1, 1, 1);
		mScale[1] = new Vector3d();
		
		mNodeType = TYPE_COMMON;
	}
	
	/**
	 * ���ؽڵ��IDֵ
	 * @return �ڵ��IDֵ
	 */
	int getId() {return Id;}
	
	protected static Scene mScene;
	
	protected final int Id;
	
	protected SceneNode mParent = null;
	protected boolean mIsVisible = true;
	protected Vector3d []mPosition;
	protected Vector3d []mRotation;
	protected Vector3d []mScale;
	
	protected int mNodeType;
	
	//! transform native method.
	private native int nativeSetParent(int parent, int Id);
	private native int nativeSetVisible(boolean isVisible, int Id);
	private native int nativeSetRotation(double x, double y, double z, int Id);
	private native int nativeSetScale(double x, double y, double z, int Id);
	private native int nativeSetPosition(double x, double y, double z, int Id);
	private native int nativeSetMaterialType(int type, int Id);
	
	//! animator native method.
	private native int nativeAddRotationAnimator(
			double x, double y, double z, int Id);
			
	private native int nativeAddFlyCircleAnimator(
			double cx, double cy, double cz, double radius, double speed, 
			double ax, double ay, double az, double startPosition, 
			double radiusEllipsoid, int Id);
			
	private native int nativeAddFlyStraightAnimator(double sx, double sy, double sz,
			double dx, double dy, double dz, double time, 
			boolean loop, boolean pingpong, int Id);
			
	private native int nativeAddDeleteAnimator(int ms, int Id);
	private native int nativeRemoveAllAnimator(int Id);
	private native int nativeAddCollisionResponseAnimator(int selId, int Id);
	
	public class TransformationInfo{
		public Vector3d Position;
		public Vector3d Rotation;
		public Vector3d Scale;
	}
}
