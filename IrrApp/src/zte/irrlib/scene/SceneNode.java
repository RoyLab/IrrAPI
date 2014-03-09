package zte.irrlib.scene;

import zte.irrlib.core.Vector3d;

/**
 * 场景节点的基本类。
 * @author Fxx
 *
 */
public class SceneNode {
	
	//节点类型
	public static final int TYPE_NULL = 0x00000000;
	public static final int TYPE_COMMON = 0x00000001;
	public static final int TYPE_MESH = 0x00000102;
	public static final int TYPE_ANIMATE_MESH = 0x00000103;
	public static final int TYPE_LIGHT = 0x00000004;
	public static final int TYPE_BILLBOARD = 0x00001005;
	public static final int TYPE_BILLBOARD_GROUP = 0x00000006;
	public static final int TYPE_CAMERA = 0x00000007;
	public static final int TYPE_PARTICLE_SYSTEM = 0x00000008;
	
	//变换方式
	public static final int TRANS_ABSOLUTE = 0;
	public static final int TRANS_RELATIVE = 1;
	
	public static final int FLAG_MATERIAL_OWNER = 0x00001000;
	
	//材质类型
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
	 * 设置场景对象。
	 * @param scene 所使用的场景对象
	 */
	public static final void setScene(Scene scene){
		mScene = scene;
	}
	
	/**
	 * 返回节点类型。
	 * @return 节点类型
	 */
	public final int getNodeType(){
		return mNodeType;
	}
	
	/**
	 * 对初始位置、旋转及大小信息进行备份。
	 */
	public void mark(){
		mPosition[1].copy(mPosition[0]);
		mRotation[1].copy(mRotation[0]);
		mScale[1].copy(mScale[0]);
	}
	
	/**
	 * 设置节点的父节点。
	 * @param node 父节点对象
	 */
	public void setParent(SceneNode node){
		mParent = node;
		nativeSetParent(mScene.getId(mParent), getId());
	}
	
	/**
	 * 设置节点是否可见。
	 * @param isVisible 值为true时节点可见，否则不可见
	 */
	public void setVisible(boolean isVisible){
		mIsVisible = isVisible;
		nativeSetVisible(isVisible, getId());
	}
	
	/**
	 * 设置节点位置坐标。
	 * @param para	节点位置坐标
	 * @param mode 坐标变换模式
	 * 		TRANS_ABSOLUTE：	依据父节点进行位置变换
	 * 		TRANS_RELATIVE：		依据模型节点自身初始位置进行变换
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
	 * 设置节点旋转角度。
	 * @param para	沿三个轴向的旋转角度值
	 * @param mode 旋转模式
	 * 		TRANS_ABSOLUTE：	依据旋转参数直接进行旋转
	 * 		TRANS_RELATIVE：		依据模型节点自身初始旋转角度进行旋转
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
	 * 设置节点的大小。
	 * @param para	沿三个轴向大小变换值
	 * @param mode 变换模式
	 * 		TRANS_ABSOLUTE：	依据节点的父节点进行大小变换
	 * 		TRANS_RELATIVE：		依据模型节点自身初始大小进行变换
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
	 * 返回节点相对于父节点的位置坐标。
	 * @return 相对于父节点的位置坐标
	 */
	public Vector3d getPosition(){
		return new Vector3d(mPosition[0]);
	}
	
	/**
	 * 返回节点的旋转角度。
	 * @return 节点的旋转角度
	 */
	public Vector3d getRotation(){
		return new Vector3d(mRotation[0]); 
	}
	
	/**
	 * 返回节点相对于父节点的大小。
	 * @return 节点相对于父节点的大小
	 */
	public Vector3d getScale(){
		return new Vector3d(mScale[0]); 
	}
	
