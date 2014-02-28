package zte.irrlib.scene;

import zte.irrlib.core.Color4i;
import android.graphics.Bitmap;

public class MeshSceneNode extends SceneNode{

	MeshSceneNode(){
		super();
		mNodeType = TYPE_MESH;
	}
	
	public void enableLighting(boolean flag){
		nativeEnableLighting(flag, getId());
	}
	
	public void setTouchable(boolean flag){
		nativeSetTouchable(flag, getId());
	}
	
	public void setSmoothShade(boolean flag, int materialId){
		nativeSetSmoothShade(flag, materialId, getId());
	}
	
	public void setAmbientColor(Color4i color, int materialId) {
		nativeSetAmbientColor(color.r(), color.g(), color.b(), color.a(), materialId, getId());
	}

	public void setDiffuseColor(Color4i color, int materialId) {
		nativeSetDiffuseColor(color.r(), color.g(), color.b(), color.a(), materialId, getId());
	}

	public void setEmissiveColor(Color4i color, int materialId) {
		nativeSetEmissiveColor(color.r(), color.g(), color.b(), color.a(), materialId, getId());
	}

	public void setSpecularColor(Color4i color, int materialId) {
		nativeSetSpecularColor(color.r(), color.g(), color.b(), color.a(), materialId, getId());
	}

	public void setShininess(double para, int materialId) {
		nativeSetShininess(para, materialId, getId());
	}

	public void setTexture(String path, int materialId) {
		nativeSetTexture(mScene.getFullPath(path), materialId, getId());
	}
	
	public void setTexture(Bitmap bitmap, int materialId){
		nativeSetBitmapTexture(bitmap.toString(), bitmap, materialId, getId());
	}
	
	public void setMediaTexture(int materialId){
		nativeSetMediaTexture(materialId, getId());
	}

	public void setTexture(String path) {
		nativeAllSetTexture(mScene.getFullPath(path), getId());
	}
	
	public void setTexture(Bitmap bitmap){
		nativeAllSetBitmapTexture(bitmap.toString(), bitmap, getId());
	}
	
	public void setMediaTexture(){
		nativeAllSetMediaTexture(getId());
	}
	
	public void addTextureAnimator(String[] path, int timePerFrame, boolean loop) {
		String text[] = path.clone();
		for (int i = 0; i < text.length; i++){
			text[i] = mScene.getFullPath(text[i]);
		}
		nativeAddTextureAnimator(text, timePerFrame, loop, getId());
	}
	
	public void setBBoxVisibility(boolean flag){
		nativeSetBBoxVisibility(flag, getId());
	}
	
	public int getMaterialCount(){
		return nativeGetMaterialCount(getId());
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
}
