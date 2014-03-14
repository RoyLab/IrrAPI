package zte.irrlib.scene;

import zte.irrlib.core.BoundingBox;
import zte.irrlib.core.Color4i;
import android.graphics.Bitmap;

/**
 * ����νڵ���
 * @author Fxx
 *
 */
public class MeshSceneNode extends SceneNode{
	
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
	 * ����ָ�����ʵ���ͼΪλͼ��
	 * @param bitmap ��ʹ�õ�λͼ����
	 * @param materialId ָ�����ʵ�IDֵ
	 */
	public void setTexture(Bitmap bitmap, int materialId){
		nativeSetBitmapTexture(bitmap.toString(), bitmap, materialId, getId());
	}
	
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
	 * Ϊģ�����нڵ�ָ��ͳһλͼ��ͼ
	 * @param bitmap ��ʹ�õ�λͼ����
	 */
	public void setTexture(Bitmap bitmap){
		nativeAllSetBitmapTexture(bitmap.toString(), bitmap, getId());
	}
	
	/**
	 * Ϊģ�����нڵ�ָ��ͳһ��Ƶ��ͼ
	 * @param bitmap ��ʹ�õ���Ƶ��ͼ����
	 */
	public void setMediaTexture(){
		nativeAllSetMediaTexture(getId());
	}
	
	/**
	 * Ϊģ�Ͳ����趨������ͼ
	 * @param path ������ͼ���·��
	 * @param timePerFrame ��ͼ�����������ʣ���λframe/second
	 * @param loop ֵΪtrueʱѭ��������ͼ���������򵥴β���
	 */
	public void addTextureAnimator(String[] path, int timePerFrame, boolean loop) {
		String text[] = path.clone();
		for (int i = 0; i < text.length; i++){
			text[i] = mScene.getFullPath(text[i]);
		}
		nativeAddTextureAnimator(text, timePerFrame, loop, getId());    
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
		//nativeGetBoundingBox(bbox, false, getId());
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
	
	private native int nativeAllSetTexture(String path, int Id);
	private native int nativeAllSetBitmapTexture(String name, Bitmap bitmap, int Id);
	private native int nativeAllSetMediaTexture(int Id);

	private native int nativeAddTextureAnimator(String[] path, int timePerFrame, boolean loop, int Id);
	private native int nativeGetMaterialCount(int Id);
	
	private native int nativeGetBoundingBox(BoundingBox box, boolean isAbsolute, int id);
}
