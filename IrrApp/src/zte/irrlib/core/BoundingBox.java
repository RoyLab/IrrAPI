package zte.irrlib.core;

/**
 * 包围盒类
 * @author Roy
 *
 */
public class BoundingBox {
	
	public Vector3d MinEdge, MaxEdge;
	
	public BoundingBox(){
		MinEdge = new Vector3d();
		MaxEdge = new Vector3d();
	}
	
	public BoundingBox(Vector3d init){
		MinEdge = new Vector3d(init);
		MaxEdge = new Vector3d(init);
	}
	
	public BoundingBox(Vector3d minInit, Vector3d maxInit){
		MinEdge = new Vector3d(minInit);
		MaxEdge = new Vector3d(maxInit);
	}
}