	/**
	 * 返回节点的位置、旋转和大小的变换值。
	 * @return 节点的位置、旋转和大小的变换值
	 */
	public TransformationInfo getTransformationInfo(){
		TransformationInfo info = new TransformationInfo();
		info.Position = new Vector3d(mPosition[0]);
		info.Rotation = new Vector3d(mRotation[0]);
		info.Scale = new Vector3d(mScale[0]);
		
		return info;
	}
	
	/**
	 * 为节点设置位置、旋转和大小的变换值。
	 * @param info 节点变换信息值
	 */
	public void setTransformationInfo(TransformationInfo info){
		mPosition[0].copy(info.Position);
		mRotation[0].copy(info.Rotation);
		mScale[0].copy(info.Scale);
	}
	
	/**
	 * 添加平移动画。
	 * @param start 平移动画起始点坐标
	 * @param end 平移动画目标点坐标
	 * @param time 平移动画所用时间，单位：毫秒（ms）
	 * @param loop 值为true时动画循环进行，值为false时动画进行一次
	 * @param pingpong 值为true时节点到达目标点时会返回初始点，否则不返回
	 */
	public void addFlyStraightAnimator(Vector3d start, Vector3d end, 
			double time, boolean loop, boolean pingpong){
		nativeAddFlyStraightAnimator(start.X, start.Y, start.Z, 
				end.X, end.Y, end.Z, time, loop, pingpong, getId());
	}
	
	/**
	 * 添加环形运动动画。
	 * @param center 运动所绕圆环的圆心坐标
	 * @param radius 运动所绕圆环的半径
	 * @param speed 运动速率，单位：弧度/毫秒
	 * @param axis 运动所绕的轴，即运动平面的up vector
	 * @param startPoint 运动相对起始点。即0.5时从半圆处开始运动
	 * @param radiusEllipsoid 运动轨迹的椭圆程度。默认值为0，运动轨迹为圆。
	 */
	public void addFlyCircleAnimator(Vector3d center, double radius,
			double speed, Vector3d axis, double startPoint, double radiusEllipsoid){
		nativeAddFlyCircleAnimator(center.X, center.Y, center.Z, radius, speed, 
				axis.X, axis.Y, axis.Z, startPoint, startPoint, getId());
	}
	
	/**
	 * 添加旋转动画。
	 * @param speed 绕各个轴向的旋转速率，单位：度/10毫秒
	 */
	public void addRotationAnimator(Vector3d speed){
		nativeAddRotationAnimator(speed.X, speed.Y, speed.Z, getId());
	}
	
	/**
	 * 添加消失动画，节点将于指定时间内消失。
	 * @param ms 消失动画的时间，单位毫秒
	 */
	public void addDeleteAnimator(int ms){
		nativeAddDeleteAnimator(ms, getId());
	}
	
	/**
	 * 添加对指定节点的碰撞检测响应。
	 * @param selNode 指定的节点对象
	 */
	public void addCollisionResponseAnimator(SceneNode selNode){
		nativeAddCollisionResponseAnimator(mScene.getId(selNode),getId());
	}
	
	/**
	 * 删除节点上的所有动画
	 */
	//单个去除动画的函数实现所需代码略多，暂不管
	public void removeAllAnimator(){
		nativeRemoveAllAnimator(getId());
	}
	
	/**
	 * 将节点从场景中移除
	 */
	public void remove(){
		mScene.removeNode(this);
	}	
	
	public int setMaterialType(E_MATERIAL_TYPE type){
		return nativeSetMaterialType(type.ordinal(), getId());
	}

	/**
	 * 在Java层保存及初始化节点信息
	 * @param pos 节点的初始位置
	 * @param parent 节点的父节点
	 */
	void javaLoadDataAndInit(Vector3d pos, SceneNode parent){
		mPosition[0] = pos;
		mParent = parent;
		mark();
		mScene.registerNode(this);
	}
	
	/**
	 * 唯一构造函数
	 */
	SceneNode() {
		this.Id = mScene.getNewId();
		
		//第一个是当前值，第二个是标记值
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
	 * 返回节点的ID值
	 * @return 节点的ID值
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
