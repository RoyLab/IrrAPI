package zte.irrlib.scene;

import zte.irrlib.core.BoundingBox;
import zte.irrlib.core.Color4i;
import android.graphics.Bitmap;

/**
 * 多边形节点类
 * @author Fxx
 *
 */
public class MeshSceneNode extends SceneNode{
	
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
	 * @param color 材质对漫反射光的响应参数，默认值为全白（255，255，255）
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
	 * 设置指定材质的贴图为位图。
	 * @param bitmap 所使用的位图对象
	 * @param materialId 指定材质的ID值
	 */
	public void setTexture(Bitmap bitmap, int materialId){
		nativeSetBitmapTexture(bitmap.toString(), bitmap, materialId, getId());
	}
	
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
	 * 为模型所有节点指定统一位图贴图
	 * @param bitmap 所使用的位图对象
	 */
	public void setTexture(Bitmap bitmap){
		nativeAllSetBitmapTexture(bitmap.toString(), bitmap, getId());
	}
	
	/**
	 * 为模型所有节点指定统一视频贴图
	 * @param bitmap 所使用的视频贴图对象
	 */
	public void setMediaTexture(){
		nativeAllSetMediaTexture(getId());
	}
	
	/**
	 * 为模型材质设定动画贴图
	 * @param path 所用贴图组的路径
	 * @param timePerFrame 贴图动画播放速率，单位frame/second
	 * @param loop 值为true时循环播放贴图动画，否则单次播放
	 */
	public void addTextureAnimator(String[] path, int timePerFrame, boolean loop) {
		String text[] = path.clone();
		for (int i = 0; i < text.length; i++){
			text[i] = mScene.getFullPath(text[i]);
		}
		nativeAddTextureAnimator(text, timePerFrame, loop, getId());    
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
		//nativeGetBoundingBox(bbox, false, getId());
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
	 * 唯一构造函数
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
	
	private native int nativeAllSetTexture(String path, int Id);
	private native int nativeAllSetBitmapTexture(String name, Bitmap bitmap, int Id);
	private native int nativeAllSetMediaTexture(int Id);

	private native int nativeAddTextureAnimator(String[] path, int timePerFrame, boolean loop, int Id);
	private native int nativeGetMaterialCount(int Id);
	
	private native int nativeGetBoundingBox(BoundingBox box, boolean isAbsolute, int id);
}
